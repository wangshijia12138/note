# 数据表元素

## <font color =gold>1.数据类型</font>

>数值型

|              数据类型               |                占用内存                 |                        说明                        |           Java中对应类型            |
| :---------------------------------: | :-------------------------------------: | :------------------------------------------------: | :---------------------------------: |
|               tinyint               |                 1个字节                 |                    十分小的数据                    |                                     |
|              smallint               |                 2个字节                 |                     较小的数据                     |                                     |
|              mediumint              |                 3个字节                 |                   中等大小的数据                   |                                     |
| <font color=green size=4>int</font> | <font color=green size=4>4个字节</font> | <font color=green size=4>标准的整数(最常用)</font> | <font color=green size=4>int</font> |
|               bigint                |                 8个字节                 |                     较大的数据                     |                long                 |
|                float                |                 4个字节                 |                    单精度浮点数                    |                float                |
|               double                |                 8个字节                 |                    双精度浮点数                    |               double                |
|               decimal               |                 8个字节                 |         字符串形式浮点数(一般用于金融计算)         |               decimal               |

>字符串型

|                数据类型                 | 占用内存 |    说明    | Java中对应类型 |
| :-------------------------------------: | :------: | :--------: | :------------: |
|                  char                   |  0-255   | 固定字符串 |                |
| <font color=green size=4>varchar</font> | 0-65535  | 可变字符串 |     String     |
|                tinytext                 |  2^8-1   |  微型文本  |                |
|                  text                   |  2^16-1  |    文本    |                |

>日期类型

| 数据类型  | 占用内存 |             说明              | Java中对应类型 |
| :-------: | :------: | :---------------------------: | :------------: |
|   date    |          |          YYYY-MM-DD           |                |
|   time    |          |           HH:mm:ss            |                |
| datetime  |          |      YYYY-MM-DD HH:mm:ss      |                |
| timestamp |          | 时间戳,1970.1.1到现在的毫秒值 |                |
|   year    |          |             年份              |                |

>NULL值

- 理解为 "没有值" 或 "未知值"
- 不要用NULL进行算术运算 , 结果仍为NULL

>面试题: char 和 varchar 的区别是什么？

```
char是固定长度的字符串,例如定义一个char(10)的字段时,存储abc,那么其占用内存依旧是10,所以一般使用于存储固定长度的字符串,例如MD5加密字符串
vrchar是可变长度字符串,其存储大小是本身占用字节大小,加上一个长度记录字节的大小
相比而言,从空间利用上而言vrchar更合适,从效率上讲char更合适
```

## <font color =gold>2.属性</font>

>UnSigned

- 无符号的
- 声明该数据列不允许负数 

>ZEROFILL

- 0填充的
- 不足位数的用0来填充 , 如int(3),5则为005

>Auto_InCrement

- 自动增长的 , 每添加一条数据 , 自动在上一个记录数上加 1(默认)

- 通常用于设置**主键** , 且为整数类型

- 可定义起始值和步长

  - 当前表设置步长(AUTO_INCREMENT=100) : 只影响当前表
  - SET @@auto_increment_increment=5 ; 影响所有使用自增的表(全局)

>NULL 和 NOT NULL

- 默认为NULL , 即没有插入该列的数值
- 如果设置为NOT NULL , 则该列必须有值

>DEFAULT

- 默认的
- 用于设置默认值
- 例如,性别字段,默认为"男" , 否则为 "女" ; 若无指定该列的值 , 则默认值为"男"的值



## <font color =gold>3.索引</font>

>索引的定义:索引（Index）是帮助MySQL高效获取数据的数据结构


>索引的作用

- 提高查询速度
- 确保数据的唯一性
- 可以加速表和表之间的连接 , 实现表与表之间的参照完整性
- 使用分组和排序子句进行数据检索时 , 可以显著减少分组和排序的时间
- 全文检索字段进行搜索优化.

> 主键索引 (Primary Key)

主键 : 某一个属性组能唯一标识一条记录

特点 :

- 最常见的索引类型
- 确保数据记录的唯一性
- 确定特定数据记录在数据库中的位置

>唯一索引(Unique)

作用 : 避免同一个表中某数据列中的值重复

与主键索引的区别

- 主键索引只能有一个
- 唯一索引可能有多个

>常规索引

作用 : 快速定位特定数据

注意 :

- index 和 key 关键字都可以设置常规索引
- 应加在查询找条件的字段
- 不宜添加太多常规索引,影响数据的插入,删除和修改操作

>全文索引

作用 : 快速定位特定数据

注意 :

- 只能用于MyISAM类型的数据表
- 只能用于CHAR , VARCHAR , TEXT数据列类型
- 适合大型数据集

