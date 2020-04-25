$(document).ready(
		function() {
			/**
			 * 获取活动列表
			 */
			getNoticeListPage();

			function getNoticeListPage() {
				var page = getUrlParms("page") || 1;
				$("#paginatorPage").val(page);
				$("#pageSize").val(10);
				doRequest(contentArticlePageSuccessCallback,
						contentArticlePageErrorCallback, webPath     
								+ "/contentarticle/getNoticeListPage.do?type=2",
								$("#listForm").serialize(), true, null, "new-pagination");
			}
			
			//获取URL参数
			function getUrlParms(name){
				var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
				var r = window.location.search.substr(1).match(reg);
				if(r!=null)
				return unescape(r[2]);
				return null;
		    }

			/**
			 * 获取活动列表信息成功回调
			 */
			function contentArticlePageSuccessCallback(data) {              
				var contentArticleList = data.contentArticleList;
				// 挂载数据
				var strNoticePages = "";

				for (var i = 0; i < contentArticleList.length; i++) {
					strNoticePages = strNoticePages
							+ "<li><a target='_self' itemid='lt"+i+"' href='"
							+ webPath+"/contentarticle/getNoticeInfo.do?id="
							+ contentArticleList[i].id
							+ "'>"
							+"<span class='float-left title'>"
							+ contentArticleList[i].title
							+"</span>"
							+"<span class='float-right date'>"
							+ new Date(contentArticleList[i].createTime).format("yyyy-MM-dd")
							+ "</span></a>"
							+ "</li>";
					
				}
				
				$("#announce-list").html(strNoticePages);
			}

			/**
			 * 获取活动信息失败回调
			 */
			function contentArticlePageErrorCallback(data) {
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
				document.cookie = 'announcePage='+paginatorPage+';path=/'
				doRequest(contentArticlePageSuccessCallback,
						contentArticlePageErrorCallback, webPath
								+ "/contentarticle/getNoticeListPage.do?type=2",
								$("#listForm").serialize(), true, null, "new-pagination");
				scrollTo();
			}
			
			function getLocalTime(nS) {  
				 return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');  
				}
			
		});

function goDetail(id){
	window.location.href=webPath+"/contentarticle/initdetail.do?articleId="+id;
}
