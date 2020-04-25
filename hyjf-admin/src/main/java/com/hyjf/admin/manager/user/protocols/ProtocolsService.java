package com.hyjf.admin.manager.user.protocols;

import com.hyjf.admin.BaseService;
import com.hyjf.admin.manager.config.borrow.borrowflow.BorrowFlowBean;
import com.hyjf.mybatis.model.auto.FddTemplet;
import com.hyjf.mybatis.model.customize.FddTempletCustomize;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface ProtocolsService extends BaseService {
    /**
     * 获取记录数
     *
     * @return
     */
    public int countRecord();
    /**
     * 获取列表
     *
     * @return
     */
    public List<FddTempletCustomize> getRecordList(int limitStart, int limitEnd);

    /**
     *
     * 根据主键获取详情
     * @author liubin
     * @param Integer id
     * @return
     */
    public FddTemplet getRecordInfo(Integer id);

    /**
     *
     * 取得新规的模板编号
     * @author liubin
     * @param protocolType
     * @return String
     */
    public String getNewTempletId(Integer protocolType);

    /**
     * 调用发大大模板上传接口
     * @param multfile
     * @param templateId
     * @return
     * @throws IOException
     */
    public DzqzCallBean uploadtemplateDZApi(MultipartFile multfile, String templetId) throws IOException;

    /**
     * 调用发大大模板上传接口
     * @param url
     * @param templetId
     * @return
     * @throws IOException
     */
    public DzqzCallBean uploadtemplateDZApi(String url, String templetId) throws IOException;

    /**
     *
     * 插入操作
     * @author liubin
     * @param form
     * @return
     */
    public int insertRecord(ProtocolsBean form);

    /**
     *
     * 更新操作
     * @author liubin
     * @param form
     * @return
     */
    public int updateRecord(ProtocolsBean form);

    /**
     *
     * 根据表的类型,期数,项目类型检索管理费件数
     * @author liubin
     * @param manChargeType
     * @param manChargeTime
     * @param projectType
     * @return
     */
    public int countRecordByPK(String templetId);

    /**
     * 上传协议模板文件到Ftp服务器
     * @param multfile
     * @param saveDir
     * @param type
     * 是否删除上传目录 0：否 1：是
     * @return
     */
    public String uploadTempletToFtp(MultipartFile multfile, String saveDir, int type);

    /**
     * 从request中取得MultipartFile列表
     * @param request
     * @return
     */
    public List<MultipartFile> getMultipartFileList(MultipartHttpServletRequest multipartRequest);

    /**
     * 保存协议FTP上传路径
     * @param templetId
     * @param httpUrl
     * @return
     */
    boolean updateFddTemplate(String templetId, String httpUrl);
}