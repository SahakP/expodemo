package com.expo.demo.logic;

import com.expo.demo.constants.Diciton;
import com.expo.demo.model.user.Role;
import com.expo.demo.model.user.User;
import com.expo.demo.service.SecurityServiceImpl;
import com.expo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserHelper {

    @Autowired
    private UserService mUserService;

    @Autowired
    private SecurityServiceImpl mSecurityService;

    public User getCurrentUser() {
        String username;
        Authentication auth = mSecurityService.findLoggedInUsername();
        if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
        } else {
            return null;
        }

        return mUserService.findByUsername(username);
    }


    public Role getCurrentRole() {
        User user = getCurrentUser();
        if (user != null) {
            return user.getRole();
        }
        return null;
    }


    public boolean isAdmin() {
        Role role = getCurrentRole();
        if (role == null) {
            return false;
        }
        return role.getName().equals(Diciton.ROLE_ADMIN);
    }


    public boolean isMember() {
        Role role = getCurrentRole();
        if (role == null) {
            return false;
        }
        return role.getName().equals(Diciton.ROLE_MEMBER);
    }


    public boolean isModerator() {
        Role role = getCurrentRole();
        if (role == null) {
            return false;
        }
        return role.getName().equals(Diciton.ROLE_MODERATOR);
    }


}