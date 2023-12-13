package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@SpringBootApplication
@RestController
public class DemoApplication {
	public static int num = 0;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@RequestMapping(value = "/hello", method = GET)
	public ModelAndView getIndex(){
		Object o = new Object();
		ModelAndView mav = new ModelAndView("index.html","script.js",o);
		return mav;
	}
	@RequestMapping(value = "/", method = GET)
	public ModelAndView shit(){
		Object o = new Object();
		ModelAndView mav = new ModelAndView("index.html","script.js",o);
		return mav;
	}
	@RequestMapping(value = "/home", method = GET)
	public ModelAndView getHomePage(){
		ModelAndView mav = new ModelAndView("home.html","script.js",new Object());
		return mav;
	}
	@RequestMapping(value = "/hello", method = POST, headers = "incrementCounter=true", produces = MediaType.TEXT_PLAIN_VALUE)
	public int getCounter(){
		return ++num;
	}

	@GetMapping(value = "/js", produces = "text/javascript")
	public String getJavaScript() throws IOException {
		Resource resource = new ClassPathResource("static/script.js");
		byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
		return new String(bytes);
	}

}
