// --------------------------------------------------------------------------------------------------------------------------------
// 二维码生成工具
$(function() {
	var codeWidth = $(".friend-code-div").width();
	$(".friend-code-div").height(codeWidth);
	$("#qrcode").each(function() {
		$(this).qrcode({
			text: $("#qrcodeValue").val(),
			render: "canvas", 
			width: codeWidth, height: codeWidth, 
			typeNumber: -1, 
			correctLevel: QRErrorCorrectLevel.H,
			background: "#ffffff",
			foreground: "#000000"
		})
	});
})
