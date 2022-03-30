package com.example.servingwebcontent;

import com.example.servingwebcontent.smtp.SendMail;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecuringWebApplication {

	public static void main(String[] args) throws Throwable {
		SendMail.init();
		SpringApplication.run(SecuringWebApplication.class, args);
	}

}
