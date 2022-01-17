# DQL-数据库查询语言

## 基础查询

>select 表达式

```sql
-- 查询文本值
SELECT 'xxx'
-- 查询字段
SELECT username FROM t_user
-- 查询NULL
SELECT NULLsql
-- 查询计算表达式
SELECT 100*3+10
-- 查询数据库版本(函数)
SELECT VERSION()
-- 查询自增的步长(系统变量)
SELECT @@auto_increment_increment
```



## 条件查询

>逻辑运算符

| 操作符 | 语法    | 描述   |
| ------ | ------- | ------ |
| AND    | a AND b | 逻辑与 |
| OR     | a OR b  | 逻辑或 |
| NOT    | NOT a   | 逻辑非 |

```sql
-- AND 
SELECT * FROM t_order WHERE id = 2 AND setmeal_id =6
-- OR
SELECT * FROM t_order WHERE id = 2 OR setmeal_id =6
-- NOT
SELECT * FROM t_order WHERE NOT id = 2 AND setmeal_id =6
```

>比较运算符

| 操作符          | 语法                   | 描述                                |
| --------------- | ---------------------- | ----------------------------------- |
| >,<,>=,<=,<>,!= |                        | xml代码中<>为特殊字符,需要用表达式  |
| BETWEEN...AND   | a BETWEEN b AND c      | 若a在b与c之间,则结果为真            |
| LIKE            | a LIKE b               | 模糊匹配,可以使用_和%               |
| IN              | a IN (a1, a2, a3,....) | 若a等于a1,a2,...中的一个,则结果为真 |
| IS NULL         | a IS NULL              | a 为NULL 结果为真                   |
| IS NOT NULL     | a IS NOT NULL          | a 不为NULL 结果为真                 |

```sql
-- <> 不等于
SELECT * FROM t_order WHERE id <> 2 AND setmeal_id =6
-- != 不等于
SELECT * FROM t_order WHERE id != 2 AND setmeal_id =6
-- BETWEEN .. AND ..
SELECT * FROM t_order WHERE id BETWEEN 2 AND 4
-- IN
SELECT * FROM t_order WHERE id IN (2,3,4)
-- LIKE _占1个字符位 %占0个或多个字符位
SELECT * FROM t_order WHERE orderType LIKE '微%_'
```

## 联表查询

<img src="https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7LwfjFbCQXic0pcE21lUFGvDw5aZLehIYzwLprCfqdxSjsm2wficHrSEzJiaJBGaKWpatQ7sISib9MgCQ/640?wx_fmt=png&amp;tp=webp&amp;wxfrom=5&amp;wx_lazy=1&amp;wx_co=1" alt="图片的替代文字" style="zoom: 67%;" />

>JOIN 对比

| 操作符     | 描述                                    |
| ---------- | --------------------------------------- |
| INNER JOIN | 如果表中至少有一个匹配,则返回行         |
| LEFT JOIN  | 即使右表中没有匹配,也从左表中返回所有行 |
| RIGHT JOIN | 即使左表中没有匹配,也从右表中返回所有行 |

```sql
/*思路:
(1):分析需求,确定查询的列来源于哪几张表
(2):确定使用哪种连接查询?(7种)
(3):确定交叉点,一般是主外键
*/

-- 隐式内连接
-- 显示内连接
-- 左外连接
-- 右外连接
```



>自连接

```sql
/*思路:
(1):将一张表拆成两张表查询,分别取别名为 a,b
(2):将a表的id 和b表的父id作为交叉点
*/

SELECT a.name '父菜单',b.name '子菜单'
from t_menu a ,t_menu b
where a.id = b.parentMenuId

```

## 排序查询

>ORDER BY

## 分页查询

>Limit   索引值,每页显示数量

## 子查询



## 总结-DQL语法汇总

>**注意 : [ ] 括号代表可选的 , { }括号代表必选得**

```sql
SELECT [ALL | DISTINCT] {* | table.* | [table.field1[as alias1][,table.field2[as alias2]][,...]]}
FROM table_name [as table_alias]
[left | right | inner join table_name2]  -- 联合查询
[WHERE ...]  -- 指定结果需满足的条件
[GROUP BY ...]  -- 指定结果按照哪几个字段来分组
[HAVING]  -- 过滤分组的记录必须满足的次要条件
[ORDER BY ...]  -- 指定查询记录按一个或多个条件排序
[LIMIT {[offset,]row_count | row_countOFFSET offset}];-- 指定查询的记录从哪条至哪条
```

