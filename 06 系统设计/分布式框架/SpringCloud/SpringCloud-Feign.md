 <center><h1><b><font color='gold'>SpringCloud-Feign </font></b></h1></center>

# 一.概述

>1.Feign是一种声明式、模板化的HTTP客户端
>
>2.Feign最初由Netflix公司提供,但不支持SpringMVC注解,后由SpirngCloud封装整合,编入了SpringCloud全家桶
>
>3.Feign整合了 Spring Cloud Ribbon 与 Spring Cloud Hystrix 两大基础框架,  除了整合这两者的强大功能之外，它还提
>供了声明式的服务调用(不再通过RestTemplate)。

# 二.Feign-简化Ribbon开发

## 1.服务调用开发流程

>step0:构建Eureka基本架构,包括注册中心,服务消费者和服务提供者,<font color='yellow'>以下操作都在服务消费者模块中进行</font>

>step1:导入Feign依赖

```xml
<!--feign-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

>step3:启用Feign的服务

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients //开启Feign的功能
@EnableEurekaClient
@SpringBootApplication
public class ConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class,args);
    }
}
```

>step4:编写配置文件(可省略)

```yml
# 由于Feign整合了Ribbon,所以使用ribbon的参数也可以配置feign

# 设置Ribbon的超时时间
ribbon:
  ConnectTimeout: 1000 # 连接超时时间 默认1s  默认单位毫秒
  ReadTimeout: 3000 # 逻辑处理的超时时间 默认1s 默认单位毫秒
  
# 设置Ribbon的辅助均衡策略
eureka-provider: #被调用的服务应用名 
  ribbon:u
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #随机
```

>step5:编写Feign调用接口

```java
import com.itheima.consumer.domain.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("FEIGN-PROVIDER")//value属性为服务应用名
public interface GoodsFeignClient {
    
    @GetMapping("/goods/findOne/{id}") //服务的资源路径uri
    public Goods findOne(@PathVariable("id") int id);
}
```

>step6:注入对象,调用方法

```java
@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private GoodsFeignClient goodsFeignClient;
    
    @GetMapping("/goods2/{id}")
    public Goods findGoodsById2(@PathVariable("id") int id) {
        return goodsFeignClient.findOne(id);
    }
}

```

## 2.扩展-日志记录

>step1:编写配置文件

```yml
# 设置当前的日志级别 debug，feign只支持记录debug级别的日志
logging:
  level:
    com.itheima: debug
```

>step2:配置Feign日志级别Bean

```java
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLogConfig {
    /*
        NONE,不记录
        BASIC,记录基本的请求行，响应状态码数据
        HEADERS,记录基本的请求行，响应状态码数据，记录响应头信息
        FULL;记录完成的请求 响应数据
     */
    @Bean
    public Logger.Level level(){
        return Logger.Level.FULL;
    }
}

```

>step3:启用日志Bean

```java
import com.itheima.consumer.config.FeignLogConfig;
import com.itheima.consumer.domain.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "FEIGN-PROVIDER",configuration = FeignLogConfig.class) //这里添加日志的配置类文件
public interface GoodsFeignClient {

    @GetMapping("/goods/findOne/{id}")
    public Goods findGoodsById(@PathVariable("id") int id);

}
```

## 3.总结

Ribbon开发虽然在一定程度上减少代码量,但是还是需要在控制器中将url地址进行硬编码,而在Spring Cloud中使用Feign, 我们可以做到使用HTTP请求远程服务时能与调用本地方法一样的编码体验，开发者完全感知不到这是远程方法，更感知不到这是个HTTP请求。

# 三.Feign-简化Hystrix开发

## 1.消费端服务降级开发流程

>step0:构建Eureka基本架构,包括注册中心,服务消费者和服务提供者,并加入Feign组件,由于服务降级一般只需在消费者一端配置即可,所以以下操作<font color='yellow'>都在服务消费者模块中进行</font>

>~~step1:导入Hystrix依赖,由于Feign整合了Hystrix,所以无需额外导入依赖~~

>~~step2:注解方面只需启用Feign服务即可~~

>step3:编写配置文件

```yml
# 开启feign对hystrix的支持
feign:
  hystrix:
    enabled: true
# hystrix熔断机制相关配置
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 2000  #设置Hystrix的超时时间2s，默认1ss
      circuitBreaker:
        errorThresholdPercentage: 50 # 触发熔断错误比例阈值，默认值50%
        sleepWindowInMilliseconds: 10000 # 熔断后休眠时长，默认值5秒
        requestVolumeThreshold: 10 # 熔断触发最小请求次数，默认值是20
```

>step4:定义Feign的调用接口实现类,并复写方法,该方法将作为降级方法

```java
@Component //注意:要注册到容器中,才能生效
public class GoodsFeignClientFallback implements GoodsFeignClient {
     
    @Override
    public Goods findOne(int id) {
        Goods goods = new Goods();
        goods.setTitle("又被降级了...");
        return goods;
    }
}

```

>step5:在Feign的调用接口中通过注解设置降级类

```java
import com.itheima.consumer.domain.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "FEIGN-PROVIDER",fallback = GoodsFeignClientFallback.class)//这里的fallback设置为降级类的全类名
public interface GoodsFeignClient {

    @GetMapping("/goods/findOne/{id}")
    public Goods findOne(@PathVariable("id") int id);
}

```

## 2.总结

一般我们在企业开发中,我们只会在消费者一端使用进行服务降级,因为,无论服务是否调用成功,我们都可以进行服务降级,否则,有些模块即作为消费者又作为提供者,需要提供两套降级方案,过于麻烦.

消费者端需要进行远程的服务调用,所以一般都会使用Feign组件,而Feign也同时整合了Hystrix,可以简化我们开发