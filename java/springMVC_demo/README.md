# SpringMVC Demo

|日期|版本|修改人员|确认人员|
|--------|-------|---------|--------|
2016-09-11 | 1.0 | Blair Chan | Libin Chan


## 1. starting point

为一些新手和我个人备份，写一个入门`整洁`的编写 java 后端程序的代码架。(当然我也是比较业余的人)

> 个人认为，写这样的程序，最重要的是 `整洁与约定规范` 而不是多么高深的技术，让接手你代码的人，不痛苦，这才是成功
 
## 2. involved technology

 - Java
 - Restful
 - SpringMVC
 - Mybatis
 - logback
 - Spring-task

> logback : 一个“可靠、通用、快速而又灵活的Java日志框架”。
> Spring-task 编写非web程序，仅仅是后台需要定时跑的任务，经常被用到。

## 3. how to run

### 3.1 修改数据库连接信息

编辑 ~/resources/props/db.properties 将其中的

```
# main mysql lib dataSource
main.jdbc.driverClassName=com.mysql.jdbc.Driver
main.jdbc.url=jdbc:mysql://192.168.***.**:3306/testdb01
main.jdbc.username=your_username
main.jdbc.password=your_password
```

改为你自己的 dataSource 连接信息

### 3.2 数据库中建立你用到的表

参见语句  ~/resources/sql/projects.sql 在你的 数据库 中执行其中语句，建立 table `user`.

### 3.3 确认需要的环境已准备好

> 确认 ~/resources/logback.xml 中，日志的打印路径，是否适合你的环境  
> 我这里是 /data0/www/logs/ ， 如有需要改变，请自行更改。（如果为 windows 环境，请注意路径是否正确）

### 3.4 编译-打包-启动jetty
```
➜  github ll
total 0
drwxr-xr-x 13 hp staff 442 Sep 10 14:03 language/
➜  github cd language/java/springMVC_demo
➜  springMVC_demo git:(master) ✗ ll
total 24
-rw-r--r-- 1 hp staff   665 Sep 11 15:52 README.md
-rw-r--r-- 1 hp staff 10712 Sep 11 15:10 pom.xml
drwxr-xr-x 4 hp staff   136 Sep 10 13:30 src/
➜  springMVC_demo git:(master) ✗ mvn clean
➜  springMVC_demo git:(master) ✗ mvn compile
➜  springMVC_demo git:(master) ✗ mvn clean package
➜  springMVC_demo git:(master) ✗ mvn jetty:run
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building x_demo Maven Webapp 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] FrameworkServlet 'mvc-dispatcher': initialization completed in 572 ms
[INFO] Started SelectChannelConnector@0.0.0.0:8080
[INFO] Started Jetty Server
[INFO] Starting scanner at interval of 5 seconds.
```

> 当然你也可以通过 IDEA -> Maven Projects -> Plugins -> jetty:run 启动 (或者 Tomcat 启动)

启动成功后，这时你可以在你的浏览器分别访问以下接口，查看效果了

```
http://localhost:8080/
http://localhost:8080/user/getusers
http://localhost:8080/user/addusers
http://localhost:8080/user/getusers
http://localhost:8080/user/getuser/2

------

http://localhost:8080/user/getuser/2
{
  "status": 0,
  "errmsg": "success",
  "data": {
    "id": 2,
    "firstName": "Andy",
    "lastName": "Wong",
    "age": 31
  }
}
```

> 在你测试的时候，如果你想在浏览器中看到格式化后的json，请自行安装 chrome 相关的json插件等。

## 4. attentions

> 注意： 版本控制中，涉及的敏感 库地址，用户名，密码 等 不上传。



