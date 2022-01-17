 <center><h1><b><font color='grey'>Spring配置详解</font></b></h1></center>

# 核心一:Spring-IOC

## 1.xml方式

```xml
	<!-- UserDaoImpl配置-->
    <bean id="userDao" class="cn.wangshijia.demo2.UserDaoImpl" 
          init-method="init" destroy-method="destroy"></bean>

	<!-- User配置-->
    <bean id="user1" class="cn.wangshijia.bean.User">
        <property name="name" value="xioaming"></property>
        <property name="age" value="13"></property>
    </bean>
    <bean id="user2" class="cn.wangshijia.bean.User">
        <property name="name" value="tom"></property>
        <property name="age" value="23"></property>
    </bean>


	<!-- UserServiceImpl配置-->
    <bean id="userService" class="cn.wangshijia.demo2.UserServiceImpl">
        <!-- 引用类型注入-->
        <property name="userDao" ref="userDao"></property>
        <!-- 普通类型注入-->
        <property name="name" value="zhangsan"></property>
		
        <!-- List<普通类型>注入-->
        <property name="list1">
            <list>
                <value>小明</value>
                <value>小红</value>
            </list>
        </property>
 		<!-- List<引用类型>注入-->
        <property name="list2">
            <list>
                <ref bean="user1"></ref>
                <ref bean="user2"></ref>
            </list>
        </property>
		 <!-- map类型注入-->
        <property name="map">
            <map>
                <entry key="1" value-ref="user1"></entry>
                <entry key="2" value-ref="user2"></entry>
            </map>
        </property>
		<!-- properties注入-->
        <property name="pros">
            <props>
                <prop key="user1">Jerry</prop>
                <prop key="user2">lucy</prop>
            </props>
        </property>
    </bean>

<!-- 创建细节 -->
 <!--
 * 1) 对象创建： 单例/多例
 * scope="singleton", 默认值， 即 默认是单例 【service/dao/⼯具类】
 * scope="prototype", 多例； 【Action对象】

 * 2) 什么时候创建?
 * scope="prototype" 在⽤到对象的时候，才创建对象。
 * scope="singleton" 在启动(容器初始化之前)， 就已经创建了bean，且整个应⽤只有⼀个。

 * 3)是否延迟创建
 * lazy-init="false" 默认为false, 不延迟创建，即在启动时候就创建对象
 * lazy-init="true" 延迟初始化， 在⽤到对象的时候才创建对象（只对单例有效）

 * 4) 创建对象之后，初始化/销毁
 * init-method="init_user" 【对应对象的init_user⽅法，在对象创建之后执⾏】
 * destroy-method="destroy_user" 【在调⽤容器对象的destroy⽅法时候执⾏，(容器⽤实现类)】
-->

```

## 2.注解方式

+ application.xml文件中

```xml
1.
<!-- 引入context命名空间 -->
2.
<!-- 扫描注解信息-->
<context:component-scan base-package="cn.wangshijia.demo2"></context:component-scan>
```

+ Java代码中

| 注解           | 说明                                           |
| -------------- | ---------------------------------------------- |
| @Component     | 使用在类上用于实例化Bean                       |
| @Controller    | 使用在web层类上用于实例化Bean                  |
| @Service       | 使用在service层类上用于实例化Bean              |
| @Repository    | 使用在dao层类上用于实例化Bean                  |
| @Autowired     | 使用在字段上用于根据类型依赖注入               |
| @Qualifier     | 结合@Autowired一起使用用于根据名称进行依赖注入 |
| @Resource      | 相当于@Autowired+@Qualifier，按照名称进行注入  |
| @Value         | 注入普通属性                                   |
| @Scope         | 标注Bean的作用范围                             |
| @PostConstruct | 使用在方法上标注该方法是Bean的初始化方法       |
| @PreDestroy    | 使用在方法上标注该方法是Bean的销毁方法         |

## 3.JavaConfig方式

```java
@Configuration      //指定该类为Spring的一个配置类
@ComponentScan("cn.wangshijia.demo2") //扫描包
public class JavaConfig {
    //将方法的返回的bean配置到IOC容器中,ID默认为方法名
    @Bean
    public User getUser(){
        User user = new User();
        user.setAge(13);
        user.setName("jerry");
        return user;
    }
}
```
※补充

1.如何解决配置文件内容过多,不易维护的问题

