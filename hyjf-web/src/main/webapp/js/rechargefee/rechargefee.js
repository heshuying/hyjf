	$(document).ready(
			function() {
				//交易明细页面form表单查询条件添加
				var feeform=" <input id='startTimeSrch' name='startTimeSrch' type='text'></input>"
					+"<input id='endTimeSrch' name='endTimeSrch' type='text' ></input>"
					+"<input id='statusSrch' name='statusSrch' type='text' ></input>";
				
				$("#listForm").append(feeform);
				$("#startTime").val("");
				$("#endTime").val("");
				$("#dstatus").val("");
				getFeeListPage();
	});
		/**
		 * 获取明细信息
		 */
		function getFeeListPage() {
			$("#paginatorPage").val(1);
			$("#pageSize").val(pageSize);
			$("#startTimeSrch").val($("#startTime").val());
			$("#endTimeSrch").val($("#endTime").val());
			$("#statusSrch").val($("#dstatus").val());
			doRequest(
					feePageSuccessCallback,
					feePageErrorCallback, 
					webPath+ "/rechargeFee/rechargeFeeList.do",
					$("#listForm").serialize(), true,"flipClass","new-pagination");
		}
		
		/**
		 * 获取明细信息成功回调
		 */
		function feePageSuccessCallback(data) {
			var rechargeFeeListStr ="";
			// 挂载数据
				var rechargeFeeList = data.rechargeFeeList;
				rechargeFeeListStr = "<tr class='invite-table-header'> " +
						"<td>账单编号</td>" +
						"<td>账单周期</td> " +
						"<td>累计充值金额（元）</td>" +
						"<td>平台累计垫付手续费（元）</td> " +
						"<td>应付款</td> " +
						"<td>状态</td>" +
						"<td>生成时间</td> " +
						"<td>操作</td></tr>";
				for (var i = 0; i < rechargeFeeList.length; i++) {
					var rechargeFee = rechargeFeeList[i];
					rechargeFeeListStr = rechargeFeeListStr
					+ "<tr>"
					+   "<td>"+rechargeFee.rechargeNid+"</td>"
					+ 	"<td>"+timeView(rechargeFee.startTime,'yyyy-MM-dd')+" - "+timeView(rechargeFee.endTime,'yyyy-MM-dd')+"</td>"
					+ 	"<td>"+rechargeFee.rechargeAmount+"</td>"
					+ 	"<td>"+rechargeFee.rechargeFee+"</td>"
					+ 	"<td>"+rechargeFee.rechargeFee+"</td>"
					+ 	"<td class='"+(rechargeFee.status == '0'?'failed':'')+"'>"+statusView(rechargeFee.status)+"</td>"
					+ 	"<td>"+timeView(rechargeFee.addTime,'yyyy-MM-dd HH:mm:ss')+"</td>"
					+ 	"<td>"+opertionView(rechargeFee.status,rechargeFee.id,timeView(rechargeFee.startTime,'yyyy-MM-dd'),timeView(rechargeFee.endTime,'yyyy-MM-dd'))+"</td>"
					+ 	"</tr>";
				}
				$("#feetable").html(rechargeFeeListStr);
		}

		/**
		 * 获取明细信息失败回调
		 */
		function feePageErrorCallback(data) {

		}
		$(document).on("click", ".flipClass", function() {
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
			doRequest(
					feePageSuccessCallback,
					feePageErrorCallback, 
					webPath+ "/rechargeFee/rechargeFeeList.do",
					$("#listForm").serialize(), true,"flipClass","new-pagination");
		}
		//状态中文显示
		function statusView(status){
		    if(status == 0){
				return "待付款";
			}else if(status == 1){
				
				return "已付款";
			}else{
				return status;
			}
		}
		//操作栏展示内容
		function opertionView(status,recordId,startDate,endDate){
			var downurl = webPath + "/rechargeFee/exportAction.do?startDate="+startDate+"&endDate="+endDate;
		    if(status == 0){
		    	var payurl = webPath+"/rechargeFee/rechargeFeePay.do?recordId="+recordId;
				return "<a class='btn' href='"+payurl+"'>付款</a> <a href='"+downurl+"' class='detail-btn'>下载明细</a>";
			}else if(status == 1){
				return "<a href='"+downurl+"' class='detail-btn' >下载明细</a>";
			}else{
				return "<a href='"+downurl+"' class='detail-btn' >下载明细</a>";
			}
		}
		//时间戳转换
		function timeView(timestamp,pattern){
		    if(timestamp == null){
				return "";
			}else{
				var newDate = new Date();
				newDate.setTime(timestamp * 1000);
				return newDate.Format(pattern);
			}
		}		
		Date.prototype.Format = function (fmt) { //author: meizz 
		    var o = {
		        "M+": this.getMonth() + 1, //月份 
		        "d+": this.getDate(), //日 
		        "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
		        "H+" : this.getHours(), //小时 
		        "m+": this.getMinutes(), //分 
		        "s+": this.getSeconds(), //秒 
		        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		        "S": this.getMilliseconds() //毫秒 
		    };
		    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		    for (var k in o)
		    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		    return fmt;
		}
	