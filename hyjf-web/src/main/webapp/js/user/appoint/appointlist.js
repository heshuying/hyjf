subMenu("userAppoint");
$(document).ready(function() {
	//预约查询状态
	var appointStatus = $("#appointStatus").val("1");
	setActById('userAppoint');
	$(".project-tab").click(function(e) {
	    var _self = $(e.target);
	    if (_self.is("li")) {
	        var idx = _self.attr("panel");
	        var panel = _self.parent().siblings(".project-tab-panel");
	        _self.siblings("li.active").removeClass("active");
	        _self.addClass("active");
	        panel.children("li.active").removeClass("active");
	        panel.children("li[panel="+idx+"]").addClass("active");
	        $("#appointStatus").val(_self.data("status"));
	        getAppointListPage();
	    }
	})
	$("#appointing").delegate(".cancel-btn","click",function(){
	    showTip('', '您确认要取消预约吗？<br />取消预约会给你来带相应的惩罚<br /><a href="'+webPath+'/user/appoint/appointPunish.do'+'" class="highlight"><查看处罚规则></a>', 'confirm', $(this).data("orderid"));
	});
	/**获取预约列表*/
	getAppointListPage();
	/**
	 * 为分页按钮绑定事件
	 */
	$(document).on("click", ".appointing-pagination", function() {
		flip($(this).data("page"));
	});
	/**
	 * 为分页按钮绑定事件
	 */
	$(document).on("click", ".appointed-pagination", function() {
		flip($(this).data("page"));
	});
});
//重定义确认事件 执行取消预约
function dealAction(id){
	if(id == ""){
		return false;
	}
	$.ajax({
		type : "POST",
		async:false,
		url : webPath+'/user/appoint/cancelAppoint.do',
		dataType : 'json',
		data : {
			"orderId" : id,
		},
		success : function(data) {
			if (data.status == true) {
				 showTip('', '预约取消成功！', 'tip', "");
				 getAppointListPage();
			} else {
				 showTip('', data.message, 'tip', "");
			}
		},
		error : function() {
			showTip('', '网络异常，请检查您的网络', 'tip', "");
		}
	});
}
/**
 * 
 * @param msg
 */
function popupWin(msg) {
	$('.settlement_mask').fadeIn();
	$('.js_zr').fadeIn();
	$("#popmsg").text(msg);
}
/**
 * 关闭弹窗
 */
function popoutWin() {
	$('.settlement_mask').fadeOut();
	$('.settlement').fadeOut();
}
/*
 *  @func 打开弹窗
 *  @param ele string 选择器名
 *  @param msg string 填充文字信息或者dom元素
 */
 function assignPopupWin(ele,msg){
     if(!ele){
         ele = $(".settlement");
     }else{
         ele = $(ele);
     }
     $('.settlement_mask').fadeIn();
     ele.fadeIn();
     if(msg){
         ele.find(".poptxt").html(msg);
     }
 }
 /**
  * 
  */
 function assignPopoutWin(){
     $('.settlement_mask').fadeOut();
     $('.settlement').fadeOut();
 }
/**
 * 获取我的投资信息
 */
function getAppointListPage() {
	appointStatus=$("#appointStatus").val();
	var data="paginatorPage="+$("#paginatorPage").val()+
		"&pageSize=8"+"&appointStatus="+appointStatus;
	//预约中
	var pannelId="appointing-pagination";
	//已预约
	if(appointStatus=='0'){
		pannelId="appointed-pagination";
	}
	doRequest(
			getAppointListPageSuccessCallback,
			getAppointListPageErrorCallback, 
			webPath+ "/user/appoint/appointList.do",
			data, true,pannelId,pannelId);
}
/**
 * 获取我的投资信息成功回调
 */