```txt
一般我们可以将配置文件分开,分为主配置文件和其他分配置文件
⭐如果XML的配置⽂件是分散的，我们也是创建⼀个更⾼级的配置⽂件（root），然后使⽤ 
<import resource="applicationContext-xxx.xml"/> 来将配置⽂件组合
⭐如果JavaConfig的配置类是分散的，我们⼀般再创建⼀个更⾼级的配置类（root），然后
使⽤@Import({DataSourceConfiguration.class})来将配置类进⾏组合
```

2.Xml,注解,JavaConfig都可以配置,我们如何取舍

```txt
XML,注解,JavaConfig可以混合使用,且可以互相引用
⭐在JavaConfig引⽤XML : 使⽤@ImportResource()
⭐在XML引⽤JavaConfig : 使⽤ <bean> 节点就⾏了

一般情况下,我们使用注解+Xml的形式
```

# 核心二:Spring-AOP

## 1.注解方式

+ application.xml文件

  ```xml
  <!-- 扫描注解信息-->
      <context:component-scan base-package="cn.wangshijia.demo3"></context:component-scan>
  <!-- 开启aspectj 自动代理 -->
      <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
  ```

+ Java代码

```java
@Component
@Aspect//配置切面类
public class myAspect {

    //切面表达式抽取
    @Pointcut("execution(* cn.wangshijia.demo3.TargetImpl.*(..))")
    public void myPointcut() {
    }

    //前置增强
	@Before("myPointcut()")
    public void before() {
        System.out.println("前置增强");
    }

    //后置增强
	@AfterReturning("myPointcut()")
    public void afterReturning() {
        System.out.println("后置增强");
    }
    
    
    //环绕增强,注意:环绕增强有特定的格式
    @Around("myPointcut()")
    public Object Around(ProceedingJoinPoint pj) {
        Object proceed = null;
       //获取目标对象的方法名
        Signature signature = pj.getSignature();
        System.out.println("signature.getName();"+signature.getName());
 
        try {
            before();
            proceed = pj.proceed();
            afterReturning();
        } catch (Throwable throwable) {
            error();
            throwable.printStackTrace();
        } finally {
            after();
        }
        return proceed;
    }

    //异常抛出增强
	@AfterThrowing("myPointcut()")
    public void error() {
        System.out.println("异常抛出增强");
    }

    //最终增强
	@After("myPointcut()")
    public void after() {
        System.out.println("最终增强");
    }
}

```

## 2.xml方式

```xml
<!--将目标类和切面类放入IOC容器-->
    <bean id="target" class="cn.wangshijia.demo3.TargetImpl"></bean>
    <bean id="myAspect" class="cn.wangshijia.demo3.MyAspect"></bean>

<!--织入关系配置-->
<aop:config>
    <!--指定切面类-->
    <aop:aspect ref="myAspect">
        <!--切面表达式抽取 -->
        <aop:pointcut id="myPointcut" expression="execution(* cn.wangshijia.demo3.TargetImpl.(..))"/>
        <!--前置增强 -->
        <aop:before method="before" pointcut-ref="myPointcut"></aop:before>
        <!--后置增强 -->
        <aop:after-returning method="afterReturning" pointcut-ref="myPointcut"></aop:after-returning>
        <!--异常抛出增强 -->
        <aop:after-throwing method="error" pointcut-ref="myPointcut"></aop:after-throwing>
        <!--最终增强 -->
        <aop:after method="after" pointcut-ref="myPointcut"></aop:after>
        <!--环绕增强 -->
        <aop:around method="Around" pointcut-ref="myPointcut"></aop:around>
    </aop:aspect>
</aop:config>

```

# ※Spring-Junit

## step1:导包

```xml
<dependency>
<groupId>junit</groupId>
<artifactId>junit</artifactId>
<version>4.12</version>
</dependency>
```

## step2:编写Test类

```java
@RunWith(SpringJUnit4ClassRunner.class)//将代码交给Spring,Spring负责调用Junit进行单元测试
//@ContextConfiguration(classes = JavaConfig.class)//加载JavaConfig配置文件
@ContextConfiguration(locations = "classpath*:spring-dao.xml")//加载xml配置文件
public class MyTest {
    /*
    Spring可以集成Junit,方便了我们进行单元测试
    需要用到的Bean对象,可以直接从容器中取出,进行测试
    */
    //注入对象
    @Autowired
    private UserService userService;
    
    //测试
    @Test
    public void method1() {
        userService.testUserService();
    }
}
```

# ※Spring-JDBC

## step1:导包

## step2:配置数据源

