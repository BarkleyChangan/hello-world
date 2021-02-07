* 下载
  <https://services.gradle.org/distributions>
* 配置环境变量

  1. GRADLE_HOME:具体指的是Gradle的解压目录
  2. 添加将GRADLE_HOME\bin配置到Path中  
  3. ***NOTE:***GRADLE_USER_HOME:D:\repository-maven # 本地Maven仓库配置
* 查看版本
  `gradle -v`  
* 运行
  

创建一个名为 build.gradle 的脚本，脚本内容如下:

  ```build.gradle
task hello{
  	doLast{
  		println 'Hello world'
    }
}
  ```

  使用`gradle -q hello`命令执行上述脚本  

* 执行周期  
  
  gradle构建的生命周期主要分为三个阶段:Initialization、Configuration、Execution
  
  1. Initialization:Gradle支持单个或多个工程的构建，在此阶段Gradle决定哪些工程将参与到当前构建的过程中，并为每一个这样的工程创建一个Project实例，一般情况下，参与构建的工程信息将在settings.gradle中定义
  2. Configuration:在此阶段，配置Project实例。所有工程的构建脚本都将被执行。Task、Configuraion和许多其他对象将被创建和配置
  3. Execution:在之前的Configuration阶段，Task的一个子集被创建并配置，这些子集来自于作为参数传入Gradle命令的Task名字，在Execution阶段，这一子集将被依次执行
  
* 三个基本对象  
  1. Gradle对象:当我们执行gradle xxx或者什么的时候，gradle会从默认的**配置脚本**中构造出一个Gradle对象。在整个执行过程中，只有这么一个对象。Gradle对象的数据类型就是Gradle。我们一般很少去定制这个默认的配置脚本
  2. Project对象:每一个build.gradle会转换成一个Project对象
  3. Settings对象:每一个settings.gradle都会转换成一个Settings对象
  
* 基本命令

```gradle
# 显示包含多少个子Project
gradle projects
# 显示某个Project包含哪些Task信息
gradle project-path:tasks # project-path是目录名,后面必须跟冒号;cd到某个Project目录,则不需指定project-path
# 列出项目中所有可用的task
gradle -q tasks [--all]
# 在执行时排除任务
gradle groupTherapy -x yayGradle0
# 打印出所有可用命令行选项
-?,h,--help
# 执行一个特定名字的构建脚本
-b,--build-file
# 离线方式运行,仅仅在本地缓存中检查依赖是否存在
--offline

# 守护进程
守护进程只会被创建一次,会在3小时空闲时间之后自动过期
gradle groupTherapy --daemon
不适用守护进程:
gradle groupTherapy --no-daemon
手动停止守护进程:
gradle --stop
```

* Gradle Wrapper
  是对Gradle的一层包装,方便在团队中统一管理Gradle的版本,项目开发中通常使用的Wrapper 这种方式,使用Wrapper之后就不需要采用配置Gradle的构建环境的方式,使用Wrapper启用Gradle的时候,Wrapper会检查Gradle有没有下载关联,如果没有下载则从配置的地址下载并进行构建,这就一定程度上方便开发人员 [build.gradle](C:\Users\chang\Desktop\to-app\build.gradle) 构建项目
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
  task wrapper(type:Wrapper){
      gradleVersion='1.2'
      distributionUrl='http://myenterprise.com/gradle/dists'
      distributionPath='gradle-dists'
  }
  
  gradle wrapper
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
  
* 闭包

  1. 定义一个方法,参数closure用于接收闭包
  2. 闭包的执行就是花括号里面代码的执行
  3. 闭包接收的参数就是闭包参数closure参数中的i,如果是一个参数默认就是it变量
  4. Groovy 的闭包有三个属性:thisObject、owner、delegate,当在一个闭包中调用定义的方法时,由这三个属性来确定该方法由哪个对象来执行,默认owner和delegate是相等的,其中delegate是可以被修改的,Gradle 中闭包的很多功能都是通过修改delegate来实现的
  
* 解决相关依赖包下载缓慢问题

  %GRADLE_USER_HOME%\.gradle目录下,新增下`init.gradle`文件
  
