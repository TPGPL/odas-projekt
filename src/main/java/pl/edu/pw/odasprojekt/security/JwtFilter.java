package pl.edu.pw.odasprojekt.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.pw.odasprojekt.services.JwtService;

import java.io.IOException;
import java.util.Arrays;

public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        var jwt = getJwtFromRequest(request);

        if (jwt != null && jwtService.validateJwt(jwt)) {
            var clientNumber = jwtService.getClientNumberFromJwt(jwt);
            var userDetails = userDetailsService.loadUserByUsername(clientNumber);
            var authToken = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        var jwtCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwtToken")).findFirst();

        return jwtCookie.map(Cookie::getValue).orElse(null);
    }
}
