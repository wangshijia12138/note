 <center><h1><b><font color='gold'>SpringBoot</font></b></h1></center>

# 一.概述

## 1.什么是SpringBoot

>Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
>
>We take an opinionated view of the Spring platform and third-party  libraries so you can get started with minimum fuss. Most 
>
>Spring Boot  applications need minimal Spring configuration.
>
>Spring Boot使得创建可以“直接运行”的独立的、生产级的基于Spring的应用程序变得很容易。
>
>我们对Spring平台和第三方库有自己的看法，这样您就可以从零开始。
>
>大多数Spring引导应用程序需要最小的Spring配置。

## 2.为什么要使用SpringBoot

>早期,我们在使用Spring开发时,我们可以基于一种规范的框架去开发软件,人们只需要进行配置,而不需要再去手动实现一些功能,极大的提高了我们的开发效率,而随着Spring的发展,其涉及的领域越来越多,其**配置的复杂度也越来越大**,被人们称作"配置地狱",从另一方面来说,Spring并没有对Jar包的管理进行优化,在中大型项目中,**Jar包的管理将变得十分麻烦**,SpringBoot就是在这种背景下应运而生,开发他的目的就是解决以上问题,进一步简化我们的开发流程.
>
>SpringBoot基于**约定大于配置**的基本思想,开发出了**自动配置**和**起步依赖**两大核心功能,通过自动配置降低了配置的复杂度,通过起步依赖优化了Jar包的管理.从本质上讲,SpringBoot并不属于一个新的技术,他并没有在Spring基础上进行功能的扩展,而是极大的简化了Spring的开发,从而进一步提高了我们的开发效率,我们可以把他理解为一个简化版的Spring 或者 是一种Spring 简化使用方式
>
>一般微服务都会基于SpringBoot进行构建,但企业中如果没有使用微服务,也可能使用SpringMVC-SpingBoot-Mybatis进行开发

## 3.SpringBoot项目的基本结构

### ①Pom文件(父依赖和启动器)

```xml
<!-- 父依赖 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.5.RELEASE</version>
    <relativePath/>
</parent>

<dependencies>
    <!-- web场景启动器 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- springboot单元测试 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <!-- 剔除依赖 -->
        <exclusions>
            <exclusion>
                <groupId>org.junit.vintage</groupId>
                <artifactId>junit-vintage-engine</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- 打包插件 -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

### ②引导类

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### ③测试文件

```java
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }
  
```

## 

# 三.SpringBoot的配置

## 1.配置文件的作用

>SpringBoot本身是约定大于配置的,底层为我们进行了大量的自动配置,当我们需要修改这些默认配置时,就需要编写配置文件

## 2.配置文件的基本规范

>1.SpringBoot只支持application.properties或application.yml/application.yaml格式的配置文件,
>
>2.配置文件名称默认是application
>
>3.配置文件的读取顺序是application.properties>application.yml>application.yaml

## 3.配置文件的切换

>在企业中,开发,测试,生产,各自的配置文件都各不相同

