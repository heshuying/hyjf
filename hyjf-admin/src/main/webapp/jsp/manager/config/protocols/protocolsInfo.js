var
	/* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 联动下拉事件
        protocolTypeAction : "protocolTypeAction"
    },
	/* 事件动作处理 */
    Events = {
        // 确认按键单击事件绑定
        confirmClkAct : function() {
            if(Page.validate.check(false)) {
                Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
                    if(isConfirm) {
                        Page.submit();
                    }
                })
            }
        },
        // 取消按键单击事件绑定
        cancelClkAct : function() {
            parent.$.colorbox.close();
        },

        // 协议类型选择事件绑定
        protocolTypeOnchangeAct : function() {
            var protocolType = $("#protocolType").val();
            $("#templetId").val("");
            Page.validate.check(false,"#protocolType");
            if (protocolType != "") {
                $.ajax({
                    url : Action.protocolTypeAction,
                    type : "POST",
                    text : "",
                    async : true,
                    data :  {
                        protocolType : protocolType
                    },
                    success : function(data) {
                        $("#protocolType").select2({
                            width : 268,
                            placeholder : "全部",
                            allowClear : true,
                            language : "zh-CN"
                        });
                        $("#templetId").val(data);
                    },
                    error : function(err) {
                        Page.alert("","协议模板编号取得失败！");
                    }
                });
            }

        }
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 初始化画面事件处理
    initEvents : function() {
        // 确认按键单击事件绑定
        $(".fn-Confirm").click(Events.confirmClkAct);
        $(".fn-Cancel").click(Events.cancelClkAct);
        $(".fn-Upload").click(Events.uploadClkAct);

        // 协议类型选择事件绑定
        $("#protocolType").change(Events.protocolTypeOnchangeAct);

        // 协议模板上传
        $('#fileupload').fileupload({
            url : "uploadFile",
            autoUpload : true,
            dataType: 'json',
            formData:{templetId:$("#templetId").val()},
            //设置进度条
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100);
                $('#progress .bar').css(
                    'width',
                    progress + '%'
                );

            },
            // add: function (e, data) {
            //     $("#fileuploadmsg").val("上传中。。。");
            // },
            done : function(e, data) {
                $("#fileuploadmsg").text(data.result.code+" "+data.result.msg);
                $("#fileUrl").val(data.result.fileUrl);
                if (data.result.result=="success" || data.result.msg.indexOf("模板编号重复") > 0 ){
                    Page.alert("","上传完成！");
                    $("#caFlag").val(1);
                    // Page.validate.check(false,"#caFlag");
                }
            }
        });

        //文件上传前触发事件
        $('#fileupload').bind('fileuploadsubmit', function (e, data) {
            data.formData = { templetId:$("#templetId").val() };  //如果需要额外添加参数可以在这里添加
        });
    },
    // 画面初始化
    initialize : function() {
        // 执行成功后刷新画面
        ($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
        $("#protocolType").select2({
            width : 268,
            placeholder : "全部",
            allowClear : true,
            language : "zh-CN"
        });
        // 初始化表单验证
        Page.validate = Page.form.Validform({
            tiptype: 3
        });
    }
}),

    Page.initialize();
