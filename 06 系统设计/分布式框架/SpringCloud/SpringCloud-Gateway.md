 <center><h1><b><font color='gold'>SpringCloud-Gateway</font></b></h1></center>

# 一.概述

>1.网关旨在为微服务架构提供一种简单而有效的统一的API路由管理方式。
>
>2.网关就是系统的入口,为系统提供了一个统一的访问路径,除此之外,一些与业务本身功能无关的公共逻辑可以在这里实现，诸如认证、鉴权、监控、缓存、负载均衡、流量管控、路由转发等

# 二.Gateway环境搭建

## 1.前期开发环境搭建

>step0:构建基础的Eureka架构

## 2.搭建Gateway服务端

>step1:单独构建一个Eureka客户端模块作为Gateway服务模块,导入Gateway相关依赖

```xml
 <dependencies>
        <!--引入gateway 网关-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!-- eureka-client -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
</dependencies>
```

>~~step2:编写启动类,只需要启动Eureka服务即可~~

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class GateWayApp {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApp.class,args);
    }
}
```

>step3:编写配置文件

```yml
# 通用配置
server:
  port: 80 #端口号 ,推荐HTTP协议的默认端口80
spring:
  application:
    name: gateway-server #应用名称
  cloud:
  # 网关配置
    gateway:   
    
     	# 路由配置(集合)
      routes:
        - id: feign-consumer 	     # id: 唯一标识。默认是一个UUID         
          predicates:  			    # predicates: 断言,用于请求网关路径的匹配规则 (集合)
            - Path=/order/**
          filters: 				    # filters：配置局部过滤器 (集合)
            - StripPrefix=1  
          uri: lb://feign-consumer   # uri: 转发路径    注意:这里配置的是动态路由
          
      	# 微服务名称配置(配置后,是否添加应用名都不影响访问)
      discovery:  
        locator:
          enabled: true 			 # 设置为true 请求路径前可以添加微服务名称
          lower-case-service-id: true # 允许为小写
        # 跨域处理
      globalcors:
        cors-configurations:
          '[/**]': # 匹配所有请求
            allowedOrigins: "*" #跨域处理 允许所有的域
            allowedMethods: # 支持的方法
              - GET
              - POST
              - PUT
              - DELETE
          
# eureka客户端配置
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

```

>step4:启动测试
>
>GateWay的搭建只需要构建一个GateWay的服务端即可,无需配置其他模块,他在微服务的架构中起到了路由和过滤器的作用,其底层集成了Ribbon,可以实现负载均衡,GateWay可以搭建集群防止其单点故障,再在其前面构建一层Nginx进行负载均衡,由于Nginx的性能比较好,其故障率远低于GateWay,所以Nginx只需要单点布置即可,当然,为了不将地址写死,网关也会注册到Eureka中,从而解除网关与其他模块的耦合

## 3.扩展-自定义全局过滤器

>step1:定义类实现 GlobalFilter 和 Ordered接口复写方法完成逻辑处理

```java
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MyFilter implements GlobalFilter, Ordered {
    /**
     *  过滤器业务逻辑
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //exchange 一个上下文对象,可以获取request ,response等
        //ServerHttpRequest request = exchange.getRequest();
        //ServerHttpResponse response = exchange.getResponse();
        System.out.println("过滤器执行....");
        return chain.filter(exchange);//放行
    }

    /**
     *  过滤器执行优先级
     * @return 数值越小,优先级越高
     */

    @Override
    public int getOrder() {
        return 0;
    }
}
```

>step2:启动,测试

# 三.GateWay限流处理(令牌桶)

>step1:导入Redis依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
    <version>2.1.3.RELEASE</version>
</dependency>
```

>step2:编写配置类

```java
package com.changgou.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //针对IP地址进行一个限制
                return Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
            }
        };
    }
}
```

>step3:编写配置文件

```yml
server:
  port: 9101
spring:
  application:
    name: sysgateway
  cloud:
    gateway:
      routes:
        - id: goods
          uri: lb://goods
          predicates:
            - Path=/goods/**
          filters:
            - StripPrefix= 1
            - name: RequestRateLimiter #请求数限流 名字不能随便写-----------------------------------------新增
              args:
                key-resolver: "#{@ipKeyResolver}"
                redis-rate-limiter.replenishRate: 1 #令牌桶每秒填充平均速率
                redis-rate-limiter.burstCapacity: 1 #令牌桶总容量
        - id: system
          uri: lb://system
          predicates:
            - Path=/system/**
          filters:
            - StripPrefix= 1

      globalcors:
        cors-configurations:
          '[/**]': # 匹配所有请求
            allowedOrigins: "*" #跨域处理 允许所有的域
            allowedMethods: # 支持的方法
              - GET
              - POST
              - PUT
              - DELETE
  # 配置Redis 127.0.0.1可以省略配置--------------------------------------------------------------新增
  redis:
    host: 192.168.200.128
    port: 6379

eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:6868/eureka
  instance:
    prefer-ip-address: true
```

>4.测试,1s内多次访问,会返回429响应码