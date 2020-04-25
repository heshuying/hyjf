$(document).ready(
	function() {
		/**
		 * 获取公司动态
		 */
		getProjectListPage();
		
		function getProjectListPage() {
			$("#paginatorPage").val(1);
			$("#pageSize").val(pageSize);
			doRequest(
					projectPageSuccessCallback,
					projectPageErrorCallback, 
					webPath+ "/bank/web/borrow/getBorrowList.do?projectType="+$("#projectType").val()+"&borrowClass="+$("#borrowClass").val(),
					$("#listForm").serialize(), true,"flipClass","new-pagination");
		}
	
		/**
		 * 获取公司动态信息成功回调
		 */
		function projectPageSuccessCallback(data) {
			var projectList = data.projectList;
			
			// 挂载数据
			var projectListStr = "";
			if(projectList.length==0){
				projectListStr =projectListStr 
				+'<div class="noitem">项目准备中，请耐心等待 ……</div>'
				$("#projectList").hide().parent().append(projectListStr);
			}else{
				for (var i = 0; i < projectList.length; i++) {
					var project =projectList[i];
					var length = project.borrowNid.length;
					projectListStr =projectListStr 
					+ '<li>'
					+ '	<div class="new-title">'
					+ '		<a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="id">'+(project.borrowType =='融通宝' ?project.borrowAssetNumber : project.borrowNid)+'</a>'
					/*+ '		<a href="'+webPath+'/project/getProjectDetail.do?borrowNid='+project.borrowNid+'" class="id">'+project.borrowNid.substring(0,length-2)+'-'+project.borrowNid.substring(length-2,length)+'</a>'*/
					/*+ '		<a href="'+webPath+'/project/getProjectDetail.do?borrowNid='+project.borrowNid+'" class="name">'+project.borrowName+'</a>'*/
					+ '		<a href="javascript:;" class="tag">'+project.borrowStyle+'</a>'
					+ '	</div>'
					+ '	<div class="new-content">'
					+ '		<div class="con1">'
					+ '  		<div class="num highlight">'+project.borrowApr+'<span>%'
					//带有“产品加息率”的标的（融通宝）需显示“产品加息率”
					+ (project.borrowExtraYield == 0 ? '</span>' : ' </span><span style="font-size:22px;">+<div class="highlight"  style="display:inline;"> '+project.borrowExtraYield+'</div>%</span>')
					+'</div>'
					+ '  		<div class="con-title">历史年回报率</div>'
					+ '		</div>'
					+ ' 	<div class="con2">'
					+ '  		<div class="num">'+project.borrowPeriod+'</div>'
					+ '  		<div class="con-title">期限</div>'
					+ ' 	</div>'
					+ '		<div class="con3">'
					+ ' 		<div class="num">'+project.borrowAccount+'</div>'
					+ ' 		<div class="con-title">金额（元）</div>'
					+ '		</div>'
					;
					if(project.borrowType=="尊享汇"&&project.status=="10"&&project.bookingBeginTime!=0&&project.bookingEndTime!=0){
						projectListStr =projectListStr 
						+ '		<div class="con4">'
						+ ' 		<div class="progress-con num">'
						+ '     		<div class="progress-all">'
						+ '           		<div class="progress-cur" data-percent="'+project.borrowAccountScaleAppoint+'"></div>'
						+ '     		</div>'
						+ '      		<div class="percent"><span>'+project.borrowAccountScaleAppoint+'</span>%</div>'
						+ '   		</div>'
						+ '   		<div class="con-title">预约进度</div>'
						+ '		</div>';
			
					}else {
						projectListStr =projectListStr 
						+ '		<div class="con4">'
						+ ' 		<div class="progress-con num">'
						+ '     		<div class="progress-all">'
						+ '           		<div class="progress-cur" data-percent="'+project.borrowSchedule+'"></div>'
						+ '     		</div>'
						+ '      		<div class="percent"><span>'+project.borrowSchedule+'</span>%</div>'
						+ '   		</div>'
						+ '   		<div class="con-title">进度</div>'
						+ '		</div>';
			
					}
					if(project.status=="10"){
						if(project.borrowType=="尊享汇"&&project.bookingStatus=="0"&&project.bookingBeginTime!=0&&project.bookingEndTime!=0){
							projectListStr = projectListStr
							+ '	<div class="con5">'
							+ '		<div class="num">'
							+ '	    	<a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="new-list-btn wait">等待预约</a>'
							+ '		</div>'
							+ '		<div class="con-title time" data-time="'+project.time+'" data-nid ="'+project.borrowNid+'"></div>'
							+ '	</div>';
						}else if(project.borrowType=="尊享汇"&&project.bookingStatus=="1"){
							projectListStr = projectListStr
							+ '	<div class="con5">'
							+ '		<div class="num">'
							+ '	    	<a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="new-list-btn avalible">我要预约</a>'
							+ '		</div>'
							+ '		<div class="con-title time" data-time="'+project.time+'" data-nid ="'+project.borrowNid+'"></div>'
							+ '	</div>';
						}else if(project.borrowType=="尊享汇"&&project.bookingStatus=="2"){
							projectListStr = projectListStr
							+ '	<div class="con5">'
							+ '		<div class="num">'
							+ '	    	<a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="new-list-btn wait">预约完成</a>'
							+ '		</div>'
							+ '		<div class="con-title time" data-time="'+project.time+'" data-nid ="'+project.borrowNid+'"></div>'
							+ '	</div>';
						}else{
							projectListStr = projectListStr
							+ '	<div class="con5">'
							+ '		<div class="num">'
							+ '	    	<a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="new-list-btn wait">等待投标</a>'
							+ '		</div>'
							+ '		<div class="con-title time" data-time="'+project.time+'" data-nid ="'+project.borrowNid+'"></div>'
							+ '	</div>';
						}
						
					}else if(project.status=="11"){
						projectListStr = projectListStr
						+ '	<div class="con5">'
						+ ' 	<div class="num">'
						+ '      	<a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="new-list-btn avalible">立即出借</a>'
						+ '   	</div>'
						+ '	</div>'
					}else if(project.status=="12"){
						projectListStr = projectListStr
						+ '<div class="con5">'
						+ '		<div class="num">'
						+ '    		<a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="new-list-btn done">复审中</a>'
						+ '		</div>'
						+ '</div>'
					}else if(project.status=="13"){
						projectListStr = projectListStr
						+ '<div class="con5">'
						+ '		<div class="num">'
						+ '    		<a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="new-list-btn done">还款中</a>'
						+ '		</div>'
						+ '</div>'
					}
					projectListStr = projectListStr
					+ ' </div>'
					+ '</li>'
				}
				$("#projectList").html(projectListStr);
				initEvent();
			}
		}
	
		/**
		 * 获取公司动态信息失败回调
		 */
		function projectPageErrorCallback(data) {
	
		}
		$(document).on("click", ".flipClass", function() {
			flip($(this).data("page"));
		});
	
		/**
		 * 分页按钮发起请求
		 * 
		 * @param successCallback
		 * @param errorCallback
		 * @param url
		 * @param paginatorPage
		 */
		function flip(paginatorPage) {
			$("#paginatorPage").val(paginatorPage);
			$("#pageSize").val(pageSize);
			doRequest(
					projectPageSuccessCallback,
					projectPageErrorCallback, 
					webPath+ "/bank/web/borrow/getBorrowList.do?projectType="+$("#projectType").val()+"&borrowClass="+$("#borrowClass").val(), 
					$("#listForm").serialize(), true,"flipClass","new-pagination");
			scrollTo();
		}
		function scrollTop(speed){
			$("html,body").animate({
	            scrollTop: 0
	        }, speed);
		}
	}
);
