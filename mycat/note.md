* 官网
  <http://www.mycat.org.cn>
<https://github.com/MyCATApache>
  
* 主要功能

  1. 数据分离
  2. 数据分片
  3. 都数据源整合

* 安装

  1. 解压: `tar -zxvf Mycat-server-1.6-RELEASE-20161028204710-linux.tar.gz`

  2. 拷贝: `cp -r mycat /usr/local`

  3. `useradd mycat`
     `passwd mycat`
     `chown -R mycat.mycat /usr/local/mycat`

  4. 三个配置文件
     a. schema.xml: 定义逻辑库,表、分片节点等内容
     b. rule.xml: 定义分片规则
     c. server.xml: 定义用户以及系统相关变量,如端口等

  5. 修改配置文件
     a. server.xml文件

     ```xml
     <user name="mycat">
     	<property name="password">111111</property>
     	<property name="schemas">TESTDB</property>
     </user>
     ```

     b. schema.xml文件
     先备份: `cp schema.xml schema.xml.bak`

     ```xml
     # 删除schema节点中的所有子节点
     <schema name="TESTDB" checkSQLschema="false" sqlMaxLimit="100" dataNode="dn1">
     </schema>
     <dataNode name="dn1" dataHost="host1" database="test_db"/>
     <dataHost name="host1" maxCon="1000" minCon="10" balance="0" writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
     	<heartbeat>select user()</heartbeat>
     	<!-- can have multi write hosts -->
     	<writeHost host="hostM1" url="192.168.100.21:3306" user="root" password="Server2008">
     		<!-- can have multi read hosts -->
     		<readHost host="hostS1" url="192.168.100.22:3306" user="root" password="Server2008" />
     	</writeHost>
     </dataHost>
     
     <schema name="TESTDB" checkSQLschema="false" sqlMaxLimit="100" dataNode="dn1">
     </schema>
     <dataNode name="dn1" dataHost="host1" database="test_db"/>
     <dataHost name="host1" maxCon="1000" minCon="10" balance="0" writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
     	<heartbeat>select user()</heartbeat>
     	<!-- can have multi write hosts -->
     	<writeHost host="hostM1" url="192.168.100.21:3306" user="root" password="Server2008"></writeHost>
     	<writeHost host="hostS1" url="192.168.100.22:3306" user="root" password="Server2008"></writeHost>
     </dataHost>
     
     # 以上两种方式第一种当写挂了读不可用,第二种可以继续使用,事务内部的一切操作都会走写节点,所以都节点不要加事务
     # 强制走从:
     /*!mycat:db_type=slave*/SELECT * FROM travel;
     /*#mycat:db_type=slave*/SELECT * FROM travel;
     # 强制走写:
     /*!mycat:db_type=master*/SELECT * FROM travel;
     /*#mycat:db_type=master*/SELECT * FROM travel;
     ```

     ***schema.xml配置文件***

     * schema标签:

     ​    1. checkSQLschema: 设置为true时会去掉SQL语句中逻辑库(TESTDB)Schema

     ​    2. sqlMaxLimit: MyCat自动添加LIMIT后的指

     *  table标签:

     ​        1. name: 逻辑表名称

     ​        2. dataNode: 定义这个逻辑表所属的dataNode,多个逗号分隔

     ​        3. rule: 指定逻辑表要使用的规则名字(rule.xml)

     ​        4.  ruleRequired: 是否必须绑定分片规则

     ​        5. primaryKey: 逻辑表对应真实表的主键

     ​        6. type: 逻辑表类型(global:全局表;普通表:不指定global)

     ​        7. autoIncrement: true:指定这个表有使用自增长主键

     ​        8. subTables: 1.6以后开始支持

     ​        9. needAddLimit: 是否需要自动在每个语句后面加上LIMIT

     ​            * childTable标签: 定义E-R分片的子表

     ​                1. name: 定义子表表名

     ​                2. joinKey: 插入子表时会使用这个列的值查找父表存储的数据节点

     ​                3. parentkey: 指定与父表建立关系的列名

     ​                4. primaryKey: 同table标签所述

     ​                5. needAddLimit: 同table标签所述

     * dataNode标签: 绑定逻辑库到某个具体的databases上,一个dataNode标签就是一个独立的数据分片

     ​    1. name: 定义数据节点的名字

     ​    2. dataHost: 定义该分片属于哪个数据库实例

     ​    3. database: 具体数据库实例上的具体库

     * dataHost标签: 指定具体数据库实例、读写分离配置和心跳语句

     ​    1. name: 唯一标识dataHost标签名称

     ​    2. maxCon: 指定每个读写实例连接池最大连接

     ​    3. minCon: 指定每个读写实例连接池最小连接,初始化连接池的大小

     ​    4. balance: 0:不开启读写分离机制,所有读操作都发送到当前可用的writeHost上

     ​                        1: 全部的readHost与stand by writeHost参与SELECT语句的负载均衡,简单说当双柱双从模式(M1->S1,M2->S2,并且M1与M2互为主备),正常情况下M1,S1,S2都参与SELECT语句的负载均衡

     ​                        2: 所有读操作都随机的在writeHost、readHost上分发

     ​                        3: 所有读请求随机分发到writeHost对应的readHost执行,writeHost不负担读压力,1.4以后可以设置此值

     ​    5. writeType: 负载均衡类型

     ​                        0: 所有写操作发送到配置的第一个writeHost,第一个挂了切换到还生存的第二个writeHost,重新启动后以切换后的为准,切换记录在配置文件:dnindex.properties

     ​                        1: 所有写操作都随机的发送到配置的writeHost,1.5版本以后废弃不推荐

     ​    6. dbType: 指定后端连接的数据库类型

     ​    7. dbDriver: 指定连接后端数据库使用的Driver

     ​                       native: 这个值执行的二进制的MySQL协议,所以可以使用MySQL、MariDB、PostgreSQL(1.6)

     ​                       JDBC: 其他类型数据库,需将jar包放到mycat/lib目录下

     ​    8. switchType:  -1: 不自动切换

     ​                                 1: 自动切换

     ​                                 2: MySQL主从同步的状态决定是否切换(SHOW SLAVE STATUS)

     ​                                 3: 基于MySQL Galary Cluster的切换机制(SHOW STATUS LIKE 'wsrep%')

     ​    9. tempReadHostAvailable: writeHost下面的readHost仍旧可用

     ​    * heartbeat标签: 指定与后端数据库进行心跳检测的语句(SELECT 1 FROM DUAL)

     ​       * writeHost、readHost标签:

     ​        1. host: 标识不同实例

     ​        2. url: 后端实例连接地址

     ​        3. user: 后端实例用户名

     ​        4. password: 后端实例密码

     ​        5. weight: 权重,配置在readHost中作为读节点的权重

     ​        6. usingDecrypt: 是否对密码加密,默认为0 `java -cp Mycat-server-1.6-RELEASE.jar io.mycat.util.DecryptUtil 1:host:user:password`    1为db端加密标志

     ***server.xml配置文件***

     dml    insert,update,select,delete    0000

     ***双主双备配置***:

     ```
     # NOTE:balance="1"
     <schema name="TESTDB" checkSQLschema="false" sqlMaxLimit="100" dataNode="dn1">
         <!-- 配置需要分片的表 -->
     </schema>
     <dataNode name="dn1" dataHost="host1" database="test_db"/>
     <dataHost name="host1" maxCon="1000" minCon="10" balance="1" writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
     	<heartbeat>select user()</heartbeat>
     	<!-- can have multi write hosts -->
     	<writeHost host="hostM1" url="192.168.100.21:3306" user="root" password="Server2008">
     		<!-- can have multi read hosts -->
     		<readHost host="hostS1" url="192.168.100.22:3306" user="root" password="Server2008" />
     	</writeHost>
     	<writeHost host="hostM2" url="192.168.100.23:3306" user="root" password="Server2008">
     		<!-- can have multi read hosts -->
     		<readHost host="hostS2" url="192.168.100.24:3306" user="root" password="Server2008" />
     	</writeHost>
     </dataHost>
     ```

