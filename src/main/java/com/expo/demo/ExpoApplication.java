package com.expo.demo;

import com.expo.demo.constants.Constants;
import com.expo.demo.constants.Diciton;
import com.expo.demo.logic.UserHelper;
import com.expo.demo.model.user.Role;
import com.expo.demo.model.user.User;
import com.expo.demo.model.user.UserInfo;
import com.expo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class ExpoApplication implements CommandLineRunner {
    @Autowired
    private UserService mUserService;

    @Autowired
    private UserHelper mUserHelper;

    public static void main(String[] args) {
        SpringApplication.run(ExpoApplication.class, args);
    }

    /**
     * In this method we add new ADMIN user, if there is no admins in DB
     *
     * @param args that I'm not using
     */
    @Override
    public void run(String... args) {
        //Important. This code initializes system, do not remove
        mUserService.createRoles();


        List<User> admins = mUserService.findAllAdmins();
        if (admins.isEmpty()) {
            Role roleAdmin = mUserService.findRoleByName(Diciton.ROLE_ADMIN);
            User adminUser = new User();
            adminUser.setUsername(Constants.DEFAULT_ADMIN_USERNAME);
            adminUser.setPassword(Constants.DEFAULT_ADMIN_PASSWORD);
            adminUser.setRole(roleAdmin);
            adminUser.setEnabled(true);

            // for testing purposes

            UserInfo userInfo = new UserInfo();
            userInfo.setFirstName("Admin");
            userInfo.setLastName("Admin");
            userInfo.setPatternName("Admin");
            userInfo.setEmail("admin@admin.com");
            userInfo.setPhone("111 1111 11");
            adminUser.setUserInfo(mUserService.saveUserInfo(userInfo));

            mUserService.save(adminUser);
        }
    }
}