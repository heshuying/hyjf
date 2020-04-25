
//引导页 翻页
function findNext(){
	var $findNext = $(".findNext");
	var next = function(){
		var _this = $(this);
		var $thisForm =_this.parents(".findForm");
		var $thisDiv = _this.parents(".newRegContent ");
		var $findPos = $(".findPos");
		var $topIndex = $thisForm.index();
		var $newRegInput = $(".newRegInput");
		var $phoneLength = $newRegInput.val().length;//手机号长度
		var $dragBgWidth = $(".drag_bg").width();//drag宽度
		var $dragInput = $("#dragInput") //drag input 验证
		var $newRegInputLength = $("#newRegVerify").val().length;//
		var $setPwd = $(".findSetPwd").val();
		var $confirmPwd = $(".findConfirmPwd").val();
		if($topIndex===2){
			if($phoneLength==11&&$dragBgWidth>270&&$dragInput.prop("checked")==true){
				findShow()
			}
		}
		if($topIndex===3){
			if($newRegInputLength==4){
				findShow()
			}
		}
		if($topIndex===4){
			if(($setPwd==$confirmPwd)&&$setPwd.length>4&&$confirmPwd.length>4){
				findShow()
			}
		}
		//隐藏 显示 下一步  当前位置显示
		function findShow(){
			$findPos.eq($topIndex-1).addClass("askCurPos").siblings().removeClass("askCurPos")
			$thisDiv.hide(500);
			$thisForm.next().find(".newRegContent ").show(500);
			var $phoneNum = $newRegInput.val();
			$(".phoneNum").text($phoneNum);
			//手机号格式化
			var numA = $phoneNum.substring(0,3);
			var numB = $phoneNum.substring(3,7);
			var numC = $phoneNum.substring(7);
			var numFormat = numA+"-"+numB+"-"+numC;
			$(".phoneNum").text(numFormat);
		}
	}
	$findNext.on("click",next);
	//点击返回修改
	$(".findChangeNum").on("click",function(){
		$(this).parents(".newRegContent").hide(500);
		$(this).parents(".findForm").prev().find(".newRegContent").show(500);
		$(".findPos").eq(0).addClass("askCurPos").siblings().removeClass("askCurPos");
	})
	//账户提现 计算实际到账金额
	var $widInput = $(".widInput");//提现金额的juqery对象
	var $widWithdrawFees = $(".widWithdrawFees").text();//提现费用
	var $widAvailableAmount = parseFloat($("#total").val().replace(/,/g, ""));//当前可用金额格式化转为浮点型
	var keypressHandler = function(){
		var $widInput_val = $widInput.val();//输入的提现金额
		var realMoney = parseFloat($widInput.val()-$widWithdrawFees);//输入的提现金额-提现费用
		
//		if(realMoney<0){
//			$(".widRealMount").text('0.00');
//		}else{
//			$(".widRealMount").text(realMoney.toFixed(2));
//		}
		
//		实际到账金额的显示（现在的判断是：实际到账金额=提现金额-提现费用）
		if(realMoney<0){
			$(".widRealMount").text('0.00');
		}else if(($widAvailableAmount-$widInput_val)<1){
			$(".widRealMount").text(realMoney.toFixed(2));
		}else{
			var a = parseFloat($widInput_val);
			$(".widRealMount").text(realMoney.toFixed(2));
//			$(".widRealMount").text(parseFloat($widInput_val).toFixed(2));
		}
		
	}
	$widInput.on("keyup",keypressHandler)
}
findNext()