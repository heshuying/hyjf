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

package com.hyjf.app.user.vip;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.app.BaseDefine;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.auto.VipInfoExample;

@Service("vipService")
public class VipServiceImpl extends BaseServiceImpl implements VipService {

    @Override
    public void userVipDetailInit(ModelAndView modelAndView, Integer userId, HttpServletRequest request) {
        String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        imghost = imghost.substring(0, imghost.length() - 1);// http://cdn.huiyingdai.com/
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
        webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
        String iconUrl = "";
        
        //获取用户头像
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andUserIdEqualTo(userId);
        List<Users> userslist = usersMapper.selectByExample(usersExample);
        if (userslist != null && userslist.size() > 0) {
            iconUrl = userslist.get(0).getIconurl();
            modelAndView.addObject("username",userslist.get(0).getUsername());
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
                modelAndView.addObject("iconUrl",imghost + fileUploadTempPath + iconUrl);
            } else {
                if (usersInfoList.get(0).getSex() == null || usersInfoList.get(0).getSex().intValue() == 1) {
                    modelAndView.addObject("iconUrl",webhost + "/img/" + "icon_man.png");
                } else {
                    modelAndView.addObject("iconUrl",webhost + "/img/" + "icon_woman.png");
                }
            }
            //获取用户名
            if (usersInfoList.get(0).getTruename() != null) {
                modelAndView.addObject("username",usersInfoList.get(0).getTruename());
            }
            //获取vip等级图片
            if(usersInfoList.get(0).getVipId()!=null&&usersInfoList.get(0).getVipId()>0){
                VipInfo vipInfo=vipInfoMapper.selectByPrimaryKey(usersInfoList.get(0).getVipId());
                //vip名称显示
                modelAndView.addObject("vipName",vipInfo.getVipName());
                //vip等级显示图片
                modelAndView.addObject("vipLevelUrl",webhost + "/img/"+"l"+vipInfo.getVipLevel()+".png");
                
                
                VipInfoExample example=new VipInfoExample();
                example.createCriteria().andDelFlgEqualTo(0);
                int count=vipInfoMapper.countByExample(example);
                if(count==usersInfoList.get(0).getVipId()){
                    VipInfo nextVipInfo=vipInfoMapper.selectByPrimaryKey(usersInfoList.get(0).getVipId());
                    //获取当前用户vip升级上限
                    modelAndView.addObject("vipValueUp",nextVipInfo.getVipValue());
                    modelAndView.addObject("ifVipLevelUp","1");
                    modelAndView.addObject("vipUpgradeRemarks","您已是最高等级会员");
                }else{
                  
                    VipInfo nextVipInfo=vipInfoMapper.selectByPrimaryKey(usersInfoList.get(0).getVipId()+1);
                    modelAndView.addObject("ifVipLevelUp","0");
                    //获取当前用户vip升级上限
                    modelAndView.addObject("vipValueUp",nextVipInfo.getVipValue());
                    //获取当前用户vip升级上限
                   modelAndView.addObject("vipUpgradeRemarks","还差"+(nextVipInfo.getVipValue()-usersInfoList.get(0).getVipValue())+"点成长值升级至"+nextVipInfo.getVipName());  
                }
                
            }
            //获取用户vip到期时间
            if(usersInfoList.get(0).getVipExpDate()!=null){
                modelAndView.addObject("vipExpDate",GetDate.times10toStrYYYYMMDD(usersInfoList.get(0).getVipExpDate()));
            }
            
            //获取用户vip到期时间
            if(usersInfoList.get(0).getVipValue()!=null){
                modelAndView.addObject("vipValue",usersInfoList.get(0).getVipValue());
            }
            modelAndView.addObject("url",webhost + VipDefine.REQUEST_MAPPING + VipDefine.VIP_LEVEL_CAPTION_ACTIVE_INIT + packageStr(request));
            modelAndView.addObject("host",webhost);
        }
        
    }

    @Override
    public void vipLevelCaptionInit(ModelAndView modelAndView, Integer userId, HttpServletRequest request) {
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
        webhost = webhost.substring(0, webhost.length() - 1);
        UsersInfoExample usersInfoExample = new UsersInfoExample();
        usersInfoExample.createCriteria().andUserIdEqualTo(userId);
        List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoExample);
        if (usersInfoList != null && usersInfoList.size() > 0) {
           
            //获取vip等级图片
            if(usersInfoList.get(0).getVipId()!=null&&usersInfoList.get(0).getVipId()>0){
                VipInfo vipInfo=vipInfoMapper.selectByPrimaryKey(usersInfoList.get(0).getVipId());
                //vip名称显示
                modelAndView.addObject("vipName",vipInfo.getVipName());
                //vip等级
                modelAndView.addObject("vipLevel",vipInfo.getVipLevel());
                //获取当前用户vip升级上限
                modelAndView.addObject("vipValueUp",vipInfo.getVipValue());
            }
            //获取用户vip到期时间
            if(usersInfoList.get(0).getVipValue()!=null){
                modelAndView.addObject("vipValue",usersInfoList.get(0).getVipValue());
            }

        }
        
        VipInfoExample vipInfoExample=new VipInfoExample();
        vipInfoExample.createCriteria().andDelFlgEqualTo(0);
        List<VipInfo> vipInfoList=vipInfoMapper.selectByExample(vipInfoExample);
        formtVipName(vipInfoList);
        modelAndView.addObject("host",webhost);
        modelAndView.addObject("vipInfoList",vipInfoList);
    }

    private void formtVipName(List<VipInfo> vipInfoList) {
        for (VipInfo vipInfo : vipInfoList) {
            vipInfo.setVipName(vipInfo.getVipName().substring(0,2));
        }
        
    }
    
    // 组装url
    private String packageStr(HttpServletRequest request) {
        StringBuffer sbUrl = new StringBuffer();
        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // token
        String token = request.getParameter("token");
        // 唯一标识
        String sign = request.getParameter("sign");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // Order
        String order = request.getParameter("order");
        sbUrl.append("?").append("version").append("=").append(version);
        sbUrl.append("&").append("netStatus").append("=").append(netStatus);
        sbUrl.append("&").append("platform").append("=").append(platform);
        sbUrl.append("&").append("randomString").append("=").append(randomString);
        sbUrl.append("&").append("sign").append("=").append(sign);
        if(token!=null&&token.length()!=0){
            sbUrl.append("&").append("token").append("=").append(strEncode(token));
        }
        if(order!=null&&order.length()!=0){
            sbUrl.append("&").append("order").append("=").append(strEncode(order));
        }
        
        return sbUrl.toString();
    }

  
}
