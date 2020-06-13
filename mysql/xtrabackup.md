[TOC]

###### 1. 简介 ######

​	Xtrabackup中主要包含两个工具:

 	1. xtrabackup:是用来备份InnoDB表的,不能备份非 InnoDB 表,和MySQL Server没有交互
 	2. innobackupex:用来备份非 InnoDB 表,同时会调用xtrabackup命令来备份InnoDB表,还会和MySQL Server 发送命令进行交互,如加全局读锁(FTWRL)、获取位点(SHOW SLAVE STATUS)等。即innobackupex是在xtrabackup之上做了一层封装实现的。**还有就是myisam不支持增量备份**

   官网: <https://www.percona.com>

   手册:<https://www.percona.com/doc/percona-xtrabackup/LATEST/index.html>

​	下载地址: <https://www.percona.com/downloads/XtraBackup/Percona-XtraBackup-2.4.9/binary/redhat/7/x86_64/percona-xtrabackup-24-2.4.9-1.el7.x86_64.rpm>

​                      <https://www.percona.com/downloads/XtraBackup/LATEST/>

 ###### 2. 安装 ######

```bash
# 先安装需要的依赖
yum install -y rsync
yum install -y perl-DBI
yum install -y perl perl-devel libaio libaio-devel perl-Time-HiRes perl-DBD-MySQL
yum install -y perl-Digest-MD5

cd /usr/local/src/
rpm -ivh libev-4.15-7.el7.x86_64.rpm

# 开始安装
rpm -ivh percona-xtrabackup-24-2.4.9-1.el7.x86_64.rpm

# 查看版本
xtrabackup -version
# 查看安装路径
rpm -ql percona-xtrabackup-24
rpm -qa | grep xtrabackup
```

###### 3. 备份 ######

命令:`innobackupex [option] BACKUP-ROOT-DIR`

选项说明:

```
--defaults-file 指定从哪个文件读取MySQL配置,必须放在命令行第一个选项位置
--socket        指定连接的socket文件
--host          指定备份数据库的地址
--user          指定备份账号
--password      指定密码
--databases     指定数据库名，如果要指定多个数据库，彼此间需要以空格隔开；如："xtra_test dba_test"，同时，在指定某数据库时，也可以只指定其中的某张表。如："mydatabase.mytable"。该选项对innodb引擎表无效，还是会备份所有innodb表
--include=name  指定表名,格式:databasename.tablename
--incremental   指定创建一个增量备份，需要指定--incremental-basedir
--incremental-basedir 指定为前一次全备份或增量备份的目录,与--incremental同时使用
--incremental-dir     指定还原时增量备份的目录
```

###### 4. Prepare ######

命令: `innobackupex --apply-log [option] BACKUP-DIR`

选项说明:

```
--apply-log 一般情况下,在备份完成后，数据尚且不能用于恢复操作，因为备份的数据中可能会包含尚未提交的事务或已经提交但尚未同步至数据文件中的事务。因此，此时数据文件仍处理不一致状态。此选项作用是通过回滚未提交的事务及同步已经提交的事务至数据文件使数据文件处于一致性状态( 最后一次时使用，中间不用）
--use-memory 和--apply-log选项一起使用,当prepare备份时,做crash recovery分配的内存大小,单位字节,也可1MB,1M,1G,1GB等,推荐1G
--export     表示开启可导出单独的表之后再导入其他Mysql中
--redo-only  此选项在prepare base full backup,往其中合并增量备份时候使用,但不包括对最后一个增量备份的合并(中间使用，最后一次不用)
```

###### 5. 还原 ######

命令: `innobackupex --copy-back [选项] BACKUP-DIR`

选项说明:

```
--copy-back 做数据恢复时将备份数据文件拷贝到MySQL服务器的datadir
--move-back 这个选项与--copy-back相似，唯一的区别是它不拷贝文件，而是移动文件到目的地。这个选项移除backup文件，用时候必须小心。使用场景：没有足够的磁盘空间同时保留数据文件和Backup副本

```

还原注意事项:

1. datadir目录必须为空。除非指定innobackupex --force-non-empty-directorires选项指定,否则--copy-backup选项不会覆盖
2. 在restore之前,必须shutdown MySQL实例,不能将一个运行中的实例restore到datadir目录中
3. 由于文件属性会被保留,大部分情况下需要在启动实例之前将文件的属主改为mysql,这些文件将属于创建备份的用户
   chown -R mysql:mysql /data/mysql
   以上需要在用户调用innobackupex之前完成
