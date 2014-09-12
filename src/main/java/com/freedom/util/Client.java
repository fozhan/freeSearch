package com.freedom.util;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Client {

	public static void main(String[] args) throws Exception {

		HttpHost proxy = new HttpHost("10.217.128.254", 8080, "http");
		RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).setProxy(proxy).build();
		
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		//httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);test
		try {
			HttpGet httpget = new HttpGet("http://t.qq.com/");
			httpget.setConfig(config);
			CloseableHttpResponse response1 = httpclient.execute(httpget);
			try {
				HttpEntity entity = response1.getEntity();

				System.out.println("Login form get: "
						+ response1.getStatusLine());
				EntityUtils.consume(entity);

				System.out.println("Initial set of cookies:");
				List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
					}
				}
			} finally {
				response1.close();
			}

			HttpUriRequest login = RequestBuilder.post().setConfig(config)
					.setUri(new URI("http://t.qq.com/FoxZhang-Zone"))
					.addParameter("u", "837838004")
					.addParameter("p", "Zhang123").build();
			
			CloseableHttpResponse response2 = httpclient.execute(login);
			try {
				HttpEntity entity = response2.getEntity();

				System.out.println("Login form get: "
						+ response2.getStatusLine());
				
				//System.out.println(EntityUtils.toString(entity,"UTF-8"));
				EntityUtils.consume(entity);

				System.out.println("Post logon cookies:");
				List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
						System.out.println("-path " + cookies.get(i).getPath());
					}
				}
			} finally {
				response2.close();
			}
			
			HttpGet httpGet = new HttpGet("http://zhaoren.t.qq.com/people?from=top&pgv_ref=web.wide.nav.people");
            httpGet.setConfig(config);
            CloseableHttpResponse response3 = httpclient.execute(httpGet);
            System.out.println("-------------------1--------------------------");
            System.out.println(response3.getStatusLine());
           // System.out.println(EntityUtils.toString(response3.getEntity(),"UTF-8"));
            EntityUtils.consume(response3.getEntity());
		} finally {
			httpclient.close();
		}
	}

}
