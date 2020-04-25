$(document)
		.ready(
				function() {

					// 列表切换绑定事件
					$(".project-tab").children("li").click(function() {
						if ($(this).hasClass("active")) {

						} else {
							$("#status").val($(this).data("status"));
							getLoanList();
						}
					});

					getAllLoanList();

					

					/**
					 * 为分页按钮绑定事件
					 */
					$(document).on("click", ".flip", function() {
						flip($(this).data("page"));
					});

					/**
					 * 为分页按钮绑定事件
					 */
					$(document)
							.on(
									"click",
									"a.title",
									function() {
										$("#loanForm")
												.attr(
														"action",
														webPath
																+ "/project/" + $(this).data("detail") + ".do");
										$("#borrowNid").val(
												$(this).data("borrownid"));
										$("#loanForm").submit();
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
						$("#loanPaginatorPage").val(paginatorPage);
						$("#loanPageSize").val(pageSize);
						doRequest(getLoanListSuccessCallback,
								getLoanListErrorCallback, webPath
										+ "/user/repay/repaylist.do", $(
										"#loanForm").serialize(), true, null,
								"new-pagination");
					}

					$(".project-tab").click(
							function(e) {
								var _self = $(e.target);
								if (_self.is("li")) {
									var idx = _self.attr("panel");
									var panel = _self.parent().siblings(
											".project-tab-panel");
									_self.siblings("li.active").removeClass(
											"active");
									_self.addClass("active");
								}
							})

				});

//获取所有借款列表
function getAllLoanList() {
	$("#status").val('');
	getLoanList();
}

// 根据标号获取列表
function getLoanListById() {
	$("#borrowNid").val($("#qNid").val());
	getLoanList();
}

// 获取借款列表
function getLoanList() {
	doRequest(getLoanListSuccessCallback,
			getLoanListErrorCallback, webPath
					+ "/user/repay/repaylist.do", $(
					"#loanForm").serialize(), true, null,
			"new-pagination");
}

// 获取借款列表成功
function getLoanListSuccessCallback(data) {
	var loanList = data.replayList;
	var roleId = data.roleId;
	var strLoanList = "";
	if (loanList != null) {
		for (var i = 0; i < loanList.length; i++) {
			var length = loanList[i].borrowNid.length;

			if (loanList[i].status == "1") {
				if(loanList[i].projectType == '融通宝'){//融通宝不展示协议
				strLoanList = strLoanList
						+ '<tr><td><span class="title" data-borrowNid="'
						+ loanList[i].borrowNid
						+ '"><span class="id">'
						+ loanList[i].borrowAssetNumber
						+ '</span> <br><span class="date">借款时间 ：'
						+ loanList[i].loanTime
						+ '</span></span>';
				}else{
					strLoanList = strLoanList
					+ '<tr><td><span class="title" data-borrowNid="'
					+ loanList[i].borrowNid
					+ '"><span class="id">'
					+ loanList[i].projectType
					/*+ loanList[i].borrowNid.substring(
							0, length - 2)
					+ '-'
					+ loanList[i].borrowNid.substring(
							length - 2, length)*/
					+ loanList[i].borrowNid
					+ '</span> <br><span class="date">借款时间 ：'
					+ loanList[i].loanTime
					+ '</span></span>';
				}
			} else {
				if(loanList[i].tType == "1"){//汇添金专属标的
					strLoanList = strLoanList
					+ '<tr><td><a href="javascript:void(0)" class="title" data-borrowNid="'
					+ loanList[i].borrowNid
					+ '" data-detail="getHtjProjectDetail'
					+ '"><span class="id">'
					+ loanList[i].projectType
					/*+ loanList[i].borrowNid.substring(
					0, length - 2)
					+ '-'
					+ loanList[i].borrowNid.substring(
							length - 2, length)*/
					+ loanList[i].borrowNid
					+ '</span> <br><span class="date">借款时间 ：'
					+ loanList[i].loanTime
					+ '</span></a>';
				}else{
					
					if(loanList[i].projectType == '融通宝'){//融通宝不展示协议
						strLoanList = strLoanList
						+ '<tr><td><a href="javascript:void(0)" class="title" data-borrowNid="'
						+ loanList[i].borrowNid
						+ '" data-detail="getProjectDetail'
						+ '"><span class="id">'
//						+ loanList[i].projectType
						+ loanList[i].borrowAssetNumber
						+ '</span> <br><span class="date">借款时间 ：'
						+ loanList[i].loanTime
						+ '</span></a>';
					}else{
						strLoanList = strLoanList
						+ '<tr><td><a href="javascript:void(0)" class="title" data-borrowNid="'
						+ loanList[i].borrowNid
						+ '" data-detail="getProjectDetail'
						+ '"><span class="id">'
						+ loanList[i].projectType
						/*+ loanList[i].borrowNid.substring(
						0, length - 2)
						+ '-'
						+ loanList[i].borrowNid.substring(
								length - 2, length)*/
						+ loanList[i].borrowNid
						+ '</span> <br><span class="date">借款时间 ：'
						+ loanList[i].loanTime
						+ '</span></a>';
					}
				
				}
			}
			if(loanList[i].status == "3"){
				strLoanList += '</td><td><span class="highlight">'
					+ loanList[i].borrowInterest
					+ '<em> %</em></span></td><td>'
					+ loanList[i].borrowPeriod
					+ '</td><td>'
					+ loanList[i].borrowAccount
					+ '</td><td>'
					+ loanList[i].borrowTotal
					+ '</td><td><span class="end-date">';
			}else{
				strLoanList += '</td><td><span class="highlight">'
					+ loanList[i].borrowInterest
					+ '<em> %</em></span></td><td>'
					+ loanList[i].borrowPeriod
					+ '</td><td>'
					+ loanList[i].borrowAccount
					+ '</td><td>'
					+ loanList[i].borrowTotal
					+ '</td><td><span class="end-date">'
					+ loanList[i].repayTime;
					
			}
			if(loanList[i].tType == "1"){//汇添金专属标的
				strLoanList += ' <br> <a href="'
						+ webPath
						+ '/user/repay/downloadBorrowerPdf.do?tType=1&borrowNid='
						+ loanList[i].borrowNid
						+ '" target="_blank" class="highlight">下载协议</a></span></td>';
			}else{
				if(loanList[i].projectType == '融通宝'){//融通宝不展示协议
					strLoanList += '</span></td>';
				}else{
					strLoanList += ' <br> <a href="'
							+ webPath
							+ '/user/repay/downloadBorrowerPdf.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="highlight">下载协议</a>'
							+ '</span></td>';
				}
			}
			//alert(data.nowdate+"~~~"+new Date(loanList[i].repayTime)+"---"+(new Date(loanList[i].repayTime)-data.nowdate));
			if (loanList[i].status == "0") {// 还款
				if(roleId=="3"){//垫付机构暂不支持汇添金还款
					if(loanList[i].tType == "1"){//汇添金专属表还款
						strLoanList = strLoanList
						+ '<td><span class="btn credit-btn disabled">还 款</span></td> </tr>';
					}else{//常规还款
						var repaydate = new Date(loanList[i].repayTime);
						var nowdate = data.nowdate; // new Date();
						if(repaydate - nowdate <=3*24*60*60*1000){ //3天以内，垫付机构可以代为还款
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/user/repay/repaydetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '&roleId='
							+ roleId
							+ '" class="btn credit-btn">还 款</a></td> </tr>';
						}else{
							strLoanList = strLoanList
							+ '<td><span class="btn credit-btn disabled">还 款</span></td> </tr>';
						}
						
						
					}
				}else{ //借款人
					strLoanList = strLoanList
					+ '<td><a href="'
					+ webPath
					if(loanList[i].tType == "1"){//汇添金专属表还款
						strLoanList = strLoanList
						+ '/plan/userRepay/repayDetail.do?borrowNid='
						+ loanList[i].borrowNid
						+ '" class="btn credit-btn">还 款</a></td> </tr>';
					}else{//常规还款
						strLoanList = strLoanList
						+ '/user/repay/repaydetail.do?borrowNid='
						+ loanList[i].borrowNid
						+ '" class="btn credit-btn">还 款</a></td> </tr>';
					}
				}
			} else if (loanList[i].status == "1") {// 已还款
				strLoanList = strLoanList
						+ '<td><span class="btn credit-btn disabled">已还款</span></td> </tr>';
			} else if (loanList[i].status == "2") {// 还款中
				if(loanList[i].tType == "1"){//汇添金专属表还款
					if(roleId=="3"){//垫付机构登录
						if(loanList[i].repayMoneySource !=null && loanList[i].repayMoneySource == 1){
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/plan/userRepay/repayDetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="btn credit-btn disabled">还款中</a></td> </tr>';
						}else{
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/plan/userRepay/repayDetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="btn credit-btn disabled">个人还款中</a></td> </tr>';
						}
					}else{//借款人登录
						if(loanList[i].repayMoneySource !=null && loanList[i].repayMoneySource == 1){
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/plan/userRepay/repayDetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="btn credit-btn disabled">机构还款中</a></td> </tr>';
						}else{
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/plan/userRepay/repayDetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="btn credit-btn disabled">还款中</a></td> </tr>';
						}
					}
					
				}else{//非汇添金还款
//					+ '" target="_blank" class="btn credit-btn disabled">还款中'+roleId+'</a></td> </tr>';
					if(roleId=="3"){//垫付机构登录
						if(loanList[i].repayMoneySource !=null && loanList[i].repayMoneySource == 1){
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/user/repay/repaydetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="btn credit-btn disabled">还款中</a></td> </tr>';
						}else{
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/user/repay/repaydetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="btn credit-btn disabled">个人还款中</a></td> </tr>';
						}
					}else{//借款人登录
						if(loanList[i].repayMoneySource !=null && loanList[i].repayMoneySource == 1){//机构垫付
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/user/repay/repaydetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="btn credit-btn disabled">机构还款中</a></td> </tr>';
						}else{
							strLoanList = strLoanList
							+ '<td><a href="'
							+ webPath
							+ '/user/repay/repaydetail.do?borrowNid='
							+ loanList[i].borrowNid
							+ '" target="_blank" class="btn credit-btn disabled">还款中</a></td> </tr>';
						}
					}
					
				}
			} else if (loanList[i].status == "3") {// 复审中
				strLoanList = strLoanList
						+ '<td><span class="btn credit-btn disabled">复审中</span></td> </tr>';
			}
		}
	} else {
		$("#new-pagination").html("")
	}
	if(strLoanList==""){
		strLoanList="<td>未查到"+$("#qNid").val()+"标号的相关信息</td>";
	}
	$("#loanList").nextAll().remove();
	$("#loanList").after(strLoanList);
}

// 获取借款列表失败
function getLoanListErrorCallback(data) {
}