4. --force-non-empty-directories:指定该参数时候,使得innobackupex --copy-back或--move-back选项转移文件到非空目录,已存在的文件不会被覆盖。如果--copy-back和--move-back文件需要从备份目录拷贝一个在datadir已经存在的文件,会报错失败

###### 6. 备份生成的相关文件  ######

1. xtrabackup_info:innobackupex工具执行时的相关信息,包括版本,备份选项,备份时长,备份LSN(log sequence number日志序列号),BINLOG的位置
2. xtrabackup_checkpoints:备份类型(如完全或增量)、备份状态(如是否已经为prepared状态)和LSN范围信息,每个InnoDB页(通常为16k大小)都会包含一个日志序列号LSN。LSN是整个数据库系统的系统版本号,每个页面相关的LSN能够表明此页面最近是如何发生改变的
3. xtrabackup_binlog_info:MySQL服务器当前正在使用的二进制日志文件及至备份这一刻为止二进制日志事件的位置,可利用实现基于binlog的恢复
4. backup-my.cnf:备份命令用到的配置选项信息
5. xtrabackup_logfile:备份生成的日志文件

###### 7. 备份与恢复(旧版) ######

注意:备份时需启动MySQL,恢复时需关闭MySQL,清空mysql数据目录且不能重新初始化,恢复数据后应该立即进行一次完全备份

```bash
# 创建备份账户
create user 'backup'@'%' identified by 'Server2008';
grant process,reload,lock tables,replication client,create tablespace,super  on *.* to 'backup'@'%';
FLUSH PRIVILEGES;

### 全量备份 ###
innobackupex --defaults-file=/usr/local/mysql/conf/my.cnf --host=127.0.0.1 --user=backup --password=Server2008 /tmp/backup/

### 恢复全备 ###
systemctl stop mysqld.service
mv data data_bak
mkdir data
innobackupex --apply-log /tmp/backup/2020-06-12_23-20-32/
innobackupex --defaults-file=/usr/local/mysql/conf/my.cnf --copy-back --rsync /tmp/backup/2020-06-12_23-20-32/
chown -R mysql.mysql data
systemctl start mysqld.service

### 增量备份 ###
# 在进行增量备份时,首先要进行一次全量备份,第一次增量备份是基于全备的,之后的增量备份是基于上一次的增量备份,以此类推
# 1.先进行一次全备
innobackupex --defaults-file=/usr/local/mysql/conf/my.cnf --host=127.0.0.1 --user=backup --password=Server2008 /tmp/backup/full
# 2.创建增量备份
innobackupex --defaults-file=/usr/local/mysql/conf/my.cnf --host=127.0.0.1 --user=backup --password=Server2008 --incremental /tmp/backup/incremental/ --incremental-basedir=/tmp/backup/full/2020-06-12_23-59-57 --parallel=2

### 恢复增备 ###
# 1.恢复完全备份
# 2.恢复增量备份到完全备份（开始恢复的增量备份要添加--redo-only参数，到最后一次增量备份去掉--redo-only参数）
# 3.对整体的完全备份进行恢复，回滚那些未提交的数据

# 恢复完全备份(注意这里一定要加--redo-only参数,该参数的意思是只应用xtrabackup日志中已提交的事务数据,不回滚还未提交的数据)
innobackupex --apply-log --redo-only /tmp/backup/full/2020-06-12_23-59-57/
# 将增量备份1应用到完全备份
innobackupex --apply-log --redo-only /tmp/backup/full/2020-06-12_23-59-57 --incremental-dir=/tmp/backup/incremental/2020-06-13_00-03-57
# 将增量备份2应用到完全备份(注意恢复最后一个增量备份时需要去掉--redo-only参数,回滚xtrabackup日志中那些还未提交的数据)
innobackupex --apply-log /tmp/backup/full/2020-06-12_23-59-57 --incremental-dir=/tmp/backup/incremental/2020-06-13_00-10-39
# 把所有合在一起的完全备份整体进行一次apply操作,回滚未提交的数据
innobackupex --apply-log /tmp/backup/full/2020-06-12_23-59-57/
# 把恢复完的备份复制到数据库目录文件中,赋权,然后启动mysql数据库,检测数据正确性
systemctl status mysqld.service
mv data data_bak
mkdir data
innobackupex --defaults-file=/usr/local/mysql/conf/my.cnf --copy-back --rsync /tmp/backup/full/2020-06-12_23-59-57/
chown -R mysql.mysql data
systemctl start mysqld.service
```

###### 8. 备份与恢复(新版) ######

