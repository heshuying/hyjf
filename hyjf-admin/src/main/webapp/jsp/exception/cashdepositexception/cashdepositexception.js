var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 页面查询
        searchAction : "searchAction",
        //保证金不足处理
        modifyAction : "ToModifyAction"
    },
    /* 事件动作处理 */
    Events = {
        // 重置表单
        resetFromClkAct : function() {
            $(".form-select2").val("").trigger('change');
        },
        // 刷新按钮的单击动作事件
        refreshClkAct : function() {
            //window.location.reload();
            $("#mainForm").attr("target", "");
            $("#paginator-page").val(1), Page.submit(Action.searchAction);
        },
        // 查找按钮的单击事件绑定
        searchClkAct : function(selection, cds) {
            $("#mainForm").attr("target", "");
            $("#paginator-page").val(1), Page.submit(Action.searchAction);
        },
        //全选按钮
        selectAllAct : function(){
            $(".listCheck").prop("checked", this.checked);
        },
        // 刷新按钮的单击动作事件
        refreshClkAct : function() {
            window.location.reload();
        },


        modifyClkAct : function () {
            Page.primaryKey.val($(this).data("assetid"));

            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 合作额度不足处理",
                width: 350, height: 230,
                inline: true,  fixed: true, returnFocus: false, open: true,
                // Open事件回调
                onOpen: function() {
                    setTimeout(function() {
                            Page.form.attr("target", "dialogIfm").attr("action", Action.modifyAction).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function() {
                    window.location.reload();
                    Page.form.attr("target", "");
                }
            })
        },
        //批量处理数据
        batchModifyAct : function(){
            var assetid="";
            if($('input:checkbox[name=assetid]:checked').get(0)!=null){
                $('input:checkbox[name=assetid]:checked').each(function(i){
                    assetid+=$(this).val()+",";
                });
                Page.primaryKey.val(assetid);
                $.colorbox({
                    overlayClose: false,
                    href: "#urlDialogPanel",
                    title: "<i class=\"fa fa-plus\"></i> 合作额度不足处理",
                    width: 350, height: 230,
                    inline: true,  fixed: true, returnFocus: false, open: true,
                    // Open事件回调
                    onOpen: function() {
                        setTimeout(function() {
                            Page.form.attr("target", "dialogIfm").attr("action", Action.modifyAction).submit();
                        }, 0)
                    },
                    // Close事件回调
                    onClosed: function() {
                        window.location.reload();
                        Page.form.attr("target", "");
                    }
                })
            }else{
                Page.alert("","请选择一条数据!");
            }
            },
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
//	// 画面的主键
	primaryKey : $("#assetId"),
    // 画面布局
    doLayout : function() {
        // 条件下拉框
        $(".form-select2").select2({
            width : 268,
            placeholder : "全部",
            allowClear : true,
            language : "zh-CN"
        }),
            // 日历选择器
            $('#start-date-time').datepicker({
                autoclose: true,
                todayHighlight: true,
                endDate:new Date()
            });
        $('#end-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        var day30 = 30 * 24 * 60 * 60 * 1000;
        $("#start-date-time").on("changeDate", function(ev) {
            var now = new Date();
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#end-date-time').datepicker("getDate");
            var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
            $('#end-date-time').datepicker("setStartDate", selectedDate);
            $('#end-date-time').datepicker("setEndDate", finalEndDate);
            //如果end值范围超过30天，设置end最大结束时间
            if (endDate != null && (+endDate - selectedDate >= day30)) {
                $('#end-date-time').datepicker("setDate", finalEndDate);
            }
        });
        if($("#start-date-time").val() != ''){
            $('#start-date-time').datepicker("setDate", $("#start-date-time").val());
        }
    },
    // 初始化画面事件处理
    initEvents : function() {
            // 边界面板按钮的单击事件绑定
            $(".fn-searchPanel").click(Events.searchPanelClkAct),
            // 重置表单
            $(".fn-Reset").click(Events.resetFromClkAct),
            // 刷新按钮的单击事件绑定
            $(".fn-Refresh").click(Events.refreshClkAct),
            // 查找按钮的单击事件绑定
            $(".fn-Search").click(Events.searchClkAct),
            // 清空按钮的单击事件绑定
            $(".fn-ClearForm").click(Events.clearClkAct);
            //批量处理
            $(".fn-Add").click(Events.batchModifyAct);

            $(".fn-Modify").click(Events.modifyClkAct);
            // SelectAll
            $("#checkall").change(Events.selectAllAct);
    }
});

