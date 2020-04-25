//设置唯一active
function uniqueClass(element,className){
	element.parent().children("."+className).removeClass(className);
	element.addClass(className);
}
$(".acc-set-tab li").on("click",function(){
	var index = $(this).index();
	uniqueClass($(this),"tab-active");
	uniqueClass($(".acc-set-item-li").eq(index),"acc-set-active");
});

//图片裁剪
var $image = $('#image')
var options = {
    aspectRatio: 1 / 1,
    preview: '.img-preview',
    crop: function (e) {
      
    }
  };
var originalImageURL = $image.attr('src');
var uploadedImageURL;
  // Cropper
  $image.on({
    ready: function (e) {
      console.log(e.type);
    },
    cropstart: function (e) {
      console.log(e.type, e.action);
    },
    cropmove: function (e) {
      console.log(e.type, e.action);
    },
    cropend: function (e) {
      console.log(e.type, e.action);
    },
    crop: function (e) {
      console.log(e.type, e.x, e.y, e.width, e.height, e.rotate, e.scaleX, e.scaleY);
    },
    zoom: function (e) {
      console.log(e.type, e.ratio);
    }
  }).cropper(options);
 // Methods
  $('.acc-set-avatar-1-upload').on('click', '[data-method]', function () {
    var $this = $(this);
    var data = $this.data();
    var $target;
    var result;

    if ($this.prop('disabled') || $this.hasClass('disabled')) {
      return;
    }

    if ($image.data('cropper') && data.method) {
      data = $.extend({}, data); // Clone a new one

      if (typeof data.target !== 'undefined') {
        $target = $(data.target);

        if (typeof data.option === 'undefined') {
          try {
            data.option = JSON.parse($target.val());
          } catch (e) {
            console.log(e.message);
          }
        }
      }
      result = $image.cropper(data.method, data.option, data.secondOption);
      switch (data.method) {
      	//保存按钮
        case 'getCroppedCanvas':
          if (result) {
			var image = new Image(); 
			// canvas.toDataURL 返回的是一串Base64编码的URL，当然,浏览器自己肯定支持 
			// 指定格式 PNG 
			$("#test").prop("src",result.toDataURL("image/png"))
			imageData = result.toDataURL("image/png").substr(22); 
			 $.ajax( {  
	            url: webPath + '/user/safe/uploadAvatarAction.do',  
	            dataType:'json',  
	            type: "POST",  
	            data: {"image":imageData},  
	            success: function (data) {  
	            	utils.alert({
						id:"getCroppedCanvasTip",
						content:data.statusDesc,
						fnconfirm:function(){
							window.location.href = window.location.href;
						}
					});
	            },  
	            error: function (data) {  
	            	utils.alert({
						id:"getCroppedCanvasTip",
						content:"头像修改失败,请刷新重试！",
						fnconfirm:function(){
							window.location.href = window.location.href;
						}
					});
	            }  
	         });  
			
          }

          break;

        case 'destroy':
          if (uploadedImageURL) {
            URL.revokeObjectURL(uploadedImageURL);
            uploadedImageURL = '';
            $image.attr('src', originalImageURL);
          }

          break;
      }

      if ($.isPlainObject(result) && $target) {
        try {
          $target.val(JSON.stringify(result));
        } catch (e) {
          console.log(e.message);
        }
      }

    }
  });
 //点击选择图片上传图片
 $("#inputImage1").change(function(){
	var files = this.files;
	var file;
    if (!$image.data('cropper')) {
	    return;
	 }
	
	if (files && files.length) {
	    file = files[0];
	
	    if (/^image\/\w+$/.test(file.type)) {
	      if (uploadedImageURL) {
	        URL.revokeObjectURL(uploadedImageURL);
	      }
	      uploadedImageURL = URL.createObjectURL(file);
	      $image.cropper('destroy').attr('src', uploadedImageURL).cropper(options);
	      $inputImage.val('');
	      $(".acc-set-avatar-1").hide();
	      $(".acc-set-avatar-2").show();
	      $(".upload-error").addClass("hidden")
	    } else {
	    	$(".upload-error").removeClass("hidden")
	    }
	 }
 })
   // Import image
  var $inputImage = $('.inputImage');

  if (URL) {
    $inputImage.each(function(){
    	$(this).change(function () {
	      var files = this.files;
	      var file;
	
	      if (!$image.data('cropper')) {
	        return;
	      }
	
	      if (files && files.length) {
	        file = files[0];
	
	        if (/^image\/\w+$/.test(file.type)) {
	          if (uploadedImageURL) {
	            URL.revokeObjectURL(uploadedImageURL);
	          }
	
	          uploadedImageURL = URL.createObjectURL(file);
	          $image.cropper('destroy').attr('src', uploadedImageURL).cropper(options);
	          $inputImage.val('');
	           $(".upload-error").addClass("hidden");
	        } else {
	          $(".upload-error").removeClass("hidden");
	        }
	      }
	    })
    });
  } else {
    $inputImage.prop('disabled', true).parent().addClass('disabled');
  }

  
// 从 canvas 提取图片 image 
function convertCanvasToImage(canvas) { 
//新Image对象，可以理解为DOM 
var image = new Image(); 
// canvas.toDataURL 返回的是一串Base64编码的URL，当然,浏览器自己肯定支持 
// 指定格式 PNG 
image.src = canvas.toDataURL("image/png"); 
return image; 
} 
