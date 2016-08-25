package com.gmail.bogatyr.alexander.intech.security;

import com.gmail.bogatyr.alexander.intech.constant.App;
import com.gmail.bogatyr.alexander.intech.util.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        return (currentUserLogin != null ? currentUserLogin : App.SYSTEM_ACCOUNT);
    }
}
