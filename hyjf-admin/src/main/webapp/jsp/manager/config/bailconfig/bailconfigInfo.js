var
// --------------------------------------------------------------------------------------------------------------------------------
Action = {
	// 联动下拉事件
	instCodeChangeAction : "instCodeChangeAction"
},
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
            // var monthDEL = $("#monthDEL").val();
            // var endDEL = $("#endDEL").val();
            // var endmonthDEL = $("#endmonthDEL").val();
            // var enddayDEL = $("#enddayDEL").val();
            // var principalDEL = $("#principalDEL").val();
            // var seasonDEL = $("#seasonDEL").val();
            // var endmonthsDEL = $("#endmonthsDEL").val();
            var checkResult = true;

            // 合规改造删除 2018-12-03
            /*if (null != monthDEL && monthDEL == 0){
                var checked=$("input[class='monthCheckbox']:checked");
                if(checked.length==0){
                    checkResult = false;
                    Page.alert("","请选择等额本息的授信校验方式！");
                }
            }
            if (checkResult && null != endDEL && endDEL == 0){
                var checked=$("input[class='endCheckbox']:checked");
                if(checked.length==0){
                    checkResult = false;
                    Page.alert("","请选择按月计息，到期还本还息的授信校验方式！");
                }
            }
            if (checkResult && null != endmonthDEL && endmonthDEL == 0){
                var checked=$("input[class='endmonthCheckbox']:checked");
                if(checked.length==0){
                    checkResult = false;
                    Page.alert("","请选择先息后本的授信校验方式！");
                }
            }
            if (checkResult && null != enddayDEL && enddayDEL == 0){
                var checked=$("input[class='enddayCheckbox']:checked");
                if(checked.length==0){
                    checkResult = false;
                    Page.alert("","请选择按天计息，到期还本息的授信校验方式！");
                }
            }
            if (checkResult && null != principalDEL && principalDEL == 0){
                var checked=$("input[class='principalCheckbox']:checked");
                if(checked.length==0){
                    checkResult = false;
                    Page.alert("","请选择等额本金的授信校验方式！");
                }
            }
            if (checkResult && null != seasonDEL && seasonDEL == 0){
                var checked=$("input[class='seasonCheckbox']:checked");
                if(checked.length==0){
                    checkResult = false;
                    Page.alert("","请选择按季还款的授信校验方式！");
                }
            }
            if (checkResult && null != endmonthsDEL && endmonthsDEL == 0){
                var checked=$("input[class='endmonthsCheckbox']:checked");
                if(checked.length==0){
                    checkResult = false;
                    Page.alert("","请选择按月付息到期还本的授信校验方式！");
                }
            }*/
            if(checkResult){
                Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
                    if (isConfirm) {
                        Page.submit();
                    }
                })
            }
		}
	},
	topLimitCheck : function() {
		var capitalToplimit = $("#capitalToplimit").val() * 100;
		var capitalUsed = $("#capitalUsed").val() * 100;
		$("#capitalAvailable").val((capitalToplimit - capitalUsed)/100);
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
    instCodeSrchOnchangeAct : function() {
        var instCode = $("#instCode").val();
        // 隐藏所有回滚方式
        if (instCode == "") {
            return;
        }
        $.ajax({
            url : Action.instCodeChangeAction,
            type : "POST",
            text : "",
            async : true,
            dataType : "json",
            data :  {
                instCode : instCode
            },
            success : function(data) {
            	var result = 0;
            	// 变更机构时先将所有设置隐藏再根据数据库数值显示
                // 初始化数据
                $("#month").attr("style","display:none");
                $("#end").attr("style","display:none");
                $("#endday").attr("style","display:none");
                $("#endmonth").attr("style","display:none");
                $("#principal").attr("style","display:none");
                $("#season").attr("style","display:none");
                $("#endmonths").attr("style","display:none");
                $("#endDEL").val(1);
                $("#monthDEL").val(1);
                $("#endmonthDEL").val(1);
                $("#principalDEL").val(1);
                $("#seasonDEL").val(1);
                $("#endmonthsDEL").val(1);
                $("#enddayDEL").val(1);
                // 更新数据
				if (data.endDEL==0){
                    $("#end").attr("style","display:");
                    $("#endDEL").val(0);
                    result = 1;
				}
                if (data.enddayDEL==0){
                    $("#endday").attr("style","display:");
                    $("#enddayDEL").val(0);
                    result = 1;
                }
                if (data.monthDEL==0){
                    $("#month").attr("style","display:");
                    $("#monthDEL").val(0);
                    result = 1;
                }
                if (data.endmonthDEL==0){
                    $("#endmonth").attr("style","display:");
                    $("#endmonthDEL").val(0);
                    result = 1;
                }
                if (data.principalDEL==0){
                    $("#principal").attr("style","display:");
                    $("#principalDEL").val(0);
                    result = 1;
                }
                if (data.seasonDEL==0){
                    $("#season").attr("style","display:");
                    $("#seasonDEL").val(0);
                    result = 1;
                }
                if (data.endmonthsDEL==0){
                    $("#endmonths").attr("style","display:");
                    $("#endmonthsDEL").val(0);
                    result = 1;
                }
                if (result == 0){
					// 没有还款方式确认按钮不可用
                    $("#fn-Confirm").attr("disabled", true);
                    Page.alert("","没有对应的还款方式，请先设置还款方式!");
				} else{
                    $("#fn-Confirm").attr("disabled", false);
				}
            },
            error : function(err) {
                Page.alert("","没有对应的还款方式，请先设置还款方式!");
            }
        });
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面布局
    doLayout: function() {
        // 条件下拉框
        $(".form-select2").select2({
            width: 268,
            placeholder: "请选择条件",
            allowClear: true,
            language: "zh-CN"
        }),
        // 日历选择器
        $('.datepicker').datepicker({
            autoclose: true,
            todayHighlight: true
        });
        // 日历范围限制
        $('.input-daterange :text[id$="timestart"]').on("change", function(evnet, d) {
            d = $(this).parent().nextAll(".form-control").datepicker("getDate"),
            d && $(this).datepicker("setTimeend", d)
        }),
        $('.input-daterange :text[id$=timeend]').on("change", function(evnet, d) {
            d = $(this).prevAll(".input-icon").find(":text").datepicker("getDate"),
            d && $(this).datepicker("setTimestart", d)
        });
    },
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		// 用户名修改事件绑定
		$("#capitalToplimit").change(Events.topLimitCheck);
        // 资产来源选择事件绑定 （合规改造删除 2018-12-03）
        // $("#instCode").change(Events.instCodeSrchOnchangeAct);
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct())
				|| Page.coverLayer();
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
	}
}),

Page.initialize();
