$(function(){
	//打开礼盒
	var giftFlag=true
	$('.gift').click(function(){
		if(giftFlag){
			giftFlag=false
			$.ajax({
				type:"post",
				dataType:'json',
				url:webPath + "/activity/list2018/actone.do",
				success:function(data){
					if(!data.status){
						$('.close-gift').click()
						utils.alert({id:'error',title:'打开失败',content:data.msg});
						giftFlag=true
					}else{
						$('.gift-detail div span').text(data.reward)
						$('.gift-box').hide()
						$('.gift-detail').show()
						giftFlag=true
					}
				},
				error:function(){
					$('.close-gift').click()
					utils.alert({id:'error',title:'打开失败',content:'打开失败，请刷新页面重试'});
				}
			})
		}
	})
	//关闭弹层
	$('.close-gift').click(function(){
		$('#shadowBg').hide()
	})
	$('#notStarted .confrim-btn').click(function(){
		$('#notStarted').hide()
	})
	//点击领取
	$('.login.receive').click(function(){
		$('#shadowBg').show()
	})
	//活动tab切换
	$('.active-tab .tab-btn').each(function(index,el){
		$(el).click(function(){
			$('.active-tab .tab-btn').removeClass('select')
			$(this).addClass('select')
			$(".active-content").hide()
			$(".active-content:eq("+index+")").show()
			scroll[0].setSize();
		})
	})
	//榜单滚动条
	var scroll=[];
	scroll[0] = new MyScrollBar({
	    selId: 't1',
	    enterShow:false
	});
	scroll[1] = new MyScrollBar({
	    selId: 't2',
	    enterShow:false
	});
	scroll[2] = new MyScrollBar({
	    selId: 't3',
	    enterShow:false
	});
	scroll[3] = new MyScrollBar({
	    selId: 't4',
	    enterShow:false
	});
	scroll[4] = new MyScrollBar({
	    selId: 't5',
	    enterShow:false
	});
	//活动三榜单tab切换
	$('.table-bg .table-tab>div').each(function(index,el){
		$(el).click(function(){
			$('.table-bg .table-tab>div').removeClass('select')
			$(this).addClass('select')
			$(".rank-content").hide()
			$(".rank-content:eq("+index+")").show()
			var isTr=$(".rank-content:eq("+index+")").find('table tbody tr').is('tr')
			var type=$(this).data('val')
			if(!isTr){
				$(this).data('val');
				$.ajax({
					type:"post",
					url:webPath + "/activity/list2018/select.do",
					dataType:'json',
					data:{
						type:$(this).data('val')
					},
					success:function(data){
						if(data.status=='true'){
							var s='';
							$.each(data.list,function(index,val){
								var i=index+1
								if(type==4){
									s+="<tr><td>第"+i+"名</td><td>"+val.userName+"</td><td>"+val.userMobile+"</td></tr>"
								}else{
									s+="<tr><td>第"+i+"名</td><td>"+val.userName+"</td><td>"+val.userMobile+"元</td></tr>"
								}
								
							});
							$(".rank-content:eq("+index+")").find('table tbody').html(s);
							scroll[index].setSize();
						}
					}
				})
			}else{
				scroll[index].setSize();
			}
		})
	})
	//榜单初始化
	$.ajax({
		type:"post",
		url:webPath + "/activity/list2018/select.do",
		data:{
			type:1
		},
		dataType:'json',
		success:function(data){
			if(data.status=='true'){
				var s='';
				$.each(data.list,function(index,val){
					var i=index+1
					s+="<tr><td>第"+i+"名</td><td>"+val.userName+"</td><td>"+val.userMobile+"元</td></tr>"
				});
				$(".rank-content:eq(0)").find('table tbody').html(s);
				scroll[0].setSize();
			}
		}
	})
})
