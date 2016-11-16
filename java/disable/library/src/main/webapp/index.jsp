<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<head>
    <meta charset="UTF-8">
    <title>Qunar图书系统</title>
    <link rel="stylesheet" href="http://freshzz.corp.qunar.com/2014_2_team7/prd/styles/index@<jsp:include page="/resource/ver/styles/index.css.ver"></jsp:include>.css">
</head>
<body>
    <!-- 弹层 -->
    <section class="PopBoxWrap">
    </section>
    <!-- 公共头部 -->
    <section class="logo">
        <div class="wrap">
            <div class="ft">
            </div>
            <div class="hd">
                <div><img src="http://sources.corp.qunar.com/QunarLibrary/logo.png" alt=""></div>
            </div>
            <div class="bd"><strong>Qunar图书馆</strong></div>
        </div>
    </section>
    <!-- 正文展示 -->
    <section class="main">
        <div class="main-wrap">
            <!-- 左 -->
            <div class="main-left">
                <!-- 书籍分类列表 -->
                <section class="m-nav">
                    <div class="wrap">
                        <div class="nav">
                          
                        </div>
                    </div>
                </section>
            </div>
            <!-- 右 -->
            <div class="main-right">
                <!-- 搜索块 -->
                <section class="m-search">
                    <div class="wrap">
                        <div class="fm">
                            <form action="" class="search">
                                <input type="text" id="querystring" name="quertstring" size="20" placeholder="请输入书名" class="input">
                                <button class="btn btn-search toclick" data-type="user">搜索</button>
                            </form>
                        </div>
                    </div>
                </section>
                <!-- 分类块 -->
                <section class="m-kind">

                </section>
                <!-- 结果块 -->
                <section class="m-result">
                    <div class="wrap">
                        <div>
                            <ol class="list">
                            </ol>
                        </div>
                        
                    </div>
                </section>
            </div>
        </div>
    </section>
    <script src="http://freshzz.corp.qunar.com/2014_2_team7/prd/scripts/index/index@<jsp:include page="/resource/ver/scripts/index/index.js.ver"></jsp:include>.js"></script>
</body>
</html>