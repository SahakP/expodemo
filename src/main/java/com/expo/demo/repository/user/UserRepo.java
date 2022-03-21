package com.expo.demo.repository.user;

import com.expo.demo.model.user.Role;
import com.expo.demo.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByRole(Role role);
    Page<User> findByRoleOrderByUpdatedAtDesc(Role role, Pageable pageable);
    Page<User> findByRoleAndEnabledOrderByUpdatedAtDesc(Role role, boolean enabled, Pageable pageable);
    Page<User> findByRoleInOrderByUpdatedAtDesc(List<Role> roles, Pageable pageable);

    List<User> findByRoleIn(List<Role> roles);

    @Query("SELECT u FROM User u INNER JOIN UserInfo ui on u.userInfo = ui.id WHERE u.enabled = TRUE AND " + "(" + "lower(ui.firstName) LIKE %:keyword% OR lower(ui.lastName) LIKE %:keyword% OR lower(ui.patternName) LIKE %:keyword%" + ")")
    List<User> search(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u INNER JOIN UserInfo ui on u.userInfo = ui.id WHERE u.enabled = FALSE AND " + "(" + "lower(ui.firstName) LIKE %:keyword% OR lower(ui.lastName) LIKE %:keyword% OR lower(ui.patternName) LIKE %:keyword%" + ")")
    List<User> searchDisabled(@Param("keyword") String keyword);
}
