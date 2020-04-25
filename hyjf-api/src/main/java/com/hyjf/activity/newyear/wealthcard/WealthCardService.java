package com.hyjf.activity.newyear.wealthcard;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.NewyearCaishenCardQuantity;
import com.hyjf.mybatis.model.auto.NewyearPrizeConfig;
import com.hyjf.mybatis.model.auto.UsersInfo;

public interface WealthCardService extends BaseService {

    NewyearCaishenCardQuantity getUserCardQuantity(String userId);

    UsersInfo getUserInfoByPhone(String phoneNum);

    boolean checkCanGive(String userId, int cardType);

    String getCardName(int cardIdentity);

    public boolean insertCardSend(String userId, int cardType, String phoneNum, Integer cardSendUpdateTime);

    boolean checkCanOpenPrize(String userId);

    int generatePrize();

    boolean insertPrizeDraw(String userId, NewyearPrizeConfig prize, Integer cardUpdateTime);

    NewyearPrizeConfig getPrizeById(int prizeId);

}
