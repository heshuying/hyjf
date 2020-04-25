var
numTxts = ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九", "三十十", "三十一", "三十二", "三十三", "三十四", "三十五"],
imageCount = 0,
/* 对应JAVA的Controllor的 @RequestMapping */
Action = {
	// 下载模板的Action
	downloadCarAction : "downloadCarAction",
	// 下载模板的Action
	downloadHouseAction : "downloadHouseAction",
	// 下载模板的Action
	downloadAuthenAction : "downloadAuthenAction",
	// 下载模板的Action
	downloadContentFillAction : "downloadContentFillAction",
	// 联动下拉事件
	getProductTypeAction : "getProductTypeAction",
    // 信用评级下拉事件
    getBorrowLevelAction:"getBorrowLevelAction"
},
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 提交按钮的单击动作事件
	submitClkAct : function(event) {

		$("#cldyxxList").find("select[name=isSafe]").each(function(idx, obj) {
			$(this).val() != "" && $(this).parent().removeClass("has-error")&& $(this).next().removeClass("Validform_wrong").html('');
		});

		$("#fcdyxxList").find("select[name=housesType]").each(function(idx, obj) {
			$(this).val() != "" && $(this).parent().removeClass("has-error")&& $(this).next().removeClass("Validform_wrong").html('');
		});

		if(Page.validate.check(false) && $(".has-error").length == 0) {

			var message = "确定要保存当前填写的内容吗？";
			var _investStart=parseFloat($("#tenderAccountMin").val());
			var _increaseMoney=parseFloat($("#borrowIncreaseMoney").val());
			if(_investStart<_increaseMoney){
				Page.alert("递增金额不能大于最低投标金额！");
				return false;
			}

			/*if("BORROW_FIRST" == $("#moveFlag").val() && !$("#verifyStatus3").prop("checked")) {
				var borrowNid = $("#borrowNid").val();

				var borrowNidIdx = borrowNid.substring(borrowNid.length - 2, borrowNid.length) * 1;

				if(borrowNidIdx != 1) {
					message = "发标的标的不是第一期，确定要发标吗？";
				}
			}*/
			var disableds=$(":disabled");
			// 增加disabled属性
			disableds.each(function (index,domEle){
				$(domEle).attr("disabled","disabled");
			});
			Page.confirm("", message, function(isConfirm) {
				isConfirm && (
					$.each({
						"#borrowCarJson": "#cldyxxList",
						"#borrowHousesJson": "#fcdyxxList",
						"#borrowAuthenJson": "#rzxxList"
					}, function(key, val) {
						$(key).val(JSON.stringify(Page.getJsonByTable($(val))))
					}),
					$("#borrowNameJson").val(JSON.stringify(Page.getJsonByTableName($("#chaiBiaoList")))),
					$("#borrowImageJson").val(JSON.stringify(Page.getJsonByTableImage($("#xmzxList")))),
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
		var borrowInterestCoupon = "1"
		var borrowTasteMoney = "1";
		if(borrowInterestCoupon=='1'){
			$("#borrowInterestCoupon").attr("checked",'true');
		}
		if(borrowTasteMoney=='1'){
			$("#borrowTasteMoney").attr("checked",'true');
		}
		if(borrowTasteMoney=='1'&&borrowInterestCoupon=='1'){
			$("#checkAllUse").attr("checked",'true');
		}
		// 是否加息flag
		var increaseInterestFlag =$(this).find("option:selected").data("increaseinterestflag");
		if(increaseInterestFlag == '1'){
            $("#borrowExtraYield").removeAttr("disabled");
		}else{
            $("#borrowExtraYield").attr("disabled","disabled");
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
		if(borrowCd == "15" || borrowCd == "16" || borrowCd == "19"){
			$.post('getXJDBorrowPreNid', {}, function (text, status) {
				$("#borrowPreNid").val(text).blur();
			});
		}else{
			$.post('getBorrowPreNid', {}, function (text, status) {
				$("#borrowPreNid").val(text).blur();
			});
		};
		// 移除未打上标签报错
		$("input[name=isEngineUsed]").parents(".form-group").removeClass("has-error")
		.find(".Validform_checktip.Validform_wrong").remove()
	},
	// 资产来源change事件
	instCodeOnchangeAct:function(){
		var instCode = $("#instCode").val();
		$("#productTypeList").empty();
		$("#productTypeList").change()
		if (instCode == "") {
			return;
		}
		$.ajax({
			url : Action.getProductTypeAction,
			type : "POST",
			async : true,
			dataType : "json",
			data :  {
				instCode : instCode
			},
			success : function(data) {
				$("#productTypeList").select2({
					data: data,
					placeholder : "全部",
					allowClear : true,
					language : "zh-CN"
				});
			},
		});
	},

    borrowLevelOnChangeAct:function () {
		var borrowLevel =  $("#borrowLevel").val();
        $.ajax({
            url : Action.getBorrowLevelAction+"?borrowLevel="+ encodeURIComponent(borrowLevel),
            type : "POST",
            async : true,
            contentType : "application/json",
            data :  {
                borrowLevel : borrowLevel
            },
           success: function(data) {
            	// 运营信息建议出借者类型更改
            	$("#investLevel").val(data);
            	if(data != "进取型"  ){
              	    $("#investLevelLabel").text(data+ "及以上");
                }else{
                    $("#investLevelLabel").text(data);
                }
            },
        });
    },
	// 借款人用户名change事件
	usernameChgAct: function() {
		$("#txtJieKuanren").text($(this).val() || "--");
	},
	// 获取借款预编号
	getBorrowPreNidClkAct : function() {
		var borrowCd = $("#projectType").find("option:selected").val();
		if(borrowCd == "15" || borrowCd == "16" || borrowCd == "19"){
			$.post('getXJDBorrowPreNid', {}, function (text, status) {
				$("#borrowPreNid").val(text).blur();
			});
		}else{
			$.post('getBorrowPreNid', {}, function (text, status) {
				$("#borrowPreNid").val(text).blur();
			});
		}
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
		// 移除未打上标签报错
		$("input[name=isEngineUsed]").parents(".form-group").removeClass("has-error")
		.find(".Validform_checktip.Validform_wrong").remove()
	},
	// 还款方式change事件
	selHkfsChgAct: function() {
		$("#borrowPeriod").next().text(this.value == "endday" ? "天" : "月"),
		$("#borrowPeriod").change();
		// 移除未打上标签报错
		$("input[name=isEngineUsed]").parents(".form-group").removeClass("has-error")
		.find(".Validform_checktip.Validform_wrong").remove()
	},
	// 借款期限change事件
	txtPeriChgAct : function() {
		if($("#sel-hkfs").val() != "" && this.value) {
			$.post('getScale', {
				borrowPeriod: $(this).val(),
				borrowStyle: $("#sel-hkfs").val(),
				projectType: $("#projectType").val(),
                instCode: $("#instCode").val()
			}, function (text, status, result) {
				result = JSON.parse(text);

				$("#borrowManagerScale").text(result.borrowManagerScale + " ");
			});
		} else {

			$("#borrowManagerScale").text("-- ");
		}
		// 移除未打上标签报错
		$("input[name=isEngineUsed]").parents(".form-group").removeClass("has-error")
		.find(".Validform_checktip.Validform_wrong").remove()
	},
	// 发标方式变更
	verifyradioClkAct : function() {
		Page.validate.ignore("#ontime");
		if($("input[name='verifyStatus']:checked").val() == "3") {
			$("#ontimeDiv").show();
			Page.validate.unignore("#ontime");
		} else {
			$("#ontimeDiv").hide();
			$("#ontime").val("");
			Page.validate.ignore("#ontime");
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
		if(len >= 35) {
			Page.notice("预约数最多为35条。", "添加预约", "warning");
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
		$(this).closest("tr").remove(),
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
		$(this).closest("tr").remove(),
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
	// 图片信息的添加行事件
	xmzxAddRowClkAct: function(row, len) {
		len = $("#xmzxList tr").length;
// if(len == 20) {
// Page.notice("项目资料最多为20条。", "添加项目资料", "warning");
// return;
// }
// len == 19 && $("#xmzxList .fn-addRow").hide(),
		row = Page.xmzxRowTmp.clone(),
		row.appendTo("#xmzxList table").find(".fileupload").fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];

				var imageSort = $(this).parent().parent().parent().parent().parent().find("input[name=imageSort]");
				if(imageSort.val() == "") {
					imageSort.val($("#xmzxList input[name=imageName]").length - 1);
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
		$("#xmzxRowCounts").text(len + 1);
	},
	// 图片信息的删除行事件
	xmzxRemoveRowClkAct: function(row) {
		$(this).closest("tr").remove(),
		$("#xmzxRowCounts").text($("#xmzxList tr").length),
		$("#xmzxList .fn-addRow").show();
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

	downloadContentFillClkAct : function() {
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#downloadForm").attr("action", Action.downloadContentFillAction);
		$("#downloadForm").submit();
	},


	borrowMeasuresInstitChgAct:function(){
		var summary=$("#borrowMeasuresInstit option:selected").attr("data-summary");
		var operatingprocess=$("#borrowMeasuresInstit option:selected").attr("data-operatingprocess");
		var measures=$("#borrowMeasuresInstit option:selected").attr("data-measures");
		if(measures==""){
			measures="1、汇盈金服已对该项目进行了严格的审核，最大程度的确保借款方信息的真实性，但不保证审核信息完全无误。<br>"+
					 "2、汇盈金服仅为信息发布平台，不对出借人提供担保或承诺保本保息，出借人应根据自身的出借偏好和风险承受能力进行独立判断和作出决策。市场有风险，出借需谨慎。<br>"
		}

		$("#borrowCompanyInstruction").val(summary);
		$("#borrowOperatingProcess").val(operatingprocess);
		$("#borrowMeasuresMea").val(measures);
	},
	entrustedFlgClkAct: function(){
		//是否受托支付
		var radio = $(this),
		entrustedUser = $("#entrustedUsername"),
		res = true;
		if(radio.val() == '0'){
			entrustedUser.val('');
			entrustedUser.parent('.form-group').removeClass('has-success');
			entrustedUser.prop('disabled',true);
			entrustedUser.attr('ignore','ignore');
			entrustedUser.prev().children('.symbol').removeClass('required');
			Page.validate.ignore("#entrustedUsername");
		}else{
			entrustedUser.prev().children('.symbol').addClass('required');
			entrustedUser.prop('disabled',false);
			entrustedUser.removeAttr('ignore');
			Page.validate.unignore("#entrustedUsername");
		}
		//手动检测是否
		res = Page.validate.check(false,"#entrustedUsername");
		if(res){
			entrustedUser.parent().removeClass("has-error") && entrustedUser.next().addClass('Validform_right');
		}else{
			entrustedUser.addClass("Validform_error").parent().addClass("has-error") && entrustedUser.next('.Validform_wrong').removeClass("Validform_right");
		}

	},
	clearEngineUsed : function(){
		// 是否使用引擎
		$("input[name=isEngineUsed]").change(function(){
			$(this).parents(".form-group").removeClass("has-error")
				.find(".Validform_checktip.Validform_wrong").remove()
		});
		$("#borrowApr").change(function(){
			$("input[name=isEngineUsed]").parents(".form-group").removeClass("has-error")
			.find(".Validform_checktip.Validform_wrong").remove()
		})
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
		//获取是否拆标
		if($(".shifuchaibiao :radio:checked").val() != "yes") {
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
			money != "" && ipts.eq(1).blur(); //有金额时候主动触发校验
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
		// 图片信息行模板
		Page.xmzxRowTmp = $("#rowTemplts tr:eq(4)"),
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
		}).on('changeDate', function(e) {
			$(e.currentTarget).trigger('blur');
            // `e` here contains the extra attributes
        });
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
		// 信用评级change事件
		$("#borrowLevel").change(Events.borrowLevelOnChangeAct),
		// 发标时间change事件
		$("#ontime").blur(Events.onTimeChgAct),
		// 预约开始时间change事件
		$("#bookingBeginTime").blur(Events.bookingBeginTimeChgAct),
		// 预约截止时间change事件
		$("#bookingEndTime").blur(Events.bookingEndTimeChgAct),
		// 项目类型change事件
		$("#projectType").change(Events.selProjIdChgAct),
		// 借款人用户名change事件
		$("#username").change(Events.usernameChgAct),
		// 获取借款预编号
		$("#getBorrowPreNid").click(Events.getBorrowPreNidClkAct),
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
		$("#xmzxList").on("click", ".fn-addRow", Events.xmzxAddRowClkAct)
				.on("click", ".fn-removeRow", Events.xmzxRemoveRowClkAct),
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
		// 资产来源选择事件绑定
		$("#instCode").change(Events.instCodeOnchangeAct);

		// 合作机构change事件
		$("#borrowMeasuresInstit").change(Events.borrowMeasuresInstitChgAct),
		// 借款主体change事件
		$("#comName").change(Events.comNameChgAct),

            //企业借款及个人借款的选择事件
		$("#jklxQYJK").click(function(){
            $("#manname").attr("ignore","ignore");
            $("#manname").removeAttr("ajaxurl");
            $("#cardNo").attr("ignore","ignore");
            $("#cardNo").removeAttr("ajaxurl");

            $("#comName").removeAttr("ignore");
            $("#comName").attr("ajaxurl","isBorrowUserCACheck");
            $("#comSocialCreditCode").removeAttr("ignore");
            $("#comSocialCreditCode").attr("ajaxurl","isCAIdNoCheck");
			// add by nxl 20180711
            $("#registrationAddress").removeAttr("ignore");
            $("#address").attr("ignore","ignore");
            // add by liushouyi nifa2 20181129
            $("#annualIncome").attr("ignore","ignore");
            $("#position").attr("ignore","ignore");
            $("#comRegCaptial").removeAttr("ignore");
            $("#comLocationCity").removeAttr("ignore");
            $("#comLegalPerson").removeAttr("ignore");
            $("#comRegTime").removeAttr("ignore");
        });
		//个人借款
        $("#jklxGRJK").click(function(){
            $("#comName").attr("ignore","ignore");
            $("#comName").removeAttr("ajaxurl");
            $("#comSocialCreditCode").attr("ignore","ignore");
            $("#comSocialCreditCode").removeAttr("ajaxurl");

            $("#manname").removeAttr("ignore");
            $("#manname").attr("ajaxurl","isBorrowUserCACheck");
            $("#cardNo").removeAttr("ignore");
            $("#cardNo").attr("ajaxurl","isCAIdNoCheck");
			// add by nxl 20180711
            $("#registrationAddress").attr("ignore","ignore");
            $("#address").removeAttr("ignore");
            // add by liushouyi nifa2 20181129
            $("#annualIncome").removeAttr("ignore");
            $("#position").removeAttr("ignore");
            $("#comRegCaptial").attr("ignore","ignore");
            $("#comLocationCity").attr("ignore","ignore");
            $("#comLegalPerson").attr("ignore","ignore");
            $("#comRegTime").attr("ignore","ignore");
        });

        //初始化
        if($("#jklxGRJK").attr("checked") == "checked"){
            $("#comName").attr("ignore","ignore");
            $("#comName").removeAttr("ajaxurl");
            $("#comSocialCreditCode").attr("ignore","ignore");
            $("#comSocialCreditCode").removeAttr("ajaxurl");

            $("#manname").removeAttr("ignore");
            $("#manname").attr("ajaxurl","isBorrowUserCACheck");
            $("#cardNo").removeAttr("ignore");
            $("#cardNo").attr("ajaxurl","isCAIdNoCheck");
            // add by nxl 20180711
            $("#registrationAddress").attr("ignore","ignore");
            $("#address").removeAttr("ignore");
            // add by liushouyi nifa2 20181129
            $("#annualIncome").removeAttr("ignore");
            $("#position").removeAttr("ignore");
            $("#comRegCaptial").attr("ignore","ignore");
            $("#comLocationCity").attr("ignore","ignore");
            $("#comLegalPerson").attr("ignore","ignore");
            $("#comRegTime").attr("ignore","ignore");

        }else{
            $("#manname").attr("ignore","ignore");
            $("#manname").removeAttr("ajaxurl");
            $("#cardNo").attr("ignore","ignore");
            $("#cardNo").removeAttr("ajaxurl");

            $("#comName").removeAttr("ignore");
            $("#comName").attr("ajaxurl","isBorrowUserCACheck");
            $("#comSocialCreditCode").removeAttr("ignore");
            $("#comSocialCreditCode").attr("ajaxurl","isCAIdNoCheck");
            // add by nxl 20180711
            $("#registrationAddress").removeAttr("ignore");
            $("#address").attr("ignore","ignore");
            // add by liushouyi nifa2 20181129
            $("#annualIncome").attr("ignore","ignore");
            $("#position").attr("ignore","ignore");
            $("#comRegCaptial").removeAttr("ignore");
            $("#comLocationCity").removeAttr("ignore");
            $("#comLegalPerson").removeAttr("ignore");
            $("#comRegTime").removeAttr("ignore");
        }

		// 图片上传
		$('.fileupload').fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];
				var imageSort = $(this).parent().parent().parent().parent().parent().find("input[name=imageSort]");
				if(imageSort.val() == "") {
					imageSort.val($("#xmzxList input[name=imageName]").length - 1);
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
					$("tr:not(:first)", "#xmzxList").remove();
					el = $("#xmzxList").find("tr");
					imageCount = 1;
				} else {
					// 打开模板
					Events.xmzxAddRowClkAct();
					el = $("#xmzxList").find("tr:last");
				}
				
				el.find("input[name=imageSort]").val($("#xmzxList tr").length - 1);
				el.find("input[name=imageName]").val(file.imageName);
				el.find("input[name=imageRealName]").val(file.imageRealName);
				el.find("input[name=imagePath]").val(file.imagePath);
				el.find("img").attr("src", file.imageSrc).parent().attr("data-content", "<img src='" + file.imageSrc + "' style='max-height:350px;'/>");	
			}
		}).find("input:file").removeAttr('disabled');
		
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
					el.find("input[name=number]").val(file.number);
					el.find("input[name=registration]").val(file.registration);	
					el.find("input[name=vin]").val(file.vin);	
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
					el.find("input[name=housesAssessPrice]").val(file.housesAssessPrice);
					el.find("input[name=housesBelong]").val(file.housesBelong);
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

		// del by liuyang 20170714 start
//		$("#monthlyIncome").keyup(function(){
//		    var _self = $(this);
//		    var val = _self.val();
//		    var reg = /^[0]|[^0-9.]/g;	
//		    _self.val(val.replace(reg, ''));
//		})
		// del by liuyang 20170714 end
		//借款内容模板下载
		$(".btn-DownloadContentFill").click(Events.downloadContentFillClkAct);
		
		// 借款内容填充
		$('.btn-contentfill').fileupload({
			url: "contentFillAction",
			autoUpload: true,
			done: function (e, data) {
				Page.coverLayer();
				var datas = data.result;
				//借款人用户名
				$("#username").val(datas.username);
				if(datas.username != null && datas.username != ''){
					$("#username").blur();
				}
				//项目申请人
				$("#applicant").val(datas.applicant);
				if(datas.applicant != null && datas.applicant != ''){
					$("#applicant").blur();
				}
				//担保机构用户名
				$("#repayOrgName").val(datas.repayOrgName);
				if(datas.repayOrgName != null && datas.repayOrgName != ''){
					$("#repayOrgName").blur();
				}
				//项目名称
				$("#projectName").val(datas.projectName);				
				//项目名称
				$("#jkName").val(datas.jkName);
				//借款金额
				if(datas.account != null && datas.account != ''){
					$("#account").val(parseInt(datas.account));
				}
				//出借利率
				$("#borrowApr").val(datas.borrowApr);					
				//融资用途
				$("#financePurpose").val(datas.financePurpose).change();
				//月薪收入
				$("#monthlyIncome").val(datas.monthlyIncome);				
				//还款来源
				$("#payment").val(datas.payment);	
				//第一还款来源
				$("#firstPayment").val(datas.firstPayment);					
				//第二还款来源
				$("#secondPayment").val(datas.secondPayment);					
				//费用说明
				$("#costIntrodution").val(datas.costIntrodution);					
				//项目信息
				$("#borrowContents").val(datas.borrowContents);					
				//财务状况
				$("#fianceCondition").val(datas.fianceCondition);					
				//车辆品牌
				$("#brand").val(datas.brand);		
				//型号
				$("#model").val(datas.model);	
				//产地
				$("#place").val(datas.place);					
				//购买价格
				if(datas.price != null && datas.price != ''){
					$("#price").val(parseInt(datas.price));	
				}
				//评估价（元）
				if(datas.toprice != null && datas.toprice != ''){
					$("#toprice").val(parseInt(datas.toprice));	
				}
				//车牌号
				$("#number").val(datas.number);					
				//房产类型
				if(datas.housesType == '住宅'){
					$("#housesType").val(1);
				}else if(datas.housesType == '房产'){
					$("#housesType").val(2);
				}else if(datas.housesType == '土地'){
					$("#housesType").val(3);
				}else if(datas.housesType == '工业用房'){
					$("#housesType").val(4);
				}else if(datas.housesType == '工业用地'){
					$("#housesType").val(5);
				}
				//建筑面积
				$("#housesArea").val(datas.housesArea);	
				//资产数量
				if(datas.housesCnt != null && datas.housesCnt != ''){
					$("#housesCnt").val(parseInt(datas.housesCnt));	
				}
				//评估价值（元）
				if(datas.housesToprice != null && datas.housesToprice != ''){
					$("#housesToprice").val(parseInt(datas.housesToprice));	
				}
				//资产所属
				$("#housesBelong").val(datas.housesBelong);	
				//借款类型
				if(datas.companyOrPersonal == '个人'){
					$("label[for=jklxGRJK]").trigger("click");
					$("#comName").attr("ignore","ignore");
                    $("#comName").removeAttr("ajaxurl");
                    $("#comSocialCreditCode").attr("ignore","ignore");
                    $("#comSocialCreditCode").removeAttr("ajaxurl");
                    // add by nxl 20180810
                    $("#registrationAddress").attr("ignore","ignore");
                    $("#manname").removeAttr("ignore");
                    $("#manname").attr("ajaxurl","isBorrowUserCACheck");
                    $("#cardNo").removeAttr("ignore");
                    $("#cardNo").attr("ajaxurl","isCAIdNoCheck");
                    $("#registrationAddress").attr("ignore","ignore");
                    $("#address").removeAttr("ignore");
                    // add by liushouyi nifa2 20181129
                    $("#annualIncome").removeAttr("ignore");
                    $("#position").removeAttr("ignore");
                    $("#comRegCaptial").attr("ignore","ignore");
                    $("#comLocationCity").attr("ignore","ignore");
                    $("#comLegalPerson").attr("ignore","ignore");
                    $("#comRegTime").attr("ignore","ignore");

                }else if(datas.companyOrPersonal == '企业'){
                    $("label[for=jklxQYJK]").trigger("click");
                    // add by nxl 20180810
                    $("#manname").attr("ignore","ignore");
                    $("#manname").removeAttr("ajaxurl");
                    $("#cardNo").attr("ignore","ignore");
                    $("#cardNo").removeAttr("ajaxurl");

                    $("#comName").removeAttr("ignore");
                    $("#comName").attr("ajaxurl","isBorrowUserCACheck");
                    $("#comSocialCreditCode").removeAttr("ignore");
                    $("#comSocialCreditCode").attr("ajaxurl","isCAIdNoCheck");
                    $("#registrationAddress").removeAttr("ignore");
                    $("#address").attr("ignore","ignore");
                    // add by liushouyi nifa2 20181129
                    $("#annualIncome").attr("ignore","ignore");
                    $("#position").attr("ignore","ignore");
                    $("#comRegCaptial").removeAttr("ignore");
                    $("#comLocationCity").removeAttr("ignore");
                    $("#comLegalPerson").removeAttr("ignore");
                    $("#comRegTime").removeAttr("ignore");
                }
				//融资主体
				$("#comName").val(datas.comName);					
				//法人
				$("#comLegalPerson").val(datas.comLegalPerson);					
				//注册地区
				$("#comLocationCity").val(datas.comLocationCity);						
				//主营业务
				$("#comMainBusiness").val(datas.comMainBusiness);				
				//在平台逾期次数
				if(datas.comOverdueTimes != null && datas.comOverdueTimes != ''){
					$("#comOverdueTimes").val(parseInt(datas.comOverdueTimes));				
				}
				//在平台逾期金额
				if(datas.comOverdueAmount != null && datas.comOverdueAmount != ''){
					$("#comOverdueAmount").val(parseInt(datas.comOverdueAmount));	
				}
				//注册时间
				$("#comRegTime").val(datas.comRegTime);
				//统一社会信用代码
				$("#comSocialCreditCode").val(datas.comSocialCreditCode);	
				//注册号
				$("#comRegistCode").val(datas.comRegistCode);
				if(datas.comRegCaptial!=null && datas.comRegCaptial != ''){
                    //注册资本
                    var comRegCaptial = datas.comRegCaptial.substr(0,datas.comRegCaptial.indexOf("."));
                    $("#comRegCaptial").val(comRegCaptial);
                }
				//所属行业
				$("#comUserIndustry").val(datas.comUserIndustry);
				//涉诉情况
				$("#comLitigation").val(datas.comLitigation);	
				//姓名
				$("#manname").val(datas.manname);
				//身份证号
				$("#cardNo").val(datas.cardNo);
				//年龄
				if(datas.old != null && datas.old != ''){
					$("#old").val(parseInt(datas.old));
				}
				//岗位职业
				$("#position").val(datas.position).change();
				//性别
				$("#sex").val(datas.sex);
				if(datas.sex == '男'){
					$("#jkgrMan").attr("checked","checked");
				}else if(datas.sex == '女'){
					$("#jkgrWon").attr("checked","checked");
				}
				
				//婚姻状况
				if(datas.merry == '已婚'){
					$("#jkgrYihun").attr("checked","checked");
				}else if(datas.merry == '未婚'){
					$("#jkgrWeihun").attr("checked","checked");
				}else if(datas.merry == '离异'){
					$("#jkgrLiyi").attr("checked","checked");
				}else if(datas.merry == '丧偶'){
					$("#jkgrSangou").attr("checked","checked");
				}
				
				//工作城市
				$("#location_c").val(datas.location_c);
				//户籍地
				$("#domicile").val(datas.domicile);
				//在平台逾期次数
				if(datas.overdueTimes != null && datas.overdueTimes != ''){
					$("#overdueTimes").val(parseInt(datas.overdueTimes));
				}
				//在平台逾期金额
				if(datas.overdueAmount != null && datas.overdueAmount != ''){
					$("#overdueAmount").val(parseInt(datas.overdueAmount));
				}
				//涉诉情况
				$("#litigation").val(datas.litigation);
	
				// 信批需求新增(个人/企业)
				if(datas.companyOrPersonal == '个人'){
					//(个人)年收入
					if(datas.annualIncome != null && datas.annualIncome != ''){
					    var annualIncome = datas.annualIncome.substr(0,datas.annualIncome.indexOf("."));
						$("#annualIncome").val(annualIncome);
					}
					//(个人)征信报告逾期情况
					if(datas.overdueReport != null && datas.overdueReport != ''){
						$("#overdueReport").val(datas.overdueReport);
					}
					//(个人)重大负债状况
					if(datas.debtSituation != null && datas.debtSituation != ''){
						$("#debtSituation").val(datas.debtSituation);
					}
					//(个人)其他平台借款情况
					if(datas.otherBorrowed != null && datas.otherBorrowed != ''){
						$("#otherBorrowed").val(datas.otherBorrowed);
					}
					//(个人)借款资金运用情况
					if(datas.isFunds != null && datas.isFunds != ''){
						$("#isFunds").val(datas.isFunds);
					} 
					//(个人)借款人经营状况及财务状况
					if(datas.isManaged != null && datas.isManaged != ''){
						$("#isManaged").val(datas.isManaged);
					} 
					//(个人)借款人还款能力变化情况
					if(datas.isAbility != null && datas.isAbility != ''){
						$("#isAbility").val(datas.isAbility);
					}
					//(个人)借款人逾期情况
					if(datas.isOverdue != null && datas.isOverdue != ''){
						$("#isOverdue").val(datas.isOverdue);
					}
					//(个人)借款人涉诉情况
					if(datas.isComplaint != null && datas.isComplaint != ''){
						$("#isComplaint").val(datas.isComplaint);
					}
					//(个人)借款人受行政处罚情况
					if(datas.isPunished != null && datas.isPunished != ''){
						$("#isPunished").val(datas.isPunished);
					}
					//(个人)原个人勾选改上传 start
					//身份证 0.0
					if(datas.isCard != null && datas.isCard != ''){
						$("#isCard").prop('checked',parseInt(datas.isCard));
					}
					//收入状况
					if(datas.isIncome != null && datas.isIncome != ''){
						$("#isIncome").prop('checked',parseInt(datas.isIncome));
					}
					//信用状况
					if(datas.isCredit != null && datas.isCredit != ''){
						$("#isCredit").prop('checked',parseInt(datas.isCredit));
					}
					//资产状况
					if(datas.isAsset != null && datas.isAsset != ''){
						$("#isAsset").prop('checked',parseInt(datas.isAsset));
					}
					//车辆状况
					if(datas.isVehicle != null && datas.isVehicle != ''){
						$("#isVehicle").prop('checked',parseInt(datas.isVehicle));
					}
					//行驶证
					if(datas.isDrivingLicense != null && datas.isDrivingLicense != ''){
						$("#isDrivingLicense").prop('checked',parseInt(datas.isDrivingLicense));
					}
					//车辆登记证
					if(datas.isVehicleRegistration != null && datas.isVehicleRegistration != ''){
						$("#isVehicleRegistration").prop('checked',parseInt(datas.isVehicleRegistration));
					}
					//婚姻状况
					if(datas.isMerry != null && datas.isMerry != ''){
						$("#isMerry").prop('checked',parseInt(datas.isMerry));
					}
					//工作状况
					if(datas.isWork != null && datas.isWork != ''){
						$("#isWork").prop('checked',parseInt(datas.isWork));
					}
					//户口本
					if(datas.isAccountBook != null && datas.isAccountBook != ''){
						$("#isAccountBook").prop('checked',parseInt(datas.isAccountBook));
					}
					//(个人)原个人勾选改上传 end
					// add by nxl 20180809 互金添加借款人地址start
                    if(datas.address != null && datas.address != ''){
                        $("#address").val(datas.address);
                    }
                    // add by nxl 20180809 互金添加借款人地址end

				} else if(datas.companyOrPersonal == '企业') {
					//(企业)征信报告逾期情况
					if(datas.comOverdueReport != null && datas.comOverdueReport != ''){
						$("#comOverdueReport").val(datas.comOverdueReport);
					}
					//(企业)重大负债状况
					if(datas.comDebtSituation != null && datas.comDebtSituation != ''){
						$("#comDebtSituation").val(datas.comDebtSituation);
					}
					//(企业)其他平台借款情况
					if(datas.comOtherBorrowed != null && datas.comOtherBorrowed != ''){
						$("#comOtherBorrowed").val(datas.comDebtSituation);
					}
					//(企业)借款资金运用情况
					if(datas.comIsFunds != null && datas.comIsFunds != ''){
						$("#comIsFunds").val(datas.comIsFunds);
					}
					//(企业)借款人经营状况及财务状况
					if(datas.comIsManaged != null && datas.comIsManaged != ''){
						$("#comIsManaged").val(datas.comIsManaged);
					}
					//(企业)借款人还款能力变化情况
					if(datas.comIsAbility != null && datas.comIsAbility != ''){
						$("#comIsAbility").val(datas.comIsAbility);
					}
					//(企业)借款人逾期情况
					if(datas.comIsOverdue != null && datas.comIsOverdue != ''){
						$("#comIsOverdue").val(datas.comIsOverdue);
					}
					//(企业)借款人涉诉情况
					if(datas.comIsComplaint != null && datas.comIsComplaint != ''){
						$("#comIsComplaint").val(datas.comIsComplaint);
					}
					//(企业)借款人受行政处罚情况
					if(datas.comIsPunished != null && datas.comIsPunished != ''){
						$("#comIsPunished").val(datas.comIsPunished);
					}
					
					//(企业)原个人勾选改上传 start
					//企业证件
					if(datas.comIsCertificate != null && datas.comIsCertificate != ''){
						$("#comIsCertificate").prop('checked',parseInt(datas.comIsCertificate));
					}
					//经营状况
					if(datas.comIsOperation != null && datas.comIsOperation != ''){
						$("#comIsOperation").prop('checked',parseInt(datas.comIsOperation));
					}
					//财务状况
					if(datas.comIsFinance != null && datas.comIsFinance != ''){
						$("#comIsFinance").prop('checked',parseInt(datas.comIsFinance));
					}
					//企业信用
					if(datas.comIsEnterpriseCreidt != null && datas.comIsEnterpriseCreidt != ''){
						$("#comIsEnterpriseCreidt").prop('checked',parseInt(datas.comIsEnterpriseCreidt));
					}
					//法人信息
					if(datas.comIsLegalPerson != null && datas.comIsLegalPerson != ''){
						$("#comIsLegalPerson").prop('checked',parseInt(datas.comIsLegalPerson));
					}
					//资产状况
					if(datas.comIsAsset != null && datas.comIsAsset != ''){
						$("#comIsAsset").prop('checked',parseInt(datas.comIsAsset));
					}
					//购销合同
					if(datas.comIsPurchaseContract != null && datas.comIsPurchaseContract != ''){
						$("#comIsPurchaseContract").prop('checked',parseInt(datas.comIsPurchaseContract));
					}
					//供销合同
					if(datas.comIsSupplyContract != null && datas.comIsSupplyContract != ''){
						$("#comIsSupplyContract").prop('checked',parseInt(datas.comIsSupplyContract));
					}
					//(企业)原个人勾选改上传 end
                    // add by nxl 20180809 互金添加企业注册地址和企业注册地 start
                    if(datas.corporateCode != null && datas.corporateCode != ''){
                        $("#corporateCode").val(datas.corporateCode);
                    }
                    if(datas.registrationAddress != null && datas.registrationAddress != ''){
                        $("#registrationAddress").val(datas.registrationAddress);
                    }
                    // add by nxl 20180809 互金添加企业注册地址和企业注册地 end

                }
			},
			start:function() {
				Page.coverLayer("正在上传文件请稍后");
			}
		}).find("input:file").removeAttr('disabled')
		//受托支付标志change事件
		$('input[name=entrustedFlg]').change(Events.entrustedFlgClkAct);
		
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
		Events.clearEngineUsed(); // 提交后，切换 是否调用标的分配规则引擎  删除报错
	}
}),

Page.initialize();