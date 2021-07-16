 <center><h1><b><font color='grey'>MyBatis配置详解</font></b></h1></center>

# 一、核心配置(sqlMapConfig.xml)

## ※配置文件约束头

```xml-dtd
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	...
</configuration>
```

## 1.外部属性文件加载

```xml
<!-- 加载外部properties文件   -->
<properties resource="jdbc.properties"/><!-- resource代表类路径下  -->
```

## 2.类别名设置

```xml
<typeAliases>
    <!--方式一:-->
    <!--1.mybatis内置了一些常用的别名-->
    <!--2.type 不可省略 alias可省略 ,默认使用类名作为别名(不区分大小写)-->
    <typeAlias type="cn.wangshijia.domain.User" alias="user"/>

    <!--方式二:-->
    <!--1.设置包下所有类的别名,默认使用类名作为别名(不区分大小写)-->
    <package name="cn.wangshijia"/>
</typeAliases>
```

## 3.数据库环境配置

```xml
<!-- 数据库环境  允许配置多个,但只能指定一个默认环境-->
<environments default="mysql_development">
    <environment id="mysql_development">
        <!--事务管理器-->
        <!--JDBC: 直接使用了JDBC 的提交和回滚设置，它依赖于从数据源得到的连接来管理事务作用域-->
        <!--MANAGED: 通过第三方容器管理事务(不常用)-->
        <transactionManager type="JDBC"/>
        <!--数据源-->
        <!--UNPOOLED 这个数据源的实现只是每次被请求时打开和关闭连接。         -->
        <!--POOLED   这种数据源的实现利用“池”的概念将 JDBC 连接对象组织起来。       -->
        <!--JNDI     这个数据源的实现是为了能在如 EJB 或应用服务器这类容器中使用，容器可以集中或在外部配			置数据源，然后放置一个 JNDI 上下文的引用。 -->
        <dataSource type="POOLED">
            <property name="driver" value="${jdbc.driver}"/>
            <property name="url" value="${jdbc.url}"/>
            <property name="username" value="${jdbc.username}"/>
            <property name="password" value="${jdbc.password}"/>
        </dataSource>
    </environment>
</environments>
```

## 4.映射文件加载

```xml
<!--加载映射配置-->
<mappers>
    <!-- 方式一:-->
    <!-- 1.加载类路径下的映射文件 -->
    <mapper resource="cn/wangshijia/mapper/UserMapping.xml"/>
    <!-- 2.加载全限定路径下的映射文件 -->
    <!--<mapper url="file:///var/mappers/AuthorMapper.xml"/>-->
    <!-- 3.加载接口实现类的完全限定类名 -->
    <!-- <mapper class="org.mybatis.builder.AuthorMapper"/>-->

    <!-- 方式二:-->
    <package name="cn.wangshijia.mapper"/>
    <!-- 注意：通过扫描包的方式引入映射文件时,必须保证映射文件和接口在同一个目录中,
        我们一般在resources中创建和mapper接口一样的层级目录来存储映射文件,
        从而保证项目编译后配置文件和接口文件在同一个文件夹下-->
</mappers>

```

## 5.类型转换配置

```xml
<!--类型处理器-->
<typeHandlers>
    <!--配置步骤: -->
    <!--1.编写转换器类 extends BaseTypeHandler<T> T为需要被转换的目标类型-->
    <!--2.配置类型处理器 -->
    <!--3.测试 -->
    <typeHandler handler="cn.wangshijia.demo.MyDateTypeHandler"/>
</typeHandlers>
```

## 6.外部插件配置

```xml
<!--插件-->
<plugins>
    <!--配置步骤-->
    <!--1.导入插件Jar包-->
    <!--2.配置插件-->
    <!--3.测试-->
    <plugin interceptor="com.github.pagehelper.PageHelper" >
        <!--配置插件参数-->
        <property name="dialect" value="mysql"/>
    </plugin>
</plugins>
```



## 7.全局参数配置

```xml
<settings>
    <!-- 日志 -->
    <setting name="logImpl" value="LOG4J"/>
    <!--全局启用或禁用延迟加载。当禁用时，所有关联对象都会即时加载 默认为false -->
    <setting name="lazyLoadingEnabled" value="true"/>
    <!--当启用时，有延迟加载属性的对象在被调用时将会完全加载任意属性 默认为false -->
    <!-- <setting name="aggressiveLazyLoading" value="false"/>-->
</settings>
```



# 二、映射文件配置(xxxMapping.xml)

