package vc.thinker.email;

import javax.mail.*;

import lombok.Data;
/**
 * @author YanZhuoMin
 *
 * @date 2019年7月10日
 */
@Data
public class MyAuthenticator extends Authenticator{
	String userName=null;  
    String password=null;  
       
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
