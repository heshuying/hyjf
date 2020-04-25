/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.nifa.download;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.batch.util.FtpUtil;
import com.hyjf.batch.util.SFTPParameter;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.NifaReportLog;
import com.hyjf.mybatis.model.auto.NifaReportLogExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaDownloadFileServiceImpl, v0.1 2018/7/7 18:33
 */
@Service
public class NifaDownloadFileServiceImpl extends BaseServiceImpl implements NifaDownloadFileService {

    Logger _log = LoggerFactory.getLogger(NifaDownloadFileServiceImpl.class);

    private static String DOWNLOAD_PATH = PropUtils.getSystem("hyjf.nifa.download.path");

    /** 获取上传地址前缀 */
    private static String DOWNLOAD_URL = PropUtils.getSystem("hyjf.nifa.download.url");

    @Override
    public boolean updateNifaReportLog(NifaReportLog nifaReportLog) {
        nifaReportLog.setFeedbackPath(DOWNLOAD_PATH + nifaReportLog.getFeedbackPath());
        boolean result = nifaReportLogMapper.updateByPrimaryKeySelective(nifaReportLog) > 0 ? true : false;
        if (!result) {
            _log.info("【互金上传文件】更新上传日志表失败！Id:" + nifaReportLog.getId());
        }
        return result;
    }

    @Override
    public List<NifaReportLog> selectNifaReportLogDownloadPath() {
        NifaReportLogExample example = new NifaReportLogExample();
        example.createCriteria().andFeedbackResultNotEqualTo(1).andUploadImeLessThan(GetDate.getDayStart10(new Date()));
        List<NifaReportLog> nifaReportLogList = this.nifaReportLogMapper.selectByExample(example);
        if (null != nifaReportLogList && nifaReportLogList.size() > 0) {
            return nifaReportLogList;
        }
        return null;
    }