## ※配置文件约束头

```xml-dtd
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace 命名空间 : 一般是Dao层的接口全类名 -->
<mapper namespace="cn.wangshijia.mapper.UserMapper">
    ...
</mapper>
```

## 1.参数传递

## 2.结果映射

### ①自动映射

### ②自定义映射

```xml
<!-- 自定义映射-->
<!-- type : 最后封装的JavaBean    -->
<resultMap id="userMap" type="MyUser">
    <!-- 主键映射-->
    <!-- column:字段名 property:属性名-->
    <id column="userId" property="id"/>
    <!-- 基本类型映射--> 
    <result column="username" property="username"/>
    <result column="email" property="email"/>
    <result column="password" property="password"/>
    <result column="phoneNum" property="phoneNum"/>
    <!--POJO类型映射-->   
    <!-- property:属性名  javaType:POJO类型 -->
    <association property="school" javaType="school">
        <!-- 主键映射-->
        <id column="sid" property="id"/>
         <!-- 基本类型映射-->
        <result column="name" property="name"/>
    </association> 
    <!--集合类型映射-->
    <!-- property:属性名  ofType:集合内JavaBean类型 -->
    <collection property="roles" ofType="role">
        <!-- 主键映射-->
        <id column="roleId" property="id"/>
        <!-- 基本类型映射-->
        <result column="roleName" property="roleName"/>
        <result column="roleDesc" property="roleDesc"/>
    </collection>
</resultMap>
```

## 3.SQL语句编写

### ①基础SQL

```xml
<!--查询 -->
<!--List<User> findAll();-->
<select id="findAll" resultType="user">
    select * from user
</select>
<!--添加 -->
<!--void addUser();-->
<insert id="addUser">
    insert into user values(#{id},#{username},#{password})
</insert>
<!--更新 -->
<!--void updateUser();-->
<update id="updateUser">
    update user set username=#{username},password=#{password} where id=#{id}
</update>
<!--删除 -->
<!--void deleteUser();-->
<delete id="deleteUser">
    delete from user where id = #{id}
</delete>

<!--属性解析 -->
<!--1.resultType : 结果类型 , mybatis默认以属性名将查询结果进行封装-->
<!--2.parameterType : 参数类型 ,由于mybatis会自动进行类型推导,所以该属性一般省略-->

<!--注意点 -->
<!--1.返回值为集合类型时,resultType提供内部JavaBean的类型即可-->
<!--2.增删改只能返回integer类型(影响行数)和Boolean类型(是否执行成功)-->
```



### ②动态SQL

+ foreach 一般用于批量操作,遍历集合或数组类型参数

```xml
<!--foreach 一般用于批量操作,遍历集合或数组类型参数-->
<!--void deleteByIds(List<Integer> ids);-->
<delete id="deleteByIds">
    delete from user where id in
    <!--collection: 遍历对象 ,集合为list,数组为array -->
    <!--item: 代表每次遍历从中取出的值-->
    <!--open:代表从...开始-->
    <!--close:代表以...结束-->
    <!--separator:每次循环间添加的分隔符-->
    <foreach collection="list" item="id" open="(" close=")" separator=",">
        <!--循环体内容-->
        #{id}
    </foreach>
</delete>
```

+ if  一般用于多条件查询

```xml
<select id="findByCondition" resultType="hashMap" parameterType="user">
    select * from user
     <!--where: 根据if结果自动添加where ,还可以自动去除后面多余and-->
    <where>
        <!--if: test返回值为ture ,添加语句, test值内部可以使用and or 表示逻辑操作-->
        <if test="id!=null">
            and id = #{id}
        </if>
        <if test="username!=null">
            and username = #{username}
        </if>
        <if test="password!=null">
            and password= #{password}
        </if>
    </where>
</select>
```

### ③模糊查询

```sql
concat('%',#{参数},'%')
```

### ④自动获取主键id

方式一 适用于所有关系型数据库

```xml
<insert id="addUser" >
    <selectKey keyColumn="id" keyProperty="id" resultType="int" order="AFTER">
        select LAST_INSERT_ID()
    </selectKey>
    insert into user values(#{id},#{username},#{password},#{birthday},#{school})
</insert>
```

方式二 适用于有自增长主键功能的数据库

```xml
<!--    void addUser(User user);-->
<insert id="addUser" useGeneratedKeys="true" keyProperty="id" keyColumn="id" >
    insert into user values(#{id},#{username},#{password},#{birthday},#{school})
</insert>
```


