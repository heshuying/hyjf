/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
package com.hyjf.web.user.repay;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.common.zip.ZIPGenerator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserRepayProjectListCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.user.mytender.MyTenderDefine;
import com.hyjf.web.user.mytender.MyTenderService;
import com.hyjf.web.user.mytender.ProjectRepayListBean;
import com.hyjf.web.user.mytender.UserInvestListBean;
import com.hyjf.web.user.planinfo.PlanInfoService;
import com.hyjf.web.util.WebUtils;

@Controller("userrepayController")
@RequestMapping(value = UserRepayDefine.REQUEST_MAPPING)
public class UserRepayController extends BaseController {

    @Autowired
    private UserRepayService repayService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MyTenderService mytenderService;

    @Autowired    
    private PlanInfoService planInfoService;
    
	public static JedisPool pool = RedisUtils.getPool();

    /**
     * 
     * 查看协议
	 * 
     * @author renxingchen
     * @param request
     * @param borrowNid
     * @return
     */
    @RequestMapping(value = UserRepayDefine.GET_AGREEMENT_ACTION)
    public ModelAndView getAgreement(HttpServletRequest request, String borrowNid) {
        ModelAndView modelAndView = new ModelAndView();
        WebViewUser user = WebUtils.getUser(request);
		Borrow borrow = this.repayService.searchRepayProject(user.getUserId(), user.getUsername(), user.getRoleId(),
				borrowNid);
        if (null != borrow.getBorrowFullTime()) {
            modelAndView.addObject("borrowFullTime", new Date(borrow.getBorrowFullTime() * 1000L));
        } else {
			modelAndView.addObject("borrowFullTime", new Date(
                                    (Integer.parseInt(borrow.getVerifyTime()) + borrow.getBorrowValidTime() * 24 * 60 * 60) * 1000L));
        }
        modelAndView.addObject("username", user.getUsername());
        modelAndView.addObject("borrowNid", borrowNid);
        // 查询用户项目的出借情况
        List<WebUserInvestListCustomize> investlist = repayService.selectUserInvestList(borrowNid, -1, -1);
        modelAndView.addObject("investlist", investlist);
        if (borrow.getProjectType() == 8) {
            modelAndView.setViewName(UserRepayDefine.AGREEMENT_CONTRACT_HXF_PAGE);
        } else {
            modelAndView.setViewName(UserRepayDefine.AGREEMENT_CONTRACT_PAGE);
        }
        return modelAndView;
    }

