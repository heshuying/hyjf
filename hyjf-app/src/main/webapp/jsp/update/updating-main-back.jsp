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
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <style type="text/css">
    * {
        margin: 0;
        padding: 0;
    }

    .systerm-update {
        position: relative;
        font-family: "microsoft yahei";
    }

    .systerm-update>img:nth-child(2) {
        height: 66.5%;
    }

    .update-bottom {
        margin: 8% auto 0;
        display: inline-block;
    }

    .systerm-update>img {
        display: block;
        width: 100%;
    }

    .systerm-update-logo {
        width: 24%;
        margin: 0 auto 6%;
    }

    .systerm-update-logo>img {
        max-width: 100%;
        margin-top: 60%;
    }

    .systerm-update-img-1 {
        margin: 16% auto 6%;
        width: 100%;
    }

    .systerm-update-img-2 {
        width: 17.3%;
        margin: 0 auto 3%;
    }

    .systerm-cont {
        margin: auto;
        text-align: center;
        width: 100%;
        padding: 0 3% 7%;
        height: 100%;
        box-sizing: border-box;
        -webkit-box-sizing: border-box;
        background: url(../../img/systerm-update-bg.jpg) no-repeat;
        background-size: cover;
    }

    .systerm-title,
    .systerm-sub {
        color: #ff650a;
    }

    .systerm-title {
        font-weight: 600;
    }

    .systerm-sub {
        text-transform: Uppercase;
    }

    .systerm-ban .color-orange {
        color: #ff650a;
    }

    @media only screen and (max-width:480px) {
        .systerm-cont .systerm-title {
            font-size: 14px;
        }
        .systerm-cont .systerm-sub {
            font-size: 10px;
            line-height: 1.2;
            margin-top: 8px;
        }
        .systerm-cont .systerm-ban {
            font-size: 10px;
            margin-top: 30px;
            line-height: 1.6;
        }
    }

    @media only screen and (min-width:480px) {
        .systerm-cont .systerm-title {
            font-size: 23px;
        }
        .systerm-cont .systerm-sub {
            font-size: 12px;
            line-height: 1.2;
            margin-top: 10px;
        }
        .systerm-cont .systerm-ban {
            font-size: 16px;
            margin-top: 40px;
            line-height: 1.8;
        }
    }

    @media only screen and (min-width:768px) {
        .systerm-cont .systerm-title {
            font-size: 28px;
        }
        .systerm-cont .systerm-sub {
            font-size: 14px;
            line-height: 1.2;
            margin-top: 15px;
        }
        .systerm-cont .systerm-ban {
            font-size: 18px;
            margin-top: 50px;
            line-height: 2;
        }
    }
    </style>
</head>

<body>
    <div class="systerm-update">
        <div class="systerm-cont">
            <div class="systerm-update-logo"></div>
            <img src="../../img/systerm-update-2.png" class="systerm-update-img-2">
            <p class="systerm-title">
                江西银行资金存管系统停机维护通知
            </p>
            <p class="systerm-ban">
                为更好地为用户服务，
                <br /> 现接江西银行通知，江西银行存管系统将于
                <br /><span class="color-orange">2017年11月09日22:00至11月10日6:00</span><br />进行停机优化升级。
                <br /> 
                <br /> 在此期间，您可以正常登陆汇盈金服平台，但是将不能进行充值、投资、提现等交易操作。所有功能和服务将暂停使用（包括但不限于注册、登录、充值、投资、提现等服务），并显示升级维护页面。
                <br /> 
                <br /> 给您带来不便敬请见谅！
                <br /> 
                <br /> 感谢您一直以来对汇盈金服的支持和信任，我们将一如既往地为您提供优质安全的服务。
                <br />
                <br /> 如有任何疑问，请联系在线客服或直接拨打客服热线400-900-7878!
                <br />
                <br />
                <span class="color-orange update-bottom">更多公告，请关注PC页面</span>
            </p>

        </div>
    </div>
</body>

</html>