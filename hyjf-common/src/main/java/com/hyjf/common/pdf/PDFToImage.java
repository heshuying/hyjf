package com.hyjf.common.pdf;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;
public class PDFToImage {
    public final  static String  IMG_TYPE_JPG = "jpg";
    public final  static String  IMG_TYPE_PNG = "png";

    public static void main(String[] args) {
       //pdf2img("E://jujian20181018.pdf", "E://Applications/Demo_Contract_Travel_img_jujian",IMG_TYPE_PNG);
       pdf2img("E://zhaizhuan20181018.pdf", "E://Applications/Demo_Contract_Travel_img",IMG_TYPE_PNG);

    }


    /**
     *
     * @param pdfPath pdf文件的路径
     * @param savePath 图片保存的地址
     * @param imgType 图片保存方式
     */
    public static void pdf2img(String pdfPath,String savePath,String imgType){
        String fileName = pdfPath.substring(pdfPath.lastIndexOf("/")+1, pdfPath.length());
        fileName = fileName.substring(0,fileName.lastIndexOf("."));
        File file = new File(pdfPath);
        PDDocument pdDocument =  null;
        File p=new File(savePath);
        if(!p.exists()){
            p.mkdir();
        }
        try {

        	pdDocument =  PDDocument.load(file);
            PDPageTree pages = pdDocument.getPages();
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            for (int i = 0; i < pages.getCount(); i++) {
                String saveFileName = savePath+"/"+fileName+i+"."+imgType;

				File dstFile = new File(saveFileName);
				BufferedImage image = renderer.renderImageWithDPI(i, 122);
				BufferedImage srcImage = resize(image, 1191, 1684);//产生缩略图
				//image.setRGB(1191, 1684, BufferedImage.TYPE_USHORT_GRAY);
				ImageIO.write(srcImage,imgType, dstFile);
                //pdfPage2Img(page,saveFileName,imgType);
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


    /**
     * pdf页转换成图片
     * @param page
     * @param saveFileName
     * @throws IOException
     */
//    public static  void pdfPage2Img(PDPage page,String saveFileName,String imgType) throws IOException {
//        BufferedImage img_temp  = page.convertToImage();
//        Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix(imgType);
//        ImageWriter writer = (ImageWriter) it.next();
//        ImageOutputStream imageout = ImageIO.createImageOutputStream(new FileOutputStream(saveFileName));
//        writer.setOutput(imageout);
//        writer.write(new IIOImage(img_temp, null, null));
//    }



    public static PDDocument pdfInfo(String filePath) throws IOException{
    	 File file = new File(filePath);
    	 PDDocument pdDocument =  PDDocument.load(file);
        return pdDocument;
    }

}
