 <center><h1><b><font color='yellow'>Git操作详解</font></b></h1></center>

#  一、基础操作

## 1. 环境配置

#### 配置查询

```shell
#查看系统config
git config --system --list
　　
#查看当前用户（global）配置
git config --global  --list
```

#### 设置用户信息

```shell
#设置用户名
git config --global user.name “wangshijia”
#设置邮箱
git config --global user.email “523707408@qq.com”
```

## 2. Git仓库创建

#### 本地初始化

```shell
git init
```

#### 远程克隆

```shell
#克隆仓库的默认分支(master)
git clone http......
#克隆仓库的指定分支
git clone -b 分支名 https:....
#注意:同一仓库的不同分支的地址是相同的
```

#  二、本地仓库操作

## 1. 本地文件状态查询

#### 查询详细信息

```shell
git status
```

#### 查询简略信息

```shell
git status -s
```

## 2. 工作区域-->暂存区

#### 添加到暂存区

```shell
git add hello.txt #未跟踪或是已修改文件
```

#### 取消添加到暂存区

```shell
git reset hello.txt #已暂存文件
```

## 3.暂存区-->本地仓库

#### 提交指定文件到本地仓库

```shell
git commit -m "log..." hello.txt
```

#### 提交所有文件到本地仓库

```shell
git commit -m "log..."
```

#### 添加到暂存区并提交到本地仓库

```shell
git commit -a -m "log..." #只对已修改文件有用
```

## 4.日志查询

```shell
git log
```

#  三、远程仓库操作

## 1. 查看远程仓库

#### 查询仓库短名

```shell
git remote
```

#### 查询仓库简略信息

```shell
git status -v
```

#### 查询仓库详细信息

```shell
git status show origin#仓库名
```

## 2. 添加远程仓库

```shell
git remote add origin http...#<短名> <地址>
```

## 3.移除远程仓库

```shell
git remote rm origin#短名
#注意:移除远程仓库只是移除本地和远程的关系,并没有删除远程仓库
```

## 4.从远程仓库抓取和拉取

#### 抓取

```shell
git fetch origin master
#注意 抓取操作不会自动merge 
git merge origin master
```

#### 拉取

```shell
git pull origin master
```

## 5.推送到远程仓库

```shell
git push origin master
```

#  三、分支操作

## 1. 查看分支

#### 查询所有本地分支

```shell
git branch
```

#### 查询所有远程分支

```shell
git branch -r
```

#### 查询所有本地和远程分支

```shell
git branch -a
```

## 2. 创建分支

```shell
git branch b1#分支名
git branch -b newBranche#创建分支并切换
```

## 3.切换分支

```shell
git checkout b1#分支名
```

## 4.推送分支

```shell
git push origin newBranche#新键的分支名
```

## 5.合并分支

```shell
git merge b1#被合并分支名

#注意:合并时如果两个分支对同一文件的同一部分做了不同的修改,就会报冲突
#冲突需要手动解决,解决后通过git add 标识冲突已经解决
```

## 3.删除分支

#### 删除未修改分支

```shell
git branch -d b1#分支名
```

#### 删除已修改分支

```shell
git branch -D b1#分支名
```

