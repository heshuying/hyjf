/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.vip;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.auto.VipInfoExample;

@Service
public class VipServiceImpl extends BaseServiceImpl implements VipService {

    @Override
    public VipDetailResultBean userVipDetailInit(Integer userId, HttpServletRequest request) {
        VipDetailResultBean vipResultBean=new VipDetailResultBean();
        try {
            
            String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
            imghost = imghost.substring(0, imghost.length() - 1);// http://cdn.huiyingdai.com/
            String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.api.web.url")) + BaseDefine.REQUEST_HOME.substring(1, BaseDefine.REQUEST_HOME.length()) + "/";
            webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
            String iconUrl = "";
            
            //获取用户头像
            UsersExample usersExample = new UsersExample();
            usersExample.createCriteria().andUserIdEqualTo(userId);
            List<Users> userslist = usersMapper.selectByExample(usersExample);
            if (userslist != null && userslist.size() > 0) {
                iconUrl = userslist.get(0).getIconurl();
                vipResultBean.setUsername(userslist.get(0).getUsername());
            }
            
            UsersInfoExample usersInfoExample = new UsersInfoExample();
            usersInfoExample.createCriteria().andUserIdEqualTo(userId);
            List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoExample);
            if (usersInfoList != null && usersInfoList.size() > 0) {
                //获取用户头像
                // 上传文件的CDNURL
                if (StringUtils.isNotEmpty(iconUrl)) {
                    // 实际物理路径前缀2
                    String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));
                    vipResultBean.setIconUrl(imghost + fileUploadTempPath + iconUrl);
                } else {
                    if (usersInfoList.get(0).getSex() == null || usersInfoList.get(0).getSex().intValue() == 1) {
                        vipResultBean.setIconUrl(webhost + "/img/" + "icon_man.png");
                    } else {
                        vipResultBean.setIconUrl(webhost + "/img/" + "icon_woman.png");
                    }
                }
                //获取用户名
                if (usersInfoList.get(0).getTruename() != null) {
                    vipResultBean.setUsername(usersInfoList.get(0).getTruename());
                }
                //获取vip等级图片
                if(usersInfoList.get(0).getVipId()!=null&&usersInfoList.get(0).getVipId()>0){
                    VipInfo vipInfo=vipInfoMapper.selectByPrimaryKey(usersInfoList.get(0).getVipId());
                    //vip名称显示
                    vipResultBean.setVipName(vipInfo.getVipName());
                    vipResultBean.setVipLevel(vipInfo.getVipLevel()+"");
                    VipInfoExample example=new VipInfoExample();
                    example.createCriteria().andDelFlgEqualTo(0);
                    int count=vipInfoMapper.countByExample(example);
                    if(count==usersInfoList.get(0).getVipId()){
                        VipInfo nextVipInfo=vipInfoMapper.selectByPrimaryKey(usersInfoList.get(0).getVipId());
                        //获取当前用户vip升级上限
                        vipResultBean.setVipValueUp(nextVipInfo.getVipValue()+"");
                        vipResultBean.setIfVipLevelUp("1");
                        vipResultBean.setVipUpgradeRemarks("您已是最高等级会员");
                    }else{
                      
                        VipInfo nextVipInfo=vipInfoMapper.selectByPrimaryKey(usersInfoList.get(0).getVipId()+1);
                        vipResultBean.setIfVipLevelUp("0");
                        //获取当前用户vip升级上限
                        vipResultBean.setVipValueUp(nextVipInfo.getVipValue()+"");
                        //获取当前用户vip升级上限
                        vipResultBean.setVipUpgradeRemarks("还差"+(nextVipInfo.getVipValue()-usersInfoList.get(0).getVipValue())+"点成长值升级至"+nextVipInfo.getVipName());  
                    }
                    
                }
                //获取用户vip到期时间
                if(usersInfoList.get(0).getVipExpDate()!=null){
                    vipResultBean.setVipExpDate(GetDate.times10toStrYYYYMMDD(usersInfoList.get(0).getVipExpDate()));
                }
                
                //获取用户vip到期时间
                if(usersInfoList.get(0).getVipValue()!=null){
                    vipResultBean.setVipValue(usersInfoList.get(0).getVipValue()+"");
                    
                }
                vipResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
                vipResultBean.setStatusDesc("");
            }else{
                vipResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                vipResultBean.setStatusDesc("用户信息不存在");
            }
        
        } catch (Exception e) {
            vipResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            vipResultBean.setStatusDesc("系统异常");
            throw e;
        }
        return vipResultBean;
        
    }

    @Override
    public VipLevelCaptionResultBean vipLevelCaptionInit(Integer userId, HttpServletRequest request) {
        VipLevelCaptionResultBean vipLevelCaptionResultBean=new VipLevelCaptionResultBean();
        try {
            
            UsersInfoExample usersInfoExample = new UsersInfoExample();
            usersInfoExample.createCriteria().andUserIdEqualTo(userId);
            List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoExample);
            if (usersInfoList != null && usersInfoList.size() > 0) {
               
                //获取vip等级图片
                if(usersInfoList.get(0).getVipId()!=null&&usersInfoList.get(0).getVipId()>0){
                    VipInfo vipInfo=vipInfoMapper.selectByPrimaryKey(usersInfoList.get(0).getVipId());
                    //vip名称显示
                    vipLevelCaptionResultBean.setVipName(vipInfo.getVipName());
                    //vip等级
                    vipLevelCaptionResultBean.setVipLevel(vipInfo.getVipLevel()+"");
                    //获取当前用户vip升级上限
                    vipLevelCaptionResultBean.setVipValue(vipInfo.getVipValue()+"");
                }
                //获取用户vip到期时间
                if(usersInfoList.get(0).getVipValue()!=null){
                    vipLevelCaptionResultBean.setVipValue(usersInfoList.get(0).getVipValue()+"");
                }
                VipInfoExample vipInfoExample=new VipInfoExample();
                vipInfoExample.createCriteria().andDelFlgEqualTo(0);
                List<VipInfo> vipInfoList=vipInfoMapper.selectByExample(vipInfoExample);
                formtVipName(vipInfoList);
                vipLevelCaptionResultBean.setVipInfoList(vipInfoList);
                
                vipLevelCaptionResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
                vipLevelCaptionResultBean.setStatusDesc("");
                return vipLevelCaptionResultBean;
            }else{
                vipLevelCaptionResultBean.setStatus(BaseResultBean.STATUS_FAIL);
                vipLevelCaptionResultBean.setStatusDesc("用户信息不存在");
                return vipLevelCaptionResultBean;
            }
            
            
        } catch (Exception e) {
            throw e;
        }
    }

    private void formtVipName(List<VipInfo> vipInfoList) {
        for (VipInfo vipInfo : vipInfoList) {
            vipInfo.setVipName(vipInfo.getVipName().substring(0,2));
        }
        
    }
}
