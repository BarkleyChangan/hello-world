* 下载
  <https://services.gradle.org/distributions/gradle-4.1-all.zip>

* 配置环境变量

  1. GRADLE_HOME:具体指的是Gradle的解压目录
  2. 添加将GRADLE_HOME\bin配置到Path中

* 查看版本
  `gradle -v`

* 运行
  `gradle -q hello`

* Gradle Wrapper
  是对Gradle的一层包装,方便在团队中统一管理Gradle的版本,项目开发中通常使用的Wrapper 这种方式,使用Wrapper之后就不需要采用配置Gradle的构建环境的方式,使用Wrapper启用Gradle的时候,Wrapper会检查Gradle有没有下载关联,如果没有下载则从配置的地址下载并进行构建,这就一定程度上方便开发人员构建项目
  Gradle提供了内置的Wrapper Task来生成Wrapper所需的目录文件,在相应的目录执行`gradle wrapper`命令即可生成,生成的目录如下:

  ```
  │─gradlew
  │─gradlew.bat
  └─gradle
      └─wrapper
              gradle-wrapper.jar
              gradle-wrapper.properties
  ```
  gradlew:Linux下可执行脚本
  gradlew.bat:Window下可执行脚本
  gradle-wrapper.jar:根据具体业务实现的jar包
  gradle-wrapper.properties:配置使用哪一个版本的gradle进行构建操作
  
* Gradle Wrapper配置
  ```gradle
  //指定使用的Gradle版本
  gradle wrapper --gradle-version 3.3
  
  //指定下载Gradle的地址
  gradle wrapper --gradle-distribution-url ...
  ```
  
* gradle-wrapper.properties
  ```
  distributionBase //下载的Gradle压缩包解压后存储的主目录
  distributionPath //相对于distributionBase解压后压缩包的路径
  zipStoreBase     //相对于distributionBase存放Gradle压缩包的
  zipStorePath     //相对于distributionPath存放Gradle压缩包的
  distributionUrl  //Gradle的下载地址，一般是官网地址
  
  //表示用户目录,用户目录下.gradle下的目录
  GRADLE_USER_HOME
  //表示项目目录,项目下gradlew所在的目录
  PROJECT
  ```

* Gradle日志
  ```
  ERROR     //错误消息
  QUIET     //重要消息
  WARNING   //警告消息
  LIFECYCLE //进度消息
  INFO      //信息消息
  DEBUG     //调试信息
  
  使用时可以通过命令行的方式控制日志显示级别,下面是可以使用命令控制的日志选项,如下：
  -q 或 --quiet //表示QUIET及其更高级别
  -i 或 --info  //表示INFO及其更高级别
  -d 或 --debug //DEBUG 及其更高级别（输出所有日志）
  如果不指定,则默认输出的日志是LIFECYCLE及其更高级别的日志
  ```
  
* 输出堆栈信息
  ```
  -s 或 --stacktrace         //输出关键性的堆栈信息
  -S 或 --full--stacktrace   //输出全部堆栈信息
  ```
  
* 日志信息调试
  ```
  //日志测试
  task hello{
  	doLast{
  		println 'Hello world'
  		print 'Hi'
  		logger.quiet('quiet 日志')
  		logger.lifecycle('lifecycle 日志')
  		logger.error('error 日志')
  		logger.info('info 日志')
  		logger.warn('warn 日志')
  		logger.debug('debug 日志')
  	}
  }
  ```
  
* Gradle命令行
  ```
  gradle -h
  gradle -?
  gradle -help
  ```
  
* 查看可执行Tasks
  
  ```
  ./gradlew tasks
  
  // 查看某个Task的帮助
  // 命令格式
  gradle help --task Task名称
  // 举例
  gradle help --task projects
  ```
  
  