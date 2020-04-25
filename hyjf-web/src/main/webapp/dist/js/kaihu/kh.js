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

        var auth_url = " "; //获取注册码的url地址
        var remainingTime = countDown; //倒计时时间
        var timerClock = null;

        //发送验证码 校验手机号 手机号校验成功后才能发送手机号
        var khPhoneNum = $('#khPhone').val();
        //var checkVer = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-3,5-9]{1})|(17[0,1,3,5-8]{1})|(18[0-9]{1}))+\d{8})$/.test(khPhoneNum);
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
            utils.alert({content:'请输入正确的手机号！'});
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
                remainingTime--;
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

    $(".get-btn").on("click", function(){
        if($("#khPhone").valid()){
            getver();
        }
    });
});

$().ready(function() {
    // 在键盘按下并释放及提交后验证提交表单
    $("#khForm").validate({
        errorElement: "span",
        rules: {
            khAgree: "required",
            //	      用户名验证
            khName: {
                required: true,
                userName: true,
            },
            //	      身份证验证
            khIdcard: {
                required: true,
                isIdCard: true,
            },
            //	      电话号码验证
            khPhone: {
                required: true,
                number: true,
                isMobile: true,
                minlength: 11,
                maxlength: 11
            },
            //	      银行卡验证
            khCredit: {
                required: true,
                isBankCard: true,
            },
            //	      验证码验证
            khVerify: {
                required: true,
                maxlength: 4,
                minlength: 4
            },
            agree: "required"
        },
        messages: {
            khName: {
                required: "请输入用户名",
                userName: "用户名只能包括中文字、英文字母、数字和下划线"
            },
            khIdcard: {
                required: "请输入身份证号",
            },
            khCredit: {
                required: "请输入银行卡号",
            },
            khPhone: {
                required: "请输入手机号",
                minlength: "手机号不能小于11位",
                number: "请输入数字"
            },
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
                data: {
                    name: $("#khName").val(),
                    idcard: $("#khIdcard").val(),
                    credit: $("#khCredit").val(),
                    phone: $("#khPhone").val(),
                    verify: $("#khVerify").val()
                },
                success: function(msg) {
                //要执行的代码
                }
            });
        }
    });
});
