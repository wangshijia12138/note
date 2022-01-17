 <center><h1><b><font color='grey'>SQL操作大全</font></b></h1></center>

# DML(数据操作语言)

## 1. INSERT-添加数据

```sql
-- 为表中的每一列添加数据
Insert into 表名 values (数据1,数据2);
-- 为表中的指定列添加数据
Insert into 表名(列名1,列名2) values (数据1,数据2);
```

## 2. DELETE -删除数据

```sql
-- 删除表中部分数据
delete from 表名 [where 条件]
-- 删除表中所有数据
delete from 表名 -- 不推荐,效率较低,需要一条一条删除	
truncate table 表名 -- 推荐,效率高,它直接删除了表,并创建了同名的新表
```

## 3. UPDATE-更新数据

```sql
-- 更新该列所有值
UPDATE 表名  SET 列名1=值,列名2 =值
-- 更新该列某一个行的值
UPDATE 表名  SET 列名1=值,列名2 =值 [ WHERE 条件]
```



