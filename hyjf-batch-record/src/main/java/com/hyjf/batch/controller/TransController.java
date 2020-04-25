package com.hyjf.batch.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.util.SegFile;
import com.hyjf.batch.util.TransUtil;
import com.hyjf.common.util.PropUtils;

/**
 * 数据迁移
 * 
 * @author Tony
 */
@Controller
@RequestMapping(value = "/dataTransfer")
public class TransController {

	private static final Logger LOG = LoggerFactory.getLogger(TransController.class);

	@Autowired
	private JobLauncher jobLauncher;
	
	// 开户请求JOB
	@Autowired
	private Job userAccountJob;

	// 开户结果JOB
    @Autowired  
    private Job userAccountResultJob;  
    
    // 债权迁移请求JOB
    @Autowired  
    private Job debtTransferJob; 
    
    // 债权迁移结果JOB
    @Autowired  
    private Job debtTransferResultJob;
    
	// 标的迁移请求JOB
	@Autowired
	private Job subjectTransferJob;

	// 标的迁移结果JOB
	@Autowired
	private Job subjectTransferResultJob;
	
	//签约关系迁移请求JOB
	@Autowired
	private Job sigtranTransferJob;
	
	//签约关系迁移结果JOB
	@Autowired
	private Job sigtranTransferResultJob;
	
	@Autowired
	private JobParametersBuilder jobParameterBulider;
	/** 服务器地址  */
	public static String HOST = PropUtils.getSystem("hyjf.file.server.host").trim();
	
	/** 文件生成地址  */
	public static String CREATE_FILE = PropUtils.getSystem("hyjf.file.server.upload.url").trim();
	
	/** 文件结果地址  */
	public static String RESULT_FILE = PropUtils.getSystem("hyjf.file.server.download.url").trim();
	
