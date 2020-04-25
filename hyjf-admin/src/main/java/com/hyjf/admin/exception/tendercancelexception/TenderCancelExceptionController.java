package com.hyjf.admin.exception.tendercancelexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.FavFTPUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = TenderCancelExceptionDefine.REQUEST_MAPPING)
public class TenderCancelExceptionController extends BaseController {


	@Autowired
	private TenderCancelExceptionService tenderCancelService;

	@RequestMapping(value = "test")
	@ResponseBody
	public String test(HttpServletRequest request){
		try{
			System.out.println("--------------------开始修复线上脱敏异常合同---------");
			tenderCancelService.putMessage();

		}catch (Exception e){
			e.printStackTrace();
		}
		return "修复成功！";
	}


    /**
     * 上传脱敏文件
     * @param upParentDir
     * @return
     */
    private boolean uplodTmImage(String upParentDir, String saveDir) {

        String ftpIP = "39.106.9.158";
        String port = "21";
        String basePathImage = "/hyjfdata/upfiles/contract/img";
        String password = "hyjf2018";
        String username = "developer";
        try {
            File paraentDir = new File(upParentDir);
//            String upParaFile = paraentDir.getParent();

            File[] files = paraentDir.listFiles();

            for (File file : files) {
                String fileName = file.getName();
                FileInputStream in = new FileInputStream(file);
                boolean flag = FavFTPUtil.uploadFile(ftpIP, Integer.valueOf(port), username, password,
                        basePathImage, saveDir, fileName, in);
                if (!flag){
                    throw new RuntimeException("上传失败!fileName:" + fileName);
                }
            }
            //删除原目录
//            FileUtil.deltree(upParaFile);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return  true;
    }

//    hyjf.ftp.ip=39.106.9.158
//            #ftp端口
//    hyjf.ftp.port=21
//            #ftp用户名
//    hyjf.ftp.username=developer
//#ftp密码
//    hyjf.ftp.password=hyjf2018
//#ftp存储基本路径
//    hyjf.ftp.basepath.img=/hyjfdata/upfiles/contract/img
//
//    hyjf.ftp.basepath.pdf=/hyjfdata/upfiles/contract/pdf
	/**
	 * 复审记录
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TenderCancelExceptionDefine.INIT)
	@RequiresPermissions(TenderCancelExceptionDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(TenderCancelExceptionDefine.FORM) TenderCancelExceptionBean form) {
		LogUtil.startLog(TenderCancelExceptionController.class.toString(), TenderCancelExceptionDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(TenderCancelExceptionDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TenderCancelExceptionController.class.toString(), TenderCancelExceptionDefine.INIT);
		return modelAndView;
	}
	
	

	/**
	 * 查找
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(TenderCancelExceptionDefine.SEARCH_ACTION)
	@RequiresPermissions(TenderCancelExceptionDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, @ModelAttribute(TenderCancelExceptionDefine.FORM) TenderCancelExceptionBean form) {
		LogUtil.startLog(TenderCancelExceptionController.class.toString(), TenderCancelExceptionDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(TenderCancelExceptionDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(TenderCancelExceptionController.class.toString(), TenderCancelExceptionDefine.SEARCH_ACTION);
		return modelAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, TenderCancelExceptionBean form) {
		int recordTotal = this.tenderCancelService.queryCount(form);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			form.setLimitStart(paginator.getOffset());
			form.setLimitEnd(paginator.getLimit());
			List<BorrowTenderTmp> recordList = this.tenderCancelService.queryRecordList(form);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
		}
		modelAndView.addObject(TenderCancelExceptionDefine.FORM, form);
	}

	/**
	 * 解冻异常修复
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TenderCancelExceptionDefine.BIDCANEL_ACTION)
	@RequiresPermissions(TenderCancelExceptionDefine.PERMISSIONS_BIDCANCEL)
	public String bidCancelAction(HttpServletRequest request, @RequestBody TenderCancelExceptionBean form) {
		JSONObject ret = new JSONObject();
		if (StringUtils.isEmpty(form.getOrderId())) {
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "参数错误，请稍后再试！");
			return ret.toString();
		}
		boolean tenderTempFlag = this.tenderCancelService.selectTenderIsExists(form.getOrderId());
		if (tenderTempFlag) {
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "订单号在系统已经存在，只能撤销只在银行存在的订单！");
			return ret.toString();
		}
		String orgOrderId = form.getOrderId();
		BorrowTenderTmp tenderTmp = this.tenderCancelService.getBorrowTenderTmp(orgOrderId);
		if (Validator.isNull(tenderTmp)) {
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "出借撤销失败，出借可能已经撤销！");
			return ret.toString();
		}
		int userId = tenderTmp.getUserId();
		String productId = tenderTmp.getBorrowNid();
		BigDecimal txAmount = tenderTmp.getAccount();
		// 出借人的账户信息
		BankOpenAccount bankAccount = this.tenderCancelService.getBankOpenAccount(userId);
		if (Validator.isNull(bankAccount)) {
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "出借撤销失败，请联系客服！");
			return ret.toString();
		}
		String accountId = bankAccount.getAccount();
		int status = 0;//撤销成功状态 0:正常撤销 1:异常记录处理
		boolean bidCancelFlag = false;
		BankCallBean bean = this.tenderCancelService.bidCancel(userId, accountId, productId, orgOrderId, txAmount.toString());
		if (Validator.isNotNull(bean)) {
			String retCode = StringUtils.isNotBlank(bean.getRetCode()) ? bean.getRetCode() : "";
			if (retCode.equals(BankCallConstant.RESPCODE_SUCCESS)) {
				bidCancelFlag = true;
			} else if (retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST1) || retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST2)) {
				status = 1;
				bidCancelFlag = true;
			} else if (retCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_RIGHT)) {
				status = 1;
				bidCancelFlag = true;
			}
		}
		if (bidCancelFlag) {
			try {
				boolean tenderCancelFlag = this.tenderCancelService.updateBidCancelRecord(tenderTmp);
				if (tenderCancelFlag) {
					ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_OK);
					if (status == 1) {
						ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "出借异常记录处理成功!");
					}else{
						ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "出借撤销成功!");
					}
					return ret.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
		ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "出借撤销失败，请联系客服！");
		return ret.toString();
	}
	
	/**
	 * 查询标的状态
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = TenderCancelExceptionDefine.QUERY_BORRORSTATUS_ACTION)
	public String queryBorrorStatusAction(HttpServletRequest request, @RequestBody TenderCancelExceptionBean form) {
		JSONObject ret = new JSONObject();
		if (StringUtils.isEmpty(form.getBorrowNid())) {
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "参数错误，请稍后再试！");
			return ret.toString();
		}
		String borrowNid = form.getBorrowNid();
		Borrow borrow = this.tenderCancelService.getBorrowByNid(borrowNid);
		Integer status = borrow.getStatus();
		if (status!=null && status == 2) {//出借中标的
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_OK);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "出借中标的,可撤销！");
			return ret.toString();
		}else{
			ret.put(TenderCancelExceptionDefine.JSON_STATUS_KEY, TenderCancelExceptionDefine.JSON_STATUS_NG);
			ret.put(TenderCancelExceptionDefine.JSON_RESULT_KEY, "该标的已经不是出借中的数据,请谨慎操作！是否继续撤销?");
			return ret.toString();
		}
	}
}
