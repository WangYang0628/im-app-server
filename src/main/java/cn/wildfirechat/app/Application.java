package cn.wildfirechat.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "cn.wildfirechat.app.mapper")
@EnableAutoConfiguration()
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
