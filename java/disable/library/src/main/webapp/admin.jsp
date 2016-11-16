<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!doctype html>
<head>
    <meta charset="UTF-8">
    <title>Qunar图书系统</title>
     <link rel="stylesheet" href="http://freshzz.corp.qunar.com/2014_2_team7/prd/styles/manager/manager@<jsp:include page="/resource/ver/styles/manager/manager.css.ver"></jsp:include>.css">
    <!--<link rel="stylesheet" href="http://freshzz.corp.qunar.com/prd/styles/manager/manager@sds.css">-->
</head>
<body>
    <!-- 弹层 -->
    <section class="PopBoxWrap">
        
    </section>
    <!-- 公共头部 -->
    <section class="logo">

        <div class="wrap">
            <div class="hd">
                <div><img src="http://sources.corp.qunar.com/QunarLibrary/logo.png" alt=""></div>
            </div>
            <div class="bd"><strong>Qunar图书馆</strong></div>
            <div class="ft">
            </div>
        </div>
    </section>

    <!-- 正文展示 -->
    <section class="main">
        <div class="main-wrap">
            <!-- 左 -->
            <div class="main-left">
                <!-- 导航 -->
                <section class="m-nav">
                    <div class="wrap">
                        <div class="pagekind">
                            <div class="divtobtn"><a href="#" data-type="borw" class="curshow">借 书</a></div>
                            <div class="divtobtn"><a href="#" data-type="ret">还 书</a></div>
                            <div class="divtobtn"><a href="#" data-type="view">查看借阅</a></div>
                            <div class="divtobtn"><a href="#" data-type="bookmag">书籍管理</a></div>
                            <div class="divtobtn"><a href="#" data-type="sys">系统设置</a></div>
                            <div class="divtobtn"><a href="#" data-type="log">日志信息</a></div>
                        </div>
                    </div>
                </section>
            </div>
            <!-- 右 -->
            <div class="main-right">
                <!-- 还书块 -->
                <section class="m-borw">
                    <div class="wrap">
                        <div class="search">
                            <div class="fm">
                                <div class="booksearch">
                                    <input type="text" id="bookid"placeholder="请输入书籍编号" class="input">
                                    <button class="btn btn-search toclick" data-type="book">搜索</button>
                                </div>
                                <div class="usersearch">
                                    <input type="text" class="input" id="userrtx" name="userrtx"placeholder="请输入员工RTX">
                                    <button class="btn btn-search toclick" data-type="user">搜索</button>
                                </div>
                            </div>
                        </div>
                        <div class="result">
                            <div class="bookresult hide">
                                <fieldset>
                                    <legend>书籍信息</legend>
                                    <div class="bookinfo">
                                        <div class="tag">
                                            <span class="bookid">编号</span>
                                            <span class="bookname">书名</span>
                                            <span class="otherinfo">其他信息</span>
                                            <span class="select"><input type="checkbox" class="checkbox" id="checkAll" data-name="bookid"></span>
                                        </div>
                                    </div>
                                </fieldset>
                            </div>
                            <div class="userresult hide">
                            </div>
                            <div class="submit hide">
                                <a href="#" class="atobtn toclick" data-type="submit">确 认</a>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </section>
     <script src="http://freshzz.corp.qunar.com/2014_2_team7/prd/scripts/manager/index@<jsp:include page="/resource/ver/scripts/manager/index.js.ver"></jsp:include>.js"></script>
    <!--<script src="http://freshzz.corp.qunar.com/prd/scripts/manager/index@sds.js"></script>-->
</body>
</html>