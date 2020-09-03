MySQL配置主从复制
1. 主机my.cnf配置文件修改:
   ```mysql
   # 主服务器唯一ID
   server_id=21
   # 启动二进制日志
   log_bin=mysql-bin
   # 设置不要复制的数据库(可设置多个)
   binlog-ignore-db=information_schema
   binlog-ignore-db=mysql
   binlog-ignore-db=performance_schema
   binlog-ignore-db=sys
   # 设置需要复制的数据库,如果不配置默认为全部数据库
   binlog-do-db=test_db
   # 设置logbin格式
   binlog_format=STATEMENT # now()
                 ROW       # 
                 MIXED     # @@hostname获取当前主机名称
   # 不区分大小写
   lower_case_table_names=1
   # bin-log日志保存天数
   expire-logs-days=30
   ```
   
   修改完配置文件后需要重启MySQL,并查看主库状态:
   
    `SHOW MASTER STATUS`
   
   `SHOW VARIABLES LIKE 'log_bin';`
   
   `SHOW VARIABLES LIKE 'server_id';`
   
2. 从机my.cnf配置文件修改:

   ```mysql
   # 从机服务器唯一ID
   server_id=22
   # 启动二进制日志
   log_bin=mysql-bin
   # 指定中继日志的位置和命名
   #relay_log=/var/lib/mysql/mysql-relay-bin
   relay-log=mysql-relay
   # 从库通过I0线程读取主库二进制日志文件通过SQL线程写入的数据,是不记录binlog日志
   # 允许备库将其重放的事件也记录到自身的二进制日志中
   log-slave_updates=1
   read_only=1
   # 不区分大小写
   lower_case_table_names=1
   
   ##### 以下为可选配置 #####
   # 表示自增长字段每次递增的量,指自增字段的起始值默认为1,取值范围1...65535
   auto-increment-increment=2
   # 表示自增字段从哪个数开始,指字段一次递增多少,取值范围1...65535
   auto-increment-offset=1
   ```

3. 主机、从机重启MySQL服务(NOTE: 关闭防火墙)
   `systemctl restart mysqld.service`

4. 在主机上创建复制账号

   ```mysql
   # 监控管理复制的账号需要REPLICATION CLIENT权限(方便交换主备库角色)
   #GRANT REPLICATION SLAVE,REPLICATION CLIENT ON *.* TO slave@'192.168.0.%' IDENTIFIED BY '123456';
GRANT REPLICATION SLAVE,REPLICATION CLIENT ON *.* TO slave@'%' IDENTIFIED BY '123456';
   
   FLUSH PRIVILEGES;
   ```
   
5. 在从机上设置主机

   ```mysql
   # 在从机上测试链接主机
   mysql -h 192.168.100.21 -uroot -p
   
   CHANGE MASTER TO MASTER_HOST='192.168.100.21',
   MASTER_USER='slave',
   MASTER_PASSWORD='123456',
   MASTER_LOG_FILE='mysql-bin.000001',
   MASTER_LOG_POS=602;
   ```
   
6. 启动复制
   在从机上执行命令: `start slave;`
   
   ```bash
   # 在从机上执行命令
   start slave;
   show slave status\G;
   
   # 如果看到以下结果表明主从搭建成功
   ...
   Slave_IO_Running: Yes
   Slave_SQL_Running: Yes
   ...
   ```
   
7. 相关命令

   ```mysql
   # 查看主机状态
   show master status\G;
   # 查看从机状态
   show slave status\G;
   
   # 开启从机复制
   start slave
   # 停止从机复制
   stop slave;
   
   # 查看复制线程
   show processlist;
   
   # 重置
   reset master;
   reset slave all;
   ```

8. 验证读写分离

   ```
   # 修改Linux系统的主机名(重启失效)
   hostname 192.168.100.21
   
   INSERT INTO mytbl VALUES (1,@@hostname);
   ```

9. 从库:断开主从

   ```
   stop slave io_thread
   stop slave sql_thread
   ```

10. 主库:断开主从

    ```
    drop user ${user}@${slave_ip}
    ```

11. 配置好错误号机制

    ```
    # 一般我们可以像下面这样，在my.cnf中的[MySQLd]的启动参数中添加如下内容
    --slave-skip-errors=1062,1053  
    --slave-skip-errors=all  
    --slave-skip-errors=ddl_exist_errors
    
    # 通过如下语句查看当前MySQL配置的变量
    MySQL> show variables like 'slave_skip%';  
    
    # 通过如下命令可以查看到出现的errorno
    show slave status; # 观察Last_Errno
    
    # 常见的errorno
    1007：数据库已存在，创建数据库失败
    1008：数据库不存在，删除数据库失败
    1050：数据表已存在，创建数据表失败
    1051：数据表不存在，删除数据表失败
    1054：字段不存在，或程序文件跟数据库有冲突
    1060：字段重复，导致无法插入
    1061：重复键名
    1068：定义了多个主键
    1094：位置线程ID
    1146：数据表缺失，请恢复数据库
    1053：复制过程中主服务器宕机
    1062：主键冲突 Duplicate entry '%s' for key %d
    ```

    