    /**
     * 
     * 检测还款密码
	 * 
     * @author renxingchen
     * @param request
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = UserRepayDefine.CHECK_PASSWORD_ACTION)
    public boolean checkPassword(HttpServletRequest request, String password) {
        WebViewUser user = WebUtils.getUser(request);
        if (null == user) {
            return true;
        } else {
            if (loginService.queryPasswordAction2(user.getUsername(), password) < 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 
     * 用户还款页面
	 * 
     * @author renxingchen
     * @param request
     * @return
     */
    @RequestMapping(value = UserRepayDefine.USER_REPAY_PAGE_ACTION)
    public ModelAndView userRepayPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(UserRepayDefine.USER_REPAY_PAGE);
        // 查询该用户的未还款金额
//        Integer userId = WebUtils.getUserId(request);
        WebViewUser user = WebUtils.getUser(request);
        if(user!=null){
        	//用户为借款人
        	if("2".equals(user.getRoleId())){
                BigDecimal repay = repayService.getAccount(user.getUserId()).getRepay();
                modelAndView.addObject("repayMoney", CustomConstants.DF_FOR_VIEW.format(repay));
        	}
        	//用户为垫付机构
        	else if("3".equals(user.getRoleId())){
        		BigDecimal repay = repayService.getRepayOrgRepaywait(user.getUserId());
                modelAndView.addObject("repayMoney", CustomConstants.DF_FOR_VIEW.format(repay));
        	}
        	modelAndView.addObject("userRole", user.getRoleId());
        }
        return modelAndView;
    }

    /**
     * 获取用户的借款项目
     * 
     * @param userRepay
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = UserRepayDefine.REPAY_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public UserRepayListAjaxResult searchUserRepayList(@ModelAttribute UserRepayProjectListBean userRepay,
        HttpServletRequest request, HttpServletResponse response) {
        UserRepayListAjaxResult result = new UserRepayListAjaxResult();
        // 用户ID
//        Integer userId = WebUtils.getUserId(request);-
//        userRepay.setUserId(userId.toString());
//        this.createUserRepayListPage(request, result, userRepay);
        
        WebViewUser user = WebUtils.getUser(request);
        if(user!=null){
            userRepay.setUserId(user.getUserId().toString());
            userRepay.setRoleId(user.getRoleId());//角色分借款人、垫付机构
            result.setRoleId(user.getRoleId());
            this.createUserRepayListPage(request, result, userRepay);
        }
        
        return result;
    }

    /**
     * 获取用户还款项目分页信息
     * 
     * @param request
     * @param info
     * @param form
     */
    private void createUserRepayListPage(HttpServletRequest request, UserRepayListAjaxResult result,
        UserRepayProjectListBean form) {

        int recordTotal = this.repayService.countUserRepayRecordTotal(form);
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
            // 获取用户待还款的项目列表
			List<WebUserRepayProjectListCustomize> recordList = repayService.searchUserRepayList(form,
					paginator.getOffset(), paginator.getLimit());
            result.success();
            result.setPaginator(paginator);
            result.setReplayList(recordList);
            result.setNowdate(new java.util.Date());
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
            result.success();
            result.setPaginator(paginator);
        }
    }

    /**
     * 获取用户借款项目的用户出借详情列表
     * 
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = UserRepayDefine.INVEST_LIST_ACTION, produces = "application/json; charset=utf-8")
    public String searchUserInvestList(@ModelAttribute InvestListBean form, HttpServletRequest request,
        HttpServletResponse response) {

        LogUtil.startLog(UserRepayDefine.THIS_CLASS, UserRepayDefine.INVEST_LIST_ACTION);
        JSONObject ret = new JSONObject();
        JSONObject info = new JSONObject();
        createUserInvestPage(request, info, form);
        ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
        ret.put(CustomConstants.DATA, info);
        ret.put(CustomConstants.MSG, "");
        LogUtil.endLog(UserRepayDefine.THIS_CLASS, UserRepayDefine.INVEST_LIST_ACTION);
        return JSONObject.toJSONString(ret, true);
    }

    /**
     * 创建用户借款项目的用户出借详情分页信息
     * 
     * @param request
     * @param info
     * @param form
     */
    private void createUserInvestPage(HttpServletRequest request, JSONObject info, InvestListBean form) {

        int recordTotal = this.repayService.countUserInvestRecordTotal(form.getBorrowNid());
        if (recordTotal > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            // 查询用户项目的出借情况
			List<WebUserInvestListCustomize> recordList = repayService.selectUserInvestList(form.getBorrowNid(),
					paginator.getOffset(), paginator.getLimit());
            info.put("paginator", paginator);
            info.put("userinvestlist", recordList);
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
            info.put("paginator", paginator);
            info.put("userinvestlist", "");
        }
    }

    /**
     * 获取用户借款项目还款详情(需要计算还款总额----第一遍)
     * 
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ParseException
     * @throws NumberFormatException
     */
    @RequestMapping(value = UserRepayDefine.REPAY_DETAIL_ACTION)
    public ModelAndView searchRepayProjectDetail(@ModelAttribute UserRepayProjectBean form, HttpServletRequest request,
        HttpServletResponse response) throws NumberFormatException, ParseException {
        ModelAndView modelAndView = new ModelAndView(UserRepayDefine.REPAY_DETAIL_PAGE);
        // 用户ID
//        Integer userId = WebUtils.getUserId(request);
        WebViewUser user = WebUtils.getUser(request);
        if(user!=null){
        	form.setUserId(user.getUserId().toString());
        	form.setUsername(user.getUsername());
        	form.setRoleId(user.getRoleId());
        }
        // 查询用户的出借详情
        UserRepayProjectBean repayProject = repayService.searchRepayProjectDetail(form);
        modelAndView.addObject("repayProject", repayProject);
        return modelAndView;
    }

    /***
     * 用户还款
     * 
     * @param request
     * @param response
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = UserRepayDefine.REPAY_ACTION)
    public ModelAndView repayUserBorrowProject(HttpServletRequest request, HttpServletResponse response)
        throws ParseException {
        ModelAndView modelAndView = new ModelAndView(UserRepayDefine.REPAY_ERROR_PAGE);
        JSONObject info = new JSONObject();
//        Integer userId = WebUtils.getUserId(request);
        WebViewUser user = WebUtils.getUser(request);
        if(user==null){
        	modelAndView.addObject("message", "登录超时，请重新登录！");
            return modelAndView;
        }
        Integer userId = user.getUserId();
        String roleId= user.getRoleId();
        String username= user.getUsername();
        String password = request.getParameter("password"); 
        String borrowNid = request.getParameter("borrowNid");
        modelAndView.addObject("borrowNid", borrowNid);
        Borrow borrow = this.repayService.searchRepayProject(userId, username, roleId, borrowNid);
		/** redis 锁 */
