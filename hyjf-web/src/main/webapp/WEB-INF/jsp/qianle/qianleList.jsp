<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/headResponsive.jsp"></jsp:include>
<style>
	html{
		min-width:1920px;
	}
    .qianle-container {
      font-size: 14px;
      width: 95%;
      max-width: 1800px;
      box-sizing: border-box;
      margin-left: auto;
      margin-right: auto;
    }

    .loan-tradedetails .trade-main .top .btn-group{
      float: right;
      margin-right: 0;
    }
    .loan-tradedetails .trade-main .top .btn-group .btn{
      display: block;
      float: left;
      margin-right: 10px;
      height: 30px;
      line-height: 30px;
    }
    .loan-tradedetails .trade-main .top {
      margin: 0 30px;
      height: auto;
      overflow: hidden;
    }
    .loan-tradedetails .trade-main .top>div{
      margin: 5px 30px 5px 0;
    }

    .loan-tradedetails .trade-main .list {
      width: 100%;
      padding-left: 30px;
      padding-right: 30px;
    }

    .loan-tradedetails .trade-main .list .tab-panels {
      width: 100%;
    }

    .loan-tradedetails .trade-main .list .tab-panels .loan-div .ui-list-item {
      line-height: 16px;
      padding-top: 20px;
      padding-bottom: 20px;
    }
    .loan-tradedetails .trade-main .loan-divright .date{
      padding-left: 10px;
    }
    .intro{
      margin-top: 20px;
    }
    .intro span {
      margin-right: 20px;
    }
    .selector{
      float: left;
    }
    .selector label,
    .selector .selector-input{
      float: left;
      position: relative;
      height: 30px;
      line-height: 30px;
    }
    .selector label{
      padding-right:10px;
      z-index: 0;
    }
    .selector .selector-input{
      min-width: 120px;
      border: 1px solid #e5e5e5;
      outline: none;
      color: #999;
      padding: 0 10px;
    }
    .selector .selector-input select{
      padding-left:15px;
      position: absolute;
      border: 1px solid #e5e5e5;
      outline: none;
      top: 0;
      left: 0;
      width: 100%;
      height: 30px;
      z-index: 9;
      appearance: none;
        -moz-appearance: none;
        -webkit-appearance: none;
    }
    .input-test{
      float: left;
      padding-top:10px;

    }
  </style>
  <link rel="stylesheet" href="${cdn}/dist/css/lib/bootstrap-datepicker3.standalone.css" />
