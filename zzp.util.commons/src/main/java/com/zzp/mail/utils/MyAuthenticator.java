package com.zzp.mail.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 密码校验器
 * 
 * @author karyzeng
 * @since 2019.01.29
 *
 */
public class MyAuthenticator extends Authenticator{  
    String userName = null;  
    String password = null;  
       
    public MyAuthenticator(){  
    	
    }  
    
    public MyAuthenticator(String username, String password) {   
        this.userName = username;   
        this.password = password;   
    }   
    
    protected PasswordAuthentication getPasswordAuthentication(){  
        return new PasswordAuthentication(userName, password);  
    }  
}  
