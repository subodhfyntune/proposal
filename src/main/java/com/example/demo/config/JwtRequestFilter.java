package com.example.demo.config;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private static final List<String> EXCLUDED_PATHS = List.of(
    	    "/v3/api-docs",
    	    "/v3/api-docs/",
    	    "/v3/api-docs/",
    	    "/v3/api-docs/swagger-config",
    	    "/swagger-ui",
    	    "/swagger-ui/",
    	    "/swagger-ui.html",
    	    "/swagger-ui/index.html",
    	    "/swagger-ui/**",
    	    "/swagger-resources/**",
    	    "/webjars/**",
    	    "/users/login",
    	    "/users/register"
    	);


    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {



        final String authorizationHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();

        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response); // Skip filtering
            return;
        }
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Bypassing JWT Filter for: " + path);


        String username = null;
        String jwt = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);

                username = jwtUtil.extractUsername(jwt);
                String email = jwtUtil.extractEmail(jwt);
                Long userId = jwtUtil.extractUserId(jwt);
                String role = jwtUtil.extractRole(jwt);
                
                System.err.println(username);
                System.err.println(email);

                System.err.println(userId);

                System.err.println("aaaa" +jwtUtil.validateToken(jwt, username, userId,email,role));

                
                

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwt, username, userId,email,role)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("Invalid or expired JWT token");
            return;
        }                                    

        chain.doFilter(request, response);
    }
}
