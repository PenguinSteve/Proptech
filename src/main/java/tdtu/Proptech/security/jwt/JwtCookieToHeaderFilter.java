package tdtu.Proptech.security.jwt;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtCookieToHeaderFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "token";  

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null && !token.isEmpty()) {
                    	 HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
                             @Override
                             public String getHeader(String name) {
                                 if ("Authorization".equalsIgnoreCase(name)) {
                                     return "Bearer " + token;  
                                 }
                                 return super.getHeader(name);
                             }
                         };
                         filterChain.doFilter(requestWrapper, response);
                         return;
                    }
                    break;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
