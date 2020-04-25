$(function() {
    customForm.init();
    $("input.new-form-input").keyup(function() {
        if ($(this).valid()) {
            $(this).parent().removeClass("error");
        }
    });
    $("input:hidden").change(function() {
        if ($(this).valid()) {
            $(this).parent().removeClass("error");
        }
    });

});
//小数不足两位补零
function fixNum(f_x) {
    var s_x = f_x.toString();
    var pos_decimal = s_x.indexOf('.');
    if (pos_decimal < 0) {
        pos_decimal = s_x.length;
        s_x += '.';
    }
    while (s_x.length <= pos_decimal + 2) {
        s_x += '0';
    }
    return s_x;
}
Number.prototype.toFixed2 = function() {
    return parseFloat(this.toString().replace(/(\.\d{2})\d+$/, "$1"));
};
Number.prototype.toFixed4 = function() {
    return parseFloat(this.toString().replace(/(\.\d{4})\d+$/, "$1"));
};
$(function() {
    $("#yearRate").val(window.loadRate);
    //计算期次
    function getTerm() {
        return parseInt(years.val() || "0") * 12 + parseInt(month.val() || "0");
    }
    // x ^ y
    function power(x, y) {
        var t = x;
        while (y-- > 1) {
            t *= x;
        }
        return t;
    }
});
//计算器开始
//按日计算时改月为天
$("#ForGender li").click(function() {
        var type = $(this).prop("value");
        if (type == 1) {
            $(".month-day").text("天");
        } else {
            $(".month-day").text("月");
        }
    })
    //投资金额 输入框输入控制
$("#investMoney").keyup(function() {
    var _self = $(this);
    var val = _self.val();
    _self.val(val.replace(/[^0-9]*$/g, ''));
});
//投资期限 输入框输入控制
$("#month").keyup(function() {
    var _self = $(this);
    var val = _self.val();
    _self.val(val.replace(/[^0-9]*$/g, ''));
});
//收益率 输入框输入控制
$("#percent").keyup(function() {
    var _self = $(this);
    var val = _self.val();
    _self.val(val.replace(/[^0-9.]*$/g, ''));
});
//计算不同类型的收益
$("#formSubmit").click(function() {
    var val = parseInt($("#Gender").val()),
        investMoney = parseInt($("#investMoney").val() || "0"),
        percent = parseFloat($("#percent").val() || "0") / 100,
        month = parseInt($("#month").val() || "0"),
        interest;
    switch (val) {
        case 0:
            var interest = parseFloat((investMoney * (percent / 12)).toFixed4() * 10000 * month / 10000);
            var outcomeTotal = parseFloat(investMoney + interest);
            $(".outcomeTotal").text(fixNum(outcomeTotal.toFixed2()));
            $(".interst").text(fixNum(interest.toFixed2())); //利息总计
            $(".jisuanqi-table").hide(200);
            $(".jisuanqi-table table tr:not(.invite-table-header)").remove(); //删除除表头意外的tr
            $(".content-outcome").slideDown(200);
            break;
        case 1:
            var interest = parseFloat((investMoney * percent / 360).toFixed4() * 10000 * month / 10000);
            var outcomeTotal = parseFloat(investMoney + interest);
            $(".outcomeTotal").text(fixNum(outcomeTotal.toFixed2()));
            $(".interst").text(fixNum(interest.toFixed2())); //利息总计
            $(".jisuanqi-table").hide(200);
            $(".jisuanqi-table table tr:not(.invite-table-header)").remove();
            $(".content-outcome").slideDown(200);
            break;
        case 2: //先息后本
            $(".jisuanqi-table table tr:not(.invite-table-header)").remove();
            var interest = parseFloat((investMoney * percent / 12).toFixed4() * 10000 * month / 10000); //利息总和
            var monthInterst = parseFloat(investMoney * percent / 12); //每月利息
            var outcomeTotal = parseFloat(investMoney + interest); //本息总和
            var lastMonthTotal = parseFloat(monthInterst + investMoney); //最后一月
            $(".outcomeTotal").text(fixNum(outcomeTotal.toFixed2()));
            $(".interst").text(fixNum(interest.toFixed2())); //利息总计
            for (var i = 1; i < month; i++) {
                var str = "<tr><td>" + i + "</td><td>" + fixNum(monthInterst.toFixed2()) + "</td><td>0.00</td><td>" + fixNum(monthInterst.toFixed2()) + "</td></tr>";
                $(".jisuanqi-table table").append(str);
            }
            var str1 = "<tr><td>" + month + "</td><td>" + fixNum(lastMonthTotal.toFixed2()) + "</td><td>" + fixNum(investMoney.toFixed2()) + "</td><td>" + fixNum(monthInterst.toFixed2()) + "</td></tr>";
            $(".jisuanqi-table table").append(str1); //最后一行
            $(".content-outcome").slideDown(200);
            $(".jisuanqi-table").slideDown(200);
            break;
        case 3: //等额本息
            $(".jisuanqi-table table tr:not(.invite-table-header)").remove();
            var monthPer = (percent / 12).toFixed4(); //月利率
            //每月本息
            var monthMoneyInter = ((investMoney * monthPer * Math.pow((1 + monthPer), month)) / (Math.pow((1 + monthPer), month) - 1)).toFixed4();
            $(".outcomeTotal").text(fixNum((monthMoneyInter * 10000 * month / 10000).toFixed2()));
            var interest = 0;
            for (var i = 1; i < month + 1; i++) {
                //每月利息
                var monthInterst = parseFloat(investMoney * monthPer * (Math.pow(1 + monthPer, month) - Math.pow(1 + monthPer, i - 1)) / (Math.pow(1 + monthPer, month) - 1));
                //每月本金
                var monthMoney = parseFloat(investMoney * monthPer * Math.pow(1 + monthPer, i - 1) / (Math.pow(1 + monthPer, month) - 1));
                var str = "<tr><td>" + i + "</td><td>" + fixNum(monthMoneyInter.toFixed2()) + "</td><td>" + fixNum(monthMoney.toFixed2()) + "</td><td>" + fixNum(monthInterst.toFixed2()) + "</td></tr>";
                $(".jisuanqi-table table").append(str);
                interest = interest + monthInterst.toFixed2();
            }
            $(".interst").text(fixNum(interest.toFixed2())); //利息总计
            $(".content-outcome").slideDown(200);
            $(".jisuanqi-table").slideDown(200);
            break;
        case 4:
            $(".jisuanqi-table table tr:not(.invite-table-header)").remove();
            var monthPer = (percent / 12).toFixed4(); //月利率
            //每月本金
            var monthMoney = parseFloat(investMoney / month);
            var interest = 0;
            for (var i = 1; i < month + 1; i++) {
                //每月本息
                var monthMoneyInter = ((investMoney / month) + (investMoney - monthMoney * (i - 1)) * monthPer).toFixed2();
                //每月利息
                var monthInterst = ((investMoney - monthMoney * (i - 1)) * monthPer).toFixed2()
                var str = "<tr><td>" + i + "</td><td>" + fixNum(monthMoneyInter.toFixed2()) + "</td><td>" + fixNum(monthMoney.toFixed2()) + "</td><td>" + fixNum(monthInterst.toFixed2()) + "</td></tr>";
                $(".jisuanqi-table table").append(str);
                interest += monthInterst.toFixed4();
            }

            $(".interst").text(fixNum(interest.toFixed2())); //利息总计
            $(".outcomeTotal").text(fixNum((interest.toFixed2() + investMoney).toFixed2()));
            $(".content-outcome").slideDown(200);
            $(".jisuanqi-table").slideDown(200);
            break;

    }
});
