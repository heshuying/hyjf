<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="bond-nav">
            <div class="bond-nav-container">
                <div class="bond-nav-div"><a href="#">债权投资</a></div>
                <div class="bond-nav-div"><a class="bg-bgcolor" href="#">债权转让</a></div>
            </div>
    </div>
     <article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->  
             <div class="bond-investlist" id="bond-transferlist">
                <div class="bond-banner">
                    <h4>金融优质服务</h4>
                    <h5>定期理财、理财增值、信托业务、产品代理</h5>
                </div>
                <div style="padding-top: 20px; background: #f2f2f2;width: 1100px;float: left;"></div>
                <div class="bond-list">
                    <div class="bond-thead">
                        <div>
                            <div class="fl trf1">项目名称</div>
                            <div class="fl trf2">预期年化收益率</div>
                            <div class="fl trf3">投资期限</div>
                            <div class="fl trf4">折让率</div>
                            <div class="fl trf5">项目金额</div>
                            <div class="fl trf6 fl-padding">进度</div>
                            <div class="fl fl-padding trf7">状态</div>
                        </div>
                    </div>
                    <ul>
                        <a href="#">
                            <li class="trf1">青岛某加工厂</li>
                            <li class="trf2"><div class="yield">16.00%<span class="bg-add">+1%</span></div></li>
                            <li class="trf3">36个月</li>
                            <li class="trf4">
                            <div class="yield">0.2<span class="bg-add">%</span></div>
                            </li>
                            <li class="trf5">46,100.00元</li>
                            <li class="trf6">
                            <div class="bond-num">
                                <div class="progress-all">
                                    <div class="progress-cur"></div>
                                </div>
                                <div class="percent">
                                    <span>10%</span>
                                </div>
                            </div>
                            </li>
                            <li class="trf7">    
                                <div class="btn-large">投资</div>                     
                            </li>
                        </a>
                    </ul>
                    <ul>
                        <a href="#">
                            <li class="trf1">青岛某加工厂</li>
                            <li class="trf2"><div class="yield">16.00%<span class="bg-add">+1%</span></div></li>
                            <li class="trf3">36个月</li>
                            <li class="trf4">
                            <div class="yield">0.2<span class="bg-add">%</span></div>
                            </li>
                            <li class="trf5">46,100.00元</li>
                            <li class="trf6">
                            <div class="bond-num">
                                <div class="progress-all">
                                    <div class="progress-cur"></div>
                                </div>
                                <div class="percent">
                                    <span>10%</span>
                                </div>
                            </div>
                            </li>
                            <li class="trf7">    
                                <div class="btn-large">投资</div>                     
                            </li>
                        </a>
                    </ul>
                    
                </div>
                    <!--分页-->
                     <div class="pages-nav">
                         <div class="prev">上一页</div>
                         <a href="" class="active">1</a>
                         <a href="">2</a>
                         <a href="">3</a>
                         <a href="">4</a>
                         <a href="">...</a>
                         <a href="">50</a>
                         <a href="">51</a>
                         <a class="next">下一页</a>
                     </div>    
             </div>   
             
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/product/product-list.js"></script>
</body>
</html>