 <center><h1><b><font color='grey'>SpringMVC配置详解</font></b></h1></center>

# 一、Tomcat配置(web.xml)

## 1. Servlet配置(DispatcherServlet )

```xml
<!-- 前端控制器DispatcherServlet 基本配置(SpringMVC内置) -->
<servlet>
    <servlet-name>DispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!--加载配置文件 -->
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
    <!--加载时机:正数表示Tomcat启动时加载控制器对象,数字越小,启动越早-->
    <load-on-startup>1</load-on-startup>
</servlet>
<!-- 核心控制器DispatcherServlet 拦截路径-->
<servlet-mapping>
    <servlet-name>DispatcherServlet</servlet-name>
    <!-- / 并不是真正意义上的拦截所有请求，它不会拦截jsp的页面请求
         /* 其他的请求则会拦截才是真正意义上的拦截所有请求-->
    <url-pattern>/</url-pattern>
</servlet-mapping>

```

## 2. Filter 配置(处理中文乱码)

​	CharacterEncodingFilter(编码过滤器)

```xml
<!--配置全局过滤的filter - 解决请求参数中文乱码问题(SpringMVC内置)-->
<filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <!-- SpringMVC 内置了编码过滤器,可以直接使用-->
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <!--拦截路径设为拦截所有请求-->
    <url-pattern>/*</url-pattern>
</filter-mapping>

```

## 3. Listener配置(Spring的集成)

```xml
<!--Tomcat启动时,会先生成Spring容器,再生成SpringMVC子容器 , SpringMVC容器是基于Spring容器生成的,所以SpringMVC容器中可以直接使用父容器内的Bean-->
<!--全局参数-->
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:applicationContext.xml</param-value>
</context-param>
<!--Spring的监听器-->
<listener>
	<listener-class>
        <!--spring web模块提供的监听类-->
       org.springframework.web.context.ContextLoaderListener
   </listener-class>
 </listener>

```

# 二、基础组件配置

## 1.处理器映射器

```xml
 <!--
   支持mvc注解驱动
   自动注册DefaultAnnotationHandlerMapping,它会处理@RequestMapping 注解，并将其注册到请求映射表中。
 -->
<mvc:annotation-driven/>
```

## 2.处理器适配器

```xml
 <!--
   支持mvc注解驱动
   RequestMappingHandlerAdapter，则是处理请求的适配器，确定调用哪个类的哪个方法，并且构造方法参数，返回值。
 -->
<mvc:annotation-driven/>
```

## 3.视图解析器

①自定义配置

```xml
<!--视图解析器-->
<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <!-- 视图路径 = http://localhost/SpringMVC/+前缀+逻辑视图名+后缀-->
    <!--视图路径前缀-->
    <property name="prefix" value="/WEB-INF/jsp/"/>
    <!--视图路径后缀-->
    <property name="suffix" value=".jsp"/>
</bean>
```

②默认配置

```java
public class UrlBasedViewResolver extends AbstractCachingViewResolver implements Ordered {
	//重定向前缀:"redirect:"
    public static final String REDIRECT_URL_PREFIX = "redirect:";
    //请求转发前缀"forward:"(默认)
    public static final String FORWARD_URL_PREFIX = "forward:"; 
    //视图路径前缀 ""
    private String prefix = "";
    //视图路径后缀 ""
    private String suffix = "";
    
    ......
}
```

## 4.IOC容器

```xml
<!--扫描注解,将控制器存入IOC容器中-->
<context:component-scan base-package="cn.wangshijia.controller"/>
```



# 三、扩展组件配置

## 1.JSON 的自动转化

手动配置

```xml
<!--通过SpringMVC帮助我们对对象或集合进行json字符串的转换并回写，为处理器适配器配置消息转换参数，指定使用jackson进行对象或集合的转换，因此需要在spring-mvc.xml中进行如下配置： -->
<!--详细版 -->
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
        <property name="messageConverters">
            <list>
 <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"/>
            </list>
        </property>
</bean>
```

自动配置

```xml
<!--自动注册适配器处理器时会自动配置Json的自动转换 -->
<mvc:annotation-driven/>
```

## 2.开启静态资源的访问

```xml
<!--解决静态资源访问报错问题-->
<!--由于DispatcherServlet本身配置了拦截除jsp外所有请求,所以在访问静态资源时,由于无对应映射,导致报错-->
<!-- 配置默认servlet处理器,在无对应映射时,将请求给到Tomcat,Tomcat会根据路径去找静态资源 -->
<mvc:default-servlet-handler/>
```

## 3.自定义类型转化器

