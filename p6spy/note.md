* 引入依赖  

  ```
  <!-- https://mvnrepository.com/artifact/p6spy/p6spy -->
  <dependency>
      <groupId>p6spy</groupId>
      <artifactId>p6spy</artifactId>
      <version>3.0.0</version>
  </dependency>
  ```

* 自定义输出格式  

  ```java
  package com.post.p6spy;
  
  import com.p6spy.engine.logging.Category;
  import com.p6spy.engine.spy.appender.StdoutLogger;
  import org.apache.commons.lang3.StringUtils;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  
  /**
   * @author Barkley.Chang
   * @className:P6SpyLogger
   * @description: 设置P6Spy输出日志格式类
   * @date 2021-01-15 09:26
   */
  public class P6SpyLogger extends StdoutLogger {
      protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  
      /**
       * 重写输出方法
       *
       * @param connectionId 连接id
       * @param now          当前时间
       * @param elapsed      执行时长，包括执行 SQL 和处理结果集的时间(可以参考来调优)
       * @param category     语句分类，statement、resultset 等
       * @param prepared     查询语句。可能是 prepared statement，表现为 select * from table1 where c1=?，问号参数形式
       * @param sql          含参数值的查询语句，如 select * from from table1 where c1=7
       */
      @Override
      public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql) {
          if (!Category.COMMIT.equals(category) && !prepared.startsWith("select count(")) {
              this.logText(this.strategy.formatMessage(connectionId, now, elapsed, category.toString(), "-prepared-", sql));
          }
      }
  
      @Override
      public void logText(String text) {
          StringBuilder sb = new StringBuilder();
          //匹配到最后一个|作为分隔符
          String[] arrString = text.split("\\|(?![^\\|]*\\|)");
          if (arrString.length > 1) {
              sb.append(arrString[0]);
              //去最后一段语句做替换进行格式化
              String sss = arrString[1].trim();
              if (StringUtils.isNotBlank(sss) && !";".equalsIgnoreCase(sss)) {
                  String sql = new SQLFormatter().format(arrString[1]);
                  sb.append("\r\n");
                  sb.append(sql);
                  sb.append("\r\n");
              } else {
                  sb.append(sss);
              }
              //this.getStream().println(sb.toString());
              logger.debug(sb.toString());
          } else {
              //this.getStream().println(text);
              logger.debug(text);
          }
          arrString = null;
      }
  }
  ```

  ```java
  package com.post.p6spy;
  
  import java.util.HashSet;
  import java.util.LinkedList;
  import java.util.Set;
  import java.util.StringTokenizer;
  
  /**
   * @author Barkley.Chang
   * @className:SQLFormatter
   * @description: TODO
   * @date 2021-01-15 09:47
   */
  public class SQLFormatter {
      private static final Set<String> BEGIN_CLAUSES = new HashSet<String>();
      private static final Set<String> END_CLAUSES = new HashSet<String>();
      private static final Set<String> LOGICAL = new HashSet<String>();
      private static final Set<String> QUANTIFIERS = new HashSet<String>();
      private static final Set<String> DML = new HashSet<String>();
      private static final Set<String> MISC = new HashSet<String>();
      public static final String WHITESPACE = " \n\r\f\t";
  
      static {
          BEGIN_CLAUSES.add("left");
          BEGIN_CLAUSES.add("right");
          BEGIN_CLAUSES.add("inner");
          BEGIN_CLAUSES.add("outer");
          BEGIN_CLAUSES.add("group");
          BEGIN_CLAUSES.add("order");
  
          END_CLAUSES.add("where");
          END_CLAUSES.add("set");
          END_CLAUSES.add("having");
          END_CLAUSES.add("join");
          END_CLAUSES.add("from");
          END_CLAUSES.add("by");
          END_CLAUSES.add("join");
          END_CLAUSES.add("into");
          END_CLAUSES.add("union");
  
          LOGICAL.add("and");
          LOGICAL.add("or");
          LOGICAL.add("when");
          LOGICAL.add("else");
          LOGICAL.add("end");
  
          QUANTIFIERS.add("in");
          QUANTIFIERS.add("all");
          QUANTIFIERS.add("exists");
          QUANTIFIERS.add("some");
          QUANTIFIERS.add("any");
  
          DML.add("insert");
          DML.add("update");
          DML.add("delete");
  
          MISC.add("select");
          MISC.add("on");
      }
  
      static final String indentString = "    ";
      static final String initial = "\n    ";
  
      public String format(String source) {
          return new FormatProcess(source).perform();
      }
  
      private static class FormatProcess {
          boolean beginLine = true;
          boolean afterBeginBeforeEnd = false;
          boolean afterByOrSetOrFromOrSelect = false;
          boolean afterValues = false;
          boolean afterOn = false;
          boolean afterBetween = false;
          boolean afterInsert = false;
          int inFunction = 0;
          int parensSinceSelect = 0;
          private LinkedList<Integer> parenCounts = new LinkedList<Integer>();
          private LinkedList<Boolean> afterByOrFromOrSelects = new LinkedList<Boolean>();
  
          int indent = 1;
  
          StringBuilder result = new StringBuilder();
          StringTokenizer tokens;
          String lastToken;
          String token;
          String lcToken;
  
          public FormatProcess(String sql) {
              tokens = new StringTokenizer(
                      sql,
                      "()+*/-=<>'`\"[]," + WHITESPACE,
                      true
              );
          }
  
          public String perform() {
              result.append(initial);
  
              while (tokens.hasMoreTokens()) {
                  token = tokens.nextToken();
                  lcToken = token.toLowerCase();
  
                  if ("'".equals(token)) {
                      String t;
                      do {
                          t = tokens.nextToken();
                          token += t;
                      }
                      while (!"'".equals(t) && tokens.hasMoreTokens()); // cannot handle single quotes
                  } else if ("\"".equals(token)) {
                      String t;
                      do {
                          t = tokens.nextToken();
                          token += t;
                      }
                      while (!"\"".equals(t));
                  }
  
                  if (afterByOrSetOrFromOrSelect && ",".equals(token)) {
                      commaAfterByOrFromOrSelect();
                  } else if (afterOn && ",".equals(token)) {
                      commaAfterOn();
                  } else if ("(".equals(token)) {
                      openParen();
                  } else if (")".equals(token)) {
                      closeParen();
                  } else if (BEGIN_CLAUSES.contains(lcToken)) {
                      beginNewClause();
                  } else if (END_CLAUSES.contains(lcToken)) {
                      endNewClause();
                  } else if ("select".equals(lcToken)) {
                      select();
                  } else if (DML.contains(lcToken)) {
                      updateOrInsertOrDelete();
                  } else if ("values".equals(lcToken)) {
                      values();
                  } else if ("on".equals(lcToken)) {
                      on();
                  } else if (afterBetween && lcToken.equals("and")) {
                      misc();
                      afterBetween = false;
                  } else if (LOGICAL.contains(lcToken)) {
                      logical();
                  } else if (isWhitespace(token)) {
                      white();
                  } else {
                      misc();
                  }
  
                  if (!isWhitespace(token)) {
                      lastToken = lcToken;
                  }
              }
  
              return result.toString();
          }
  
          private void commaAfterOn() {
              out();
              indent--;
              newline();
              afterOn = false;
              afterByOrSetOrFromOrSelect = true;
          }
  
          private void commaAfterByOrFromOrSelect() {
              out();
              newline();
          }
  
          private void logical() {
              if ("end".equals(lcToken)) {
                  indent--;
              }
              newline();
              out();
              beginLine = false;
          }
  
          private void on() {
              indent++;
              afterOn = true;
              newline();
              out();
              beginLine = false;
          }
  
          private void misc() {
              out();
              if ("between".equals(lcToken)) {
                  afterBetween = true;
              }
              if (afterInsert) {
                  newline();
                  afterInsert = false;
              } else {
                  beginLine = false;
                  if ("case".equals(lcToken)) {
                      indent++;
                  }
              }
          }
  
          private void white() {
              if (!beginLine) {
                  result.append(" ");
              }
          }
  
          private void updateOrInsertOrDelete() {
              out();
              indent++;
              beginLine = false;
              if ("update".equals(lcToken)) {
                  newline();
              }
              if ("insert".equals(lcToken)) {
                  afterInsert = true;
              }
          }
  
          @SuppressWarnings({"UnnecessaryBoxing"})
          private void select() {
              out();
              indent++;
              newline();
              parenCounts.addLast(Integer.valueOf(parensSinceSelect));
              afterByOrFromOrSelects.addLast(Boolean.valueOf(afterByOrSetOrFromOrSelect));
              parensSinceSelect = 0;
              afterByOrSetOrFromOrSelect = true;
          }
  
          private void out() {
              result.append(token);
          }
  
          private void endNewClause() {
              if (!afterBeginBeforeEnd) {
                  indent--;
                  if (afterOn) {
                      indent--;
                      afterOn = false;
                  }
                  newline();
              }
              out();
              if (!"union".equals(lcToken)) {
                  indent++;
              }
              newline();
              afterBeginBeforeEnd = false;
              afterByOrSetOrFromOrSelect = "by".equals(lcToken)
                      || "set".equals(lcToken)
                      || "from".equals(lcToken);
          }
  
          private void beginNewClause() {
              if (!afterBeginBeforeEnd) {
                  if (afterOn) {
                      indent--;
                      afterOn = false;
                  }
                  indent--;
                  newline();
              }
              out();
              beginLine = false;
              afterBeginBeforeEnd = true;
          }
  
          private void values() {
              indent--;
              newline();
              out();
              indent++;
              newline();
              afterValues = true;
          }
  
          @SuppressWarnings({"UnnecessaryUnboxing"})
          private void closeParen() {
              parensSinceSelect--;
              if (parensSinceSelect < 0) {
                  indent--;
                  parensSinceSelect = parenCounts.removeLast().intValue();
                  afterByOrSetOrFromOrSelect = afterByOrFromOrSelects.removeLast().booleanValue();
              }
              if (inFunction > 0) {
                  inFunction--;
                  out();
              } else {
                  if (!afterByOrSetOrFromOrSelect) {
                      indent--;
                      newline();
                  }
                  out();
              }
              beginLine = false;
          }
  
          private void openParen() {
              if (isFunctionName(lastToken) || inFunction > 0) {
                  inFunction++;
              }
              beginLine = false;
              if (inFunction > 0) {
                  out();
              } else {
                  out();
                  if (!afterByOrSetOrFromOrSelect) {
                      indent++;
                      newline();
                      beginLine = true;
                  }
              }
              parensSinceSelect++;
          }
  
          private static boolean isFunctionName(String tok) {
              final char begin = tok.charAt(0);
              final boolean isIdentifier = Character.isJavaIdentifierStart(begin) || '"' == begin;
              return isIdentifier &&
                      !LOGICAL.contains(tok) &&
                      !END_CLAUSES.contains(tok) &&
                      !QUANTIFIERS.contains(tok) &&
                      !DML.contains(tok) &&
                      !MISC.contains(tok);
          }
  
          private static boolean isWhitespace(String token) {
              return WHITESPACE.indexOf(token) >= 0;
          }
  
          private void newline() {
              result.append("\n");
              for (int i = 0; i < indent; i++) {
                  result.append(indentString);
              }
              beginLine = true;
          }
      }
  
      public static void main(String[] args) {
          String sql = new SQLFormatter().format("select * from t_sss");
          System.out.println(sql);
      }
  }
  ```

* 配置spy.properties 

  ```text
  #################################################################
  # P6Spy Options File                                            #
  # See documentation for detailed instructions                   #
  # http://p6spy.github.io/p6spy/2.0/configandusage.html          #
  #################################################################
  
  #################################################################
  # MODULES                                                       #
  #                                                               #
  # Module list adapts the modular functionality of P6Spy.        #
  # Only modules listed are active.                               #
  # (default is com.p6spy.engine.logging.P6LogFactory and         #
  # com.p6spy.engine.spy.P6SpyFactory)                            #
  # Please note that the core module (P6SpyFactory) can't be      #
  # deactivated.                                                  #
  # Unlike the other properties, activation of the changes on     #
  # this one requires reload.                                     #
  #################################################################
  #modulelist=com.p6spy.engine.spy.P6SpyFactory,com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory
  
  ################################################################
  # CORE (P6SPY) PROPERTIES                                      #
  ################################################################
  
  # A comma separated list of JDBC drivers to load and register.
  # (default is empty)
  #
  # Note: This is normally only needed when using P6Spy in an
  # application server environment with a JNDI data source or when
  # using a JDBC driver that does not implement the JDBC 4.0 API
  # (specifically automatic registration).
  driverlist=com.mysql.jdbc.Driver
  # for flushing per statement
  # (default is false)
  #autoflush = false
  
  # sets the date format using Java's SimpleDateFormat routine.
  # In case property is not set, miliseconds since 1.1.1970 (unix time) is used (default is empty)
  dateformat=yyyy-MM-dd HH:mm:ss
  
  # prints a stack trace for every statement logged
  #stacktrace=false
  # if stacktrace=true, specifies the stack trace to print
  #stacktraceclass=
  
  # determines if property file should be reloaded
  # Please note: reload means forgetting all the previously set
  # settings (even those set during runtime - via JMX)
  # and starting with the clean table
  # (default is false)
  #reloadproperties=false
  
  # determines how often should be reloaded in seconds
  # (default is 60)
  #reloadpropertiesinterval=60
  
  # specifies the appender to use for logging
  # Please note: reload means forgetting all the previously set
  # settings (even those set during runtime - via JMX)
  # and starting with the clean table
  # (only the properties read from the configuration file)
  # (default is com.p6spy.engine.spy.appender.FileLogger)
  #appender=com.p6spy.engine.spy.appender.Slf4JLogger
  #appender=com.p6spy.engine.spy.appender.StdoutLogger
  #appender=com.p6spy.engine.spy.appender.FileLogger
  #自定义的SQL格式化输出
  appender=com.post.p6spy.P6SpyLogger
  
  # name of logfile to use, note Windows users should make sure to use forward slashes in their pathname (e:/test/spy.log)
  # (used for com.p6spy.engine.spy.appender.FileLogger only)
  # (default is spy.log)
  #logfile = spy.log
  
  # append to the p6spy log file. if this is set to false the
  # log file is truncated every time. (file logger only)
  # (default is true)
  #append=true
  
  # class to use for formatting log messages (default is: com.p6spy.engine.spy.appender.SingleLineFormat)
  logMessageFormat=com.p6spy.engine.spy.appender.SingleLineFormat
  #logMessageFormat=com.p6spy.engine.spy.appender.MultiLineFormat
  
  # format that is used for logging of the date/time/... (has to be compatible with java.text.SimpleDateFormat)
  # (default is dd-MMM-yy)
  databaseDialectDateFormat=yyyy-MM-dd HH:mm:ss
  
  # whether to expose options via JMX or not
  # (default is true)
  #jmx=true
  
  # if exposing options via jmx (see option: jmx), what should be the prefix used?
  # jmx naming pattern constructed is: com.p6spy(.<jmxPrefix>)?:name=<optionsClassName>
  # please note, if there is already such a name in use it would be unregistered first (the last registered wins)
  # (default is none)
  #jmxPrefix=
  
  # if set to true, the execution time will be measured in nanoseconds as opposed to milliseconds
  # (default is false)
  #useNanoTime=false
  
  #################################################################
  # DataSource replacement                                        #
  #                                                               #
  # Replace the real DataSource class in your application server  #
  # configuration with the name com.p6spy.engine.spy.P6DataSource #
  # (that provides also connection pooling and xa support).       #
  # then add the JNDI name and class name of the real             #
  # DataSource here                                               #
  #                                                               #
  # Values set in this item cannot be reloaded using the          #
  # reloadproperties variable. Once it is loaded, it remains      #
  # in memory until the application is restarted.                 #
  #                                                               #
  #################################################################
  #realdatasource=/RealMySqlDS
  #realdatasourceclass=com.mysql.jdbc.jdbc2.optional.MysqlDataSource
  
  #################################################################
  # DataSource properties                                         #
  #                                                               #
  # If you are using the DataSource support to intercept calls    #
  # to a DataSource that requires properties for proper setup,    #
  # define those properties here. Use name value pairs, separate  #
  # the name and value with a semicolon, and separate the         #
  # pairs with commas.                                            #
  #                                                               #
  # The example shown here is for mysql                           #
  #                                                               #
  #################################################################
  #realdatasourceproperties=port;3306,serverName;myhost,databaseName;jbossdb,foo;bar
  
  #################################################################
  # JNDI DataSource lookup                                        #
  #                                                               #
  # If you are using the DataSource support outside of an app     #
  # server, you will probably need to define the JNDI Context     #
  # environment.                                                  #
  #                                                               #
  # If the P6Spy code will be executing inside an app server then #
  # do not use these properties, and the DataSource lookup will   #
  # use the naming context defined by the app server.             #
  #                                                               #
  # The two standard elements of the naming environment are       #
  # jndicontextfactory and jndicontextproviderurl. If you need    #
  # additional elements, use the jndicontextcustom property.      #
  # You can define multiple properties in jndicontextcustom,      #
  # in name value pairs. Separate the name and value with a       #
  # semicolon, and separate the pairs with commas.                #
  #                                                               #
  # The example shown here is for a standalone program running on #
  # a machine that is also running JBoss, so the JDNI context     #
  # is configured for JBoss (3.0.4).                              #
  #                                                               #
  # (by default all these are empty)                              #
  #################################################################
  #jndicontextfactory=org.jnp.interfaces.NamingContextFactory
  #jndicontextproviderurl=localhost:1099
  #jndicontextcustom=java.naming.factory.url.pkgs;org.jboss.nameing:org.jnp.interfaces
  
  #jndicontextfactory=com.ibm.websphere.naming.WsnInitialContextFactory
  #jndicontextproviderurl=iiop://localhost:900
  
  ################################################################
  # P6 LOGGING SPECIFIC PROPERTIES                               #
  ################################################################
  
  # filter what is logged
  # please note this is a precondition for usage of: include/exclude/sqlexpression
  # (default is false)
  #filter=false
  
  # comma separated list of strings to include
  # please note that special characters escaping (used in java) has to be done for the provided regular expression
  # (default is empty)
  #include =
  # comma separated list of strings to exclude
  # (default is empty)
  #exclude =
  
  # sql expression to evaluate if using regex
  # please note that special characters escaping (used in java) has to be done for the provided regular expression
  # (default is empty)
  #sqlexpression =
  
  #list of categories to exclude: error, info, batch, debug, statement,
  #commit, rollback and result are valid values
  # (default is info,debug,result,resultset,batch)
  #excludecategories=info,debug,result,resultset,batch
  
  # Execution threshold applies to the standard logging of P6Spy.
  # While the standard logging logs out every statement
  # regardless of its execution time, this feature puts a time
  # condition on that logging. Only statements that have taken
  # longer than the time specified (in milliseconds) will be
  # logged. This way it is possible to see only statements that
  # have exceeded some high water mark.
  # This time is reloadable.
  #
  # executionThreshold=integer time (milliseconds)
  # (default is 0)
  #executionThreshold=
  
  ################################################################
  # P6 OUTAGE SPECIFIC PROPERTIES                                #
  ################################################################
  # Outage Detection
  #
  # This feature detects long-running statements that may be indicative of
  # a database outage problem. If this feature is turned on, it will log any
  # statement that surpasses the configurable time boundary during its execution.
  # When this feature is enabled, no other statements are logged except the long
  # running statements. The interval property is the boundary time set in seconds.
  # For example, if this is set to 2, then any statement requiring at least 2
  # seconds will be logged. Note that the same statement will continue to be logged
  # for as long as it executes. So if the interval is set to 2, and the query takes
  # 11 seconds, it will be logged 5 times (at the 2, 4, 6, 8, 10 second intervals).
  #
  # outagedetection=true|false
  # outagedetectioninterval=integer time (seconds)
  #
  # (default is false)
  #outagedetection=false
  # (default is 60)
  #outagedetectioninterval=30
  ```

* 修改db.properties  

  ```text
  #jdbc.driver=com.mysql.jdbc.Driver
  jdbc.driver=com.p6spy.engine.spy.P6SpyDriver
  #jdbc.url=jdbc:mysql://localhost:3306/dbname
  jdbc.url=jdbc:p6spy:mysql://localhost:3306/dbname
  ```

* Spring配置  

  ```xml
  <bean id="dataSource-p6spy" class="com.p6spy.engine.spy.P6DataSource">
  	<constructor-arg ref="dataSource-druid" />
  </bean>
  ```

  