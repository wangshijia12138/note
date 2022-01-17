 <center><h1><b><font color='yellow'>日常Git使用场景</font></b></h1></center>

> 场景一:需要获得项目代码,本目前本地没有，我们可以找到Git地址，然后**Clone**到本地

```shell
#克隆仓库的默认分支(master)
git clone http......
#克隆仓库的指定分支
git clone -b 分支名 https:....
```

>场景二:接到了任务,我们在目前项目的基础上新建分支,进行开发

```shell
#创建分支并切换
git branch -b newBranche
```

>场景三:开发过程中,我们项目基础上进行了新增和修改,需要提交代码到本地仓库

```shell
git add .
git commit  -m "try to commit files to GitHub"
```

>场景四:一切顺利,我们将开发好的代码推送到本地

```shell
# 可能有其他人在开发过程中修改了远程仓库,所以需要先拉取,保证本地仓库与远程仓库一致,如果有冲突,手动解决
git pull origin master
#将本地开发好的代码推送到远程仓库
git push origin master
```

>场景五:代码写到一半,需要查看同事另一个分支上的代码

```shell
# 手动拉取远程仓库更新的信息
git fetch origin master  
# 切换到他的分支
git checkout  分支名   
```

>