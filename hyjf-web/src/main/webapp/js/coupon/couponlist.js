	$(document).ready(
			function() {
				getMyCouponListPage();

				/**
				 * 为分页按钮绑定事件
				 */
				$(document).on("click", ".flip", function() {
					flip($(this).data("page"));
				});

				/**
				 * 分页按钮发起请求
				 */
				function flip(paginatorPage) {
					$("#paginatorPage").val(paginatorPage);
					$("#pageSize").val(pageSize);
					var pannelid="fenye1";//已使用
					if(usedFlag==0){
						pannelid="fenye0";//未使用
					}else if(usedFlag==4){
						pannelid="fenye2";//已失效
					} 
					doRequest(
							getMyCouponListPageSuccessCallback,
							getMyCouponListPageErrorCallback, 
							webPath+ "/coupon/getUserCouponList.do?usedFlag="+usedFlag,
							$("#listForm").serialize(), true,null,pannelid);
				}
	});
		/**
		 * 获取我的优惠券信息
		 */
		function getMyCouponListPage() {
			$("#paginatorPage").val(1);
			$("#pageSize").val(10); //$("#pageSize").val(pageSize);
			var pannelid="fenye1";//已使用
			if(usedFlag==0){
				pannelid="fenye0";//未使用
			}else if(usedFlag==4){
				pannelid="fenye2";//已失效
			} 
			doRequest(
					getMyCouponListPageSuccessCallback,
					getMyCouponListPageErrorCallback, 
					webPath+ "/coupon/getUserCouponList.do?usedFlag="+usedFlag,
					$("#listForm").serialize(), true,null,pannelid);
		}
		
		
		function changetype(type){
			usedFlag=type;
			getMyCouponListPage();
		}
		
		
		/**
		 * 获取我的优惠券信息成功回调
		 */
		function getMyCouponListPageSuccessCallback(data) {
			var couponListStr ="";
			// 挂载数据
			if(data.usedFlag!=null){
				var couponList = data.couponUserList;
				
				//未使用
				if(data.usedFlag==0){
					couponListStr = "<tbody><tr>" +
				    '<th width="352">面值</th>' +
					"<th>使用规则</th>" +
					"<th>来源与发放时间</th>" +
					" </tr>";
					if(couponList.length == 0){
						couponListStr+='<tr><td colspan="3" class="nocoupon" align="center">暂无可用优惠券</td></tr>';
					}else{
						if($("#btnGoProject").length==0){
							$(".project-tab-panel").after('<div style="text-align:center;" id="btnGoProject"><a href="'+webPath+'/project/initProjectList.do?projectType=HZT" class="btn btn-mid btn-default">立即投资</a></div>');
						}
						for (var i = 0; i < couponList.length; i++) {
							var coupon =couponList[i];
							var couponQuota='';
							if(coupon.couponType=='代金券' || coupon.couponType=='体验金'){
								couponQuota='<span class="unit">¥</span>' + coupon.couponQuota;
							}else{
								couponQuota=coupon.couponQuota + '<span class="unit">%</span>';
							}
							
							var color="";
			            	if(coupon.couponType == "加息券"){
			            		color = "yellow";
			            	}else if(coupon.couponType == "体验金"){
			            		color = "gold";
			            	}else if(coupon.couponType == "代金券"){
			            		color = "gold";
			            	}
							couponListStr =couponListStr  + '<tr>';
							couponListStr =couponListStr 
							+   '<td>'
							
							
							/*+   '<div class="coupon '+color+'">'
							+ 	'<div class="left-side">' 
							+ 	'<div class="number">编号：' + coupon.couponUserCode + '</div>'
							+ 	'<div class="left-top">'
							+ 	'<span class="value">' + couponQuota
							+ 	'<span class="txt">' + coupon.couponType + '</span>'
							+ 	'</div>'
							+ 	'<div class="number">金额范围：' + coupon.tenderQuota + '</div>'
							+ 	'</div>'
							+   '<div class="right-side">'
							+   '<div class="title">有效期</div> <br/>' + coupon.addTime + '<br/> - <br/>' + coupon.endTime 
							+   '</div>'
							+   '</div>'*/
							
							+   '<div class="coupon '+color+'">'
							+   '<div class="left-side">'
							+   '<div class="value">' + couponQuota+'</div>'
							+   '<div class="txt">' + coupon.couponType + '</div>'
							+   '</div>'
							+   '<div class="right-side">'
							+   '<div class="number"><span>编号：</span>' + coupon.couponUserCode + '</div>'
							+   '<div class="number"><span>金额范围：</span></div>'
							+   '<div class="area">' + coupon.tenderQuota + '</div>'
							+   '<div class="title">' + coupon.addTime + ' - ' + coupon.endTime +' </div>'
							+   '</div>'
							+   '</div>'
							+   '</td>';
							
							couponListStr =couponListStr 
							+   '<td>'
							+   '<div class="coupon-rules">'
							+   '操作平台：' + coupon.couponSystem + ' <br/>'
							+   '项目类型：' + coupon.projectType + '<br/>'
							+   '项目期限：' + coupon.projectExpirationType + '<br/>'
							+   '投资金额：' + coupon.tenderQuota + '<br/>';
							if(coupon.couponType == "体验金"){
								couponListStr = couponListStr + '收益期限：' + coupon.couponProfitTime + '天<br/>';
							}
							if(coupon.content){
								couponListStr = couponListStr +   '备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：' + coupon.content 
								
							}
							couponListStr =couponListStr 
							+   ' </div>'
							+   '</td>';
							
							couponListStr =couponListStr 
							+   '<td>'
							+   '<div class="coupon-from">' + coupon.couponFrom + '</div>'
							+   '<div class="coupon-date">'
							+   coupon.addTime
							+   '</div>'
							+   '</td>';
							couponListStr =couponListStr + '</tr>';
							
						}
					}
					
					couponListStr =couponListStr + '</tbody>';
					$("#pant0").html(couponListStr);
					$(".progress-cur").each(function() {
				        var perc = $(this).data("percent");
				        if (perc) {
				            $(this).animate({ "width": perc });
				        }
				    });
				//已使用
				}else if(data.usedFlag==1){
					couponListStr = "<tbody><tr>" +
				    '<th width="352">面值</th>' +
					"<th>项目信息</th>" +
					"<th>投资时间</th>" +
					"<th>待收收益</th>" +
					"<th>还款状态</th>" +
					" </tr>";
					if(couponList.length == 0){
						couponListStr+='<tr><td colspan="5" class="nocoupon" align="center">暂无已使用优惠券</td></tr>';
					}else{
						for (var i = 0; i < couponList.length; i++) {
							var coupon =couponList[i];
							var couponQuota='';
							if(coupon.couponType=='代金券' || coupon.couponType=='体验金'){
								couponQuota='<span class="unit">¥</span>' +coupon.couponQuota;
							}else{
								couponQuota=coupon.couponQuota + '<span class="unit">%</span>';
							}
							
							var color="";
			            	if(coupon.couponType == "加息券"){
			            		color = "yellow";
			            	}else if(coupon.couponType == "体验金"){
			            		color = "gold";
			            	}else if(coupon.couponType == "代金券"){
			            		color = "gold";
			            	}
							couponListStr =couponListStr  + '<tr>';
							couponListStr =couponListStr 
							+   '<td>'
							
							
							/*+   '<div class="coupon '+color+'">'
							+ 	'<div class="left-side">' 
							+ 	'<div class="number">编号：' + coupon.couponUserCode + '</div>'
							+ 	'<div class="left-top">'
							+ 	'<span class="value">' + couponQuota
							+ 	'<span class="txt">' + coupon.couponType + '</span>'
							+ 	'</div>'
							+ 	'<div class="number">金额范围：' + coupon.tenderQuota + '</div>'
							+ 	'</div>'
							+   '<div class="right-side">'
							+   '<div class="title">有效期</div> <br/>' + coupon.addTime + '<br/> - <br/>' + coupon.endTime 
							+   '</div>'
							+   '</div>'*/
							
							
							+   '<div class="coupon '+color+'">'
							+   '<div class="left-side">'
							+   '<div class="value">' + couponQuota+'</div>'
							+   '<div class="txt">' + coupon.couponType + '</div>'
							+   '</div>'
							+   '<div class="right-side">'
							+   '<div class="number"><span>编号：</span>' + coupon.couponUserCode + '</div>'
							+   '<div class="number"><span>金额范围：</span></div>'
							+   '<div class="area">' + coupon.tenderQuota + '</div>'
							+   '<div class="title">' + coupon.addTime + ' - ' + coupon.endTime +' </div>'
							+   '</div>'
							+   '</div>'
							+   '</td>';
							
							var recoverTime='';
							var borrowPeriod = '';
							var borrowApr = '';
							if(coupon.tenderType == '1'){
								borrowPeriod = coupon.borrowPeriod;
								borrowApr = coupon.borrowApr;
								if(coupon.recoverYesTime){
									recoverTime= coupon.recoverYesTime;
								}else{
									recoverTime= coupon.recoverTime;
								}
							}else if(coupon.tenderType == '2'){
								borrowPeriod = coupon.debtLockPeriod + '个月';
								borrowApr = coupon.expectApr;
								if(coupon.debtPlanStatus == '4' || coupon.debtPlanStatus == '12'){
									recoverTime = '待确认';
								}else{
									if(coupon.recoverYesTime){
										recoverTime= coupon.recoverYesTime;
									}else{
										recoverTime= coupon.recoverTime;
									}
								}
								
							}
							
							couponListStr =couponListStr 
							+   '<td>'
							+   '<div class="coupon-rules">'
							+   '投资项目：' + coupon.projectNid + ' <br/>'
							+   '项目期限：' + borrowPeriod + '<br/>'
							+   '投资金额：' + coupon.account + '<br/>'
							+   '历史年回报率：' + borrowApr + '<br/>';
							if(recoverTime!=null&&recoverTime!=''){
								couponListStr = couponListStr +   '还款时间：' + recoverTime + '<br/>';
							}
							
							if(coupon.couponType == "体验金"){
								couponListStr = couponListStr + '收益期限：' + coupon.couponProfitTime + '天<br/>';
							}
							couponListStr =couponListStr 
							+   ' </div>'
							+   '</td>';
							
							couponListStr =couponListStr 
							+   '<td>'
							+   '<div class="coupon-date">'
							+   coupon.tenderTimeDay
							+   '</div>'
							+   '<div class="coupon-date">'
							+   coupon.tenderTimeHour
							+   '</div>'
							+   '</td>';
							
							couponListStr +='<td>' + coupon.recoverAccount + '</td>';
							
							var recoverStatus='';
							if(coupon.recoverStatus==null||coupon.recoverStatus==''){
								if(coupon.tenderType == '1'){
									recoverStatus='<td>待放款</td>'
								}else if(coupon.tenderType == '2'){
									recoverStatus='<td>待还款</td>'
								}
							}else {
								recoverStatus='<td>'+coupon.recoverStatus+'</td>'
							}
							couponListStr +=recoverStatus;
							couponListStr =couponListStr + '</tr>';
							
						}
					}
					couponListStr =couponListStr + '</tbody>';
					$("#pant1").html(couponListStr);
					
				//已失效	
				}else if(data.usedFlag==4){
					couponListStr = "<tbody><tr>" +
				    '<th width="352">面值</th>' +
					"<th>使用规则</th>" +
					"<th>来源与发放时间</th>" +
					"<th>过期时间</th>" +
					" </tr>";
					if(couponList.length == 0){
						couponListStr+='<tr><td colspan="4" class="nocoupon" align="center">暂无失效优惠券</td></tr>';
					}else{
						for (var i = 0; i < couponList.length; i++) {
							var coupon =couponList[i];
							var couponQuota='';
							if(coupon.couponType=='代金券' || coupon.couponType=='体验金'){
								couponQuota='<span class="unit">¥</span>' +coupon.couponQuota;
							}else{
								couponQuota=coupon.couponQuota + '<span class="unit">%</span>';
							}
							
							var color="";
			            	if(coupon.couponType == "加息券"){
			            		color = "yellow";
			            	}else if(coupon.couponType == "体验金"){
			            		color = "gold";
			            	}else if(coupon.couponType == "代金券"){
			            		color = "gold";
			            	}
							couponListStr =couponListStr  + '<tr>';
							couponListStr =couponListStr 
							+   '<td>'
							
							/*+   '<div class="coupon '+color+'">'
							+ 	'<div class="left-side">' 
							+ 	'<div class="number">编号：' + coupon.couponUserCode + '</div>'
							+ 	'<div class="left-top">'
							+ 	'<span class="value">' + couponQuota
							+ 	'<span class="txt">' + coupon.couponType + '</span>'
							+ 	'</div>'
							+ 	'<div class="number">金额范围：' + coupon.tenderQuota + '</div>'
							+ 	'</div>'
							+   '<div class="right-side">'
							+   '<div class="title">有效期</div> <br/>' + coupon.addTime + '<br/> - <br/>' + coupon.endTime 
							+   '</div>'
							+   '</div>'*/
							
							+   '<div class="coupon '+color+'">'
							+   '<div class="left-side">'
							+   '<div class="value">' + couponQuota+'</div>'
							+   '<div class="txt">' + coupon.couponType + '</div>'
							+   '</div>'
							+   '<div class="right-side">'
							+   '<div class="number"><span>编号：</span>' + coupon.couponUserCode + '</div>'
							+   '<div class="number"><span>金额范围：</span></div>'
							+   '<div class="area">' + coupon.tenderQuota + '</div>'
							+   '<div class="title">' + coupon.addTime + ' - ' + coupon.endTime +' </div>'
							+   '</div>'
							+   '</div>'
							
							+   '</td>';
							
							couponListStr =couponListStr 
							+   '<td>'
							+   '<div class="coupon-rules">'
							+   '操作平台：' + coupon.couponSystem + ' <br/>'
							+   '项目类型：' + coupon.projectType + '<br/>'
							+   '项目期限：' + coupon.projectExpirationType + '<br/>'
							+   '投资金额：' + coupon.tenderQuota + '<br/>';
							if(coupon.couponType == "体验金"){
								couponListStr = couponListStr + '收益期限：' + coupon.couponProfitTime + '天<br/>';
							}
							if(coupon.content){
								couponListStr = couponListStr +   '备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：' + coupon.content 
								
							}
							couponListStr =couponListStr 
							+   ' </div>'
							+   '</td>';
							
							couponListStr =couponListStr 
							+   '<td>'
							+   '<div class="coupon-from">' + coupon.couponFrom + '</div>'
							+   '<div class="coupon-date">'
							+   coupon.addTime
							+   '</div>'
							+   '</td>';
							
							couponListStr =couponListStr 
							+   '<td>'
							+   '<div class="coupon-date">'
							+   coupon.endTime
							+   '</div>'
							+   '</td>';
							
							couponListStr =couponListStr + '</tr>';
							
						}
					}
					
					
					couponListStr =couponListStr + '</tbody>';
					$("#pant2").html(couponListStr);
				}
			}
			$(".newFormatNum").html($.fmtThousand($(this).html()))
			//格式化数字
			var num = $(".newFormatNum");
	        for(var i=0;i<num.length;i++){
	        	var format = num.eq(i).html();
	        	num.eq(i).html($.fmtThousand(format));
	        }
		}

		/**
		 * 获取我的优惠券信息失败回调
		 */
		function getMyCouponListPageErrorCallback(data) {

		}

		