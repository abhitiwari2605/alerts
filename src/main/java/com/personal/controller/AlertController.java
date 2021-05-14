package com.personal.controller;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("v1")
@RestController
public class AlertController {

	RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/getData")
	public String getData() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		String res = restTemplate.exchange(
				"https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=143001&date=09-05-2021",
				HttpMethod.GET, entity, String.class).getBody();
		System.out.println(res);
		return res;

	}
	
	@GetMapping("/test")
	public String test() {
		return "test";
	}
}