	/**
	 * 批量开户请求
	 */
	@ResponseBody
	@RequestMapping(value = "/batchOpenAccountRequset", produces = "application/json; charset=utf-8")
	public JSONObject batchOpenAccountRequset(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("批量开户生成文件开始...");
		JSONObject ret = new JSONObject();
		//生成文件名
		String fileName = TransUtil.createFilesName(0);
		try {
			jobParameterBulider.addDate("date", new Date());
			jobParameterBulider.addString("outputFilePath", CREATE_FILE +  fileName);
			jobLauncher.run(userAccountJob, jobParameterBulider.toJobParameters());
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "生成文件异常");
			return ret;
		}
		ret.put("status", true);
		ret.put("error", "生成文件成功：" + fileName);
		LOG.info("批量开户生成文件结束...");
		return ret;
	}

	/**
	 * 批量开户结果
	 */
	@ResponseBody
	@RequestMapping(value = "/batchOpenAccountResult", produces = "application/json; charset=utf-8")
	public JSONObject batchOpenAccountResult(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("批量开户处理结果文件开始...");
		JSONObject ret = new JSONObject();
		String fileName = request.getParameter("fileName");
		if(StringUtils.isBlank(fileName)){
			ret.put("status", false);
			ret.put("error", "文件名称不可为空");
			return ret;
		}
        try{
        	jobParameterBulider.addDate("date", new Date());  
        	jobParameterBulider.addString("accountResultFileUrl", RESULT_FILE + fileName);
            jobLauncher.run(userAccountResultJob, jobParameterBulider.toJobParameters());  
		}catch(Exception e){
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "处理批量开户结果异常");
			return ret;
		}
    	ret.put("status", true);
		ret.put("error", "处理批量开户结果文件成功：" + fileName);
        LOG.info("批量开户处理结果文件结束...");   
		return ret;
	}

	/**
	 * 债权迁移请求
	 */
	@ResponseBody
	@RequestMapping(value = "/debtTransferRequest", produces = "application/json; charset=utf-8")
	public JSONObject debtTransferRequest(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("债权迁移请求生成文件开始...");
		JSONObject ret = new JSONObject();
		//生成文件名
		String fileName = TransUtil.createFilesName(1);
		try {
			jobParameterBulider.addDate("date", new Date());
			jobParameterBulider.addString("debtTransferFilePath",CREATE_FILE + fileName);
			jobLauncher.run(debtTransferJob, jobParameterBulider.toJobParameters());
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "生成债权迁移文件异常");
			return ret;
		}
		ret.put("status", true);
		ret.put("error", "生成债权迁移文件成功：" + fileName);
		LOG.info("债权迁移请求生成文件结束...");
		return ret;
	}

	/**
	 * 债权迁移结果
	 */
	@ResponseBody
	@RequestMapping(value = "/debtTransferResult", produces = "application/json; charset=utf-8")
	public JSONObject debtTransferResult(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("债权迁移结果文件开始...");
		JSONObject ret = new JSONObject();
		String fileName = request.getParameter("fileName");
		if(StringUtils.isBlank(fileName)){
			ret.put("status", false);
			ret.put("error", "文件名称不可为空");
			return ret;
		}
        try{
        	jobParameterBulider.addDate("date", new Date());  
        	jobParameterBulider.addString("debtTransferResultFileUrl", RESULT_FILE + fileName);
            jobLauncher.run(debtTransferResultJob, jobParameterBulider.toJobParameters());  
		}catch(Exception e){
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "处理债权迁移结果异常");
			return ret;
		}
    	ret.put("status", true);
		ret.put("error", "处理债权迁移结果成功：" + fileName);
        LOG.info("债权迁移结果文件结束...");  
		return ret;
	}

	/**
	 * 标的迁移请求
	 */
	@ResponseBody
	@RequestMapping(value = "/subjectTransferRequest", produces = "application/json; charset=utf-8")
	public JSONObject subjectTransferRequest(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("标的迁移生成文件开始...");
		JSONObject ret = new JSONObject();
		String fileName = TransUtil.createFilesName(2);
		if(StringUtils.isBlank(fileName)){
			ret.put("status", false);
			ret.put("error", "生成标的迁移文件名称异常");
			return ret;
		}
		try {
			jobParameterBulider.addDate("date", new Date());
			jobParameterBulider.addString("transferFileUrl",CREATE_FILE  + fileName);
			jobLauncher.run(subjectTransferJob, jobParameterBulider.toJobParameters());
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "生成标的迁移文件异常");
			return ret;
		}
		ret.put("status", true);
		ret.put("error", "生成标的迁移文件成功：" + fileName);
		LOG.info("标的迁移生成文件结束...");
		return ret;
	}

	/**
	 * 标的迁移结果
	 */
	@ResponseBody
	@RequestMapping(value = "/subjectTransferResult", produces = "application/json; charset=utf-8")
	public JSONObject subjectTransferResult(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("标的迁移结果文件处理开始...");
		JSONObject ret = new JSONObject();
		String fileName = request.getParameter("fileName");
		if(StringUtils.isBlank(fileName)){
			ret.put("status", false);
			ret.put("error", "文件名称不可为空");
			return ret;
		}
		try {
			jobParameterBulider.addDate("date", new Date());
			jobParameterBulider.addString("transferResultFileUrl",RESULT_FILE + fileName);
			jobLauncher.run(subjectTransferResultJob, jobParameterBulider.toJobParameters());
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "处理标的迁移结果文件异常");
			return ret;
		}
		ret.put("status", true);
		ret.put("error", "处理标的迁移结果文件成功：" + fileName);
		LOG.info("标的迁移结果文件处理结束...");
		return ret;
	}
    
	/**
	 * 签约关系迁移请求   
	 */
	@ResponseBody
	@RequestMapping(value = "/sigtranTransferRequest",produces = "application/json;chatset=utf-8")
	public JSONObject sigtranTransferJob(HttpServletRequest request, HttpServletResponse response){
		LOG.info("签约关系迁移请求文件生成开始...");
		JSONObject ret = new JSONObject();
		String fileName = TransUtil.createFilesName(3);
		if(StringUtils.isBlank(fileName)){
			ret.put("status", false);
			ret.put("error", "生成签约关系文件名称异常");
			return ret;
		}
		try{
			jobParameterBulider.addDate("date", new Date());
			jobParameterBulider.addString("sigtranTransferFileUrl",CREATE_FILE + TransUtil.createFilesName(3));
			jobLauncher.run(sigtranTransferJob, jobParameterBulider.toJobParameters());
		}catch(Exception e){
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "生成签约关系文件异常");
			return ret;
		}
		ret.put("status", false);
		ret.put("error", "生成签约关系文件成功：" + fileName);
		LOG.info("签约关系迁移请求生成文件结束...");
	    return ret;	
	}
	
	/**
	 *签约关系迁移结果
	 */
	@ResponseBody
	@RequestMapping(value = "/sigtranTransferResult",produces = "application/json;chatset=utf-8")
	public JSONObject sigtranTransferResult(HttpServletRequest request, HttpServletResponse response){
		LOG.info("签约关系迁移处理结果文件开始...");
		JSONObject ret = new JSONObject();
		String fileName = request.getParameter("fileName");
		if(StringUtils.isBlank(fileName)){
			ret.put("status", false);
			ret.put("error", "文件名称不可为空");
			return ret;
		}
		try{
			jobParameterBulider.addDate("date",new Date());
			jobParameterBulider.addString("sigtranTransferResultFileUrl",RESULT_FILE + fileName);
			jobLauncher.run(sigtranTransferResultJob, jobParameterBulider.toJobParameters());
		}catch(Exception e){
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "处理签约关系结果文件异常");
			return ret;
		}
		ret.put("status", true);
		ret.put("error", "处理签约关系结果文件成功：" + fileName);
		LOG.info("签约关系迁移结果文件结束...");
		return ret;
	}
	
	
	/**
	 *分割文件
	 */
	@ResponseBody
	@RequestMapping(value = "/segFile",produces = "application/json;chatset=utf-8")
	public JSONObject segFile(HttpServletRequest request, HttpServletResponse response){
		LOG.info("分割文件开始...");
		JSONObject ret = new JSONObject();
		String lineNum = request.getParameter("lineNum");
		String fileName = request.getParameter("fileName");
		String fileType = request.getParameter("fileType"); // fileType 0 批量开户 1 债权迁移 2 标的迁移 3 签约关系迁移
		String fileNames = "";//分割文件名
		if(StringUtils.isBlank(lineNum) || StringUtils.isBlank(fileName) || StringUtils.isBlank(fileType)){
			ret.put("status", false);
			ret.put("error", "参数不正确");
			return ret;
		}
		try{
			fileNames = SegFile.start(Integer.parseInt(lineNum), fileName, Integer.parseInt(fileType));
		}catch(Exception e){
			e.printStackTrace();
			ret.put("status", false);
			ret.put("error", "分割文件异常，请查看日志");
			return ret;
		}
		ret.put("status", true);
		ret.put("error", fileNames);
		LOG.info("分割文件结束...");
		return ret;
	}
	
}
