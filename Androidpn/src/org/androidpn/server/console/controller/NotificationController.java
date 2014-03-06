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
import org.androidpn.server.service.UserExistsException;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.xmpp.packet.IQ;

/** 
 * A controller class to process the notification related requests.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationController extends MultiActionController {

    private NotificationManager notificationManager;
    public static long timeStart;

    private UserService userService;
    
    public NotificationController() {
        notificationManager = new NotificationManager();
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        // mav.addObject("list", null);
        mav.setViewName("notification/form");
        return mav;
    }

    
    
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	// 获取android客户端发来的订阅或取消订阅请求
    	System.out.println("notification get====");
    	String subscriber = ServletRequestUtils.getStringParameter(request, "subscriber"); //订阅者
    	String subscriptions = ServletRequestUtils.getStringParameter(request, "subscriptions"); //要订阅或取消订阅的topic
    	String apiKey = Config.getString("apiKey", "");
    	
    	ServletOutputStream out = response.getOutputStream();
    	
    	//更新数据库
        userService = ServiceLocator.getUserService();
        try{
	        User us = userService.getUserByUsername(subscriber);
	        us.setSubscriptions(subscriptions);
	        userService.saveUser(us);
	        response.setContentType("text/plain");
			out.print("subscribe:success"); //订阅成功
			out.flush();
        }catch(UserNotFoundException e){
        	response.setContentType("text/plain");
			out.print("subscribe:failure"); // 订阅失败
			out.flush();
        }catch(UserExistsException e){
        	response.setContentType("text/plain");
			out.print("subscribe:failure"); //订阅失败
			out.flush();
        }
    	
        /*
    	ModelAndView mav0 = new ModelAndView();
        mav0.setViewName("redirect:notification.do");
        return mav0;
        */
        return null;
    	
    }    
    
    
    public ModelAndView send(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String apiKey = Config.getString("apiKey", "");

	        /**
	         * 为南燕新闻网、深研院主页、push网站、视频监控等平台推送消息
	         */
	        System.out.println("请求的字符编码是："+request.getCharacterEncoding());  // UTF-8
	        //获取PuSH subcriber post过来的feed信息
	        String feedTitle = ServletRequestUtils.getStringParameter(request, "feedTitle");
	        String feedContent = ServletRequestUtils.getStringParameter(request, "feedContent");
	        String feedLink = ServletRequestUtils.getStringParameter(request, "feedLink");
	        System.out.println("---------------------feedTitle--------------------:"+feedTitle);
	        System.out.println("---------------------feedContent------------------:"+feedContent);
	        System.out.println("---------------------feedLink------------------:"+feedLink);
	     
	        
	        //发给对应订阅的用户
	        //来自摄像头监控 http://xx.xx.xx.xx/videoMonitor/monitor_laboratory/xxxx
	        if(feedLink.contains("videoMonitor")){
	        	//timeStart = System.currentTimeMillis();
        		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "monitor_all"); 
        		response.setContentType("text/plain");
        		ServletOutputStream out = response.getOutputStream();
        		out.print("getNotice:success"); //订阅成功
    			out.flush();
	        }
	       
	        //来自在线视频网站
	        else if(feedLink.contains("push.pkusz.edu.cn")){
	        	/*if(feedLink.contains("videocourseware")){ //所有视频课件
	        		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "video_videocourseware"); 
	        	}
	        	else
	        	*/ if(feedLink.contains("leisurevideo")){ //所有休闲视频
	        		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "video_leisurevideo"); 
	        	}
	        	else if(feedLink.contains("schoolvideo")){ //所有校园视频
	        		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "video_schoolvideo"); 
	        	}
	        	else if(feedLink.contains("cievideo")){ //信息学院视频课件
	        		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "video_cievideo"); 
	        	}
	        	else if(feedLink.contains("hsbcvideo")){ //汇丰商学院视频课件
	        		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "video_hsbcvideo"); 
	        	}
	        	else if(feedLink.contains("stlvideo")){ //国际法学院视频课件
	        		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "video_stlvideo"); 
	        	}
	        	else if(feedLink.contains("renwenvideo")){ //人文学院视频课件
	        		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "video_renwenvideo"); 
	        	}
	        }
	              
	        //来自英文网站
	        /*
	    	else if(feedLink.contains("english.pkusz.edu.cn")){
	    			if(feedLink.contains("News&Bulletin")){ // News&Bulletin
	    				notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "english_news");
	    			}
	    	}
	    	*/
	    	
	        //来自南燕新闻网
	    	else if(feedLink.contains("news.pkusz.edu.cn")){
	    			if(feedLink.contains("南燕要闻")){ //南燕要闻
	    				notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "news_yaowen");
	    			}
	    			/*
	    			else if(feedLink.contains("专题报道")){ //专题报道
	    	    		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "news_zhuanti");
	    	    	}
	    			else if(feedLink.contains("南燕人物")){ //南燕人物
	    	    		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "news_renwu");
	    	    	}
	    			else if(feedLink.contains("南燕电视")){ //南燕电视
	    	    		notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "news_dianshi");
	    	    	}
	    	    	*/
	    	}
	    	
	        //来自北大深研院——〉通知公告
	    	else if(feedLink.contains("www.pkusz.edu.cn")){
	    		if(feedLink.contains("通知公告")){ //通知公告
	    			notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, "pkusz_notification");
	    		}
	    	}            
	        //来自别的网站平台，统统推送
	    	else {
	    		String feedSection = ServletRequestUtils.getStringParameter(request, "feedSection");
	    		if(feedSection!=null&&feedSection!="")
	    			notificationManager.sendMyNotifications(apiKey, feedTitle, feedContent, feedLink, feedSection);
	    		else{
	    			System.out.println("null feedsection");
	    		}
	    	}
    	return null;
    }

    //在后台界面发送消息
    public ModelAndView admin_send(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String apiKey = Config.getString("apiKey", "");

    	String broadcast = ServletRequestUtils.getStringParameter(request,"broadcast");
    	/**
    	 * 从本服务器直接推送消息
    	 */
    	System.out.println("收到的broadcast是："+broadcast);
    	if(broadcast!=null){
	        String username = ServletRequestUtils.getStringParameter(request,"username");
	        String title = ServletRequestUtils.getStringParameter(request, "title");
	        String message = ServletRequestUtils.getStringParameter(request, "message");
	        String uri = ServletRequestUtils.getStringParameter(request, "uri");
	        
	        //发给在线用户
	        if (broadcast.equalsIgnoreCase("Y")) {	        	
	        	timeStart = System.currentTimeMillis();
	        	
	            notificationManager.sendBroadcast(apiKey, title, message, uri);
	        //发给所有用户
	        }else if (broadcast.equalsIgnoreCase("A")) {
	            notificationManager.sendAllBroadcast(apiKey, title, message, uri);
	        //发给指定用户
	        }else if (broadcast.equalsIgnoreCase("N")){	        	
	            notificationManager.sendNotifications(apiKey, username, title, message, uri);
	        }
    	}
    	
    	ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:notification.do");
        return mav;
        
    }
}