function getAppointListPageSuccessCallback(data) {
	var appointListStr ="";
	// 挂载数据
	if(data.appointStatus!=null){
		var appointList = data.appointlist;
		var recordTotal = data.recordTotal;
		$("#recordTotal").html(recordTotal);
		if(data.appointStatus=="1"){
			appointListStr = appointListStr
			+'<tr>' 
			+'	<th>项目编号</th>' 
			+'	<th>历史年回报率</th>'
			+'	<th>项目期限</th>'
			+'	<th>预约金额（元）</th>'
			+'	<th>预约时间</th>'
			+'	<th width="120">操作</th>'
			+'</tr>';
			if(appointList.length==0){
				appointListStr = appointListStr 
				+'<tr> <td colspan="6"><div>未查询到预约中数据</div></td></tr>';
			}else{
				for (var i = 0; i < appointList.length; i++) {
					var appoint = appointList[i];
					appointListStr = appointListStr 
					+ '	<tr>'
					+ '		<td>'
					+ '			<a href="'+webPath+'/project/getProjectDetail.do?borrowNid='+appoint.borrowNid+'" class="title">'
					+ '				<span class="id">'+appoint.borrowNid+'</span> <br>'
					+ '				<span class="date">发标时间 :'+appoint.verifyTime+'</span>'
					+ '			</a>'
					+ '		</td>'
					+ '		<td><span class="highlight">'+appoint.borrowApr+'<em> %</em></span></td>'
					+ '		<td>'+appoint.borrowPeriod+'</td>'
					+ '		<td class="newFormatNum">'+appoint.account+'</td>'
					+ '		<td>'+appoint.appointTime+'</td>'
					+ '		<td><div class="btn cancel-btn" data-orderid="'+appoint.orderId+'">取消预约</div></td>'
					+ '</tr>';
				}
			}
			$("#appointing").html(appointListStr);
		}else if(data.appointStatus=="0"){
			appointListStr = appointListStr
			+'<tr>'
			+'	<th>项目编号</th>'
			+'	<th>历史年回报率</th>'
			+'	<th>项目期限</th>'
			+'	<th>预约金额（元）</th>'
			+'	<th>预约时间</th>'
			+'	<th>状态</th>'
			+'	<th width="220">备注</th>'
			+'</tr>';
			if(appointList.length==0){
				appointListStr = appointListStr 
				+'<tr><td colspan="7"><div>未查询到已预约的数据 </div></td></tr>';
			}else{
				for (var i = 0; i < appointList.length; i++) {
					var appoint =appointList[i];
					appointListStr = appointListStr 
					+ '	<tr>'
					+ '		<td>'
					+ '     	<a href="'+webPath+'/project/getProjectDetail.do?borrowNid='+appoint.borrowNid+'" class="title">'
					+ '         	<span class="id">'+appoint.borrowNid+'</span><br>'
					+ '         	<span class="date">发标时间 :'+appoint.verifyTime+'</span>'
					+ '     	</a>'
					+ ' 	</td>'
					+ ' 	<td><span class="highlight">'+appoint.borrowApr+'<em> %</em></span></td>'
					+ ' 	<td>'+appoint.borrowPeriod+'</td>'
					+ ' 	<td class="newFormatNum">'+appoint.account+'</td>'
					+ ' 	<td>'+appoint.appointTime+'</td>';
					if(appoint.appointStatus != 1){
						appointListStr = appointListStr 
						+ ' <td>'+appoint.appointStatusInfo+'</td>';
					}else{
						appointListStr = appointListStr
						+ ' <td>'+appoint.tenderStatusInfo+'</td>';
					}
					appointListStr = appointListStr
					+ ' 	<td>';
					if(appoint.appointStatus != 1){
						if(appoint.appointStatus==0){//预约失败
							appointListStr = appointListStr
							+ '		<span>'+appoint.appointRemark+'</span><br/>'
							+ ' 	<span style="font-size:14px;">预约时间 :'+appoint.createTime+'</span>';
						}else if(appoint.appointStatus==2){//预约取消
							appointListStr = appointListStr
							+ '		<span>'+appoint.appointRemark+'</span><br/>'
							+ ' 	<span style="font-size:14px;">取消预约时间 :'+appoint.cancelTime+'</span>';
						}
					}else{
						if(appoint.tenderStatus==0){
							appointListStr = appointListStr
							+ '		<span>'+appoint.tenderRemark+'</span><br/>'
							+ ' 	<span style="font-size:14px;">投标时间 :'+appoint.tenderTime+'</span>';
						}else if(appoint.tenderStatus==2){
							appointListStr = appointListStr
							+ '		<span>'+appoint.tenderRemark+'</span><br/>'
							+ ' 	<span style="font-size:14px;">投标时间 :'+appoint.tenderTime+'</span>';
						}
					}
					appointListStr = appointListStr
					+ ' 	</td>'
					+ ' </tr>';
				}
			}
			$("#appointed").html(appointListStr);
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
 * 获取我的投资信息失败回调
 */
function getAppointListPageErrorCallback(data) {

}

/**
 * 分页按钮发起请求
 */
function flip(paginatorPage) {
	//预约状态
	appointStatus=$("#appointStatus").val();
	var data="paginatorPage="+paginatorPage+
	"&pageSize=8"+"&appointStatus="+appointStatus;
	//预约中
	var pannelId="appointing-pagination";
	//已预约
	if(appointStatus=='0'){
		pannelId="appointed-pagination";
	}
	doRequest(
			getAppointListPageSuccessCallback,
			getAppointListPageErrorCallback, 
			webPath+ "/user/appoint/appointList.do",
			data, true,pannelId,pannelId);
}

		
		