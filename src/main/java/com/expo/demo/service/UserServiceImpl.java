package com.expo.demo.service;

import com.expo.demo.constants.Constants;
import com.expo.demo.constants.Diciton;
import com.expo.demo.model.user.Role;
import com.expo.demo.model.user.User;
import com.expo.demo.model.user.UserInfo;
import com.expo.demo.repository.user.RoleRepo;
import com.expo.demo.repository.user.UserInfoRepo;
import com.expo.demo.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserInfoRepo userInfoRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void createRoles() {
        for (String key : Diciton.ROLES.keySet()) {
            String value = Diciton.ROLES.get(key);
            if (roleRepo.findByName(value) == null) {
                Role role = new Role();
                role.setName(value);
                roleRepo.save(role);
            }
        }
    }

    @Override
    public void save(User user) {
        if (user.getNotValidatePassword() == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(user.getPassword());
        }
        userRepo.save(user);
    }


    @Override
    public UserInfo saveUserInfo(UserInfo userInfo) {
        return userInfoRepo.save(userInfo);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepo.findByName(name);
    }

    @Override
    public List<User> findAllAdmins() {
        return userRepo.findByRole(roleRepo.findByName(Diciton.ROLE_ADMIN));
    }

    @Override
    public Page<User> getUsersByPage(String role, int page) {
        Pageable pageable = PageRequest.of(page, Constants.DEFAULT_PAGE_SIZE);
        return userRepo.findByRoleOrderByUpdatedAtDesc(roleRepo.findByName(role), pageable);
    }

    @Override
    public Page<User> getUsersByPage(String role, int page, boolean status) {
        Pageable pageable = PageRequest.of(page, Constants.DEFAULT_PAGE_SIZE);
        return userRepo.findByRoleAndEnabledOrderByUpdatedAtDesc(roleRepo.findByName(role), status, pageable);
    }

    @Override
    public Page<User> getUsersByPage(List<String> roles, int page) {
        Pageable pageable = PageRequest.of(page, Constants.DEFAULT_PAGE_SIZE);
        List<Role> rolesList = new ArrayList<>();
        for (String role : roles) {
            rolesList.add(roleRepo.findByName(role));
        }
        return userRepo.findByRoleInOrderByUpdatedAtDesc(rolesList, pageable);
    }


    @Override
    public Optional<User> findUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public void deleteUser(User user) {
        userRepo.delete(user);
    }
}
