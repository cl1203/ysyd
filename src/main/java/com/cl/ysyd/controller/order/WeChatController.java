package com.cl.ysyd.controller.order;

import com.cl.ysyd.common.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理微信消息和事件专用控制器
 */
@Controller
@RequestMapping("/webchat")
@CrossOrigin(origins = {"*", "null"})
@Slf4j
public class WeChatController {
	
	private String appId = "wx3c0198d93a741cfe";
	
	private String appSecret = "db596beec77e9cc42331f7830cd65d85";
	
	private String token = "test";

	/**
	 * 验证微信服务器端GET请
	 * @param request 请求
	 * @param response 响应
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public void checkSignature(HttpServletRequest request, HttpServletResponse response) {
		
		String signature = request.getParameter("signature"); // 微信加密签名
		String timestamp = request.getParameter("timestamp"); // 时间戳
		String nonce = request.getParameter("nonce"); // 随机数
		String echostr = request.getParameter("echostr"); // 随机字符串
		
		String checkResult = WeChatUtil.checkSignature(signature, timestamp, nonce, echostr, token);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(checkResult);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("微信服务配置验证失败:",e);
		}finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	@GetMapping("/ticket")
	@ResponseBody
	public String getWebChatTicket() {
		return WeChatUtil.getWebChatJsApiTicketByCache(appId, appSecret);
	}
}
