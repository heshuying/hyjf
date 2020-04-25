$(document).ready(
	function() {
		/**
		 * 获取投资列表
		 */
		var type=$("#type").val();
		if(type!=0){
			getProjectListPage();
			
			$(document).on("click", ".flipClass", function() {
				
				flip($(this).data("page"));
			});
		}
	}
);

function getProjectListPage() {
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	$("#projectList").html('<tr><td colspan="6" >'+utils.loadingHTML+'</tr>');
	doRequest(
			projectPageSuccessCallback,
			projectPageErrorCallback, 
			webPath+ "/user/assetmanage/getBorrowList.do?accedeOrderId="+$("#accedeOrderId").val(),
			$("#listForm").serialize(), true,"flipClass","new-pagination");
}

//下载事件延迟
function downloading(){
	$('.downloadargrement').click(function(){
		if(!$(this).hasClass('disable')){
			var that=this
			location.href = $(that).data('href');
			$(that).next('.loadingargrement').show()
			$(that).addClass('disable')
			setTimeout(function(){
				$(that).removeClass('disable')
				$(that).next('.loadingargrement').hide()
			},60000)
		}
	})
}

/**
 * 获取投资列表成功回调
 */
function projectPageSuccessCallback(data) {
	var projectList = data.projectList;
	// 挂载数据
	var projectListStr = "";
	if(projectList.length == 0){
		projectListStr ='<tr><td colspan="6">暂时没有数据记录</td></tr>';
		$('#new-pagination').hide();
	}else{
		var type=$("#type").val();
		/*锁定中*/
		if(type==1){
			for (var i = 0; i < projectList.length; i++) {
				var project =projectList[i];
				/*非债转的显示借款协议+投资协议(居间服务协议)*/
				if(project.type==0){
					if(project.fddStatus==1){
						projectListStr += '<tr>'+
						'<td class="ui-list-item pl1"><a href="' + webPath + '/hjhdetail/getBorrowDetail.do?borrowNid='+project.borrowNid +'" target="_blank">'+project.borrowNid+'</a></td>'+
						'<td class="ui-list-item pl2">' + project.borrowPeriod +'</td>'+
						'<td class="ui-list-item pl3">'+project.account +'</td>'+
						'<td class="ui-list-item pl4">'+project.addTime +'</td>'+
//						'<td class="ui-list-item pl5">'+project.recoverTime +'</td>'+
						'<td class="ui-list-item pl6">	'+
						/*'<a href="' + webPath + '/bank/web/user/repay/downloadIntermediaryPdf.do?borrowNid='+ project.borrowNid + '&nid='+project.nid + '&assignNid=' +project.assignOrderId + '" class="highlight">下载协议</a></span>'+*/
						'<a class="downloadargrement" data-href="' + webPath + '/createAgreementPDF/creditPaymentPlan.do?borrowNid='+project.borrowNid +'&nid='+project.nid +'" target="_blank" class="value">下载协议</a><a class="loadingargrement" style="display:none;"><img style="width:12px;height:12px;margin-left:5px" src="'+webPath+'/dist/images/loading.gif"/></a>'+
						'</td>'+
						'</tr>';
					}else{
						projectListStr += '<tr>'+
						'<td class="ui-list-item pl1"><a href="' + webPath + '/hjhdetail/getBorrowDetail.do?borrowNid='+project.borrowNid +'" target="_blank">'+project.borrowNid+'</a></td>'+
						'<td class="ui-list-item pl2">' + project.borrowPeriod +'</td>'+
						'<td class="ui-list-item pl3">'+project.account +'</td>'+
						'<td class="ui-list-item pl4">'+project.addTime +'</td>'+
//						'<td class="ui-list-item pl5">'+project.recoverTime +'</td>'+
						'<td class="ui-list-item pl6">	'+
						'协议生成中'+
						'</td>'+
						'</tr>';
					}
					
				} 
				/*债转的协议显示借款协议+投资协议(债转协议)*/
				else if (project.type==1) {
					if(project.fddStatus==1){
						projectListStr += '<tr>'+
						'<td class="ui-list-item pl1"><a href="' + webPath + '/hjhdetail/getBorrowDetail.do?borrowNid='+project.borrowNid +'" target="_blank">'+project.borrowNid+'</a></td>'+
						'<td class="ui-list-item pl2">' + project.borrowPeriod +'</td>'+
						'<td class="ui-list-item pl3">'+project.account +'</td>'+
						'<td class="ui-list-item pl4">'+project.addTime +'</td>'+
//						'<td class="ui-list-item pl5">'+project.recoverTime +'</td>'+
						'<td class="ui-list-item pl6">	'+
						'<a class="downloadargrement" data-href="' + webPath + '/bank/web/user/repay/downloadIntermediaryPdf.do?borrowNid='+ project.borrowNid + '&nid='+project.nid + '&assignNid=' +project.investOrderId + '" class="highlight">下载协议</a><a class="loadingargrement" style="display:none;"><img style="width:12px;height:12px;margin-left:5px" src="'+webPath+'/dist/images/loading.gif"/></a>'+
						 '</td>'+
						'</tr>';
					}else{
						projectListStr += '<tr>'+
						'<td class="ui-list-item pl1"><a href="' + webPath + '/hjhdetail/getBorrowDetail.do?borrowNid='+project.borrowNid +'" target="_blank">'+project.borrowNid+'</a></td>'+
						'<td class="ui-list-item pl2">' + project.borrowPeriod +'</td>'+
						'<td class="ui-list-item pl3">'+project.account +'</td>'+
						'<td class="ui-list-item pl4">'+project.addTime +'</td>'+
//						'<td class="ui-list-item pl5">'+project.recoverTime +'</td>'+
						'<td class="ui-list-item pl6">	'+
						'协议生成中</span>'+
						 '</td>'+
						'</tr>';
					}
					
				}
				else{
					projectListStr += '<tr>'+
					'<td class="ui-list-item pl1"></td>'+
					'<td class="ui-list-item pl2"></td>'+
					'<td class="ui-list-item pl3"></td>'+
					'<td class="ui-list-item pl4"></td>'+
					'<td class="ui-list-item pl5"></td>'+
					'<td class="ui-list-item pl6">'+
					'</td>'+
					'</tr>';
				}
			}
		}
		
		/*已退出*/
		if(type==2){
			for (var i = 0; i < projectList.length; i++) {
				var project =projectList[i];
				/*非债转的显示借款协议+投资协议(居间服务协议)*/
				if(project.type==0){
					if(project.fddStatus==1){
						projectListStr += '<tr>'+
						'<td class="ui-list-item pl1"><a href="' + webPath + '/hjhdetail/getBorrowDetail.do?borrowNid='+project.borrowNid +'" target="_blank">'+project.borrowNid+'</a></td>'+
						'<td class="ui-list-item pl2">' + project.borrowPeriod +'</td>'+
						'<td class="ui-list-item pl3">'+project.account +'</td>'+
						'<td class="ui-list-item pl4">'+project.addTime +'</td>'+
//						'<td class="ui-list-item pl5">'+project.recoverTime +'</td>'+
						'<td class="ui-list-item pl6">	'+
						'<a class="downloadargrement" data-href="' + webPath + '/createAgreementPDF/creditPaymentPlan.do?borrowNid='+project.borrowNid +'&nid='+project.nid +'" target="_blank" class="value">下载协议</a><a class="loadingargrement" style="display:none;"><img style="width:12px;height:12px;margin-left:5px" src="'+webPath+'/dist/images/loading.gif"/></a>'+
						'</td>'+
						'</tr>';
					}else{
						projectListStr += '<tr>'+
						'<td class="ui-list-item pl1"><a href="' + webPath + '/hjhdetail/getBorrowDetail.do?borrowNid='+project.borrowNid +'" target="_blank">'+project.borrowNid+'</a></td>'+
						'<td class="ui-list-item pl2">' + project.borrowPeriod +'</td>'+
						'<td class="ui-list-item pl3">'+project.account +'</td>'+
						'<td class="ui-list-item pl4">'+project.addTime +'</td>'+
//						'<td class="ui-list-item pl5">'+project.recoverTime +'</td>'+
						'<td class="ui-list-item pl6">	'+
						'协议生成中'+
						'</td>'+
						'</tr>';
					}
					
				} 
				/*债转的协议显示借款协议+投资协议(债转协议)*/
				else if(project.type==1){
					if(project.fddStatus==1){
						projectListStr += '<tr>'+
						'<td class="ui-list-item pl1"><a href="' + webPath + '/hjhdetail/getBorrowDetail.do?borrowNid='+project.borrowNid +'" target="_blank">'+project.borrowNid+'</a></td>'+
						'<td class="ui-list-item pl2">' + project.borrowPeriod +'</td>'+
						'<td class="ui-list-item pl3">'+project.account +'</td>'+
						'<td class="ui-list-item pl4">'+project.addTime +'</td>'+
//						'<td class="ui-list-item pl5">'+project.recoverTime +'</td>'+
						'<td class="ui-list-item pl6">	'+
						'<a class="downloadargrement" data-href="' + webPath + '/bank/web/user/repay/downloadIntermediaryPdf.do?borrowNid='+ project.borrowNid + '&nid='+project.nid + '&assignNid=' +project.investOrderId + '" class="highlight">下载协议</a><a class="loadingargrement" style="display:none;"><img style="width:12px;height:12px;margin-left:5px" src="'+webPath+'/dist/images/loading.gif"/></a>'+
						 '</td>'+
						'</tr>';
					}else{
						projectListStr += '<tr>'+
						'<td class="ui-list-item pl1"><a href="' + webPath + '/hjhdetail/getBorrowDetail.do?borrowNid='+project.borrowNid +'" target="_blank">'+project.borrowNid+'</a></td>'+
						'<td class="ui-list-item pl2">' + project.borrowPeriod +'</td>'+
						'<td class="ui-list-item pl3">'+project.account +'</td>'+
						'<td class="ui-list-item pl4">'+project.addTime +'</td>'+
//						'<td class="ui-list-item pl5">'+project.recoverTime +'</td>'+
						'<td class="ui-list-item pl6">	'+
						'协议生成中</span>'+
						 '</td>'+
						'</tr>';
					}
					
				}
				else{
					projectListStr += '<tr>'+
					'<td class="ui-list-item pl1"></td>'+
					'<td class="ui-list-item pl2"></td>'+
					'<td class="ui-list-item pl3"></td>'+
					'<td class="ui-list-item pl4"></td>'+
					'<td class="ui-list-item pl5"></td>'+
					'<td class="ui-list-item pl6">'+
					'</td>'+
					'</tr>';
				}
			}
		}
		$('#new-pagination').show();
	}
	$("#projectList").html(projectListStr);
	downloading()
}

/**
 * 获取投资列表失败回调
 */
function projectPageErrorCallback(data) {

}

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
	$("#projectList").html('<tr><td colspan="6" >'+utils.loadingHTML+'</tr>');
	doRequest(
			projectPageSuccessCallback,
			projectPageErrorCallback, 
			webPath+ "/user/assetmanage/getBorrowList.do?accedeOrderId="+$("#accedeOrderId").val(),
			$("#listForm").serialize(), true,"flipClass","new-pagination");
}

