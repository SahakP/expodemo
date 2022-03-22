package com.expo.demo.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Diciton {

    public static final String SCHEMA = "expo";

    public static final String TABLE_USER = "user";
    public static final String TABLE_USERINFO = "user_info";
    public static final String TABLE_ROLE = "role";


    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MEMBER = "ROLE_MEMBER";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final HashMap<String, String> ROLES;
    public static final List<String> CAN_EDIT_ROLES;

    static {
        CAN_EDIT_ROLES = new ArrayList<>();
        CAN_EDIT_ROLES.add(ROLE_ADMIN);
        CAN_EDIT_ROLES.add(ROLE_MODERATOR);
    }


    static {
        ROLES = new HashMap<>();
        ROLES.put(ROLE_ADMIN, ROLE_ADMIN);
        ROLES.put(ROLE_MEMBER, ROLE_MEMBER);
        ROLES.put(ROLE_MODERATOR, ROLE_MODERATOR);
    }


}
