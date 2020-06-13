* wait_timeout默认值8小时

  > MySQL 5.7 或更新版本，可以在每次执行一个比较大的操作后，通过执行 mysql_reset_connection 来重新初始化连接资源。这个过程不需要重连和重新做权限验证，但是会将连接恢复到刚刚创建完时的状态。

* WAL(Write-Ahead Logging)

* Redo Log与Bin Log的差别

  1. redo log 是 InnoDB 引擎特有的；binlog 是 MySQL 的 Server 层实现的，所有引擎都可以使用
  2. redo log 是物理日志，记录的是“在某个数据页上做了什么修改”；binlog 是逻辑日志，记录的是这个语句的原始逻辑，比如“给 ID=2 这一行的 c 字段加 1 ”
  3. redo log 是循环写的，空间固定会用完；binlog 是可以追加写入的。“追加写”是指 binlog 文件写到一定大小后会切换到下一个，并不会覆盖以前的日志

* 一致性视图

  `show variables like 'transaction_isolation';`

  1. 在“可重复读”隔离级别下，这个视图是在事务启动时创建的，整个事务存在期间都用这个视图
  2. 在“读提交”隔离级别下，这个视图是在每个 SQL 语句开始执行的时候创建的
  3. 在“读未提交”隔离级别下直接返回记录上的最新值，没有视图概念
  4. 在“串行化”隔离级别下直接用加锁的方式来避免并行访问

* commit work and chain语法

  在 autocommit 为 1 的情况下，用 begin 显式启动的事务，如果执行 commit 则提交事务。如果执行 commit work and chain，则是提交事务并自动启动下一个事务，这样也省去了再次执行 begin 语句的开销。同时带来的好处是从程序开发的角度明确地知道每个语句是否处于事务中

  你可以在 information_schema 库的 innodb_trx 这个表中查询长事务，比如下面这个语句，用于查找持续时间超过 60s 的事务

  `select * from information_schema.innodb_trx where TIME_TO_SEC(timediff(now(),trx_started)) > 60`

* 重建索引

  重建聚集索引、非聚集索引可以使用如下语句(MySQL会自动完成转存数据、交换表名、删除旧表的操作):

  `ALTER TABLE T ENGINE=InnoDB`

  从MySQL 5.6版本开始,alter table t engine = InnoDB就是recreate

  analyze table t其实不是重建表,只是对表的索引信息做重新统计,没有修改数据,这个过程中加了MDL读锁

  optimize table t等于recreate + analyze

* 锁

  加锁规则里面，包含了两个“原则”、两个“优化”和一个“bug”:

  原则1：加锁的基本单位是 next-key lock前开后闭区间

  原则2：查找过程中访问到的对象才会加锁

  优化1：索引上的等值查询，给唯一索引加锁的时候，next-key lock 退化为行锁

  优化2：索引上的等值查询，向右遍历时且最后一个值不满足等值条件的时候，next-key lock 退化为间隙锁

  一个bug：唯一索引上的范围查询会访问到不满足条件的第一个值为止

  lock in share mode只锁覆盖索引，但是如果是for update就不一样了。 执行for update时，系统会认为你接下来要更新数据，因此会顺便给主键索引上满足条件的行加上行锁。

  1. 全局锁

     ```mysql
     # 断开连接会自动释放全局锁
     FLUSH TABLES WITH READ LOCK
     # 不推荐
     SET GLOBAL READONLY=true 
     
     # 释放锁
     unlock tables
     ```

  2. 表锁

     a. 表锁

     ```mysql
     # 断开连接会自动释放全局锁
     LOCK TABLES tbl_name READ
     LOCK TABLES tbl_name WRITE
     
     UNLOCK TABLES
     ```

     b. 元数据锁(Meta Data Lock,MDL)

     ​	MySQL5.5版本引入。事务中的 MDL 锁，在语句执行开始时申请，但是语句结束后并不会马上释放，而会等到整个事务提交后再释放。

  3. 行锁

     a. 在 InnoDB 事务中，行锁是在需要的时候才加上的，但并不是不需要了就立刻释放，而是要等到事务结束时才释放。这个就是两阶段锁协议。

  4. Next-Key Lock(RR隔离级别下才出现)

     间隙锁和行锁合称为Next-Key Lock,每个Next-Key Lock是前开后闭的区间

  5. 死锁

     ```mysql
     # 开启死锁检测
     innodb_deadlock_detect
     # 死锁检测超时设置,默认值是50s
     innodb_lock_wait_timeout 
     ```

