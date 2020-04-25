$(document).ready(
		function() {
			// 列表切换绑定事件
			$(".project-tab").children("li").click(function() {
				if (!$(this).hasClass("active")) {
					$("#status").val($(this).data("status"));
					getRepayList();
				}
			});
			getAllRepayList();
			$(document).on("click", ".flip", function() {
				flip($(this).data("page"));
			});
			$(document).on( "click", "a.title", function() {
				$("#repayForm").attr( "action", webPath + $(this).data("detail")+ ".do");
				$("#borrowNid").val($(this).data("borrownid"));
				$("#repayForm").submit();
			});
			function flip(paginatorPage) {
				$("#paginatorPage").val(paginatorPage);
				$("#pageSize").val(pageSize);
				doRequest(getRepayListSuccessCallback, getRepayListErrorCallback, webPath + "/bank/web/user/repay/repaylist.do", $("#repayForm").serialize(), true, null, "new-pagination");
			}
			$(".project-tab").click(function(e) {
				var _self = $(e.target);
				if (_self.is("li")) {
					var idx = _self.attr("panel");
					var panel = _self.parent().siblings(".project-tab-panel");
					_self.siblings("li.active").removeClass("active");
					_self.addClass("active");
				}
			})
		});

// 获取所有借款列表
function getAllRepayList() {
	$("#status").val('');
	$("#borrowNid").val('');
	getRepayList();
}

// 根据标号获取列表
function getRepayListById() {
	$("#borrowNid").val($("#qNid").val());
	getRepayList();
}

// 获取借款列表
function getRepayList() {
	doRequest(getRepayListSuccessCallback, getRepayListErrorCallback, webPath+ "/bank/web/user/repay/repaylist.do", $("#repayForm").serialize(), true,null, "new-pagination");
}

