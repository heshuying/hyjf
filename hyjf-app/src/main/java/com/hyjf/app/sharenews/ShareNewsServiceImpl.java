package com.hyjf.app.sharenews;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Invite;
import com.hyjf.mybatis.model.auto.InviteExample;
@Service
public class ShareNewsServiceImpl extends BaseServiceImpl implements ShareNewsService {

	/**
	 * 获取分享信息
	 * @return
	 */
	public ShareNewsBean queryShareNews(){
		InviteExample ie = new InviteExample();
		List<Invite> result= inviteMapper.selectByExampleWithBLOBs(ie);
		
		ShareNewsBean shareNewsBean= new ShareNewsBean();
		if(result!=null && result.size()>0){
			Invite invite= result.get(0);
			shareNewsBean.setTitle(invite.getTitle());
			shareNewsBean.setContent(invite.getContent());
			shareNewsBean.setImg(invite.getImg());
			shareNewsBean.setLinkUrl(invite.getLinkUrl());
		}
		return shareNewsBean;
	}
	
}




