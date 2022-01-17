 <center><h1><b><font color='grey'>JDBC详解</font></b></h1></center>

# Step 1:配置数据源(Data Source)

***

### 1.导入数据源坐标及数据库驱动坐标

```xml
<!-- C3P0连接池 -->
<dependency>
    <groupId>c3p0</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.1.2</version>
</dependency>
<!-- Druid连接池 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.10</version>
</dependency>
<!-- mysql驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.39</version>
</dependency>
```

### 2.配置数据源参数

#### 		~~①手动配置~~

```java
/*创建C3P0连接池*/
@Test
public void testC3P0() throws Exception {
	//创建数据源
	ComboPooledDataSource dataSource = new ComboPooledDataSource();
	//设置数据库连接参数
    dataSource.setDriverClass("com.mysql.jdbc.Driver");    	               	               		         dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
    dataSource.setUser("root");
    dataSource.setPassword("root");
	//获得连接对象
	Connection connection = dataSource.getConnection();
	System.out.println(connection);
}


/*创建Druid连接池*/
@Test
public void testDruid() throws Exception {
    //创建数据源
    DruidDataSource dataSource = new DruidDataSource();
    //设置数据库连接参数
    dataSource.setDriverClassName("com.mysql.jdbc.Driver"); 
    dataSource.setUrl("jdbc:mysql://localhost:3306/test");   
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    //获得连接对象
    Connection connection = dataSource.getConnection();    
    System.out.println(connection);
}
```

#### 	②自动配置		

+ 编写数据源配置文件

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test
jdbc.username=root
jdbc.password=root
```

+ 编写Spring配置文件

```xml
<!-- 
命名空间：
xmlns:context="http://www.springframework.org/schema/context"
约束路径：
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd 
-->

<!--读取外部properties配置文件 注意:该功能需要引入以上命名空间和约束路径 -->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!--C3P0数据源的配置-->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="${jdbc.driver}"/>
    <property name="jdbcUrl" value="${jdbc.url}"/>
    <property name="user" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!--Druid数据源的配置-->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

```

# Step 2:配置JdbcTemplate

***

### 1.导入依赖Jar包坐标

### 2.配置JdbcTemplate,并注入数据源

```xml
<!--jdbcTemplate配置-->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="dataSource"></property>
</bean>
```





# ※代码总结

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

<!--jdbcTemplate配置-->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="dataSource"></property>
</bean>
```