```xml
<!--......引入context名称空间......-->

<!--加载外部的properties文件-->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!--Druid数据源的配置-->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

# ※Spring-Transaction

## step1:导包

## step2:applicationContext.xml相关

```xml
<!-- 扫描包-->
<context:component-scan base-package="cn.wangshijia.demo4"/>

<!-- 启动spring事务注解驱动-->
<tx:annotation-driven transaction-manager="transactionManager"/>

<!--加载外部的properties文件-->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!--Druid数据源的配置-->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!-- 配置事务管理对象 -->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

```

## step3:.java文件中注解相关

```java
@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    @Transactional//事务注解,可以加在类上,代表类中所有方法都有事务控制
    public void transfer() {
        accountDao.updateBalance("tom", -500);
        accountDao.updateBalance("lucy", 500);
    }

}
```

# ※Spring-Schedule

## step1:导包

注:Spring3.0以后自带的task，即：spring schedule，可以将它看成一个轻量级的Quartz，而且使用起来比Quartz简单许多。

## step2:Spring-Schedule.xml相关配置

```xml
   <!-- 
	配置处理定时任务的线程池
    task:executor ，是为了某个任务如果要异步的执行时，实现当前任务内的多线程并发。
    -->
    <task:executor id="executor" pool-size="10"/>

    <!--  
	配置处理 异步定时任务的线程池
     task:scheduler 参数的线程池，是为了根据任务总数来分配调度线程池的大小
    -->
    <task:scheduler id="scheduler" pool-size="10"/>

    <!-- 启用annotation方式 -->
    <task:annotation-driven scheduler="scheduler" executor="executor" proxy-target-class="true"/>
```

## step3.java文件中注解相关

```java
@Component
public class Spring_Task {
    @Scheduled(cron="0/1 * *  * * ? ")//cron意为记时系统,后面是cron表达式,有一套独立的语法
    public void task(){
        System.out.println("------------Task 执行了--------");
    }
}
```

# ※Spring-Security

## Step1.导包

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-web</artifactId>
    <version>5.0.5.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-config</artifactId>
    <version>5.0.5.RELEASE</version>
</dependency>
```

## Step2.web.xml相关配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <filter>
        <!--
          DelegatingFilterProxy用于整合第三方框架
          整合Spring Security时过滤器的名称必须为springSecurityFilterChain，
          否则会抛出NoSuchBeanDefinitionException异常
        -->
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <servlet>
        <servlet-name>spring-mvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 指定加载的配置文件 ，通过参数contextConfigLocation加载 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-security.xml</param-value>
        </init-param>
        <!-- 在使用DelegatingFilterProxy 整合第三方框架时,必须设置启动时加载,否则会报错    -->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>spring-mvc</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>
