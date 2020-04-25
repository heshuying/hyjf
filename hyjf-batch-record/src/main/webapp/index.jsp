<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="">
        <meta name="keywords" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="renderer" content="webkit">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="apple-mobile-web-app-title" content="Amaze UI" />
        <meta name="msapplication-TileColor" content="#0e90d2">
        <link rel="stylesheet" href="${ctx}/assets/css/amazeui.min.css">
        <link rel="stylesheet" href="${ctx}/assets/css/app.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
    </head>
	
	<body>
	
	       <div class="header">
            <div class="am-g">
                <h1>生成文件下载地址</h1>
                <p class="am-monospace">
               	 	host + /data/upfiles/createFile/ + 文件名<br/>
                   	 例（newweb）：https://newweb.hyjf.com/hyjf-batch-record/data/upfiles/createFile/ + 文件名 <br/>
                   	即信处理文件会有行数要求，单个文件不要超过9999行，所以如果文件过大，需要执行下面的文件分割，分割完成后文件里面的批次号需要手动修改！
                </p>
               	<h1>结果文件存储地址</h1>
                <p class="am-monospace">
               	 	/data/upfiles/resultFile/ + 文件名<br/>
               	 	这个文件应由即信返回，然后交给运维，放到这个目录，处理结果文件时只需传文件名
                </p>
            </div>
            <hr>
        </div>
        <div class="am-g">
            <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
                <form name="f1" id="f1" action="${ctx}/dataTransfer/batchOpenAccountRequset" class="am-form">
                    <fieldset>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '批量开户生成文件'}" class="am-btn am-btn-primary" value="">批量开户生成文件</button>
                        </div>
                    </fieldset>
                </form>
                <hr>
                <form name="f2" id="f2" action="${ctx}/dataTransfer/batchOpenAccountResult" method="post" class="am-form">
                    <fieldset>
                        <div class="am-form-group">
                            <label for="">文件名称：</label>
                            <input type="text" name="fileName" id="" value="" />
                        </div>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '批量开户结果文件处理'}" class="am-btn am-btn-primary" value="">批量开户结果文件处理</button>
                        </div>
                    </fieldset>
                </form>
                <hr>
                
                <form class="am-form" name="f5" id="f5" action="${ctx}/dataTransfer/subjectTransferRequest">
                    <fieldset>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '标的迁移生成文件'}" class="am-btn am-btn-primary" value="">标的迁移生成文件</button>
                        </div>
                    </fieldset>
                </form>
                <hr>
                <form class="am-form" name="f6" id="f6" action="${ctx}/dataTransfer/subjectTransferResult" method="post">
                    <fieldset>
                        <div class="am-form-group">
                            <label for="">文件名称：</label>
                            <input type="text" name="fileName" id="" value="" />
                        </div>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '标的迁移结果文件处理'}" class="am-btn am-btn-primary" value="">标的迁移结果文件处理</button>
                        </div>
                    </fieldset>
                </form>
                <hr>
                
                <form class="am-form" name="f3" id="f3" action="${ctx}/dataTransfer/debtTransferRequest">
                    <fieldset>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '债权迁移生成文件'}" class="am-btn am-btn-primary" value="">债权迁移生成文件</button>
                        </div>
                    </fieldset>
                </form>
                <hr>
                <form class="am-form" name="f4" id="f4" action="${ctx}/dataTransfer/debtTransferResult" method="post">
                    <fieldset>
                        <div class="am-form-group">
                            <label for="">文件名称：</label>
                            <input type="text" name="fileName" id="" value="" />
                        </div>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '债权迁移结果文件处理'}" class="am-btn am-btn-primary" value="">债权迁移结果文件处理</button>
                        </div>
                    </fieldset>
                </form>
                <hr>

                <!--前面还有两个form表单，此处暂用name="f9"  -->
                <form class="am-form" name="f9" id="f9" action="${ctx}/dataTransfer/sigtranTransferRequest">
                    <fieldset>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '签约关系迁移生成文件'}" class="am-btn am-btn-primary" value="">签约关系迁移生成文件</button>
                        </div>
                    </fieldset>
                </form>
                <hr>
                <form class="am-form" name="f10" id="f10" action="${ctx}/dataTransfer/sigtranTransferResult">
                    <fieldset>
                        <div class="am-form-group">
                            <label for="">文件名称：</label>
                            <input type="text" name="fileName" id="" value="" />
                        </div>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '签约关系迁移结果文件处理'}" class="am-btn am-btn-primary" value="">签约关系迁移结果文件处理</button>
                        </div>
                    </fieldset>
                </form>
                <hr>
                <form class="am-form" name="f11" id="f11" action="${ctx}/dataTransfer/segFile">
                    <fieldset>
                        <div class="am-form-group">
                            <label for="">分割行数（例：9999）：</label>
                            <input type="number" name="lineNum" id="lineNum" value="" />
                        </div>
                        <div class="am-form-group">
                            <label for="">文件名称：</label>
                            <input type="text" name="fileName" id="" value="" />
                        </div>
                        <div class="am-form-group">
                            <label for="fileType">文件类型：</label>
                            <select name="fileType" id="fileType">
                                <option value="0">批量开户</option>
                                <option value="1">债权迁移</option>
                                <option value="2">标的迁移</option>
                                <option value="3"> 签约关系迁移</option>
                            </select>
                            <br/>
                        </div>
                        <div class="am-form-group">
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '分割文件'}" class="am-btn am-btn-primary" value="">分割文件</button>
                        </div>
                    </fieldset>
                </form>
                
                
                <hr>
                <form class="am-form" name="f11" id="f11" action="${ctx}/couponInterest/update">
                    <fieldset>
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '优惠券收益累加'}" class="am-btn am-btn-primary" value="">优惠券收益累加</button>
                    </fieldset>
                </form>
                <form class="am-form" name="f12" id="f12" action="${ctx}/increaseInterest/update">
                    <fieldset>
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '融通宝加息收益累加'}" class="am-btn am-btn-primary" value="">融通宝加息收益累加</button>
                    </fieldset>
                </form>  
				<form class="am-form" name="f14" id="f14" action="${ctx}/manageFee/update">
                    <fieldset>
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '借款人管理费'}" class="am-btn am-btn-primary" value="">借款人管理费计算</button>
                    </fieldset>
                </form>
                <form class="am-form" name="f15" id="f15" action="${ctx}/interestSum/update">
                    <fieldset>
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '累计收益'}" class="am-btn am-btn-primary" value="">累计收益计算</button>
                    </fieldset>
                </form>
                  <form class="am-form" name="f13" id="f13" action="${ctx}/bankInvestSum/update">
                    <fieldset>
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '累计投资计算'}" class="am-btn am-btn-primary" value="">累计投资计算</button>
                    </fieldset>
                </form>
                <form class="am-form" name="f16" id="f16" action="${ctx}/bankTotal/update">
                    <fieldset>
                            <button type="button" style="display: none" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '账户总资产'}" class="am-btn am-btn-primary" value="">账户总资产计算</button>
                    </fieldset>
                </form>
                <form class="am-form" name="f17" id="f17" action="${ctx}/increaseInterestSub/update">
                    <fieldset>
                            <button type="button" data-am-loading="{spinner: 'circle-o-notch', loadingText: '加载中...', resetText: '融通宝加息减扣'}" class="am-btn am-btn-primary" value="">融通宝加息减扣</button>
                    </fieldset>
                </form>
            </div>
        </div>

        <div class="am-modal am-modal-no-btn" tabindex="-1" id="modal-ajax">
		  <div class="am-modal-dialog">
		    <div class="am-modal-hd">提 示
		      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
		    </div>
		    <div class="am-modal-bd" id="ajaxmsg">
		      
		    </div>
		  </div>
		</div>
        <script src="${ctx}/assets/js/jquery.min.js"></script>
        <script src="${ctx}/assets/js/amazeui.min.js"></script>
        <script>
        //封装ajax
        function ajax(url, param, type) {
            // 利用了jquery延迟对象回调的方式对ajax封装，使用done()，fail()，always()等方法进行链式回调操作
            // 如果需要的参数更多，比如有跨域dataType需要设置为'jsonp'等等，可以考虑参数设置为对象
            return $.ajax({
                url: url,
                data: param || {},
                type: type || 'POST',
                datatype:"json"
            });
        }
        // 链式回调

        function handleAjax(url, param, type) {
            return ajax(url, param, type).then(function(resp) {
                // 成功回调
                if (resp.result) {
                    return resp.data; // 直接返回要处理的数据，作为默认参数传入之后done()方法的回调
                } else {
                    return $.Deferred().reject(resp.msg); // 返回一个失败状态的deferred对象，把错误代码作为默认参数传入之后fail()方法的回调
                }
            }, function(err) {
                // 失败回调
                console.log(err.status); // 打印状态码
            });
        }
        </script>
        <script>
        $("form").find("button").click(function() {
        	var $btn = $(this);
        	$btn.next('p').remove();
        	$btn.button('loading');
            var form = $btn.parents("form");
            ajax(form.attr("action"), form.serialize()).done(function(resp) {
                $btn.button('reset');
                if(resp.status){
                	 $btn.parent().append('<p class="am-text-success ajax-message">'+resp.error+' <a href="/hyjf-batch-record/data/upfiles/createFile/'+resp.error.split("：")[1]+'">下载文件</a> </p>');
                }else{
                	 $btn.parent().append('<p class="am-text-danger ajax-message">'+resp.error+'</p>');
                }
            }).fail(function(err) {
                $btn.button('reset');
                $btn.parent().append('<p class="am-text-danger ajax-message">'+通信失败+'</p>');
            });
        });
        </script>
	
	</body>
	
</html>
