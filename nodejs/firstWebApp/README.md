## Nodejs server.js

## http helloworld webApp

**在我们创建 Node.js 第一个 "Hello, World!" 应用前，让我们先了解下 Node.js 应用是由哪几部分组成的:**

 1. 引入 required 模块：我们可以使用 require 指令来载入 Node.js 模块。
 2. 创建服务器：服务器可以监听客户端的请求，类似于 Apache 、Nginx 等 HTTP 服务器。
 3. 接收请求与响应请求 服务器很容易创建，客户端可以使用浏览器或终端发送 HTTP 请求，服务器接收请求后返回响应数据。


### 步骤一、引入 required 模块

**我们使用 require 指令来载入 http 模块，并将实例化的 HTTP 赋值给变量 http，实例如下:**

```nodejs
var http = require("http");
```
