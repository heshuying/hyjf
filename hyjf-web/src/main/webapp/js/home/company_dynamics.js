$(document).ready(
		function() {

			/**
			 * 获取公司动态
			 */
			getCompanyDynamicsListPage();

			function getCompanyDynamicsListPage() {
				$("#paginatorPage").val(1);
				$("#pageSize").val(8);
				doRequest(companyDynamicsPageSuccessCallback,
						companyDynamicsPageErrorCallback, webPath
								+ "/homepage/getCompanyDynamicsListPage.do", $(
								"#listForm").serialize(), true, null,
						"new-pagination");
			}

			/**
			 * 获取公司动态信息成功回调
			 */
			function companyDynamicsPageSuccessCallback(data) {

				var companyDynamicsList = data.companyDynamicsList;

				// 挂载数据
				var strCompanyDynamicsPage = "";

				for (var i = 0; i < companyDynamicsList.length; i++) {
					strCompanyDynamicsPage = strCompanyDynamicsPage
							+ "<li><div class='ann-li-title'><a href='"
							+ webPath
							+ "/homepage/getCompanyDynamicsDetail.do?id="
							+ companyDynamicsList[i].id
							+ "'>"
							+ companyDynamicsList[i].title
							+ "</a></div><div class='ann-li-txt'>"
							+ companyDynamicsList[i].summary
							+ "</div><div class='ann-li-date'>"
							+ new Date(companyDynamicsList[i].createTime)
									.format("yyyy-MM-dd hh:mm:ss");
					+"</div></li>"

				}

				$("#strCompanyDynamicsPage").html(strCompanyDynamicsPage);

			}

			/**
			 * 获取公司动态信息失败回调
			 */
			function companyDynamicsPageErrorCallback(data) {

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
				doRequest(companyDynamicsPageSuccessCallback,
						companyDynamicsPageErrorCallback, webPath
								+ "/homepage/getCompanyDynamicsListPage.do", $(
								"#listForm").serialize(), true, null,
						"new-pagination");
			}

		});
