var
// --------------------------------------------------------------------------------------------------------------------------------
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
        // 显示|隐藏 H5,原生切换
        // @param b Boolean true:显示  false:隐藏
        displayH5:function(b){
            if(b){
                $(".h5-class").show();
                // 取消忽略验证
                // 跳转内容
                var unignoreItems = $(".h5-class:visible").find("input:visible,textarea:visible");
                Page.validate.unignore(unignoreItems);
                if($("input[name=jumpContent]:checked").val() == '1'){
                    Events.displayCustom(true)
                }
            }else{
                $(".h5-class").hide();
                var ignoreItems = $(".h5-class:hidden").find("input:hidden,textarea:hidden");
                Events.displayCustom(false)
                Page.validate.ignore(ignoreItems);
            }
        },
        // 显示|隐藏 url 自定义切换
        // @param b Boolean true:显示  false:隐藏
        displayCustom:function(b){
            if(b){
                // 显示自定义
                $(".custom-class").show();
                $(".url-class").hide();
                var ignoreItems = $(".url-class").find("input,textarea");
                var unignoreItems = $(".custom-class").find("input,textarea");
                // 添加 自定义 验证
                Page.validate.unignore(unignoreItems);
                // 取消url验证
                Page.validate.ignore(ignoreItems);
            }else{
                // 隐藏自定义 显示url
                $(".custom-class").hide();
                $(".url-class").show();
                var ignoreItems = $(".custom-class").find("input,textarea");
                var unignoreItems = $(".url-class").find("input,textarea");
                //添加url验证
                Page.validate.unignore(unignoreItems);
                // 取消自定义验证
                Page.validate.ignore(ignoreItems);
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
        // 图片上传
        $('#fileupload').fileupload({
            url : "uploadFile",
            autoUpload : true,
            done : function(e, data) {
                var file = data.result[0];
                $("#imgurl").val(file.imagePath);
            }
        });
        // 原生 \ h5 类别切换
        $("input[name=jumpType]").change(function(){

            var val = $(this).val();   // 0:原生 1:h5
            Events.displayH5(val == '1')
        })
        // h5 url \自定义 方式切换
        $("input[name=jumpContent]").change(function(){
            var val = $(this).val();   // 0:url 1:自定义
            Events.displayCustom(val == '1')
        })


    },
    // 画面布局
    doLayout: function() {
        // 日历选择器
        $('.datepicker').datepicker({
            autoclose: true,
            todayHighlight: true
        });

        // 初始化富文本编辑域
        $('textarea.tinymce').tinymce({
            // Location of TinyMCE script
            script_url: themeRoot + '/vendor/plug-in/tinymce/tinymce.min.js',
            language_url : themeRoot + '/vendor/plug-in/tinymce/langs/zh_CN.js',

            height: 260,

            //theme: "modern",
            plugins: [
                "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",
                "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
                "save table contextmenu directionality emoticons template paste textcolor colorpicker textpattern"
            ],
            external_plugins: {
                //"moxiemanager": "/moxiemanager-php/plugin.js"
            },

            // Example content CSS (should be your site CSS)
            //content_css: "css/development.css",
            add_unload_trigger: false,

            toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | print preview media fullpage | forecolor backcolor emoticons table",

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
    // 画面初始化
    initialize : function() {
        // 执行成功后刷新画面
        // ($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
        ($("#success").val() && Page.submit("searchAction")) || Page.coverLayer();
        // 初始化表单验证
        Page.validate = Page.form.Validform({
            tiptype: 3
        });

        if($("#mainForm").attr("action") == "insertAction"){
            // 添加
            Events.displayCustom(false)
            Events.displayH5(false)

        }else if ($("#mainForm").attr("action") == "updateAction"){
            // 修改
            // 跳转类型
            var jumpTypeFlg = $("input[name=jumpType][checked]").val() == "0" ? false : true; //0 原生  1 h5
            // 跳转内容
            var jumpContentFlg = jumpTypeFlg ? ($("input[name=jumpContent][checked]").val() == "0" ? false : true)  : false;
            Events.displayCustom(jumpContentFlg)
            Events.displayH5(jumpTypeFlg)
        }

    }
}),

    Page.initialize();
