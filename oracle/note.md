* 登录Oracle数据库:

  sqlplus fapdb/dba_0329

* 查看数据库版本

  SELECT * FROM V$VERSION

* 查看数据库名

  SELECT name FROM v$database;

* 查看sid

  SELECT instance_name FROM v$instance;