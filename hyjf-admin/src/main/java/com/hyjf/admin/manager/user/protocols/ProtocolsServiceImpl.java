package com.hyjf.admin.manager.user.protocols;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.FddTemplet;
import com.hyjf.mybatis.model.auto.FddTempletExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.FddTempletCustomize;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import com.hyjf.pay.lib.fadada.util.DzqzCallUtil;
import com.hyjf.pay.lib.fadada.util.DzqzConstant;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class ProtocolsServiceImpl extends BaseServiceImpl implements ProtocolsService {

	Logger _log = LoggerFactory.getLogger(this.getClass());

	private static final String TEMPLET_ID_REFIX = "HYHT";

	@Override
	public int countRecord() {
		return fddTempletCustomizeMapper.countRecord();
	}

	/**
	 * 获取列表列表
	 *
	 * @return
	 */
	@Override
	public List<FddTempletCustomize> getRecordList(int limitStart, int limitEnd) {
		FddTempletCustomize fddTemplet = new FddTempletCustomize();
		if (limitStart != -1) {
			fddTemplet.setLimitStart(limitStart);
			fddTemplet.setLimitEnd(limitEnd);
		}
		return fddTempletCustomizeMapper.getRecordList(fddTemplet);
	}

	/**
	 * 获取当前最大模版编号
	 *
	 * @return
	 */
	private String getMaxTempletId(Integer protocolType) {
		String templetId = null;
		FddTempletCustomize fddTempletCustomizeF = new FddTempletCustomize();
		FddTempletCustomize fddTemplet = new FddTempletCustomize();

		fddTemplet.setProtocolType(protocolType);
		List<FddTempletCustomize> list = fddTempletCustomizeMapper.getMaxTempletId(fddTemplet);
		if (list != null && list.size() == 1){
			templetId = list.get(0).getTempletId();
		}
		return templetId;
	}


	/**
	 * 获取信息
	 * @param id
	 * @return
	 */
	@Override
	public FddTemplet getRecordInfo(Integer id) {
		return this.fddTempletMapper.selectByPrimaryKey(id);
	}

	/**
	 * 取得新规的模板编号(追后4位累加1)
	 * @param protocolType
	 * @return
	 */
	@Override
	public String getNewTempletId(Integer protocolType) {
		String templetId = null;
		templetId = getMaxTempletId(protocolType);
		if (templetId == null){
			return TEMPLET_ID_REFIX + String.format("%02d",protocolType) + String.format("%04d",1);
		}
		String preFixStr = templetId.substring(0, templetId.length()-4);
		String postSNStr = templetId.substring(templetId.length()-4);
		postSNStr = String.format("%04d",Integer.parseInt(postSNStr) + 1);
		return preFixStr + postSNStr;
	}

	/**
	 * MultipartFile 转换成File
	 *
	 * @param multfile 原文件类型
	 * @return File
	 * @throws IOException
	 */
	private File multipartToFile(MultipartFile multfile) throws IOException {
		CommonsMultipartFile cf = (CommonsMultipartFile)multfile;
		//这个myfile是MultipartFile的
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File file = fi.getStoreLocation();
		//手动创建临时文件private
		if(file.length() < 2024){
			File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +
					file.getName());
			multfile.transferTo(tmpFile);
			return tmpFile;
		}
		return file;
	}

	/**
	 * 调用发大大模板上传接口
	 * @param multfile
	 * @param templetId
	 * @return
	 * @throws IOException
	 */
	@Override
	public DzqzCallBean uploadtemplateDZApi(MultipartFile multfile, String templetId) throws IOException {
		//MultipartFile 转换成File
		File file = multipartToFile(multfile);
		//参数生成
		DzqzCallBean bean = new DzqzCallBean();
		bean.setUserId(Integer.parseInt(ShiroUtil.getLoginUserId()));
		bean.setTxCode("uploadtemplate");
		bean.setApp_id(DzqzConstant.HYJF_FDD_APP_ID);
		bean.setV(DzqzConstant.HYJF_FDD_VERSION);
		bean.setTimestamp(GetDate.getDate("yyyyMMddHHmmss"));
		bean.setTemplate_id(templetId);
		bean.setFile(file);
		bean.setDoc_url("");
		//调用接口
		return DzqzCallUtil.callApiBg(bean);
	}

	/**
	 * 调用发大大模板上传接口
	 * @param url
	 * @param templetId
	 * @return
	 * @throws IOException
	 */
	@Override
	public DzqzCallBean uploadtemplateDZApi(String url, String templetId) throws IOException {
		//参数生成
		DzqzCallBean bean = new DzqzCallBean();
		bean.setUserId(Integer.parseInt(ShiroUtil.getLoginUserId()));
		bean.setTxCode("uploadtemplate");
		bean.setApp_id(DzqzConstant.HYJF_FDD_APP_ID);
		bean.setV(DzqzConstant.HYJF_FDD_VERSION);
		bean.setTimestamp(GetDate.getDate("yyyyMMddHHmmss"));
		bean.setTemplate_id(templetId);
		bean.setFile(null);
		bean.setDoc_url(url);
		//调用接口
		return DzqzCallUtil.callApiBg(bean);
	}

	@Override
	public int insertRecord(ProtocolsBean form) {
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		FddTemplet record = new FddTemplet();
		BeanUtils.copyProperties(form, record);
		int nowTime = GetDate.getNowTime10();
		record.setCertificateTime(nowTime);//认证时间
		// 登陆信息
		record.setCreateTime(nowTime);
		record.setCreateUserId(Integer.parseInt(adminSystem.getId()));
		record.setCreateUserName(adminSystem.getUsername());
		return fddTempletMapper.insertSelective(record);
	}

	@Override
	public int updateRecord(ProtocolsBean form) {
		AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
		FddTemplet record = new FddTemplet();
		BeanUtils.copyProperties(form, record);
		int nowTime = GetDate.getNowTime10();
		// 更新信息
		record.setUpdateTime(nowTime);
		record.setCreateUserId(Integer.parseInt(adminSystem.getId()));
		record.setCreateUserName(adminSystem.getUsername());
		return fddTempletMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int countRecordByPK(String templetId) {
		FddTempletExample example = new FddTempletExample();
		FddTempletExample.Criteria cra = example.createCriteria();
		cra.andTempletIdEqualTo(templetId);
		return fddTempletMapper.countByExample(example);
	}

	/**
	 * 上传协议模板文件到Ftp服务器
	 * @param multfile
	 * @param saveDir
	 * @param type
	 * 是否删除上传目录 0：否 1：是
	 * @return
	 */
	@Override
	public String uploadTempletToFtp(MultipartFile multfile, String saveDir, int type) {
		String ftpIP = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_IP);
		String port = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PORT);
		String basePath = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_PDF);
		String password = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PASSWORD);
		String username = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_USERNAME);
		String domain = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_DOMAIN);
		String ftpPath = "ftp://" + ftpIP + ":" + port + basePath + "/" + saveDir;
		String httpPath = domain + basePath + "/" + saveDir;
		String httpUrl = null;
		try {
			_log.info("----------待上传目录：" + multfile.getOriginalFilename());
			File paraentDir = multipartToFile(multfile);
			String upParaFile = paraentDir.getParent();
			if(paraentDir.isDirectory()){

				_log.info("----------待删除目录：" + upParaFile);
				File[] files = paraentDir.listFiles();
				for (File file : files) {
					String fileName = file.getName();
					_log.info("--------循环目录，开始上传文件：" + fileName);
					FileInputStream in = new FileInputStream(file);
					boolean flag = FavFTPUtil.uploadFile(ftpIP, Integer.valueOf(port), username, password,
							basePath, saveDir, fileName, in);
					if (!flag){
						throw new RuntimeException("上传失败!fileName:" + fileName);
					}
				}
			}else{
				String fileName = multfile.getOriginalFilename();
				_log.info("--------开始上传文件：" + fileName + "  上传到：" + httpPath );
				FileInputStream in = new FileInputStream(paraentDir);
				boolean flag = FavFTPUtil.uploadFile(ftpIP, Integer.valueOf(port), username, password,
						basePath, saveDir, fileName, in);
				if (!flag){
					throw new RuntimeException("上传失败!fileName:" + fileName);
				}
				httpUrl = httpPath + "/" + fileName;
			}
			if(type == 1){
				//删除原目录
				FileUtil.deltree(upParaFile);
				_log.info("--------删除原目录：" + upParaFile );
			}
		}catch (Exception e){
			e.printStackTrace();
			_log.info(e.getMessage());
			return null;
		}
		return httpUrl;
	}

	@Override
	public List<MultipartFile> getMultipartFileList(MultipartHttpServletRequest multipartRequest) {
		List<MultipartFile> multipartFileList = new ArrayList<MultipartFile>();

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			if (multipartFile != null){
				multipartFileList.add(multipartFile);
			}
		}
		return multipartFileList;
	}

    @Override
    public boolean updateFddTemplate(String templetId, String httpUrl) {
        // 要更新的数据
        FddTemplet fddTemplet = new FddTemplet();
        fddTemplet.setFileUrl(httpUrl);
        // 检索条件
        FddTempletExample example = new FddTempletExample();
        example.createCriteria().andTempletIdEqualTo(templetId);
        boolean result = this.fddTempletMapper.updateByExampleSelective(fddTemplet, example) > 0 ? true : false;
        return result;
    }

}
