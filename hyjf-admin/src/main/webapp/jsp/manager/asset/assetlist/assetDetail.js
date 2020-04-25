// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		$("#qrcode").each(function() {
			$(this).qrcode({
				text: $("#qrcodeValue").val(),
				render: "canvas", 
				width: 128, height: 128,
				typeNumber: -1, 
				correctLevel: QRErrorCorrectLevel.H,
				background: "#ffffff",
				foreground: "#000000"
			})
		});
	}
});
