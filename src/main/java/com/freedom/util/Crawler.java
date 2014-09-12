package com.freedom.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class Crawler {
	
	 public static void main(String[] args)throws Exception {
		 
		 	BasicCookieStore cookieStore = new BasicCookieStore();
//			CloseableHttpClient httpclient = HttpClients.custom()
//					.setDefaultCookieStore(cookieStore).build();
		 	CloseableHttpClient httpclient = HttpClients.custom().build();
	       // CloseableHttpClient httpclient = HttpClients.createDefault();
			// 创建本地的HTTP内容
			HttpContext localContext = new BasicHttpContext();
			// 绑定定制的cookie store到本地内容中
			localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
	        try {
	            HttpHost proxy = new HttpHost("10.217.128.254", 8080, "http");
	            
	            HttpPost httppost = new HttpPost("http://t.qq.com/FoxZhang-Zone");  
	            //HttpPost httppost = new HttpPost("http://zhaoren.t.qq.com/people?from=top&pgv_ref=web.wide.nav.people");
	            
	            List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
	            formparams.add(new BasicNameValuePair("u", "837838004"));  
	            formparams.add(new BasicNameValuePair("p", "Zhang123"));  
	            
	            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
	            httppost.setEntity(uefEntity);
	            
	            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
	            httppost.setConfig(config);

	            CloseableHttpResponse response = httpclient.execute(httppost,localContext);
	            BasicCookieStore cookiestoreafter = (BasicCookieStore) localContext.getAttribute(HttpClientContext.COOKIE_STORE);
	            List<Cookie> cookies = cookiestoreafter.getCookies();
	            for (int i = 0; i < cookies.size(); i++) {
					System.out.println("used- " + cookies.get(i).toString());
				}
	            try {
	                System.out.println("----------------------------------------");
	                System.out.println(response.getStatusLine());
	                
	                
	               //System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));
	                EntityUtils.consume(response.getEntity());
	            } finally {
	                response.close();
	            }
	            
	            HttpGet httpGet = new HttpGet("http://zhaoren.t.qq.com/people?from=top&pgv_ref=web.wide.nav.people");
	            httpGet.setConfig(config);
	            CloseableHttpResponse response1 = httpclient.execute(httpGet,localContext);
	            System.out.println("-------------------1--------------------------");
	            System.out.println(response1.getStatusLine());
	           // System.out.println(EntityUtils.toString(response1.getEntity(),"UTF-8"));
	            EntityUtils.consume(response1.getEntity());
	        } finally {
	            httpclient.close();
	        }
	    }

}
