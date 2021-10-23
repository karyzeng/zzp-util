package com.zzp.http.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.zzp.convert.utils.ConvertUtils;
import com.zzp.sort.utils.MapSort;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * http请求工具类
 * 
 * @author karyzeng
 * @since 2018.08.28
 *
 */
public class HttpRequestUtils {

	public static void main(String[] args) {
//		System.out.println(HttpRequest.sendGet("http://api.map.baidu.com/geocoder/v2/", "address=  广东省  广州市天河区华景  	新城东区华景二期粤生街98号405 &output=json&ak=KaqwkA7ozlPx4KgYVXbTfbGOGUXiIp3R"));
		
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("method", "GetVehicleNoList");
//		params.put("appkey", "30914b89-262a-4832-b8b9-fe33770b4b4d");
//		params.put("timestamp", "2018-08-29 16:35:36");
//		params.put("format", "json");
//		System.out.println(HttpRequest.sendGet("http://api.e6gps.com/public/v3/StatisticsReport/Call", "69411cff-d7be-4458-952f-2fca0267b408", params));

//        Map<String, String> params = new HashMap<String, String>();
//        params.put("accessId", "30fcb777-3a23-4258-8bbd-d9c3fca6827c");
//        params.put("accessKey", "13017619-c132-48b8-b746-2b70d17b64f5");
//        params.put("aliyunId", "挚联企业:hoolinks_BI");
//        params.put("validityTime", "60");
//
//        Map<String, String> requestPropertys = new HashMap<String, String>();
//        requestPropertys.put("Content-Type", "application/json");
//
//        String result = HttpRequest.sendGet("https://das.base.shuju.aliyun.com/api/ac3rdservice/token.json", params);
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = JSON.parseObject(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (jsonObject != null) {
//            System.out.println(jsonObject.getBoolean("success"));
//        } else {
//            System.out.println("获取token失败");
//        }


//        Map<String, String> requestPropertys = new HashMap<String, String>();
//        requestPropertys.put("Content-Type", "application/json");
//
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("userUid", "U1591150157277");
//        params.put("modulesName", "scm/importPlannedOrder/checkList");
//
//        String result = HttpRequest.sendPost("https://test-bff.hoolinks.com/generateHash/save", JSON.toJSONString(params), requestPropertys);
//        System.out.println(result);

        try {
            String filePath = downloadFile("https://qa-fc.hoolinks.com/qa/scm/202101/06/进货计划商品信息导入-20210106112934.xlsx", "E:\\scptemp");
            System.out.println(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
	
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url 发送请求的URL
     * @param appsecret 私钥
     * @param params 签名参数自动生成，不需要传入
     * 
     * @return String 响应结果
     */
    public static String sendGetByMD5(String url, String appsecret, Map<String, String> params){
    	String sign = getSignByMD5(appsecret, params);
    	params.put("sign", sign);
        String param = convertParamList2ParamStr(params);
    	return sendGet(url, param);
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url 发送请求的URL
     * @param appsecret 私钥
     * @param params 签名参数自动生成，不需要传入
     * 
     * @return String 响应结果
     */
    public static String sendGetBySHA1(String url, String appsecret, Map<String, String> params){
    	String appId = params.get("appId");
    	String time = params.get("time");
    	String signature = getSignBySHA1(appId, appsecret, time);
    	params.put("signature", signature);
        String param = convertParamList2ParamStr(params);
    	return sendGet(url, param);
    }

    /**
     * get请求
     * @param url 请求url
     * @param params 参数map
     * @return String
     */
    public static String sendGet(String url, Map<String, String> params){
        String param = convertParamList2ParamStr(params);
        return sendGet(url, param, null);
    }

    /**
     * get请求
     * @param url 请求url
     * @param params 参数map
     * @param requestPropertys 请求属性
     * @return String
     */
    public static String sendGet(String url, Map<String, String> params, Map<String, String> requestPropertys){
        String param = convertParamList2ParamStr(params);
        return sendGet(url, param, requestPropertys);
    }

    /**
     * get请求
     * @param url 请求url
     * @param param 请求参数，格式如name1=value1&name2=value2
     * @return String
     */
    public static String sendGet(String url, String param){
        return sendGet(url, param, null);
    }

    /**
     * get请求
     * @param url 请求url
     * @param param 请求参数，格式如name1=value1&name2=value2
     * @return String
     */
    public static String sendGet(String url, String param, Map<String, String> requestPropertys) {
        //去除参数中可能含有的空格
        param = ConvertUtils.trimSpace(param);
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            System.out.println("请求完整的url：" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (requestPropertys != null && requestPropertys.size() > 0) {
                for (Map.Entry<String, String> entry : requestPropertys.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            //设置连接超时时间，必须设置，否则会让程序卡在请求连接
            connection.setConnectTimeout(15000);
            //设置读超时时间，必须设置，否则会让程序卡在读内容中
            connection.setReadTimeout(15000);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * post请求
     * @param url 请求url
     * @param param 请求参数，格式如name1=value1&name2=value2
     * @return String
     */
    public static String sendPost(String url, String param) {
        return sendPost(url, param, null);
    }

    /**
     * post请求
     * @param url 请求url
     * @param params 参数map
     * @return String
     */
    public static String sendPost(String url, Map<String, String> params) {
        String param = convertParamList2ParamStr(params);
        return sendPost(url, param, null);
    }

    /**
     * post请求
     * @param url 请求url
     * @param params 参数map
     * @return String
     */
    public static String sendPost(String url, Map<String, String> params, Map<String, String> requestPropertys){
        String param = convertParamList2ParamStr(params);
        return sendPost(url, param, requestPropertys);
    }

    /**
     * post请求
     * @param url 请求url
     * @param param 请求参数，格式如name1=value1&name2=value2
     * @param requestPropertys 请求属性
     * @return String
     */
    public static String sendPost(String url, String param, Map<String, String> requestPropertys) {
        //去除参数中可能含有的空格
        param = ConvertUtils.trimSpace(param);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (requestPropertys != null && requestPropertys.size() > 0) {
                for (Map.Entry<String, String> entry : requestPropertys.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            //设置连接超时时间，必须设置，否则会让程序卡在请求连接
            conn.setConnectTimeout(15000);
            //设置读超时时间，必须设置，否则会让程序卡在读内容中
            conn.setReadTimeout(15000);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将参数Map转换成参数字符串，格式如name1=value1&name2=value2
     * @param params 参数map
     * @return String
     */
    private static String convertParamList2ParamStr(Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return null;
        }
        StringBuffer sBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = "";
            try {
                value = URLEncoder.encode(entry.getValue(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sBuffer.append(entry.getKey() + "=" + value + "&");
        }
        sBuffer.deleteCharAt(sBuffer.lastIndexOf("&"));
        return sBuffer.toString();
    }

    /**
     * 获得MD5加密签名sign
     * @param appsecret 私钥
     * @param params 签名参数sign自动生成，不需要传入
     * @return String 签名sign
     */
    public static String getSignByMD5(String appsecret, Map<String, String> params) {
        Map<String, String> sortMap = MapSort.sortMapByKey(params);
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(appsecret);
        for (Map.Entry<String, String> entry : sortMap.entrySet()) {
            sBuffer.append(entry.getKey() + entry.getValue());
        }
        sBuffer.append(appsecret);
        return MD5Utils.string2MD5(sBuffer.toString()).toUpperCase();
    }

    /**
     * 获得SHA1加密签名sign
     * @param appId
     * @param appSecret 私钥
     * @param time 当前时间字符串，格式“2018-10-13 11:02:10”
     * @return
     */
    public static String getSignBySHA1(String appId, String appSecret, String time) {
        String sign = SHA1SignUtils.createSignature(appId, appSecret, time);
        return sign;
    }

    /**
     * 根据url下载文件
     * @param fileUrl 文件url
     * @param filePath 存储文件的位置
     * @return
     * @throws Exception
     */
    public static String downloadFile(String fileUrl, String filePath) throws Exception{
        if (StringUtils.isBlank(fileUrl)) {
            throw new NullPointerException("fileUrl is not blank");
        }
        try {
            filePath = filePath + File.separator + getDownloadFileName(fileUrl);
            String encodeUrl = encodeDownloadFileUrl(fileUrl);
            URL url = new URL(encodeUrl);
            File file = new File(filePath);

            FileUtils.copyURLToFile(url, file, 5000, 5000);
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getDownloadFileName(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        return fileName;
    }

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
