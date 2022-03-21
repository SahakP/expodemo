package com.expo.demo.service;


import com.expo.demo.model.user.Role;
import com.expo.demo.model.user.User;
import com.expo.demo.model.user.UserInfo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);


    UserInfo saveUserInfo(UserInfo userInfo);

    User findByUsername(String username);

    Role findRoleByName(String name);

    List<User> findAllAdmins();

    Page<User> getUsersByPage(String role, int page);

    Page<User> getUsersByPage(String role, int page, boolean status);

    Page<User> getUsersByPage(List<String> roles, int page);

    Optional<User> findUserById(Long id);

    void deleteUser(User user);

    void createRoles();


}