* SQL执行顺序

  ```mysql
  # 手写
  SELECT DISTINCT <select_list>
  FROM  <left_table> <join_type>
  JOIN  <right_table> ON <join_condition>
  WHERE  <where_condition>
  GROUP BY  <group_by_list>
  HAVING <having_condition>
  ORDER BY <order_by_condition>
  LIMIT <limit_number>
  
  # 机读
  FROM  <left_table>
  ON <join_condition>
  <join_type> JOIN  <right_table>
  WHERE  <where_condition>
  GROUP BY  <group_by_list>
  HAVING <having_condition>
  SELECT
  DISTINCT <select_list>
  ORDER BY <order_by_condition>
  LIMIT <limit_number>
  ```

* 性能瓶颈定位

  ```mysql
  mysql> show status ——显示状态信息（扩展show status like ‘XXX’）
  mysql> show variables ——显示系统变量（扩展show variables like ‘XXX’）
  mysql> show innodb status ——显示InnoDB存储引擎的状态
  mysql> show processlist ——查看当前SQL执行，包括执行状态、是否锁表等
  
  Shell> mysqladmin variables -u username -p password——显示系统变量
  Shell> mysqladmin extended-status -u username -p password——显示状态信息
  ```

* 日志

  1. 慢查询日志

     ```mysql
     SHOW VARIABLES LIKE '%slow_query_log%'
     
     set global slow_query_log='ON';
     set global slow_query_log_file='/usr/local/mysql/hostname-slow.log';
     set global long_query_time=2;	-- 单位:秒
     
     [mysqld]
     slow_query_log=ON
     slow_query_log_file=/usr/local/mysql/hostname-slow.log
     long_query_time=3	-- 单位:秒
     
     # 通过mysqldumpslow --help查看操作帮助信息
     # 得到返回记录集最多的10个SQL
     mysqldumpslow -s r -t 10 /usr/local/mysql/hostname-slow.log
     # 得到访问次数最多的10个SQL
     mysqldumpslow -s c -t 10 /usr/local/mysql/hostname-slow.log
     # 得到按照时间排序的前10条里面含有左连接的查询语句
     mysqldumpslow -s t -t 10 -g "left join" /usr/local/mysql/hostname-slow.log
     # 和管道配合使用
     mysqldumpslow -s r -t 10 /usr/local/mysql/hostname-slow.log | more
     ```
     
  2. binlog

     每个线程有自己 binlog cache，但是共用同一份 binlog 文件

     write:指的就是指把日志写入到文件系统的 page cache，并没有把数据持久化到磁盘，所以速度比较快

     fsync:才是将数据持久化到磁盘的操作。一般情况下，我们认为 fsync 才占磁盘的IOPS

     sync_binlog=0，表示每次提交事务都只 write，不 fsync

     sync_binlog=1，表示每次提交事务都会执行 fsync

     sync_binlog=N(N>1) ，表示每次提交事务都 write，但累积 N 个事务后才 fsync

     ```mysql
     show binlog events in 'master.000001';
     
     mysqlbinlog  -vv data/master.000001 --start-position=8900;
     
     mysqlbinlog master.000001  --start-position=2738 --stop-position=2973 | mysql -h127.0.0.1 -P13000 -u$user -p$pwd;
     ```

  3. redo log

     innodb_flush_log_at_trx_commit为0,表示每次事务提交时都只是把redo log留在redo log buffer中 

     innodb_flush_log_at_trx_commit为1,表示每次事务提交时都将redo log直接持久化到磁盘

     innodb_flush_log_at_trx_commit为 2,表示每次事务提交时都只是把redo log写到page cache

     InnoDB有一个后台线程，每隔1秒，就会把redo log buffer中的日志，调用write写到文件系统的page cache，然后调用fsync持久化到磁盘。

