package vc.thinker.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import vc.thinker.email.MailSenderInfo;
import vc.thinker.email.MailUtil;
import vc.thinker.email.SimpleMailSender;
import vc.thinker.sms.SmsCodeService;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月2日
 */
@Api(tags = {"测试"})
@RestController
@RequestMapping("/sms")
public class CloudTestController {

	public static String MOBILE_VERFIY_CODE_KEY = "MOBILE_VERFIY_CODE_";

	public static String EMAIL_VERFIY_CODE_KEY = "EMAIL_VERFIY_CODE_";

	@Autowired
	SmsCodeService smsCodeService;
	@Autowired
	private JedisPool jedisPool;

	@RequestMapping(value = "/test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("success");
	}

	@GetMapping(value = "/phone_verfiy")
	@ApiOperation(value = "获取短信验证码", notes = "获取短信验证码")
	public ResponseEntity<Boolean> phoneVerfiy(@RequestParam String phone) {
		Jedis jedis = jedisPool.getResource();
		// 获取六位数随机码
		String code = obtainRandomCode();
		// 发送短信
		smsCodeService.sendSms(phone, code);
		// 存入redis
		jedis.set(MOBILE_VERFIY_CODE_KEY + phone, code);
		jedis.expire(MOBILE_VERFIY_CODE_KEY + phone, 60 * 5);
		return ResponseEntity.ok(true);
	}

	@GetMapping(value = "/check")
	@ApiOperation(value = "校验短信验证码", notes = "校验短信验证码")
	public ResponseEntity<Boolean> check(@RequestParam String phone, @RequestParam String code) {
		Jedis jedis = jedisPool.getResource();
		String jedisCode = jedis.get(MOBILE_VERFIY_CODE_KEY + phone);
		if (code != null && code.equals(jedisCode)) {
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.ok(false);
	}

	@GetMapping(value = "/send_email")
	@ApiOperation(value = "发送邮箱验证码", notes = "发送邮箱验证码")
	public ResponseEntity<Boolean> sendEmail(@RequestParam String email) {
		String code = obtainRandomCode();
		try {
			MailUtil.sendMail(email, code);
			Jedis jedis = jedisPool.getResource();
			// 存入redis
			jedis.set(MOBILE_VERFIY_CODE_KEY + email, code);
			jedis.expire(MOBILE_VERFIY_CODE_KEY + email, 60 * 5);

			return ResponseEntity.ok(true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ResponseEntity.ok(false);
		} catch (MessagingException e) {
			e.printStackTrace();
			return ResponseEntity.ok(false);
		}
	}

	/**
	 * 生成6位随机数验证码
	 * 
	 * @return
	 */
	public static String obtainRandomCode() {
		String vcode = "";
		for (int i = 0; i < 6; i++) {
			vcode = vcode + (int) (Math.random() * 9);
		}
		return vcode;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {

		//MailUtil.sendMail("897526929@qq.com", "12345");
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("13510761175@163.com");
		mailInfo.setPassword("yzm158110");// 您的邮箱密码
		mailInfo.setFromAddress("13510761175@163.com");
		mailInfo.setToAddress("897526929@qq.com");
		mailInfo.setSubject("测试一下");
		mailInfo.setContent("来了老弟");
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		boolean sendTextMail = sms.sendTextMail(mailInfo);// 发送文体格式
//		boolean sendTextMail = sms.sendHtmlMail(mailInfo);// 发送html格式
		System.out.println(sendTextMail);

	}
}
