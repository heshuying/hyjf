"use strict";
var customForm = (function() {
    return {
        init: function() {
            this.onPageReady();
            this.select();
            this.radio();
            this.dateSelection();
            this.formStyle();
        },
        onPageReady: function() {
            $(".more-new-form-open .closed").click(function() {
                $(this).parent().next(".more-new-form").slideDown(300);
                $(this).hide();
                $(this).siblings(".opened").show();
            })
        },
        formStyle:function(){
        	$("input.new-form-input").focus(function(){
        		$(this).parent(".new-form-item").addClass("focus");
        	}).blur(function(){
        		$(this).parent(".new-form-item").removeClass("focus");
        	});
        },
        select: function() {
            /*
             * 自定义select菜单
             */
            var customSelect = function() {
                    //模拟select 选择option赋值
                    $(".new-form-hdselect").click(function(e) {
                        if (!$(e.target).is("li")) {
                            return false;
                        };
                        var _self = $(e.target); //选项
                        var ul = _self.parent("ul");
                        var select = ul.parent(".new-form-hdselect");
                        var hid = $("#" + ul.attr("for")); //存值的隐藏input
                        selectOption(select, hid, _self);
                    });
                    //点击select
                    $("h4.new-form-input").click(function() {
                        var _self = $(this); //显示文字
                        var select = _self.next(".new-form-hdselect"); //select菜单
                        if (select.is(":hidden")) {
                            openSelect(select);
                        } else {
                            closeSelect(select);
                        }
                    });
                    //点击页面其他部分隐藏select
                    $(document).click(function(e) {
                        var item = e.target;
                        var select = $(".new-form-hdselect:visible");
                        if (!$(item).parents(".new-form-hdselect").length && !$(item).hasClass("new-form-hdselect") && !$(item).is("h4.new-form-input")) {
                            closeSelect(select);
                        }
                    });
                }
                //选择option
            var selectOption = function(select, hidden, item) {
                var value = item.attr('value');
                var txt = item.text();
                // 赋值
                hidden.val(value);
                select.prev("h4").text(txt);
                //关闭
                closeSelect(select);
            };

            //关闭select
            var closeSelect = function(select) {
                select.slideUp(100);
                select.siblings("input:hidden").trigger("change");
                select.parent(".new-form-item").removeClass("focus");
            };

            //打开select
            var openSelect = function(select) {
                var opened = $(".new-form-hdselect:visible");
                if (opened.length) {
                    closeSelect(opened);
                }
                select.slideDown(100);
                select.parent(".new-form-item").addClass("focus");
            }
            return customSelect();
        },
        radio: function() {
            /*
             * 自定义单选按钮组
             */
            var radioGroup = $(".new-radio-group");
            radioGroup.click(function(e) {
                var _self = $(e.target);
                if (!$(_self).hasClass('radio-item')) {
                    return false;
                }
                _self.siblings(".radio-item.checked").removeClass("checked");
                _self.addClass("checked");
                _self.siblings("input:hidden").val(_self.attr('value'));
                _self.siblings("input:hidden").trigger("change");
            });

        },
        citySelection: function(cities) {


            var _bindProvince = function (cities) {
                /*
                 * 绑定省份
                 * @parma cities json 城市数据
                 */
                var list = "";
                for (var i = 0; i < cities.length; i++) {
                    list += '<li value="' + cities[i].name + '">' + cities[i].name + '</li>';
                }
                $("#ForProvince").html(list);
                $("#ForProvince").children("li").click(function() {
                    var province = $(this).attr("value");
                    _bindCity(cities, province)
                    $("#ForCity").children().eq(0).trigger("click");
                })
            }

            var _bindCity = function (cities, province) {
                /*
                 *  绑定省份
                 * @parma cities json 城市数据
                 * @param province str 省份名称
                 */
                var list = ""; //保存city html
                var obj = __getData(cities, province); //保存城市对象
                var type = obj.type; //保存城市类型 {0:市,1:省}
                var data; //保存城市对象
                var defaultCity = null;
                if (type === 1) {
                    data = obj.sub;
                } else {
                    // data = [cities[0], obj]; //有默认“请选择”的数据使用
                    data = [obj];
                }
                for (var i = 0; i < data.length; i++) {
                    list += '<li value="' + data[i].name + '">' + data[i].name + '</li>';
                }
                $("#ForCity").html(list);

                __setDefault("ForCity");

                $("#ForCity").children("li").click(function() {
                    var county = $(this).attr("value");
                    _bindCounty(data, county);
                    $("#ForCounty").children().eq(0).trigger("click");
                })


            }

            var _bindCounty = function (cities, county) {
                /*
                 *  绑定区县
                 * @parma cities json 城市数据
                 * @param county str 区县名称
                 */
                var list = ""; //保存city html
                var obj = __getData(cities, county); //区县对象
                var data;
                if(obj.sub && obj.sub.length){
                	data = obj.sub
                }else{
                	data = [cities[0]];
                }
                var defaultCounty = null;


                for (var i = 0; i < data.length; i++) {
                    list += '<li value="' + data[i].name + '">' + data[i].name + '</li>';
                }
                $("#ForCounty").html(list);

                __setDefault("ForCounty");
            }

            var __getData = function (data, name) {
                //根据名字获取列表 
                for (var i = 0; i < data.length; i++) {
                    if (data[i].name === name) {
                        return data[i];
                    }
                }
            }

            var __setDefault = function (id) {
                /*
                 * 设置默认文字 和 默认值
                 */
                $("#" + id).children("li").eq(0).trigger("click");
            }

            return _bindProvince(cities);

        },
        dateSelection: function() {
            var date = new Date();
            var year = date.getFullYear(); //获取当前年份
            $('#ForYear').click(function(e){
            	if($(e.target).is("li")){
            		setMonth();
            	}
            })
            $('#ForMonth').click(function(e) {
            	if($(e.target).is("li")){
            		var month =$(e.target).attr("value");
            		setDate();
	                monthChange(month);
            	}
            })
            var setYear = function (){
            	var dropList="";
	            for (var i = year; i > 1945; i--) {
	                // if (i == year) {
	                //     $("#Year").val(i);
	                // }
	                dropList += "<li value='" + i + "'>" + i + "</option>";
	            }
            	$('#ForYear').html(dropList); //生成年份下拉菜单
            }
            
            var setMonth = function (){
            	var monthly="";
	            for (var month = 1; month < 13; month++) {
	                monthly += "<li value='" + month + "'>" + month + "</li>";
	            }
	            $('#ForMonth').html(monthly); //生成月份下拉菜单
	            $('#ForMonth').children().eq(0).trigger("click");
            }
            var setDate = function (){
            	var dayly="";
	            for (var day = 1; day <= 31; day++) {
	                dayly += "<li value='" + day + "'>" + day + "</li>";
	            }
	            $('#ForDate').html(dayly); //生成天数下拉菜单
            }
            /*
             * 月份变化更新日
             */
            var monthChange = function monthChange(month){
            	var currentDay="";
                var year = $('#year').val();
                var currentMonth = month;
                var total;
                switch (currentMonth) {
                    case "1":
                    case "3":
                    case "5":
                    case "7":
                    case "8":
                    case "10":
                    case "12":
                        total = 31;
                        break;
                    case "4":
                    case "6":
                    case "9":
                    case "11":
                        total = 30;
                        break;
                    case "2":
                    	//判断闰年
                        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                            total = 29;
                        } else {
                            total = 28;
                        }
                    default:
                        break;
                }
                for (var day = 1; day <= total; day++) {
                    currentDay +=  "<li value='" + day + "'>" + day + "</li>";
                }
                $('#ForDate').html(currentDay); //生成日期下拉菜单
                $('#ForDate').children().eq(0).trigger("click");
            }
            return setYear();
        }
    }

}())