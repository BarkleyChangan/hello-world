* 登录Oracle数据库:

  sqlplus fapdb/dba_0329

  如果sqlplus访问不了,su - oracle切换oracle用户

* 查看数据库版本

  SELECT * FROM V$VERSION

* 查看数据库名

  SELECT name FROM v$database;

* 查看sid

  SELECT instance_name FROM v$instance;
  
* 查看Service Name

  show parameter service;

* PL/SQL配置(安装32位)

  ```
  1.修改instantclient_11_2目录下的NETWORK\ADMIN\tnsnames.ora
  ORCL11G =
    (DESCRIPTION =
      (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.1.10)(PORT = 1521))
      (CONNECT_DATA =
        (SERVER = DEDICATED)
        (SERVICE_NAME = orcl11g)
      )
    )
  2.Preferences -> Oracle -> Connection
  Oracle Home:D:\program\green\32\oracle_home\instantclient_11_2
  OCI library:D:\program\green\32\oracle_home\instantclient_11_2\oci.dll
  ```

* PL/SQL乱码解决

  1. `select userenv('language') from dual;`

  2. 设置环境变量

     变量名:NLS_LANG

     变量值:SIMPLIFIED CHINESE_CHINA.ZHS16GBK