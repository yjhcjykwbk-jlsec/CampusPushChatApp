/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.console.controller;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.presence.PresenceManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/** 
 * A controller class to process the user related requests.  
 *
 * @author xiaobingo
 */
public class UserController extends MultiActionController {

    private UserService userService;

    public UserController() {
        userService = ServiceLocator.getUserService();
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PresenceManager presenceManager = new PresenceManager();
        List<User> userList = userService.getUsers();
        for (User user : userList) {
            if (presenceManager.isAvailable(user)) {
                // Presence presence = presenceManager.getPresence(user);
                user.setOnline(true);
            } else {
                user.setOnline(false);
            }
            // logger.debug("user.online=" + user.isOnline());
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("userList", userList);
        mav.setViewName("user/list");
        return mav;
    }
    
  //�Զ���,��http://push.pkusz.edu.cn��ȡ���û��������룬�����浽���ݿ�    
    public ModelAndView saveUser(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String userName = ServletRequestUtils.getStringParameter(request, "userName");
    	String userPWD = ServletRequestUtils.getStringParameter(request, "userPWD");
    	String userEmail = ServletRequestUtils.getStringParameter(request, "userEmail");
    	System.out.println("��������Դ��վ�յ����û�����"+userName);
    	System.out.println("��������Դ��վ�յ����û����룺"+userPWD);
    	System.out.println("��������Դ��վ�յ����û�Email��"+userEmail);
    	
    	try{
    		User us = userService.getUserByUsername(userName);
    		us.setUsername(userName);
	        us.setPassword(userPWD);
	        us.setEmail(userEmail);
	        us.setName(userName);
	        userService.saveUser(us);
    	}catch(UserNotFoundException eee){ //�û�������,�½����û�
    		User newUser = new User();
	        newUser.setUsername(userName);
	        newUser.setPassword(userPWD);
	        newUser.setEmail(userEmail);
	        newUser.setName(userName);
	        userService.saveUser(newUser);
    	}   	
    	
    	return null;
    }

    //�Զ���,��android��ȡ���û��������룬��֤��������֤�Ƿ�ɹ�    
    public ModelAndView checkUser(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String androidName = ServletRequestUtils.getStringParameter(request, "androidName");
    	String androidPwd = ServletRequestUtils.getStringParameter(request, "androidPwd");
    	System.out.println("��android�յ����û�����"+androidName);
    	System.out.println("��android�յ����û����룺"+androidPwd);
    	
    	//�����ݿ���ȡ���û��Ա�
		ServletOutputStream out = response.getOutputStream();
    	try{
    		User us = userService.getUserByUsername(androidName);
    		if(us.getPassword().equalsIgnoreCase(androidPwd)){
    			System.out.println("�û�"+androidName+"��֤�ɹ�");
    			response.setContentType("text/plain");
    			out.print("check:success");
    			out.flush();
    		}
    		else{
    			System.out.println("�û�"+androidName+"��֤ʧ��");
    			response.setContentType("text/plain");
    			out.print("check:password failure");
    			out.flush();
    		}
    	}catch(UserNotFoundException e){
    		System.out.println("�û�"+androidName+"��֤ʧ�ܣ������ڸ��û�");
			response.setContentType("text/plain");
			out.print("check:not exist");
			out.flush();
    	}
        
    	return null;
    }
    
    //�Զ���,��android��ȡ�����û����ļ�¼�����ض��ļ�¼ 
    public ModelAndView getSubscription(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String userName = ServletRequestUtils.getStringParameter(request, "userName");
    	System.out.println("��android�յ����û�����"+userName);
    	
    	//�����ݿ���ȡ�����û��Ķ��ļ�¼
		ServletOutputStream out = response.getOutputStream();
    	try{
    		User us = userService.getUserByUsername(userName);
    		response.setContentType("text/plain");
			out.print(us.getSubscriptions()); //�����û��Ķ��ļ�¼
			out.flush();
    	}catch(UserNotFoundException e){
    		System.out.println("�û�"+userName+"��֤ʧ�ܣ������ڸ��û�");
			response.setContentType("text/plain");
			out.print("check:not exist");
			out.flush();
    	}

    	return null;
    }
    
    
    
}
