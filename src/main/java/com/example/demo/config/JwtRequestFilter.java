//package com.example.demo.config;
//
//
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.io.IOException;
//
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//
//
//    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//    	String path = request.getRequestURI();
//        String method = request.getMethod();
//
//       
//        if ("OPTIONS".equalsIgnoreCase(method)) {
//            chain.doFilter(request, response);
//            return;
//        }
//       
//        if (path.startsWith("/swagger-ui") ||
//                path.startsWith("/v3/api-docs") ||
//                path.startsWith("/swagger-resources") ||
//                path.startsWith("/webjars") ||
//                path.contains("favicon.ico")) {
//                chain.doFilter(request, response);
//                return;
//            }
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwt = null;
//
//        try {
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                jwt = authorizationHeader.substring(7);
//
//                username = jwtUtil.extractUsername(jwt);
//                String email = jwtUtil.extractEmail(jwt);
//                Long userId = jwtUtil.extractUserId(jwt);
//                String role = jwtUtil.extractRole(jwt);
//                
//                System.err.println("username ####>> "+username);
//                
//                System.err.println("userId ####>> "+userId);
//                System.err.println("email $$$$ >> "+email);
//                System.err.println("role ####>> "+role);
//                
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                    if (jwtUtil.validateToken(jwt, username, userId,email,role)) {
//                        UsernamePasswordAuthenticationToken authToken =
//                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.getWriter().write("Invalid or expired JWT token");
//            return;
//        }                                    
//
//        chain.doFilter(request, response);
//    }
//}