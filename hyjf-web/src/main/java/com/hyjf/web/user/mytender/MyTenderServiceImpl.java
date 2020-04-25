package com.hyjf.web.user.mytender;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.hyjf.common.file.FileUtil;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserProjectListCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class MyTenderServiceImpl extends BaseServiceImpl implements MyTenderService {

    @Override
    public List<WebUserProjectListCustomize> selectUserProjectList(Map<String, Object> params) {
        List<WebUserProjectListCustomize> list = webUserInvestListCustomizeMapper.selectUserProjectList(params);
        return list;
    }

    @Override
    public int countUserProjectRecordTotal(Map<String, Object> params) {
        int total = webUserInvestListCustomizeMapper.countUserProjectRecordTotal(params);
        return total;
    }

    @Override
    public List<WebUserInvestListCustomize> selectUserInvestList(UserInvestListBean form, int limitStart, int limitEnd) {
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String nid = StringUtils.isNotEmpty(form.getNid()) ? form.getNid() : null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("userId", userId);
        params.put("nid", nid);
        if (limitStart == 0 || limitStart > 0) {
            params.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            params.put("limitEnd", limitEnd);
        }
        List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserInvestList(params);
        return list;
    }

    @Override
    public int countUserInvestRecordTotal(UserInvestListBean form) {
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("userId", userId);
        int total = webUserInvestListCustomizeMapper.countUserInvestRecordTotal(params);
        return total;
    }

    @Override
    public int countProjectRepayPlanRecordTotal(ProjectRepayListBean form) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        params.put("userId", userId);
        params.put("borrowNid", borrowNid);
        params.put("nid", form.getNid());
        int total = webUserInvestListCustomizeMapper.countProjectRepayPlanRecordTotal(params);
        return total;
    }

    @Override
    public List<WebProjectRepayListCustomize> selectProjectRepayPlanList(ProjectRepayListBean form, int offset,
        int limit) {

        Map<String, Object> params = new HashMap<String, Object>();
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        params.put("userId", userId);
        params.put("borrowNid", borrowNid);
        params.put("nid", form.getNid());
        List<WebProjectRepayListCustomize> projectRepayList =
                webUserInvestListCustomizeMapper.selectProjectRepayPlanList(params);
        return projectRepayList;
    }

    @Override
    public int countCouponProjectRepayPlanRecordTotal(ProjectRepayListBean form) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        params.put("userId", userId);
        params.put("borrowNid", borrowNid);
        params.put("nid", form.getNid());
        int total = webUserInvestListCustomizeMapper.countCouponProjectRepayPlanRecordTotal(params);
        return total;
    }

    @Override
    public List<WebProjectRepayListCustomize> selectCouponProjectRepayPlanList(ProjectRepayListBean form, int offset,
        int limit) {

        Map<String, Object> params = new HashMap<String, Object>();
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        params.put("userId", userId);
        params.put("borrowNid", borrowNid);
        params.put("nid", form.getNid());
        List<WebProjectRepayListCustomize> projectRepayList =
                webUserInvestListCustomizeMapper.selectCouponProjectRepayPlanList(params);
        return projectRepayList;
    }

    /**
     * 借款列表
     * 
     * @return
     */
    public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
        return this.borrowCustomizeMapper.searchBorrowList(borrowCommonCustomize);
    }

    /**
     * 获取借款协议-甲方出借金额
     */
    public List<BorrowRecover> selectBorrowRecover(BorrowRecoverExample example) {

        return this.borrowRecoverMapper.selectByExample(example );
    }
    /**
     * 下载PDF文件（平台居间服务协议）
     * @param userid
     * @param nid
     * @param borrownid
     */
    public void createAgreementPDF(String userId, String nid, String borrowNid) {

        try {
            String fileName = borrowNid + ".pdf";
            String filePath =
                    PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_" + GetDate.getMillis()
                            + StringPool.FORWARD_SLASH;
            StringBuffer strBuf = new StringBuffer();
            strBuf.append(" <?xml version=\"1.0\" encoding=\"utf-8\" ?> ");
            strBuf.append(" <SealDocRequest> ");
            strBuf.append(" <BASE_DATA> ");
            strBuf.append("         <!--应用系统id--> ");
            strBuf.append("     <SYS_ID>" + PropUtils.getSystem(CustomConstants.HYJF_SEAL_SYSID) + "</SYS_ID> ");
            strBuf.append("         <!--用户id--> ");
            strBuf.append("     <USER_ID>" + PropUtils.getSystem(CustomConstants.HYJF_SEAL_USERID) + "</USER_ID> ");
            strBuf.append("         <!--用户密码--> ");
            strBuf.append("     <USER_PSD>" + PropUtils.getSystem(CustomConstants.HYJF_SEAL_PASSWORD) + "</USER_PSD> ");
            strBuf.append(" </BASE_DATA> ");
            strBuf.append(" <META_DATA> ");
            strBuf.append("      <!--是否模板合并--> ");
            strBuf.append("     <IS_MERGER>false</IS_MERGER> ");
            strBuf.append(" </META_DATA> ");
            strBuf.append(" <FILE_LIST> ");
            strBuf.append("    <TREE_NODE> ");
            strBuf.append("         <!--文档名称--> ");
            strBuf.append("         <FILE_NO>" + fileName + "</FILE_NO> ");
            strBuf.append("         <!--是否加二维码--> ");
            strBuf.append("         <IS_CODEBAR>false</IS_CODEBAR> ");
            strBuf.append("         <!--规则类型0：按照规则号进行盖章，1按照规则信息进行盖章--> ");
            strBuf.append("         <RULE_TYPE>0</RULE_TYPE> ");
            strBuf.append("         <!--规则号，多个规则用逗号隔开--> ");
            strBuf.append("         <RULE_NO>2</RULE_NO> ");
            strBuf.append("         <!--应用场景data是模板数据合成，file是读取FILEPATH文件--> ");
            strBuf.append("         <CJ_TYPE>file</CJ_TYPE> ");
            strBuf.append("         <!--请求类型，1：ftp,0:http-->  ");
            strBuf.append("         <REQUEST_TYPE>0</REQUEST_TYPE> ");
            strBuf.append("         <!--读取文件路径--> ");
            strBuf.append("         <FILE_PATH>" + PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_URL) + "?bid="
                    + borrowNid + "&userid=" + userId + "</FILE_PATH> ");
            strBuf.append("         <ftp_savepath></ftp_savepath> ");
            strBuf.append("         <!--是否添加标记印章1：是，0：否--> ");
            strBuf.append("         <AREA_SEAL>0</AREA_SEAL> ");
            strBuf.append("     </TREE_NODE> ");
            strBuf.append(" </FILE_LIST> ");
            strBuf.append(" </SealDocRequest> ");

            HashMap<String, String> params = new HashMap<String, String>();
            String url = PropUtils.getSystem(CustomConstants.HYJF_SEAL_URL);
            params.put("address", PropUtils.getSystem(CustomConstants.HYJF_SEAL_ADDRESS));
            params.put("port", PropUtils.getSystem(CustomConstants.HYJF_SEAL_PORT));
            params.put("operate", PropUtils.getSystem(CustomConstants.HYJF_SEAL_OPERATE));
            params.put("hc", strBuf.toString());
            params.put("yewudata", strBuf.toString());
            String str = HttpDeal.post(url, params);

            StringReader sr = new StringReader(str);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            NodeList r1 = doc.getElementsByTagName("FILE_MSG");
            String pdfUrl = r1.item(0).getFirstChild().getNodeValue();
            if (StringUtils.isNotEmpty(pdfUrl)) {
                File path = new File(filePath);
                if (!path.exists()) {
                    path.mkdirs();
                }
                FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * 查询优惠券已得收益
     * @author hsy
     * @param userId
     * @return
     * @see com.hyjf.web.user.mytender.MyTenderService#queryCouponInterestTotal(java.lang.Integer)
     */
    @Override
    public String queryCouponInterestTotal(Integer userId) {
        return couponRecoverCustomizeMapper.selectCouponReceivedInterestTotal(userId);
    }

    @Override
    public List<DebtBorrowCustomize> selectDebtBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
        return this.debtBorrowCustomizeMapper.selectBorrowList(debtBorrowCommonCustomize);
    }
    
	/**
	 * 获取借款信息
	 *
	 * @param borrowId
	 * @return 借款信息
	 */

	@Override
	public Borrow getBorrowByNid(String borrowNid) {
		Borrow borrow = null;
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<Borrow> list = borrowMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			borrow = list.get(0);
		}
		return borrow;
	}


}
