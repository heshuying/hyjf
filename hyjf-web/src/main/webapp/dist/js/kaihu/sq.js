$(function() {
    $(".kaihu-input").focus(function() {
        $(this).parent(".kh-item").addClass("focus");
    }).blur(function() {
        $(this).parent(".kh-item").removeClass("focus");
    });

    function verIsMobile(value) {
        //验证是否是手机号
        var length = value.toString().length;
        return /^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value);
    }
    var getver = function() {

        var auth_url = " "; //获取验证码的url地址
        var remainingTime = 60; //倒计时时间
        var timerClock = null;

        //发送验证码 校验手机号 手机号校验成功后才能发送手机号
        var khPhoneNum = $('#sqPhone').val();
       // var checkVer = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-3,5-9]{1})|(17[0,1,3,5-8]{1})|(18[0-9]{1}))+\d{8})$/.test(khPhoneNum);
        //开始计时器
        if (verIsMobile(khPhoneNum)) {
            startTimerClock();
            $.ajax({
                type: "POST",
                url: auth_url,
                data: 'khPhone=' + khPhoneNum,
                success: function(msg) {
                    if (msg != "") {
                        endTimerClock();
                        utils.alert({content:'出错了'});
                    } else {
                        utils.alert({content:'短信发送成功！'});
                    }
                }
            });
        } else {
            utils.alert({content:'出错了，请刷新页面'});
        }

        function startTimerClock() {
            clearTimeout(timerClock);
            if (remainingTime <= 0) {
                endTimerClock();
                return;
            }
            $(".get-btn").attr('disabled', 'disabled').val("剩余" + remainingTime + "秒");
            timerClock = setTimeout(function() {
                startTimerClock();
                remainingTime--
            }, 1000);
        };
        //结束计时器
        function endTimerClock() {
            clearTimeout(timerClock);
            isTimeing = false; //重置倒计时状态
            remainingTime = 60;
            $(".get-btn").removeAttr('disabled').val("获取");;
        };
    }

    $(".get-btn").on("click", getver);
});

$().ready(function() {
    // 在键盘按下并释放及提交后验证提交表单
    $("#khForm").validate({
        errorElement: "span",
        rules: {
            //	      验证码验证
            khVerify: {
                required: true,
                maxlength: 4,
                minlength: 4
            },
            khAgree: "required"
        },
        messages: {
            khVerify: {
                required: "请输入验证码",
                minlength: "验证码不能小于 4个字母",
            },
            khAgree: "需同意协议",
        },
        submitHandler: function(form) { //通过之后回调
            //进行ajax传值
            $.ajax({
                url: " ",
                type: "post",
                dataType: "json",
                data: $('#khForm').serialize(),
                success: function(msg) {
                //要执行的代码
                }
            });
        }
    });
});
