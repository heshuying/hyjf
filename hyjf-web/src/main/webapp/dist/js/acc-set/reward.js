var swiperList = []; //存储优惠券列表轮播
/*
 * 初始化优惠券列表轮播 
 * @param flg [string]优惠券列表 Flg
 */



$(function() {
	
	var ie = utils.isIe8();
	if (ie) {
		$("#copyBtn").zclip({
			path : webPath + '/dist/js/lib/ZeroClipboard.swf',
			copy : $('#copyTxt').text(),
			afterCopy : function() {
				utils.alert({
					id : "copyAlert",
					content : "复制成功，可以去粘贴分享了"
				});
			}
		});
		initSwiper = function(flg) {
			if (swiperList[flg] == undefined
					&& $("#coupon" + flg + " .swiper-container").find(".swiper-slide").length > 1) {
				swiperList[flg] = new Swiper("#coupon" + flg + " .swiper-container", {});
				$("#coupon" + flg).find('.swiper-prev').on('click', function(e) {
					e.preventDefault();
					swiperList[flg].swipePrev();
				})
				$("#coupon" + flg).find('.swiper-next').on('click', function(e) {
					e.preventDefault();
					swiperList[flg].swipeNext();
				})
			}
		}
	} else {
		var clipboard = new Clipboard('#copyBtn');
		clipboard.on('success', function() {
			utils.alert({
				id : "copyAlert",
				content : "复制成功，可以去粘贴分享了"
			});
		})
		initSwiper = function(flg) {
		    if (swiperList[flg] == undefined && $("#coupon" + flg + " .swiper-container").find(".swiper-slide").length > 1) {
		        swiperList[flg] = new Swiper("#coupon" + flg + " .swiper-container", {
		            prevButton: '.swiper-prev',
		            nextButton: '.swiper-next',
		        });
		    }
		}
	}
	initSwiper("Wsy"); //默认展示的列表初始化 Wsy = 未使用
	$("#shareQrBtn").mouseenter(function() {
		$("#shareQrPop").stop().fadeIn();
	}).mouseleave(function() {
		$("#shareQrPop").fadeOut();
	}).click(function() {
		utils.alert({
			id : "downloadAlert",
			content : "下载二维码成功"
		});
	})
	$(".tab-tags li").click(function(e) {
		var _self = $(this);
		var idx = _self.attr("panel");
		var panel = _self.parent(".tab-tags").next(".tab-panels");
		_self.siblings("li.active").removeClass("active");
		_self.addClass("active");
		panel.children("li.active").removeClass("active");
		panel.children("li[panel=" + idx + "]").addClass("active");

	});
	$(".reward-coupon").find(".tab-tags li").click(function() {
		var _self = $(this);
		initSwiper(_self.data("couponid"));
	});

	var codeWidth = 100;
	$(".friend-code-div").height(codeWidth);
	$("#qrcode").each(function() {
		$(this).qrcode({
			text : $("#copyTxt").text(),
			render : "canvas",
			width : codeWidth,
			height : codeWidth,
			typeNumber : -1,
			correctLevel : QRErrorCorrectLevel.H,
			background : "#ffffff",
			foreground : "#000000"
		});
	});

})

$.ajax({
	url : webPath + "/user/invite/logs.do",
	type : "POST",
	data : {
		"recordType" : "jljl"
	},
	async : true,
	success : function(data) {
		if (data.status) {
			var list = data.rewardList != null ? data.rewardList : [];
			var htmlstr = '';
			if (list.length == 0) {
				htmlstr = htmlstr + '<tr><td colspan="6"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无奖励记录...</p></div></td></tr>'
				$("#jljlTbody").html(htmlstr);
			} else {
				for (var i = 0; i < list.length; i++) {
					var obj = list[i];
					//备注
					var remarks="";
					//我的奖励
					var reward="";
					//奖励来源
					var rewardsource="";
						
					if(obj.type==0){
						reward=obj.pushMoney+"元现金";
						rewardsource="好友出借";
						remarks="出借"+obj.account+"元";
					}else if(obj.type==1){
						//优惠券信息
					}
					htmlstr += '<tr>' 
							+ '<td>' + (i+1) + '</td>'
							+ '<td>' + obj.username + '</td>'
							+ '<td><span class="highlight">'+ reward + '</span></td>'
							+ '<td>' + rewardsource + '</td>'
							+ '<td>' + obj.rewardTime+ '</td>' 
							+ '<td>' + remarks+ '</td>' 
							+ '</tr>'
				}
				$("#jljlTbody").html(htmlstr);
			}

		} else {
			utils.alert({
				id : "logsErrorAlert",
				content : data.message
			});
		}
	},
	error : function(err) {
		utils.alert({
			id : "logsErrorAlert",
			content :"接口调用失败!",
			fnconfirm:utils.refresh
		});
	}
});

$(document).ready(function() {
	/**
	 * 奖励记录
	 */
	getJljlPage();
	$(document).on("click", ".jljl", function() {
		jljlList($(this).data("page"));
	});

	/**
	 * 债权已回款
	 */
	getYqjlPage();
	$(document).on("click", ".yqjl", function() {
		yqjlList($(this).data("page"));
	});

});

