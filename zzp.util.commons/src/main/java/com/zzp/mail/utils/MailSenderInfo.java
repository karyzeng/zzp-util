package com.zzp.mail.utils;

import java.util.Properties;

/**
 * 邮件发送信息
 * 
 * @author karyzeng
 * @since 2019.01.29
 *
 */
public class MailSenderInfo {

	/**
	 * 邮件服务器地址
	 */
    private String mailServerHost; 
    
    /**
     * 邮件服务器端口
     */
    private String mailServerPort = "25";   
      
    /**
     * 邮件发送者的地址
     */
    private String fromAddress;   
      
    /**
     * 邮件接收者的地址   
     */
    private String toAddress;   
      
    /**
     * 登陆邮件发送服务器的用户名
     */
    private String userName;  
    
    /**
     * 登陆邮件发送服务器的密码
     */
    private String password;   
      
    /**
     * 是否需要身份验证  
     */
    private boolean validate = false;   

    /**
     * 邮件主题
     */
    private String subject;   
      
    /**
     * 邮件的文本内容   
     */
    private String content;   
      
    /**
     * 邮件附件的文件名 
     */
    private String[] attachFileNames;  
    
    /**
     * 获得邮件会话属性     
     */
    public Properties getProperties(){   
      Properties p = new Properties();   
      p.put("mail.smtp.host", this.mailServerHost);   
      p.put("mail.smtp.port", this.mailServerPort);   
      p.put("mail.smtp.auth", validate ? "true" : "false");
      //添加信任的服务器地址，多个地址之间用空格分开
	  p.put("mail.smtp.ssl.trust", "smtp.toyotsu-ea.com");
	  p.put("mail.smtp.host", "smtp.toyotsu-ea.com");
	  p.put("mail.smtp.port", "25");
      return p;   
    }   
    public String getMailServerHost() {   
      return mailServerHost;   
    }   
    public void setMailServerHost(String mailServerHost) {   
      this.mailServerHost = mailServerHost;   
    }  
    public String getMailServerPort() {   
      return mailServerPort;   
    }  
    public void setMailServerPort(String mailServerPort) {   
      this.mailServerPort = mailServerPort;   
    }  
    public boolean isValidate() {   
      return validate;   
    }  
    public void setValidate(boolean validate) {   
      this.validate = validate;   
    }  
    public String[] getAttachFileNames() {   
      return attachFileNames;   
    }  
    public void setAttachFileNames(String[] fileNames) {   
      this.attachFileNames = fileNames;   
    }  
    public String getFromAddress() {   
      return fromAddress;   
    }   
    public void setFromAddress(String fromAddress) {   
      this.fromAddress = fromAddress;   
    }  
    public String getPassword() {   
      return password;   
    }  
    public void setPassword(String password) {   
      this.password = password;   
    }  
    public String getToAddress() {   
      return toAddress;   
    }   
    public void setToAddress(String toAddress) {   
      this.toAddress = toAddress;   
    }   
    public String getUserName() {   
      return userName;   
    }  
    public void setUserName(String userName) {   
      this.userName = userName;   
    }  
    public String getSubject() {   
      return subject;   
    }  
    public void setSubject(String subject) {   
      this.subject = subject;   
    }  
    public String getContent() {   
      return content;   
    }  
    public void setContent(String textContent) {   
      this.content = textContent;   
    }   
}
