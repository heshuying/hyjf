package com.hyjf.admin.maintenance.redis;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.customize.admin.AdminRedisProjectListCustomize;

/**
 * 网站邮件设置页面
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = RedisWriteDefine.REQUEST_MAPPING)
public class RedisWriteController extends BaseController {

	@Autowired
	private RedisWriteService writeService;

	/**
	 * redis写入画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(RedisWriteDefine.INIT)
	@RequiresPermissions(RedisWriteDefine.PERMISSIONS_VIEW)
	public ModelAndView init() {
		// 日志开始
		LogUtil.startLog(RedisWriteController.class.toString(), RedisWriteDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(RedisWriteDefine.WRITE_REDIS_PATH);
		LogUtil.endLog(RedisWriteController.class.toString(), RedisWriteDefine.INIT);
		return modelAndView;
	}

	/**
	 * 写入redis
	 * 
	 * @param modelAndView
	 * @param form
	 */
	//@RequestMapping(RedisWriteDefine.WRITE_REDIS_ACTION)
	@RequiresPermissions(RedisWriteDefine.PERMISSIONS_WRITE_REDIS)
	public ModelAndView updateRedisConfig(HttpServletRequest request) {
		
		// 日志开始
		LogUtil.startLog(RedisWriteController.class.toString(), RedisWriteDefine.WRITE_REDIS_ACTION);
		
		ModelAndView modelAndView = new ModelAndView(RedisWriteDefine.WRITE_REDIS_PATH);
		
		// 标的编号
		String borrowNid = request.getParameter("borrowNid");
		if(StringUtils.isEmpty(borrowNid)){
			modelAndView.addObject("msg", "标的编号不能为空");
			return modelAndView;
		}
		
		// 调用校验
		List<AdminRedisProjectListCustomize> borrows = writeService.searchRedisBorrows(borrowNid);
		if (borrows != null && borrows.size() > 0) {
			try {
				/*for (int i = 0; i < borrows.size(); i++) {
					RedisProjectListCustomize borrow = borrows.get(i);
					writeService.writeToRedis(borrow.getBorrowNid(), new BigDecimal(borrow.getAccountWait()));
				}*/
				AdminRedisProjectListCustomize borrow = borrows.get(0);
				writeService.writeToRedis(borrow.getBorrowNid(), new BigDecimal(borrow.getAccountWait()));
				modelAndView.addObject("msg", "redis重置成功！");
			} catch (Exception e) {
				e.printStackTrace();
			}
			modelAndView.addObject(RedisWriteDefine.SUCCESS, RedisWriteDefine.SUCCESS);
		}
		// 日志结束
		LogUtil.endLog(RedisWriteController.class.toString(), RedisWriteDefine.WRITE_REDIS_ACTION);
		return modelAndView;
	}
}