</head>
<body>
<jsp:include page="/headerWithoutNav.jsp"></jsp:include>
<article class="main-content" style="padding-top: 0;">
    <div class="qianle-container">
      <!-- start 内容区域 -->
      <div class="loan-tradedetails">
        <div class="trade-main">
          <div class="top">
            <form id="listForm">
              <div class="loan-divright" style="margin-left:0;width:auto;">
                <label for="regTimeStart">注册时间</label>
                <input type="text" class="date start" id="regTimeStart" name="regTimeStart" placeholder="yyyy-mm-dd">
                <label for="regTimeEnd">至</label>
                <input type="text" class="date end" id="regTimeEnd" name="regTimeEnd" placeholder="yyyy-mm-dd">
              </div>

              <div class="selector">
                <label for="Type">产品类型</label>
                <div class="selector-input">
                  <!-- <div class="selector-text">全部</div>-->
                  <select name="Type" id="Type" >
                    <option value="1">全部</option>
                    <option value="2">智投</option>
                    <option value="3">散标</option>
                  </select>
                </div>
              </div>
              <div class="clearfloat"></div>

              <div class="loan-divright" style="margin-left:0;width:auto;">
                <label for="addTimeStart">投资时间</label>
                <input type="text" class="date start" id="addTimeStart" name="addTimeStart" placeholder="yyyy-mm-dd">
                <label for="addTimeEnd">至</label>
                <input type="text" class="date end" id="addTimeEnd" name="addTimeEnd" placeholder="yyyy-mm-dd">
              </div>
                <div class="input-test" style="margin-left:0;width:auto;">
                    <label for="truename" style="letter-spacing: 10px;margin-left: 15px;">姓名</label>
                    <input type="text" id="truename" name="truename" style="width: 120px;">
                </div>
                <div class="input-test" style="margin-left:0;width:auto;">
                    <label for="username" style="margin-left: 10px;">用户名</label>
                    <input type="text" id="username" name="username" style="width: 130px;">
                </div>
                <div class="input-test" style="margin-left:0;width:auto;">
                    <label for="reffername" style="margin-left: 10px;">推荐人姓名</label>
                    <input type="text" id="reffername" name="reffername" style="width: 130px;">
                </div>
              <input type="hidden" id="paginatorPage" name="paginatorPage"  value="1">
              <input type="hidden" id="pageSize" name="pageSize"  value="10">
            </form>

            <div class="btn-group" style="margin-left:20px;">
              <a href="#javascript" id="find-btn"  class="btn sm btn-primary">查询</a>
              <a href="#javascript" id="clear-btn" class="btn sm btn-primary">清空查询</a>
              <a href="#javascript" id="export-btn" class="btn sm btn-primary">导出列表</a>
                <a href="#javascript" id="cancle-btn" class="btn sm btn-primary">退出</a>
            </div>
              <input type="hidden" name="page" id="page"  />
          </div>
          <div class="list">
            <p class="intro">投资总额：<span id="summoney"  class="highlight">10000</span> 年化金额总额：<span class="highlight" id="yearmoney" >10000</span> 佣金总额：<span class="highlight" id="commission">10000</span></p>
            <ul class="tab-panels">
              <li class="active" panel="0">
                <table class="loan-div">
                    <colgroup>
                        <col style="width:10px;" />
                    </colgroup>
                  <thead>
                    <tr>
                      <th class="ui-list-title">序号</th>
                      <th class="ui-list-title">注册时间</th>
                      <th class="ui-list-title">用户名</th>
                      <th class="ui-list-title">姓名</th>
                      <th class="ui-list-title">手机号</th>
                      <th class="ui-list-title">推荐人姓名</th>
                      <th class="ui-list-title">投资类型</th>
                      <th class="ui-list-title">项目/智投编号</th>
                      <th class="ui-list-title">投资金额</th>
                      <th class="ui-list-title">投资期限</th>
                      <th class="ui-list-title">年化金额</th>
                      <th class="ui-list-title">是否首投</th>
                      <th class="ui-list-title">佣金7%</th>
                      <th class="ui-list-title">投资时间</th>
                    </tr>
                  </thead>
                    <tbody id="qianleList">
                    <tr><td colspan="7"><div class="loading"><div class="icon"><div class="text">Loading...</div></div></div></td></tr>
                    </tbody><!--AJAX列表-->
                </table>
                  <div class="pages-nav" id="trade-pagination"></div><!--  分页 -->
              </li>
            </ul>
            <div class="ui-pagination">
              <ul>

              </ul>

            </div>
          </div>
        </div>
      </div>
      <!-- end 内容区域 -->
    </div>
</article>
	
