package com.hyjf.mqreceiver.fdd.fddDesensitization;


import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddDessenesitizationBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.bank.service.realtimeborrowloan.zhitouloan.RealTimeBorrowLoanService;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.pdf.ImageUtil;
import com.hyjf.common.pdf.PDFToImage;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.FavFTPUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.SFTPParameter;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 法大大自动签署异步处理
 */
@Component(value = "fddDownPDFandDessensitizationMessageHandle")
public class FddDownPDFandDessensitizationMessageHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(this.getClass());

    /** 用户ID */
    private static final String VAL_USERID = "userId";
    /**汇计划名称*/
    private static final String VAL_HJH_TITLE = "val_hjh_title";
    /**预期收益*/
    private static final String VAL_INTEREST = "val_interest";
    /**预期退出时间*/
    private static final String VAL_DATE = "val_date";
    /** 用户名 */
    private static final String VAL_NAME = "val_name";
    /** 性别 */
    private static final String VAL_SEX = "val_sex";
    /** 放款金额 */
    private static final String VAL_AMOUNT = "val_amount";


    @Autowired
    @Qualifier("mailProcesser")
    private MessageProcesser mailMessageProcesser;


    @Autowired
    private FddGenerateContractService fddGenerateContractService;

    @Autowired
    private RealTimeBorrowLoanService realTimeBorrowLoanServiceImpl;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        if(message == null || message.getBody() == null){
            _log.error("【法大大下载脱敏处理任务】接收到的消息为null");
            // 消息队列的指令消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        String msgBody = new String(message.getBody());

        FddDessenesitizationBean bean = null;
        //订单号
        String ordid = null;
        try {
            bean = JSONObject.parseObject(msgBody, FddDessenesitizationBean.class);
            if(Validator.isNull(bean)){
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
            String agrementID = bean.getAgrementID();
            if(agrementID == null){
                throw new RuntimeException("传入参数agrementID不得为空！");
            }
            ordid = bean.getOrdid();
            if(ordid == null){
                throw new RuntimeException("传入参数ordid不得为空！");
            }
            String downloadUrl = bean.getDownloadUrl();
            if(downloadUrl == null){
                throw new RuntimeException("传入参数downloadUrl不得为空！");
            }
            String ftpPath = bean.getFtpPath();
            if(ftpPath == null){
                throw new RuntimeException("传入参数ftpPath不得为空！");
            }
            String savePath = bean.getSavePath();
            if(savePath  == null){
                throw new RuntimeException("传入参数savePath 不得为空！");
            }
            String transType = bean.getTransType();
            if(transType  == null){
                throw new RuntimeException("传入参数transType 不得为空！");
            }
            boolean tenderCompany = bean.isTenderCompany();
            boolean creditCompany = bean.isCreditCompany();
            _log.info("-----------------开始处理法大大下载脱敏，订单号：" + ordid);
            downPDFAndDesensitization(savePath,agrementID,transType,ftpPath,downloadUrl,tenderCompany,creditCompany);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.info("--------------------------------------法大大下载脱敏处理任务异常，订单号：" + ordid + ",错误信息："+ e1.getMessage()+"=============");
            e1.printStackTrace();
            return;
        }
        _log.info("--------------------------------------法大大下载脱敏处理任务结束，订单号：" + ordid + "=============");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     *
     * @param savePath        文件保存地址
     * @param tenderAgreementID 签署数据
     * @param transType 协议类型
     * @param download_url 协议下载地址
     * @param creditCompany
     */
    private void downPDFAndDesensitization(String savePath, String tenderAgreementID, String transType, String ftpPath, String download_url, boolean tenderCompany, boolean creditCompany) {

        String fileName = null;
        String fileType = null;
        if (Integer.valueOf(transType) == FddGenerateContractConstant.PROTOCOL_TYPE_TENDER){
            fileName = FddGenerateContractConstant.CONTRACT_DOC_FILE_NAME_TENDER;
            fileType = "10.png";
        }else if(Integer.valueOf(transType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT
                || Integer.valueOf(transType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(transType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(transType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            fileName = FddGenerateContractConstant.CONTRACT_DOC_FILE_NAME_CREDIT;
            fileType = "2.png";
        }else if(Integer.valueOf(transType) == FddGenerateContractConstant.PROTOCOL_TYPE_PLAN){
            fileName = FddGenerateContractConstant.CONTRACT_DOC_FILE_NAME_PLAN;
            fileType = "6.png";
        }
        try {
            String downFileName =fileName+".pdf";
            FileUtil.downLoadFromUrl(download_url,downFileName,savePath);

            savePath = savePath + "/";
            //获取文件路径
            String filePath = savePath + fileName + ".pdf";
            //上传PDF文件
            uplodTmImage(filePath,ftpPath,0);
            //开始脱敏文件
            //获取文件页数
            PDDocument pdDocument = PDFToImage.pdfInfo(filePath);
            int pages = pdDocument.getNumberOfPages();

            //是否企业用户
            boolean isCompanyUser = false;
            TenderAgreement tenderAgrementInfo = fddGenerateContractService.getTenderAgrementInfo(tenderAgreementID);
            String borrowNid = tenderAgrementInfo.getBorrowNid();
            if(StringUtils.isNotBlank(borrowNid)){
                BorrowWithBLOBs borrow = fddGenerateContractService.getBorrowByNid(borrowNid);
                String planNid = borrow.getPlanNid();
                if(StringUtils.isNotBlank(planNid)){//计划标的

                    Integer borrowUserId = borrow.getUserId();
                    Users users = fddGenerateContractService.getUsers(borrowUserId);
                    Integer userType = users.getUserType();
                    if(1 == userType){
                        isCompanyUser = true;
                    }
                    if(isBorrowMain(borrow)){
                        if("1".equals(borrow.getCompanyOrPersonal())){
                            isCompanyUser = true;
                        }
                    }
                }else{
                    if("1".equals(borrow.getCompanyOrPersonal())){
                        isCompanyUser = true;
                    }
                }
            }

            //拼接URL
            List jointPathList = new ArrayList();
            String imageSavePath = savePath + fileName;
            //转换成图片
            PDFToImage.pdf2img(filePath, imageSavePath, PDFToImage.IMG_TYPE_PNG);
            //签章待脱敏图片地址
            String imageFilePath = imageSavePath +"/"+  fileName + fileType;
            //真实姓名待脱敏图片地址
            String trueImageFilePath = imageSavePath +"/"+  fileName + "0.png";
            _log.info("---------------待脱敏图片地址：" + imageFilePath);
            File tmfile = new File(imageFilePath);
            String upParentDir = tmfile.getParent();
            _log.info("---------------脱敏图片上级目录：" + upParentDir);
            //图片脱敏并存储
            if (FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == Integer.valueOf(transType)){

                tmConduct(imageSavePath,imageFilePath,fileName,isCompanyUser,trueImageFilePath,jointPathList,pages,Integer.valueOf(transType),tenderCompany,creditCompany);

            }else if(Integer.valueOf(transType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                    Integer.valueOf(transType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                    || Integer.valueOf(transType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                    || Integer.valueOf(transType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){

                tmConduct(imageSavePath,imageFilePath,fileName,isCompanyUser,trueImageFilePath,jointPathList,pages,Integer.valueOf(transType),tenderCompany,creditCompany);

            }else{
                for (int i = 0; i < pages; i++) {
                    jointPathList.add(imageSavePath + "/" + fileName + i + ".png");
                    _log.info("---------------------------------拼接图片："+imageSavePath + "/" + fileName + i + ".png");
                }
            }
            //拼接后的PDf路径
            String tmpdfPath  = imageSavePath + "/" + fileName +"_tm.pdf";
            //拼接脱敏图片
            jointPDFimage(jointPathList,imageSavePath + "/pdfimage.png");
            //重新拼接为PDF文件
            replaceImageToPdf(jointPathList,tmpdfPath);

            boolean uploadPDF = uplodTmImage(tmpdfPath, ftpPath, 0);
            if(uploadPDF){
                boolean upResult = uplodTmImage(imageSavePath + "/pdfimage.png",ftpPath,1);
                if(upResult){
                    fddGenerateContractService.updateTenderAgreementImageURL(tenderAgreementID,ftpPath+"pdfimage.png",ftpPath + fileName +"_tm.pdf");
                    // 发送邮件
                    if (Integer.valueOf(transType) == FddGenerateContractConstant.PROTOCOL_TYPE_TENDER){
                        BorrowRecover recover =  this.fddGenerateContractService.selectBorrowRecoverByTenderNid(tenderAgreementID);
                        if (recover != null && StringUtils.isBlank(recover.getAccedeOrderId())){
                            this.sendMail(recover);
                        }
                    }else if(Integer.valueOf(transType) == FddGenerateContractConstant.PROTOCOL_TYPE_PLAN){
                        HjhAccede hjhAccede =  this.fddGenerateContractService.selectHjhAccede(tenderAgrementInfo.getTenderNid());

                        this.sendPlanMail(hjhAccede);
                    }
                }else{
                    _log.info("----------脱敏图片上传失败-----------");
                }
            }else{
                _log.info("----------脱敏PDF上传失败-----------");
            }

        } catch (Exception e) {
            _log.info("------------脱敏协议错误，错误信息" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 判断是否存在借款主体
     * @param borrow
     * @return
     */
    private boolean isBorrowMain(Borrow borrow) {

        boolean result = false;
        String borrowNid = borrow.getBorrowNid();
        BorrowUsers  borrowUsers = fddGenerateContractService.getBorrowUsers(borrowNid);
        if (borrowUsers != null){
            return true;
        }
        BorrowManinfo borrowManinfo = fddGenerateContractService.getBorrowManInfo(borrowNid);
        if (borrowManinfo != null){
            return true;
        }

        return result;
    }

    /**
     * 脱敏位置调整测试类
     * @param args
     */
    public static void main(String[] args) {
        //pdf 转图片

//        String imageSavePath = "/Users/yangchangwei/Downloads/jjfw";
//        //转换成图片
//        String filePath = "/Users/yangchangwei/Downloads/jjfw.pdf";
//        PDFToImage.pdf2img(filePath, imageSavePath, PDFToImage.IMG_TYPE_PNG);

//        调整脱敏地址
//        String output = "/Users/yangchangwei/Downloads/jjfw";
//        String source = "/Users/yangchangwei/Downloads/jjfw/jjfw10.png";    //签章源图片路径
////        String signIcon = "/Applications/work/需求池/脱敏样式/companyname.png"; //签章覆盖图片路径
////        String signIcon = "/Applications/work/需求池/脱敏样式/cardno.png"; //签章覆盖图片路径
//        String signIcon = "/Applications/work/需求池/脱敏样式/namesign.png"; //签章覆盖图片路径
////        String signIcon = "/Applications/work/需求池/脱敏样式/seal.png"; //签章覆盖图片路径
//
//        String signimageName = "tm_0";  //签章脱敏后图片名称
//        String imageType = "png";  //图片类型jpg,jpeg,png,gif
//        Integer degree = null; //水印旋转角度-45，null表示不旋转
//        //转让人/借款人 脱敏签章(个人显示第一个字，企业全部脱敏)——最后一页
//        int index_x = 887;
//        int index_y = 270;
//
//        ImageUtil.markImageByMoreIcon(signIcon, source, output, signimageName, imageType, degree, index_x, index_y);
    }

    /**
     * 脱敏处理
     * @param imageSavePath
     * 图片保存路径
     * @param imageFilePath
     * 签章待脱敏图片路径
     * @param fileName
     * 图片名称
     * @param isCompanyUser
     * 是否企业户
     * @param trueImageFilePath
     * 协议内容待脱敏图片路径
     * @param jointPathList
     * 脱敏后拼接图片列表
     * @param pages
     * pdf页数
     * @param pdfType
     * @param creditCompany
     */
    private void tmConduct(String imageSavePath, String imageFilePath, String fileName, boolean isCompanyUser,
                           String trueImageFilePath, List jointPathList, int pages, int pdfType, boolean isTenderConmpany, boolean creditCompany){
        //出让人、借款人真实姓名脱敏图片
        String borrowTrueNametmImage = "/image/companyname.png";
        //出让人、借款人签章图片
        String borrowSigntmImage = "/image/namesign.png";
        //出让人、借款人身份证号码图片
        String borrowCardNoImage = "/image/cardno.png";

        //承接人、出借人真实姓名脱敏图片
        String tenderTrueNametmImage = "/image/companyname.png";
        //承接人、出借人签章图片
        String tenderSigntmImage = "/image/namesign.png";
        //承接人、出借人身份证号码图片
        String tenderCardNoImage = "/image/cardno.png";

        if(Integer.valueOf(pdfType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            if(creditCompany){//出让人为企业
                borrowTrueNametmImage = "/image/companyname.png";
                borrowSigntmImage = "/image/seal.png";
                borrowCardNoImage = borrowTrueNametmImage;
            }
            if(isTenderConmpany){//承接人为企业
                tenderTrueNametmImage = "/image/companyname.png";
                tenderSigntmImage = "/image/seal.png";
                tenderCardNoImage = tenderTrueNametmImage;
            }

        }else{
            if(isCompanyUser){
                borrowTrueNametmImage = "/image/companyname.png";
                borrowSigntmImage = "/image/seal.png";
                borrowCardNoImage = borrowTrueNametmImage;
            }
            if(isTenderConmpany){
                tenderTrueNametmImage = "/image/companyname.png";
                tenderSigntmImage = "/image/seal.png";
                tenderCardNoImage = tenderTrueNametmImage;
            }
        }
        String tmName_sign = "";
        String tmName_content = "tm_0";
        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == Integer.valueOf(pdfType)){
            tmName_sign = "tm_10";
        }else if(Integer.valueOf(pdfType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            tmName_sign = "tm_2";
        }
        String output = imageSavePath;
        String source = imageFilePath;    //签章源图片路径
        String path = this.getClass().getResource("/").getFile().toString();
        File file = new File(path);
        String fileParent = file.getParent();
        String signIcon = fileParent + borrowSigntmImage; //签章覆盖图片路径
        String tenderSignIcon = fileParent + tenderSigntmImage; //出借人。承接人签章覆盖图片路径
        String signimageName = fileName + tmName_sign;  //签章脱敏后图片名称
        String imageType = "png";  //图片类型jpg,jpeg,png,gif
        Integer degree = null; //水印旋转角度-45，null表示不旋转
        //转让人/借款人 脱敏签章(个人显示第一个字，企业全部脱敏)——最后一页
        int index_x = 0;
        int index_y = 0;
        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == Integer.valueOf(pdfType)){
            index_x = 887;
            index_y = 270;
            if(isCompanyUser){
                index_x = 825;
                index_y = 155;
            }
        }else if(Integer.valueOf(pdfType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            index_x = 410;
            index_y = 1100;
            if(creditCompany){
                index_x = 400;
                index_y = 990;
            }
        }
        ImageUtil.markImageByMoreIcon(signIcon, source, output, signimageName, imageType, degree, index_x, index_y);

        //受让人/出借人/出借人 脱敏签章（个人显示第一个字，企业全部脱敏）——最后一页
        source = output + "/" + signimageName + ".png";
        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == Integer.valueOf(pdfType)){
            index_x = 385;
            index_y = 270;
            if(isTenderConmpany){
                index_x = 405;
                index_y = 270;
            }
        }else if(Integer.valueOf(pdfType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            index_x = 410;
            index_y = 1400;
            if(isTenderConmpany){
                index_x = 400;
                index_y = 1290;
            }
        }
        ImageUtil.markImageByMoreIcon(tenderSignIcon, source, output, signimageName, imageType, degree, index_x, index_y);

        //脱敏转让人/借款人 真实姓名（个人显示第一个字，其他脱敏，企业全部脱敏）——第一页
        String trueImageName = fileName + tmName_content;//真实姓名脱敏后图片
        String trueSource = trueImageFilePath;//待脱敏图片路径
        String icon = fileParent + borrowTrueNametmImage;  //覆盖图片路径
        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == Integer.valueOf(pdfType)){
            index_x = 375;
            index_y = 730;
            if(isCompanyUser){
                index_x = 350;
                index_y = 730;
            }
        }else if(Integer.valueOf(pdfType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            index_x = 390;
            index_y = 470;
            if(creditCompany){
                index_x = 370;
                index_y = 470;
            }
        }
        ImageUtil.markImageByMoreIcon(icon, trueSource, output, trueImageName, imageType, degree, index_x, index_y);

        trueSource = output + "/" + trueImageName + ".png";

        String cardnoIcon = fileParent + borrowCardNoImage;

        //脱敏转让人/借款人 证件号码（个人显示前3后4，企业全部脱敏）——第一页
        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == Integer.valueOf(pdfType)){
            index_x = 340;
            index_y = 795;
            if(isCompanyUser){
                index_x = 305;
                index_y = 795;
            }
        }else if(Integer.valueOf(pdfType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            index_x = 330;
            index_y = 530;
            if(creditCompany){
                index_x = 300;
                index_y = 530;
            }
        }
        ImageUtil.markImageByMoreIcon(cardnoIcon, trueSource, output, trueImageName, imageType, degree, index_x, index_y);

        String tenderTrueNameIcon = fileParent + tenderTrueNametmImage;
        //脱敏受让人/出借人/出借人 真实姓名（个人显示第一个，企业全部脱敏）——第一页
        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == Integer.valueOf(pdfType)){
            index_x = 375;
            index_y = 530;
            if(isTenderConmpany){
                index_x = 238;
                index_y = 530;
            }
        }else if(Integer.valueOf(pdfType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            index_x = 390;
            index_y = 610;
            if(isTenderConmpany){
                index_x = 350;
                index_y = 610;
            }
        }
        ImageUtil.markImageByMoreIcon(tenderTrueNameIcon, trueSource, output, trueImageName, imageType, degree, index_x, index_y);

        cardnoIcon = fileParent + tenderCardNoImage;
        //脱敏受让人/出借人/出借人 证件号码——第一页
        if(FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == Integer.valueOf(pdfType)){
            index_x = 332;
            index_y = 595;
            if(isTenderConmpany){
                index_x = 305;
                index_y = 595;
            }
        }else if(Integer.valueOf(pdfType) == FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT ||
                Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET
                || Integer.valueOf(pdfType) == FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET){
            index_x = 340;
            index_y = 680;
            if(isTenderConmpany){
                index_x = 300;
                index_y = 680;
            }
        }
        ImageUtil.markImageByMoreIcon(cardnoIcon, trueSource, output, trueImageName, imageType, degree, index_x, index_y);


        //获取拼接图片列表
        jointPathList.add(imageSavePath + "/" + trueImageName + ".png");
        _log.info("1---------------------------------拼接图片："+imageSavePath + "/" + trueImageName + ".png");
        for (int i = 0; i < pages-2; i++) {
            jointPathList.add(imageSavePath + "/" + fileName + (i+1) + ".png");
            _log.info("2---------------------------------拼接图片："+imageSavePath + "/" + fileName + (i+1) + ".png");
        }
        jointPathList.add(imageSavePath + "/" + signimageName + ".png");
        _log.info("3---------------------------------拼接图片："+imageSavePath + "/" + signimageName + ".png");

        FileUtil.deltree(imageFilePath);
        FileUtil.deltree(trueImageFilePath);
    }

    /**
     *
     * @param jointPathList
     * @return
     */
    private boolean jointPDFimage(List jointPathList,String imageSavePath) throws Exception {

        String[] files = new String[jointPathList.size()];
        for (int i = 0; i < jointPathList.size(); i++) {
            files[i] = (String) jointPathList.get(i);
        }
        FileUtil.mergeImage(files,2,imageSavePath);

        return true;
    }

    /**
     * 发送邮件(计划订单状态由出借成功变为锁定中，发送此邮件提醒用户出借成功)
     *
     * @param hjhAccede
     */
    private void sendPlanMail(HjhAccede hjhAccede) {
        System.out.println("计划订单状态由出借成功变为锁定中，发送此邮件提醒用户出借--------------------------开始");
        try {
            Map<String, Object> contents = new HashMap<String, Object>();
            //1基本信息
            Map<String ,Object> params=new HashMap<String ,Object>();
            params.put("accedeOrderId", hjhAccede.getAccedeOrderId());
            int userId = hjhAccede.getUserId();
            params.put("userId", userId);
            UsersInfo userInfo = this.fddGenerateContractService.getUsersInfoByUserId(Integer.valueOf(hjhAccede.getUserId()));
            contents.put("userInfo", userInfo);
            contents.put("username", hjhAccede.getUserName());
            UserHjhInvistDetailCustomize userHjhInvistDetailCustomize = fddGenerateContractService.selectUserHjhInvistDetail(params);
            contents.put("userHjhInvistDetail", userHjhInvistDetailCustomize);
            Map<String, String> msg = new HashMap<String, String>();
            msg.put(VAL_USERID, String.valueOf(userId));
            // 向每个出借人发送邮件
            if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))) {
                Users users = fddGenerateContractService.getUsersByUserId(userId);
                if (users == null || Validator.isNull(users.getEmail()) || users.getIsSmtp()==1) {
                    System.out.println("=============cwyang eamil is users == null===========");
                    return;
                }
                String email = users.getEmail();
                if (org.apache.commons.lang3.StringUtils.isBlank(email) || users.getIsSmtp()==1) {
                    System.out.println("=============cwyang eamil users.getIsSmtp()==1===========");
                    return;
                }
                System.out.println("=============cwyang eamil is " + email);
                msg.put(VAL_NAME, users.getUsername());
                UsersInfo usersInfo = this.fddGenerateContractService.getUsersInfoByUserId(Integer.valueOf(userId));
                if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
                    if (usersInfo.getSex() % 2 == 0) {
                        msg.put(VAL_SEX, "女士");
                    } else {
                        msg.put(VAL_SEX, "先生");
                    }
                }
                String fileName = hjhAccede.getAccedeOrderId()+".pdf";
                //String filePath = CustomConstants.HYJF_MAKEPDF_TEMPPATH + "/fdd/";
                //String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "/" + "BorrowLoans_" + GetDate.getMillis() + StringPool.FORWARD_SLASH;
                String filePath = "/pdf_tem/pdf/" + hjhAccede.getPlanNid();
                TenderAgreement tenderAgreement = new TenderAgreement();
                List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(hjhAccede.getAccedeOrderId());//智投服务协议
                /***************************下载法大大协议******************************************/
                //下载法大大协议--智投服务协议
                if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                    tenderAgreement = tenderAgreementsNid.get(0);
                    _log.info("sendMail", "***************************下载法大大协议--出借orderId:"+hjhAccede.getAccedeOrderId());
                    _log.info("sendMail", "***************************下载法大大协议--出借pdfUrl:"+tenderAgreement.getImgUrl());
                    if(tenderAgreement!=null){
                        String pdfUrl = tenderAgreement.getDownloadUrl();
                        if(StringUtils.isNotBlank(pdfUrl)){
                            _log.info("sendMail", "***************************下载法大大协议--pdfUrl:"+pdfUrl);
                            //FileUtil.getRemoteFile(pdfUrl, filePath + fileName);
                            FileUtil.downLoadFromUrl(pdfUrl,fileName,filePath);
                        }
                    }
                }
                String[] emails = { email };
                //先用EMAILPARAM_TPL_LOANS测试，后期改成EMAITPL_EMAIL_LOCK_REPAY
                _log.info("sendMail***************************下载法大大协议--投资filePath:"+filePath + fileName);
                // mod by nxl 汇计划智投服务协议->智投服务协议
                /*MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "汇计划投资服务协议", null, new String[] { filePath + "/" + fileName }, emails, CustomConstants.EMAITPL_EMAIL_LOCK_REPAY,
                        MessageDefine.MAILSENDFORMAILINGADDRESS);*/
                MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "智投服务协议", null, new String[] { filePath + "/" + fileName }, emails, CustomConstants.EMAITPL_EMAIL_LOCK_REPAY,
                        MessageDefine.MAILSENDFORMAILINGADDRESS);
                int f =  mailMessageProcesser.gather(message);

                System.out.println("计划订单状态由出借成功变为锁定中，发送此邮件提醒用户出借--------------------------结束："+f);
            }
        } catch (Exception e) {
            System.out.println("计划订单状态由出借成功变为锁定中，发送此邮件提醒用户出借成功-----发送邮件失败");
        }
    }

    /**
     *
     * @param imagePathList
     * 转换图片列表
     * @param pdfSavePath
     * PDF存储地址
     * @return
     */
    private boolean replaceImageToPdf(List imagePathList,String pdfSavePath){

        try{
            FileUtil.imageTOpdf(imagePathList,pdfSavePath);
        }catch (Exception e){
            _log.info("-----------------脱敏图片转换成pdf失败--------");
            return false;
        }

        return true;
    }

    /**
     * 上传脱敏文件
     * @param upParentDir
     * @param type
     * 是否删除上传目录 0：否 1：是
     * @return
     */
    private boolean uplodTmImage(String upParentDir, String saveDir, int type) {

        String ftpIP = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_IP);
        String port = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PORT);
        String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);
        if(type == 0){//上传pdf
            basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_PDF);
        }
        String password = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PASSWORD);
        String username = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_USERNAME);
        try {
            _log.info("----------待上传目录：" + upParentDir);
            File paraentDir = new File(upParentDir);
            String upParaFile = paraentDir.getParent();
            if(paraentDir.isDirectory()){

                _log.info("----------待删除目录：" + upParaFile);
                File[] files = paraentDir.listFiles();
                for (File file : files) {
                    String fileName = file.getName();
                    _log.info("--------循环目录，开始上传文件：" + fileName);
                    FileInputStream in = new FileInputStream(file);
                    boolean flag = FavFTPUtil.uploadFile(ftpIP, Integer.valueOf(port), username, password,
                            basePathImage, saveDir, fileName, in);
                    if (!flag){
                        throw new RuntimeException("上传失败!fileName:" + fileName);
                    }
                }
            }else{
                String fileName = paraentDir.getName();
                _log.info("--------开始上传文件：" + fileName);
                FileInputStream in = new FileInputStream(paraentDir);
                boolean flag = FavFTPUtil.uploadFile(ftpIP, Integer.valueOf(port), username, password,
                        basePathImage, saveDir, fileName, in);
                if (!flag){
                    throw new RuntimeException("上传失败!fileName:" + fileName);
                }
            }
            if(type == 1){
                //删除原目录
                FileUtil.deltree(upParaFile);
            }

        }catch (Exception e){
            e.printStackTrace();
            _log.info(e.getMessage());
            return false;
        }
        return  true;
    }



    /**
     * 发送邮件(出借成功)居间服务协议
     *
     * @param borrowRecover
     */
    private void sendMail(BorrowRecover borrowRecover) {
        int userId = borrowRecover.getUserId();
        String orderId = borrowRecover.getNid();
        Map<String, String> msg = new HashMap<String, String>();
        msg.put(VAL_USERID, String.valueOf(userId));
        try {
            // 向每个出借人发送邮件
            if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))) {
                Users users = this.realTimeBorrowLoanServiceImpl.getUsersByUserId(userId);
                if (users == null || Validator.isNull(users.getEmail())) {
                    return;
                }
                String email = users.getEmail();
                if (org.apache.commons.lang3.StringUtils.isBlank(email) || users.getIsSmtp()==1) {
                    return;
                }
                System.out.println("开始发送邮件。出借订单号:" + orderId);
                msg.put(VAL_NAME, users.getUsername());
                UsersInfo usersInfo = this.realTimeBorrowLoanServiceImpl.getUsersInfoByUserId(Integer.valueOf(userId));
                if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
                    if (usersInfo.getSex() % 2 == 0) {
                        msg.put(VAL_SEX, "女士");
                    } else {
                        msg.put(VAL_SEX, "先生");
                    }
                }


                String fileName = "";
                //String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_" + GetDate.getMillis() + StringPool.FORWARD_SLASH;
                String  filePath = "/pdf_tem/pdf/" + orderId;
                TenderAgreement tenderAgreement = new TenderAgreement();
                List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(orderId);//智投服务协议
                /***************************下载法大大协议******************************************/
                //下载法大大协议--居间
                if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                    tenderAgreement = tenderAgreementsNid.get(0);
                    _log.info("sendMail***************************下载法大大协议--出借orderId:"+orderId);
                    _log.info("sendMail***************************下载法大大协议--出借pdfUrl:"+tenderAgreement.getImgUrl());
                    if(tenderAgreement!=null){
                        File file= createFaddPDFImgFile(tenderAgreement,filePath);
                        fileName =  file.getName();
                    }
                }
                String[] emails = { email };
                _log.info("sendMail***************************下载法大大协议--汇盈金服互联网金融服务平台居间服务协议filePath:"+filePath + fileName);
                MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "汇盈金服互联网金融服务平台居间服务协议", null, new String[] { filePath +"/" + fileName }, emails, CustomConstants.EMAILPARAM_TPL_LOANS,
                        MessageDefine.MAILSENDFORMAILINGADDRESS);
                mailMessageProcesser.gather(message);
                // 更新BorrowRecover邮件发送状态
                borrowRecover.setSendmail(1);
                this.fddGenerateContractService.updateBorrowRecover(borrowRecover);
                System.out.println("结束发送邮件。出借订单号:" + orderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 下载法大大协议
     *
     * @param tenderAgreement
     * @param savePath
     * 返回 0:下载成功；1:下载失败；2:没有生成法大大合同记录
     */
    private File createFaddPDFImgFile(TenderAgreement tenderAgreement,String savePath) {
        SFTPParameter para = new SFTPParameter() ;
        String ftpIP = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_IP);
        String port = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PORT);
        String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);
        String basePathPdf = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_PDF);
        String password = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PASSWORD);
        String username = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_USERNAME);
        para.hostName = ftpIP;//ftp服务器地址
        para.userName = username;//ftp服务器用户名
        para.passWord = password;//ftp服务器密码
        para.port = Integer.valueOf(port);//ftp服务器端口
        para.downloadPath =basePathImage;//ftp服务器文件目录
        para.savePath = savePath;
        para.fileName=tenderAgreement.getTenderNid();
        String imgUrl = tenderAgreement.getImgUrl();
        String pdfUrl = tenderAgreement.getPdfUrl();
        if(StringUtils.isNotBlank(pdfUrl)){
            imgUrl = pdfUrl;
        }else if(StringUtils.isNotBlank(imgUrl)){
            imgUrl = tenderAgreement.getImgUrl();
        }else{
            return null;
        }
        String imagepath = imgUrl.substring(0,imgUrl.lastIndexOf("/"));
        String imagename = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
        para.downloadPath =basePathImage+ "/" +imagepath;//ftp服务器文件目录
        System.out.println("downloadPath___下载图片_______________________:"+para.downloadPath);
        para.sftpKeyFile = imagename;
        if(StringUtils.isNotBlank(pdfUrl)){
            para.downloadPath =  basePathPdf+ "/" +imagepath;//ftp服务器文件目录
            System.out.println("downloadPDFPath___下载PDF_______________________:"+para.downloadPath + "/" + imagename);
        }
        return  FavFTPUtil.downloadDirectory(para);
    }
}
