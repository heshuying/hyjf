var
// --------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateAction : "updateAction",
},
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		var flag = true;
		//转入比例之和
		var _sum = 0;
		$("#roleTbody tr").each(function(index,element){
			//“否”是0；“是”是1 
			var _val = $(this).find("select[name='autoTransferInto']").val();
			// 自动转出
			var autoTransferOut = $(this).find("select[name='autoTransferOut']").val();
			// 余额下限
			var balanceLowerLimit = $.trim($(this).find("input[name='balanceLowerLimit']").val());
			if(_val == "1" && (!$.trim($(this).find("input[name='balanceLowerLimit']").val())|| balanceLowerLimit == 0)){
				Page.coverLayer();
				Page.notice("请检查第"+(parseInt(index)+1)+"行，余额下限不能为空", "","error");
				flag = false;
				return false;
			}
			var transferIntoRatio = $.trim($(this).find("input[name='transferIntoRatio']").val());
			if(_val == "1" && ( !$.trim($(this).find("input[name='transferIntoRatio']").val()) || transferIntoRatio==0)){
				Page.coverLayer();
				Page.notice("请检查第"+(parseInt(index)+1)+"行，转入比例不能为空", "","error");
				flag = false;
				return false;
			}
			if(_val == "1" && autoTransferOut == "1"){
				Page.coverLayer();
				Page.notice("请检查第"+(parseInt(index)+1)+"行，自动转出和自动转入不能同时为是", "","error");
				flag = false;
				return false;
			}
			if(_val == "1"){
				_sum += parseInt($(this).find("input[name='transferIntoRatio']").val());
			}
			
			if(_sum>100){
				Page.coverLayer();
				Page.notice("请转入比例之和不能大于100", "","error");
				flag = false;
				return false;
			}
		});
		if (Page.validate.check(false)&&Page.form.find(".has-error").length == 0 && flag) {
			Page.confirm("", "确定要保存商户子账户余额信息吗？", function(isConfirm) {
				if (isConfirm) {
					$("#balanceDataJson").val(JSON.stringify(Page.getJsonByTableName($("#banlanceList")))),
					Page.submit(Action.updateAction);
				}
			})
		}
	},
	// 返回按钮单击事件绑定
	backClkAct : function() {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
//	changeClkAct:function(){
//		//“否”是0；“是”是1
//		var _val=$(this).val();
//	}

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Submit").click(Events.confirmClkAct);
		$(".fn-Back").click(Events.backClkAct);
		//自动转入改变事件绑定
//		$(".form-select2").change(Events.changeClkAct);
	},
	// 画面布局
	doLayout : function() {
		// 初始化下拉框
		$(".form-select2").select2({
			allowClear : true,
			language : "zh-CN"
		})
	},

	// 画面初始化
	initialize : function() {
		//CKEDITOR.disableAutoInline = true
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
	},
	// table2JSON
	getJsonByTableName: function(table, rs) {
		return rs = [],
			table && table[0] && (
					table.find("tbody").find("tr").each(function(row) {
						row = {},
						flag = false,
						$(this).find("input").each(function(cell, val) {
							cell = $("input", this),
							cell[0] && (
									val = row[cell.attr("name")] = cell.val());
						}),
						$(this).find("td").each(function(cell, val) {
							cell = $("input", this),
							cell[0] && (
									val = row[cell.attr("name")] = $.trim(cell.val()));
						}),
						$(this).find("td").each(function(cell, val) {
							cell = $("select", this),
							cell[0] && (
									val = row[cell.attr("name")] = cell.val());
						}),
						rs.push(row);
					})), rs;
	},
}),


Page.initialize();