    @Override
    public Boolean downloadFiles(String filePathDate) {

        SFTPParameter para = new SFTPParameter();
        Boolean re = false;

        //ftp服务器地址
        para.hostName = PropUtils.getSystem("nifa.hostName");
        //ftp服务器用户名
        para.userName = PropUtils.getSystem("nifa.userName");
        //ftp服务器密码
        para.passWord = PropUtils.getSystem("nifa.passWord");
        //ftp服务器端口
        para.port = PropUtils.getSystem("nifa.hostPost") == null ? 0 : Integer.valueOf(PropUtils.getSystem("nifa.hostPost"));
        //ftp服务器文件目录
        para.downloadPath = "/error/" + filePathDate;

        //本地保存目录
        String savePath = "";
        if (DOWNLOAD_PATH.endsWith("/")) {
            savePath = DOWNLOAD_PATH + filePathDate;
        } else {
            savePath = DOWNLOAD_PATH + "/" + filePathDate;
        }

        //如果文件夹不存在则创建
        File file = new File(savePath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        // 设定本地保存目录
        para.savePath = savePath;

        try {
            if (!FtpUtil.downloadFiles(para)) {
                _log.info("【互金下载反馈文件】下载ftp文件失败");
            } else {
                // 下载后文件打压缩包
                StringBuffer sb = new StringBuffer();
                sb.append(savePath + "/err_package.txt,");
                sb.append(savePath + "/file_count.txt,");
                sb.append(savePath + "/err_inv_contract.txt,");
                sb.append(savePath + "/err_general_contract.txt,");
                sb.append(savePath + "/err_pro_repay_record.txt,");
                sb.append(savePath + "/err_contract_state.txt,");
                sb.append(savePath + "/err_inv_return_record.txt,");
                if (writeZip(sb, savePath + "/" + filePathDate)) {
                    re = true;
                } else {
                    _log.info("【互金下载反馈文件】文件压缩失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _log.info("【互金下载反馈文件】下载ftp文件失败");
        }
        return re;
    }

    /**
     * 通过前置程序下载反馈文件
     *
     * @param nifaReportLog
     * @param feedBackType
     * @param filePathDate
     * @return
     */
    @Override
    public boolean downloadFilesByUrl(NifaReportLog nifaReportLog, String feedBackType, String filePathDate) {
        //本地保存目录
        String savePath = "";
        if (DOWNLOAD_PATH.endsWith("/")) {
            savePath = DOWNLOAD_PATH + filePathDate;
        } else {
            savePath = DOWNLOAD_PATH + "/" + filePathDate;
        }

        //如果文件夹不存在则创建
        File file = new File(savePath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }

        savePath.replace(":", "%3A");
        savePath.replace("/", "%2F");

        String requestURL = DOWNLOAD_URL.concat("?systemid=1&stype=").concat(feedBackType)
                // 下载文件的地址
                .concat("&sourcePath=").concat("/feedbackfile/")
                .concat(filePathDate.substring(0,4)).concat("-")
                .concat(filePathDate.substring(4,6)).concat("-")
                .concat(filePathDate.substring(6,8)).concat("/")
                // 下载文件名称
                .concat(nifaReportLog.getUploadName()).concat("1.enc&")
                // 下载路径
                .concat("targetPath=").concat(savePath);

        // 文件上传请求
        String uploadResult = HttpDeal.get(requestURL);
        // 上传结果解析
        JSONObject jsonObject = JSONObject.parseObject(uploadResult);
        if (!"true".equals(jsonObject.get("success"))) {
            _log.error("【互金下载文件】下载错误，返回错信息：" + jsonObject + ",下载请求地址：" + requestURL);
            return false;
        }
        return true;
    }
//
//    public static void main(String[] args) {
//        downloadFiles1("20180726");
//    }
//
//    private static boolean writeZip1(StringBuffer sb, String zipName) {
//        try {
//            String[] files = sb.toString().split(",");
//            OutputStream os = new BufferedOutputStream(new FileOutputStream(zipName + ".zip"));
//            ZipOutputStream zos = new ZipOutputStream(os);
//            byte[] buf = new byte[8192];
//            int len;
//            for (int i = 0; i < files.length; i++) {
//                File file = new File(files[i]);
//                if (!file.isFile()) {
//                    continue;
//                }
//                ZipEntry ze = new ZipEntry(file.getName());
//                zos.putNextEntry(ze);
//                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//                while ((len = bis.read(buf)) > 0) {
//                    zos.write(buf, 0, len);
//                }
//                zos.closeEntry();
//            }
//            zos.closeEntry();
//            zos.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private static Boolean downloadFiles1(String filePathDate) {
//
//        SFTPParameter para = new SFTPParameter();
//        Boolean re = false;
//
//        //ftp服务器地址
//        para.hostName = PropUtils.getSystem("nifa.hostName");
//        //ftp服务器用户名
//        para.userName = PropUtils.getSystem("nifa.userName");
//        //ftp服务器密码
//        para.passWord = PropUtils.getSystem("nifa.passWord");
//        //ftp服务器端口
//        para.port = PropUtils.getSystem("nifa.hostPost") == null ? 0 : Integer.valueOf(PropUtils.getSystem("nifa.hostPost"));
//        //ftp服务器文件目录
//        para.downloadPath = "/error/" + filePathDate;
//        //本地保存目录
//        para.savePath = "D:/"  + filePathDate;
//        //如果文件夹不存在则创建
//        File file = new File(para.savePath);
//        if (!file.exists() && !file.isDirectory()) {
//            file.mkdir();
//        }
//
//        try {
//            if (!FtpUtil.downloadFiles(para)) {
////                _log.info("下载ftp文件失败");
//                System.out.println("下载ftp文件失败");
//            } else {
//                // 下载后文件打压缩包
//                StringBuffer sb = new StringBuffer();
//                sb.append(para.savePath + "/err_package.txt,");
//                sb.append(para.savePath + "/file_count.txt,");
//                sb.append(para.savePath + "/err_inv_contract.txt,");
//                sb.append(para.savePath + "/err_general_contract.txt,");
//                sb.append(para.savePath + "/err_pro_repay_record.txt,");
//                sb.append(para.savePath + "/err_contract_state.txt,");
//                sb.append(para.savePath + "/err_inv_return_record.txt,");
//                if (writeZip1(sb, para.savePath + "/" + filePathDate)) {
//                    re = true;
//                } else {
////                    _log.info("【互金下载反馈文件】文件压缩失败");
//                    System.out.println("文件压缩失败");
//                }
//            }
//        } catch (Exception e) {
////            _log.info("【互金下载反馈文件】下载ftp文件失败");
//            System.out.println("下载ftp文件失败");
//        }
//        return re;
//    }
}
