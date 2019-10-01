package cn._42pay.simplepay.framework.util;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.HttpContentType;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2018/6/22.
 */
public class HttpUtil {
	private static final String PAY_APP_ID_STR="payAppId";

	private static final String MERCHANT_ID_STR="merchantId";

	private static final String PAY_CODE_STR="payCode";

	private static final String USER_ID_STR="userId";

	/**
	 * httpclient对象.
	 */
	private static final 	OkHttpClient client = new OkHttpClient();

	/**
	 * 获取请求的URL地址
	 * @param request
	 * @return
	 */
	public static String getRequestUrl(HttpServletRequest request){
		Map<String, String[]> params = request.getParameterMap();
		String queryString=params.keySet().stream().reduce("",(result,key)->
			result+Arrays.stream(params.get(key)).reduce("",(param,element)->param + key + "=" + element + "&")
		);
		if (queryString.length() > 1) {
			queryString = queryString.substring(0, queryString.length() - 1);
		}
		return request.getRequestURL() + "?" + queryString;
	}

	/**
	 * 获取请求的URL地址
	 * @param request
	 * @return
	 */
	public static String getRequestUrlRemovePaySetting(HttpServletRequest request){
		Map<String, String[]> params = request.getParameterMap();
		String queryString=params.keySet().stream().
				filter(key->!key.equals(PAY_APP_ID_STR)
						&&!key.equals(MERCHANT_ID_STR)
						&&!key.equals(PAY_CODE_STR)
						&&!key.equals(USER_ID_STR)).
				reduce("",(result,key)-> result+Arrays.stream(params.get(key)).
						reduce("",(param,element)->param + key + "=" + element + "&")
		);
		if (queryString.length() > 1) {
			queryString = queryString.substring(0, queryString.length() - 1);
		}

		return request.getRequestURL() + "?" + queryString;
	}

	/**
	 * 请求转成map
	 * @param body
	 * @return
	 */
	public static Map<String,String> requestParamToMap(String body)  {
		Map<String,String> resultMap=new HashMap<>(20);
		String[] splitStrs=body.split("&");
		for(String str:splitStrs){
			String[] param=str.split("=");
			if(param.length==2){
				String key=param[0];
				String value= null;
				try {
					value = URLDecoder.decode(param[1], PayConst.UTF8);
				} catch (UnsupportedEncodingException e) {
					throw new SimplePayException(ErrorCodeEnum.URL_DECODE_EXCEPTION);
				}
				resultMap.put(key,value);
			}
		}
		return resultMap;
	}

	/**
	 * 获取请求的参数
	 * @param request
	 * @return
	 */
	public static String getRequestParams(HttpServletRequest request){
		Map<String, String[]> params = request.getParameterMap();
		String queryString=params.keySet().stream().reduce("",(result,key)->
				result+Arrays.stream(params.get(key)).reduce("",(param,element)->param + key + "=" + element + "&")
		);
		if (queryString.length() > 1) {
			queryString = queryString.substring(0, queryString.length() - 1);
		}
		return  queryString;
	}

	public static String getBaseUrl(HttpServletRequest request){
		String basePath;
		if( request.getServerPort()==PayConst.PORT_80||request.getServerPort()==PayConst.PORT_443){
			basePath=getScheme(request) + "://" + request.getServerName() +  request.getContextPath();
		}else{
			basePath=getScheme(request) + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		}
		return basePath;
	}

	/**
	 * 获取http头
	 * @param request
	 * @return
	 */
	private static String getScheme(HttpServletRequest request){
		HttpRequest httpRequest = new ServletServerHttpRequest(request);
		UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
		return  uriComponents.getScheme();
	}

	/**
	 * 发送GET请求
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	/**
	 * 发送POST请求(缺省使用text/html媒体类型)
	 *
	 * @param content
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String post(String content, String url) throws IOException {
		RequestBody body = RequestBody.create(MediaType.parse(HttpContentType.TEXTHTML.getType()), content);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	/**
	 * 发送POST请求(自定义媒体类型)
	 *
	 * @param content
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String post(String content, String url, HttpContentType contentType) throws IOException {
		RequestBody body = RequestBody.create(MediaType.parse(contentType.getType()), content);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	/**
	 * 微信退费HTTP请求
	 *
	 * @param url      微信退费接口地址
	 * @param content  发送数据
	 * @param password 证书密码
	 * @param certPath 证书CLASSPATH绝对路径
	 * @return
	 * @throws Exception
	 */
	public static String doWechatSslPost(String url, String content, String password, String certPath) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");

		File file = new File(certPath);
		try (BufferedInputStream instream = new BufferedInputStream( new FileInputStream(file), 127)) {
			keyStore.load(instream, password.toCharArray());
		}

		SSLContext sslContext = SSLContext.getInstance("TLS");

		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);
		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

		X509TrustManager trustManager = null;
		for (TrustManager manager : trustManagers) {
			if (manager instanceof X509TrustManager) {
				trustManager = (X509TrustManager) manager;
				break;
			}
		}

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, password.toCharArray());
		KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

		sslContext.init(keyManagers, null, new SecureRandom());

		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

		OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager).build();

		RequestBody body = RequestBody.create(MediaType.parse(HttpContentType.TEXTHTML.getType()), content);
		Request request = new Request.Builder().url(url).post(body).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	/**
	 * 构建application/x-www-form-urlencoded表单提交的参数
	 *
	 * @param params
	 * @return
	 */
	public static String buildFormUrlEncodedParams(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuffer buffer = new StringBuffer();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey(), value = entry.getValue();
			if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
				buffer.append(URLEncoder.encode(key, PayConst.UTF8)).append("=").
						append(URLEncoder.encode(value, PayConst.UTF8)).append("&");
			}
		}

		return buffer.deleteCharAt(buffer.length() - 1).toString();
	}


}