实现接口

```java
//处理适配器中默认已经提供了一些常用的类型转换器，例如客户端提交的字符串转换成int型进行参数设置。
//但是不是所有的数据类型都提供了转换器，没有提供的就需要自定义转换器，例如：日期类型的数据就需要自定义转换器。
public class DateConverter implements Converter<String, Date> {
    //Converter<String, Date> String代表转化前的数据格式 Date代表转换后的数据格式
    public Date convert(String dateStr) {
        //将日期字符串转换成日期对象 并返回
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
```

配置Bean

```xml
<!-- 配置Bean -->
<bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <list>
            <bean class="com.itheima.converter.DateConverter"></bean>
        </list>
    </property>
</bean>
```

## 4.文件上传解析器

​	1.导包

​	2.配置解析器(SpringMVC提供)

```xml
<!--配置文件上传解析器-->
<!-- 配置id和class ,注意:id必须为multipartResolver    -->
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    <!-- 配置编码方式,避免中文乱码-->
    <property name="defaultEncoding" value="UTF-8"/>
    <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
    <property name="maxUploadSize" value="500000"/>
</bean>
```

​	3.使用

## 5.拦截器

实现接口

```java
public class MyInterceptor implements HandlerInterceptor {
    //预处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle....");
        return true;//true 代表放行 ,false代表阻止
    }

    //后处理
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //可以使用ModelAndView对象,修改视图信息
        System.out.println("postHandle....");
    }

    //最终处理
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //可以使用Exception信息,作异常处理
        System.out.println("afterCompletion....");
    }
}

```

