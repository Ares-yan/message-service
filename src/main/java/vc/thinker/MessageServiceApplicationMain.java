package vc.thinker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author YanZhuoMin
 *
 * @date 2019年7月2日
 */
@SpringBootApplication(scanBasePackages = "vc.thinker.**")
@MapperScan("vc.thinker.**.mapper")
public class MessageServiceApplicationMain {

	public static void main(String[] args) {
		SpringApplication.run(MessageServiceApplicationMain.class, args);
	}
}
