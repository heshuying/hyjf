package com.hyjf.mybatis.mapper.customize;

public interface UtilMapper {

    int deleteHuiyingdaiUsers(Integer userId);

    int deleteHuiyingdaiUsersInfo(Integer userId);

    int deleteHuiyingdaiAccount(Integer userId);

    int deleteHuiyingdaiAccountBank(Integer userId);

    int deleteHuiyingdaiUsersLog(Integer userId);

    int deleteHuiyingdaiUsersContact(Integer userId);

    int deleteHuiyingdaiSpreadsLog(Integer userId);

    int deleteHuiyingdaiSpreadsUsers(Integer userId);

    int deleteHuiyingdaiSpreadsUsersLog(Integer userId);

}
