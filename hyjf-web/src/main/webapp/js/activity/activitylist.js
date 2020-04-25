$(document).ready(
		function() {
			/**
			 * 获取活动列表
			 */
			getActivityListPage();

			function getActivityListPage() {
				$("#paginatorPage").val(1);
				$("#pageSize").val(8);
				doRequest(activityPageSuccessCallback,
						activityPageErrorCallback, webPath
								+ "/activity/getActivityListPage.do", $(
								"#listForm").serialize(), true, null, "new-pagination");
			}

			/**
			 * 获取活动列表信息成功回调
			 */
			function activityPageSuccessCallback(data) {

				var activityList = data.adsList;
				// 挂载数据
				var strActivityPages = "";
				for (var i = 0; i < activityList.length; i++) {
					strActivityPages = strActivityPages
							+ "<li><a href='"
							+ activityList[i].url
							+ "' class='act-li-img'>"
							+ "<img src='"
							+ webPath
							+ activityList[i].image
							+ "' alt=''>"
							+ "</a>"
							+ "<div class='act-li-content'>"
							+ "<div class='act-li-title'><a href='"
							+ webPath
							+ activityList[i].url
							+ "'> <!--hasflg未读取状态-->"
							+ activityList[i].name
							+ " "
							+ "</a>"
							if(activityList[i].isEnd == "0"){
								strActivityPages = strActivityPages
								+ "<img style='height:15px;' src='"
								+ webPath
								+ "/img/new.png'>"
								+ "</img>"
							}
							strActivityPages = strActivityPages
							+ "</div>"
							+ "<div class='act-li-date'>"
							+ new Date( activityList[i].startTime).format("yyyy-MM-dd")
							+ "— —"
							+ new Date( activityList[i].endTime).format("yyyy-MM-dd")
							+ "</div><div class='act-li-txt'>"
							+ activityList[i].activitiDesc
							+"</div><a href='"
							+ activityList[i].url
							+ "' class='act-li-more '>查看详情</a>"
							+ "</div>"
							if(activityList[i].isEnd == "1"){
								strActivityPages = strActivityPages
								+ "<di class='activityStatus'> "
								+ "<div class='endActivity'>"
								+ "</div>"
								+ "<span class='endDes'>已结束"
								+ "</span></di>"
							}
							strActivityPages = strActivityPages
							+ "</li>"
				}
				
				$("#strActivityPages").html(strActivityPages);

			}

			/**
			 * 获取活动信息失败回调
			 */
			function activityPageErrorCallback(data) {

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
				doRequest(activityPageSuccessCallback,
						activityPageErrorCallback, webPath
								+ "/activity/getActivityListPage.do", $(
								"#listForm").serialize(), true, null, "new-pagination");
				scrollTo();
			}
			
		});
