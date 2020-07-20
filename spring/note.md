* 使用注解@Aspect方式配置切面则需要在`xml`文件中配置下面一行开启AOP
  `<aop:aspectj-autoproxy />`
  如果我们希望只使用CGLib实现AOP,则可以如下进行配置:

  ```
  # xml文件配置方式
  <aop:aspectj-autoproxy proxy-target-class="true"/>
  
  # 注解方式
  @EnableAspectJAutoProxy(proxyTargetClass = true)
  ```

* Scope常量

  ```
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  ```

* 获取当前的代理类
  `((LoginDaoImpl)AopContext.currentProxy()).findUser();`

* 定时任务
  默认情况下,Spring 将会生成一个单线程ScheduledExecutorService执行定时任务。所以一旦某一个定时任务长时间阻塞这个执行线程,其他定时任务都将被影响,没有机会被执行线程执行

  ```
  <task:annotation-driven  scheduler="myScheduler"/>
  <task:scheduler id="myScheduler" pool-size="10"/>
  
  spring.task.scheduling.pool.size=10
  spring.task.scheduling.thread-name-prefix=task-test
  
  通过上面的配置,Spring将会使用TaskScheduler子类ThreadPoolTaskScheduler,内部线程数为pool-size数量,这个线程数将会直接设置ScheduledExecutorService线程数量。
  ```

* 使用SpEL表达式

  ```jsp
  <%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
  
  <s:eval expression="T(com.post.common.util.MailOrderUtil).getMailStatus(mailOrder.mailStatus)" var="mailStatus" />
  ${mailStatus }
  ```

* static成员注入方式

  1. 方式一:以set方法为跳板

     ```java
     @Component
     public class UserHelper {
         static UCClient ucClient;
     
         @Autowired
         public void setUcClient(UCClient ucClient) {
             UserHelper.ucClient = ucClient;
         }
     }
     ```

  2. 方式二:使用@PostConstruct注解,在里面为static静态成员赋值

     ```java
     @Component
     public class UserHelper {
         static UCClient ucClient;
     
         @Autowired
         ApplicationContext applicationContext;
         
         @PostConstruct
         public void init() {
             UserHelper.ucClient = applicationContext.getBean(UCClient.class);
         }
     }
     ```

  3. 方式三:

     ```java
     @Component
     public class AutowireStaticSmartInitializingSingleton implements SmartInitializingSingleton {
         @Autowired
         private AutowireCapableBeanFactory beanFactory;
     
         /**
          * 当所有的单例Bena初始化完成后，对static静态成员进行赋值
          */
         @Override
         public void afterSingletonsInstantiated() {
             // 因为是给static静态属性赋值，因此这里new一个实例做注入是可行的
             beanFactory.autowireBean(new UserHelper());
         }
     }
     ```

     ```java
     @Component
     public class AutowireStaticSmartInitializingSingleton implements SmartInitializingSingleton {
         @Autowired
         private AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor;
         
         @Override
         public void afterSingletonsInstantiated() {
             autowiredAnnotationBeanPostProcessor.processInjection(new UserHelper());
         }
     }
     ```

     

