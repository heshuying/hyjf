<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="UTF-8">
    <title>维护通知</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <style type="text/css">
        *{
            margin: 0;
            padding: 0;
            font-family: "Microsoft YaHei";
        }
        .system-update{
            width:100%;
            height:100%;
            min-height:1020px;
            min-height: 100vh;
            background:url('${ctx}/img/systerm-update-bg.jpg') no-repeat center center;
            background-size:100% 100%;
            position: relative;
        }
        .sy-container{
            font-size:16px;
            width: 935px;
            padding-top: 130px;
            margin-left: auto;
            margin-right: auto;
        }
        .sy-logo{
            width: 414px;
            margin: 0 auto;
            height: 50px;
            margin-bottom: 60px;
        }
        .sy-logo-big{
            width: 176px;
            height: 50px;
            float: left;
        }
        .sy-logo-bank{
            width: 190px;
            height: 50px;
            float: right;
        }
        .sy-fly{
            width: 135px;
            height: 136px;
            margin:0 auto;
            margin-bottom: 40px;
        }
        .sy-title{
            text-align:center;
            margin-bottom: 50px;
        }
        .sy-title p{
            width: 100%;
            display: block;
        }
        .sy-big-title{
            font-size: 26px;
            color:#ff5b29;
            margin-bottom: 15px;
        }
        .sy-desc{
            color:#ff5b29;
            font-size: 14px;
        }
        .sy-bm-desc{
            text-align: center;
            color: #404040;
        }
        .sy-bm-desc p{
            width: 100%;
            display: block;
            margin-bottom: 8px;
            font-size:16px;
        }
        .sy-bm-desc p a{
            color: #ff5b29;
            font-size: 21px;
        }
        .sy-value{
            color: #ff5b29;
            font-weight:normal
        }
        .sy-value-time{
            font-size: 20px;
        }
        .sy-bm-tel{
            margin-bottom: 35px;
        }
        .sy-big-bmdesc{
            font-size: 18px;
        }
        .overlayer{
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,.7);
            display: none;
            z-index: 9;
        }
        .overlayer-main{
            width: 483px;
            top: 50%;
            height: 622px;
            position: fixed;
            z-index: 9999;
            left: 50%;
            margin-left: -243px;
          
            z-index: 999;
            margin-top:-320px;
            background:url('${ctx}/img/wraper-mainbg.png') no-repeat;
        }
        .panel{
        }
        .stage-item{
            margin-top:40px;
        }
        .open{
            margin-right: 50px;
            margin-left: 50px;
        }
        .open-p{
            margin-top:70px;
        }
        .over-article{
            margin-top: 236px;
            padding: 0 20px 0 20px;
            width: 443px;
            height: 280px;
            background: #fff;
        }
        .over-article-con{
            overflow-y:auto;
            height: 280px;
            width: 448px;
            float: left;
            margin-top: 15px;
        }
        .over-article p{
            font-size: 12px;
            letter-spacing: 1px;
            /*margin-top: 5px;
            margin-bottom: 5px;*/
            line-height: 1.5;
            margin-top: 5px;
            margin-bottom: 5px;
        }
        .over-article p.over-value{
            color: #ff5b29;
           
        }
        .over-top{
            margin-top: 20px;
        }
        .over-btn{
            width: 100%;
            height: 70px;
            background: #fff;
            text-align: center;
            padding-top: 35px;
            border-radius: 0 0 20px 20px;
        }
        .over-btn a{
            text-decoration: none;
            font-size:16px;
            color: #fff;
            width: 235px;
            height: 35px;
            background: #fc6524;
            display: block;
             line-height: 35px;
            margin:0 auto;
           /* margin-top: 25px;*/
        }
        .over-close{
            width: 35px;
            height: 35px;
            cursor: pointer;
            margin-right: 15px;
            float: right;
            margin-top: 113px;
        }
        .over-img{
            max-width: 431px;
        }
        .noticelayer{
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,.7);
            display: none;
            z-index: 9;
        }
        .noticelayer-main{
            width: 824px;
            top: 50%;
            height: 590px;
            position: fixed;
            z-index: 9999;
            left: 50%;
            margin-left: -412px;
            z-index: 999;
            margin-top:-295px;
           
        }
        .notice-border{
            border:12px solid #f0d4bd;
            float: left;
            height: 565px;
            width: 800px;
            overflow: auto;
            background: #fff;
        }
        .notice-close{
            position: absolute;
            right: -55px;
            top: 0;
            cursor: pointer;
        }
        /*客服*/
        .toolbar {
            /*position: fixed;
            bottom: 127px;
            right: 400px;
            z-index: 1;*/
            z-index: 1;
            display: block;
            position: fixed;
            bottom:25%;
            left: 50%;
            margin-left: 405px;
            
        }
        .toolbar ul {
            margin-left: auto;
            margin-right: auto;
            height: 100%;
        }
        .toolbar li.item {
            display: block;
            width: 160px;
            height: 106px;
        
            float: left;
            cursor: pointer;
            position: relative;
        }
        .toolbar li.item .icon{
            width: 160px;
            height: 106px;
        }
        .toolbar li.item .icon img{           
            width: 100%;
            text-align: center;
        }
        .toolbar li.item a {
            display: block;
        }
        .toolbar li.item .hover-box {
            width: 160px;
            height: 106px;
            display: none;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 100;
            text-decoration: none;
        }
        .toolbar li.item:hover .hover-box {
            display: block;
        }
        .panel{
            display: none;
        }
        .sy-fly img{
            width:100%;
        }
        @media screen and (max-width : 768px) {
            .system-update{
                height: auto;
                box-sizing: border-box;
                padding-bottom: 10px;
            }
            .sy-container{
                font-size:14px;
                width: 90%;
                padding-top: 30px;
                margin-left: auto;
                margin-right: auto;
            }
            .sy-logo{
                width: 30%;
                margin: 0 auto;
                height: 50px;
                margin-bottom: 60px;
            }
            .sy-fly{
                max-width: 30%;
                height: auto;
                margin:0 auto;
                margin-bottom: 20px;
            }
  
            .sy-title{
                text-align:center;
                margin-bottom: 20px;
            }
            .sy-title p{
                width: 100%;
                display: block;
            }
            .sy-big-title{
                font-size: 16px;
                color:#ff5b29;
                margin-bottom: 15px;
            }
            .sy-bm-desc p{
                font-size: 13px;
            }
            .sy-desc{
                color:#ff5b29;
                font-size: 13px;
                margin-bottom: 0;
            }
            .sy-value-time{
                font-size: 14px;
            }
        }
        
    </style>
</head>

<body style="height: 100%;">
    <div class="system-update">
        <div class="sy-container">
            <div class="sy-fly"><img src="${ctx}/img/systerm-update-2.png"></div>
            <div class="sy-title">
                <p class="sy-big-title">系统升级停机维护通知</p>
                <p class="sy-desc">NOTICE ON UPGRADE OF REMITTANCE SERVICE PLATFORM</p>
            </div>
            <div class="sy-bm-desc">
                <p class="sy-big-bmdesc">平台系统将于<strong class="sy-value sy-value-time">2018年1月25日23:00至1月26日1:00 </strong>进行停机优化升级。</p>
                <br>
                <p>在此期间，将无法访问汇盈金服（www.hyjf.com）。</p>
                <p>给您带来不便敬请见谅！</p>
                <br>
                <p>感谢您一直以来对汇盈金服的支持和信任，我们将一如既往地为您提供优质安全的服务。</p>
                <p>如有任何疑问，请联系在线客服或直接拨打客服热线400-900-7878!</p>
                <br />
                <p>惠众商务顾问（北京）有限公司</p>
            </div>
        </div>
    </div> 
</body>
</html>