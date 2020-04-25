<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>双十二活动</title>
    <style  type="text/css">
    *{margin:0;padding:0}
.double-active1-introduce img {
	display: block;
	width: 100%;
}

.bg-purple {
	background: #a13aeb;
}

.introduce-table-title {
	width: 6.94rem;
	margin: 0 auto
}

.introduce-table {
	width: 11.22rem;
	background: #ff7200;
	border-radius: 0.5rem;
	-webkit-border-radius: 0.5rem;
	margin: 0 auto;
	box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding: 0.3rem 0.38rem;
}

.introduce-table {
	color: #df373d;
	font-size: 0.50rem;
}

.introduce-table-list {
	background: #FFFFFF;
}

.introduce-table dl {
	margin-bottom: 0.2rem;
}

.introduce-table dt {
	display: block;
	background: #fcd8a2;
	padding-right: 0.7rem;
}

.introduce-table dt span {
	display: block;
	text-align: center;
	float: left;
}

.introduce-table dt span.num {
	width: 2.00rem;
}

.introduce-table dt span.cont {
	width: 7.00rem;
}

.introduce-table dt span.img {
	width: 0.48rem;
	padding-left: 0.26rem;
	vertical-align: middle;
}

.introduce-table dt span.cont i {
	display: block;
	font-size: 0.4rem;
	font-style: normal;
}

.introduce-table dd {
	padding: 0.5rem;
	text-align: center;
	color: #ff7100;
	font-size: 0.4rem;
	background: #ffffff;
	line-height: 1.8;
}

.condition-1 dt {
	height: 1.50rem;
}

.condition-2 dt {
	height: 2.57rem;
}

.condition-3 dt {
	height: 3.25rem
}

.condition-1 dt span.num {
	height: 1.50rem;
	line-height: 1.50rem;
}

.condition-1 dt span.img {
	padding-top: 0.48rem;
}

.condition-1 dt span.cont {
	padding-top: 0.43rem
}

.condition-2 dt span.num {
	height: 2.57rem;
	line-height: 2.57rem;
}

.condition-2 dt span.img {
	padding-top: 1.048rem;
}

.condition-2 dt span.cont {
	padding-top: 0.48rem
}

.condition-3 dt span.num {
	height: 3.25rem;
	line-height: 3.25rem;
}

.condition-3 dt span.img {
	padding-top: 1.348rem;
}

.condition-3 dt span.cont {
	padding-top: 0.48rem
}

.rotate {
	transform: rotate(-90deg);
	-ms-transform: rotate(-90deg);
	-moz-transform: rotate(-90deg);
	-webkit-transform: rotate(-90deg);
	-o-transform: rotate(-90deg);
}

.clear {
	clear: both;
}
    </style>
</head>
<body class="bg-purple">
<div class="double-active1-introduce">
    <img src="${ctx}/img/act20171212/double-active1-introduce01.jpg" alt="" />
    <img src="${ctx}/img/act20171212/double-active1-introduce02.jpg" alt="" />
    <img src="${ctx}/img/act20171212/double-active1-introduce03.jpg" alt="" />
    <img src="${ctx}/img/act20171212/double-active1-introduce04.jpg" alt="" />
    <img src="${ctx}/img/act20171212/double-active1-introduce05.jpg" alt="" />
    <img src="${ctx}/img/act20171212/double-active1-introduce06.jpg" alt="" />
    <div class="introduce-table">
        <div class="introduce-table-title">
            <img src="${ctx}/img/act20171212/introduce-title.png" alt="" />
        </div>
        <div class="introduce-table-list">
            <dl class="condition-1">
                <dt>
                    <span class="num"><b>1只</b></span>
                    <span class="cont">
								<b>60元代金券一张</b>
							</span>
                    <span class="img">
								<img src="${ctx}/img/act20171212/introduce-arrows.png" alt="" />
							</span>
                </dt>
                <dd>60元代金券单笔投资达10000元</dd>
            </dl>
            <dl class="condition-1">
                <dt>
                    <span class="num"><b>3只</b></span>
                    <span class="cont">
								<b>100元代金券一张</b>
							</span>
                    <span class="img">
								<img src="${ctx}/img/act20171212/introduce-arrows.png" alt="" />
							</span>
                </dt>
                <dd>100元代金券单笔投资达10000元可用</dd>
            </dl>
            <dl class="condition-1">
                <dt>
                    <span class="num"><b>5只</b></span>
                    <span class="cont">
								<b>200元代金券一张</b>
							</span>
                    <span class="img">
								<img src="${ctx}/img/act20171212/introduce-arrows.png" alt="" />
							</span>
                </dt>
                <dd>200元代金券单笔投资达20000元可用</dd>
            </dl>
            <dl class="condition-2">
                <dt>
                    <span class="num"><b>10只</b></span>
                    <span class="cont">
								<b>500元红包</b>
								<i>（内含200元代金券一张+300元代金券一张）</i>
							</span>
                    <span class="img">
								<img src="${ctx}/img/act20171212/introduce-arrows.png" alt="" />
							</span>
                </dt>
                <dd>200元代金券单笔投资达20000元可用<br />
                    300元代金券单笔投资达30000元可用
                </dd>
            </dl>
            <dl class="condition-2">
                <dt>
                    <span class="num"><b>30只</b></span>
                    <span class="cont">
								<b>1200元红包</b>
								<i>（内含600元代金券一张+300元代金券两张）</i>
							</span>
                    <span class="img">
								<img src="${ctx}/img/act20171212/introduce-arrows.png" alt="" />
							</span>
                </dt>
                <dd>
                    300元代金券单笔投资达30000元可用<br />
                    600元代金券单笔投资达60000元可用
                </dd>
            </dl>
            <dl class="condition-2">
                <dt>
                    <span class="num"><b>60只</b></span>
                    <span class="cont">
								<b>3000元红包</b>
								<i>（内含1000元代金券一张+800元代金券一张+600元代金券两张）</i>
							</span>
                    <span class="img">
								<img src="${ctx}/img/act20171212/introduce-arrows.png" alt="" />
							</span>
                </dt>
                <dd>
                    600元代金券单笔投资达60000元可用<br />
                    800元代金券单笔投资达80000元可用<br />
                    1000元代金券单笔投资达100000元可用<br />
                </dd>
            </dl>
            <dl class="condition-3">
                <dt>
                    <span class="num"><b>100只</b></span>
                    <span class="cont">
								<b>6000元红包</b>
								<i>（内含2000元代金券一张+1000元代金券一张+800元代金券三张+600元代金券一张）</i>
							</span>
                    <span class="img">
								<img src="${ctx}/img/act20171212/introduce-arrows.png" alt="" />
							</span>
                </dt>
                <dd>
                    600元代金券单笔投资达60000元可用<br />
                    800元代金券单笔投资达80000元可用<br />
                    1000元代金券单笔投资达100000元可用<br />
                    2000元代金券单笔投资达200000元可用<br />
                </dd>
            </dl>
        </div>
    </div>
    <img src="${ctx}/img/act20171212/double-active1-introduce07.jpg" alt="" />
</div>
</body>
</html>
<script src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript">
    document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
    $(window).on('resize', function() {
        document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
    });
</script>
<script type="text/javascript">
    $('.introduce-table-list').delegate('dt','click',function(){
        var dd = $(this).next('dd');
        var imRotate = $(this).find('.img img');
        if(dd.is(':hidden')){
            dd.slideDown();
            imRotate.removeClass('rotate')
        }else{
            dd.slideUp();
            imRotate.addClass('rotate')
        }
    })
</script>
