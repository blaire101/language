# Nodejs server.js

## helloworld webApp

**在我们创建 Node.js 第一个 "Hello, World!" 应用前，让我们先了解下 Node.js 应用是由哪几部分组成的:**

 1. 引入 required 模块：我们可以使用 require 指令来载入 Node.js 模块。
 2. 创建服务器：服务器可以监听客户端的请求，类似于 Apache 、Nginx 等 HTTP 服务器。
 3. 接收请求与响应请求 服务器很容易创建，客户端可以使用浏览器或终端发送 HTTP 请求，服务器接收请求后返回响应数据。


### 步骤一、引入 required 模块

**我们使用 require 指令来载入 http 模块，并将实例化的 HTTP 赋值给变量 http，实例如下:**

```nodejs
var http = require("http");
```

# NPM Brief Introduce

**NPM 是随同NodeJS一起安装的包管理工具，能解决NodeJS代码部署上的很多问题，常见的使用场景有以下几种:**

 - 允许用户从NPM服务器下载别人编写的第三方包到本地使用。
 - 允许用户从NPM服务器下载并安装别人编写的命令行程序到本地使用。
 - 允许用户将自己编写的包或命令行程序上传到NPM服务器供别人使用。

由于新版的nodejs已经集成了npm，所以之前npm也一并安装好了。同样可以通过输入 "npm -v" 来测试是否成功安装。命令如下，出现版本提示表示安装成功:

```nodejs
➜  ~ node -v
v4.4.0
➜  ~ npm -v
2.14.20
➜  ~
```

[more-info_runoob](http://www.runoob.com/nodejs/nodejs-npm.html)
