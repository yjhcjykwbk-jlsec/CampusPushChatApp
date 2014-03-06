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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.console.vo.SubscriptionsVO;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/** 
 * A controller class to process the user related requests.  
 *
 * @author xiaobingo
 */
public class SubscriptionsController extends MultiActionController {

    private UserService userService;
    public SubscriptionsController() {
        userService = ServiceLocator.getUserService();
    }

    @SuppressWarnings("unchecked")
	public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
//    	int count_all=0;                  //数组第0项
//        int count_news_yaowen=0;			 //数组第1项
//        int count_pkusz_notification=0;   //数组第2项
//        int count_video_schoolvideo=0;    //数组第3项
//        int count_video_cievideo=0;       //数组第4项
//        int count_video_hsbcvideo=0;      //数组第5项
//        int count_video_stlvideo=0;       //数组第6项
//        int count_video_renwenvideo=0;    //数组第7项
//        int count_video_leisurevideo=0;   //数组第8项
    	Map<String,Integer> subCnt=new HashMap<String,Integer>();
        List<User> userList = userService.getUsers();
        for (User user : userList) {        	
        	String subscriptions = user.getSubscriptions();
        	System.out.println("该用户的subscriptions是："+subscriptions);
        	if(subscriptions!=null){
	        	String[] userSub = subscriptions.split(";");
	        	for(int i=0;i<userSub.length;i++) {
	        		if(userSub[i]==null||userSub[i].equals("")||userSub[i].equals("null")) continue;
	        		System.out.println(userSub[i]);
	        		if(subCnt.containsKey(userSub[i])){
	        			subCnt.put(userSub[i], subCnt.get(userSub[i])+1);
	        		}
	        		else subCnt.put(userSub[i], 1);
//	        	if(userSub[0]!=null){
//	        		count_all++; //订阅所有栏目的人数+1
//	        	}
//	        	if(userSub[1]!=null){
//	        		count_news_yaowen++; //订阅南燕要闻栏目的人数+1
//	        	}
//	        	if(userSub[2]!=null){
//	        		count_pkusz_notification++; //订阅通知公告栏目的人数+1
//	        	}
//	        	if(userSub[3]!=null){
//	        		count_video_schoolvideo++; //订阅校园视频栏目的人数+1
//	        	}
//	        	if(userSub[4]!=null){
//	        		count_video_cievideo++; //订阅信息学院视频栏目的人数+1
//	        	}
//	        	if(userSub[5]!=null){
//	        		count_video_hsbcvideo++; //订阅汇丰商学院视频栏目的人数+1
//	        	}
//	        	if(userSub[6]!=null){
//	        		count_video_stlvideo++; //订阅国际法学院视频栏目的人数+1
//	        	}
//	        	if(userSub[7]!=null){
//	        		count_video_renwenvideo++; //订阅人文学院视频栏目的人数+1
//	        	}
//	        	if(userSub[8]!=null){
//	        		count_video_leisurevideo++; //订阅休闲视频栏目的人数+1
	        	}
        	}
        	
        }
//        System.out.println("订阅所有栏目的共有："+count_all+" 人");
//        System.out.println("订阅南燕要闻栏目的共有："+count_news_yaowen+" 人");
//        System.out.println("订阅通知公告栏目的共有："+count_pkusz_notification+" 人");
//        System.out.println("订阅校园视频栏目的共有："+count_video_schoolvideo+" 人");
//        System.out.println("订阅信息学院视频栏目的共有："+count_video_cievideo+" 人");
//        System.out.println("订阅汇丰商学院视频栏目的共有："+count_video_hsbcvideo+" 人");
//        System.out.println("订阅国际法学院视频栏目的共有："+count_video_stlvideo+" 人");
//        System.out.println("订阅人文学院视频栏目的共有："+count_video_renwenvideo+" 人");
//        System.out.println("订阅休闲视频栏目的共有："+count_video_leisurevideo+" 人");

        List<SubscriptionsVO> subscriptionsList = new ArrayList<SubscriptionsVO>();
        for(Map.Entry<String, Integer>entry:subCnt.entrySet()){
        	System.out.println("订阅"+entry.getKey()+"的有："+entry.getValue()+"人");
        	 SubscriptionsVO vo = new SubscriptionsVO();
        	 vo.setSubscriptionName(entry.getKey());
        	 vo.setCount(entry.getValue());
        	 subscriptionsList.add(vo);
        }
        Collections.sort(subscriptionsList);
//        SubscriptionsVO vo = new SubscriptionsVO();
//        vo.setSubscriptionName("订阅所有");
//        vo.setCount(count_all);
//        subscriptionsList.add(vo);
//        SubscriptionsVO vo1 = new SubscriptionsVO();
//        vo1.setSubscriptionName("南燕要闻");
//        vo1.setCount(count_news_yaowen);
//        subscriptionsList.add(vo1);
//        SubscriptionsVO vo2 = new SubscriptionsVO();
//        vo2.setSubscriptionName("校园通知公告");
//        vo2.setCount(count_pkusz_notification);
//        subscriptionsList.add(vo2);
//        SubscriptionsVO vo3 = new SubscriptionsVO();
//        vo3.setSubscriptionName("校园视频");
//        vo3.setCount(count_video_schoolvideo);
//        subscriptionsList.add(vo3);
//        SubscriptionsVO vo4 = new SubscriptionsVO();
//        vo4.setSubscriptionName("信息学院视频");
//        vo4.setCount(count_video_cievideo);
//        subscriptionsList.add(vo4);
//        SubscriptionsVO vo5 = new SubscriptionsVO();
//        vo5.setSubscriptionName("汇丰商学院视频");
//        vo5.setCount(count_video_hsbcvideo);
//        subscriptionsList.add(vo5);
//        SubscriptionsVO vo6 = new SubscriptionsVO();
//        vo6.setSubscriptionName("国际法学院视频");
//        vo6.setCount(count_video_stlvideo);
//        subscriptionsList.add(vo6);
//        SubscriptionsVO vo7 = new SubscriptionsVO();
//        vo7.setSubscriptionName("人文学院视频");
//        vo7.setCount(count_video_renwenvideo);
//        subscriptionsList.add(vo7);
//        SubscriptionsVO vo8 = new SubscriptionsVO();
//        vo8.setSubscriptionName("休闲视频");
//        vo8.setCount(count_video_leisurevideo);
//        subscriptionsList.add(vo8);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("subscriptionsList", subscriptionsList);
        mav.setViewName("subscriptions/list");
        return mav;
    }
      
    
}