# 扩展: 数据库级别的MD5加密

>**MD5信息摘要算法**（英语：MD5 Message-Digest Algorithm），一种被广泛使用的密码散列函数

```sql
-- 插入数据时进行MD5加密
insert into t_user(username,password) values("zhangsan",md5("123456"))

-- 查询时将用户传入的密码加密,比较加密后的字符串
select * from t_user where password =md5("123456")
```

# 扩展: MySQL 常用函数

>数据函数

```sql
 SELECT ABS(-8);  /*绝对值*/
 SELECT CEILING(9.4); /*向上取整*/
 SELECT FLOOR(9.4);   /*向下取整*/
 SELECT RAND();  /*随机数,返回一个0-1之间的随机数*/
 SELECT SIGN(0); /*符号函数: 负数返回-1,正数返回1,0返回0*/
```

>字符串函数

```sql
 SELECT CHAR_LENGTH('狂神说坚持就能成功'); /*返回字符串包含的字符数*/
 SELECT CONCAT('我','爱','程序');  /*合并字符串,参数可以有多个*/
 SELECT INSERT('我爱编程helloworld',1,2,'超级热爱');  /*替换字符串,从某个位置开始替换某个长度*/
 SELECT LOWER('KuangShen'); /*小写*/
 SELECT UPPER('KuangShen'); /*大写*/
 SELECT LEFT('hello,world',5);   /*从左边截取*/
 SELECT RIGHT('hello,world',5);  /*从右边截取*/
 SELECT REPLACE('狂神说坚持就能成功','坚持','努力');  /*替换字符串*/
 SELECT SUBSTR('狂神说坚持就能成功',4,6); /*截取字符串,开始和长度*/
 SELECT REVERSE('狂神说坚持就能成功'); /*反转
```

>日期和时间函数

```sql
 SELECT CURRENT_DATE();   /*获取当前日期*/
 SELECT CURDATE();   /*获取当前日期*/
 SELECT NOW();   /*获取当前日期和时间*/
 SELECT LOCALTIME();   /*获取当前日期和时间*/
 SELECT SYSDATE();   /*获取当前日期和时间*/
 
 -- 获取年月日,时分秒
 SELECT YEAR(NOW());
 SELECT MONTH(NOW());
 SELECT DAY(NOW());
 SELECT HOUR(NOW());
 SELECT MINUTE(NOW());
 SELECT SECOND(NOW());
```

>系统信息函数

```sql
 SELECT VERSION();  /*版本*/
 SELECT USER();     /*用户*/
```

>聚合函数

```sql
-- 聚合函数
 /*COUNT:非空的*/
 SELECT COUNT(studentname) FROM student;
 SELECT COUNT(*) FROM student;
 SELECT COUNT(1) FROM student;  /*推荐*/
 
 -- 从含义上讲，count(1) 与 count(*) 都表示对全部数据行的查询。
 -- count(字段) 会统计该字段在表中出现的次数，忽略字段为null 的情况。即不统计字段为null 的记录。
 -- count(*) 包括了所有的列，相当于行数，在统计结果的时候，包含字段为null 的记录；
 -- count(1) 用1代表代码行，在统计结果的时候，包含字段为null 的记录 。
 /*
 很多人认为count(1)执行的效率会比count(*)高，原因是count(*)会存在全表扫描，而count(1)可以针对一个字段进行查询。其实不然，count(1)和count(*)都会对全表进行扫描，统计所有记录的条数，包括那些为null的记录，因此，它们的效率可以说是相差无几。而count(字段)则与前两者不同，它会统计该字段不为null的记录条数。
 
 下面它们之间的一些对比：
 
 1）在表没有主键时，count(1)比count(*)快
 2）有主键时，主键作为计算条件，count(主键)效率最高；
 3）若表格只有一个字段，则count(*)效率较高。
 */
 
 SELECT SUM(StudentResult) AS 总和 FROM result;
 SELECT AVG(StudentResult) AS 平均分 FROM result;
 SELECT MAX(StudentResult) AS 最高分 FROM result;
 SELECT MIN(StudentResult) AS 最低分 FROM result;
```

