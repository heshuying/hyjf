$(document).ready(
	function() {
		/**
		 * 获取债权转让列表
		 */
		getProjectListPage();
		
		$(document).on("click", ".flipClass", function() {
			flip($(this).data("page"));
		});
	
	}
);

function getProjectListPage() {
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	$("#projectList").html(utils.loadingHTML);
	doRequest(
			projectPageSuccessCallback,
			projectPageErrorCallback, 
			webPath+ "/bank/user/credit/webcreditlist.do",
			$("#listForm").serialize(), true,"flipClass","new-pagination");
}

/**
 * 获取债转列表成功回调
 */
function projectPageSuccessCallback(data) {
	var projectList = data.projectList;
	
	// 挂载数据
	var projectListStr = "";
	if(projectList.length==0){
		projectListStr =projectListStr 
		+ '<div class="data-empty">'
		+	'<div class="empty-icon"></div>'
		+	'<p class="align-center">暂无转让中项目...</p>'
		+ '</div>';
		$('#new-pagination').hide();
	}else{
		for (var i = 0; i < projectList.length; i++) {
			var project =projectList[i];
			//项目名称
			var nameStr = project.projectName;
			if(!nameStr){
				nameStr = project.bidName == null || project.bidName == '' ? "" : project.bidName;
			}
			
			if(nameStr.length > 8){
				nameStr = nameStr.substring(0,8) + '...';
			}
			projectListStr =projectListStr 
			+'<ul>'
			+ '<a  href="' + webPath + '/bank/user/credit/webcredittender.do?creditNid=' + project.creditNid + '">'
			+     '<li class="trf1"><div> HZR'+ project.creditNid + '</div></li>'
			+     '<li class="trf2"><div class="yield">' + project.bidApr + '%</div></li>'
			+     '<li class="trf3"><div>'+ project.creditTerm+'天</div></li>'
			+     '<li class="trf4"><div class="yield">'+ project.creditDiscount+'%</div></li>'
			+     '<li class="trf5"><div>'+ project.creditCapital +'元</div></li>'
            +     '<li class="trf6">'
            +        '<div class="bond-num">'
            +          '<div class="progress-all">'
            +            '<div class="progress-cur" style="width:'+project.creditInProgress+'%"></div>'
            +          '</div>'
            +          '<div class="percent">'
            +            '<span>'+project.creditInProgress+'%</span>'
            +          '</div>'
            +         '</div>'
            +     '</li>'
            +     '<li class="trf7"> '                       
            +        '<div class="btn sm">承接</div>'
            +     '</li>'
            + '</a>'
            +'</ul>';
		}
		$('#new-pagination').show();
	}
	$("#projectList").html(projectListStr);
}

/**
 * 获取债转列表失败回调
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
	$("#projectList").html(utils.loadingHTML);
	doRequest(
			projectPageSuccessCallback,
			projectPageErrorCallback, 
			webPath+ "/bank/user/credit/webcreditlist.do", 
			$("#listForm").serialize(), true,"flipClass","new-pagination");
	scrollTo();
}
