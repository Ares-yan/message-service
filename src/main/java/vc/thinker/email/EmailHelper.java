package vc.thinker.email;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月10日
 */
public class EmailHelper {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("mail");
	private static final String sendFrom = bundle.getString("email.from");
	private static final String username = bundle.getString("username");
	private static final String password = bundle.getString("password");
	private static final String host = bundle.getString("email.host");

	public static void sendEmail(String someone, String subject, String content) {
		Properties props = new Properties();
		props.setProperty("mail.host", host);
		props.setProperty("mail.smtp.auth", "true");

		Authenticator authenticator = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getDefaultInstance(props, authenticator);
		session.setDebug(true);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(sendFrom));
			message.setRecipients(RecipientType.TO, InternetAddress.parse(someone));
			// message.setRecipients(RecipientType.TO,InternetAddress.parse("测试的接收的邮件多个以逗号隔开"));
			try {
				message.setSubject(subject);
				message.setContent(content, "text/html;charset=UTF-8");
				Transport.send(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