// 获取借款列表成功
function getRepayListSuccessCallback(data) {
	var repayList = data.repayList;
	var roleId = data.roleId;
	var strRepayList = "";
	if (repayList != null) {
		for (var i = 0; i < repayList.length; i++) {
			var length = repayList[i].borrowNid.length;
			if (repayList[i].status == "0"){//待还款
				if (repayList[i].projectType == 'RTB') {
					// 融通宝不展示协议
					strRepayList +=
						'<tr>'
						+'	<td>'
						+'		<a href="javascript:void(0)" class="title" data-borrowNid="'+ repayList[i].borrowNid+ '" data-detail="/bank/web/borrow/getBorrowDetail'+ '">'
						+'			<span class="id">' + repayList[i].borrowAssetNumber + '</span>'
						+'			<br>'
						+'			<span class="date">借款时间 ：'+ repayList[i].loanTime + '</span>'
						+'		</a>'
						+ '	</td>'
						+'	<td>'
						+ '<span class="highlight">'+repayList[i].borrowInterest+"%</span>"
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowPeriod
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowAccount
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowTotal
						+ '	</td>'
						+'	<td>'
						+'		<span class="end-date">还款时间 ：'+ repayList[i].repayTime+'</span>'
						+ '	</td>'
						if (roleId == "3") {// 垫付机构暂不支持汇添金还款
							// 常规还款
							var repaydate = new Date(repayList[i].repayTime);
							var nowdate = data.nowdate; // new Date();
							//if (repaydate - nowdate <= 3 * 24 * 60 * 60 * 1000) { // 3天以内，垫付机构可以代为还款
								strRepayList += 
									 '	<td>'
									+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '&roleId=' + roleId + '" class="btn credit-btn">还 款</a>'
									+'	</td>'
									+'</tr>';
							//} else {
							//	strRepayList +=
							//		 '	<td>'
							//		+'		<span class="btn credit-btn disabled">还 款</span>'
							//		+'	</td>' 
							//		+'</tr>';
							//}
						} else { // 借款人
							strRepayList  +=
								 '	<td>'
								+'		<a href="' + webPath+'/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" class="btn credit-btn">还 款</a>'
								+'	</td>'
								+'</tr>';
						}
				} else {
					strRepayList += 
						'<tr>'
						+'	<td>'
						+'		<a href="javascript:void(0)" class="title" data-borrowNid="' + repayList[i].borrowNid + '" data-detail="/bank/web/borrow/getBorrowDetail">'
						+'			<span class="id">' + repayList[i].borrowNid + '</span>'
						+' 			<br>'
						+'			<span class="date">借款时间 ：' + repayList[i].loanTime + '</span>'
						+'		</a>'
						+'	</td>'
						+'	<td>'
						+ '		<span class="highlight">'+repayList[i].borrowInterest+"%</span>"
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowPeriod
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowAccount
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowTotal
						+ '	</td>'
						+'	<td>'
						+'		<span class="end-date">还款时间 ：'+ repayList[i].repayTime
						+'<br/>'
						+'		<a href="' + webPath + '/bank/web/user/repay/downloadBorrowerPdf.do?borrowNid='+ repayList[i].borrowNid + '" class="highlight">下载协议</a></span>'
						+ '	</td>'
					if (roleId == "3") {// 垫付机构暂不支持汇添金还款
						// 常规还款
						var repaydate = new Date(repayList[i].repayTime);
						var nowdate = data.nowdate; // new Date();
						//if (repaydate - nowdate <= 3 * 24 * 60 * 60 * 1000) { // 3天以内，垫付机构可以代为还款
							strRepayList += 
								 '	<td>'
								+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '&roleId=' + roleId + '" class="btn credit-btn">还 款</a>'
								+'	</td>'
								+'</tr>';
						//} else {
						//	strRepayList +=
						//		 '	<td>'
						//		+'		<span class="btn credit-btn disabled">还 款</span>'
						//		+'	</td>' 
						//		+'</tr>';
						//}
					} else { // 借款人
						strRepayList  +=
							 '	<td>'
							+'		<a href="' + webPath+'/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" class="btn credit-btn">还 款</a>'
							+'	</td>'
							+'</tr>';
					}
				}
			}else if (repayList[i].status == "1") {//已还款
				if (repayList[i].projectType == 'RTB') {
					// 融通宝不展示协议
					strRepayList +=
						'<tr>'
						+'	<td>'
						+'		<span class="title">'
						+'			<span class="id">' + repayList[i].borrowAssetNumber + '</span>'
						+'			<br>'
						+'			<span class="date">借款时间 ：'+ repayList[i].loanTime + '</span>'
						+'		</span>'
						+ '	</td>'
						+'	<td>'
						+ '<span class="highlight">'+repayList[i].borrowInterest+"%</span>"
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowPeriod
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowAccount
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowTotal
						+ '	</td>'
						+'	<td>'
						+'		<span class="end-date">还款时间 ：'+ repayList[i].repayTime +'</span>'
						+ '	</td>'
						+ '	<td>'
						+'		<span class="btn credit-btn disabled">已还款</span>'
						+'	</td>'
						+'</tr>';
				} else {
					strRepayList += 
						'<tr>'
						+'	<td>'
						+'		<span class="title">'
						+'			<span class="id">' + repayList[i].borrowNid + '</span>'
						+' 			<br>'
						+'			<span class="date">借款时间 ：' + repayList[i].loanTime + '</span>'
						+'		</span>'
						+'	</td>'
						+'	<td>'
						+ '		<span class="highlight">'+repayList[i].borrowInterest+"%<span>"
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowPeriod
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowAccount
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowTotal
						+ '	</td>'
						+'	<td>'
						+'		<span class="end-date">还款时间 ：'+ repayList[i].repayTime
						+'		<br/>'
						+'		<a href="' + webPath + '/bank/web/user/repay/downloadBorrowerPdf.do?borrowNid='+ repayList[i].borrowNid + '" class="highlight">下载协议</a></span>'
						+ '	</td>'
						+ '	<td>'
						+'		<span class="btn credit-btn disabled">已还款</span>'
						+'	</td>'
						+'</tr>';
				}
			} else if (repayList[i].status == "2") { //还款中
				if (repayList[i].projectType == 'RTB') {
					// 融通宝不展示协议
					strRepayList +=
						'<tr>'
						+'	<td>'
						+'		<a href="javascript:void(0)" class="title" data-borrowNid="'+ repayList[i].borrowNid+ '" data-detail="/bank/web/borrow/getBorrowDetail'+ '">'
						+'			<span class="id">' + repayList[i].borrowAssetNumber + '</span>'
						+'			<br>'
						+'			<span class="date">借款时间 ：'+ repayList[i].loanTime + '</span>'
						+'		</a>'
						+ '	</td>'
						+'	<td>'
						+ '<span class="highlight">'+repayList[i].borrowInterest+"%</span>"
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowPeriod
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowAccount
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowTotal
						+ '	</td>'
						+'	<td>'
						+'		<span class="end-date">还款时间 ：'+ repayList[i].repayTime + '</span>'
						+ '	</td>'
						if (roleId == "3") {
							// 垫付机构
							if (repayList[i].repayMoneySource != null && repayList[i].repayMoneySource == 1) {
								strRepayList += 
									 '	<td>'
									+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" target="_blank" class="btn credit-btn disabled">还款中</a>'
									+'	</td>'
									+'</tr>';
							} else {
								strRepayList +=
									 '	<td>'
									+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" target="_blank" class="btn credit-btn disabled">个人还款中</a>'
									+'	</td>' 
									+'</tr>';
							}
						} else { 
							// 借款人
							if (repayList[i].repayMoneySource != null && repayList[i].repayMoneySource == 1) {
								strRepayList += 
									 '	<td>'
									+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" target="_blank" class="btn credit-btn disabled">机构还款中</a>'
									+'	</td>'
									+'</tr>';
							} else {
								strRepayList +=
									 '	<td>'
									+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" target="_blank" class="btn credit-btn disabled">还款中</a>'
									+'	</td>' 
									+'</tr>';
							}
					  }
				} else {
					strRepayList += 
						'<tr>'
						+'	<td>'
						+'		<a href="javascript:void(0)" class="title" data-borrowNid="' + repayList[i].borrowNid + '" data-detail="/bank/web/borrow/getBorrowDetail">'
						+'			<span class="id">' + repayList[i].borrowNid + '</span>'
						+' 			<br>'
						+'			<span class="date">借款时间 ：' + repayList[i].loanTime + '</span>'
						+'		</a>'
						+'	</td>'
						+'	<td>'
						+ '<span class="highlight">'+repayList[i].borrowInterest+"%</span>"
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowPeriod
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowAccount
						+ '	</td>'
						+'	<td>'
							+ repayList[i].borrowTotal
						+ '	</td>'
						+'	<td>'
						+'		<span class="end-date">还款时间 ：'+ repayList[i].repayTime
						+'		<br/>'
						+'			<a href="' + webPath + '/bank/web/user/repay/downloadBorrowerPdf.do?borrowNid='+ repayList[i].borrowNid + '" class="highlight">下载协议</a>'
						+'		</span>'
						+ '	</td>'
					if (roleId == "3") {
						// 垫付机构
						if (repayList[i].repayMoneySource != null && repayList[i].repayMoneySource == 1) {
							strRepayList += 
								 '	<td>'
								+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" target="_blank" class="btn credit-btn disabled">还款中</a>'
								+'	</td>'
								+'</tr>';
						} else {
							strRepayList +=
								 '	<td>'
								+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" target="_blank" class="btn credit-btn disabled">个人还款中</a>'
								+'	</td>' 
								+'</tr>';
						}
					} else { 
						// 借款人
						if (repayList[i].repayMoneySource != null && repayList[i].repayMoneySource == 1) {
							strRepayList += 
								 '	<td>'
								+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" target="_blank" class="btn credit-btn disabled">机构还款中</a>'
								+'	</td>'
								+'</tr>';
						} else {
							strRepayList +=
								 '	<td>'
								+'		<a href="' + webPath + '/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" target="_blank" class="btn credit-btn disabled">还款中</a>'
								+'	</td>' 
								+'</tr>';
						}
					}
				}
			} 
		}
	} else {
		$("#new-pagination").html("")
	}
	if (strRepayList == "") {
		strRepayList = "<td colspan='7'>未查到" + $("#qNid").val() + "标号的相关信息</td>";
	}
	$("#repayList").nextAll().remove();
	$("#repayList").after(strRepayList);
}

// 获取借款列表失败
function getRepayListErrorCallback(data) {
}
