$(document).ready(
		function() {
			/**
			 * 获取公告列表
			 */
			getMediaReportListPage();

			function getMediaReportListPage() {
				var page = getUrlParms("page") || 1;
				$("#paginatorPage").val(page);
				$("#pageSize").val(10);
				doRequest(mediaPageSuccessCallback,
						mediaPageErrorCallback,
						webPath + "/aboutus/getKnowReportList.do",$("#listForm").serialize(), true, null, "new-pagination");
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
			 * 获取公司公告信息成功回调
			 */
			function mediaPageSuccessCallback(data) {
				var noticeList = data.mediaReportList;
				// 挂载数据
				var strNoticePages = "";
				for (var i = 0; i < noticeList.length; i++) {
					strNoticePages = strNoticePages
             		+"<li>"
                    +"<a href= '"+webPath+"/aboutus/getMediaReportInfo.do?id="+noticeList[i].id+"'"
                    +"target='_self' itemid='lt"+(i+1)+"'>"
                    +"<span class='float-left title'>"  
                    +noticeList[i].title
                    +"</span>"
                    +"<span class='float-right date'>"  
                    + new Date(noticeList[i].createTime).format("yyyy-MM-dd")
                    +"</span>"
                    +"</a>"
                    +"</li>";
				}
				$("#strMediaListPage").html(strNoticePages);
			}

			/**
			 * 获取活动信息失败回调
			 */
			function mediaPageErrorCallback(data) {
				/*alert(2);*/
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
				document.cookie = 'knowPage='+paginatorPage+';path=/'
				doRequest(mediaPageSuccessCallback,
						mediaPageErrorCallback, webPath
								+ "/aboutus/getKnowReportList.do", $(
								"#listForm").serialize(), true, null, "new-pagination");
				scrollTo();
			}
			
		});
