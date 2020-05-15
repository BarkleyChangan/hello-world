* Spring整合Thymeleaf
  1.引入jar包
  Thymeleaf的Maven引入:
  ```xml
  <!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf -->
  <dependency>
      <groupId>org.thymeleaf</groupId>
      <artifactId>thymeleaf</artifactId>
      <version>3.0.11.RELEASE</version>
  </dependency>
  ```
  Spring3Maven引入:
  ```xml
  <!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf-spring3 -->
  <dependency>
      <groupId>org.thymeleaf</groupId>
      <artifactId>thymeleaf-spring3</artifactId>
      <version>3.0.11.RELEASE</version>
  </dependency>
  ```
  Spring4Maven引入:
  ```xml
  <!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf-spring4 -->
  <dependency>
      <groupId>org.thymeleaf</groupId>
      <artifactId>thymeleaf-spring4</artifactId>
      <version>3.0.11.RELEASE</version>
  </dependency>
  ```
  Spring5Maven引入:
  ```xml
  <!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf-spring5 -->
  <dependency>
      <groupId>org.thymeleaf</groupId>
      <artifactId>thymeleaf-spring5</artifactId>
      <version>3.0.11.RELEASE</version>
  </dependency>配置Bean
  ```
  2.配置
    a. 代码配置方式:
     ```java
  @Bean
  public SpringResourceTemplateResolver templateResolver() {
      // SpringResourcetemplateResolver automatically integrates with Spring's own
      // resource resolution infrastructure,whick is highly recommended
      SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
      templateResolver.setApplicationContext(this.applicationContext);
      templateResolver.setPrefix("/WEB-INF/template");
      templateResolver.setSuffix(".html");
      // HTML is the default value,added here for the sake of clarity
      templateResolver.setTemplateMode(TemplateMode.HTML);
      // Template cache is true by default.Set to false if you want
      // templates to be automatically updated when modified
      templateResolver.setCacheable(true);
      return templateResolver;
  }
  
  @Bean
  public SpringTemplateEngine templateEngine() {
      // SpringTemplteEngine automatically applies SpringStandardDialect and
      // enables Spring's own MessageSource message resolution mechanisms
      SpringTemplateEngine templateEngine = new SpringTemplateEngine();
      templateEngine.setTemplateResolver(templateResolver());
      // Enabling the SpringEl compiler with Spring 4.2.4 or newer can
      // speed up execution in most scenarios,bug might be incompatible
      // with specific cases when expressions in one template are reused
      // across different data types,so this flag is "false" by default
      // for safer backwards compatibility
      templateEngine.setEnableSpringELCompiler(true);
      return templateEngine;
  }
  
  @Bean
public ThymeleafViewResolver viewResolver() {
      ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
      viewResolver.setTemplateEngine(templateEngine());
      // NOTE 'order' and 'viewNames' are optional
      viewResolver.setOrder(1);
      viewResolver.setViewNames(new String[]{".html", "xhtml"});
      return viewResolver;
  }
     ```
    b. Xml文件配置方式
  ```
  <!-- SpringResourceTemplateResolver automatically integrates with Spring's own resource resolution infrastructure,whick is highly recommended -->
  <bean id="templateResolver" class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
      <property name="prefix" value="/WEB-INF/template"/>
      <property name="suffix" value=".html"/>
      <!-- HTML is the default value,added here for the sake of clarity -->
      <property name="templateMode" value="HTML"/>
      <!-- Template cache is true by default.Set to false if you want templates to be automatically updated when modified -->
      <property name="cacheable" value="false"/>
      <property name="characterEncoding" value="UTF-8"/>
  </bean>
  <!-- SpringTemplateEngine automatically applies SpringStandardDialect and enables Spring's own MessageSource message resolution mechanisms -->
  <bean id="templateEngine" class="org.thymeleaf.spring5.SpringTemplateEngine">
      <property name="templateResolver" ref="templateResolver"/>
      <!-- Enabling the SpringEL compiler with Spring 4.2.4 or newer can speed up
  execution in most scenarios,but might be incompatible with specific cases when expressions in one template are reused across different data types,so this flag is "false" by default for safer backwards compatibility -->
      <property name="enableSpringELCompiler" value="true"/>
  </bean>
  <bean class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
      <property name="templateEngine" ref="templateEngine"/>
      <property name="order" value="1"/>
      <!-- NOTE 'order' and 'viewNames' are optional --> 
      <!--<property name="viewNames" value="*.html,*.xhtml"/>-->
      <property name="characterEncoding" value="UTF-8"/>
  </bean>
  <!-- 国际化配置(NOTE:bean的id必须为messageSource) -->
  <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
      <property name="basenames">
      <array>
          <value>i18n/message</value>
          <value>i18n/home</value>
      </array>
      </property>
      <property name="defaultEncoding" value="UTF-8"/>
  </bean>
  <bean id="localeResolver" class="org.springframework.web.servlet.i18n.SessionLocaleResolver">
  <property name="defaultLocale" value="en_US"/>
  </bean>
  <mvc:interceptors>
      <bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor">
         <property name="paramName" value="lang"/>
      </bean>
  </mvc:interceptors>
  ```
