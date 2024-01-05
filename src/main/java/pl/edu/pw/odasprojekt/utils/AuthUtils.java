package pl.edu.pw.odasprojekt.utils;

import org.springframework.security.core.Authentication;

public class AuthUtils {
    public static boolean isAuthorized(Authentication auth) {
        for (var aut : auth.getAuthorities()) {
            if (aut.getAuthority().equals("ROLE_USER")) {
                return true;
            }
        }

        return false;
    }
}