* 启动
  1. 验证数据库是否启动:

     ```mysql
     mysql -h192.168.100.21 -P 3306 -uroot -pServer2008
     mysql -h192.168.100.22 -P 3306 -uroot -pServer2008
     ```

  2. 启动MyCat

     ```
     cd mycat/bin
     
     # MyCat支持命令
     ./mycat console|start|stop|restart|staus|dump
     
     显示如下信息表明启动成功:
     jvm 1    | MyCAT Server startup successfully. see logs in logs/mycat.log
     
     # 后台启动
     ./mycat start
     ```

* 登录

     ````mysql
     # 登录后台管理窗口(9066管理窗口端口号)
     mysql -umycat -p111111 -h192.168.100.21 -P9066
     
     # 登录数据窗口(8066数据窗口端口号)
     mysql -umycat -p111111 -h192.168.100.21 -P8066
     
     use TESTDB;
     show tables;
     ````

* 白名单与黑名单

     在server.xml中配置

     ```xml
     <firewall>
         <whitehost>
             <!-- IP白名单 -->
             <host user="mycat" host="127.0.0.1"></host>
         </whitehost>
         <blacklist check="true">
             <!-- 黑名单允许的权限,后面为默认值 -->
             <property name="selectAllColumnAllow">false</property>
         </blacklist>
     </firewall>
     ```

     黑名单拦截明细配置:

     | 配置项                      | 默认值 | 描述                                                         |
     | :-------------------------- | :----: | :----------------------------------------------------------- |
     | selectAllow                 |  true  | 是否允许执行SELECT语句                                       |
     | selectAllColumnAllow        |  true  | 是否允许执行SELECT * FROM T这样的语句                        |
     | selectIntoAllow             |  true  | SELECT查询中是否允许INTO子句                                 |
     | deleteAllow                 |  true  | 是否允许执行DELETE语句                                       |
     | updateAllow                 |  true  | 是否允许执行UPDATE语句                                       |
     | insertAllow                 |  true  | 是否允许执行INSERT语句                                       |
     | replaceAllow                |  true  | 是否允许执行REPLACE语句                                      |
     | mergeAllow                  |  true  | 是否允许执行MERGE语句(Oracle)                                |
     | callAllow                   |  true  | 是否允许通过jdbc的call语法调存储过程                         |
     | setAllow                    |  true  | 是否允许使用SET语句                                          |
     | truncateAllow               |  true  |                                                              |
     | createTableAllow            |  true  | 是否允许创建表                                               |
     | alterTableAllow             |  true  | 是否允许执行ALTER TABLE语句                                  |
     | dropTableAllow              |  true  | 是否允许删除表                                               |
     | commentAllow                | false  | 是否允许语句中存在注释                                       |
     | noneBaseStatementAllow      | false  | 是否允许以上基本语句的其他语句,可以屏蔽DDL                   |
     | multiStatementAllow         | false  | 是否允许一次执行多条语句                                     |
     | useAllow                    |  true  | 是否允许执行USE语句                                          |
     | describeAllow               |  true  | 是否允许执行DESCRIBE语句                                     |
     | showAllow                   |  true  | 是否允许执行SHOW语句                                         |
     | commitAllow                 |  true  | 是否允许执行COMMIT操作                                       |
     | rollbackAllow               |  true  | 是否允许执行ROLL BACK操作                                    |
     | selectWhereAlwayTrueCheck   |  true  | 检查SELECT语句的WHERE子句是否是一个永真条件                  |
     | selectHavingAlwayTrueCheck  |  true  | 检查SELECT语句的HAVING子句是否是一个永真条件                 |
     | deleteWhereAlwayTrueCheck   | false  | 检查DELETE语句的WHERE子句是否是一个永真条件                  |
     | deleteWhereNoneCheck        | false  | 检查DELETE语句是否无WHERE条件                                |
     | updateWhereAlwayTrueCheck   |  true  | 检查UPDATE语句的WHERE子句是否是一个永真条件                  |
     | updateWhereNoneCheck        | false  | 检查UPDATE语句是否无WHERE条件                                |
     | conditionAndAlwayTrueAllow  | false  | 检查查询条件(WHERE/HAVING)子句是否包含AND永真                |
     | conditionAndAlwayFalseAllow | false  | 检查查询条件(WHERE/HAVING)子句是否包含AND永假                |
     | conditionLikeTrueAllow      |  true  | 检查查询条件(WHERE/HAVING)子句是否包含LIKE永真               |
     | selectIntoOutfileAllow      | false  | SELECT...INTO OUTFILE是否允许                                |
     | selectUnionCheck            |  true  | 检测SELECT UNION                                             |
     | selectMinusCheck            |  true  | 检测SELECT MINUS                                             |
     | selectExceptCheck           |  true  | 检测SELECT EXCEPT                                            |
     | selectIntersectCheck        |  true  | 检测SELECT INTERSECT                                         |
     | mustParameterized           | false  | 是否必须参数化                                               |
     | strictSyntaxCheck           |  true  | 是否进行严格的语法检测                                       |
     | conditionOpXorAllow         | false  | 查询条件中是否允许有XOR条件                                  |
     | conditionOpBitwseAllow      |  true  | 查询条件中是否允许有&、~、\|、^运算符                        |
     | conditionDoubleConstAllow   | false  | 查询条件中是否允许连续两个常量运算表达式                     |
     | minusAllow                  |  true  | 是否允许SELECT * FROM A MINUS SELECT * FROM B这样的语句      |
     | intersectAllow              |  true  | 是否允许SELECT * FROM A INTERSECT SELECT * FROM B这样的语句  |
     | constArithmeticAllow        |  true  | 拦截常量运算的条件                                           |
     | limitZeroAllow              | false  | 是否允许LIMIT 0这样的语句                                    |
     | tableCheck                  |  true  | 检测是否使用了禁用的表                                       |
     | schemaCheck                 |  true  | 检测是否使用了禁用的Schema                                   |
     | functionCheck               |  true  | 检测是否使用了禁用的函数                                     |
     | objectCheck                 |  true  | 检测是否使用了禁用的对象                                     |
     | variantCheck                |  true  | 检测是否使用了禁用的变量                                     |
     | readOnlyTables              |        | 指定的表只读,不能够在SELECT INTO,DELETE,UPDATE,INSERT,MERGE中作为"被修改表"出现 |

