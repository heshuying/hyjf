<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
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
                        <a href="${ctx}/help/index.do?indexId=hp01" itemid="ft12">出借攻略</a>
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
                <span>市场有风险  出借需谨慎 | 历史回报不等于实际收益</span>
        </div>
    </div>
    <form id="listForm" action="" style="display: none;" method="post">
        <input type="hidden" id="paginatorPage" name="paginatorPage"  value="1"></input>
        <input type="hidden" id="pageSize" name="pageSize"  value="10"></input>
    </form>
</footer>
<jsp:include page="/toolbar.jsp"></jsp:include>
<script src="${cdn}/dist/js/lib/jquery.min.js"></script>
<script src="${cdn}/dist/js/lib/jquery-migrate-1.2.1.js"></script>
<script src="${cdn}/dist/js/lib/nprogress.js"></script>
<script src="${cdn}/dist/js/lib/moment.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${cdn}/dist/js/utils.js?version=${version}"></script>
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
