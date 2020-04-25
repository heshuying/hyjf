var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 新增修改信息
        addOrSaveAction: "addOrSaveAction"
    };
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面布局
    doLayout: function () {
        $("#qrcode").each(function () {
            $(this).qrcode({
                text: $("#qrcodeValue").val(),
                render: "canvas",
                width: 128,
                height: 128,
                typeNumber: -1,
                correctLevel: QRErrorCorrectLevel.H,
                background: "#ffffff",
                foreground: "#000000"
            })
        });
    }
});

var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 事件动作处理 */
    Events = {
        // 确认按键单击事件绑定
        confirmClkAct: function () {
            if (Page.validate.check(false)
                && Page.form.find(".has-error").length == 0) {
                var error = checkAllRadio();
                if (!error) {
                    Page.confirm("", "确定要保存当前信息吗？", function (isConfirm) {
                        if (isConfirm) {
                            Page.submit();
                        }
                    })
                } else {
                    //Events.addErrorClass("#serviceRatio", error);
                    if (error.length > 1) {
                        Events.addErrorClass(error[1], "分账比例至少有两项不为0！");
                    } else {
                        Events.addErrorClass(error[0], "分账比例总和不能大于1！");
                    }

                }
            } else if (Page.form.find(".has-error").length == 0) {
                Events.addErrorClass("#serviceRatio");
                Events.addErrorClass("#creditRatio");
                Events.addErrorClass("#manageRatio");
                Events.addErrorClass("#investorCompany");
            }
        },
        cancelClkAct: function () {
            parent.$.colorbox.close();
        },
        /**
         * 用户名读取分账人配置表，选择后查询出此用户名对应的姓名、江西银行电子账号；用户名对应的姓名、江西银行电子账号的文本框为只读，不可修改
         */
        usernameOnchangeAct: function () {
            var username = $("#username").val();
            var truename = $("#username").find("option:selected").attr("truename");
            var account = $("#username").find("option:selected").attr("account");
            var valid = window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
            if (!username || username == "") {
                valid.resetForm();
                return;
            } else {
                $("#truename").val(truename);
                $("#account").val(account);
                valid.check(false, "#username");
                valid.check(false, "#truename");
                valid.check(false, "#account");
            }
        },
        /**
         * 当选择按出借人时，分账来源有：全部，服务费，债转服务费、管理费，并且跟随对应的分账比例设置；
         * 当选择按借款人分账时，分账来源只有全部，服务费，管理费，并且并且跟随对应的分账比例设置；
         * 同时债转服务费设置和出借人分公司字段不显示
         */
        ledgerTypeOnchangeAct: function () {
            var ledgerType = $("#type").val();
            var option = $("#source option[value='2']");//债转服务费
            // 清空分账来源选项
            $("#source").select2("val", "");
            // 清除校验结果
            Events.removeClass("#source");
            if (!ledgerType || ledgerType == "") {
                //$("#investorCompany").parent().parent().show();
                $("#creditRatio").parent().parent().show();
                Events.removeClass("#investorCompany", "*");
                return;
            } else if (ledgerType == 2) {//按借款人分账
                //$("#investorCompany").parent().parent().hide();
                $("#creditRatio").parent().parent().hide();
                Events.removeClass("#investorCompany", "*0-20", true);
                if (option.get(0)) {
                    // 存在“债转服务费”选项，移除该选项
                    option.remove();
                }
            } else {
                //$("#investorCompany").parent().parent().show();
                $("#creditRatio").parent().parent().show();
                Events.removeClass("#investorCompany", "*");
                if (!option.get(0)) {
                    // 不存在“债转服务费”选项，添加该选项
                    $("#source").append("<option value='2'>债转服务费</option>");
                }
            }

        },
        /**
         * 分账来源选择全部时，服务费分账比例、管理费分账比例可用；
         * 选择服务费时，管理费分账比例不可填写；
         * 选择管理费时，服务费分账比例不可填写
         */
        sourceTypeOnchangeAct: function () {
            var regular0 = "/^[0]$\|0\\.\\d{0,2}$/";// 正则表达式0-1的两位以内小数(可以为0)
            var regular = "/^0\\\.\\d{0,2}$/";// 正则表达式0-1的两位以内小数
            var sourceType = $("#source").val();
            var ledgerType = $("#type").val();
            var valid = window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
            // 清空分账来源选项
            $("#investorCompany").select2("val", "");
            // 根据分账来源选项判断需要填写的分账比例类型
            if (!sourceType || sourceType == "") {
                return;
            } else if (sourceType == "0") {//全部
                Events.removeClass("#serviceRatio", ledgerType == 2 ? regular : regular0);
                Events.removeClass("#creditRatio", ledgerType == 2 ? "*0-10" : regular0, ledgerType == 2);
                Events.removeClass("#manageRatio", ledgerType == 2 ? regular : regular0);
            } else if (sourceType == "1") {//服务费
                Events.removeClass("#serviceRatio", regular);
                Events.removeClass("#creditRatio", "*0-10", true);
                Events.removeClass("#manageRatio", "*0-10", true);
            } else if (sourceType == "3") {//管理费
                Events.removeClass("#serviceRatio", "*0-10", true);
                Events.removeClass("#creditRatio", "*0-10", true);
                Events.removeClass("#manageRatio", regular);
            } else if (sourceType == "2") {//债转服务费
                Events.removeClass("#serviceRatio", "*0-10", true);
                Events.removeClass("#creditRatio", regular);
                Events.removeClass("#manageRatio", "*0-10", true);
            }
            // 重新选择分账来源后清除已填信息
            $("#serviceRatio").val("");
            $("#creditRatio").val("");
            $("#manageRatio").val("");
        },
        /**
         * 全选按钮
         */
        checkOnchangeAct: function () {
            console.log(arguments);
            var $this = $(this);
            if ($this.attr("id") == "checkboxAll") {
                Events.checkAllOnchangeAct($this);
            } else {
                var checked = $this.is(':checked');
                // 取消选中其他按钮的情况下取消选中全选按钮
                //if (!checked) $this.parent().parent().find("#checkboxAll").removeAttr("checked");
                if (!checked) $this.parent().parent().find("#checkboxAll").prop("checked", false);
            }
        },
        /**
         * 全选按钮
         */
        checkAllOnchangeAct: function ($this) {
            var checked = $this.is(':checked');
            $this.parent().siblings("div").find("input").each(function () {
                $(this).prop("checked", checked);
                //if ($(this).is(':checked') != checked) {
                //$(this).click().attr("checked", checked);
                //}
            })
        },
        /**
         * 出借人分公司选择
         */
        investorCompanyOnchangeAct: function () {
            var investorCompanyId = $("#investorCompany option:selected").attr("investorCompanyId");
            $("#investorCompanyId").val(investorCompanyId);
        },
        /**
         * 移除样式，更换校验公式,设置是否只读
         * @param dom
         * @param datatype
         * @param readonly
         */
        removeClass: function (dom, datatype, readonly) {
            if (!dom) return;
            if (datatype) $(dom).attr("datatype", datatype);
            if (readonly) {
                $(dom).parent().parent().hide();
            } else {
                $(dom).parent().parent().show();
            }
            $(dom).attr("readonly", readonly ? readonly : false);
            $(dom).removeClass('Validform_error').siblings(".Validform_checktip").text("").removeClass('Validform_wrong').removeClass('Validform_right');
            $(dom).parent().parent('.form-group').removeClass("has-success").removeClass("has-error");
            //Events.addErrorClass(dom);
        },
        /**
         * 对校验错误的表单添加错误样式
         * @param dom
         * @param error
         */
        addErrorClass: function (dom, error) {
            var valid = window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
            if (!valid.check(false, dom) || error) {
                $(dom).addClass('Validform_error').siblings(".Validform_checktip").addClass('Validform_wrong').removeClass('Validform_right');
                $(dom).parent().parent('.form-group').removeClass("has-success").addClass("has-error");
                if (error) {
                    $(dom).parent().find(".Validform_checktip").text(error);
                }else{
                    $(dom).parent().find(".Validform_checktip").text($(dom).attr("errormsg"));
                }
            }
        }
    };

