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
package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.UserDao;
import org.androidpn.server.model.User;
import org.androidpn.server.service.UserNotFoundException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/** 
 * This class is the implementation of UserDAO using Spring's HibernateTemplate.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class UserDaoHibernate extends HibernateDaoSupport implements UserDao {

    public User getUser(Long id) {
        return (User) getHibernateTemplate().get(User.class, id);
    }

    public User saveUser(User user) {
        getHibernateTemplate().saveOrUpdate(user);
        getHibernateTemplate().flush();
        return user;
    }

    
    //删除用户
    public void removeUser(Long id) {
        getHibernateTemplate().delete(getUser(id));
    }

    //判断是否存在用户
    public boolean exists(Long id) {
        User user = (User) getHibernateTemplate().get(User.class, id);
        return user != null;
    }

    //查找所有用户
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return getHibernateTemplate().find(
                "from User u order by u.createdDate desc");
    }

    //根据订阅条件查找用户
    @SuppressWarnings("unchecked")
    public List<User> getUsersBySubscriptions(String subscription) throws UserNotFoundException{
    	System.out.println("find subscriptions:"+subscription);
		List<User> subUsers = getHibernateTemplate().find("from User where subscriptions like '%"+subscription+"%' or subscriptions like '%all%'");///////////////////////changed
		if (subUsers==null || subUsers.isEmpty()){
			throw new UserNotFoundException();
		}
		else{
			System.out.println("UserDaoHiberate.getUserby..:"+subUsers);
			return (subUsers);
		}    	    	
    }
    
    //根据用户名查找用户
    @SuppressWarnings("unchecked")
    public User getUserByUsername(String username) throws UserNotFoundException {
        List users = getHibernateTemplate().find("from User where username=?",
                username);
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("User '" + username + "' not found");
        } else {
            return (User) users.get(0);
        }
    }

    //    @SuppressWarnings("unchecked")
    //    public User findUserByUsername(String username) {
    //        List users = getHibernateTemplate().find("from User where username=?",
    //                username);
    //        return (users == null || users.isEmpty()) ? null : (User) users.get(0);
    //    }

}
