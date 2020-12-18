package com.cl.ysyd.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信工具
 * @author shibaomi
 */
@Slf4j
public class WeChatUtil {
	
	private static CloseableHttpClient httpClient;
	
	/**
	 * 配置微信ticker超时时间
	 */
	private static Long timeOut=1000*60*90l;
	
	/**
	 * 缓存微信的ticket信息,单服务使用，多服务需要保存到数据库或者redis里面
	 */
	private static Map<String,String> webchatTicket=new ConcurrentHashMap<>();
	
	static {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
			httpClient=HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取https client失败",e);
		}
	}
	
	/**
	 * 1）将token、timestamp、nonce三个参数进行字典序排序 
	 * 2）将三个参数字符串拼接成一个字符串进行sha1加密 
	 * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * @param signature:微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
	 * @param timestamp:微信时间戳
	 * @param nonce:微信请求随机数
	 * @param echostr:微信请求随机字符串，验证成功原样返回
	 * @param token:微信基本服务器配置时填写的token
	 */
	public static String checkSignature(String signature, String timestamp, String nonce,String echostr,String token) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 对数组内的token、timestamp、nonce三个元素进行字典序排序
		Arrays.sort(arr);
		// 将排序后的token、timestamp、nonce三个字符串按顺序拼接成一个字符串
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
		}
		// 对拼接好的字符串进行SHA-1加密
		String tmpStr = WeChatEncrypt.encrypt(sb.toString(), WeChatEncrypt.SHA_1);
		log.info("token={}, timestamp={},  nonce={}, 加密后字符串tmpStr={}, 微信签名字符串signature={}, 微信返回echostr={}, ",
				token, timestamp, nonce,tmpStr,signature,echostr);

		// 将加密后的字符串与signature进行比较
		if (tmpStr.equals(signature)) {
			return echostr;// 身份验证成功，将echostr字符串内容原样返回
		} else {
			return "";// 身份验证失败，返回空字符串
		}
	}
	
	/**
	 * 获取微信交互的token
	 * @param appId :微信下发的appid
	 * @param appSecret : 微信配置的秘钥
	 * @return
	 */
	public static String getToken(String appId,String appSecret) {
		StringBuilder str=new StringBuilder("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=");
		str.append(appId).append("&secret=").append(appSecret);
		String result=doGet(str.toString(), "utf-8");
		log.info("appid:{},appSecret:{},请求结果:{}",appId,appSecret,result);
		JSONObject jsonR=JSONObject.parseObject(result);
		return jsonR.getString("access_token");
	}
	
	/**
	 * 获取微信的ticket
	 * @param appId :微信下发的appid
	 * @param appSecret : 微信配置的秘钥
	 * @return
	 */
	public static String getJsApiTicket(String appId,String appSecret) {
		//获取token
		String token=getToken(appId, appSecret);
		//获取ticket
		StringBuilder str=new StringBuilder("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=");
		str.append(token);
		String result=doGet(str.toString(), "utf-8");
		log.info("token:{},请求结果:{}",token,result);
		JSONObject jsonR=JSONObject.parseObject(result);
		return jsonR.getString("ticket");
	}
	
	/**
	 * 从缓存中获取微信的ticket
	 * @param appId :微信下发的appid
	 * @param appSecret : 微信配置的秘钥
	 * @return
	 */
	public static String getWebChatJsApiTicketByCache(String appId,String appSecret) {
		String webChatCacheStr=webchatTicket.get("ticket");
		if(StringUtils.isEmpty(webChatCacheStr)) {
			//不存在微信的ticket，则重新获取并保存到缓存中
			log.info("单台服务器,第一次加载，需要请求微信获取ticket");
			return loadNewTicket(appId,appSecret);
		}else{
			//存在缓存中，则判断是否超时
			log.info("单台服务器,非第一次加载，从缓存中读取微信ticket");
			synchronized (webchatTicket){
				JSONObject ticketJs=JSONObject.parseObject(webChatCacheStr);
				Long lastTime=ticketJs.getLongValue("now");
				Long diff=System.currentTimeMillis()-lastTime;
				if(diff>timeOut) {
					log.info("单台服务器,ticket已经超时，重新从微信获取ticket");
					//超时重新加载ticket
					return loadNewTicket(appId,appSecret);
				}
				log.info("单台服务器,ticket未超时，从缓存中获取微信ticket");
				return ticketJs.getString("ticket");
			}
		}
	}
	
	/**
	 * 获取微信ticket并缓存到内存里
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	private static String loadNewTicket(String appId,String appSecret) {
		//不存在微信的ticket，则重新获取并保存到缓存中
		String ticket=getJsApiTicket(appId, appSecret);
		JSONObject ticketJs=new JSONObject();
		ticketJs.put("ticket", ticket);
		ticketJs.put("now", System.currentTimeMillis());
		//保存微信ticket到缓存里面
		webchatTicket.put("ticket", ticketJs.toJSONString());
		return ticket;
	}
	
	/**
	 * 自定义get请求
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String doGet(String url,String charset){
        String result = null;
        try{
        	HttpGet httpGet = new HttpGet(url);
        	httpGet.addHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpGet);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
            return result;
        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("get 请求失败", ex);
        }
    }
}
