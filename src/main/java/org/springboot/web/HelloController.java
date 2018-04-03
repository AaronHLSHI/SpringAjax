package org.springboot.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springboot.entity.Otp;
import org.springboot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Intellij IDEA.
 *
 * @Author LUOLIANG
 * @Date 2016/8/2
 * @Comment
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

	final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	@Autowired
    private OtpService otpService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		modelMap.put("msg", "SpringBoot Ajax 示例");

		return "index";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	@ResponseBody
	public String home() {

		return "home";
	}

	@RequestMapping(value = "/data", method = RequestMethod.POST)
	@ResponseBody
	public boolean data(HttpServletRequest req, HttpServletResponse rep) throws UnsupportedEncodingException, MessagingException {

		String email = req.getParameter("phoNum");
		String code = generateCode();

		sendEmail(email, code);

		Otp otp = new Otp();
		otp.setCode(code);
		
		otpService.insert(otp);
//		final HttpSession httpSession = req.getSession(true);
//		httpSession.setAttribute("authCode", code);
		
		return true;
	}

	@RequestMapping(value = "/CheckCode", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkCode(HttpServletRequest req, HttpServletResponse rep)
			throws MessagingException, IOException {
		List<User> list = new ArrayList<User>();

		HttpSession session = req.getSession();
		String usercode = req.getParameter("CheckCode"); // 获取用户输入的验证码
//		String sessioncode = (String) session.getAttribute("authCode"); // 获取保存在session里面的验证码
		
		List<Otp> olist = otpService.findAll();
		String authCode = olist.get(0).getCode();
		String result = "";
		if (usercode != null && usercode.equals(authCode)) { // 对比两个code是否正确
			return true;
		} else {
			return false;
		}
//		PrintWriter out = rep.getWriter();
//		out.write(result.toString()); // 将数据传到前台
	}

	public static boolean sendEmail(String emailaddress, String code)
			throws MessagingException, UnsupportedEncodingException {

		java.util.Properties props = new java.util.Properties();
		props.put("username", "490233290@qq.com");
		props.put("password", "sfltsppxcvxncbdg");

		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", "smtp.qq.com");// 指定SMTP服务器
		props.setProperty("mail.smtp.auth", "true");// 指定是否需要SMTP验证
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.port", "465");

		Session mailSession = Session.getDefaultInstance(props, new Authenticator() {
			// 身份认证
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("490233290@qq.com", "sfltsppxcvxncbdg");
			}
		});

		mailSession.setDebug(true);// 是否在控制台显示debug信息

		MimeMessage testMessage = new MimeMessage(mailSession);
		testMessage.setFrom("490233290@qq.com");
		testMessage.addRecipients(javax.mail.Message.RecipientType.TO, emailaddress);
		testMessage.setSentDate(new java.util.Date());
		testMessage.setSubject(MimeUtility.encodeText("发送邮件验证码", "gb2312", "B"));

		testMessage.setContent("验证码是:" + code, "text/html;charset=gb2312");
		System.out.println("Message constructed");

		// Step 3: Now send the message
		Transport transport = mailSession.getTransport("smtp");
		transport.connect("smtp.qq.com", "490233290@qq.com", "sfltsppxcvxncbdg");
		transport.sendMessage(testMessage, testMessage.getAllRecipients());
		return false;
	}

	public static String generateCode() {
		
		String code = String.valueOf((int)((Math.random()*9+1)*100000));
		
		return code;
	}
}
