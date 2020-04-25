$(document).ready(
		function() {
			/**
			 * 获取公告列表
			 */
			getNoticeListPage();

			function getNoticeListPage() {
				$("#paginatorPage").val(1);
				$("#pageSize").val(8);
				doRequest(noticePageSuccessCallback,
						noticePageErrorCallback, webPath
								+ "/notice/getNoticeListPage.do", $(
								"#listForm").serialize(), true, null, "new-pagination");
			}

			/**
			 * 获取公司公告信息成功回调
			 */
			function noticePageSuccessCallback(data) {

				var noticeList = data.companyNoticeList;
				// 挂载数据
				var strNoticePages = "";
				for (var i = 0; i < noticeList.length; i++) {
					strNoticePages = strNoticePages
							+ "<li><div class='ann-li-title'><a href='"
							+ webPath
							+ "/notice/getNoticeInfo.do?id="
							+ noticeList[i].id
							+ "'>"
							+ noticeList[i].title
							+ "</a></div>"
							+ "<div class='ann-li-txt'>"
							+ noticeList[i].summary
							+ "</div>"
							+ "<div class='ann-li-date'>"
							+ new Date(noticeList[i].createTime).format("yyyy-MM-dd hh:mm:ss")
							+ "</div></li>"
				}
				$("#strNoticeListPage").html(strNoticePages);

			}

			/**
			 * 获取活动信息失败回调
			 */
			function noticePageErrorCallback(data) {

			}

			/**
			 * 为分页按钮绑定事件
			 */
			$(document).on("click", ".flip", function() {
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
				doRequest(noticePageSuccessCallback,
						noticePageErrorCallback, webPath
								+ "/notice/getNoticeListPage.do", $(
								"#listForm").serialize(), true, null, "new-pagination");
				scrollTo();
			}
			
		});
