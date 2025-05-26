package com.example.demo.config;



import com.example.demo.entity.SessionEntity;
import com.example.demo.entity.Users;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Component
public class SessionManager extends OncePerRequestFilter {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionManager(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.equals("/users/login")
                || path.equals("/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String sessionId = request.getHeader("SessionId");

        try {
            if (sessionId == null || sessionId.isEmpty()) {
                throw new IllegalArgumentException("SessionId header is missing");
            }

            Optional<SessionEntity> sessionOpt = sessionRepository.findBySessionId(sessionId);
            if (sessionOpt.isEmpty()) {
                throw new IllegalArgumentException("Invalid session ID");
            }

            SessionEntity session = sessionOpt.get();
            if (session.getExpiredAt() == null || session.getExpiredAt().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Session has expired");
            }

            Optional<Users> userOpt = userRepository.findById(session.getUserId());
            if (userOpt.isEmpty()) {
                throw new IllegalArgumentException("User not found for session");
            }

            Users user = userOpt.get();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    new User(user.getUsername(), user.getPassword(), Collections.emptyList()),
                    null,
                    Collections.emptyList()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (IllegalArgumentException ex) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied: " + ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

}