* Thymeleaf使用
  1. 页面中使用方法
     `${@myBean.doSomething()}`
     
  2. 页面作用域
     ```
     Thymeleaf context or request:${x}
     request parameter:${param.x}
         ${param.size()}
         ${param.isEmpty()}
         ${param.containsKey('foo')}
     
     session:${session.x}
     servlet context:${application.x}
     ```
     
  3. 表达式
     ```
     Variable Expressions:${...}
     Selection Variable Expressions:*{...}
     Message Expressions:#{...}
     Link URL Expressions:@{...}
     Fragment Expressions:~{...}
     Format Expressions:${{...}}、*{{...}}
     Literal substitutions:|The name is ${name}|
     Preprocessing Expressions:__${...}__
                               *{rows[__${rowStat.index}__].variety}
     ```
     
  4. 隐士对象
  
     ```
     context object:#ctx
     -- IContext
        ${#ctx.locale}
        ${#ctx.variableNames}
     -- IWebContext
        ${#ctx.requset}
        ${#ctx.response}
        ${#ctx.session}
        ${#ctx.servletContext}
     
     context variables:#vars
     context locale:#locale
     HttpServletRequest:#request
         ${#request.getAttribute('foo')}
         ${#request.getParameter('foo')}
         ${#request.getContextPath()}
         ${#request.getRequestName()}
     HttpServletResponse:#response
         
     HttpSession:#session
         ${#session.getAttribute('foo')}
         ${#session.id}
         ${#session.lastAccessedTime}
     ServletContext:#servletContext
         ${#servletContext.getAttribute('foo')}
         ${#servletContext.contextPath}
         
     Execution Info:
         ${#execInfo.templateName}
         ${#execInfo.templateMode}
         ${#execInfo.processedTemplateName}
         ${#execInfo.processedTemplateMode}
         ${#execInfo.templateNames}
         ${#execInfo.templateModes}
         ${#execInfo.templateStack}
     ```
     
  5. 实用对象
     
     ```
     #execInfo:infomation about the template being processed
     #messages:methods for obtaining externalized messages inside variables expressions,in the same way as they would be obtained using #{...} syntax
     #uris:methods for escaping parts of URLs/URIs
     #conversions:methods for executing the configured conversion service
     #dates:methods for java.util.Date objects
     #calendars:analogous to #dates,but for java.util.Calendar objects
     #numbers:methods for formatting numberic objects
     #strings:methods for String objects:contains,startsWith,prepending/appending
     #objects:methods for objects in general
     #bools:methods for boolean evaluation
     #arrays:methods for arrays
     #lists:methods for lists
     #sets:methods for sets
     #maps:methods for lists
     #aggregates:methods for creating aggregates on arrays or collections
     #ids:methods for dealing with id attributes that might be repeated(for example,as a result an iteration)
     #conversions:#conversions.convert(Object,class)
                  <p th:text="${'val'+#conversions.convert(val,'String')}">...</p>
     ```
     
  6. Attribute
     
     ```
     th:alt-title
     th:lang-xmllang
     th:attrprepend
     th:attrappend
     th:classappend
     th:styleappend
     ```
     
  7. th:if|th:unless
    
     ```
     express "true" as following these rules:
     if value is not null
     1.if value is a boolean and is true
     2.if value is a number and is non-zero
     3.if value is a character and is non-zero
     4.if value is a String and is not "false","off" or "no"
     5.if value is not a boolean,a number,a character or a String
     express "false" as follwing these rules:
     1.if value is null,th:if will evaluate to false
     ```
     
  8. th:switch
  默认选项default:`th:case="*"`
     
  9. No-operation token
     `<div class="111" th:text="${null}?:_">No user authenticated</div>`
  
  10. th:remove
  
      * all: Remove both the containing tag and all its children
  
      * body: Do not remove the containing tag,but remove all its children
  
      * tag: Remove the containing tag,but do not remove its children
  
      * all-but-first: Remove all children of the containing tag except the first one
  
      * none: Do nothing 
      
  11. Attribute Precedence
      
      | Order |              Attributes              |
      | :---: | :----------------------------------: |
      |   1   |        th:insert,th:replace,         |
      |   2   |               th:each                |
      |   3   |  th:if,th:unless,th:switch,th:case   |
      |   5   | th:attr,th:attrprepend,th:attrappend |
      |   6   |      th:value,th:href,th:src...      |
      |   7   |           th:text,th:utext           |
      |   8   |             th:fragment              |
      |   9   |              th:remove               |
      
  12. Comments
      
      ```
      <!--/* This code will be remove at Thymeleaf parsing time! */-->
      静态文件:存在 解析后:不存在
      
      <!--/*-->
      <div>
          you can see me only before Thymeleaf processes me!
      </div>
      <!--*/-->
      静态文件:存在显示 解析后:不存在不显示
      
      <span>hello!</span>
      <!--/*/
      <div th:text="${value1}">Comments</div>
      /*/-->
      <span>goodbye!</span>
      静态文件:存在不显示 解析后:存在显示
      
      JAVASCRIPT and CSS template mode comment
      /*[+
      	var msg = "This is working application";
      +]*/
      静态文件:存在不显示 解析后:存在显示
      /*[-
      	var msg = "This is working application";
      -]*/
      静态文件:存在不显示 解析后:不存在不显示
      ```
      
  13. Inline expression
      ```
      [[${...}]]相当于th:text
      [(${...})]相当于th:utext
      ```
      禁用:`<p th:inline="none">As double array looks like this:[[1,2,3]],[(4,5)]!</p>`
      启用:`<p th:inline="text">As double array looks like this:[[1,2,3]],[(4,5)]!</p>`
      
  14. Text Mode
      
      ```
      [#th:block th:each="item : ${items}"]
       - [#th:block th:utext="${item}" /]
      [/th:block]
      -- 简写形式
      [# th:each="item : ${items}"]
       - [# th:utext="${item}" /]
      [/]
      ```
      
      
      
      
  

