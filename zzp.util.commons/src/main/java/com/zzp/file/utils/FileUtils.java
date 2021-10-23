package com.zzp.file.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 文件工具类
 * @Author Garyzeng
 * @since 2019.10.15
 **/
public class FileUtils {

    /**
     * 获取文件夹下的所有文件
     * @param folder
     * @return
     */
    public static List<String> getFilePaths(File folder) {
        List<String> files = new ArrayList<String>();
        File[] subs = folder.listFiles();
        if (subs != null && subs.length > 0) {
            for (int i = 0; i < subs.length; i++) {
                File sub = subs[i];
                if (sub.isFile()) {
                    files.add(sub.getPath());
                }
            }
            return files;
        }
        return null;
    }

    /**
     * 获取文件夹下的所有文件
     * @param folder
     * @return
     */
    public static List<File> getFiles(File folder) {
        List<File> files = new ArrayList<File>();
        File[] subs = folder.listFiles();
        if (subs != null && subs.length > 0) {
            for (int i = 0; i < subs.length; i++) {
                File sub = subs[i];
                if (sub.isFile()) {
                    files.add(sub);
                }
            }
            return files;
        }
        return null;
    }

    /**
     * 获取文件Url的输入流
     * @param fileUrl
     * @return InputStream
     */
    private static InputStream getInputStream(String fileUrl) {
        try {
            String encodeUrl = encodeDownloadFileUrl(fileUrl);
            URL url = new URL(encodeUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            // 设置连接超时间为5秒
            conn.setConnectTimeout(5000);
            // 设置读超时时间为5秒
            conn.setReadTimeout(5000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 得到输入流
            InputStream inputStream = conn.getInputStream();
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对下载文件的url进行编码，主要是处理下载的文件包含有中文字符的情况
     * @param fileUrl
     * @return String
     */
    private static String encodeDownloadFileUrl(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }
        int fileNameIndex = fileUrl.lastIndexOf("/");
        String fileName = fileUrl.substring(fileNameIndex + 1);
        String urlPrefix = fileUrl.substring(0, fileNameIndex + 1);
        String encodeFileName = null;
        try {
            encodeFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(encodeFileName)) {
            return urlPrefix + encodeFileName;
        }
        return fileUrl;
    }

}
