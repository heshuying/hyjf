/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.common.pdf;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * 将pdf转换为图片
 * @author yinhui
 * @version PdfToHtml, v0.1 2018/6/4 14:09
 */
public class PdfToHtml extends PDFToImage {

//    public final  static String  IMG_TYPE_JPG = "jpg";
//    public final  static String  IMG_TYPE_PNG = "png";

    public static void main(String[] args) {
//        String pdfPath="F://upload/1527831873858.pdf";
//        String savePath=pdfPath.substring(0,pdfPath.lastIndexOf("/"))+"/"+pdfPath.substring(pdfPath.lastIndexOf("/")+1,pdfPath.lastIndexOf("."));
//        String imgType= IMG_TYPE_JPG;
////        String imgUrl="";
////        String fileName=pdfPath.substring(pdfPath.lastIndexOf("/")+1,pdfPath.lastIndexOf("."));
////        app.pdf2img("/Applications/work/Demo_Contract_Travel_img.pdf", "/Applications/work/Demo_Contract_Travel_img",IMG_TYPE_PNG);
//        List<String> imgs = pdftoImg(pdfPath, savePath, imgType);
////        if(!CollectionUtils.isEmpty(imgs)){
////            String img = StringUtils.strip(imgs.toString(), "[]");
////            imgUrl=pdfPath.substring(0,pdfPath.lastIndexOf("/"))+"/"+fileName+"-"+img;
////            System.out.print(imgUrl);
////        }
    }


    /**
     * 将pdf转换为图片 ，返回图片名字
     * @param pdfPath  pdf文件的路径
     * @param savePath 图片保存的地址
     * @param imgType  图片保存方式
     */
    public static List<String> pdftoImg(String pdfPath, String savePath, String imgType) {
        List<String> listImg = new ArrayList<>();
        String fileName = pdfPath.substring(pdfPath.lastIndexOf("/") + 1, pdfPath.length());
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        
        File file = new File(pdfPath);
        PDDocument pdDocument = null;
        
        
        File p=new File(savePath);
        if(!p.exists()){
            p.mkdir();
        }
        try {
 
            pdDocument =  PDDocument.load(file);
            PDPageTree pages = pdDocument.getPages();
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            for (int i = 0; i < pages.getCount(); i++) {
//                String saveFileName = savePath+"/"+fileName+i+"."+imgType;
                String saveFileName = savePath + "/"+i + "." + imgType;
                File dstFile = new File(saveFileName);
                BufferedImage image = renderer.renderImageWithDPI(i, 122);
                BufferedImage srcImage = resize(image, 1191, 1684);//产生缩略图
                ImageIO.write(srcImage,imgType, dstFile);
                listImg.add(String.valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(pdDocument != null){
                try {
                    pdDocument.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        /*--------------------------------2.0.9jar包替换（ZhaDdaojian 2018-07-30）
        /* InputStream is = null;
         File p = new File(savePath);
        if (!p.exists()) {
            p.mkdir();
        }
        try {
            is = new BufferedInputStream(new FileInputStream(pdfPath));
            PDFParser parser = new PDFParser(is);
            parser.parse();
            pdDocument = parser.getPDDocument();
            List<PDPage> pages = pdDocument.getDocumentCatalog().getAllPages();
            for (int i = 0; i < pages.size(); i++) {
                String saveFileName = savePath + "/"+i + "." + imgType;
                PDPage page = pages.get(i);
                pdfPage2Img(page, saveFileName, imgType);
                listImg.add(String.valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pdDocument != null) {
                try {
                    pdDocument.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        return listImg;
    }
    
    private static BufferedImage resize(BufferedImage source, int targetW,  int targetH) {  
        int type=source.getType();  
        BufferedImage target=null;  
        double sx=(double)targetW/source.getWidth();  
        double sy=(double)targetH/source.getHeight();  
        if(sx>sy){  
            sx=sy;  
            targetW=(int)(sx*source.getWidth());  
        }else{  
            sy=sx;  
            targetH=(int)(sy*source.getHeight());  
        }  
        if(type==BufferedImage.TYPE_CUSTOM){  
            ColorModel cm=source.getColorModel();  
                 WritableRaster raster=cm.createCompatibleWritableRaster(targetW, targetH);  
                 boolean alphaPremultiplied=cm.isAlphaPremultiplied();  
                 target=new BufferedImage(cm,raster,alphaPremultiplied,null);  
        }else{  
            target=new BufferedImage(targetW, targetH,type);  
        }  
        Graphics2D g=target.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);  
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));  
        g.dispose();  
        return target;  
    }  

}
