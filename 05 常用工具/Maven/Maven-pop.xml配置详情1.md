 <center><h1><b><font color='grey'>Maven项目-pop.xml文件详解</font></b></h1></center>

# 1. 基础配置信息

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
	 <!--声明项目描述符遵循哪一个POM模型版本。模型本身的版本很少改变，虽然如此，但它仍然是必不可少的，
    这是为了当Maven引入了新的特性或者其他模型变更的时候，确保稳定性。-->  
    <modelVersion>4.0.0</modelVersion>
 	<!-- 公司或者组织的唯一标志 -->  
    <groupId>cn.itcast</groupId>
	<!-- 项目的唯一ID,注意:groupId和项目id的组合是唯一的 -->  
    <artifactId>maven_java_second</artifactId>
	<!--项目当前版本，格式为:主版本.次版本.增量版本-限定版本号-->  
    <version>1.0-SNAPSHOT</version> 
     <!-- 打包的机制，如pom,jar, maven-plugin, ejb, war, ear, rar, par，默认为jar -->  
    <packaging>war</packaging>
```

# 2. Jar包配置信息

```xml
<dependencies>
        <dependency>
<!--一般情况下，maven是通过groupId、artifactId、version这三个元素值（俗称坐标）来检索该构件， 然后引入你的工程。如果别人想引用你现在开发的这个项目（前提是已开发完毕并发布到了远程仓库），就需要在他的pom文件中新建一个dependency节点，将本项目的groupId、artifactId、version写入， maven就会把你上传的jar包下载到他的本地-->  
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>  
<!-- maven认为，程序对外部的依赖会随着程序的所处阶段和应用场景而变化，所以maven中的依赖关系有作用域(scope)的限制。scope包含如下的取值：compile（编译范围）、provided（已提供范围）、runtime（运行时范围）、test（测试范围）、system（系统范围） -->  
            <scope>provided</scope>
        </dependency>
 <!-- 
1.compile 
默认scope为compile，表示为当前依赖参与项目的编译、测试和运行阶段，属于强依赖。打包之时，会将该jar包封装到项目结果物中。
2.test 
该依赖仅仅参与测试相关的内容，包括测试用例的编译和执行，比如定性的Junit。
3.runtime 
依赖仅参与运行周期中的使用。一般这种类库都是接口与实现相分离的类库，比如JDBC类库，在编译之时仅依赖相关的接口，在具体的运行之时，才需要具体的mysql、oracle等等数据的驱动程序。此类的驱动都是为runtime的类库。
4.provided 
该依赖不会打包到项目结果物中，这个由运行的环境来提供，比如tomcat或者基础类库等等，事实上，该依赖可以参与编译、测试和运行等周期，与compile等同。区别在于打包阶段进行了exclude操作。
5.system 
使用上与provided相同，不同之处在于该依赖不从maven仓库中提取，而是从本地文件系统中提取，其会参照systemPath的属性进行提取依赖。
6.import 
这个是maven2.0.9版本后出的属性，import只能在dependencyManagement的中使用，能解决maven单继承问题，import依赖关系实际上并不参与限制依赖关系的传递性。--> 
         <dependency>
				...
        </dependency>  
    
</dependencies>
```

# 3. 插件配置信息

```xml
<build>
        <plugins>
            <plugin>
                <!-- JDK配置 -->
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
					<!-- source:源代码编译版本 -->
                    <source>1.8</source>
					<!-- target:目标平台编译版本； -->
                    <target>1.8</target>
                      <!-- encoding:字符集编码 -->
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            
			<!-- Tomcat配置 注意:一般我们推荐使用idea集成的Tomcat运行Maven程序 -->
            <plugin>
				<groupId>org.apache.tomcat.maven</groupId>
                 <!-- 注意:Maven默认使用Tomcat6 -->
                 <artifactId>tomcat7-maven-plugin</artifactId>
                 <version>2.1</version>
                 <!-- 配置信息 -->
                 <configuration>
                     <!-- 端口号 -->
                    <port>8080</port>
                     <!-- 虚拟路径 -->
                    <path>/maven_java_second</path>
                     <!-- 字符集编码 -->
                    <uriEncoding>UTF-8</uriEncoding>
                     <!-- 服务器名 -->
                    <server>tomcat7</server>
                 </configuration>
            </plugin>
        </plugins>
</build>
```

