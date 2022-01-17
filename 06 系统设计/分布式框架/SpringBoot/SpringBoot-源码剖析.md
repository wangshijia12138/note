 <center><h1><b><font color='gold'>SpringBoot原理剖析</font></b></h1></center>

# 一.SpringBoot起步依赖底层原理分析

## 1.版本控制优化-POM父依赖

>每一个SpringBoot项目都会继承**spring-boot-starter-parent**工程,该工程继承自spring-boot-dependencies,                                      **spring-boot-dependencies**管理着SpringBoot应用里面所有依赖版本,这些版本通过大量的测试,一般不会产生版本冲突问题

```xml
<!--管理SpringBoot项目的资源过滤及插件 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.1.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

```xml
<!--管理SpringBoot应用里面所有依赖版本-->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.3.1.RELEASE</version>
</parent>
```

## 2.Jar包管理优化-启动器

>SpringBoot根据功能场景,将Jar进行最优的组合,最后做成启动器,我们在使用时,只需要根据需求导入启动器即可 ,利用依赖传递的特性,Maven会自动将相关所有的Jar包都导入进来,由于这些Jar版本已被父项目锁定,所以不会出现版本冲突问题 .                        注意:一般由springboot官方提供的启动器都以spring-boot-starter-xxx为开头,而第三方提供的启动器则不是
>

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!--springboot-boot-starter-xxx：就是spring-boot的场景启动器-->
<!--spring-boot-starter-web：帮我们导入了web模块正常运行所依赖的组件；-->
```

## 总结

>**SpringBoot利用一个官方提供的父依赖,限定了大部分Jar包的版本,从而避免了版本冲突问题,这个父依赖的版本也就相当于SpringBoot的版本,除此之外,SpringBoot依据场景将jar包优化组合为几十个启动器,方便了我们的Jar包管理,避免因为jar导入错误引发的一些问题**

# 二.SpringBoot自动配置底层原理分析

## 1.@SpringBootApplication

>SpringBoot之所以能够实现自动配置的功能,核心就在于启动器上的@SpringBootApplication注解

```java
@SpringBootApplication
public class SpringbootTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestApplication.class, args);
	}
}
```

>@SpringBootApplication注解内部分析,核心是@EnableAutoConfiguration
>

```java
//--------元注解---------------
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//----------元注解----------
@SpringBootConfiguration//相当于@configuration 说明当前启动类是一个配置类,同时也是spring的一个组件
@EnableAutoConfiguration//启动自动配置,自动配置功能的核心
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })//组件扫描
public @interface SpringBootApplication {
//......
}

```

## 2.@EnableAutoConfiguration

>@EnableAutoConfiguration内部,核心为@Import注解
>

```java
//--------元注解---------------
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//--------元注解---------------
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)//将选择器种的数据作为Bean注入IOC容器
public @interface EnableAutoConfiguration {
    //......
}
```

## 3.@Import

>@Import导入的AutoConfigurationImportSelector最终会去加载META-INF/spring.factorie这一文件,而META-INF/spring.factorie文件处于启动器的元数数据中,由官方或第三方预先设定,里面包含大量自动配置类,通过import导入时,其内部所有bean都会被注入IOC(条件通过)

```java
public class AutoConfigurationImportSelector implements DeferredImportSelector, BeanClassLoaderAware,
		ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered {
//..........
	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return NO_IMPORTS;
		}
		AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(annotationMetadata);
		return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
	}
//..........
	protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return EMPTY_ENTRY;
		}
		AnnotationAttributes attributes = getAttributes(annotationMetadata);
		List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
		configurations = removeDuplicates(configurations);
		Set<String> exclusions = getExclusions(annotationMetadata, attributes);
		checkExcludedClasses(configurations, exclusions);
		configurations.removeAll(exclusions);
		configurations = getConfigurationClassFilter().filter(configurations);
		fireAutoConfigurationImportEvents(configurations, exclusions);
		return new AutoConfigurationEntry(configurations, exclusions);
	}
//..........
            
	protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
				getBeanClassLoader());
		Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "+ "are using a custom packaging, make sure that file is correct.");
		return configurations;
	}
```

