var
numTxts = ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五"],
imageCount = 0,
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 下载模板的Action
	downloadCarAction : "downloadCarAction",
	// 下载模板的Action
	downloadHouseAction : "downloadHouseAction",
	// 下载模板的Action
	downloadAuthenAction : "downloadAuthenAction"
},
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 提交按钮的单击动作事件
	submitClkAct : function(event) {
		
		if(Page.validate.check(false) && $(".has-error").length == 0) {
			
			var message = "确定要保存当前填写的内容吗？";

			var disableds=$(":disabled");
			// 增加disabled属性
			disableds.each(function (index,domEle){
				$(domEle).attr("disabled","disabled");
			});
			Page.confirm("", message, function(isConfirm) {
				isConfirm && (
					$("#imageJson1").val(JSON.stringify(Page.getJsonByTableImage($("#xmzxList1")))),
					$("#imageJson2").val(JSON.stringify(Page.getJsonByTableImage($("#xmzxList2")))),
					$("#imageJson3").val(JSON.stringify(Page.getJsonByTableImage($("#xmzxList3")))),
					Page.submit());
			});
			// 移除disabled属性
			disableds.each(function (index,domEle){
				$(domEle).removeAttr("disabled");
			});
		} else {
			Page.countValidErr();
		}
	},
	// 下一步按钮的单击动作事件
	nextClkAct: function(event) {
		event.preventDefault(),
		$('a[href="' + $(this).attr("href") + '"]').tab('show');
	},
	// 项目类型change事件
	selProjIdChgAct: function(borrowclass, opts) {
		// 更新还款方式
		borrowclass = $(this).find("option:selected").data("borrowclass").toLowerCase(),
		opts = $("#sel-hkfs")[0].options,
		opts.length = 0;
		$.each($("#sel-hkfs-backs")[0].options, function(idx) {
			$(this).data(borrowclass) && Array.prototype.push.call(opts, $(this).clone()[0]);
		}),
		$("#sel-hkfs").val("").select2(),
		$("#tenderAccountMin").val($(this).find("option:selected").data("min")),
		$("#tenderAccountMax").val($(this).find("option:selected").data("max"));
		$("#borrowIncreaseMoney").val($(this).find("option:selected").data("increasemoney"));
		var borrowInterestCoupon = $(this).find("option:selected").data("interestcoupon");
		var borrowTasteMoney = $(this).find("option:selected").data("tastemoney");
		if(borrowInterestCoupon=='1'){
			$("#borrowInterestCoupon").attr("checked",'true');
		}
		if(borrowTasteMoney=='1'){
			$("#borrowTasteMoney").attr("checked",'true');
		}
		if(borrowTasteMoney=='1'&&borrowInterestCoupon=='1'){
			$("#checkAllUse").attr("checked",'true');
		}
		var borrowCd = $("#projectType").find("option:selected").val();
		if(borrowCd=="11"){
			if($("input[name='verifyStatus']:checked").val() == "1"){
				$("#bookingTimeDiv").show();
			}else{
				$("#bookingTimeDiv").hide();
				$("#bookingBeginTime").val("");
				$("#bookingEndTime").val("");
			}
		}else{
			$("#bookingTimeDiv").hide();
			$("#bookingBeginTime").val("");
			$("#bookingEndTime").val("");
		}
	},
	// 下载按钮改变
	selRadioChgAct:function(){
		var _val = $("input[name='bottomButtonStatus']:checked").val();
		if("1" == $.trim(_val)){
			$("#address").hide();
			$("#downloadPath").attr("disabled",true);
			Page.validate.ignore(".changeInput");
		}else{
			$("#address").show();
			$("#downloadPath").attr("disabled",false);
			Page.validate.unignore(".changeInput");
		}
	},
	// 借款人用户名change事件
	usernameChgAct: function() {
		$("#txtJieKuanren").text($(this).val() || "--");
	},
	// pcc
	checkUtmIdChgAct : function() {
		$.post('checkUtmId', {'utmId':$("#utmId").val()}, function (text, status) {
			$("#utmId").val(text).blur();
		});
	},
	
	// pcc
	checkReferrerChgAct : function() {
		$.post('checkReferrer', {'referrerName':$("#referrerName").val()}, function (text, status) {
			$("#referrerName").val(text).blur();
		});
	},
	// 项目名称change事件
	txtNameChgAct: function() {
		// 更新拆标列表
		Page.updateCbList(true);
	},
	// 借款金额change事件
	txtAccoChgAct: function() {
		// 更新拆标列表
		Page.updateCbList(false, true);
	},
	// 还款方式change事件
	selHkfsChgAct: function() {
		$("#borrowPeriod").next().text(this.value == "endday" ? "天" : "月"),
		$("#borrowPeriod").change();
	},
	// 借款期限change事件
	txtPeriChgAct : function() {
		if($("#sel-hkfs").val() != "" && this.value) {
			$.post('getScale', { 
				borrowPeriod: $(this).val(), 
				borrowStyle: $("#sel-hkfs").val(),
				projectType: $("#projectType").val()
			}, function (text, status, result) { 
				result = JSON.parse(text);
				$("#borrowServiceScale").text(result.borrowServiceScale + " "),
				$("#borrowManagerScale").text(result.borrowManagerScale + " ");
			});
		} else {
			$("#borrowServiceScale").text("-- "),
			$("#borrowManagerScale").text("-- ");
		}
	},
	// 发标方式变更
	verifyradioClkAct : function() {
		Page.validate.ignore("#ontime");
		Page.validate.ignore("#bookingBeginTime");
		Page.validate.ignore("#bookingEndTime");
		if($("input[name='verifyStatus']:checked").val() == "1") {
			$("#ontimeDiv").show();
			var borrowCd = $("#projectType").find("option:selected").val();
			if(borrowCd=="11"){
				$("#bookingTimeDiv").show();
			}
			Page.validate.unignore("#ontime");
			Page.validate.ignore("#bookingBeginTime");
			Page.validate.ignore("#bookingEndTime");
// Page.validate.unignore("#bookingBeginTime");
// Page.validate.unignore("#bookingEndTime");
		} else {
			$("#ontimeDiv").hide();
			$("#bookingTimeDiv").hide();
			$("#ontime").val("");
			Page.validate.ignore("#ontime");
			Page.validate.ignore("#bookingBeginTime");
			Page.validate.ignore("#bookingEndTime");
		}
	},
	// 发标时间改变事件
	onTimeChgAct : function(){
		if($("#ontime").val()<=$("#bookingEndTime").val()&&$("#ontime").val()>=$("#bookingBeginTime").val()){
			$("#bookingEndTime").val($("#ontime").val());
		}
		if($("#ontime").val()<=$("#bookingEndTime").val()&&$("#ontime").val()<=$("#bookingBeginTime").val()){
			$("#bookingBeginTime").val("");
			$("#bookingEndTime").val("");
		}
		if($("#bookingBeginTime").val()!=undefined&&($("#bookingBeginTime").val() != ""||$("#bookingEndTime").val() != "")){
			$("#projectType").attr("disabled","disabled");
		}
		else{
			$("#projectType").removeAttr("disabled");
		}
	},
	// 预约开始时间改变事件
	bookingBeginTimeChgAct : function(){
		Page.validate.ignore("#bookingBeginTime");
		Page.validate.ignore("#bookingEndTime");
		if ($("#bookingBeginTime").val() != "") {
			Page.validate.unignore("#bookingBeginTime");
			Page.validate.unignore("#bookingEndTime");
		} else {
			if ($("#bookingEndTime").val() != "") {
				Page.validate.unignore("#bookingBeginTime");
				Page.validate.unignore("#bookingEndTime");
			}
		}
		if($("#bookingBeginTime").val()!=undefined&&($("#bookingBeginTime").val() != ""||$("#bookingEndTime").val() != "")){
			$("#projectType").attr("disabled","disabled");
		}
		else{
			$("#projectType").removeAttr("disabled");
		}
	},
	// 预约截止时间改变事件
	bookingEndTimeChgAct : function(){
		Page.validate.ignore("#bookingBeginTime");
		Page.validate.ignore("#bookingEndTime");
		if ($("#bookingBeginTime").val() != "") {
			Page.validate.unignore("#bookingBeginTime");
			Page.validate.unignore("#bookingEndTime");
		} else {
			if ($("#bookingEndTime").val() != "") {
				Page.validate.unignore("#bookingBeginTime");
				Page.validate.unignore("#bookingEndTime");
			}
		}
		if($("#bookingBeginTime").val()!=undefined&&($("#bookingBeginTime").val() != ""||$("#bookingEndTime").val() != "")){
			$("#projectType").attr("disabled","disabled");
		}
		else{
			$("#projectType").removeAttr("disabled");
		}
	},
	// 拆标单选框change事件
	radChaiChgAct: function(event) {
		if(this.value == "yes") {
			$("#chaiBiaoList a.btn, #chaiBiaoList input").prop("disabled", false).removeClass("disabled")
					.filter(":text").closest(".form-group").css("visibility", "visible"),
			$("#chaiBiaoList table").css("opacity", 1).addClass("table-hover"),
			Page.validate.unignore("#chaiBiaoList :text"),
			// 更新拆标列表
			Page.updateCbList(true, !!event.originalEvent);
		} else {
			$("#chaiBiaoList a.btn, #chaiBiaoList input").prop("disabled", true).addClass("disabled"),
			$("#chaiBiaoList table").css("opacity", .7).removeClass("table-hover")
					.find(".has-success,.has-error").removeClass("has-success has-error").end()
					.find("input").closest(".form-group").css("visibility", "hidden").val(""),
			Page.validate.ignore("#chaiBiaoList :text");
		}
	},
	// 拆标的添加行事件
	cbAddRowClkAct: function(row, len) {
		len = $("#chaiBiaoList tr").length;
		if(len >= 15) {
			Page.notice("拆标数最多为15条。", "添加拆标", "warning");
			return;
		}
		row = Page.cbRowTmp.clone();
		row.find("td:first").text($("#chaiBiaoList tr").length + 1),
		row.appendTo("#chaiBiaoList table"),
		$("#cbRowCounts").text(len + 1),
		// 更新拆标列表
		Page.updateCbList(true, true);
	},
	// 拆标的删除行事件
	cbRemoveRowClkAct: function(row) {
		$(this).closest("tr").remove(),
		$("#cbRowCounts").text($("#chaiBiaoList tr").length),
		// 更新拆标列表
		Page.updateCbList(true, true);
	},
	// 抵押物类型change事件
	dywlxChgAct: function() {
		$($(this).data("panel"))[this.checked ? "show" : "hide"]();
	},
	// 车辆抵押的添加行事件
	cldyxxAddRowClkAct: function(row, len) {
		len = $("#cldyxxList tr").length;
		if(len == 20) {
			Page.notice("车辆抵押最多为20条。", "车辆抵押信息", "warning");
			return;
		}
		len == 19 && $("#cldyxxList .fn-addRow").hide(),
		row = Page.cldyxxRowTmp.clone(),
		row.appendTo("#cldyxxList table")
			.find(":text[name=toprice]").change(Events.txtColTotalChgAct).end()
			.find(":text[name=buytime]").datepicker({
				autoclose: true,
				todayHighlight: true
			}),
		$("#cldyxxRowCounts").text(len + 1);
	},
	// 车辆抵押的删除行事件
	cldyxxRemoveRowClkAct: function(row) {
		$(this).closest("tr").find(":text[name=toprice]").off().end()
				.find(":text[name=buytime]").datepicker('remove').element.closest("tr").remove(),
		$("#cldyxxRowCounts").text($("#cldyxxList tr").length),
		$("#cldyxxList .fn-addRow").show(),
		// 更新总计
		Events.txtColTotalChgAct.call($("#cldyxxList tr:first :text[name=toprice]"));
	},
	// 房产抵押的添加行事件
	fcdyxxAddRowClkAct: function(row, len) {
		len = $("#fcdyxxList tr").length;
		if(len == 20) {
			Page.notice("房产抵押最多为20条。", "添加房产抵押", "warning");
			return;
		}
		len == 19 && $("#fcdyxxList .fn-addRow").hide(),
		row = Page.fcdyxxRowTmp.clone(),
		row.appendTo("#fcdyxxList table")
			.find(":text[name=housesToprice]").change(Events.txtColTotalChgAct).end()
			.find(":text[name=housesPrice]").change(Events.txtColTotalChgAct),
		$("#fcdyxxRowCounts").text(len + 1);
	},
	// 房产抵押的删除行事件
	fcdyxxRemoveRowClkAct: function(row) {
		$(this).closest("tr").find(":text[name=housesToprice],:text[name=housesPrice]").off().end().remove(),
		$("#fcdyxxRowCounts").text($("#fcdyxxList tr").length),
		$("#fcdyxxList .fn-addRow").show(),
		// 更新总计
		Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesToprice]"));
		Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesPrice]"));
	},
	// 列总计change事件
	txtColTotalChgAct: function(name, sum) {
		sum = 0,
		$(this).closest(".panel-body")
			.find("input[name=" + (name = $(this).attr("name")) + "]").each(function(val) {
				val = $(this).val(),
				/^\d+(\.\d+)?$/.test(val) && (sum += val - 0)
			}).end()
				.next().find("." + name).text(sum = $.fmtThousand(sum) || 0).attr("data-original-title", "￥ " + sum);
	},
	// 借款类型单选框change事件
	radJklxChgAct: function() {
		if(this.value == "1") {
			$("#grjkPanel").hide().prev().show();
			Page.validate.unignore("#credit");
			Page.validate.ignore("#userCredit");
		} else {
			$("#qyjkPanel").hide().next().show();
			Page.validate.unignore("#userCredit");
			Page.validate.ignore("#credit");
		}
	},
	// 认证信息的添加行事件
	rzxxAddRowClkAct: function(row, len) {
		len = $("#rzxxList tr").length;
		if(len == 20) {
			Page.notice("认证信息最多为20条。", "添加认证信息", "warning");
			return;
		}
		len == 19 && $("#rzxxList .fn-addRow").hide(),
		row = Page.rzxxRowTmp.clone(),
		row.appendTo("#rzxxList table").find(":text:last").datepicker({
			autoclose: true,
			todayHighlight: true
		}),
		$("#rzxxRowCounts").text(len + 1);
	},
	// 认证信息的删除行事件
	rzxxRemoveRowClkAct: function(row) {
		$(this).closest("tr").find(":text:last").datepicker('remove').element.closest("tr").remove(),
		$("#rzxxRowCounts").text($("#rzxxList tr").length),
		$("#rzxxList .fn-addRow").show();
	},
	// 可出借平台多选框change事件
	tzptCheckChgAct: function() {
		$(".tzptCheckbox").prop("checked", this.checked);
	},
	// 可用券
	kyqCheckChgAct: function() {
		$(".checkAllUsebox").prop("checked", this.checked);
	},
	
	// pcc start
	// 图片信息的添加行事件
	xmzxAddRowClkAct1: function(row, len) {
		len = $("#xmzxList1 tr").length;
// if(len == 20) {
// Page.notice("项目资料最多为20条。", "添加项目资料", "warning");
// return;
// }
// len == 19 && $("#xmzxList .fn-addRow").hide(),
		row = Page.xmzxRowTmp1.clone(),
		row.appendTo("#xmzxList1 table").find(".fileupload1").fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];
				
				var imageSort = $(this).parent().parent().parent().parent().parent().find("input[name=imageSort]");
				if(imageSort.val() == "") {
					imageSort.val($("#xmzxList1 input[name=imageName]").length - 1);
				}
				
				var imageName = $(this).parent().parent().parent().parent().parent().find("input[name=imageName]");
				if(imageName.val() == "") {
					imageName.val(file.imageName);
				}
				
				$(this).closest("td").next().find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>");
				$(this).next().val(file.imageRealName);
				$(this).next().next().val(file.imagePath);
			}
		})
		.find("input:file").removeAttr('disabled').end().end()
		.find('[data-toggle="popover"]').popover(),
		$("#xmzxRowCounts1").text(len + 1);
	},	
	// 图片信息的删除行事件
	xmzxRemoveRowClkAct1: function(row) {
		$(this).closest("tr").find(":text:last").datepicker('remove')
			.element.closest("tr").find('[data-toggle="popover"]').popover("destroy").end()
			.remove(),
		$("#xmzxRowCounts1").text($("#xmzxList1 tr").length),
		$("#xmzxList1 .fn-addRow").show();
	},
	// 图片信息的删除行事件
	removeRowTopClkAct : function() {
		var trObj = $(this).parent().parent();
		trObj.find("img").attr("src", "").parent().attr("data-content", "暂无预览...");
		trObj.find("input").val("");
		trObj.find("select[name='isSafe']").val("");
		trObj.find("select[name='housesType']").val("");	
		
		// 更新总计
		Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesToprice]"));
		Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesPrice]"));
		
		// 更新总计
		Events.txtColTotalChgAct.call($("#cldyxxList tr:first :text[name=toprice]"));
	},
	
	
	// 图片信息的添加行事件
	xmzxAddRowClkAct2: function(row, len) {
		len = $("#xmzxList2 tr").length;
// if(len == 20) {
// Page.notice("项目资料最多为20条。", "添加项目资料", "warning");
// return;
// }
// len == 19 && $("#xmzxList .fn-addRow").hide(),
		row = Page.xmzxRowTmp2.clone(),
		row.appendTo("#xmzxList2 table").find(".fileupload2").fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];
				
				var imageSort = $(this).parent().parent().parent().parent().parent().find("input[name=imageSort]");
				if(imageSort.val() == "") {
					imageSort.val($("#xmzxList2 input[name=imageName]").length - 1);
				}
				
				var imageName = $(this).parent().parent().parent().parent().parent().find("input[name=imageName]");
				if(imageName.val() == "") {
					imageName.val(file.imageName);
				}
				
				$(this).closest("td").next().find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>");
				$(this).next().val(file.imageRealName);
				$(this).next().next().val(file.imagePath);
			}
		})
		.find("input:file").removeAttr('disabled').end().end()
		.find('[data-toggle="popover"]').popover(),
		$("#xmzxRowCounts2").text(len + 1);
	},	
	// 图片信息的删除行事件
	xmzxRemoveRowClkAct2: function(row) {
		$(this).closest("tr").find(":text:last").datepicker('remove')
			.element.closest("tr").find('[data-toggle="popover"]').popover("destroy").end()
			.remove(),
		$("#xmzxRowCounts2").text($("#xmzxList2 tr").length),
		$("#xmzxList2 .fn-addRow").show();
	},
	// 图片信息的删除行事件
	removeRowTopClkAct : function() {
		var trObj = $(this).parent().parent();
		trObj.find("img").attr("src", "").parent().attr("data-content", "暂无预览...");
		trObj.find("input").val("");
		trObj.find("select[name='isSafe']").val("");
		trObj.find("select[name='housesType']").val("");	
		
		// 更新总计
		Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesToprice]"));
		Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesPrice]"));
		
		// 更新总计
		Events.txtColTotalChgAct.call($("#cldyxxList tr:first :text[name=toprice]"));
	},
	
	
	
	// 图片信息的添加行事件
	xmzxAddRowClkAct3: function(row, len) {
		len = $("#xmzxList3 tr").length;
// if(len == 20) {
// Page.notice("项目资料最多为20条。", "添加项目资料", "warning");
// return;
// }
// len == 19 && $("#xmzxList .fn-addRow").hide(),
		row = Page.xmzxRowTmp3.clone(),
		row.appendTo("#xmzxList3 table").find(".fileupload3").fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];
				
				var imageSort = $(this).parent().parent().parent().parent().parent().find("input[name=imageSort]");
				if(imageSort.val() == "") {
					imageSort.val($("#xmzxList3 input[name=imageName]").length - 1);
				}
				
				var imageName = $(this).parent().parent().parent().parent().parent().find("input[name=imageName]");
				if(imageName.val() == "") {
					imageName.val(file.imageName);
				}
				
				$(this).closest("td").next().find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>");
				$(this).next().val(file.imageRealName);
				$(this).next().next().val(file.imagePath);
			}
		})
		.find("input:file").removeAttr('disabled').end().end()
		.find('[data-toggle="popover"]').popover(),
		$("#xmzxRowCounts3").text(len + 1);
	},	
	// 图片信息的删除行事件
	xmzxRemoveRowClkAct3: function(row) {
		$(this).closest("tr").find(":text:last").datepicker('remove')
			.element.closest("tr").find('[data-toggle="popover"]').popover("destroy").end()
			.remove(),
		$("#xmzxRowCounts3").text($("#xmzxList3 tr").length),
		$("#xmzxList3 .fn-addRow").show();
	},
	// 图片信息的删除行事件
	removeRowTopClkAct : function() {
		var trObj = $(this).parent().parent();
		trObj.find("img").attr("src", "").parent().attr("data-content", "暂无预览...");
		trObj.find("input").val("");
		trObj.find("select[name='isSafe']").val("");
		trObj.find("select[name='housesType']").val("");	
		
		// 更新总计
		Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesToprice]"));
		Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesPrice]"));
		
		// 更新总计
		Events.txtColTotalChgAct.call($("#cldyxxList tr:first :text[name=toprice]"));
	},
	
	// pcc end
	
	downloadCarClkAct : function() {
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#downloadForm").attr("action", Action.downloadCarAction);
		$("#downloadForm").submit();
	},
	downloadHouseClkAct : function() {
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#downloadForm").attr("action", Action.downloadHouseAction);
		$("#downloadForm").submit();
	},
	downloadAuthenClkAct : function() {
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#downloadForm").attr("action", Action.downloadAuthenAction);
		$("#downloadForm").submit();
	},
	borrowMeasuresInstitChgAct:function(){
		var summary=$("#borrowMeasuresInstit option:selected").attr("data-summary");
		var operatingprocess=$("#borrowMeasuresInstit option:selected").attr("data-operatingprocess");
		var measures=$("#borrowMeasuresInstit option:selected").attr("data-measures");
		$("#borrowCompanyInstruction").val(summary);
		$("#borrowOperatingProcess").val(operatingprocess);
		$("#borrowMeasuresMea").val(measures);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 统计画面验证错误数
	countValidErr: function() {
		$("#mainTabs a").find(".badge").remove().end()
			.each(function(errCount) {
				errCount = $(".Validform_wrong", $(this).attr("href")).length,
				errCount && $("<span class='badge badge-danger margin-left-5'>" + errCount + "</span>").appendTo(this);
			});
	},
	// 更新拆标列表
	updateCbList: function(updCaption, updMoney) {
		if($(".clip-radio :radio:checked").val() != "yes") {
			return;
		}
		var	caption = $("#jkName").val(), money = $("#account").val(),
			rows = $("#chaiBiaoList tr");
		// 标题
		caption && (caption += "第{idx}期【共{total}期】"),
		// 金额
		money && (money = money % rows.length == 0 ? money / rows.length : ""),
		// 遍历更新
		rows.each(function(idx, ipts) {
			$("td:first", this).text(idx + 1),
			ipts = $(this).find("input"),
			updCaption && caption && ipts.eq(0).val(caption.replace(/\{(\w+)\}/g, function(a, b) {
				return numTxts[(b == "idx" ? idx : rows.length - 1)];
			})),
			updMoney && ipts.eq(1).val(money);
		});
	},
	// table2JSON
	getJsonByTable: function(table, rs) {
		return rs = [],
			table && table[0] && (
					table.find("tr").each(function(row, flag) {
						row = {},
						flag = false,
						$(this).find("td").each(function(cell, val) {
							cell = $("input, select", this),
							cell[0] && (
									val = row[cell.attr("name")] = cell.val(),
									val && !cell.data("ignore") && (flag = true));
						}),
						flag && rs.push(row);
					})), rs;
	},
	// table2JSON
	getJsonByTableName: function(table, rs) {
		return rs = [],
			table && table[0] && (
					table.find("tr").each(function(row) {
						row = {},
						flag = false,
						$(this).find("td").each(function(cell, val) {
							cell = $("input", this),
							cell[0] && (
									val = row[cell.attr("name")] = cell.val());
						}),
						rs.push(row);
					})), rs;
	},
	getJsonByTableImage: function(table, rs) {
		return rs = [],
		table && table[0] && (
				table.find("tr").each(function(row, flag) {
					row = {},
					flag = false,
					$(this).find("td").each(function(cell, val) {
						$("input", this).each(function() {
							val = row[$(this).attr("name")] = $(this).val(),
							val && (flag = true);
						})
					}),
					flag && rs.push(row);
				})), rs;
	},
	// 画面布局
	doLayout: function() {
		// 拆标行模板
		Page.cbRowTmp = $("#rowTemplts tr:eq(0)"),
		// 车辆抵押行模板
		Page.cldyxxRowTmp = $("#rowTemplts tr:eq(1)"),
		// 房产抵押行模板
		Page.fcdyxxRowTmp = $("#rowTemplts tr:eq(2)"),
		// 认证信息行模板
		Page.rzxxRowTmp = $("#rowTemplts tr:eq(3)"),
		
		// pcc start
		// 图片信息行模板
		Page.xmzxRowTmp1 = $("#rowTemplts tr:eq(0)"),
		
		// 图片信息行模板
		Page.xmzxRowTmp2 = $("#rowTemplts tr:eq(1)"),
		
		// 图片信息行模板
		Page.xmzxRowTmp3 = $("#rowTemplts tr:eq(2)"),
		// pcc end
		// 删除行模板原型
		$("#rowTemplts").remove(),
		// 初始化省市区连动
		$(".region-select").regionSelect(),
		// 初始化下拉框
		$(".form-select2").select2({
			allowClear: true,
			language: "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
		}),
		// 初始化富文本编辑域
		$('textarea.tinymce').tinymce({
			// Location of TinyMCE script
			script_url: themeRoot + '/vendor/plug-in/tinymce/tinymce.min.js',
			language_url : themeRoot + '/vendor/plug-in/tinymce/langs/zh_CN.js',
			
			height: 260,
			
			// theme: "modern",
			plugins: [
				"advlist autolink link image lioniteimages lists charmap print preview hr anchor pagebreak spellchecker",
				"searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
				"save table contextmenu directionality emoticons template paste textcolor colorpicker textpattern"
			],
			external_plugins: {
				// "moxiemanager": "/moxiemanager-php/plugin.js"
			},
			
			// Example content CSS (should be your site CSS)
			// content_css: "css/development.css",
			add_unload_trigger: false,
	
			toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image lioniteimages | print preview media fullpage | forecolor backcolor emoticons table",
	
			image_advtab: true,
	
			template_replace_values : {
				username : "Jack Black"
			},
	
			template_preview_replace_values : {
				username : "Preview user name"
			},
	
			link_class_list: [
				{title: 'Example 1', value: 'example1'},
				{title: 'Example 2', value: 'example2'}
			],
	
			image_class_list: [
				{title: 'Example 1', value: 'example1'},
				{title: 'Example 2', value: 'example2'}
			],
	
			templates: [
				{title: 'Some title 1', description: 'Some desc 1', content: '<strong class="red">My content: {$username}</strong>'},
				{title: 'Some title 2', description: 'Some desc 2', url: 'development.html'}
			],
	
			setup: function(ed) {
				
			},
	
			spellchecker_callback: function(method, data, success) {
				if (method == "spellcheck") {
					var words = data.match(this.getWordCharPattern());
					var suggestions = {};
	
					for (var i = 0; i < words.length; i++) {
						suggestions[words[i]] = ["First", "second"];
					}
					success({words: suggestions, dictionary: true});
				}
	
				if (method == "addToDictionary") {
					success();
				}
			}
		});
		
		var borrowclass = $("#projectType").find("option:selected").data("borrowclass");
		if(borrowclass) {
			var defaultVal = $("#sel-hkfs").find("option:selected").val();
			borrowclass = borrowclass.toLowerCase(),
			opts = $("#sel-hkfs")[0].options,
			opts.length = 0;
			$.each($("#sel-hkfs-backs")[0].options, function(idx) {
				$(this).data(borrowclass) && Array.prototype.push.call(opts, $(this).clone()[0]);
			}),
			$("#sel-hkfs").val(defaultVal).select2();
		}
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 发标时间change事件
		$("#ontime").blur(Events.onTimeChgAct),
		// 预约开始时间change事件
		$("#bookingBeginTime").blur(Events.bookingBeginTimeChgAct),
		// 预约截止时间change事件
		$("#bookingEndTime").blur(Events.bookingEndTimeChgAct),
		// 项目类型change事件
		$("#projectType").change(Events.selProjIdChgAct),
		// 下载按钮的chang事件
		$("input[name='bottomButtonStatus']").change(Events.selRadioChgAct),
		// 借款人用户名change事件
		$("#username").change(Events.usernameChgAct),
		// pcc 检查渠道
		$("#checkUtmId").click(Events.checkUtmIdChgAct),
		// pcc 检查推荐人
		$("#checkReferrer").click(Events.checkReferrerChgAct),
		
		// 项目名称change事件
		$("#jkName").change(Events.txtNameChgAct),
		// 借款金额change事件
		$("#account").change(Events.txtAccoChgAct),
		// 还款方式change事件
		$("#sel-hkfs").change(Events.selHkfsChgAct),
		// 借款期限change事件
		$("#borrowPeriod").change(Events.txtPeriChgAct),
		// 发标方式变更
		$(".verifyradio").change(Events.verifyradioClkAct).filter(":checked").change(),
		// 拆标单选框change事件
		$(".shifuchaibiao :radio").change(Events.radChaiChgAct).filter(":checked").change(),
		// 拆标行的添加、删除事件
		$("#chaiBiaoList").on("click", ".fn-addRow", Events.cbAddRowClkAct)
				.on("click", ".fn-removeRow", Events.cbRemoveRowClkAct),
		// 抵押物类型change事件
		$("#dywlx :checkbox").on("change", Events.dywlxChgAct),
		// 车辆抵押行的添加、删除事件
		$("#cldyxxList").on("click", ".fn-addRow", Events.cldyxxAddRowClkAct)
				.on("click", ".fn-removeRow", Events.cldyxxRemoveRowClkAct),
		// 车辆评估价change事件
		$("#cldyxxList input[name=toprice]").change(Events.txtColTotalChgAct).change(),
		// 房产抵押行的添加、删除事件
		$("#fcdyxxList").on("click", ".fn-addRow", Events.fcdyxxAddRowClkAct)
				.on("click", ".fn-removeRow", Events.fcdyxxRemoveRowClkAct),
		// 房产评估价change事件
		$("#fcdyxxList input[name=housesToprice]").change(Events.txtColTotalChgAct).change(),
		$("#fcdyxxList input[name=housesPrice]").change(Events.txtColTotalChgAct).change(),
		// 借款类型单选框change事件
		$(".jklx :radio").change(Events.radJklxChgAct).filter(":checked").change(),
		// 认证行的添加、删除事件
		$("#rzxxList").on("click", ".fn-addRow", Events.rzxxAddRowClkAct)
				.on("click", ".fn-removeRow", Events.rzxxRemoveRowClkAct),
		// 项目资料的添加、删除事件
		$("#xmzxList1").on("click", ".fn-addRow", Events.xmzxAddRowClkAct1)
				.on("click", ".fn-removeRow", Events.xmzxRemoveRowClkAct1),
		// 项目资料的添加、删除事件
		$("#xmzxList2").on("click", ".fn-addRow", Events.xmzxAddRowClkAct2)
				.on("click", ".fn-removeRow", Events.xmzxRemoveRowClkAct2),
		// 项目资料的添加、删除事件
		$("#xmzxList3").on("click", ".fn-addRow", Events.xmzxAddRowClkAct3)
				.on("click", ".fn-removeRow", Events.xmzxRemoveRowClkAct3),
		// 可出借平台多选框change事件
		$("#tzptCheckAll").change(Events.tzptCheckChgAct),
		// 可用券
		$("#checkAllUse").change(Events.kyqCheckChgAct),
		// 确定按钮的单击事件绑定
		$(".fn-Submit").click(Events.submitClkAct),
		// 下一步按钮的单击事件绑定
		$(".fn-Next").click(Events.nextClkAct),
		// 删除图片第一行的数据
		$(".fn-removeRowTop").click(Events.removeRowTopClkAct),
		// 合作机构change事件
		$("#borrowMeasuresInstit").change(Events.borrowMeasuresInstitChgAct),
		
		// pcc start
		// 图片上传
		$('.fileupload1').fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];
				var imageSort = $(this).parent().parent().parent().parent().parent().find("input[name=imageSort]");
				if(imageSort.val() == "") {
					imageSort.val($("#xmzxList1 input[name=imageName]").length - 1);
				}
				
				var imageName = $(this).parent().parent().parent().parent().parent().find("input[name=imageName]");
				if(imageName.val() == "") {
					imageName.val(file.imageName);
				}
				$(this).closest("td").next().find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>"),
				$(this).next().val(file.imageRealName),
				$(this).next().next().val(file.imagePath);
			}
		}).find("input:file").removeAttr('disabled');
		// 图片上传
		$('.morefileupload').click(function() {
			imageCount = 0;
		}).fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				Page.coverLayer();
				var file = data.result[0], el;
				if(imageCount == 0) {
					$("tr:not(:first)", "#xmzxList1").remove();
					el = $("#xmzxList1").find("tr");
					imageCount = 1;
				} else {
					// 打开模板
					Events.xmzxAddRowClkAct();
					el = $("#xmzxList1").find("tr:last");
				}
				
				el.find("input[name=imageSort]").val($("#xmzxList1 tr").length - 1);
				el.find("input[name=imageName]").val(file.imageName);
				el.find("input[name=imageRealName]").val(file.imageRealName);
				el.find("input[name=imagePath]").val(file.imagePath);
				el.find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>");	
			}
		}).find("input:file").removeAttr('disabled');
		
		
		// 图片上传
		$('.fileupload2').fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];
				var imageSort = $(this).parent().parent().parent().parent().parent().find("input[name=imageSort]");
				if(imageSort.val() == "") {
					imageSort.val($("#xmzxList2 input[name=imageName]").length - 1);
				}
				
				var imageName = $(this).parent().parent().parent().parent().parent().find("input[name=imageName]");
				if(imageName.val() == "") {
					imageName.val(file.imageName);
				}
				$(this).closest("td").next().find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>"),
				$(this).next().val(file.imageRealName),
				$(this).next().next().val(file.imagePath);
			}
		}).find("input:file").removeAttr('disabled');
		// 图片上传
		$('.morefileupload').click(function() {
			imageCount = 0;
		}).fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				Page.coverLayer();
				var file = data.result[0], el;
				if(imageCount == 0) {
					$("tr:not(:first)", "#xmzxList2").remove();
					el = $("#xmzxList2").find("tr");
					imageCount = 1;
				} else {
					// 打开模板
					Events.xmzxAddRowClkAct();
					el = $("#xmzxList2").find("tr:last");
				}
				
				el.find("input[name=imageSort]").val($("#xmzxList2 tr").length - 1);
				el.find("input[name=imageName]").val(file.imageName);
				el.find("input[name=imageRealName]").val(file.imageRealName);
				el.find("input[name=imagePath]").val(file.imagePath);
				el.find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>");	
			}
		}).find("input:file").removeAttr('disabled');
		
		// 图片上传
		$('.fileupload3').fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];
				var imageSort = $(this).parent().parent().parent().parent().parent().find("input[name=imageSort]");
				if(imageSort.val() == "") {
					imageSort.val($("#xmzxList2 input[name=imageName]").length - 1);
				}
				
				var imageName = $(this).parent().parent().parent().parent().parent().find("input[name=imageName]");
				if(imageName.val() == "") {
					imageName.val(file.imageName);
				}
				$(this).closest("td").next().find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>"),
				$(this).next().val(file.imageRealName),
				$(this).next().next().val(file.imagePath);
			}
		}).find("input:file").removeAttr('disabled');
		// 图片上传
		$('.morefileupload').click(function() {
			imageCount = 0;
		}).fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				Page.coverLayer();
				var file = data.result[0], el;
				if(imageCount == 0) {
					$("tr:not(:first)", "#xmzxList2").remove();
					el = $("#xmzxList3").find("tr");
					imageCount = 1;
				} else {
					// 打开模板
					Events.xmzxAddRowClkAct();
					el = $("#xmzxList3").find("tr:last");
				}
				
				el.find("input[name=imageSort]").val($("#xmzxList2 tr").length - 1);
				el.find("input[name=imageName]").val(file.imageName);
				el.find("input[name=imageRealName]").val(file.imageRealName);
				el.find("input[name=imagePath]").val(file.imagePath);
				el.find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>");	
			}
		}).find("input:file").removeAttr('disabled');
		
		// pcc end
		// 选择样式后隐藏
		$(document).on("change",'select#style',function(){
			var style = $(this).val();    
			if(style == 1){
				$("#tongyong").hide();
				$("#nextStep").hide();
				$(".imageConfig").hide();
				Page.validate.ignore("#topButton");
				Page.validate.ignore("#jumpPath");
				Page.validate.ignore("#downloadPath");
				Page.validate.ignore("#bottomButton");
			   }else{
				$("#tongyong").show();
				$("#nextStep").show();
				$(".imageConfig").show();
				Page.validate.unignore("#topButton");
				Page.validate.unignore("#jumpPath");
				Page.validate.unignore("#bottomButton");
				Page.validate.unignore("#downloadPath");
			   }
			});
		
		
		
		// 下载车辆模板
		$(".btn-DownloadCar").click(Events.downloadCarClkAct);
		// 车辆上传
		$('.btn-UploadCar').fileupload({
			url: "uploadCarAction",
			autoUpload: true,
			done: function (e, data) {
				Page.coverLayer();
				var datas = data.result, el;

				if(datas.length > 20) {
					Page.notice("车辆信息最多为20条。", "添加车辆信息", "warning");
					return;
				}
				for(var i = 0; i < datas.length; i++) {
					var file = datas[i];
					if(i == 0) {
						$("tr:not(:first)", "#cldyxxList").remove();
						el = $("#cldyxxList").find("tr");
					} else {
						Events.cldyxxAddRowClkAct();
						el = $("#cldyxxList").find("tr:last");
					}
					el.find("input[name=brand]").val(file.brand);
					el.find("input[name=model]").val(file.model);	
					el.find("input[name=carseries]").val(file.carseries);	
					el.find("input[name=color]").val(file.color);		
					el.find("input[name=year]").val(file.year);		
					el.find("input[name=place]").val(file.place);		
					el.find("input[name=buytime]").val(file.buytime);		
					el.find("input[name=price]").val(file.price);
					el.find("select[name=isSafe] option").each(function() {
						$(this).text() == file.isSafe && $(this).attr("selected","selected"); 
					});
					el.find("input[name=toprice]").val(file.toprice);	
				}

				// 更新总计
				Events.txtColTotalChgAct.call($("#cldyxxList tr:first :text[name=toprice]"));
			},
			start:function() {
				Page.coverLayer("正在上传文件请稍后");
			}
		}).find("input:file").removeAttr('disabled');
		
		
		// 下载房产模板
		$(".btn-DownloadHouse").click(Events.downloadHouseClkAct);
		
		// 房产上传
		$('.btn-UploadHouse').fileupload({
			url: "uploadHouseAction",
			autoUpload: true,
			done: function (e, data) {
				Page.coverLayer();
				var datas = data.result, el;

				if(datas.length > 20) {
					Page.notice("房产信息最多为20条。", "添加房产信息", "warning");
					return;
				}
				for(var i = 0; i < datas.length; i++) {
					var file = datas[i];
					if(i == 0) {
						$("tr:not(:first)", "#fcdyxxList").remove();
						el = $("#fcdyxxList").find("tr");
					} else {
						Events.fcdyxxAddRowClkAct();
						el = $("#fcdyxxList").find("tr:last");
					}
					el.find("select[name=housesType] option").each(function() {
						$(this).text() == file.housesType && $(this).attr("selected","selected"); 
					});
					el.find("input[name=housesLocation]").val(file.housesLocation);
					el.find("input[name=housesArea]").val(file.housesArea);	
					el.find("input[name=housesPrice]").val(file.housesPrice);	
					el.find("input[name=housesToprice]").val(file.housesToprice);	
				}

				// 更新总计
				Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesToprice]"));
				Events.txtColTotalChgAct.call($("#fcdyxxList tr:first :text[name=housesPrice]"));
			},
			start:function() {
				Page.coverLayer("正在上传文件请稍后");
			}
		}).find("input:file").removeAttr('disabled');
		
		
		// 下载认证模板
		$(".btn-DownloadAuthen").click(Events.downloadAuthenClkAct);
		// 认证模板上传
		$('.btn-UploadAuthen').fileupload({
			url: "uploadAuthenAction",
			autoUpload: true,
			done: function (e, data) {
				Page.coverLayer();
				var datas = data.result, el;

				if(datas.length > 20) {
					Page.notice("认证信息最多为20条。", "添加认证信息", "warning");
					return;
				}
				for(var i = 0; i < datas.length; i++) {
					var file = datas[i];
					if(i == 0) {
						$("tr:not(:first)", "#rzxxList").remove();
						el = $("#rzxxList").find("tr");
					} else {
						Events.rzxxAddRowClkAct();
						el = $("#rzxxList").find("tr:last");
					}
					el.find("input[name=authenSortKey]").val(file.authenSortKey);
					el.find("input[name=authenName]").val(file.authenName);
					el.find("input[name=authenTime]").val(file.authenTime);	
				}
			},
			start:function() {
				Page.coverLayer("正在上传文件请稍后");
			}
		}).find("input:file").removeAttr('disabled');
		
	},
	ready : function() {
		// 手动触发画面事件的默认连动
		$("input[name=verifyStatus]:checked").change(),
		// 渲染验证错误消息
		$(".Validform_wrong").closest(".form-group").addClass("has-error"),
		// 统计画面错误数
		this.countValidErr();
	},
	// 画面初始化
	initialize : function() {
		// CKEDITOR.disableAutoInline = true
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
		Events.onTimeChgAct();
		Events.verifyradioClkAct();
		Events.bookingBeginTimeChgAct();
	}
}),

Page.initialize();