//		if (StringUtils.isNotEmpty(RedisUtils.get("repay_borrow_nid" + borrowNid))) {
//			modelAndView.addObject("message", "项目正在还款中...");
//			return modelAndView;
//		} else {
//			RedisUtils.set("repay_borrow_nid" + borrowNid, borrowNid, 30);
//		}
		
		boolean reslut = RedisUtils.tranactionSet("repay_borrow_nid" + borrowNid, 30);
		// 如果没有设置成功，说明有请求来设置过
		if(!reslut){
			modelAndView.addObject("message", "项目正在还款中...");
			return modelAndView;
		}
        // 校验用户/垫付机构的还款
        RepayByTermBean repay = null;
        if(StringUtils.isNotEmpty(roleId) && "3".equals(roleId)){//垫付机构还款校验
        	Integer repayUserId= borrow.getUserId();
			repay = this.validatorFieldCheckRepay_org(info, userId, password, borrow, repayUserId);
        }else{ //借款人还款校验
			repay = this.validatorFieldCheckRepay(info, userId, password, borrow);
        }
        if (!ValidatorCheckUtil.hasValidateError(info) && repay != null) {
            String ip = GetCilentIP.getIpAddr(request);
            repay.setIp(ip);
            // 用户还款
            try {
                boolean flag = this.repayService.updateRepayMoney(repay,Integer.valueOf(roleId),userId);
                if (flag) {
                    modelAndView.addObject("borrowName", borrow.getName());
                    modelAndView.setViewName(UserRepayDefine.REPAY_SUCCESS_PAGE);
                    return modelAndView;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            modelAndView.addObject("message", "还款失败，请稍后再试！");
            return modelAndView;

        } else {
            modelAndView.addObject("message", info.getString(UserRepayDefine.REPAY_ERROR));
            return modelAndView;
        }
    }

    /**
     * 校验用户还款信息(计算结果形成中间结果值)
     * 
     * @param info
     * @param userId
     * @param password
     * @param borrowNid
     * @throws ParseException
     */
	private RepayByTermBean validatorFieldCheckRepay(JSONObject info, int userId, String password, Borrow borrow)
        throws ParseException {
        // 获取当前用户
        Users user = this.repayService.searchRepayUser(userId);
        // 检查用户是否存在
        if (user != null) {
            String sort = user.getSalt();
            String mdPassword = MD5.toMD5Code(password + sort);
            // 检查用户输入的密码信息同当前的用户密码信息是否对应
            if (mdPassword.equals(user.getPassword())) {
                // 获取用户的账户余额信息
                Account account = this.repayService.searchRepayUserAccount(userId);
                // 查询用户的账户余额信息
                if (account != null) {
                    // 获取用户的余额
                    BigDecimal balance = account.getBalance();
                    // 获取当前的用户还款的项目
					borrow = this.repayService.searchRepayProject(userId, user.getUsername(), "2",
							borrow.getBorrowNid());
                    // 判断用户当前还款的项目是否存在
                    if (borrow != null) {
                        // 获取项目还款方式
						String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle()
								: null;
                        // 获取历史年回报率
                        BigDecimal borrowApr = borrow.getBorrowApr();
                        // 判断项目的还款方式是否为空
                        if (StringUtils.isNotEmpty(borrowStyle)) {
                        	//一次性还款
                            if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)
                                    || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
								BigDecimal repayTotal = this.repayService.searchRepayTotal(userId, borrow);
                                if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
                                    // ** 用户符合还款条件，可以还款 *//*
                                    AccountChinapnr accountChinapnr = this.repayService.getChinapnrUserInfo(userId);
									BigDecimal userBalance = this.repayService
											.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
                                    if (repayTotal.compareTo(userBalance) == 0
                                            || repayTotal.compareTo(userBalance) == -1) {
                                        // ** 用户符合还款条件，可以还款 *//*
										RepayByTermBean repayByTerm = this.repayService.calculateRepay(userId, borrow);
                                        return repayByTerm;
                                    } else {
                                        ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                                UserRepayDefine.REPAY_ERROR10);
                                    }
                                } else {
                                    ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                            UserRepayDefine.REPAY_ERROR8);
                                }
                            } //分期还款
                            else {
								BigDecimal repayTotal = this.repayService.searchRepayByTermTotal(userId, borrow,
										borrowApr, borrowStyle, borrow.getBorrowPeriod());
                                if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
                                    AccountChinapnr accountChinapnr = this.repayService.getChinapnrUserInfo(userId);
									BigDecimal userBalance = this.repayService
											.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
                                    if (repayTotal.compareTo(userBalance) == 0
                                            || repayTotal.compareTo(userBalance) == -1) {
                                        // ** 用户符合还款条件，可以还款 *//*
										RepayByTermBean repayByTerm = this.repayService.calculateRepayByTerm(userId,
												borrow);
                                        return repayByTerm;
                                    } else {
                                        ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                                UserRepayDefine.REPAY_ERROR10);
                                    }
                                } else {
                                    ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                            UserRepayDefine.REPAY_ERROR8);
                                }
                            }
                        } else {
                            ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                    UserRepayDefine.REPAY_ERROR5);
                        }
                    } else {
                        ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                UserRepayDefine.REPAY_ERROR4);
                    }

                } else {
                    ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                            UserRepayDefine.REPAY_ERROR3);
                }
            } else {
				ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
						UserRepayDefine.REPAY_ERROR2);
            }
        } else {
            ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR, UserRepayDefine.REPAY_ERROR1);
        }
        return null;
    }
    
    /**
     * 校验垫付机构还款信息(计算结果形成中间结果值)
     * 
     * @param info
	 * @param userId
	 *            垫付机构id
     * @param password
     * @param borrowNid
	 * @param repayUserId
	 *            借款人id
     * @throws ParseException
     */
	private RepayByTermBean validatorFieldCheckRepay_org(JSONObject info, int userId, String password, Borrow borrow,
			Integer repayUserId) throws ParseException {
        // 获取当前垫付机构
        Users user = this.repayService.searchRepayUser(userId);
        // 检查垫付机构是否存在
        if (user != null) {
            String sort = user.getSalt();
            String mdPassword = MD5.toMD5Code(password + sort);
            // 检查垫付机构输入的密码信息同当前的用户密码信息是否对应
            if (mdPassword.equals(user.getPassword())) {
                // 获取垫付机构的账户余额信息
                Account account = this.repayService.searchRepayUserAccount(userId);
                // 查询垫付机构的账户余额信息
                if (account != null) {
                    // 获取用户的余额
                    BigDecimal balance = account.getBalance();
                    // 获取当前垫付机构要还款的项目
					borrow = this.repayService.searchRepayProject(repayUserId, null, null, borrow.getBorrowNid());
                    // 判断用户当前还款的项目是否存在
                    if (borrow != null) {
                        // 获取项目还款方式
						String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle()
								: null;
                        // 获取历史年回报率
                        BigDecimal borrowApr = borrow.getBorrowApr();
                        // 判断项目的还款方式是否为空
                        if (StringUtils.isNotEmpty(borrowStyle)) {
                        	//一次性还款
                            if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)
                                    || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
								BigDecimal repayTotal = this.repayService.searchRepayTotal(repayUserId, borrow);
                                if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
                                    // ** 垫付机构符合还款条件，可以还款 *//*
                                    AccountChinapnr accountChinapnr = this.repayService.getChinapnrUserInfo(userId);
									BigDecimal userBalance = this.repayService
											.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
                                    if (repayTotal.compareTo(userBalance) == 0
                                            || repayTotal.compareTo(userBalance) == -1) {
                                        // ** 垫付机构符合还款条件，可以还款 *//*
										RepayByTermBean repayByTerm = this.repayService.calculateRepay(repayUserId,
												borrow);
                                        repayByTerm.setRepayUserId(userId);//垫付机构id
                                        return repayByTerm;
                                    } else {
                                        ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                                UserRepayDefine.REPAY_ERROR10);
                                    }
                                } else {
                                    ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                            UserRepayDefine.REPAY_ERROR8);
                                }
                            } //分期还款
                            else {
								BigDecimal repayTotal = this.repayService.searchRepayByTermTotal(repayUserId, borrow,
										borrowApr, borrowStyle, borrow.getBorrowPeriod());
                                if (repayTotal.compareTo(balance) == 0 || repayTotal.compareTo(balance) == -1) {
                                	//查询垫付机构账户信息
                                    AccountChinapnr accountChinapnr = this.repayService.getChinapnrUserInfo(userId);
									BigDecimal userBalance = this.repayService
											.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
                                    if (repayTotal.compareTo(userBalance) == 0
                                            || repayTotal.compareTo(userBalance) == -1) {
                                        // ** 用户符合还款条件，可以还款 *//*
										RepayByTermBean repayByTerm = this.repayService
												.calculateRepayByTerm(repayUserId, borrow);
                                        repayByTerm.setRepayUserId(userId);//垫付机构id
                                        return repayByTerm;
                                    } else {
                                        ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                                UserRepayDefine.REPAY_ERROR10);
                                    }
                                } else {
                                    ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                            UserRepayDefine.REPAY_ERROR8);
                                }
                            }
                        } else {
                            ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                    UserRepayDefine.REPAY_ERROR5);
                        }
                    } else {
                        ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                                UserRepayDefine.REPAY_ERROR4);
                    }

                } else {
                    ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
                            UserRepayDefine.REPAY_ERROR3);
                }
            } else {
				ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR,
						UserRepayDefine.REPAY_ERROR2);
            }
        } else {
            ValidatorCheckUtil.validateSpecialError(info, UserRepayDefine.REPAY_ERROR, UserRepayDefine.REPAY_ERROR1);
        }
        return null;
    }

    /**
     * 
     * 下载借款用户的协议
	 * 
     * @author renxingchen
     */
    @RequestMapping(value = UserRepayDefine.DOWNLOAD_BORROWER_PDF_ACTION)
    public void downloadBorrowerPdf(HttpServletRequest request, HttpServletResponse response, String borrowNid,
        String tType) {
        List<File> files = new ArrayList<File>();
        // 查询用户项目的出借情况
        List<WebUserInvestListCustomize> investlist;
        if ("1".equals(tType)) {
            investlist = repayService.selectUserDebtInvestList(borrowNid,null, -1, -1);
        } else {
            investlist = repayService.selectUserInvestList(borrowNid, -1, -1);

        }
        UserInvestListBean form;
        for (WebUserInvestListCustomize tmp : investlist) {
            form = new UserInvestListBean();
            form.setBorrowNid(borrowNid);
            form.setNid(tmp.getNid());
            File file;
            if ("1".equals(tType)) {
                file = createDebtAgreementPDFFile(request, response, form, tmp.getUserId(), tmp.getOrderId());
            } else {
                file = createAgreementPDFFile(request, response, form, tmp.getUserId());
            }
            if (file != null) {
                files.add(file);
            }
        }
        ZIPGenerator.generateZip(response, files, borrowNid + ".zip");
    }

    /**
     * 
     * 生成汇添金专属表的PDF文件
	 * 
     * @author renxingchen
     * @param request
     * @param response
     * @param form
     * @param userId
     * @return
     */
    private File createDebtAgreementPDFFile(HttpServletRequest request, HttpServletResponse response,
        UserInvestListBean form, String userId, String orderId) {
        LogUtil.startLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF 生成PDF文件");
        if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
            return null;
        }
        // 查询借款人用户名
        DebtBorrowCommonCustomize borrowCommonCustomize = new DebtBorrowCommonCustomize();
        // 借款编码
        borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<DebtBorrowCustomize> recordList = mytenderService.selectDebtBorrowList(borrowCommonCustomize);
        DebtBorrowCustomize borrowCustomize;
        if (recordList.size() != 1) {
            return null;
        } else {
            borrowCustomize = recordList.get(0);
        }
        Map<String, Object> contents = new HashMap<String, Object>();
        contents.put("borrowNid", form.getBorrowNid());
        contents.put("nid", form.getNid());
        // 借款人用户名
        contents.put("borrowUsername", borrowCustomize.getUsername());
        if (StringUtils.isNotBlank(borrowCustomize.getRecoverLastTime())) {
            // 最后一笔的放款完成时间 (协议签订日期)
            contents.put("recoverTime", borrowCustomize.getRecoverLastTime());
        } else {
            // 设置为满标时间
            contents.put("recoverTime", borrowCustomize.getReverifyTime());
        }
        // 用户ID
        form.setUserId(userId);
        // 用户出借列表
		List<WebUserInvestListCustomize> tzList = repayService.selectUserDebtInvestList(form.getBorrowNid(), orderId,
				-1, -1);
        if (tzList != null && tzList.size() > 0) {
            contents.put("userInvest", tzList.get(0));
        }

        // 如果是分期还款，查询分期信息
        String borrowStyle = borrowCustomize.getBorrowStyle();// 还款模式
        if (borrowStyle != null) {
            if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
                ProjectRepayListBean bean = new ProjectRepayListBean();
                bean.setUserId(userId);
                bean.setBorrowNid(form.getBorrowNid());
                bean.setNid(form.getNid());
                int recordTotal = this.planInfoService.countProjectRepayPlanRecordTotal(bean);
                if (recordTotal > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
					List<WebProjectRepayListCustomize> fqList = this.planInfoService.selectProjectRepayPlanList(bean,
							paginator.getOffset(), paginator.getLimit());
                    contents.put("paginator", paginator);
                    contents.put("repayList", fqList);
                } else {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                    contents.put("paginator", paginator);
                    contents.put("repayList", "");
                }
            }
        }

        File file = null;
        try {
			file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + orderId + ".pdf",
                            CustomConstants.TENDER_CONTRACT, contents);
        } catch (Exception e) {
            LogUtil.errorLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF  生成PDF文件", e);
            e.printStackTrace();
        }

        LogUtil.endLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF 生成PDF文件");
        return file;
    }

    /**
     * 生成PDF文件
     * 
     * @param request
     * @param response
     * @param form
     */
    private File createAgreementPDFFile(HttpServletRequest request, HttpServletResponse response,
        UserInvestListBean form, String userId) {
        LogUtil.startLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF 生成PDF文件");
        if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
            return null;
        }
        // 查询借款人用户名
        BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
        // 借款编码
        borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<BorrowCustomize> recordList = mytenderService.selectBorrowList(borrowCommonCustomize);
        if (recordList.size() != 1) {
            return null;
        }
        Map<String, Object> contents = new HashMap<String, Object>();
        contents.put("borrowNid", form.getBorrowNid());
        // 借款人用户名
        contents.put("borrowUsername", recordList.get(0).getUsername());
        if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
            // 最后一笔的放款完成时间 (协议签订日期)
            contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
        } else {
            // 设置为满标时间
            contents.put("recoverTime", recordList.get(0).getReverifyTime());
        }
        // 用户ID
        form.setUserId(userId);
        // 用户出借列表
        List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
        if (tzList != null && tzList.size() > 0) {
            contents.put("userInvest", tzList.get(0));
        }

        // 如果是分期还款，查询分期信息
        String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
        if (borrowStyle != null) {
            if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
                ProjectRepayListBean bean = new ProjectRepayListBean();
                bean.setUserId(userId);
                bean.setBorrowNid(form.getBorrowNid());
                bean.setNid(form.getNid());
                int recordTotal = this.mytenderService.countProjectRepayPlanRecordTotal(bean);
                if (recordTotal > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
					List<WebProjectRepayListCustomize> fqList = mytenderService.selectProjectRepayPlanList(bean,
							paginator.getOffset(), paginator.getLimit());
                    contents.put("paginator", paginator);
                    contents.put("repayList", fqList);
                } else {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                    contents.put("paginator", paginator);
                    contents.put("repayList", "");
                }
            }
        }

        File file = null;
        try {
			file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                            CustomConstants.TENDER_CONTRACT, contents);
        } catch (Exception e) {
            LogUtil.errorLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF  生成PDF文件", e);
            e.printStackTrace();
        }

        LogUtil.endLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF 生成PDF文件");
        return file;

    }
}
