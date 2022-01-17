 <center><h1><b><font color='grey'>Maven配置详解</font></b></h1></center>

# 继承

```xml
<parent>
    <artifactId>Maven_Parent</artifactId>
    <groupId>cn.wangshijia</groupId>
    <version>1.0-SNAPSHOT</version>
</parent>
```

# 聚合

```xml
<modules>
    <module>Maven_Dao</module>
    <module>Maven_Service</module>
    <module>Maven_Web</module>
    <module>Maven_Pojo</module>
</modules>
```



# 版本锁定

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>${mybatis.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${springmvc.version}</version>
        </dependency>
 </dependencyManagement>
    <!-- 注意:版本锁定并不会导入Jar包-->
```



# 依赖排除

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>4.3.9.RELEASE</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