配置拦截器bean

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <!--配置哪些资源进行拦截操作 /** 代表拦截所有-->
        <mvc:mapping path="/**"/>
        <!--配置哪些资源排除拦截操作-->
        <mvc:exclude-mapping path="/user/login"/>
        <!--配置拦截器-->
        <bean class="cn.wangshijia.interceptor.MyInterceptor"/>
    </mvc:interceptor>
    <mvc:interceptor>
        <mvc:mapping path="/**"/>
        <bean class="cn.wangshijia.interceptor.MyInterceptor2"/>
    </mvc:interceptor>
</mvc:interceptors>
```



## 6.异常处理器

### 1.简单异常处理器(内置)

```xml
<!--注意:SimpleMappingExceptionResolver由SpringMVC提供,无需自己编写-->
<!--简单异常处理器只能配置异常与页面的映射关系-->
<!--配置简单映射异常处理器-->
<bean class=“org.springframework.web.servlet.handler.SimpleMappingExceptionResolver”>    
    <property name=“defaultErrorView” value=“error”/>   <!--默认错误视图-->
    <property name=“exceptionMappings”>
        <map>			<!--异常类型-->	              <!--错误视图-->	           
            <entry key="com.itheima.exception.MyException" value="error"/>
            <entry key="java.lang.ClassCastException" value="error"/>
        </map>
    </property>
</bean>
```



### 2.自定义异常处理器

实现接口

```java
//自定义异常处理,除了可以实现页面跳转外,还可以进行更多业务逻辑操作
public class MyExceptionResolver implements HandlerExceptionResolver {
@Override
public ModelAndView resolveException(HttpServletRequest request, 
    HttpServletResponse response, Object handler, Exception ex) {
    //处理异常的代码实现
    //创建ModelAndView对象
    ModelAndView modelAndView = new ModelAndView(); 
    modelAndView.setViewName("exceptionPage");
    return modelAndView;
    }
}
```

配置Bean

```xml
<bean id="exceptionResolver"        
      class="com.itheima.exception.MyExceptionResolver"/>
```



# 四、处理器配置

## 1.数据回显

### ①页面跳转

```java
//页面跳转 -返回逻辑路径字符串
@RequestMapping("/test1")
public String test1() {
    System.out.println("test1.....");
    return "test";
}

//页面跳转 - 返回ModelAndView
@RequestMapping("/test2")
//ModelAndView也可以通过new自己创建
public ModelAndView test2(ModelAndView mv) {
    System.out.println("test2.....");
    //在request域中存储数据
    mv.addObject("username", "jack");
    //在mv中存储视图路径
    mv.setViewName("test");
    return mv;
}

//页面跳转 -返回Model
@RequestMapping("/test3")
//Model也可以通过new自己创建
public String test3(Model model) {
    System.out.println("test3.....");
    //在request域中存储数据
    model.addAttribute("username", "lucy");
    return "test";
}
```



### ②数据回写

```java
//回写数据 -返回字符串
@RequestMapping("/test4")
@ResponseBody //返回值不经过视图解析器,直接放到响应体中返回
public String test4() {
    System.out.println("test4.....");
    return "test4...";
}

//回写数据 -返回对象或集合(Json格式字符串)
@RequestMapping("/test5")
@ResponseBody //返回值不经过视图解析器,直接放到响应体中返回
public User test5() {
    System.out.println("test5.....");
    //前提:配置处理器适配器,导入Jackon坐标,框架会自动将对象转化为Json格式字符串
    return new User("wang", 18);
}
```

## 2.参数绑定

### ①参数绑定(基本)

```java
//请求-字面量类型
@RequestMapping("/test6")
@ResponseBody
//注意:@ResponseBody代表不进行页面跳转
//      void代表不进行数据回写
public void test6(String name,int age) {
    System.out.println("test6....");
    System.out.println(name);
    System.out.println(age);
    //注意:参数名一致,类型不一致时会进行自动转化,无法转化则会报错
}

//请求-POJO类型参数
@RequestMapping("/test7")
@ResponseBody
public void test7(User user) {
    System.out.println("test7....");
    System.out.println(user);
    //注意:参数名一致,类型不一致时会进行自动转化,无法转化则会报错
}

//请求-数组类型参数
@RequestMapping("/test8")
@ResponseBody
public void test8(String[] str) {
    System.out.println("test8....");
    System.out.println(Arrays.asList(str));
}

//请求-集合类型参数(方式一)
@RequestMapping("/test9")
@ResponseBody
//请求参数格式:userList[1].name: xxx&userList[1].age: 14
public void test9(Vo vo) {
    System.out.println(vo);
}

//请求-集合类型参数(方式二)
@RequestMapping("/test10")
@ResponseBody
//通过@requestBody可以将请求体中的JSON字符串绑定到相应的bean上，当然，也可以将其分别绑定到对应的字符串上。
//请求参数格式:[{"name": "jack", "age": 23}, {"name": "rose", "age": 25}]
public void test10(@RequestBody List<User> userList) {
    System.out.println(userList);
}
```

### ②参数绑定(细节处理)

```java
//当请求的参数名称与Controller的业务方法参数名称不一致时，就需要通过@RequestParam注解显示的绑定
@RequestMapping("/test11")
@ResponseBody
public void test11(@RequestParam("name")String username) {
System.out.println(username);
}


//使用@RequestHeader可以获得请求头信息，相当于web阶段学习的request.getHeader(name)
@RequestMapping(value="/quick20")
@ResponseBody
public void save20(@RequestHeader(value = "User-Agent",required = false) String user_agent) throws IOException {
    System.out.println(user_agent);
}

//使用@CookieValue可以获得指定Cookie的值
@RequestMapping(value="/quick21")
@ResponseBody
public void save21(@CookieValue(value = "JSESSIONID") String jsessionId) throws IOException {
    System.out.println(jsessionId);
}
```



### ③文件上传

#### 前端

```html
<!--
文件上传客户端表单需要满足：
1.表单项type=“file”
2.表单的提交方式是post
3.表单的enctype属性是多部分表单形式，及enctype=“multipart/form-data”
-->
<form action="SpringMVC/user/quick22" method="post" enctype="multipart/form-data">
        名称<input type="text" name="username"><br/>
        文件1<input type="file" name="uploadFile"><br/>
        <input type="submit" value="提交">
</form>
```

#### 文件解析器配置

```xml
<!--配置文件上传解析器-->
<!-- 配置id和class ,注意:id必须为multipartResolver    -->
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    <!-- 配置编码方式,避免中文乱码-->
    <property name="defaultEncoding" value="UTF-8"/>
    <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
    <property name="maxUploadSize" value="500000"/>
</bean>

```

#### 后端

```java
//单个文件上传
@RequestMapping("/test")
@ResponseBody
public void test(String name,MultipartFile file, HttpServletRequest request) throws IOException {
    System.out.println(name);
    //获取上传文件的原名
    String originalFilename = file.getOriginalFilename();
    //将上传文件保存到一个目录文件中
    file.transferTo(new File("D:\\"+ originalFilename));
}

//多个文件上传
@RequestMapping("/test1")
@ResponseBody
public void test1(String name,MultipartFile[] file, HttpServletRequest request) throws IOException {
    System.out.println(name);
    for (int i = 0; i < file.length; i++) {
        //获取上传文件的原名
        String originalFilename = file[i].getOriginalFilename();
        //将上传文件保存到一个目录文件中
        file[i].transferTo(new File("D:\\"+ originalFilename));
    }

}
```

