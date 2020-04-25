package com.hyjf.admin;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.hyjf.common.util.DateEditor;

/**
 * <p>
 * BaseController
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public class BaseController extends MultiActionController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        //binder.registerCustomEditor(String.class, new HtmlCleanEditor(true, true));
        binder.registerCustomEditor(Date.class, new DateEditor(true));
    }

    @ExceptionHandler({ Exception.class, AuthorizationException.class })
    public ModelAndView exception(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        if (e instanceof AuthorizationException) {
            if ("user_disabled".equals(e.getMessage())) {
                modelAndView.setViewName("/error/user_disabled");
            } else {
                modelAndView.setViewName("/error/noperms");
            }

        } else {
            modelAndView.setViewName("/error/systemerror");
        }
        e.printStackTrace();
        return modelAndView;
    }

    /**
     * 多文件上传
     *
     * @param request
     * @return
     */
    public String uploadByName(HttpServletRequest request, String name) {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        String path = this.getServletContext().getRealPath("/upload") + UUID.randomUUID().toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<MultipartFile> mFiles = multiRequest.getFiles(name);
        String fileNames = "";
        String fileName;
        for (int i = 0; i < mFiles.size(); i++) {
            try {
                // 文件名MD5加密
                fileName = (mFiles.get(i).getInputStream())
                        + mFiles.get(i).getOriginalFilename()
                                .substring(mFiles.get(i).getOriginalFilename().lastIndexOf("."));
                fileNames += UUID.randomUUID().toString() + fileName + ",";
            } catch (IOException e1) {
                return null;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
                fileOutputStream.write(mFiles.get(i).getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                return null;
            }
        }
        return fileNames.substring(0, fileNames.length() - 1);
    }

    /**
     * javabean转map
     *
     * @param bean
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> convertBean(Object bean) throws Exception {
        Class type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
}
