package com.zzp.barcode.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description 条形码工具
 * @Author Garyzeng
 * @since 2019.11.21
 **/
public class BarCodeUtils {

    private static final int QRCOLOR = 0xFF000000; // 二维码颜色 默认是黑色
    private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

    /**
     * 生成条形码
     *
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    public static void create(String contents, int width, int height, String imgPath) throws Exception{
        int codeWidth = 40;
        codeWidth = Math.max(codeWidth, width);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, codeWidth, height, null);
        MatrixToImageWriter.writeToPath(bitMatrix, "png", Paths.get(imgPath));
    }

    /**
     * 批量生成条形码
     * @param exportImgFolderPath 生成的条形码存放的文件夹
     * @param barCodeContents 条形码文件名和内容键值对
     * @param width 条形码的宽
     * @param height 条形码的高
     * @throws Exception
     */
    public static void batchCreate(String exportImgFolderPath, Map<String, String> barCodeContents, int width, int height) throws Exception{
        if (barCodeContents != null && barCodeContents.size() > 0) {
            File exportImgFolder = new File(exportImgFolderPath);
            if (!exportImgFolder.exists()) {
                exportImgFolder.mkdirs();
            }
            Iterator<Map.Entry<String, String>> entries = barCodeContents.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                String imgPath = exportImgFolderPath + File.separator + entry.getKey();
                String contents = entry.getValue();
                create(contents, width, height, imgPath);
            }
        }
    }

    /**
     * 生成条形码
     *
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     * @param words 条形码下面显示的文字
     */
    public static void create(String contents, int width, int height, String imgPath, String words) throws Exception{
        int codeWidth = 40;
        codeWidth = Math.max(codeWidth, width);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, codeWidth, height, null);
        // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
        BufferedImage image = new BufferedImage(codeWidth, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? QRCOLOR : BGWHITE);
            }
        }

        image=insertWords(image,words, width, height, height + 20);

        image.flush();
        ImageIO.write(image, "png", new File(imgPath));
    }

    /**
     * 把带logo的二维码下面加上文字
     * @param image
     * @param words
     * @return
     */
    private static BufferedImage insertWords(BufferedImage image,String words, Integer width, Integer height, Integer wordHeight){
        // 新的图片，把带logo的二维码下面加上文字
        if (StringUtils.isNotEmpty(words)) {

            //创建一个带透明色的BufferedImage对象
            BufferedImage outImage = new BufferedImage(width, wordHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D outg = outImage.createGraphics();
            setGraphics2D(outg, width, height);

            // 画二维码到新的面板
            outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            // 画文字到新的面板
            Color color=new Color(0,0,0);
            outg.setColor(color);
            // 字体、字型、字号
            outg.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            //文字长度
            int strWidth = outg.getFontMetrics().stringWidth(words);
            //总长度减去文字长度的一半  （居中显示）
            int wordStartX=(width - strWidth) / 2;
            //height + (outImage.getHeight() - height) / 2 + 12
            int wordStartY=height + 17;
            // 画文字
            outg.drawString(words, wordStartX, wordStartY);
            outg.dispose();
            outImage.flush();
            return outImage;
        }
        return null;
    }

    /**
     * 设置 Graphics2D 属性  （抗锯齿）
     * @param graphics2D
     */
    private static void setGraphics2D(Graphics2D graphics2D, Integer width, Integer height){
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        graphics2D.setStroke(s);
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, width, 100);
        graphics2D.setPaint(Color.RED);
    }


    /**
     * 条形码解码
     *
     * @param imgPath
     * @return String
     */
    public static String decode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            String imgPath = "E:/zzp/test.png";
            String contents = "692655730036095845";
            int width = 440, height = 60;
            create(contents, width, height, imgPath, contents);
            System.out.println("finished zxing EAN-13 encode.");
//        String decodeContent = decode(imgPath);
//        System.out.println("解码内容如下：" + decodeContent);
//        System.out.println("finished zxing EAN-13 decode.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
