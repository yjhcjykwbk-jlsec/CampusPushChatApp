package org.androidpn.server.console.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import javax.servlet.http.HttpSession;

public class AdminLoginController  extends MultiActionController  {
    private UserService userService;
	public AdminLoginController(){
		userService = ServiceLocator.getUserService();
	}
	
	public ModelAndView login(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	 User admin = userService.getUserByUsername("pushadmin");
    	 String adminName = request.getParameter("adminname");  
    	 String adminPwd = request.getParameter("adminpwd");
    	 String encryptedPW = null;
    	 HttpSession session = request.getSession();
    	 //是管理员登陆
    	 if(adminName!=null&&adminPwd!=null&&adminName.equals("pushadmin")){
    		 	//密码进行加密
				try {
					encryptedPW = toMD5((adminPwd).getBytes("GBK"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			System.out.println("MD5加密后的密码："+encryptedPW);
			//密码匹配成功
			if(encryptedPW.equals(admin.getPassword())){
				System.out.println("admin验证成功!");				
				session.setAttribute("Login", "OK");
			}
			else{
				session.setAttribute("Login", "failed");
			}
    	 }
    	 else{
				session.setAttribute("Login", "failed");
		 }
    	 
        ModelAndView mav = new ModelAndView();
        // mav.addObject("pushstaticsList", staticsList);
        mav.setViewName("index");
        return mav;
   }
    	
	
	//对用户密码MD5加密
		protected String toMD5(byte[] pwd) {
			// TODO Auto-generated method stub
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(pwd);
				StringBuffer sb =new StringBuffer();
				for (byte b:md.digest()) {
					sb.append(String.format("%02x", b&0xff) );
				}
				return sb.toString();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();return null;
			}		
			
		}
}