* 加速Gradle构建  

  ```
  1.并行构建:在gradle.properties文件中添加如下代码
  org.gradle.parallel=true
  2.启动Gradle daemon:在gradle.properties文件中添加如下代码
org.gradle.daemon=true
  3.调整Java虚拟机参数加速编译:在gradle.properties文件中添加如下代码
  org.gradle.jvmargs=-Xms256m -Xmx1024m
  4.多模块加速构建:在gradle.properties文件中添加如下代码
  org.gradle.configureondemand=true
  ```
  
* 国内镜像加速  

  ```
  # 1.国内访问国外仓库地址很慢，第一种方法是在每个项目中设置repositories
  repositories {
      mavenLocal() // 先从maven本地仓库加载依赖
      'maven(http://maven.aliyun.com/nexus/content/groups/public/)'
      mavenCentral()
  }
  # 2.更推荐的方式是类似的Maven的settings.xml全局的配置，在配置的GRADLE_USER_HOME路径下，添加init.gradle文件，以下配置文件中使用了阿里云的Gradle代理，支持jcenter、google、maven仓库
  gradle.projectsLoaded {
      rootProject.allprojects {
          buildscript {
              repositories {
                  def JCENTER_URL = 'https://maven.aliyun.com/repository/jcenter'
                  def GOOGLE_URL = 'https://maven.aliyun.com/repository/google'
                  def NEXUS_URL = 'http://maven.aliyun.com/nexus/content/repositories/jcenter'
                  all { ArtifactRepository repo ->
                      if (repo instanceof MavenArtifactRepository) {
                          def url = repo.url.toString()
                          if (url.startsWith('https://jcenter.bintray.com/')) {
                              project.logger.lifecycle "Repository ${repo.url} replaced by $JCENTER_URL."
                              println("buildscript ${repo.url} replaced by $JCENTER_URL.")
                              remove repo
                          }
                          else if (url.startsWith('https://dl.google.com/dl/android/maven2/')) {
                              project.logger.lifecycle "Repository ${repo.url} replaced by $GOOGLE_URL."
                              println("buildscript ${repo.url} replaced by $GOOGLE_URL.")
                              remove repo
                          }
                          else if (url.startsWith('https://repo1.maven.org/maven2')) {
                              project.logger.lifecycle "Repository ${repo.url} replaced by $REPOSITORY_URL."
                              println("buildscript ${repo.url} replaced by $REPOSITORY_URL.")
                              remove repo
                          }
                      }
                  }
                  jcenter {
                      url JCENTER_URL
                  }
                  google {
                      url GOOGLE_URL
                  }
                  maven {
                      url NEXUS_URL
                  }
              }
          }
          repositories {
              def JCENTER_URL = 'https://maven.aliyun.com/repository/jcenter'
              def GOOGLE_URL = 'https://maven.aliyun.com/repository/google'
              def NEXUS_URL = 'http://maven.aliyun.com/nexus/content/repositories/jcenter'
              all { ArtifactRepository repo ->
                  if (repo instanceof MavenArtifactRepository) {
                      def url = repo.url.toString()
                      if (url.startsWith('https://jcenter.bintray.com/')) {
                          project.logger.lifecycle "Repository ${repo.url} replaced by $JCENTER_URL."
                          println("buildscript ${repo.url} replaced by $JCENTER_URL.")
                          remove repo
                      }
                      else if (url.startsWith('https://dl.google.com/dl/android/maven2/')) {
                          project.logger.lifecycle "Repository ${repo.url} replaced by $GOOGLE_URL."
                          println("buildscript ${repo.url} replaced by $GOOGLE_URL.")
                          remove repo
                      }
                      else if (url.startsWith('https://repo1.maven.org/maven2')) {
                          project.logger.lifecycle "Repository ${repo.url} replaced by $REPOSITORY_URL."
                          println("buildscript ${repo.url} replaced by $REPOSITORY_URL.")
                          remove repo
                      }
                  }
              }
              jcenter {
                  url JCENTER_URL
              }
              google {
                  url GOOGLE_URL
              }
              maven {
                  url NEXUS_URL
              }
          }
      }
  }
  ```

  