* 数据分片

     1. 全局表

        ```xml
        # NOTE: 全局表每个分片节点上都要有运行创建表的DDL语句
        <table name="company" primaryKey="ID" type="global" dataNode="dn1,dn2"/>
        ```

     2. ER Join

        ```xml
        <table name="order" dataNode="dn1,dn2" rule="sharding-by-intfile">
        	<childTable name="order_detail" primaryKey="id" joinKey="order_id" parentKey="order_id"/>
        </table>
        ```

     3. Share Join

        ```
        <table name="A" dataNode="dn1,dn2" rule="auto-sharding-long"/>
        <table name="B" dataNode="dn1,dn2" rule="auto-sharding-long"/>
        ```

* 注解

     相当于对MyCat不支持的SQL语句做了一层透明代理转发,直接交给目标的数据节点进行SQL语句的执行,其中注解的SQL用于确定最终执行SQL的数据节点。

     ```sql
     /*!mycat:sql=注解SQL语句*/真正执行的sql
     
     /*#mycat:db_type=master*/SELECT * FROM travel;
     /*!#mycat:db_type=master*/SELECT * FROM travel;
     /**#mycat:db_type=master*/SELECT * FROM travel;
     # 建存储过程
     /*!mycat:sql=SELECT 1 FROM test*/CREATE PROCEDURE `test_proc`() BEGIN END;
     # 建表
     /*!mycat:sql=SELECT 1 FROM test*/CREATE TABLE test2(id INT);
     # 特殊语句自定义分片
     /*!mycat:sql=SELECT 1 FROM test*/INSERT INTO t_user(id,name) SELECT id,name FROM t_user2;
     
     # 读写分离
     a.事务内的SQL默认走写节点,以注解/*balance*/开头,则会根据schema.xml的dataHost标签属性的balance=1或2去获取节点
     b.非事务内的SQL,开启读写分离默认根据balance=1或2去获取,以注解/*balance*/开头则会走写节点解决部分已经开启读写分离,但是需要强一致性数据实时获取的情况走写节点
     /*balance*/SELECT a.* FROM customer a WHERE a.company_id=1;
     
     # 指定走哪个Schema
     /*!mycat:schema=test_01*/sql;
     ```

