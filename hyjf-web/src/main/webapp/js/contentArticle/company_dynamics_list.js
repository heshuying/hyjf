$(document).ready(
	function() {

		/**
		 * 获取公司动态
		 */
		getCompanyDynamicsListPage();
		
		function getCompanyDynamicsListPage() {
			var page = getUrlParms("page") || 1;
			$("#paginatorPage").val(page);
			$("#pageSize").val(10);
			doRequest(companyDynamicsPageSuccessCallback,
					companyDynamicsPageErrorCallback, webPath
							+ "/contentarticle/getCompanyDynamicsListPage.do", $(
							"#listForm").serialize(), true, null,
					"new-pagination");
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
		 * 获取公司动态信息成功回调
		 */
		function companyDynamicsPageSuccessCallback(data) {
			
			var contentArticleList = data.contentArticleList;

			// 挂载数据
			var strCompanyDynamicsPage = "";

			for (var i = 0; i < contentArticleList.length; i++) {
				strCompanyDynamicsPage = strCompanyDynamicsPage
         		+"<li>"
                +"<a href= '"+webPath+"/contentarticle/getCompanyDynamicsDetail.do?id="+contentArticleList[i].id+"'"
                +"' target='_self' itemid='lt1'>"
                +"<span class='float-left title'>"  
                +contentArticleList[i].title
                +"</span>"
                +"<span class='float-right date'>"  
                + new Date(contentArticleList[i].createTime).format("yyyy-MM-dd")
                +"</span>"
                +"</a>"
                +"</li>";
			} 
			
			$("#strCompanyDynamicsPage").html(strCompanyDynamicsPage);
			utils.scrollTo();
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
			document.cookie = 'companyDyPage='+paginatorPage+';path=/'
			doRequest(companyDynamicsPageSuccessCallback,
					companyDynamicsPageErrorCallback, webPath
							+ "/contentarticle/getCompanyDynamicsListPage.do", $(
							"#listForm").serialize(), true, null,"new-pagination");
		}

 });
