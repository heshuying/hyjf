/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.www.login;

import com.hyjf.admin.employee.employeedimission.EmployeeDimissionController;
import com.hyjf.admin.employee.employeedimission.EmployeeDimissionDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.customize.AdminSystem;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author A2
 * @version RandomValidateCode, v0.1 2018/3/26 12:51
 */
public class RandomValidateCode extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        PictureInitUtil pu = new PictureInitUtil();
        BufferedImage image = new BufferedImage(pu.getWidth(), pu.getHeight(),
                BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();
        // 定义字体样式
        Font myFont = new Font("黑体", Font.BOLD, 16);
        // 设置字体
        g.setFont(myFont);

        g.setColor(pu.getRandomColor(200, 250));
        // 绘制背景
        g.fillRect(0, 0, pu.getWidth(), pu.getHeight());

        g.setColor(pu.getRandomColor(180, 200));
        pu.drawRandomLines(g, 160);
        // 将验证码放入会话中
        Long r = pu.drawRandomLong(10L, g);
        HttpSession session = request.getSession();
        session.setAttribute("validateCode", r);
        LogUtil.infoLog(RandomValidateCode.class.toString(),"doPost","验证码="+r);
        g.dispose();
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