* 管理命令与监控

     ```
     # NOTE:使用9066登录
     mysql -umycat -p111111 -h192.168.100.21 -P9066
     
     # 查看所有命令
     show @@help;
     # 更新配置文件(热部署,执行时服务不可用)
     reload @@config;
     # 显示MyCat数据库列表
     show @@database;
     # 显示MyCat节点列表
     show @@datanode;
     show @@datanode where schema=mycat;
     # 报告心跳状态
     show @@heartbeat;
     # MyCat版本
     show @@version;
     # 获取MyCat前端连接状态
     show @@connection;
     # 杀掉连接
     kill @@connection id1,id2,id3;
     # 查看后端连接状态
     show @@backend;
     # 查看MyCat缓存
     show @@cache;
     # 查看数据源状态
     show @@datasource;
     # 配置了多主,可以切换(index:dataHost的writeHost index位标,从0开始,执行时服务不可用)
     switch @@datasource dn1:index;
     # 显示系统日志
     show @@syslog limit=10;
     # 清除缓存
     reload @@user_stat;
     # 显示统计信息
     show @@sql;
     show @@sql.show;
     show @@sql.sum;
     # 设置慢SQL阈值
     reload @@sysslow=0;
     # 显示执行计划
     explain2 datanode=dn1 sql=SELECT * FROM mytbl;
     ```

* 调用存储过程

     ```
     # 1.无返回值
     /*#mycat:sql=SELECT * FROM test*/CALL p_test(1,@pout)
     
     # 2.返回普通out参数
     /*#mycat:sql=SELECT * FROM test*/SET @pin=111;call p_test(@pin,@pout);SELECT @pout;
     
     # 返回结果中有结果集时,则必须加注释,且注释中必须在list_fields中包括所有结果集参数名称,以逗号隔开结果集参数必须在最后
     /*#mycat:sql=SELECT * FROM test WHERE id=1,list_fields='@p_CURSOR,@p_cursor1'*/
     ```

     