```properties
#.......
#spring.factories文件
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration,\
org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration,\
org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoCo
#..............
```

## 4.@ConditionalOnXXX

>但这些配置类中的Bean并不是导入SpringBoot就会进行自动配置,每个配置类中都有@ConditionalOnXXX的注解,
>
>会根据当前依赖是否导入,当前配置文件是否存在该参数,当前Bean是否被自定义等条件,动态判断是否进行自动配置

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({ LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class })
public class RedisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

}
```

## 5.@EnableConfigurationProperties(xxxProperties.class)

>自动配置类会通过@EnableConfigurationProperties注解加载参数类,参数类中包含了预设的默认值,我们可以通过配置文件去覆盖这些默认值,这里的prefix+属性名就是配置文件中的键名

```java
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

	/**
	 * Database index used by the connection factory.
	 */
	private int database = 0;

	/**
	 * Connection URL. Overrides host, port, and password. User is ignored. Example:
	 * redis://user:password@example.com:6379
	 */
	private String url;

	/**
	 * Redis server host.
	 */
	private String host = "localhost";

//......

}

```

## 总结

>**SpringBoot会在项目启动时依据spring.factories文件,将其中的自动配置类中的所有Bean都自动注入IOC容器,并且依据其属性类,为他们赋上默认值,但这样有可能导致我们导入大量无用的Bean,浪费系统性能,所以,自动配置类中,通过@ConditionalOnXXX注解,动态判断项目当前的状态,根据对应jar包是否导入,是否自定义相应Bean等条件,判断当前是否注入该bean,利用这种方式,我们无需再写大量配置信息去注入Bean,一般,我们只需导入依赖,修改一些默认参数,即可直接在项目中注入对象,直接使用**



# 三.SpringBoot启动流程分析

![image-20200701212327607](.img/image-20200701212327607.png)

## 1.开始启动

>通过入口方法我们可以得知,项目的经历了初始化和启动两个阶段 

## ![image-20200701210412496](.img/image-20200701210412496.png)

## 2.项目初始化

>项目的初始化,从源码层面分析就是 SpringApplication对象的构建

```java
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
 	//1.配置Source
    this.resourceLoader = resourceLoader;
    Assert.notNull(primarySources, "PrimarySources must not be null");
    this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
    //2、推断应用的类型是普通的项目还是Web项目
    this.webApplicationType = WebApplicationType.deduceFromClasspath();
    //3、查找并加载所有可用初始化器 ， 设置到initializers属性中
	setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
    //4、找出所有的应用程序监听器，设置到listeners属性中
    setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
    //5、推断并设置main方法的定义类，找到运行的主类
    this.mainApplicationClass = deduceMainApplicationClass();
}
```

  

## 3.项目启动

>项目的启动,从源码层面分析就是 SpringApplication执行run方法

```java
public ConfigurableApplicationContext run(String... args) {
    	//启动计时器
		StopWatch stopWatch = new StopWatch();
    	//开始记时
		stopWatch.start();
		ConfigurableApplicationContext context = null;
		Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
		configureHeadlessProperty();
    	//获取所有SpringApplicationRunListener的实现类
		SpringApplicationRunListeners listeners = getRunListeners(args);
    	//执行所有SpringApplicationRunListener的实现类的start方法
		listeners.starting();
		try {
            //配置环境
			ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
			ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
			configureIgnoreBannerBeanInfo(environment);
            //加载Banner
			Banner printedBanner = printBanner(environment);
            //配置应用上下文
			context = createApplicationContext();
			exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
					new Class[] { ConfigurableApplicationContext.class }, context);
			prepareContext(context, environment, listeners, applicationArguments, printedBanner);
            refreshContext(context);
			afterRefresh(context, applicationArguments);
            //停止记时
			stopWatch.stop();
			if (this.logStartupInfo) {
				new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
			}
			listeners.started(context);
			callRunners(context, applicationArguments);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, listeners);
			throw new IllegalStateException(ex);
		}

		try {
			listeners.running(context);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, null);
			throw new IllegalStateException(ex);
		}
		return context;
	}
```





























