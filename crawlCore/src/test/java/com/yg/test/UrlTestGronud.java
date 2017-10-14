package com.yg.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yg.webshow.crawl.seeds.sample.clien.ClienCommentData;
import com.yg.webshow.crawl.seeds.sample.clien.ClienCommentDataArray;

public class UrlTestGronud {
	
	public static void main(String ... v) {
		try {
			String conv = URLDecoder.decode("https://www.clien.net/service/api/board/park/11306455/comment?param=%7B%22order%22%3A%22date%22%2C%22po%22%3A0%2C%22ps%22%3A100%7D", "UTF-8");
			System.out.println("Converted -> " + conv);
			
			getJsonData() ;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getJsonData() throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		String targetUrl = "https://www.clien.net/service/api/board/park/11306455/comment?param=";
		String dataParam = URLEncoder.encode("{\"order\":\"date\",\"po\":0,\"ps\":100}", "UTF-8");
		targetUrl = targetUrl + dataParam ;
		
		System.out.println("URL >" + targetUrl);
		
		HttpGet request = new HttpGet(targetUrl);
		
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		
		if(statusCode == 200) {
			HttpEntity resEntity = response.getEntity();
			String strResponse = EntityUtils.toString(resEntity, "UTF-8");
			System.out.println("Response :" + strResponse);
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			ClienCommentData[] lstComment = mapper.readValue(strResponse, ClienCommentData[].class);
						
			int i = 0;
			for(ClienCommentData ccd : lstComment) {
				System.out.println(i++ + "\t" + ccd.getComment());
			}
			
		} else {
			System.out.println("Invalid Code from CERT :" + statusCode);
		}
		
	}
	
}