<footer id="footer">
    <div class="nav-footer">
        <div class="container">
            <div class="footer-left">
                <div class="footer-hotline">
                    <div class="hotline-num">
                        400-900-7878
                    </div>
                    <div class="hotline-time">服务时间 9:00-18:00</div>
                    <p><a class="ir-link" href="http://ir.hyjf.com/" target="_blank">Investor Relations</a></p>
                </div>
            </div>
            <nav class="footer-nav">
                <dl>
                    <dt>安全保障</dt>
                    <dd>
                        <a href="${ctx}/aboutus/searchKnowReportList.do">网贷知识</a>
                        <a href="${ctx}/contentarticle/getSecurityPage.do?pageType=bank-page">银行存管</a>
                        <a href="${ctx}/aboutus/searchFXReportList.do">风险教育</a>
                    </dd>
                </dl>
                <dl>
                    <dt>关于我们</dt>
                    <dd>
                        <a href="${ctx}/aboutus/events.do" itemid="ft6">公司历程</a>
                        <a href="${ctx}/aboutus/partners.do" itemid="ft7">合作伙伴</a>
                        <a href="${ctx}/aboutus/contactus.do" itemid="ft8">联系我们</a>
                    </dd>
                </dl>
                <dl>
                    <dt>帮助中心</dt>
                    <dd>
                        <a href="${ctx}/help/index.do?indexId=hp13" itemid="ft10">注册登录</a>
                        <a href="${ctx}/help/fresher.do" itemid="ft11">新手进阶</a>
                        <a href="${ctx}/help/index.do?indexId=hp01" itemid="ft12">投资攻略</a>
                    </dd>
                </dl>
            </nav>
            <div class="footer-right">
                <div class="footer-qr">
                    <div class="qr-img"><img src="${cdn}/dist/images/app-android.png?version=${version}" alt="下载客户端" /></div>
                    <p>客户端下载</p>
                </div>
                <div class="footer-qr">
                    <div class="qr-img">
                        <img src="${cdn}/dist/images/qr_weixin.png" alt="微信关注我们" />
                    </div>
                    <p>关注我们</p>
                </div>
            </div>
        </div>
    </div>
    <div class="footer-approve">
        <div class="container">
            <ul>
                <li>
                    <a href="//ss.knet.cn/verifyseal.dll?sn=e13121111010044010zk2c000000&amp;ct=df&amp;a=1&amp;pa=0.4153385634999722" target="_blank" id="kx_verify" class="rove1"></a>
                </li>
                <li>
                    <a href="http://si.trustutn.org/info?sn=416160113000360141924" target="_blank" class="rove2"></a>
                </li>
                <li>
                    <a href="http://webscan.360.cn/index/checkwebsite?url=hyjf.com" target="_blank" class="rove3"></a>
                </li>
                <li>
                    <a href="http://www.itrust.org.cn/home/index/itrust_certifi/wm/PJ2017080901" class="rove4"  rel="nofollow" target="_blank"></a>
                </li>
                <li>
                    <a href="http://www.itrust.org.cn/home/index/satification_certificate/wm/MY2017092701" class="rove5" rel="nofollow" target="_blank"></a>
                </li>
                <li>
                    <a href="//www.cfca.com.cn/" rel="nofollow" target="_blank" class="rove6"></a>
                </li>
                <li>
                    <a href="http://ec.eqixin.com/?sn=QX1428703800761502091526" target="_blank" class="rove7"></a>
                </li>
                <li>
                    <a href="https://www.fadada.com/" target="_blank" class="rove10"></a>
                </li>
                <li>
                    <a key="57a96b64efbfb00b58328394" logo_size="124x47" logo_type="business" href="//v.pinpaibao.com.cn/authenticate/cert/?site=www.hyjf.com&amp;at=business" target="_blank" class="rove8">
                        <!-- <script src="//static.anquan.org/static/outer/js/aq_auth.js"></script> -->
                    </a>
                </li>
                <li>
                   	<a href="https://cert.ebs.org.cn/2fb7c491-2611-43e1-a917-0da964e119d5.html" target="_blank" class="rove9"></a>
                </li>
            </ul>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
                <span>&copy; 汇盈金服 All rights reserved | 惠众商务顾问（北京）有限公司 京ICP备13050958号 | <a class="a-link" href="/homepage/systemSafetyLevelProtectionRecordInit.do" target="_blank">信息系统安全等级保护备案证明（三级）</a></span>
                <br/><br/>
                <span>市场有风险  投资需谨慎 | 历史回报不等于实际收益</span>
        </div>
    </div>
    <form id="listForm" action="" style="display: none;" method="post">
        <input type="hidden" id="paginatorPage" name="paginatorPage"  value="1"></input>
        <input type="hidden" id="pageSize" name="pageSize"  value="10"></input>
    </form>
</footer>
<script src="${cdn}/dist/js/lib/jquery.min.js"></script>
<script src="${cdn}/dist/js/lib/jquery-migrate-1.2.1.js"></script>
<script src="${cdn}/dist/js/lib/nprogress.js"></script>
<script src="${cdn}/dist/js/utils.js"></script>
<script src="${cdn}/js/common/common.js?version=${version}" type="text/javascript"></script>
<script type="text/javascript">
    //设置menu定位
    function setActById(id){
        $("#"+id).addClass("active");
    }
</script>

<!--站长统计开始-->
<div style="display: none;">
    <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1257473260'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/stat.php%3Fid%3D1257473260%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));</script>
</div>
<!--站长统计结束-->

<script src="${cdn}/dist/js/lib/bootstrap-datepicker.min.js"></script>
<script  type="text/javascript" charset="utf-8">
 var now = new Date();
  $('.loan-divright input').each(function() {
      $(this).datepicker({
        autoclose: true,
        format: "yyyy-mm-dd",
        language: "zh-CN",
        endDate: now
      });
  });
    var dateTime = [];
    /*
     * 初始化日期插件
     */
    function setDatepicker(idx) {
      var now = new Date();
      var panel = $(".top-fr");
      if (dateTime[idx] === undefined) {
        dateTime[idx] = [];
        dateTime[idx]["start"] = panel.find(".loan-divright").children(".start").datepicker({
          autoclose: true,
          format: "yyyy-mm-dd",
          language: "zh-CN",
          endDate: now,
        }).on("hide", function(e) {
          dateTime[idx]["end"].datepicker('setStartDate', e.date);
        });
        dateTime[idx]["end"] = panel.find(".loan-divright").children(".end").datepicker({
          autoclose: true,
          format: "yyyy-mm-dd",
          language: "zh-CN",
          endDate: now,
        });
      }
    }
    setDatepicker(0);




</script>
<script src="${cdn}/js/qianle/qianleList.js?version=201810101548" type="text/javascript"></script>

</body>
</html>