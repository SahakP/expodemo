package com.expo.demo.util;


import com.expo.demo.constants.Diciton;
import com.expo.demo.model.user.User;

public class XUtils {
    public static boolean isEditRole(User user) {
        if (user == null) {
            return false;
        }

        if (user.getRole() == null) {
            return false;
        }

        return user.getRole().getName().equals(Diciton.ROLE_ADMIN)
                || user.getRole().getName().equals(Diciton.ROLE_MODERATOR);
    }

    public static String getCurrentUserName(User user) {
        if (user == null || user.getUserInfo() == null) {
            return "";
        }
        return user.getUserInfo().getFirstName() + " " + user.getUserInfo().getLastName();
    }

}