</web-app>
```

## Step3.spring-security.xml相关配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:security="http://www.springframework.org/schema/security"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
							http://www.springframework.org/schema/beans/spring-beans.xsd
							http://www.springframework.org/schema/mvc
							http://www.springframework.org/schema/mvc/spring-mvc.xsd
							http://www.springframework.org/schema/context
							http://www.springframework.org/schema/context/spring-context.xsd
                          http://www.springframework.org/schema/security
                          http://www.springframework.org/schema/security/spring-security.xsd">

    <!--
        http：用于定义相关权限控制
        指定哪些资源不需要进行权限校验，可以使用通配符
     -->
    <security:http security="none" pattern="/login.html"/>

    <!--
        http：用于定义相关权限控制
        auto-config:自动配置,设置为true时,会默认提供登录登出页面
        use-expressions：用于指定intercept-url中的access属性是否使用表达式
     -->
    <security:http auto-config="true" use-expressions="true">
         <security:headers>
            <!--设置在页面可以通过iframe访问受保护的页面，默认为不允许访问-->
            <security:frame-options policy="SAMEORIGIN"/>
        </security:headers>
        
        <!--
            intercept-url：定义一个拦截规则
            pattern：对哪些url进行权限控制
            access：在请求对应的URL时需要什么权限，默认配置时它应该是一个以逗号分隔的角色列表，
                                    请求的用户只需拥有其中的一个角色就能成功访问对应的URL
            isAuthenticated()：已经经过认证（不是匿名用户）
        -->
        <!-- isAuthenticated() 只要认证通过就可以访问-->
        <security:intercept-url pattern="/pages/a.html" access="isAuthenticated()"/>
        <security:intercept-url pattern="/index.html" access="isAuthenticated()"/>
        <!--hasAnyAuthority('ADD') 拥有add权限就可以访问b.html页面-->
        <security:intercept-url pattern="/pages/b.html" access="hasAnyAuthority('ADD')"/>
        <!--hasRole('ADMIN') 拥有ROLE_ADMIN角色就可以访问c.html页面-->
        <security:intercept-url pattern="/pages/c.html" access="hasRole('ADMIN')"/>
        <!--hasRole('ADMIN') 拥有ROLE_ADMIN角色就可以访问d.html页面(与上等价)-->
        <security:intercept-url pattern="/pages/d.html" access="hasRole('ROLE_ADMIN')"/>
        <!--
            form-login：自定义表单登录信息
            login-page:登录页面(自定义页面)
            username-parameter:表单中用户名参数
            password-parameter:表单中密码参数
            login-processing-url:登录处理页面(表单action路径)
            default-target-url:默认目标路径(登录成功后跳转的页面)
            authentication-failure-url:认证失败页面(登录失败后跳转的页面)
        -->
        <security:form-login login-page="/login.html"
                             username-parameter="username"
                             password-parameter="password"
                             login-processing-url="/login.do"
                             default-target-url="/index.html"
                             authentication-failure-url="/login.html"/>
        <!--关闭CsrfFilter过滤器,如果使用自定义登录页面需要关闭此项，否则登录操作会被禁用（403)-->
        <security:csrf disabled="true"/>
        <!--
            logout：退出登录
            logout-url：退出登录操作对应的请求路径
            logout-success-url：退出登录后的跳转页面
            invalidate-session:是否销毁session
        -->
        <security:logout logout-url="/logout.do" logout-success-url="/login.html" invalidate-session="true"/>
    </security:http>

    <!--配置密码加密对象 这里使用bcrypt加密-->
    <bean id="passwordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"/>
    <!--配置认证逻辑类-->
    <bean id="demo2" class="cn.wangshijia.controller.demo2"/>
    
    <!--authentication-manager：认证管理器，用于处理认证操作-->
    <security:authentication-manager>
        <!--
            authentication-provider：认证提供者，执行具体的认证逻辑
            user-service-ref:引用认证逻辑类
        -->
        <security:authentication-provider user-service-ref="demo2">
            <!--指定密码加密策略-->
            <security:password-encoder ref="passwordEncoder"/>
        </security:authentication-provider>
    </security:authentication-manager>

    <!--开启注解方式权限控制-->
    <security:global-method-security pre-post-annotations="enabled"/>
</beans>

```

## 扩展1.UserDetailsService实现类

```java
//step1:必须继承UserDetailsService类
public class demo2 implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private HashMap<String, User> map;
    public void init() {
        map = new HashMap<String, User>();
        User user1 = new User();
        user1.setUsername("root");
        user1.setPassword(passwordEncoder.encode("root1"));
        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword(passwordEncoder.encode("1231"));
        map.put("root", user1);
        map.put("xiaoming", user2);
    }
    
    //step2:必须实现loadUserByUsername方法
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //step3: 根据用户输入的username,从数据库中查询用户信息
        init();
        User user = map.get(username);
        if (user == null) {
            //查询失败时,返回null,代表认证失败
            return null;
        }
        //step4:new一个List<GrantedAuthority>,并在其中添加权限
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        if (username.equals("root")) {
            //注意:这里的权限与配置同统一,一般情况下,有ROLE前缀的为角色,否则为权限
            list.add(new SimpleGrantedAuthority("ADD"));
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        //step5:将查询的用户名,密码,权限集合封装在User对象中,返回给框架,框架会自动完成用户的认证和授权
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
    }
}

```

## 扩展2.通过注解进行权限控制

```java
package cn.wangshijia.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    //注意:使用注解的前提是配置中开启了权限注解驱动
    //相比xml,通过注解方式控制权限,权限的控制范围可以精确到方法级别
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADD')")//只有拥有ADD权限的用户,才可以调用该方法
    public String add() {
        System.out.println("add....");
        return "success";
    }
}

```

## 扩展3.获取认证通过的用户信息

```java
package cn.wangshijia.controller;

import cn.wangshijia.constant.MessageConstant;
import cn.wangshijia.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUsername")
    public Result getUsername() {
        //注意:用户在认证通过后,框架会将用户信息存储在session中,可以通过以下方式取出(这里的User对象为security内置)
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
             //User对象中封装了用户的用户名,密码,权限信息
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }

}

```

