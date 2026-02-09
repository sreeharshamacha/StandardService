package com.standard.service.config;

import com.standard.service.constant.AppConstants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of AuditorAware to provide the user name from JWT
 * preferred_username claim.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(AppConstants.SYSTEM_USER);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            String username = jwt.getClaim(AppConstants.JWT_PREFERRED_USERNAME);
            return Optional.ofNullable(username);
        }

        return Optional.of(authentication.getName());
    }
}
