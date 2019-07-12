package vc.thinker.sms;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月9日
 */
@Log4j
@Service
public class SmsCodeService {
	
	@Value("${user.aliyun.smsCode.domain}")
    String domain;
	@Value("${user.aliyun.smsCode.accessKeySecret}")
	String accessKeySecret;
	@Value("${user.aliyun.smsCode.accessKeyId}")
	String accessKeyId;
	@Value("${user.aliyun.smsCode.phoneVerify}")
	String phoneVerify;
	@Value("${user.aliyun.smsCode.signName}")
	String signName;
	
	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		json.put("code", 123);
		System.out.println(json.toString());
	}
	
	@SuppressWarnings("deprecation")
	public void sendSms(String phoneNumber,String code) {
		DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessKeySecret);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain(domain);
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("PhoneNumbers", phoneNumber);
		request.putQueryParameter("SignName", signName);
		request.putQueryParameter("TemplateCode", phoneVerify);
		JSONObject json = new JSONObject();
		json.put("code", code);
		request.putQueryParameter("TemplateParam", json.toString());
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
}
