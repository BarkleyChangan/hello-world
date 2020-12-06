* 官网

  <http://groovy-lang.org/>

* 下载

  <https://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.20.zip>

* 配置环境变量

  ```
  GROOVY_HOME:D:\program\green\groovy-2.4.20
  PATH:;%GROOVY_HOME%\bin
  ```

* ItelliJ IDEA安装Groovy插件

  ```
  File -> Settings -> Plugins -> 搜索Groovy进行安装
  ```

* 闭包中this,owner,delegate区别

  this:代表闭包定义处的类

  owner:代表闭包定义处的类或闭包对象

  delegate:代表任意对象,默认值为owner