function checkAllRadio(fn) {
    var serviceRatio = $("#serviceRatio").val();
    var creditRatio = $("#creditRatio").val();
    var manageRatio = $("#manageRatio").val();
    var all = 0;
    var isZero = Array();
    if (serviceRatio) {
        var ratio = parseFloat(serviceRatio);
        all += ratio;
        ratio || isZero.push("#serviceRatio");
    }
    if (creditRatio) {
        var ratio = parseFloat(creditRatio);
        all += ratio;
        ratio || isZero.push("#creditRatio");
    }
    if (manageRatio) {
        var ratio = parseFloat(manageRatio);
        all += ratio;
        ratio || isZero.push("#manageRatio");
        ;
    }
    if (isZero.length >= 2) {
        return isZero;
    }
    if (all > 1) {
        return isZero.contains("#serviceRatio") ? ["#creditRatio"] : ["#serviceRatio"];
    }
    return "";
}

Array.prototype.contains = function (needle) {
    for (i in this) {
        if (this[i] == needle) return true;
    }
    return false;
}
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面的主键
    primaryKey: $("#id"),

    // 初始化画面事件处理
    initEvents: function () {
        // 确认按钮的点击事件
        $(".fn-Confirm").click(Events.confirmClkAct);
        // 取消按钮的点击事件
        $(".fn-Cancel").click(Events.cancelClkAct);
        // 用户名选择
        $("#username").change(Events.usernameOnchangeAct);
        // 分账类型选择
        $("#type").change(Events.ledgerTypeOnchangeAct);
        // 分账来源选择
        $("#source").change(Events.sourceTypeOnchangeAct);
        // 适用项目类型全选
        $("#checkboxGroup .checkbox input").change(Events.checkOnchangeAct);
        // 出借人分公司选择
        $("#investorCompany").change(Events.investorCompanyOnchangeAct);
    },
    // 画面布局
    doLayout: function () {
        // 初始化下拉框
        $(".form-select2").select2({
            allowClear: true,
            language: "zh-CN"
        })
    },
    // 画面初始化
    initialize: function () {
        // 执行成功后刷新画面
        ($("#success").val() && parent.Events.refreshClkAct())
        || Page.coverLayer();
        // 初始化表单验证
        Page.validate = Page.form.Validform({
            tiptype: 3
        });
    }
});
Page.initialize();
