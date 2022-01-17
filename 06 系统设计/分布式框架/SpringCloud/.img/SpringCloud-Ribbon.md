 <center><h1><b><font color='gold'>SpringCloud-远程服务调用</font></b></h1></center>

# 一.DiscoveryClient - 客户端

### 1.开发流程

>step1:构建Eureka架构,导入相关依赖

```xml
<!-- 注意: 该依赖一般在构建服务消费端时就会被导入 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>		
```

>step2:在服务消费者端启用@EnableDiscoveryClient注解

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

>step3:注入DiscoveryClient,动态获取注册信息,进行服务的调用

```java
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("getOrder/{id}")
    public Goods getOrder(@PathVariable("id") int id) {
        //获取服务列表
        List<ServiceInstance> instances = discoveryClient.getInstances("EUREKA-PROVIDER");

        //判空
        if (instances == null || instances.size() == 0) {
            return null;
        }
        //获取服务实例
        ServiceInstance serviceInstance = instances.get(0);
        //获取主机名
        String host = serviceInstance.getHost();
        //获取端口号
        int port = serviceInstance.getPort();
        //拼接url地址
        String url = "http://"+host+":"+port+"/goods/findOne/"+id;
        return restTemplate.getForObject(url, Goods.class);
    }
}

```

### 2.总结

>**该方法通过DiscoveryClient对象获取服务信息,再通过RestTemplate方法进行服务调用,代码过于繁琐,在集群环境下,需要手动编写负载均衡算法,代码复杂冗长,不易后期维护,所以我们需要通过其他组件进行进一步封装**

# 二.Ribbon - 客户端

### 1.开发流程

>step1:构建Eureka架构,导入相关依赖

```xml
<!-- 注意: 该依赖一般在构建服务消费端时就会被导入 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>		
```

>step2:配置RestTemplate

```java
@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced//使得RestTemplate对象具备负载均衡的功能
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

>step3:编写Controller方法

```java
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/goods/{id}")
    public Goods findGoodsById(@PathVariable("id") int id) {
        //这里使用服务应用名代替uri
        String url = "http://FEIGN-PROVIDER/goods/findOne/" + id;
        return restTemplate.getForObject(url, Goods.class);
    }
}
```

### 2.总结

>**Ribbon在原生方法的基础上进一步封装,在客户端(消费者)一方记录服务ip,并提供了负载均衡算法,极大的简化了我们的代码,但是Ribbon还是存在一些不足,他的url地址还是需要进行拼接,如果服务的提供者接口名称改变,就需要修改消费端的源码,十分麻烦**

### 3.扩展-负载均衡策略的配置

>step1:配置负载均衡策略-编写策略类

```java
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRule {

    //IRule 是策略类的顶层接口
    @Bean
    public IRule getRule(){
        return new RandomRule(); //这里使用随机策略,默认是轮询
    }
}
```

>step2:配置负载均衡策略-编写

```yml
feign-provider:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

