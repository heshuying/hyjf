<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/validate.css"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
	<title>绑定银行卡</title>
	</head>
	<body class="bg_grey">
		<div class="wx-register">
             <form method="post" id="form1" action="${ctx}/bank/user/bindCard/bindCard.do" >
                 <div class="wx-register-con">
                     <div class="wx-register-txt">
                         <input type="hidden" name="sign" id="sign" value="${sign}" />
                         <input type="hidden" name="version" id="version" value="${version}" />
                         <input type="hidden" name="netStatus" id="netStatus" value="${netStatus}" />
                         <input type="hidden" name="token" id="token" value="${token}" />
                         <input type="hidden" name="platform" id="platform" value="${platform}" />
                         <input type="hidden" name="randomString" id="randomString" value="${randomString}" />
                         <input type="hidden" name="order" id="order" value="${order}" />
                         <input type="number" name="cardNo" id="cardNo"  class="" maxlength="19" placeholder="请填写常用银行卡卡号" oninput="if(value.length>19)value=value.slice(0,19)"  data-conditional="isbank" data-required="true" data-descriptions="bank"/>
                     </div>
                </div>       
                 <input type="submit" class="newRegReg wx-register-btn"  value="确定绑卡" /> 
             </form>
         </div>
	</body>
</html>
<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/js/jquery-mvalidate.js" type="text/javascript" charset="utf-8"></script>
<script>
    $(function(){
        function bankCardVer(cardnum){
            if(cardnum.length<16 || cardnum.length>19){
                return false;
            }
            var sum = 0;
            var digit = 0;
            var addend = 0;
            var timesTwo = false;
            for (var i = cardnum.length - 1; i >= 0; i--)
            {
                digit = cardnum[i] - '0';
                if (timesTwo)
                {
                    addend = digit * 2;
                    if (addend > 9) {
                        addend -= 9;
                    }
                }
                else {
                    addend = digit;
                }
                sum += addend;
                timesTwo = !timesTwo;
            }
            var modulus = sum % 10;
            return modulus == 0;
        }


        $.mvalidateExtend({
//          验证银行卡号
           
        });
        
         $("#form1").mvalidate({
            type:1,
            onKeyup:true,
            sendForm:true,
            firstInvalidFocus:false,
            valid:function(event,options){
               
            },
            invalid:function(event, status, options){
                //点击提交按钮时,表单未通过验证触发函数
            },
            eachField:function(event,status,options){
                //点击提交按钮时,表单每个输入域触发这个函数 this 执向当前表单输入域，是jquery对象
            },
            eachValidField:function(val){},
            eachInvalidField:function(event, status, options){},
             conditional:{
                isbank:function(val,options){
                    return bankCardVer(val);
                }
            },
            descriptions:{
                bank:{
                    required : '请输入银行卡号',
                    conditional : '请输入正确的银行卡号',
                }
            },   
        })
    })
</script>