```SQL
/*
#方法一：创建表时
  　　CREATE TABLE 表名 (
               字段名1 数据类型 [完整性约束条件…],
               字段名2 数据类型 [完整性约束条件…],
               [UNIQUE | FULLTEXT | SPATIAL ]   INDEX | KEY
               [索引名] (字段名[(长度)] [ASC |DESC])
               );


#方法二：CREATE在已存在的表上创建索引
       CREATE [UNIQUE | FULLTEXT | SPATIAL ] INDEX 索引名
                    ON 表名 (字段名[(长度)] [ASC |DESC]) ;


#方法三：ALTER TABLE在已存在的表上创建索引
       ALTER TABLE 表名 ADD [UNIQUE | FULLTEXT | SPATIAL ] INDEX
                            索引名 (字段名[(长度)] [ASC |DESC]) ;
                           
                           
#删除索引：DROP INDEX 索引名 ON 表名字;
#删除主键索引: ALTER TABLE 表名 DROP PRIMARY KEY;


#显示索引信息: SHOW INDEX FROM student;
*/

/*增加全文索引*/
ALTER TABLE `school`.`student` ADD FULLTEXT INDEX `studentname` (`StudentName`);

/*EXPLAIN : 分析SQL语句执行性能*/
EXPLAIN SELECT * FROM student WHERE studentno='1000';

/*使用全文索引*/
-- 全文搜索通过 MATCH() 函数完成。
-- 搜索字符串作为 against() 的参数被给定。搜索以忽略字母大小写的方式执行。对于表中的每个记录行，MATCH() 返回一个相关性值。即，在搜索字符串与记录行在 MATCH() 列表中指定的列的文本之间的相似性尺度。
EXPLAIN SELECT *FROM student WHERE MATCH(studentname) AGAINST('love');

/*
开始之前，先说一下全文索引的版本、存储引擎、数据类型的支持情况

MySQL 5.6 以前的版本，只有 MyISAM 存储引擎支持全文索引；
MySQL 5.6 及以后的版本，MyISAM 和 InnoDB 存储引擎均支持全文索引;
只有字段的数据类型为 char、varchar、text 及其系列才可以建全文索引。
测试或使用全文索引时，要先看一下自己的 MySQL 版本、存储引擎和数据类型是否支持全文索引。
*/
```

>扩展：索引的准则

- 索引不是越多越好
- 不要对经常变动的数据加索引
- 小数据量的表建议不要加索引
- 索引一般应加在查找条件的字段

>扩展：索引的数据结构

```SQL
-- 我们可以在创建上述索引的时候，为其指定索引类型，分两类
hash类型的索引：查询单条快，范围查询慢
btree类型的索引：b+树，层数越多，数据量指数级增长（我们就用它，因为innodb默认支持它）

-- 不同的存储引擎支持的索引类型也不一样
InnoDB 支持事务，支持行级别锁定，支持 B-tree、Full-text 等索引，不支持 Hash 索引；
MyISAM 不支持事务，支持表级别锁定，支持 B-tree、Full-text 等索引，不支持 Hash 索引；
Memory 不支持事务，支持表级别锁定，支持 B-tree、Hash 等索引，不支持 Full-text 索引；
NDB 支持事务，支持行级别锁定，支持 Hash 索引，不支持 B-tree、Full-text 等索引；
Archive 不支持事务，支持表级别锁定，不支持 B-tree、Hash、Full-text 等索引；
```



## <font color =gold>4.存储引擎</font>

>MySQL有哪些常用的存储引擎













>MySQL常见的引擎类型有MyISAM,InnoDB,HEAP,BOB,CSV等

| 功能       | MyISAM | InnoDB(默认) |
| ---------- | ------ | ------------ |
| 事务处理   | 不支持 | 支持         |
| 数据行锁定 | 不支持 | 支持         |
| 外键约束   | 不支持 | 支持         |
| 全文索引   | 支持   | 不支持       |
| 表空间大小 | 较小   | 较大,约2倍   |

>面试题:说一下 mysql 常用的引擎

```
MySQL种常用的引擎有InnoDB 和 MyISAM ,其中InnoDB是Mysql的默认引擎,他支持事务处理,行锁定和外键约束,所以他的安全性,以及多表操作,高并发操作的性能比MyISAM更好,所以在一些高并发的场景下,都会使用InnoDB引擎
MyISAM引擎的话,不支持事务,行锁定和外键约束,但是他支持全文索引,且他的表空间更小,所以在一些对事务没有要求,数据查询较多的场景下,可以考虑使用MyISAM引擎,以获得更快的响应速度,和更小空间成本
```

## <font color =gold>5.字符集</font>

我们可为数据库,数据表,数据列设定不同的字符集，设定方法 :

- 创建时通过命令来设置 , 如 : CREATE TABLE 表名()CHARSET = utf8;
- 如无设定 , 则根据MySQL数据库配置文件 my.ini 中的参数设定,默认字符集不支持中文

# 数据表创建语句

>基本格式

```sql
create table [if not exists] `表名`(
    '字段名1' 列类型 [属性][索引][注释],
    '字段名2' 列类型 [属性][索引][注释],
    #...
    '字段名n' 列类型 [属性][索引][注释]
)[表引擎类型][表字符集][注释];
```

>示例

```sql
CREATE TABLE IF NOT EXISTS `student` (
    `id` int(4) NOT NULL AUTO_INCREMENT COMMENT '学号',
    `name` varchar(30) NOT NULL DEFAULT '匿名' COMMENT '姓名',
    `pwd` varchar(20) NOT NULL DEFAULT '123456' COMMENT '密码',
    `sex` varchar(2) NOT NULL DEFAULT '男' COMMENT '性别',
    `birthday` datetime DEFAULT NULL COMMENT '生日',
    `address` varchar(100) DEFAULT NULL COMMENT '地址',
    `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

>扩展

```sql
/*
企业开发中，每个表都必须存在以下五个字段：
id 主键
version 乐观锁
is_delete 伪删除
gmt_create 创建时间   gmt：格林威治标准时间（Greenwich Mean Time）
gmt_update 修改时间
*/
```

# 数据表设计理念