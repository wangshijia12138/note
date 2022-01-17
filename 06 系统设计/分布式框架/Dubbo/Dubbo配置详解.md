 <center><h1><b><font color='gold'>Dubbo配置详解</font></b></h1></center>

# 服务提供者相关配置

```xml
<!-- 指定Dubbo应用名称 -->
<dubbo:application name="health_service_provider"/>
<!--指定协议信息 ,以及端口号(端口号默认为20080) 消费方被动接收-->
<dubbo:protocol name="dubbo" port="20887"/>
<!--指定服务注册中心地址-->
<dubbo:registry address="zookeeper://127.0.0.1:2181"/>
<!--批量扫描，发布服务-->
<dubbo:annotation package="cn.wangshijia.service"/>
```

# 服务消费者相关配置

```xml
<!-- 指定应用名称 -->
<dubbo:application name="health_backend" />
<!--指定服务注册中心地址-->
<dubbo:registry address="zookeeper://127.0.0.1:2181"/>
<!--批量扫描-->
<dubbo:annotation package="cn.wangshijia"/>
<!--
        超时全局设置 10分钟
        check=false 不检查服务提供方，开发阶段建议设置为false
        check=true 启动时检查服务提供方，如果服务提供方没有启动则报错
-->
<dubbo:consumer timeout="600000" check="false"/>
```

