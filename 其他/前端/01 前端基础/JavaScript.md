## 1.JDK 和 JRE 有什么区别？

>JDK是Java的开发工具包,包含了Java开发工具和Java运行环境,而JRE是Java只是单纯的运行环境,所以,如果我们需要运行Java程序,只需要安装JRE,但如果需要进行Java开发,就必须要JDK

## 2.== 和 equals 的区别是什么？

>==比较的是两个引用对象的引用地址,而equals在重写的情况下,比较的某些特征,比如String equals重写后,先会去判断引用类型是否相同,如果不相同的话,还会去比较内容是否一致,如果内容一致,也会返回true

## 3.两个对象的 hashCode()相同，则 equals()也一定为 true，反过来呢?

> 一般来说,hashcode相同的两个对象,equals不一定相同,因为哈希值本身是并不是无限的,所以是存在说两个不同的对象Hashcode相同的这种情况,而equals相同的话,hashcode方法一般是相同的,除非我们没有在重写equals的时候同时重写hashcode方法

## 4.hashCode()有什么作用?

>hashCode()的主要作用是提高我们的存储和查询效率,比如我们在Set中进行存储,我们需要保证数据唯一,假如里面已经有10000个数据,我们此时插入数据时如果没有hashCode(),我们就需要调用10000次equals方法进行判断,效率极低,同样,查询也是如此
>
>引入hashCode()就可以基于一种叫hash表的数据结构,先通过hashcode对数据进行筛选,计算出对象可能存在的位置,再通过equls键精确匹配,就可以大大提高我们的存储和查询效率

## 5.为什么重写equlas的时候一定要重写hashcode

>这就是一种规范,因为我们要保证equlas相同的两个对象,hashcode值也相同,否则在进行一些散列集合存储时,hashcode不同的两个对象就会直接判定为不同的两个对象,就无法保证集合的唯一性

## 6.final,finally,finalize有什么区别

>final是一个关键词,用在类上,代表类不可被继承,用在方法上代表方法不可被重写,用在变量上代表该变量只能重写一次
>
>finally的话属于一个区块标志,一般与try-catch联用,在finally修饰代码块中不论是否有异常都会执行,一般用于资源的释放,当然也不是绝对的,在一些特殊情况下,比如try之前出现了异常也不会执行
>
>finalize是一个方法,在对象即将被回收时,由JVM来调用,用来执行一些内存清理工作

## 5.java 中的 Math.round(-1.5) 等于多少？

>-1

## 6.String 属于基础的数据类型吗？

>String不属于基础类型,他是一个final修饰的Java类

## 7.Java 中操作字符串都有哪些类？它们之间有什么区别？

>想要操作字符串的话,有三个类,分别是String , StringBuilder ,StringBuffer
>
>String的在设计之初,基于安全和性能的考虑,设计为不可变,底层是final修饰的char数组,在程序运行时会通过一个字符串常量池来维护字符串,所以,我们对字符串进行频繁的拼接操作,就会在内存中产生大量无效的字符串,非常浪费内存和性能
>
>StringBuilder 的话是一个可变的字符序列,底层是一个没有final修饰的char数组,所以StringBuilder 在进行操拼接作时,不会产生新的对象,所以,相比String,更加节约内存空间
>
>StringBuffer的话,和StringBuilder 功能基本一致,但是StringBuffer时线程安全的,所以在性能上会比StringBuilder差一些,我们在平时开发中还是StringBuilder用的比较多一些

## 8.String 类的常用方法都有那些

>replace()替换 trim()去空格 spilt()分割 subString() 截取 其他话就是toUpperCase()toLowerCase转换大小写的方法

## 9.String str="i"与 String str=new String("i")一样吗？

>不一样,前者会根据字符串常量池中是否拥有该字符串,判断是否创建新的对象,如果字符串池中已经有,就不会再创建,也就是说他可能创建一个或0个对象
>
>String str=new String("i")会在内存中创建一个对象,并根据字符串池是否有该字符串,选择性创建对象,也就是说,他可能创建一个或两个对象

## 11.抽象类必须要有抽象方法吗？

>不一定,抽象类可以没有抽象方法,但是抽象方法只能在抽象类中

## 12.抽象类能使用 final 修饰吗？

>不能,因为抽象类需要被继承,而final修饰的类不能被继承

## 13.普通类和抽象类有哪些区别？

>

## 14.接口和抽象类有什么区别？

>

## 15.谈谈什么是继承,封装,多态

>

## 16.谈谈对OO的理解

>



## 17.java 中 IO 流分为几种？

>按流向分的话,可以分为输入流和输出流
>
>按传输单元来分,可以分为字节流和字符流

## 18.BIO、NIO、AIO 有什么区别？

>BIO是同步阻塞IO NIO是同步非阻塞IO AIO是异步非阻塞IO

## 19.Files的常用方法都有哪些？

>createfile() 创建文件  delete() 删除一个文件或目录
>
>copy() 复制文件 move() 移动文件 size() 查看文件个数 exists() 查看文件是否存在

## 17.JDK8有什么新特性

>JDK8中为接口引入了默认方法和静态方法,方便我们后续进行扩展,
>
>除此之外,JDK8还引入了函数式编程思想,具体体现就是引入了Lambda表达式和函数式接口





是否可以从一个static方法内部发出对非static方法的调用？

String s =  "Hello";s = s + "world!";这两行代码执行后，原始的String对象中的内容到底变了没有？
 String s =  new String("xyz");创建了几个StringObject？是否可以继承String类？

String s =  "Hello";s = s + "world!";这两行代码执行后，原始的String对象中的内容到底变了没有？





