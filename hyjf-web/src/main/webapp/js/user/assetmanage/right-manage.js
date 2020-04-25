      var orderData = {
				"currentHoldObligatoryRightOrderBy":{
					order:"",
					sort:""
				},
				"repayMentOrderBy":{
					order:"",
					sort:""
				},
				"currentHoldPlanOrderBy":{
					order:"",
					sort:""
				},
				"repayMentPlanOrderBy":{
					order:"",
					sort:""
				}
		}
      var myChart = echarts.init(document.getElementById('main'));
        //设置鼠标以上改变列表数据字体颜色
        myChart.on('mouseover',function(param){

            if (typeof param.dataIndex != 'undefined') {
                var id=param.dataIndex;
            }
       
            $('ul.table-main'+':eq('+id+')').find('li').css("color","#fa5e28");
        })
        
         myChart.on('mouseout',function(param){
            
            if (typeof param.dataIndex != 'undefined') {
               var id=param.dataIndex;
            }
            $('ul.table-main li').css("color","#404040");
        
        })
        
       
      $(function(){  
        option = {
            title:{
                show:true,
                text:'总计(元)',
                textStyle:{
                    color:'#404040',
                    fontSize:14,
                    fontWeight:'bolder',
                },
                x:'center',
                y:'125px',
                subtext:$("#accountAwait").val(),
                subtextStyle:{
                    color:'#fa5e28',
                    fontSize:14,
                }
            },
            series: [
                {
                    
                    type:'pie',
                    radius: ['37%', '55%'],
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    color:['#fa5e28','#f19725'],  
                    label: {
                        normal: {
                             show: false
                        }
                    },
                    data:[
							{value:$("#bankAccountAwait").val(),id:0},
							{value:$("#planAccountWait").val(),id:1},
                    ]
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
      });

        
        //设置鼠标以上改变列表数据字体颜色
        myChart.on('mouseover',function(param){

		    if (typeof param.seriesIndex != 'undefined') {
		        var id=param.data.id;
		    }
		     
		    $('ul.table-main'+':eq('+id+')').find('li').css("color","#fa5e28");
        })
         myChart.on('mouseout',function(param){
        	
		    if (typeof param.seriesIndex != 'undefined') {
		       var id=param.data.id;
		    }
		    $('ul.table-main li').css("color","#404040");
		
        })
        
        
        //tab
        $('.show1').click(function(){
            $(this).addClass("active").siblings().removeClass("active");
            $('.tab-parents').removeClass('show').addClass('hide');
            $('.tab-itempar').removeClass('hide').addClass('show');
        });
        $('.show2').click(function(){
            $(this).addClass("active").siblings().removeClass("active");
            $('.tab-parents').removeClass('hide').addClass('show');
            $('.tab-itempar').removeClass('show').addClass('hide');
            

        });
        
        function onShow(btn){
            var i = $(btn).index();
            $(btn).addClass('active-in').siblings().removeClass('active-in');
            $(btn).parent().next().children().eq(i).addClass('show').siblings().removeClass('show').addClass('hide');
            
            
        }
        //箭头
        $('.ui-list-title a').click(function(){
           if($(this).hasClass("selected")){
        	   if($(this).hasClass("selected-asc")){
                   $(this).removeClass("selected-asc");
               }else{
                   $(this).addClass("selected-asc");
               }
            }else{
                $(this).addClass("selected");
                var showedEle = $(".ui-list-title a.selected").not($(this));
                hideDetail(showedEle);
            }   

        });
        
        function hideDetail(ele) {
            ele.removeClass("selected");
        }
        var wraperMain = $('.wraper-main');
        var wraperMaincon = $('.wraper-main-con');
        function showPop(creditNid){
        	getMyCreditAssignedDetailPage(creditNid);
        }
        function closePop(){
            $('.wraper').css('display','none');
            $('.wraper-main').css('display','none');
            $('body').removeAttr('style');
        }
        function closePop2(){
            $('.wraper').css('display','none');
            wraperMaincon.css('display','none');
            $('body').removeAttr('style');
        }
        function showPop2(borrowNid,nid,typeStr){
        	
        	$.ajax({
    			url : webPath+"/user/assetmanage/getRepaymentPlan.do",
    			type : "POST",
    			data : {
    	        	"borrowNid" : borrowNid,
    				"nid" : nid,
    				"typeStr":typeStr
    	        },
    			success : function(data) {
    				var currentHoldRepayMentPlanList=data.currentHoldRepayMentPlanList;
    				var currentHoldRepayMentPlanDetails=data.currentHoldRepayMentPlanDetails;
    				$("#borrow_nid").html(currentHoldRepayMentPlanDetails.borrowNid);
    				$("#add_time").html(currentHoldRepayMentPlanDetails.addtime);
    				$("#recoverAccountYes").html(currentHoldRepayMentPlanDetails.recoverAccountYes);
    				$("#recoverCapitalWait").html(currentHoldRepayMentPlanDetails.recoverCapitalWait);
    				$("#recoverInterestWait").html(currentHoldRepayMentPlanDetails.recoverInterestWait);
    				$("#recoverAccountWait").html(currentHoldRepayMentPlanDetails.recoverAccountWait);
    				var htmlstr='';
    				if(currentHoldRepayMentPlanList.length==0){
						htmlstr = htmlstr +
						'<tr><td colspan="8"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无持有中的债权...</p></div></td></tr>'
						$("#currentHoldRepayMentPlan").html(htmlstr);
						$("#currentHoldObligatoryRight-pagination").hide();
					}else{
						for (var int = 0; int < currentHoldRepayMentPlanList.length; int++) {
							var obj=currentHoldRepayMentPlanList[int];
							htmlstr =htmlstr+
								'<tr>'+
		                            '<td class="ui-list-item pc1">'+obj.recoverPeriod+'</td>'+
		                            '<td class="ui-list-item pc2">'+obj.recoverAccountWait+'</td>'+
		                            '<td class="ui-list-item pc3">'+obj.recoverCapitalWait+'</td>'+
		                            '<td class="ui-list-item pc4">'+obj.recoverInterestWait+'</td>'+
		                            '<td class="ui-list-item pc5">'+obj.recoverTime+'</td>'+
		                            '<td class="ui-list-item pc6">'+obj.recoverAccount+'</td>'+
		                            '<td class="ui-list-item pc7 value">'+obj.recoveStatus+'</td>'+
		                        '</tr>';
						}
						$("#currentHoldObligatoryRight-pagination").show();
						$("#currentHoldRepayMentPlan").html(htmlstr);
						rerenderPopPos();
					}
    				/**<fmt:formatNumber value='${account.bankAwait+account.planAccountWait}' pattern='#,##0.' />
    				 * 
    				 */
 
    			},
    			error : function(err) {
    				utils.alert({
						id:"errorMsgDialog",
						content:"调用接口失败",
					})
    			}
    		});
        	function rerenderPopPos(){
    			$('.wraper').css('display','block');
                wraperMaincon.css('display','block');
                $('body').css('overflow','hidden');
                var height = wraperMaincon.height();
                wraperMaincon.css('margin-top','-'+(height+25)/2+'px');
    		} 
        }
		
		$(document).ready(function() {
			var currentTab = $("#currentTab").val();
			/**
			 * 债权当前持有
			 */
			getCurrentHoldObligatoryRightPage();
			$(document).on("click", ".currentHoldObligatoryRightClass", function(e) {
				$("#currentHoldObligatoryRight").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
				currentHoldObligatoryRightList($(this).data("page"));
			});
			$(document).on("click", ".currentHoldObligatoryRightOrderBy", function(e) {
				$("#currentHoldObligatoryRight").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
				var page = $("#currentHoldObligatoryRight-pagination").find("a.active").data("page");
				var ele = $(e.target).is(".icon")?$(e.target).parent():$(e.target);
				setOrder("currentHoldObligatoryRightOrderBy",ele);
				currentHoldObligatoryRightList(page);
			});
			
			
			
			
			/**
			 * 债权已回款
			 */
			$(document).on("click", ".repayMentClass", function(e) {
				$("#repayMent").html("<tr><td colspan='7'>"+utils.loadingHTML+"</td></tr>");
				repayMentList($(this).data("page"));
			});
			$(document).on("click", ".repayMentOrderBy", function(e) {
				$("#repayMent").html("<tr><td colspan='7'>"+utils.loadingHTML+"</td></tr>");
				var page = $("#repayMent-pagination").find("a.active").data("page");
				var ele = $(e.target).is(".icon")?$(e.target).parent():$(e.target);
				setOrder("repayMentOrderBy",ele);
				repayMentList(page);
			});
			
			
			/**
			 * 转让记录
			 */
			$(document).on("click", ".myCreditListClass", function() {
				$("#creditList").html("<tr><td colspan='9'>"+utils.loadingHTML+"</td></tr>");
				getMyCreditList($(this).data("page"));
				//utils.scrollTo()
			});
			if(currentTab == 'myCreditListTab'){
				 $(".myCreditListClass").click();
			}
			/**
			 * 计划当前持有
			 */
			$(document).on("click", ".currentHoldPlanClass", function(e) {
				$("#currentHoldPlan").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
				currentHoldPlanList($(this).data("page"));
				//utils.scrollTo()
			});
			$(document).on("click", ".currentHoldPlanOrderBy", function(e) {
				$("#currentHoldPlan").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
				var page = $("#currentHoldPlan-pagination").find("a.active").data("page");
				var ele = $(e.target).is(".icon")?$(e.target).parent():$(e.target);
				setOrder("currentHoldPlanOrderBy",ele);
				currentHoldPlanList(page);
				//utils.scrollTo()
			});
			
			
			/**
			 * 计划已回款
			 */
			$(document).on("click", ".repayMentPlanClass", function(e) {
				$("#repayMentPlan").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
				repayMentPlanList($(this).data("page"));
				//utils.scrollTo()
			});
			$(document).on("click", ".repayMentPlanOrderBy", function(e) {
				$("#repayMentPlan").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
				var page = $("#repayMentPlan-pagination").find("a.active").data("page");
				var ele = $(e.target).is(".icon")?$(e.target).parent():$(e.target);
				setOrder("repayMentPlanOrderBy",ele);
				repayMentPlanList(page);
				//utils.scrollTo()
			});
			
		});
		//设置排序数据
		function setOrder(tab,sortEle){
			var order = sortEle.data("val"),
				sort = sortEle.hasClass("selected-asc")?"ASC":"DESC";
			orderData[tab] = {
					order:order,
					sort:sort
			};
		}
		/**************************债权当前持有*******************************/
		/**
		 * 债权当前持有首次加载
		 * 
		 * @param successCallback
		 * @param errorCallback
		 * @param url
		 * @param paginatorPage
		 */
		function getCurrentHoldObligatoryRightPage() {
			var orderBy = $(".ui-list-title a.selected:visible").data("val") || "";
			$("#paginatorPage").val(1);
			$("#pageSize").val(pageSize);
			
			doRequest(currentHoldObligatoryRightPageSuccessCallback, 
					currentHoldObligatoryRightPageErrorCallback, 
					webPath+"/user/assetmanage/getCurrentHoldObligatoryRight.do",
					{"orderByFlag":orderBy,
				 	 "paginatorPage":$("#paginatorPage").val(),
				 	 "pageSize":$("#pageSize").val()},
					true, 
					"currentHoldObligatoryRightClass", 
					"currentHoldObligatoryRight-pagination");
		}
		/**
		 * 债权当前持有分页按钮发起请求
		 * 
		 * @param successCallback
		 * @param errorCallback
		 * @param url
		 * @param paginatorPage
		 */
		function currentHoldObligatoryRightList(paginatorPage) {
			var orderBy = orderData["currentHoldObligatoryRightOrderBy"].order,sortBy=orderData["currentHoldObligatoryRightOrderBy"].sort;
			$("#paginatorPage").val(paginatorPage==undefined?1:paginatorPage);
			$("#pageSize").val(pageSize);
			
			doRequest(currentHoldObligatoryRightPageSuccessCallback, 
					currentHoldObligatoryRightPageErrorCallback, 
					webPath+"/user/assetmanage/getCurrentHoldObligatoryRight.do", 
					{"orderByFlag":orderBy,
					 "sortBy":sortBy,
					 "paginatorPage":$("#paginatorPage").val(),
					 "pageSize":$("#pageSize").val()},
					true, 
					"currentHoldObligatoryRightClass", 
					"currentHoldObligatoryRight-pagination");
		}
		//协议loading
		function downloading(){
			$('.downloadargrement').click(function(){
				var that=this
				location.href = $(that).data('href');
				$(that).hide()
				$(that).next('.loadingargrement').show()
				setTimeout(function(){
					$(that).show()
					$(that).next('.loadingargrement').hide()
				},60000)
			})
		}
		function downloading2(){
			$('.downloadargrement2').click(function(){
				if(!$(this).hasClass('disable')){
					var that=this
					location.href = $(that).data('href');
					$(that).addClass('disable')
					$(that).next('.loadingargrement').show()
					setTimeout(function(){
						$(that).removeClass('disable')
						$(that).next('.loadingargrement').hide()
					},60000)
				}
			})
		}
		
		
		/**
		 * 获取债权当前持有信息成功回调
		 */
		function currentHoldObligatoryRightPageSuccessCallback(data) {

			if(data.status){
				$("#currentHoldObligatoryRightCount").html(data.currentHoldObligatoryRightCount);
				$("#repayMentCount").html(data.repayMentCount);
				$("#tenderCreditDetailCount").html(data.tenderCreditDetailCount);
				var list=data.currentHoldObligatoryRightList;
				var htmlstr='';
				if(list.length==0){
					htmlstr = htmlstr +
					'<tr><td colspan="8"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无持有中的债权...</p></div></td></tr>'
					$("#currentHoldObligatoryRight").html(htmlstr);
					$("#currentHoldObligatoryRight-pagination").hide();
				}else{
					for (var int = 0; int < list.length; int++) {
						var obj=list[int];
						var coupon='';
						if(obj.type==3){
							if(obj.couponType==1){
								coupon='<em class="ui-loantype green" style="border: 1px solid #5bc1a8;">体验金</em>';
							}else if(obj.couponType==2){
								coupon='<em class="ui-loantype blue">加息券</em>';
							}else if(obj.couponType==3){
								coupon='<em class="ui-loantype value" style="border: 1px solid #fa5e28;">代金券</em>';
							}
						}
						// 显示产品加息
                        if(obj.type==4){
                            coupon='<em class="ui-loantype value" style="color: #fff;background: #3476b9;">加息'+obj.borrowApr+'</em>';
						}
						var data='';
						if(obj.data=='转让中'){
							data='<div class="fn-text-overflow value">'+obj.data+'</div>';
						}else if(obj.data=='投资中'){
							data='<div class="fn-text-overflow highlight">'+obj.data+'</div>';
						}else{
							data='<div class="fn-text-overflow">'+obj.data+'</div>';
						}
						var operation='';
						if(obj.investDate!='--'){
							var objStr="'"+obj.borrowNid+"','"+obj.nid+"','"+obj.type+"'";
							operation='<a onclick="showPop2('+objStr+')" href="javascript:void(0)" class="small value">还款计划</a>';
							var fddStatus = obj.fddStatus;
							if(fddStatus==1){
								if(obj.type==0){
									/*债权协议下载*/
									operation=operation+ '<a data-href="'+webPath+'/createAgreementPDF/intermediaryAgreementPDF.do?borrowNid='+obj.borrowNid+'&nid='+obj.nid+'&projectType='+obj.projectType+'" title="下载《投资协议》" class="downloadargrement"><i class="icon iconfont icon-xiazai"></i></a><a class="loadingargrement" style="display:none;"><img style="width:12px;height:12px;margin-left:5px" src="'+webPath+'/dist/images/loading.gif"/></a>';
								}else if(obj.type==1){
									/*部分债转协议下载*/
									operation=operation+ '<a data-href="'+webPath+'/createAgreementPDF/intermediaryAgreementPDF.do?borrowNid='+obj.borrowNid+'&nid='+obj.nid+'&projectType='+obj.projectType+'" title="下载《投资协议》" class="downloadargrement"><i class="icon iconfont icon-xiazai"></i></a><a class="loadingargrement" style="display:none;"><img style="width:12px;height:12px;margin-left:5px" src="'+webPath+'/dist/images/loading.gif"/></a>';
								} else if(obj.type==2){
									/*承接债转协议下载*/
									operation=operation+ '<a data-href="'+webPath+'/createAgreementPDF/creditTransferAgreementPDF.do?bidNid='+obj.borrowNid+'&creditNid='+obj.creditNid +'&creditTenderNid='+obj.creditTenderNid +'&creditUserId='+obj.creditUserId +'&assignNid='+obj.nid+'" title="下载《投资协议》"  class="downloadargrement"><i class="icon iconfont icon-xiazai"></i></a><a class="loadingargrement" style="display:none;"><img style="width:12px;height:12px;margin-left:5px" src="'+webPath+'/dist/images/loading.gif"/></a>';
								}
							}
						}
						htmlstr =htmlstr+'<tr>'+
						
                        			'<td class="ui-list-item pl1">'+
			                            '<div class="fn-text-overflow">'+
			                                '<span class="rrd-dimgray">'+obj.borrowNid+'</span>'+coupon+
			                            '</div>'+
			                        '</td>'+
			                        '<td class="ui-list-item pl2">'+obj.borrowPeriod+'/'+obj.borrowApr+'</td>'+
			                        '<td class="ui-list-item pl3">'+obj.capital+'</td>'+
			                        '<td class="ui-list-item pl4">'+obj.totalWait+'</td>'+
			                        '<td class="ui-list-item pl5">'+obj.addtime+'</td>'+
			                        '<td class="ui-list-item pl6">'+obj.investDate+'</td>'+
			                        '<td class="ui-list-item pl7">'+data+
			                        '</td>'+
			                        '<td class="ui-list-item pl8">'+
		                            '<div class="fn-text-overflow text-right">'+
		                            	operation
		                            '</div>'+
		                            '</td>'+
			                        
			                    '</tr>'
					}
					$("#currentHoldObligatoryRight-pagination").show();
					$("#currentHoldObligatoryRight").html(htmlstr);
					downloading()
				}
				
			}else{
				utils.alert({
					id:"errorMsgDialog",
					content:data.message,
				})
			}
		}
		/**
		 * 获取债权当前持有信息失败回调
		 */
		function currentHoldObligatoryRightPageErrorCallback(data) {
			utils.alert({
				id:"errorMsgDialog",
				content:"接口调用失败！",
			})
		}
		
		/**************************债权已回款*******************************/	

		/**
		 * 债权已回款分页按钮发起请求
		 * 
		 * @param successCallback
		 * @param errorCallback
		 * @param url
		 * @param paginatorPage
		 */
		function repayMentList(paginatorPage) {
			var orderBy=orderData["repayMentOrderBy"].order,sortBy=orderData["repayMentOrderBy"].sort;
			$("#paginatorPage").val(paginatorPage==undefined?1:paginatorPage);
			$("#pageSize").val(pageSize);
			doRequest(repayMentPageSuccessCallback, 
					repayMentPageErrorCallback, 
					webPath+"/user/assetmanage/getRepayMent.do", 
					{"orderByFlag":orderBy,
				 	 "sortBy":sortBy,
					 "paginatorPage":$("#paginatorPage").val(),
					 "pageSize":$("#pageSize").val()},
					true, 
					"repayMentClass", 
					"repayMent-pagination");
		}
		
		/**
		 * 获取债权已回款信息成功回调
		 */
		function repayMentPageSuccessCallback(data) {

			if(data.status){
				$("#currentHoldObligatoryRightCount").html(data.currentHoldObligatoryRightCount);
				$("#repayMentCount").html(data.repayMentCount);
				$("#tenderCreditDetailCount").html(data.tenderCreditDetailCount);
				var list=data.repayMentList;
				var htmlstr='';
				if(list.length==0){
					htmlstr = htmlstr +
					'<tr><td colspan="8"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无已回款的债权...</p></div></td></tr>'
					$("#repayMent").html(htmlstr);
					$("#repayMent-pagination").hide();
				}else{
					
					for (var int = 0; int < list.length; int++) {
						var obj=list[int];
						var coupon='';
						if(obj.type==3){
							if(obj.couponType==1){
								coupon='<em class="ui-loantype green" style="border: 1px solid #5bc1a8;">体验金</em>';
							}else if(obj.couponType==2){
								coupon='<em class="ui-loantype blue">加息券</em>';
							}else if(obj.couponType==3){
								coupon='<em class="ui-loantype value" style="border: 1px solid #fa5e28;">代金券</em>';
							}
						}else if(obj.type==4){
                            coupon='<em class="ui-loantype value" style="color: #fff;background: #3476b9;">加息'+obj.borrowApr+'</em>';
                        }
						htmlstr =htmlstr+
								'<tr>'+
			                    '<td class="ui-list-item pl1">'+
			                    '    <div class="fn-text-overflow">'+
			                    '        <span class="rrd-dimgray">'+obj.borrowNid+'</span>'+coupon+
			                    '    </div>'+
			                    '</td>'+
			                    '<td class="ui-list-item pl2">'+obj.borrowPeriod+'/'+obj.borrowApr+'</td>'+
			                    '<td class="ui-list-item pl3">'+obj.account+'</td>'+
			                    '<td class="ui-list-item pl4">'+obj.recoverAccountAll+'</td>'+
			                    '<td class="ui-list-item pl5">'+obj.recoverAccountInterest+'</td>'+
			                    '<td class="ui-list-item pl6">'+obj.repayOrddate+'</td>'+
			                    '<td class="ui-list-item pl7">'+
			                    '    <div class="fn-text-overflow">'+obj.data+'</div>'+
			                    '</td>'+
			                	'</tr>'
					}
					$("#repayMent-pagination").show();
					$("#repayMent").html(htmlstr);
				}
				
			}else{
				utils.alert({
					id:"errorMsgDialog",
					content:data.message,
				})
			}
		}
		/**
		 * 获取债权已回款信息失败回调
		 */
		function repayMentPageErrorCallback(data) {
			utils.alert({
				id:"errorMsgDialog",
				content:"接口调用失败！",
			})
		}
		
		
		/**************************债权转让记录start********************************/
		/**
		 * 债权转让分页按钮发起请求
		 * 
		 * @param successCallback
		 * @param errorCallback
		 * @param url
		 * @param paginatorPage
		 */
		function getMyCreditList(paginatorPage) {
			$("#paginatorPage").val(paginatorPage==undefined?1:paginatorPage);
			$("#pageSize").val(pageSize);
			doRequest(myCreditListPageSuccessCallback, 
					myCreditListPageErrorCallback, 
					webPath+"/user/assetmanage/getMyCreditRecordList.do", 
					'',
					true, 
					"myCreditListClass", 
					"mycreditlist-pagination");
		}
		
		/**
		 * 获取债权已回款信息成功回调
		 */
		function myCreditListPageSuccessCallback(data) {

			if(data.status){
				$("#currentHoldObligatoryRightCount").html(data.currentHoldObligatoryRightCount);
				$("#repayMentCount").html(data.repayMentCount);
				$("#tenderCreditDetailCount").html(data.tenderCreditDetailCount);
				var list=data.creditRecordList;
				var htmlstr='';
				if(list.length==0){
					htmlstr = htmlstr +
					'<tr><td colspan="9"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无转让中的债权...</p></div></td></tr>'
					$("#creditList").html(htmlstr);
					$("#mycreditlist-pagination").hide();
				}else{
					
					for (var int = 0; int < list.length; int++) {
						var obj=list[int];
						htmlstr =htmlstr+
                            '<tr>' +
	                        '<td class="ui-list-item zr1">' +
	                            '<div class="fn-text-overflow">' +
	                                '<span class="rrd-dimgray">' + obj.bidNid + '</span>' +
	                            '</div>' +
	                        '</td>' +
	                        '<td class="ui-list-item zr2">' + obj.creditCapital + '</td>' +
	                        '<td class="ui-list-item zr3">' +
	                            obj.creditDiscount +
	                        '%</td>' +
	                        '<td class="ui-list-item zr4">' + obj.addTime + '</td>' +
	                        '<td class="ui-list-item zr5">' + obj.creditTerm + '天</td>' +
	                        '<td class="ui-list-item zr6">' + obj.creditCapitalAssigned + '</td>' +
	                        '<td class="ui-list-item zr7">' + obj.receivedAccount + '</td>' +
	                        '<td class="ui-list-item value zr8" ' + (obj.creditStatus != 0 ? 'style="color:#C0C0C0;"' : "") + '>' + obj.creditInProgress + '%</td>' +
	                        '<td class="ui-list-item zr9">' +
	                            '<a class="value" onclick="showPop(' + obj.creditNid + ')" style="text-decoration: none;" href="javascript:void(0)">查看详情<span class="iconfont icon-more"></span></a></td>' +
	                    '</tr>';
					}
					$("#mycreditlist-pagination").show();
					$("#creditList").html(htmlstr);
				}
				
			}else{
				utils.alert({
					id:"errorMsgDialog",
					content:data.message,
				})
			}
		}
		/**
		 * 获取债权已回款信息失败回调
		 */
		function myCreditListPageErrorCallback(data) {
			utils.alert({
				id:"errorMsgDialog",
				content:"接口调用失败！",
			})
		}		
		
		/**
		 * 债权转让承接明细
		 * 
		 * @param successCallback
		 * @param errorCallback
		 * @param url
		 * @param paginatorPage
		 */
		function getMyCreditAssignedDetailPage(creditNid) {
			doRequest(myCreditAssignedPageSuccessCallback, 
					myCreditAssignedPageErrorCallback, 
					webPath+"/user/assetmanage/getMyCreditAssignDetail.do?creditNid=" + creditNid,
					'',false, "", "");
		}

		function myCreditAssignedPageSuccessCallback(data){
			if(data.status){
				$("#assignCapital").text(data.assignedStatistic.assignCapital);
				$("#creditCapital").text(data.assignedStatistic.creditCapital);
				$("#assignInterestAdvance").text(data.assignedStatistic.assignInterestAdvance);
				$("#creditFee").text(data.assignedStatistic.creditFee);
				$("#moneyGet").text(data.assignedStatistic.moneyGet);
				$("#bidNid").text(data.assignedStatistic.bidNid);
				$("#creditTime").text("转让时间：" + data.assignedStatistic.addTime);
				$("#creditStatusNow").text(data.borrowCredit.creditCapital == data.borrowCredit.creditCapitalAssigned?"全部转让":"部分转让");
				
				var list=data.recordList;
				var htmlstr='';
				if(list.length==0){
					htmlstr = htmlstr +
					'<tr><td colspan="8"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无转让明细...</p></div></td></tr>'
					$("#assignedList").html(htmlstr);
				}else{
					for (var int = 0; int < list.length; int++) {
						var obj=list[int];
						htmlstr +=
	                        '<tr>' +
		                        '<td class="ui-list-item pl1 ui-name">' + obj.creditAssignUserTrueName +'</td>' +
		                        '<td class="ui-list-item pl2">' + obj.assignCapital + '</td>' +
		                        '<td class="ui-list-item pl3">' + obj.assignInterestAdvance + '</td>' +
		                        '<td class="ui-list-item pl4">' + obj.assignPay + '</td>' +
		                        '<td class="ui-list-item pl5">' + obj.creditFee + '</td>' +
		                        '<td class="ui-list-item pl6">' + (obj.assignPay - obj.creditFee).toFixed(2) + '</td>' +
		                        '<td class="ui-list-item pl7">' +
		                            '<span class="ui-day">' + obj.addTimeDay + '</span>' +
		                            '<span class="ui-time">' + obj.addTimeHour + '</span>' +
		                        '</td>' +
		                        
		                        '<td class="ui-list-item pl8 value"><a data-href="' + webPath + '/createAgreementPDF/creditTransferAgreementPDF.do?bidNid='+obj.bidNid+'&creditNid='+obj.creditNid+'&creditTenderNid='+obj.creditTenderNid+'&assignNid='+obj.assignNid+'" class="downloadargrement2">下载协议</a><a class="loadingargrement" style="display:none;"><img style="width:12px;height:12px;margin-left:5px" src="'+webPath+'/dist/images/loading.gif"/></a></td>' +
		                      
		                    '</tr>';
					}
					$("#assignedList").html(htmlstr);
					downloading2()
				}
				rerenderPopPos();
				
			}else{
				utils.alert({
					id:"errorMsgDialog",
					content:data.message,
				})
			}

			function rerenderPopPos(){
				$('.wraper').css('display','block');
	            $('.wraper-main').css('display','block');
	            $('body').css('overflow','hidden');
	            var height = wraperMain.height();
	            wraperMain.css('margin-top','-'+(height+25)/2+'px');
    		}
			
			
		}
		
		function myCreditAssignedPageErrorCallback(){
			utils.alert({
				id:"errorMsgDialog",
				content:"接口调用失败！",
			})
		}
		
		/**************************债权转让记录end********************************/
		
		/**************************计划当前持有*******************************/	
		
		/**
		 * 计划当前持有分页按钮发起请求
		 * 
		 * @param successCallback
		 * @param errorCallback
		 * @param url
		 * @param paginatorPage
		 */
		function currentHoldPlanList(paginatorPage,ele) {
			var orderBy=orderData["currentHoldPlanOrderBy"].order,sortBy=orderData["currentHoldPlanOrderBy"].sort;
			$("#paginatorPage").val(paginatorPage==undefined?1:paginatorPage);
			$("#pageSize").val(pageSize);
			doRequest(currentHoldPlanPageSuccessCallback, 
					currentHoldPlanPageErrorCallback, 
					webPath+"/user/assetmanage/getCurrentHoldPlan.do", 
					{"orderByFlag":orderBy,
				 	 "sortBy":sortBy,
					 "paginatorPage":$("#paginatorPage").val(),
					 "pageSize":$("#pageSize").val()},
					true, 
					"currentHoldPlanClass", 
					"currentHoldPlan-pagination");
		}
		
		/**
		 * 获取计划当前持有信息成功回调
		 */
		function currentHoldPlanPageSuccessCallback(data) {

			if(data.status){
				$("#currentHoldPlanCount").html(data.currentHoldPlanCount);
				$("#repayMentPlanCount").html(data.repayMentPlanCount);
				var list=data.currentHoldPlanList;
				var htmlstr='';
				if(list.length==0){
					htmlstr = htmlstr +
					'<tr><td colspan="8"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无持有中的服务...</p></div></td></tr>'
					$("#currentHoldPlan").html(htmlstr);
					$("#currentHoldPlan-pagination").hide();
				}else{
					
					for (var int = 0; int < list.length; int++) {
						var obj=list[int];
						var coupon='';
						if(obj.type==2){
							if(obj.couponType==1){
								coupon='<em class="ui-loantype green" style="border: 1px solid #5bc1a8;">体验金</em>';
							}else if(obj.couponType==2){
								coupon='<em class="ui-loantype blue">加息券</em>';
							}else if(obj.couponType==3){
								coupon='<em class="ui-loantype value" style="border: 1px solid #fa5e28;">代金券</em>';
							}else if(obj.couponType==4){
								coupon='<em class="ui-loantype value" style="color: #fff;background: #3476b9;">加息'+obj.borrowExtraYield+'%</em>';
							}
						}

						var data='';
						if(obj.data=='转让中'){
							data='<div class="fn-text-overflow value">' + obj.data + '</div>';
						}else if(obj.data=='智能投标中'){
							data='<div class="fn-text-overflow highlight">' + obj.data + '</div>';
						}else{
							data='<div class="fn-text-overflow">' + obj.data + '</div>';
						}
						
						var operation='';
						if(obj.type==0){
							if(obj.data=='智能投标中'){
								operation='<a class="value" style="text-decoration: none;" href="'+
								webPath+'/user/assetmanage/toMyPlanInfoDetailPage.do?type=0&accedeOrderId='+obj.accedeOrderId+
								'">查看详情<span class="iconfont icon-more"></span></a>';
							}else{
								operation='<a class="value" style="text-decoration: none;" href="'+
								webPath+'/user/assetmanage/toMyPlanInfoDetailPage.do?type=1&accedeOrderId='+obj.accedeOrderId+
								'">查看详情<span class="iconfont icon-more"></span></a>';	
							}
						}else if(obj.type==1){
							if(obj.data=='智能投标中'){
								operation='<a class="value" style="text-decoration: none;" href="'+
								webPath+'/user/assetmanage/toMyHjhPlanInfoDetailPage.do?type=0&accedeOrderId='+obj.accedeOrderId+
								'">查看详情<span class="iconfont icon-more"></span></a>';
							}else{
								operation='<a class="value" style="text-decoration: none;" href="'+
								webPath+'/user/assetmanage/toMyHjhPlanInfoDetailPage.do?type=1&accedeOrderId='+obj.accedeOrderId+
								'">查看详情<span class="iconfont icon-more"></span></a>';	
							}
						}
						
						htmlstr =htmlstr+
							'<tr>'+
	                        '<td class="ui-list-item pl1">'+
	                        '    <div class="fn-text-overflow">'+
	                        '        <span class="rrd-dimgray">'+obj.debtPlanNid+'</span>'+coupon+
	                        '    </div>'+
	                        '</td>'+
	                        '<td class="ui-list-item pl2">' + obj.debtLockPeriod +'/' + obj.expectApr + '</td>'+
	                        '<td class="ui-list-item pl3">' + obj.accedeAccount +'</td>' +
	                        '<td class="ui-list-item pl4">' + obj.referenceReturn +'</td>' +
	                        '<td class="ui-list-item pl5">' + obj.createTime+'</td>'+
	                        '<td class="ui-list-item pl7">' + data + '</td>' +
	                        '<td class="ui-list-item pl8">' +
	                        	operation+
	                        '</td>'+
	                    	'</tr>'
					}
					$("#currentHoldPlan-pagination").show();
					$("#currentHoldPlan").html(htmlstr);
				}
				
			}else{
				utils.alert({
					id:"errorMsgDialog",
					content:data.message,
				})
			}
		}
		/**
		 * 获取计划当前持有信息失败回调
		 */
		function currentHoldPlanPageErrorCallback(data) {
			utils.alert({
				id:"errorMsgDialog",
				content:"接口调用失败!",
			})
		}
        
		
		/**************************计划已回款*******************************/	
		/**
		 * 计划已回款分页按钮发起请求
		 * 
		 * @param successCallback
		 * @param errorCallback
		 * @param url
		 * @param paginatorPage
		 */
		function repayMentPlanList(paginatorPage,ele) {
			var orderBy=orderData["repayMentPlanOrderBy"].order,sortBy=orderData["repayMentPlanOrderBy"].sort;
			$("#paginatorPage").val(paginatorPage==undefined?1:paginatorPage);
			$("#pageSize").val(pageSize);
			doRequest(repayMentPlanPageSuccessCallback, 
					repayMentPlanPageErrorCallback, 
					webPath+"/user/assetmanage/getRepayMentPlan.do", 
					{"orderByFlag":orderBy,
				 	 "sortBy":sortBy,
					 "paginatorPage":$("#paginatorPage").val(),
					 "pageSize":$("#pageSize").val()},
					true, 
					"repayMentPlanClass", 
					"repayMentPlan-pagination");
		}
		
		/**
		 * 获取计划已回款信息成功回调
		 */
		function repayMentPlanPageSuccessCallback(data) {

			if(data.status){
				$("#currentHoldPlanCount").html(data.currentHoldPlanCount);
				$("#repayMentPlanCount").html(data.repayMentPlanCount);
				var list=data.repayMentPlanList;
				var htmlstr='';
				if(list.length==0){
					htmlstr = htmlstr +
					'<tr><td colspan="8"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无已退出的服务...</p></div></td></tr>'
					$("#repayMentPlan").html(htmlstr);
					$("#repayMentPlan-pagination").hide();
				}else{
					
					for (var int = 0; int < list.length; int++) {
						var obj=list[int];
						var coupon='';
						if(obj.type==2){
							if(obj.couponType==1){
								coupon='<em class="ui-loantype green" style="border: 1px solid #5bc1a8;">体验金</em>';
							}else if(obj.couponType==2){
								coupon='<em class="ui-loantype blue">加息券</em>';
							}else if(obj.couponType==3){
								coupon='<em class="ui-loantype value" style="border: 1px solid #fa5e28;">代金券</em>';
							}else if(obj.couponType==4){
								coupon='<em class="ui-loantype value" style="color: #fff;background: #3476b9;">加息'+obj.borrowExtraYield+'%</em>';
							}
						}

						var data='';
						if(obj.data=='转让中'){
							data='<div class="fn-text-overflow value">' + obj.data + '</div>';
						}else if(obj.data=='投资中'){
							data='<div class="fn-text-overflow blue">' + obj.data + '</div>';
						}else{
							data='<div class="fn-text-overflow">' + obj.data + '</div>';
						}
						
						var operation='';
						if(obj.type==0){
							operation='<a class="value" style="text-decoration: none;" href="'+
							webPath+'/user/assetmanage/toMyPlanInfoDetailPage.do?type=2&accedeOrderId='+obj.accedeOrderId+
							'">查看详情<span class="iconfont icon-more"></span></a>';	 
						}else if(obj.type==1){
							operation='<a class="value" style="text-decoration: none;" href="'+
							webPath+'/user/assetmanage/toMyHjhPlanInfoDetailPage.do?type=2&accedeOrderId='+obj.accedeOrderId+
							'">查看详情<span class="iconfont icon-more"></span></a>';	
						}
						htmlstr +=
							'<tr>'+
	                        '<td class="ui-list-item pl1">'+
	                        '    <div class="fn-text-overflow">'+
	                        '        <span class="rrd-dimgray">' + obj.debtPlanNid + '</span>'+coupon+
	                        '    </div>'+
	                        '</td>'+
	                        '<td class="ui-list-item pl2">' + obj.debtLockPeriod +'/' + obj.expectApr + '</td>'+
	                        '<td class="ui-list-item pl3">'+obj.accedeAccount+
	                        '</td>'+
	                        '<td class="ui-list-item pl5">' + obj.repayInterestYes + '</td>'+
	                        '<td class="ui-list-item pl6">' + obj.liquidateFactTime + '</td>'+
	                        '<td class="ui-list-item pl7">'+
	                        '    <div class="fn-text-overflow value">'+data+'</div>'+
	                        '</td>'+
	                        '<td class="ui-list-item pl8">'+operation+
	                        '</td>'+
	                    	'</tr>'
	                        
					}
					$("#repayMentPlan-pagination").show();
					$("#repayMentPlan").html(htmlstr);
				}
				
			}else{
				function currentHoldPlanPageErrorCallback(data) {
					utils.alert({
						id:"errorMsgDialog",
						content:data.message,
					})
				}
			}
		}
		/**
		 * 获取计划已回款信息失败回调
		 */
		function repayMentPlanPageErrorCallback(data) {
			function currentHoldPlanPageErrorCallback(data) {
				utils.alert({
					id:"errorMsgDialog",
					content:"接口调用失败!",
				})
			}
		}

      /**
       * 我要转让--提示

		$(".ui-button-orange").click(function () {
			if($("#toggle").val()=="1"){
				window.location.href=webPath+"/bank/user/credit/userCanCreditList.do";
			}else {
				alert($("#closeDes").val());
			}
        });*/
		