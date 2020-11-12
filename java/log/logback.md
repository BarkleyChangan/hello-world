* 官方文档

  <http://logback.qos.ch/manual/index.html>

  <https://github.com/itwanger/logback-chinese-manual>

* 将log4j.properties转成logback-test.xml

  <http://logback.qos.ch/translator/>

* 打印内部状态信息

  ```java
  public class Main {
      static Logger logger = LoggerFactory.getLogger(Main.class);
  
      public static void main(String[] args) {
          logger.debug("logback");
          System.out.println("#########################");
          LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
          StatusPrinter.print(lc);
      }
  }
  ```

* RollingFileAppender

  1. RollingPolicy:负责日志的滚动功能,如果 RollingPolicy也实现了TriggeringPolicy接口,那么只需要设置 RollingPolicy就好
  2. TriggeringPolicy:负责日志滚动的时机
  3. TimeBasedRollingPolicy:同时实现了RollingPolicy 与TriggeringPolicy接口,因此使用 TimeBasedRollingPolicy的时候就可以不指定TriggeringPolicy
     * fileNamePattern:用来定义文件的名字(必选项)。它的值应该由文件名加上一个`%d`的占位符。`%d` 应该包含`java.text.SimpleDateFormat`中规定的日期格式，缺省是`yyyy-MM-dd`。滚动周期是通过 fileNamePattern推断出来的。
     * maxHistory:最多保留多少数量的日志文件(可选项),将会通过异步的方式删除旧的文件。比如,你指定按月滚动,指定 `maxHistory=6`,那么 6 个月内的日志文件将会保留,超过 6 个月的将会被删除。
     * totalSizeCap:所有日志文件的大小(可选项)。超出这个大小时,旧的日志文件将会被异步删除。需要配合maxHistory属性一起使用,并且是第二条件。