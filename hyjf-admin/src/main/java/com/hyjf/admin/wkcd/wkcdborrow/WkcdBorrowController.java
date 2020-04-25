package com.hyjf.admin.wkcd.wkcdborrow;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSON;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.security.utils.AESUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DownloadPictureUtil;
import com.hyjf.common.util.ExportExcel;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.zip.ZIPGenerator;
import com.hyjf.mybatis.model.auto.WkcdBorrow;


@Controller
@RequestMapping(value = WkcdBorrowDefine.REQUEST_MAPPING)
public class WkcdBorrowController extends BaseController {
	@Autowired
	private WkcdBorrowService wkcdBorrowService;

	@RequestMapping(WkcdBorrowDefine.WKCDBORROW_LIST_ACTION)
	@RequiresPermissions(WkcdBorrowDefine.PERMISSIONS_VIEW)
	public ModelAndView searchWkcdBorrowList(HttpServletRequest request,
			@ModelAttribute(WkcdBorrowDefine.WKCDBORROW_FORM) WkcdBorrowBean form) {
		LogUtil.startLog(WkcdBorrowDefine.THIS_CLASS, WkcdBorrowDefine.WKCDBORROW_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(WkcdBorrowDefine.GOTO_LIST);
		// 创建分页
		this.createPage(request, modelAndView, form);
		modelAndView.addObject("wkcdBorrowForm", form);
		LogUtil.endLog(WkcdBorrowDefine.THIS_CLASS, WkcdBorrowDefine.WKCDBORROW_LIST_ACTION);
		return modelAndView;
	}

	/**
	 * 资产汇总
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, WkcdBorrowBean form) {
		int recordTotal = wkcdBorrowService.countRecordTotal(form.getMobile(),form.getUserName(),form.getHyjfStatus());
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			List<WkcdBorrow> recordList = this.wkcdBorrowService.searchRecord(form.getMobile(),form.getUserName(),form.getHyjfStatus(),paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(recordList);
			modelAndView.addObject(WkcdBorrowDefine.WKCDBORROW_FORM, form);
		}
	}
	
	/**
	 * 跳转至详情页面
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(WkcdBorrowDefine.DETAIL)
	public ModelAndView detail(HttpServletRequest request, WkcdBorrowBean form){
		ModelAndView modelAndView = new ModelAndView(WkcdBorrowDefine.GOTO_DETAIL);
		WkcdBorrow wkcdBorrow = wkcdBorrowService.findById(form.getId());
		form.setWkcdId(wkcdBorrow.getWkcdId());
		form.setCheckDesc(wkcdBorrow.getCheckDesc());
		form.setHyjfStatus(wkcdBorrow.getHyjfStatus());
		String token = RedisUtils.get("Third-Party-WKCD-Token");
		modelAndView.addObject(WkcdBorrowDefine.WKCDBORROW_FORM, form);
		modelAndView.addObject("wkcdId",wkcdBorrow.getWkcdId());
		modelAndView.addObject("token",token);
		return modelAndView;
	}
	
	/**
	 * 下载附件
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(WkcdBorrowDefine.DOWNLOAD)
	public void download(HttpServletRequest request,HttpServletResponse response,String id){
		File file = new File(PropUtils.getSystem("file.download.wkcd.real.path")+File.separator+id+File.separator);
        if(!file.exists()){
        	//下载车贷图片至服务器
            try {
				downLoadPic(id);
				file = new File(PropUtils.getSystem("file.download.wkcd.real.path")+File.separator+id+File.separator);
			} catch (Exception e) {
				System.out.println("再次下载微可车贷附件失败...");
			}
            
        }
		List<File> file_array = new LinkedList<>();
		File[] files = file.listFiles();
		Collections.addAll(file_array, files);
		ZIPGenerator.generateZip(response, file_array, id+"");
	}
	
	/**
	 * 审核提交
	 * @return
	 */
	@RequestMapping(WkcdBorrowDefine.VERIFY)
	public String verify(Integer verify,String yijian,Integer id,RedirectAttributesModelMap redirect){
		String userId = ShiroUtil.getLoginUserId();
		Map<String, Object> map = wkcdBorrowService.verify(verify, yijian, id, Integer.valueOf(userId));
		if(verify.equals(2)){
			return "redirect:init";
		}
		if(map == null){
			return "redirect:/manager/wkcd/wkcdborrow/error";
		}
		String borrowNid = wkcdBorrowService.saveBorrowNid(map);
		redirect.addAttribute("borrowNid", borrowNid);
		redirect.addAttribute("moveFlag","BORROW_LIST");
		return "redirect:/manager/borrow/borrowcommon/infoAction";
	}
		
	/**
	 * 跳转至微可车贷详情页
	 * @param wkcdId
	 * @param type
	 * @return
	 */
	@RequestMapping(WkcdBorrowDefine.GOTO_WKCD_PAGE)
	public String gotoWkcdPage(String wkcdId,String type){
		String url = "redirect:" + PropUtils.getSystem("wkcd.host")+"/thirdparty/getBorrowInfo?_partnerId=hyjf&_bid=" + wkcdId + "&_type=" + type + "&_token=" + RedisUtils.get("Third-Party-WKCD-Token");
		return url;
	}

	/**
	 * 导出Excel
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(WkcdBorrowDefine.EXPORT)
	public void exportUserLeaveExcel(@ModelAttribute WkcdBorrowBean form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LogUtil.startLog(WkcdBorrowDefine.THIS_CLASS, WkcdBorrowDefine.EXPORT);
		// 表格sheet名称
		String sheetName = "微可车贷资产汇总";
		// 文件名称
		String fileName = sheetName + StringPool.UNDERLINE + GetDate.getServerDateTime(8, new Date())
				+ CustomConstants.EXCEL_EXT;

		List<WkcdBorrow> recordList = this.wkcdBorrowService.searchRecord(form.getMobile(),form.getUserName(),form.getHyjfStatus(),-1, -1);
		String[] titles = new String[] { "序号", "微可唯一标识", "用户名", "借款人姓名", "手机号", "借款金额", "年化率", "期限", "车牌", "车型", "所属门店", "微可审核状态","汇盈审核状态","对应项目编号","汇盈审核时间"};
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles, sheetName + "_第1页");

		if (recordList != null && recordList.size() > 0) {

			int sheetCount = 1;
			int rowNum = 0;

			for (int i = 0; i < recordList.size(); i++) {
				rowNum++;
				if (i != 0 && i % 60000 == 0) {
					sheetCount++;
					sheet = ExportExcel.createHSSFWorkbookTitle(workbook, titles,
							(sheetName + "_第" + sheetCount + "页"));
					rowNum = 1;
				}

				// 新建一行
				Row row = sheet.createRow(rowNum);
				// 循环数据
				for (int celLength = 0; celLength < titles.length; celLength++) {
					WkcdBorrow wkcdBorrow = recordList.get(i);
					// 创建相应的单元格
					Cell cell = row.createCell(celLength);
					if (celLength == 0) {// 序号
						cell.setCellValue(i + 1);
					} else if (celLength == 1) {
						cell.setCellValue(wkcdBorrow.getWkcdId());
					} else if (celLength == 2) {
						cell.setCellValue(wkcdBorrow.getUserName());
					} else if (celLength == 3) {
						cell.setCellValue(wkcdBorrow.getTruename());
					} else if (celLength == 4) {
						cell.setCellValue(wkcdBorrow.getMobile());
					} else if (celLength == 5) {
						cell.setCellValue(wkcdBorrow.getBorrowAmount().toString());
					} else if (celLength == 6) {
						cell.setCellValue(wkcdBorrow.getApr().toString()); 
					} else if (celLength == 7) {
						cell.setCellValue(wkcdBorrow.getWkcdBorrowPeriod());
					} else if (celLength == 8) {
						cell.setCellValue(wkcdBorrow.getCarNo());
					} else if (celLength == 9) {
						cell.setCellValue(wkcdBorrow.getCarType());
					} else if (celLength == 10) {
						cell.setCellValue(wkcdBorrow.getCarShop());
					} else if (celLength == 11) {
						cell.setCellValue(wkcdBorrow.getWkcdStatus());
					} else if (celLength == 12) {
						String status_hyjf="";
						switch (wkcdBorrow.getHyjfStatus()) {
						case 0:
							status_hyjf="未审核";
							break;
						case 1:
							status_hyjf="审核通过";
							break;
						case 2:
							status_hyjf="审核未通过";
							break;
						default:
							break;
						}
						cell.setCellValue(status_hyjf);
					} else if (celLength == 13) {
						cell.setCellValue(wkcdBorrow.getBorrowNid());
					} else {
						if(wkcdBorrow.getCheckTime()!=null){
							long time = Long.valueOf(wkcdBorrow.getCheckTime()) * 1000;
							Date date = new Date(time);
							SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String s = dateformat.format(date);
							cell.setCellValue(s);
						}
					}
				}
			}
		}
		// 导出
		ExportExcel.writeExcelFile(response, workbook, titles, fileName);
		LogUtil.endLog(WkcdBorrowDefine.THIS_CLASS, WkcdBorrowDefine.EXPORT);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void downLoadPic(String wkcd_id) throws Exception{
		//下载车贷图片至服务器
        String token = RedisUtils.get("Third-Party-WKCD-Token");
        String url = PropUtils.getSystem("wkcd.host")+"/thirdparty/getEnclosureUrl/borrowInfo?_partnerId=hyjf"+"&_token="+token+"&_bid="+wkcd_id;
        String json_result = HttpDeal.get(url);
        Map<String, Object> map_pic = new HashMap<>();
        Map<String, Object> images = new HashMap<>();
        try {
        	 map_pic = JSON.parseObject(json_result, Map.class);
        	 images = JSON.parseObject(AESUtil.decryptAES(map_pic.get("response").toString(),PropUtils.getSystem("wkcd.aes.key")), Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		Iterator iter = images.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			if (val != null) {
				if (val instanceof String && StringUtils.isNotBlank(val.toString())) {
					DownloadPictureUtil.download(val.toString(), DownloadPictureUtil.convert(key.toString()), PropUtils.getSystem("file.download.wkcd.real.path")+File.separator + wkcd_id + File.separator);
				} else if (val instanceof List) {
					List list = (List) val;
					for (int i = 0; i < list.size(); i++) {
						if (StringUtils.isNotBlank(list.get(i).toString())) {
							DownloadPictureUtil.download(list.get(i).toString(), DownloadPictureUtil.convert(key.toString()) + (i + 1), PropUtils.getSystem("file.download.wkcd.real.path")+File.separator + wkcd_id + File.separator);
						}
					}
				} else if(key.toString().equals("carPhoto")){
					 Map<String, Object> carMap = (Map)val;
					 Iterator iterCar = carMap.entrySet().iterator();
					 while(iterCar.hasNext()){
						 Map.Entry entryCar = (Map.Entry) iterCar.next();
						 if(entryCar.getValue()!=null){
							 DownloadPictureUtil.download(entryCar.getValue().toString(), DownloadPictureUtil.convert(entryCar.getKey().toString()), PropUtils.getSystem("file.download.wkcd.real.path")+File.separator + wkcd_id + File.separator);
						 }
					 }
				}
			}
		}   
	}
}
