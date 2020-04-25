package com.hyjf.admin.app.maintenance.borrowimage;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.AppBorrowImage;
import com.hyjf.mybatis.model.auto.AppBorrowImageExample;

@Service
public class AppBorrowImageServiceImpl extends BaseServiceImpl implements AppBorrowImageService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<AppBorrowImage> getRecordList(AppBorrowImage config, int limitStart, int limitEnd) {
		AppBorrowImageExample example = new AppBorrowImageExample();
	    AppBorrowImageExample.Criteria crt =example.createCriteria();
	    if (config.getStatus()!=null ) {
	        crt.andIdEqualTo(config.getStatus());   
        }
	    if (config.getStatus()!=null ) {
	        crt.andPageTypeEqualTo(config.getPageType());
	    }
	    if (config.getVersion()!=null ) {
	        crt.andVersionEqualTo(config.getVersion());
	    }
	    if (config.getVersionMax()!=null ) {
	        crt.andVersionMaxEqualTo(config.getVersionMax());
	    }
	    if (config.getId()!=null ) {
	        crt.andIdEqualTo(config.getId());
	    }
	   
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return appBorrowImageMapper.selectByExample(example);
	}

	/**
	 * 获取单个
	 * 
	 * @return
	 */
	public AppBorrowImage getRecord(Integer id) {
	    AppBorrowImageExample example = new AppBorrowImageExample();
	    AppBorrowImageExample.Criteria crt =example.createCriteria();
	    crt.andIdEqualTo(id);
	    List<AppBorrowImage> borrowImages =this.appBorrowImageMapper.selectByExample(example);
	    if(borrowImages.size()>0){
	        return borrowImages.get(0);
	    }
		return null;
	}

	/**
	 * 插入
	 * 
	 * @param record
	 * @throws Exception
	 */
	public void insertRecord(AppBorrowImage record) throws Exception {
		int nowTime = GetDate.getNowTime10();
		record.setAddtime(nowTime);
		record.setBorrowImageUrl(this.moveUploadImage(record));
		appBorrowImageMapper.insertSelective(record);
	}

	/**
	 * 上传图片的信息
	 * 
	 * @param borrowBean
	 * @return
	 * @throws Exception
	 */
	private String moveUploadImage(AppBorrowImage record) throws Exception {

		// 保存的物理路径
		String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
		// 正式保存的路径
		String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path")) + "borrowImage/" + UploadFileUtils.getDoPath(record.getBorrowImage());
		// 临时文件夹
		String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.temp.path"));

		File file = new File(filePhysicalPath + fileUploadRealPath + record.getBorrowImageRealname());
		if (!file.exists()) {
			UploadFileUtils.removeFile4Dir(filePhysicalPath + fileUploadRealPath);
			UploadFileUtils.upload4CopyFile(filePhysicalPath + fileUploadTempPath + record.getBorrowImageRealname(), filePhysicalPath + fileUploadRealPath);
		}

		return fileUploadRealPath + record.getBorrowImageRealname();
	}

	/**
	 * 更新
	 * 
	 * @param record
	 * @throws Exception
	 */
	public void updateRecord(AppBorrowImage record) throws Exception {
		int updatetime = GetDate.getNowTime10();
		record.setUpdatetime(updatetime);
		if (record.getStatus()==null) {
		    record.setBorrowImageUrl(this.moveUploadImage(record)); 
        }
		appBorrowImageMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 删除
	 * 
	 * @param record
	 */
	public void deleteRecord(Integer id) {
	    AppBorrowImageExample example = new AppBorrowImageExample();
        AppBorrowImageExample.Criteria crt =example.createCriteria();
        crt.andIdEqualTo(id);
		this.appBorrowImageMapper.deleteByExample(example);
	}

    @Override
    public AppBorrowImage getRecords(String borrowImage) {
        AppBorrowImageExample example = new AppBorrowImageExample();
        AppBorrowImageExample.Criteria crt =example.createCriteria();
        crt.andBorrowImageEqualTo(borrowImage);
        List<AppBorrowImage> borrowImages =this.appBorrowImageMapper.selectByExample(example);
        if(borrowImages.size()>0){
            return borrowImages.get(0);
        }
        return null;
    }
}
