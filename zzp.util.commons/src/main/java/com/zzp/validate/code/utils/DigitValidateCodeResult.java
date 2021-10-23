package com.zzp.validate.code.utils;

import java.awt.image.BufferedImage;

/**
 * @Description 数字验证码工具类返回值
 * @Author Garyzeng
 * @since 2021.10.17
 **/
public class DigitValidateCodeResult {

    private String randomCode;

    private BufferedImage image;

    public DigitValidateCodeResult(String randomCode, BufferedImage image) {
        this.randomCode = randomCode;
        this.image = image;
    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
