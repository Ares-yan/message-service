package vc.thinker.sms;

import java.io.UnsupportedEncodingException;

import javax.annotation.PreDestroy;
import javax.mail.MessagingException;

import org.junit.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


import lombok.Data;
import vc.thinker.email.MailUtil;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月9日
 */
@Data
public class AliyunSmsCode {
//	private String accessId;
//
//	private String accessKey;
//
//	private String mnsEndpoint;
//
//	private String topic;
//
//	private String signName;
//
//	private String regTemplateCode;
//
//	private String loginTemplateCode;
//
//	private String forgetTemplateCode;
//
//	private String bindTemplateCode;
//
//	private String upgradeTemplateCode;
//
//	private CloudTopic tp;
//	
//	private MNSClient client;

//	@Bean("mnsClient")
//	MNSClient createClient() {
//		CloudAccount account = new CloudAccount(this.accessId, this.accessKey, this.mnsEndpoint);
//		MNSClient client = account.getMNSClient();
//		this.client = client;
//		return client;
//	}
//	
//    @Bean
//    CloudTopic cloudTopic(MNSClient mnsClient) {
//        CloudTopic tp = mnsClient.getTopicRef(this.topic);
//        this.tp = tp;
//        return tp;
//    }
//    
//    @PreDestroy
//    public void destroy() {
//        client.close();
//    }
	@Test
	public void sendEmail() {
		try {
			MailUtil.sendMail("897526929@qq.com", "12345");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
