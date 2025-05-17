package uz.alifservice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.alifservice.config.security.CustomUserDetails;
import uz.alifservice.domain.auth.Role;
import uz.alifservice.domain.auth.User;

public class SpringSecurityUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUser();
        }
        return null;
    }

    public static boolean hasRole(Role requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(sga -> sga.getAuthority().equals(requiredRole.getRoleName()));
    }
}
