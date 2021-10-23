package com.zzp.validate.code.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Description 数字验证码工具类
 * @Author Garyzeng
 * @since 2021.10.17
 **/
public class DigitValidateCodeUtils {

    /**
     * 给定范围获得随机颜色
     *
     * @param fc 颜色范围开始
     * @param bc 颜色范围结束
     * @return {@link Color}
     */
    public static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 得到随机验证码
     *
     * @param codeLength 验证码长度
     * @return {@link String}
     */
    public static String getRandomValidateCode(int codeLength) {
        // 生成随机类
        Random random = new Random();
        // 取随机产生的认证码(4位数字)
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
        }
        return sRand.toString();
    }

    /**
     * 获取验证码图片
     *
     * @param width  宽度
     * @param height 高度
     * @param codeLength 验证码长度
     * @return {@link BufferedImage}
     */
    public static DigitValidateCodeResult getValidateCodeImage(int width, int height, int codeLength) {
        // 在内存中创建图象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics g = image.getGraphics();

        // 生成随机类
        Random random = new Random();

        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);

        // 设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        // 画边框
        // g.setColor(new Color());
        // g.drawRect(0,0,width-1,height-1);

        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 取随机产生的认证码
        // 4位数字
        String randomCode = getRandomValidateCode(codeLength);
        String[] codeArr = randomCode.split("");
        for (int i = 0; i < codeLength; i++) {
            String rand = codeArr[i];
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(rand, 13 * i + 6, 16);
        }
        // 图象生效
        g.dispose();
        return new DigitValidateCodeResult(randomCode, image);
    }

}