/**************************奖励记录*******************************/
/**
 * 奖励记录首次加载
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function getJljlPage() {
	var orderBy = $(".ui-list-title a.selected:visible").data("val") || "";
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);

	doRequest(jljlPageSuccessCallback, jljltPageErrorCallback, webPath
			+ "/user/invite/logs.do", {
		"recordType" : "jljl",
		"paginatorPage":$("#paginatorPage").val(),
	 	"pageSize":$("#pageSize").val()
	}, true, "jljl", "jljl-pagination");
}
/**
 * 奖励记录分页按钮发起请求
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function jljlList(paginatorPage) {
	$("#paginatorPage").val(paginatorPage==undefined?1:paginatorPage);
	$("#pageSize").val(pageSize);
	$("#jljlTbody").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
	doRequest(jljlPageSuccessCallback, jljltPageErrorCallback, webPath
			+ "/user/invite/logs.do", {
		"recordType" : "jljl",
		"paginatorPage":$("#paginatorPage").val(),
	 	"pageSize":$("#pageSize").val()
	}, true, "jljl", "jljl-pagination");
}

/**
 * 获取奖励记录信息成功回调
 */
function jljlPageSuccessCallback(data) {

	if (data.status) {
		var list = data.rewardList != null ? data.rewardList : [];
		var htmlstr = '';
		if (list.length == 0) {
			htmlstr = htmlstr + '<tr><td colspan="6"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无奖励记录...</p></div></td></tr>'
			$("#jljlTbody").html(htmlstr);
			$('#jljl-pagination').hide();
		} else {
			for (var i = 0; i < list.length; i++) {
				var obj = list[i];
				var remarks="";
				var reward="";
				var rewardsource="";
					
				if(obj.type==0){
					reward=obj.pushMoney+"元现金";
					rewardsource="好友出借";
					remarks="出借"+obj.account+"元";
				}else if(obj.type==1){
					//优惠券信息
				}
				htmlstr += '<tr>' 
						+ '<td>' + (i+1) + '</td>'
						+ '<td>' + obj.username + '</td>'
						+ '<td><span class="highlight">'+ reward + '</span></td>'
						+ '<td>' + rewardsource + '</td>'
						+ '<td>' + obj.rewardTime+ '</td>' 
						+ '<td>' + remarks+ '</td>' 
						+ '</tr>'
			}
			$('#jljl-pagination').show();
			$("#jljlTbody").html(htmlstr);
		}

	} else {
		utils.alert({
			id : "jljlErrorAlert",
			content :data.message
		});
	}
}
/**
 * 获取奖励记录信息失败回调
 */
function jljltPageErrorCallback(data) {
	utils.alert({
		id : "jljlErrorAlert",
		content :"接口调用失败!",
		fnconfirm:utils.refresh
	});
}

/**************************奖励记录*******************************/

/**************************邀请记录*******************************/
/**
 * 邀请记录首次加载
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function getYqjlPage() {
	var orderBy = $(".ui-list-title a.selected:visible").data("val") || "";
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	$("#yqjlTbody").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
	doRequest(yqjlPageSuccessCallback, yqjltPageErrorCallback, webPath
			+ "/user/invite/logs.do", {
		"recordType" : "yqfs",
		"paginatorPage":$("#paginatorPage").val(),
	 	"pageSize":$("#pageSize").val()
	}, true, "yqjl", "yqjl-pagination");
}
/**
 * 邀请记录分页按钮发起请求
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function yqjlList(paginatorPage) {
	$("#paginatorPage").val(paginatorPage==undefined?1:paginatorPage);
	$("#pageSize").val(pageSize);
	$("#yqjlTbody").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
	doRequest(yqjlPageSuccessCallback, yqjltPageErrorCallback, webPath
			+ "/user/invite/logs.do", {
		"recordType" : "yqfs",
		"paginatorPage":$("#paginatorPage").val(),
	 	"pageSize":$("#pageSize").val()
	}, true, "yqjl", "yqjl-pagination");
}

/**
 * 邀请记录记录信息成功回调
 */
function yqjlPageSuccessCallback(data) {

	if (data.status) {
		var list = data.inviteList != null ? data.inviteList : [];
		var htmlstr = '';
		if (list.length == 0) {
			htmlstr = htmlstr + '<tr><td colspan="4"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无邀请记录...</p></div></td></tr>'
			$("#yqjlTbody").html(htmlstr);
			$('#yqjl-pagination').hide();
		} else {
			for (var i = 0; i < list.length; i++) {
				var obj = list[i];
				var yhzt = '已开户';
				if (obj.userStatus == '0') {
					yhzt = '未开户';
				}
				var recentWaitTime = obj.inviteTime;
				if (recentWaitTime == null || recentWaitTime == 'null') {
					recentWaitTime = "";
				}
				htmlstr += '<tr>' 
						+ '   <td>' + (i+1) + '</td>'
						+ '   <td>' + obj.username + '</td>'
						+ '   <td>' + yhzt + '</td>' 
						+ '   <td>' + recentWaitTime + ' </td>'
						+ '</tr>'
			}
			$('#yqjl-pagination').show();
			$("#yqjlTbody").html(htmlstr);
		}

	} else {
		utils.alert({
			id : "yqjlErrorAlert",
			content :data.message
		});
	}
}
/**
 * 获取邀请记录信息失败回调
 */
function yqjltPageErrorCallback(data) {
	utils.alert({
		id : "qyjlErrorAlert",
		content :"接口调用失败!",
		fnconfirm:utils.refresh
	});
}

/**************************邀请记录*******************************/

