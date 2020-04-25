var
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
		// 查找的Action
		searchAction : "searchInfoAction",
},
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
		
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		//Page.primaryKey.val($(this).data("debtPlanNid"));
		Page.primaryKey1.val($("#borrowNid").val());
		Page.primaryKey2.val($("#borrowStyle").val());
		Page.submit(Action.searchAction);
	},
	// 提交按钮的单击动作事件
	submitClkAct : function(event) {
		
		var message ="";
		// 取得选择行
		selection = $(".listCheck:checked");
		if (!selection[0]) {
			message = "不关联专属项目，意味着该计划只承接到期清算的债权。您确定不关联吗？";
			Page.primaryKey.val("");
		} else {
			cds = [],
			selection.each(function() {
				cds.push(this.value);
			}),
			Page.primaryKey.val(JSON.stringify(cds));
		}
		if(Page.validate.check(false) && $(".has-error").length == 0) {
			
			message = "确定要保存当前填写的内容吗？";
			if (!selection[0]) {
				message = "不关联专属项目，意味着该计划只承接到期清算的债权。您确定不关联吗？";
			}
			var _investStart=parseFloat($("#tenderAccountMin").val());
			var _increaseMoney=parseFloat($("#borrowIncreaseMoney").val());
			if(_investStart<_increaseMoney){
				Page.alert("递增金额不能大于最低投标金额！");
				return false;
			}
			
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
	
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").trigger('change');
		$("#borrowNidSrch").val("");
		$("#borrowStyleSrch").val("");
	},
	// 计划类型change事件
	selPlanIdChgAct: function() {
		var debtPlanType = $(this).find("option:selected").data("debtplantype");
		var debtPlanName = $(this).find("option:selected").data("debtplantypename");
		jQuery.ajax({
			type: "POST",
			async: "async",
			url: "getPlanName.do",
			contentType:"application/x-www-form-urlencoded;charset=utf-8",
			dataType: "json",
			data: {
				"debtPlanType":debtPlanType
			},
			error: function(request) {
				popupWin("连接服务器出错,请稍后重试");
			},
			success: function(data){
				if(data){
					$("#debtPlanName").val(data.data);
				}
			}
		});
		// 退出方式
		$("#debtQuitStyle").val($(this).find("option:selected").data("debtquitstyle")),
		// 最低出借金额
		$("#debtMinInvestment").val($(this).find("option:selected").data("debtmininvestment")),
		// 递增金额
		$("#debtInvestmentIncrement").val($(this).find("option:selected").data("debtinvestmentincrement"));
		// 最高加入金额
		$("#debtMaxInvestment").val($(this).find("option:selected").data("debtmaxinvestment"));
		// 锁定期限
		$("#debtLockPeriod").val($(this).find("option:selected").data("debtlockperiod"));
		// 退出所需天数
		$("#debtQuitPeriod").val($(this).find("option:selected").data("debtquitperiod"));
	},

	// 获取借款预编号
	getBorrowPreNidClkAct : function() {
		$.post('getBorrowPreNid', {}, function (text, status) {
			$("#borrowPreNid").val(text).blur();
		});
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
	
			readonly:true,
			
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
		// 项目类型change事件
		$("#debtPlanType").change(Events.selPlanIdChgAct),
		// 获取借款预编号
		$("#getBorrowPreNid").click(Events.getBorrowPreNidClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 确定按钮的单击事件绑定
		$(".fn-Submit").click(Events.submitClkAct),
		// 下一步按钮的单击事件绑定
		$(".fn-Next").click(Events.nextClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct)
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
	}
}),

Page.initialize();