/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.user.portrait;

import com.hyjf.mybatis.model.auto.UsersPortrait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author ${yaoy}
 * @version OntimeUserPortraitTask, v0.1 2018/5/9 11:54
 *
 * 定时 员工画像
 */

public class OntimeUserPortraitTask {

    /**
     * 运行状态
     */
    private static int isRun = 0;

    private Logger log = LoggerFactory.getLogger(OntimeUserPortraitTask.class);

    @Autowired
    OntimeUserPortraitService ontimeUserPortraitService;

    /**
     * 定时 员工画像
     */
    public void run() {
        userPortrait();
    }

    private boolean userPortrait() {

        if (isRun == 0) {
            log.info("定时 用户画像 OntimeUserPortraitTask.run 开始...");
            isRun = 1;
            try {
                // 查询登录用户信息
                List<UsersPortrait> usersPortraits = ontimeUserPortraitService.selcetUserList();
                for (UsersPortrait usersPortrait:usersPortraits) {
                    // 更新用户画像表信息
                    log.info("更新用户id ：{}", usersPortrait.getUserId());
                    int count = ontimeUserPortraitService.updateImformation(usersPortrait);
                    log.info("更新条数 ：{}", count);
                    if (count <= 0) {
                       int count1 = ontimeUserPortraitService.insertImformation(usersPortrait);
                        log.info("插入条数 ：{}", count1);
                    }
                }
            } catch (Exception e) {
                log.error("错误：{}",e);
            } finally {
                isRun = 0;
            }
            log.info("定时 用户画像 OntimeUserPortraitTask.run 结束...");
        }
        return false;
    }
}
