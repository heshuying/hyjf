package com.hyjf.admin.manager.config.protocol;


import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.pdf.PdfToHtml;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AdminSystem;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * Created by xiehuili on 2018/5/25.
 */
@Service
public class ProtocolServiceImpl extends BaseServiceImpl implements ProtocolService {
    /**
     * 统计全部个数
     *
     * @return
     */
    @Override
    public Integer countRecord(int limitStart, int limitEnd){
        Integer count=0;
        ProtocolTemplateExample example = new ProtocolTemplateExample();
        ProtocolTemplateExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);
        List<ProtocolTemplate> protocolTemplates=protocolTemplateMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(protocolTemplates)){
            count=protocolTemplates.size();
        }
        return count;
    }
    /**
     * 获取全部列表
     *
     * @return
     */
    @Override
    public List<ProtocolTemplateCommon> getRecordList(ProtocolTemplateBean form,int limitStart, int limitEnd) {
        List<ProtocolTemplateCommon> recordList=new ArrayList<ProtocolTemplateCommon>();
        //查询所有协议
        ProtocolTemplateExample example=new ProtocolTemplateExample();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        ProtocolTemplateExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);
        // 条件查询
        example.setOrderByClause("`id` Desc,`create_time` ASC");
        List<ProtocolTemplate> protocolTemplates=protocolTemplateMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(protocolTemplates)){
            for(int i=0;i< protocolTemplates.size() ;i++){
                ProtocolTemplateCommon protocolTemplateCommon=new ProtocolTemplateCommon();
                //时间显示转换
                Date updateTime= protocolTemplates.get(i).getUpdateTime();
                String time = GetDate.dateToString2(updateTime, "yyyy-MM-dd HH:mm:ss");
                protocolTemplateCommon.setUpdateTime(time);
                protocolTemplateCommon.setProtocolTemplate( protocolTemplates.get(i));
                recordList.add(protocolTemplateCommon);
            }
        }
         return recordList;
    }

    /**
     * 根据协议id查询协议和版本
     *
     * @return
     */
    @Override
    public ProtocolTemplateCommon getProtocolTemplateById(Integer id){
        ProtocolTemplateCommon protocolTemplateCommon=new ProtocolTemplateCommon();
        //根据协议的id查询协议
        ProtocolTemplate protocolTemplate= protocolTemplateMapper.selectByPrimaryKey(id);
        if(protocolTemplate != null){
            //时间显示转换
            Date t= protocolTemplate.getUpdateTime();

            Admin adminTemplate = adminMapper.selectByPrimaryKey(protocolTemplate.getUpdateUserId());
            if(adminTemplate!=null){
                protocolTemplate.setUpdateUserName(adminTemplate.getUsername());
            }else{
                protocolTemplate.setUpdateUserName("admin");
            }

            String time = GetDate.dateToString2(t, "yyyy-MM-dd HH:mm:ss");
            protocolTemplateCommon.setUpdateTime(time);
            protocolTemplateCommon.setProtocolTemplate(protocolTemplate);
            String protocolId= protocolTemplate.getProtocolId();
            if(StringUtils.isNotBlank(protocolId)){
                //根据protocolId查询版本
                ProtocolVersionExample protocolVersionExample=  new ProtocolVersionExample();
                ProtocolVersionExample.Criteria create=protocolVersionExample.createCriteria();
                create.andProtocolIdEqualTo(protocolId);
                List<ProtocolVersion> protocolVersions =protocolVersionMapper.selectByExample(protocolVersionExample);
                if( !CollectionUtils.isEmpty(protocolVersions)){
                    for(int i=0;i<protocolVersions.size();i++){
                        //时间显示转换
                        Date updateTime= null;
                        Integer updateUserId=0;
                        if(protocolVersions.get(i).getUpdateUserId().intValue() != 0){
                            updateTime= protocolVersions.get(i).getUpdateTime();
                            updateUserId= protocolVersions.get(i).getUpdateUserId();
                        }else{
                            updateTime= protocolVersions.get(i).getCreateTime();
                            updateUserId= protocolVersions.get(i).getCreateUserId();
                        }
                        protocolVersions.get(i).setTime(GetDate.dateToString2(updateTime, "yyyy-MM-dd HH:mm:ss"));
                        Admin admin = adminMapper.selectByPrimaryKey(updateUserId);
                        if(adminTemplate!=null){
                            protocolVersions.get(i).setUserName(admin.getUsername());
                        }else{
                            protocolVersions.get(i).setUserName("admin");
                        }

                    }
                    protocolTemplateCommon.setProtocolVersion(protocolVersions);
                }
            }
        }
        return protocolTemplateCommon;
    }

    /**
     * 添加协议模板
     *
     * @return
     */
    @Override
    public void insertProtocolTemplate(ProtocolTemplateCommon form){
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        Integer createUserId = Integer.parseInt(adminSystem.getId());
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
        //年月日，随机数
        Calendar calendar1 = Calendar.getInstance();
        String yearNow = String.valueOf(calendar1.get(Calendar.YEAR)).substring(2);
        String monthNow = calendar1.get(Calendar.MONTH) + 1< 10? "0"+(calendar1.get(Calendar.MONTH) + 1):String.valueOf(calendar1.get(Calendar.MONTH) + 1);
        String dayNow =calendar1.get(Calendar.DATE)<10? "0"+calendar1.get(Calendar.DATE): String.valueOf(calendar1.get(Calendar.DATE));
        int random = (int)(Math.random()*900)+100;
        int randomUrl = (int)(Math.random()*9000)+1000;
        //查询协议模板名称
        ProtocolTemplate protocolTemplate=form.getProtocolTemplate();
        ProtocolTemplateExample example=new ProtocolTemplateExample();
        int num=0;
//        Set<String> set=new HashSet<String>();
        List<ProtocolTemplate> lists=protocolTemplateMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(lists)){
            num=lists.size()+1;
        }
        //协议id规则：Agreement001180526001
        String protocolId="";
        if(protocolTemplate != null){
            //判断删除的协议中是否存在当前协议模板名称Agreement006
            example.createCriteria().andProtocolNameEqualTo(protocolTemplate.getProtocolName()).andStatusEqualTo(0);
            List<ProtocolTemplate> list=protocolTemplateMapper.selectByExample(example);
            if(!CollectionUtils.isEmpty(list)){
                protocolId = list.get(0).getProtocolId().substring(0,12)+ yearNow+monthNow+dayNow+random;
            }else{
                if(num<10){
                    protocolId="Agreement"+"00"+num+ yearNow+monthNow+dayNow+random;
                }else if(num>=10&&num<100){
                    protocolId="Agreement"+"0"+num+ yearNow+monthNow+dayNow+random;
                }else{
                    protocolId="Agreement"+"0"+num+ yearNow+monthNow+dayNow+random;
                }
            }
            String protocolNam= protocolTemplate.getProtocolName();
            String versionNumber= protocolTemplate.getVersionNumber();
            String protocolUrl = protocolTemplate.getProtocolUrl();
            //将pdf转为图片---参数
            String pdfPath=protocolUrl;
            String savePath=pdfPath.substring(0,pdfPath.lastIndexOf("."));
            String imgUrl="";
            //1.保存协议模板
            protocolTemplate.setProtocolId(protocolId);
            protocolTemplate.setCreateUserId(createUserId);
            protocolTemplate.setUpdateUserId(createUserId);
            //将pdf转为图片
            List<String> imgs = PdfToHtml.pdftoImg(fileDomainUrl+pdfPath,fileDomainUrl+savePath,PdfToHtml.IMG_TYPE_JPG);
            if(!CollectionUtils.isEmpty(imgs)){
                String img =  StringUtils.join(imgs.toArray(),",");
                imgUrl=savePath+"-"+img;
            }
            protocolTemplate.setImgUrl(imgUrl);
            protocolTemplateMapper.insertSelective(protocolTemplate);
            //3.新增协议版本
            this.insertProtocolVersion(protocolTemplate,createUserId);
            //4.添加新增协议日志
            this.insertProtocolLog(protocolTemplate,0);
            //将协议模板放入redis中
            RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_URL+protocolTemplate.getProtocolId(),protocolTemplate.getProtocolUrl()+"&"+protocolTemplate.getImgUrl());
            //获取协议模板前端显示名称对应的别名
            String alias = ProtocolEnum.getAlias(protocolTemplate.getProtocolType());
            if(StringUtils.isNotBlank(alias)){
                RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_ALIAS+alias,protocolTemplate.getProtocolId());//协议 ID放入redis
            }
        }
    }

    /**
     * 修改协议模板
     *
     * @return
     */
    @Override
    public void updateProtocolTemplate(ProtocolTemplateCommon form){
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
        Integer updateUserId = Integer.parseInt(adminSystem.getId());
        //1.1修改协议模板
        ProtocolTemplate protocolTemplate = form.getProtocolTemplate();
        if(protocolTemplate != null){
            protocolTemplate.setUpdateUserId(updateUserId);
            //将pdf转为图片---参数
            String pdfPath=protocolTemplate.getProtocolUrl();
            String savePath=pdfPath.substring(0,pdfPath.lastIndexOf("."));
            String imgUrl="";
            //将pdf转为图片
            List<String> imgs = PdfToHtml.pdftoImg(fileDomainUrl+pdfPath,fileDomainUrl+savePath,PdfToHtml.IMG_TYPE_JPG);
            if(!CollectionUtils.isEmpty(imgs)){
                String img =  StringUtils.join(imgs.toArray(),",");
                imgUrl=savePath+"-"+img;
            }
            protocolTemplate.setImgUrl(imgUrl);
            protocolTemplateMapper.updateByPrimaryKeySelective(protocolTemplate);
            //获取将要启用的版本号和协议模板名称
            String protocolName = protocolTemplate.getProtocolName();
            String versionNumber = protocolTemplate.getVersionNumber();
            //之前的版本的启用状态改成不启用
            ProtocolVersionExample examplev = new ProtocolVersionExample();
            ProtocolVersionExample.Criteria criteriav = examplev.createCriteria();
            criteriav.andProtocolIdEqualTo(protocolTemplate.getProtocolId()).andDisplayFlagEqualTo(1);
            List<ProtocolVersion> listsv= protocolVersionMapper.selectByExample(examplev);
            if(!CollectionUtils.isEmpty(listsv)){
                ProtocolVersion protocolVersion=listsv.get(0);
                protocolVersion.setDisplayFlag(0);
//                protocolVersion.setUpdateTime(new Date());
//                protocolVersion.setUpdateUserId(updateUserId);
                protocolVersionMapper.updateByPrimaryKey(protocolVersion);
            }
            //根据协议模板名称和协议版本号查询版本表
            ProtocolVersionExample example = new ProtocolVersionExample();
            ProtocolVersionExample.Criteria criteria = example.createCriteria();
            criteria.andProtocolIdEqualTo(protocolTemplate.getProtocolId()).andVersionNumberEqualTo(versionNumber);
            List<ProtocolVersion> lists = protocolVersionMapper.selectByExample(example);
            if(!CollectionUtils.isEmpty(lists)){
                //2.21 存在，修改协议版本，设置为启用状态，
                ProtocolVersion protocolVersion= lists.get(0);
                protocolVersion.setDisplayFlag(1);
//                protocolVersion.setUpdateTime(new Date());
//                protocolVersion.setUpdateUserId(updateUserId);
                protocolVersionMapper.updateByPrimaryKey(protocolVersion);
            }else{
                //2.31新增协议版本
                this.insertProtocolVersion(protocolTemplate,updateUserId);
            }
            //3.添加修改协议的日志
            this.insertProtocolLog(protocolTemplate,1);
            //将协议模板放入redis中
            RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_URL+protocolTemplate.getProtocolId(),protocolTemplate.getProtocolUrl()+"&"+protocolTemplate.getImgUrl());
            //获取协议模板前端显示名称对应的别名
            String alias = ProtocolEnum.getAlias(protocolTemplate.getProtocolType());
            if(StringUtils.isNotBlank(alias)){
                RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_ALIAS+alias,protocolTemplate.getProtocolId());//协议 ID放入redis
            }

        }
    }

    /**
     * 修改已经存在的协议模板
     *
     * @return
     */
    @Override
    public void updateExistAction(ProtocolVersion form) {
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        Integer updateUserId = Integer.parseInt(adminSystem.getId());
        //通过版本id拿到版本列表
        ProtocolVersion versionList = protocolTemplateMapper.selectVersionById(form.getId());
        //查询上一个版本的模版名称
        ProtocolTemplate protocolTemplateList = protocolTemplateMapper.selectTemplateById(versionList.getProtocolId());
        //1.1修改协议模板
        ProtocolTemplate protocolTemplate = new ProtocolTemplate();
        protocolTemplate.setDisplayName(versionList.getDisplayName());
        protocolTemplate.setRemarks(versionList.getRemarks());
        protocolTemplate.setProtocolId(versionList.getProtocolId());
        protocolTemplate.setVersionNumber(versionList.getVersionNumber());
        protocolTemplate.setProtocolName(protocolTemplateList.getProtocolName());
        protocolTemplate.setProtocolType(protocolTemplateList.getProtocolType());
        protocolTemplate.setProtocolUrl(versionList.getProtocolUrl());
        if(protocolTemplate != null){
            //获得修改人的id
            protocolTemplate.setUpdateUserId(updateUserId);
            String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
            //将pdf转为图片---参数
            String pdfPath=protocolTemplate.getProtocolUrl();
            String savePath=pdfPath.substring(0,pdfPath.lastIndexOf("."));
            String imgUrl="";
            //将pdf转为图片
            List<String> imgs = PdfToHtml.pdftoImg(fileDomainUrl+pdfPath,fileDomainUrl+savePath,PdfToHtml.IMG_TYPE_JPG);
            if(!CollectionUtils.isEmpty(imgs)){
                String img =  StringUtils.join(imgs.toArray(),",");
                imgUrl=savePath+"-"+img;
            }
            protocolTemplate.setImgUrl(imgUrl);

            protocolTemplateMapper.startUseExistProtocol(protocolTemplate);
            //获取将要启用的版本号和协议模板名称
            String protocolName = protocolTemplate.getProtocolName();
            String versionNumber = protocolTemplate.getVersionNumber();
            //之前的版本的启用状态改成不启用
            ProtocolVersionExample examplev = new ProtocolVersionExample();
            ProtocolVersionExample.Criteria criteriav = examplev.createCriteria();
            criteriav.andProtocolIdEqualTo(protocolTemplate.getProtocolId()).andDisplayFlagEqualTo(1);
            List<ProtocolVersion> listsv= protocolVersionMapper.selectByExample(examplev);
            if(!CollectionUtils.isEmpty(listsv)){
                ProtocolVersion protocolVersion=listsv.get(0);
                protocolVersion.setDisplayFlag(0);
                protocolVersionMapper.updateByPrimaryKey(protocolVersion);
            }
            //根据协议模板名称和协议版本号查询版本表
            ProtocolVersionExample example = new ProtocolVersionExample();
            ProtocolVersionExample.Criteria criteria = example.createCriteria();
            criteria.andProtocolIdEqualTo(protocolTemplate.getProtocolId()).andVersionNumberEqualTo(versionNumber);
            List<ProtocolVersion> lists = protocolVersionMapper.selectByExample(example);
            if(!CollectionUtils.isEmpty(lists)){
                //2.21 存在，修改协议版本，设置为启用状态，
                ProtocolVersion protocolVersion= lists.get(0);
                protocolVersion.setDisplayFlag(1);
                protocolVersionMapper.updateByPrimaryKey(protocolVersion);
            }else{
                //2.31新增协议版本
                this.insertProtocolVersion(protocolTemplate,updateUserId);
            }
            //3.添加修改协议的日志
            this.insertProtocolLog(protocolTemplate,1);
            //将协议模板放入redis中
            RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_URL+protocolTemplate.getProtocolId(),protocolTemplate.getProtocolUrl()+"&"+protocolTemplate.getImgUrl());
            //获取协议模板前端显示名称对应的别名
            String alias = ProtocolEnum.getAlias(protocolTemplate.getProtocolType());
            if(StringUtils.isNotBlank(alias)){
                RedisUtils.set(RedisConstants.PROTOCOL_TEMPLATE_ALIAS+alias,protocolTemplate.getProtocolId());//协议 ID放入redis
            }

        }
    }


    /**
     * 删除协议模板
     *
     */
    @Override
    public void deleteProtocolTemplate(Integer id){
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        Integer updateUserId = Integer.parseInt(adminSystem.getId());
        ProtocolTemplateCommon protocolTemplateCommon=new ProtocolTemplateCommon();
        //根据协议的id查询协议
        ProtocolTemplate protocolTemplate= protocolTemplateMapper.selectByPrimaryKey(id);
        if(protocolTemplate != null){
            //将协议模板设置为删除状态状态（0：删除状态）
            ProtocolTemplate protocolTemplateNew=protocolTemplate;
            protocolTemplateNew.setUpdateUserId(updateUserId);
            protocolTemplateNew.setStatus(0);
            protocolTemplateMapper.updateByPrimaryKeySelective(protocolTemplateNew);
            String protocolId= protocolTemplate.getProtocolId();
            if(StringUtils.isNotBlank(protocolId)){
                //根据protocolId查询正在启用的版本号
                ProtocolVersionExample protocolVersionExample=  new ProtocolVersionExample();
                ProtocolVersionExample.Criteria create=protocolVersionExample.createCriteria();
                create.andProtocolIdEqualTo(protocolId).andDisplayFlagNotEqualTo(2);
                List<ProtocolVersion> protocolVersions =protocolVersionMapper.selectByExample(protocolVersionExample);
                if( !CollectionUtils.isEmpty(protocolVersions)){
                    //模板对应的所有版本设置为删除状态
                    for (int i = 0 ; i < protocolVersions.size() ; i++){
                        ProtocolVersion persion = protocolVersions.get(i);
                        persion.setUpdateUserId(updateUserId);
                        persion.setDisplayFlag(2);
                        protocolVersionMapper.updateByPrimaryKey(persion);
                    }
                }
                //3.添加删除协议日志
                this.insertProtocolLog(protocolTemplate,2);
                //将协议模板移除redis中
                RedisUtils.del(RedisConstants.PROTOCOL_TEMPLATE_URL+protocolTemplate.getProtocolId());
                //获取协议模板前端显示名称对应的别名
                String alias = ProtocolEnum.getAlias(protocolTemplate.getDisplayName());
                if(StringUtils.isNotBlank(alias)){
                    RedisUtils.del(RedisConstants.PROTOCOL_TEMPLATE_ALIAS+alias);//删除对应协议ID
                }
            }
        }

    }

    //添加协议版本
    public void insertProtocolVersion(ProtocolTemplate protocolTemplate,Integer createUserId){
        //新增协议版本
        ProtocolVersion protocolVersion=new ProtocolVersion();
        String protocolId = protocolTemplate.getProtocolId();
        int randomUrl = (int)(Math.random()*9000)+1000;
        protocolVersion.setProtocolId(protocolId);
        protocolVersion.setProtocolName(protocolId.substring(0,protocolId.length()-3)+randomUrl+".pdf");
        protocolVersion.setVersionNumber(protocolTemplate.getVersionNumber());
        protocolVersion.setProtocolUrl(protocolTemplate.getProtocolUrl());
        protocolVersion.setRemarks(protocolTemplate.getRemarks());
        protocolVersion.setDisplayName(protocolTemplate.getDisplayName());
        protocolVersion.setCreateUserId(createUserId);
        protocolVersion.setUpdateUserId(createUserId);
        protocolVersionMapper.insertSelective(protocolVersion);
    }

    //添加协议日志
    public void insertProtocolLog(ProtocolTemplate protocolTemplate,Integer operation){
        //3.添加协议日志
        ProtocolLog protocolLog = new ProtocolLog();
        protocolLog.setProtocolId(protocolTemplate.getProtocolId());
        protocolLog.setProtocolName(protocolTemplate.getProtocolName());
        protocolLog.setVersionNumber(protocolTemplate.getVersionNumber());
        protocolLog.setOperation(operation);
        if(operation.intValue() == 0){
            //添加协议时候，设置创建时间
            protocolLog.setCreateUserId(protocolTemplate.getCreateUserId());
            protocolLog.setCreateTime(new Date());
        } else if(operation.intValue() == 1){
            //修改协议时候，设置修改时间
            protocolLog.setUpdateUserId(protocolTemplate.getUpdateUserId());
            protocolLog.setUpdateTime(new Date());
        }
        if(operation.intValue() == 2){
            //删除协议时候，设置删除时间
            protocolLog.setDeleteUserId(protocolTemplate.getUpdateUserId());
            protocolLog.setDeleteTime(new Date());
        }
        protocolLogMapper.insertSelective(protocolLog);

    }

    /**
     * 校验字段是否为唯一
     *
     * @return
     */
    @Override
    public JSONObject validatorFieldCheck(String protocolName,String versionNumber,String displayName,String protocolUrl,String protocolType,String oldDisplayName,String flagT){
        JSONObject json = new JSONObject();

        boolean flag = false;
        //提示信息初始化
        json.put("n_error", "");
        json.put("v_error", "");
        json.put("d_error", "");
        json.put("e_error", "");
        json.put("p_error", "");
        //通过前台输入信息判断展示提示信息
        if(StringUtils.isEmpty(protocolName)){
            json.put("n_error", "协议模板名称不能为空");
            flag =true;
        }else if(StringUtils.isEmpty(versionNumber)) {
            json.put("v_error", "协议版本号不能为空");
            flag =true;
        }else if(StringUtils.isEmpty(displayName)){
            json.put("d_error", "前台展示名称不能为空");
            flag =true;
        }else if(StringUtils.isEmpty(protocolType)){
            json.put("e_error", "协议类别不能为空");
            flag =true;
        }else if(StringUtils.isEmpty(protocolUrl)){
            json.put("p_error", "文件不能为空");
            flag =true;
        }

        if(flag){
            return json;
        }

        if(protocolName.length() > 20 ){
            json.put("n_error", "协议模板名称过长");
            flag =true;
        }
        if(versionNumber.length() > 10 ){
            json.put("v_error", "协议版本号过长");
            flag =true;
        }
        if(!protocolUrl.contains(".pdf")){
            json.put("p_error", "文件格式不对");
            flag = false;
        }

        if(flag){
            return json;
        }

        ProtocolTemplateExample example=new ProtocolTemplateExample();
        ProtocolTemplateExample.Criteria criteria = example.createCriteria();
        List<ProtocolTemplate> lists=null;
       if(StringUtils.isNotBlank(protocolName)){
           criteria.andProtocolNameEqualTo(protocolName).andStatusEqualTo(1);
           lists=protocolTemplateMapper.selectByExample(example);
           if(!CollectionUtils.isEmpty(lists) ) {
               json.put("n_error", "协议模板名称已经存在");
               if(StringUtils.isNotBlank(versionNumber)){
                   ProtocolVersionExample e= new ProtocolVersionExample();
                   ProtocolVersionExample.Criteria c = e.createCriteria();
                   c.andProtocolIdEqualTo(lists.get(0).getProtocolId()).andVersionNumberEqualTo(versionNumber);
                   List<ProtocolVersion> list = protocolVersionMapper.selectByExample(e);
                   if(!CollectionUtils.isEmpty(list) ) {
                       json.put("v_error", "协议版本号已经存在");
                   }
               }
           }
        }
        if(StringUtils.isNotBlank(displayName)){
            List<ProtocolVersion> versionList = null;
            List<ProtocolVersion> versionLists = null;
            ProtocolVersionExample exampleT=new ProtocolVersionExample();
            ProtocolVersionExample.Criteria criteriaT = exampleT.createCriteria();
            criteriaT.andDisplayFlagNotEqualTo(2);
            versionLists = protocolVersionMapper.selectByExample(exampleT);
            criteriaT.andDisplayNameEqualTo(displayName);
            versionList = protocolVersionMapper.selectByExample(exampleT);
            //添加校验
            if(flagT.equals("0") && !CollectionUtils.isEmpty(versionList)) {
                json.put("d_error", "前台展示名称已经存在");
            }
            //修改校验
            if(flagT.equals("1")){
                if(!displayName.equals(oldDisplayName)){
                    ArrayList arrayList = new ArrayList();
                    for (ProtocolVersion ver:versionLists) {
                        arrayList.add(ver.getDisplayName());
                    }
                   if(arrayList.contains(displayName)){
                       json.put("d_error", "前台展示名称已经存在");
                   }
                }
            }
        }
        //是否在枚举中有定义
        String alias = ProtocolEnum.getAlias(protocolType);
        if (StringUtils.isEmpty(alias)) {
            json.put("e_error", "请选择正确的协议类别");
        }else{

            ProtocolTemplateExample exampleT=new ProtocolTemplateExample();
            ProtocolTemplateExample.Criteria criteriaT = exampleT.createCriteria();
            criteriaT.andProtocolTypeEqualTo(protocolType).andStatusEqualTo(1);
            lists=protocolTemplateMapper.selectByExample(exampleT);
            if(!CollectionUtils.isEmpty(lists) ) {
                json.put("e_error", "协议类别已经存在");
            }
        }
        return json;
    }

    /**
     * 图片上传
     *
     * @param request
     * @return
     */
    @Override
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
        String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.temp.path"));
        String logoRealPathDir = filePhysicalPath + fileUploadTempPath;
        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists()) {
            logoSaveFile.mkdirs();
        }
        BorrowCommonImage fileMeta = null;
        LinkedList<BorrowCommonImage> files = new LinkedList<BorrowCommonImage>();

        Iterator<String> itr = multipartRequest.getFileNames();
        MultipartFile multipartFile = null;

        boolean flag =true;
        while (itr.hasNext()) {
            // 文件错误信息
            String errorMessage = null;
            multipartFile = multipartRequest.getFile(itr.next());
            String fileRealName = String.valueOf(new Date().getTime());
            String originalFilename = multipartFile.getOriginalFilename();
            String  suf = UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());
            fileRealName = fileRealName + suf;
            if(StringUtils.isEmpty(suf) ) {
                errorMessage="上传的文件不能是空";
            }
            try {
                //判断上传文件是否是Pdf格式的
                if(!suf.equalsIgnoreCase(".pdf")){
                    errorMessage="上传的文件必须是pdf格式";
                    flag = false;
                }else{
                    Long size=multipartFile.getSize();
                    if(multipartFile.getSize() > 5000000L){
                        errorMessage="上传的文件过大";
                        flag = false;
                    }else if(multipartFile.getSize() < 0L){
                        errorMessage="上传的文件为空";
                        flag = false;
                    }else{
                        errorMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir, multipartFile.getInputStream(), 50000000L);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileMeta = new BorrowCommonImage();
            int index = originalFilename.lastIndexOf(".");
            if (index != -1) {
                fileMeta.setImageName(originalFilename.substring(0, index));
            } else {
                fileMeta.setImageName(originalFilename);
            }

            fileMeta.setImageRealName(fileRealName);
            fileMeta.setImageSize(multipartFile.getSize() / 1024 + "");// KB
            fileMeta.setImageType(multipartFile.getContentType());
            fileMeta.setErrorMessage(errorMessage);
            // 获取文件路径
            fileMeta.setImagePath(fileUploadTempPath + fileRealName);
            fileMeta.setImageSrc(fileDomainUrl + fileUploadTempPath + fileRealName);
            files.add(fileMeta);

        }
        return JSONObject.toJSONString(files, flag);
    }

    /**
     * 获得最新协议模版 前台展示信息
     *
     * @return
     */
    @Override
    public List<ProtocolTemplate> getNewInfo() {
        return protocolTemplateMapper.getdisplayNameDynamic();
    }

}
