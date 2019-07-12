package vc.thinker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import vc.thinker.sms.EmailService;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月10日
 */
@Api(tags = {"邮箱"})
@RequestMapping(value = "/email")
@RestController
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/send_email_verify")
	public ResponseEntity<Boolean> sendEmailVerify(@RequestParam String email){
		String code = obtainRandomCode();
		Boolean sendEmail = emailService.sendEmail(email, code);
		return ResponseEntity.ok(sendEmail);
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
}