* Show Profile分析查询

  ```mysql
  show variables like 'profiling'; --默认是关闭，使用前需要开启
  set profiling=1;
  show profiles;
  ```

* 事务

  begin/start transaction 命令并不是一个事务的起点，在执行到它们之后的第一个操作 InnoDB 表的语句，事务才真正启动。如果你想要马上启动一个事务，可以使用 start transaction with consistent snapshot 这个命令。

  一个数据版本，对于一个事务视图来说，除了自己的更新总是可见以外，有三种情况：

  1. 版本未提交，不可见
  2. 版本已提交，但是是在视图创建后提交的，不可见
  3. 版本已提交，而且是在视图创建前提交的，可见

  NOTE:对于可重复读，查询只承认在事务启动前就已经提交完成的数据

  NOTE:对于读提交，查询只承认在语句启动前就已经提交完成的数据

* 一致性视图

  1. 在可重复读隔离级别下，只需要在事务开始的时候创建一致性视图，之后事务里的其他查询都共用这个一致性视图
  2. 在读提交隔离级别下，每一个语句执行前都会重新算出一个新的视图
  
* merge的执行流程是这样的

  1. 从磁盘读入数据页到内存（老版本的数据页）
  2. 从 change buffer 里找出这个数据页的 change buffer 记录 (可能有多个），依次应用，得到新版数据页
  3. 写 redo log。这个 redo log 包含了数据的变更和 change buffer 的变更

  到这里 merge 过程就结束了。这时候，数据页和内存中 change buffer 对应的磁盘位置都还没有修改，属于脏页，之后各自刷回自己的物理数据，就是另外一个过程了。

* my.cnf

  ```mysql
  # InnoDB的磁盘能力,平时要多关注脏页比例,不要让它经常接近75%
  innodb_io_capacity
  
  # 脏页比例:Innodb_buffer_pool_pages_dirty/Innodb_buffer_pool_pages_total
  select VARIABLE_VALUE into @a from global_status where VARIABLE_NAME = 'Innodb_buffer_pool_pages_dirty';
  select VARIABLE_VALUE into @b from global_status where VARIABLE_NAME = 'Innodb_buffer_pool_pages_total';
  select @a/@b;
  
  # 不找邻居,自己刷自己的脏页
  innodb_flush_neighbors=0
  
  # 每个InnoDB表数据存储在一个以.ibd为后缀的文件中
  innodb_file_per_table=ON
  
  # 内存临时表的大小，默认值是16M
  tmp_table_size
  
  # 0:表示采用之前MySQL 5.0版本的策略，即语句执行结束后才释放锁
  # 1:普通insert语句，自增锁在申请之后就马上释放；类似insert … select这样的批量插入数据的语句，自增锁还是要等语句结束后才被释放
  # 2:所有的申请自增主键的动作都是申请后就释放锁
  innodb_autoinc_lock_mode=2
  ```

* count效率:尽量使用 count(*)

  `count(字段)<count(主键 id)<count(1)≈count(*)`

* 确定一个排序语句是否使用了临时文件

  ```mysql
  /* 打开optimizer_trace，只对本线程有效 */
  SET optimizer_trace='enabled=on'; 
  
  /* @a保存Innodb_rows_read的初始值 */
  select VARIABLE_VALUE into @a from  performance_schema.session_status where variable_name = 'Innodb_rows_read';
  
  /* 执行语句 */
  select city, name,age from t where city='杭州' order by name limit 1000; 
  
  /* 查看OPTIMIZER_TRACE输出 */
  /* 通过查看number_of_tmp_files是否使用了临时文件 */
  /* sort_mode里面的packed_additional_fields的意思是，排序过程对字符串做了“紧凑”处理 */
  SELECT * FROM `information_schema`.`OPTIMIZER_TRACE`\G
  
  /* @b保存Innodb_rows_read的当前值 */
  select VARIABLE_VALUE into @b from performance_schema.session_status where variable_name = 'Innodb_rows_read';
  
  /* 查看查询扫描了多少行 */
  /* internal_tmp_disk_storage_engine的默认值是 InnoDB。如果使用的是InnoDB引擎的话，把数据从临时表取出来的时候会让Innodb_rows_read的值加1 */
  select @b-@a;
  ```

* 优先队列排序算法

  filesort_priority_queue_optimization这个部分的chosen=true，就表示使用了优先队列排序算法，这个过程不需要临时文件，因此对应的 number_of_tmp_files是0

* 随机排序算法

  ```mysql
  select count(*) into @C from t;
  set @Y = floor(@C * rand());
  set @sql = concat("select * from t limit ", @Y, ",1");
  prepare stmt from @sql;
  execute stmt;
  DEALLOCATE prepare stmt;
  
  # 取随机3个word值
  select count(*) into @C from t;
  set @Y1 = floor(@C * rand());
  set @Y2 = floor(@C * rand());
  set @Y3 = floor(@C * rand());
  select * from t limit @Y1，1； //在应用代码里面取Y1、Y2、Y3值，拼出SQL后执行
  select * from t limit @Y2，1；
  select * from t limit @Y3，1；
  ```

* GTID

  GTID=server_uuid:gno 一主多从切换

  ```mysql
  # 启动GTID模式
  gtid_mode=on
  enforce_gtid_consistency=on
  
  set gtid_next='aaaaaaaa-cccc-dddd-eeee-ffffffffffff:10';
  begin;
  commit;
  set gtid_next=automatic;	-- 恢复GTID的默认分配行为
  start slave;
  ```

* 等主库位点方案

  ```mysql
  # 它是在从库执行的；
  # 参数file和pos指的是主库上的文件名和位置
  # timeout可选，设置为正整数N表示这个函数最多等待N秒
  # 这个命令正常返回的结果是一个正整数M，表示从命令开始执行，到应用完file和pos表示的binlog位置，执行了多少事务;除了正常返回一个正整数M外，这条命令还会返回一些其他结果，包括：如果执行期间，备库同步线程发生异常，则返回NULL；如果等待超过N秒，就返回-1；如果刚开始执行的时候，就发现已经执行过这个位置了，则返回0
  select master_pos_wait(file, pos[, timeout]);
  
  # 数据库开启了GTID模式，对应的也有等待GTID的方案
  # 等待，直到这个库执行的事务中包含传入的gtid_set，返回0;超时返回1
   select wait_for_executed_gtid_set(gtid_set, 1);
  ```

* Kill

  ```mysql
  # 终止这个线程中正在执行的语句
  kill query+线程id
  # 断开这个线程的连接，当然如果这个线程有语句正在执行，也是要先停止正在执行的语句的
  kill connection+线程id
  ```

* MRR(Multi-Range Read优化)

  这个优化的主要目的是尽量使用顺序读盘

  ```mysql
  # 官方文档的说法，是现在的优化器策略，判断消耗的时候，会更倾向于不使用MRR，把mrr_cost_based 设置为off，就是固定使用MRR
  set optimizer_switch='mrr_cost_based=off';
  explain select * from t2 where a>=100 and a<=200\G;
  ```

* BKA(Batched Key Access)优化算法

  把表 t1的数据取出来一部分，先放到一个临时内存。这个临时内存不是别人，就是join_buffer

  我们知道join_buffer在BNL算法里的作用，是暂存驱动表的数据。但是在NLJ算法里并没有用。那么，我们

  刚好就可以复用join_buffer到BKA算法中

  ```mysql
  # 前两个参数的作用是要启用MRR。这么做的原因是，BKA算法的优化要依赖于MRR
  set optimizer_switch='mrr=on,mrr_cost_based=off,batched_key_access=on';
  ```

* 复制表

  ```mysql
  # –single-transaction的作用是，在导出数据的时候不需要对表db1.t加表锁，而是使用START TRANSACTION WITH CONSISTENT SNAPSHOT的方法
  # –add-locks设置为0，表示在输出的文件结果里，不增加"LOCK TABLES t WRITE;"
  # –no-create-info的意思是，不需要导出表结构
  # –set-gtid-purged=off 表示的是，不输出跟GTID 相关的信息
  # –result-file指定了输出文件的路径，其中client表示生成的文件是在客户端机器上的
  mysqldump -h$host -P$port -u$user --add-locks=0 --no-create-info --single-transaction  --set-gtid-purged=OFF db1 t --where="a>900" --result-file=/client_tmp/t.sql
  
  # 放到db2库里去执行
  mysql -h127.0.0.1 -P13000  -uroot db2 -e "source /client_tmp/t.sql"
  
  # 参数secure_file_priv 的可选值和作用分别是：
  # 如果设置为empty，表示不限制文件生成的位置，这是不安全的设置
  # 如果设置为一个表示路径的字符串，就要求生成的文件只能放在这个指定的目录，或者它的子目录
  # 如果设置为NULL，就表示禁止在这个MySQL实例上执行select … into outfile操作
  
  # 导出CSV文件
  select * from db1.t where a>900 into outfile '/server_tmp/t.csv';
  # 导入CSV文件
  load data infile '/server_tmp/t.csv' into table db2.t;
  
  # mysqldump提供了一个–tab参数，可以同时导出表结构定义文件和csv数据文件
  mysqldump -h$host -P$port -u$user ---single-transaction --set-gtid-purged=OFF db1 t --where="a>900" --tab=$secure_file_priv
  ```

* 变量

  ```mysql
  # 局部变量
  declare val_name varchar(32) default 'unknown';
  # 为局部变量赋值
  set val_name = 'Centi';
  # 查询局部变量
  select val_name;
  
  # 用户变量
  # 当前会话（连接）有效。注意:用户变量不需要提前声明，使用即为声明
  set @val_name = 'Lacy';
  # 查询该用户变量
  select @val_name
  
  # 会话变量
  # 由系统提供的，只在当前会话（连接）中有效# 查看所有会话变量
  show session variables;
  # 查看指定的会话变量
  select @@session.val_name;
  # 修改指定的会话变量
  set @@session.val_name=0;
  
  # 全局变量
  # 全局变量由系统提供，整个MySQL服务器内有效
  show global variables like '%char%'
  select @@global.character_set_client
  ```

* 游标

  **注意：** 在语法中，变量声明、游标声明、handler声明是必须按照先后顺序书写的，否则创建存储过程出错

  ```
  delimiter //
  create procedure f()
  begin
  declare val_id int;
      declare val_name varchar(32);
      declare val_salary double;
  
      # 声明flag标记
      declare flag boolean default true;
  
      # 声明游标
      declare emp_flag cursor for
      select id, name, salary from employee;
  
      # 使用handler句柄来解决结束循环问题
      declare continue handler for 1329 set flag = false;
  
      # 打开
      open emp_flag;
  
      # 使用循环取值
      c:loop
          fetch emp_flag into val_id, val_name, val_salary;
          # 如果标记为true则查询结果集
          if flag then
              select val_id, val_name, val_salary;
          # 如果标记为false则证明结果集查询完毕，停止死循环
          else
              leave c;
          end if;
      end loop;
  
      # 关闭
      close emp_flag;
  
      select val_id, val_name, val_salary;
  end //
  
  call f();
  ```

* 预编译

  ```mysql
  # 预编译 
  PREPARE 数据库对象名 FROM 参数名
  # 执行
  EXECUTE 数据库对象名 [USING @var_name [, @var_name] ...]
  # 通过数据库对象创建或删除表
  {DEALLOCATE|DROP} PREPARE 数据库对象名
  ```

