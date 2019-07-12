package vc.thinker.sms;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;
import vc.thinker.email.MailSenderInfo;
import vc.thinker.email.SimpleMailSender;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月10日
 */
@Component
@Configuration
@PropertySource("classpath:mailConfig.properties")
@Service
public class EmailService {
	
	@Value("${mailUsername}")
	private String mailUsername;
	@Value("${mailHost}")
	private String mailHost;
	@Value("${mailPassword}")
	private String mailPassword;
	@Value("${mailFrom}")
	private String mailFrom;
	@Value("${personal}")
	private String personal;
	@Value("${mailPort}")
	private String mailPort;
	@Value("${verifyCodeTemplate}")
	private String verifyCodeTemplate;
	
	public Boolean sendEmail(String email,String content) {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(mailHost);
		mailInfo.setMailServerPort(mailPort);
		mailInfo.setValidate(true);
		mailInfo.setUserName(mailUsername);
		// 您的邮箱授权码
		mailInfo.setPassword(mailPassword);
		mailInfo.setFromAddress(mailFrom);
		mailInfo.setToAddress(email);
		mailInfo.setSubject("验证码");
		mailInfo.setContent(verifyCodeTemplate + content);
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		boolean sendTextMail = false;
		try {
			sendTextMail = sms.sendTextMail(mailInfo);
			return sendTextMail;
			// 发送文体格式
			//sms.sendHtmlMail(mailInfo);// 发送html格式
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendTextMail;
	}
}
