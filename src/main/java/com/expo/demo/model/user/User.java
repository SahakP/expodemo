package com.expo.demo.model.user;

import com.expo.demo.constants.Diciton;
import com.expo.demo.model.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = Diciton.TABLE_USER, schema = Diciton.SCHEMA)
public class User extends AuditModel {
    private Long id;
    private String username;
    private String password;
    private String oldPassword;
    private String newPassword;
    private String passwordConfirm;
    private String selectedRoleName;
    private boolean enabled;
    private Date lastLogin;
    private Role role;
    private UserInfo userInfo;
    private Boolean notValidatePassword;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Transient
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Transient
    public String getSelectedRoleName() {
        return selectedRoleName;
    }

    public void setSelectedRoleName(String selectedRoleName) {
        this.selectedRoleName = selectedRoleName;
    }

    @Transient
    public Boolean getNotValidatePassword() {
        return notValidatePassword;
    }

    public void setNotValidatePassword(Boolean notValidatePassword) {
        this.notValidatePassword = notValidatePassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_info", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }


    @Override
    public String toString() {
        String res = "";
        if (userInfo == null) {
            return res;
        }
        if (userInfo.getFirstName() != null) {
            res += userInfo.getFirstName() + " ";
        }

        if (userInfo.getLastName() != null) {
            res += userInfo.getLastName() + " ";
        }

        if (userInfo.getEmail() != null) {
            res += userInfo.getEmail();
        }

        return res;
    }


}
