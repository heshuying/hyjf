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
								+ "/newactivity/getActivityList.do", $(
								"#listForm").serialize(), true, null, "new-pagination");
			}

			/**
			 * 获取活动列表信息成功回调
			 */
			function activityPageSuccessCallback(data) {
				var activityList = data.activityListBeanList;
				// 挂载数据
				var strActivityPages = "";
				for (var i = 0; i < activityList.length; i++) {
					strActivityPages = strActivityPages
							+ "<li><a href='"
							+ activityList[i].url
							+ "' class='act-li-img'>"
							+ "<img src='"
							+ webPath
							+ activityList[i].img
							+ "' alt=''>"
							+ "</a>"
							+ "<div class='act-li-content'>"
							+ "<div class='act-li-title'><a href='"
							+ activityList[i].urlForeground
							+ "'> <!--hasflg未读取状态-->"
							+ activityList[i].title
							+ " "
							+ "</a>"
							if(activityList[i].isConductFlg == "1"){
								strActivityPages = strActivityPages
								+ "<img style='height:15px;' src='"
								+ webPath
								+ "/img/new.png'>"
								+ "</img>"
							}
							strActivityPages = strActivityPages
							+ "</div>"
							+ "<div class='act-li-date'>"
							+ getLocalTime(activityList[i].timeStart)
							+ "—"
							+ getLocalTime(activityList[i].timeEnd)
							+ "</div><div class='act-li-txt'>"
							+ activityList[i].description
							+"</div><a href='"
							+ activityList[i].urlForeground
							+ "' class='act-li-more '>查看详情</a>"
							+ "</div>"
							if(activityList[i].overdueflg == "1"){
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
								+ "/newactivity/getActivityList.do", $(
								"#listForm").serialize(), true, null, "new-pagination");
				scrollTo();
			}
			
			function getLocalTime(nS) {  
				 return new Date(parseInt(nS)*1000).format("yyyy-MM-dd").toLocaleString().replace(/:\d{1,2}$/,' ');  
				}
			
		});
