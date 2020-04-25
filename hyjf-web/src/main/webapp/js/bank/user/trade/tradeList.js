	$(document).ready(
			function() {
				//交易明细页面form表单查询条件添加
				var tradeform=" <input id='startDate' name='startDate' type='text'></input>"
					+"<input id='endDate' name='endDate' type='text' ></input>"
					+"<input id='trade' name='trade' type='text' ></input>";
				
				
				$("#listForm").append(tradeform);
				$("#qStartTime").val("");
				$("#qEndTime").val("");
				$("#trade").val("");
				getTradeListPage();
	});
		/**
		 * 获取明细信息
		 */
		function getTradeListPage() {
			$("#paginatorPage").val(1);
			$("#pageSize").val(pageSize);
			$("#startDate").val($("#qStartTime").val());
			$("#endDate").val($("#qEndTime").val());
			$("#trade").val($("#tradeSelect").val());
			doRequest(
					tradePageSuccessCallback,
					tradePageErrorCallback, 
					webPath+ "/bank/user/trade/"+listtyp+".do",
					$("#listForm").serialize(), true,"flipClass","new-pagination");
		}
		
		
		function changetype(type){
			listtyp=type;
			getTradeListPage();
		}
		
		
		/**
		 * 获取明细信息成功回调
		 */
		function tradePageSuccessCallback(data) {
			var tradeListStr ="";
		
			// 挂载数据
			if(data.listType=="trade"){
				var tradeList = data.tradeList;
				tradeListStr = "<tr class='invite-table-header'> " +
						"<td>交易日期</td>" +
						"<td>交易类型</td> " +
						"<td>交易金额</td>" +
						"<td>银行存管账户余额</td> " +
						(data.isChinapnrOpen == "true" ? "<td>汇付天下账户余额</td> " : "") +
						"<td>备注</td></tr>";
				for (var i = 0; i < tradeList.length; i++) {
					var trade =tradeList[i];
					tradeListStr =tradeListStr 
					+ '<tr id="'+trade.id+'">'
					+ 	'<td>'+trade.time+'</td>'
					+ 	'<td>'+trade.type+'</td>'
					+ 	'<td>'+trade.money+'</td>'
					+ 	'<td>'+trade.bankBalance+'</td>'
					+ 	(data.isChinapnrOpen == "true" ? '<td>'+trade.balance+'</td>' : '')
					+ 	'<td>'+ifnull(trade.remark)+'</td>'
					+ 	'</tr>';
				}
				$("#pant0").html(tradeListStr);
			}
			if(data.listType!="trade"){
				var tradeList = null;
				var name="";
				if(data.listType=="recharge"){
					tradeList = data.rechargeList;
				}
				if(data.listType=="withdraw"){
					tradeList = data.withdrawList;
					 name="<td>操作平台</td>";
					 
					
				}
				tradeListStr = "<tr class='invite-table-header'> " +
						"<td>交易日期</td> " +
						"<td>交易金额(元)</td> " +
						"<td>费用(元)</td> " +
						"<td>到账金额(元)</td> " +
						"<td>状态</td>"+
						name+
						" </tr>"
					
						;
				for (var i = 0; i < tradeList.length; i++) {
					var trade =tradeList[i];
					tradeListStr =tradeListStr 
					+ '<tr id="'+trade.id+'">'
					+ 	'<td>'+trade.time+'</td>'
					+ 	'<td>'+trade.money+'</td>'
					+ 	'<td>'+trade.fee+'</td>'
					+ 	'<td>'+trade.balance+'</td>'
					+ 	'<td>'+ifnull(trade.status)+'</td>';
					if(data.listType=="withdraw"){
						if(trade.bankFlag==null ||trade.bankFlag=="0"){
							tradeListStr =tradeListStr 
							+'<td>汇付天下</td></tr>';
						}else {
							tradeListStr =tradeListStr 
							+'<td>江西银行</td></tr>';
						}
					}else{
						tradeListStr =tradeListStr  + 	'</tr>';
					}
				}
				if(data.listType=="recharge"){
					$("#pant1").html(tradeListStr);
				}
				if(data.listType=="withdraw"){
					$("#pant2").html(tradeListStr);
				}
			}
		}

		/**
		 * 获取明细信息失败回调
		 */
		function tradePageErrorCallback(data) {

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
					tradePageSuccessCallback,
					tradePageErrorCallback, 
					webPath+ "/bank/user/trade/"+listtyp+".do",
					$("#listForm").serialize(), true,"flipClass","new-pagination");
		}
		
		//处理null显示
		//状态中文显示
		function ifnull(nullstr){
			if(nullstr==null||nullstr=="null"){
				return "";
			}else if(nullstr=="0"||nullstr=="1"){
				return "处理中";
			}else if(nullstr=="2"){
				return "成功";
			}else if(nullstr=="3"){
				return "失败";
			}else{
				return nullstr;
			}
		}
		