package com.hyjf.app.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class SSLClient extends DefaultHttpClient {

	public SSLClient() throws Exception {
		super();
		// 支持SSL数字证书
		SSLContext ctx = SSLContext.getInstance("SSL", "SunJSSE");
		X509TrustManager tm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		// 设置协议的类型和密钥信息，以及断开信息
		this.getConnectionManager().getSchemeRegistry()
				.register(new Scheme("https", 443, ssf));

	}

	/*public static HttpClient getSSLClient() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		FileInputStream instream = null;
		KeyStore trustStore = null;
		Scheme sch = null;
		try {
			// 获取默认的存储密钥类
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			// 加载本地的密钥信息
			instream = new FileInputStream(new File("my.keystore"));
			trustStore.load(instream, "nopassword".toCharArray());
			// 创建SSLSocketFactory，创建相关的Socket
			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			// 设置协议的类型和密钥信息，以及断开信息
			sch = new Scheme("https", socketFactory, 443);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 在连接管理器中注册中信息
		httpclient.getConnectionManager().getSchemeRegistry().register(sch);
		return httpclient;
	}*/
}
