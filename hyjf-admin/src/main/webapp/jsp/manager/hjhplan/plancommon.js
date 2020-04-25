var
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
		// 关联资产添加的Action
		/*addAction : "updateBorrowInfoAction",*/
},
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
		$("#borrowNid").val("");
		$("#borrowStyle").val("");
		$("#borrowStyleSrch").val("");
	},

	//有效期类型 选择事件
	changeExpiraClkAct : function() {
		if ($(this).val() == "endday") {//按天计息，到还本还息
			$("#expType-1").show();
			$("#expType-2").hide();
			Page.validate.unignore("#lockPeriod");
            document.getElementById("isMonthD").checked=true;

		}else if($(this).val() == "month"){//等额本息
			$("#expType-1").show();
			$("#expType-2").hide();
			Page.validate.unignore("#lockPeriod");

		}else if($(this).val() == "end"){//按月计息，到期还本还息
			$("#expType-1").show();
			$("#expType-2").hide();
			Page.validate.unignore("#lockPeriod");
            document.getElementById("isMonthM").checked=true;

		}else if($(this).val() == "endmonth"){//先息后本
			$("#expType-1").show();
			$("#expType-2").hide();
			Page.validate.unignore("#lockPeriod");

		}else if($(this).val() == "principal"){//等额本金
			$("#expType-1").show();
			$("#expType-2").hide();
			Page.validate.unignore("#lockPeriod");

		}
	},

	// 提交按钮的单击动作事件
	submitClkAct : function(event) {
		var message ="";
		// 取得选择行
		selection = $(".listCheck:checked");
		if(Page.validate.check(false) && $(".has-error").length == 0) {

			message = "确定要保存当前填写的内容吗？";
			var disableds=$(":disabled");
			// 增加disabled属性
			disableds.each(function (index,domEle){
				$(domEle).attr("disabled","disabled");
			});
			Page.confirm("", message, function(isConfirm) {
				isConfirm && (
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

	// 可用券
	kyqCheckChgAct: function() {
		var isChecked = $("#checkAllUse").prop("checked");

		$("input[name='couponConfig']").each(function(index,element){
			$(this).prop("checked",isChecked);
		});
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#debtPlanBorrowNid"),
	primaryKey1 : $("#borrowNidSrch"),
	primaryKey2 : $("#borrowStyleSrch"),

	// 统计画面验证错误数
	countValidErr: function() {
		$("#mainTabs a").find(".badge").remove().end()
			.each(function(errCount) {
				errCount = $(".Validform_wrong", $(this).attr("href")).length,
				errCount && $("<span class='badge badge-danger margin-left-5'>" + errCount + "</span>").appendTo(this);
			});
	},
	// 画面布局
	doLayout: function() {
		// 初始化下拉框
		$(".form-select2").select2({
			width: 280,
			placeholder: "请选择条件...",
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

	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 确定按钮的单击事件绑定
		$(".fn-Submit").click(Events.submitClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 下一步按钮的单击事件绑定
		$(".fn-Next").click(Events.nextClkAct),
		// 可用券
		$("#checkAllUse").click(Events.kyqCheckChgAct);
		// 还款方式选择事件绑定
		$("#borrowStyle").change(Events.changeExpiraClkAct).change();

		$("input[name='couponConfig']").change(function(){
			var len = $("input[name='couponConfig']").length;
			var checkedLen = $("input[name='couponConfig']:checked").length;
			if(checkedLen<len){
				$("#checkAllUse").prop("checked",false).change();
			}else{
				$("#checkAllUse").prop("checked",true).change();
			}
		});
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
			tiptype: 3,
			datatype:{
				/*
				 * 最高加入金额与最低加入金额比较
				 */
				"compareMinInvest":function(gets,obj,curform,regxp){
					//gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
					//var m=/^(\s*|\d{0,6}|1250000|[1][2][0-4][0-9]{4}|[1][0-1][0-9]{5})$/;
                    var m=/^[1-9]\d*$/;
					if(!m.test(gets)){
						return false;
					}
					var min=curform.find("#debtMinInvestment").val();
					if(parseInt(min)>=parseInt(gets)){
						return "添加智投中最高授权服务金额必须大于最低授权服务金额";
					}
					return true;
				},
				/*
				 * 递增金额与最高加入金额比较
				 */
				"compareMaxInvest":function(gets,obj,curform,regxp){
					var max=curform.find("#debtMaxInvestment").val();
					if(max.trim()==""){
						return "请先填写最高授权服务金额";
					}
					if(parseInt(gets)>parseInt(max)){
						return "添加智投中的递增金额必须小于最高授权服务金额";
					}
					return true;
				},
			}
		});
		/*最低加入金额变化同步判断最高加入金额情况*/
		$("#debtMinInvestment").change(function(){
			if($("#debtMaxInvestment").val()!=""){
				$("#debtMaxInvestment").val(parseInt($("#debtMaxInvestment").val())+1);
				Page.validate.check(false,"#debtMaxInvestment");
				$("#debtMaxInvestment").val(parseInt($("#debtMaxInvestment").val())-1);
				Page.validate.check(false,"#debtMaxInvestment");
			}
		});
		/*最高加入金额变化同步判断递增金额字段*/
		$("#debtMaxInvestment").change(function(){
			if($("#debtInvestmentIncrement").val()!=""){
				$("#debtInvestmentIncrement").val(parseInt($("#debtInvestmentIncrement").val())+1);
				Page.validate.check(false,"#debtInvestmentIncrement");
				$("#debtInvestmentIncrement").val(parseInt($("#debtInvestmentIncrement").val())-1);
				Page.validate.check(false,"#debtInvestmentIncrement");
			}
		});
	}
}),

Page.initialize();
