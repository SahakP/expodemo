package com.expo.demo.util;

import com.expo.demo.constants.Diciton;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

public class XUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {


    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) {
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    public String determineTargetUrl(Authentication authentication) {
        boolean isAdmin = false;
        boolean isMember = false;
        boolean isModerator = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority == null || grantedAuthority.getAuthority() == null) {
                throw new IllegalStateException("No such role!");
            }
            if (grantedAuthority.getAuthority().equals(Diciton.ROLE_ADMIN)) {
                isAdmin = true;
                break;
            } else if (grantedAuthority.getAuthority().equals(Diciton.ROLE_MEMBER)) {
                isMember = true;
                break;
            } else if (grantedAuthority.getAuthority().equals(Diciton.ROLE_MODERATOR)) {
                isModerator = true;
                break;
            }
        }

        if (isAdmin) {
            return "/admin/users";
        } else if (isMember) {
              return "/member/welcome";
        }else if (isModerator) {
            return "/admin/users";
        }else {
            throw new IllegalStateException();
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}