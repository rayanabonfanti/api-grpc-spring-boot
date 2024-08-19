package api.grpc.spring.boot.orchestrator.security;

import api.grpc.spring.boot.orchestrator.utils.ConstantsUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final String secret;

    public TokenFilter(String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Algorithm algorithm = Algorithm.HMAC256(secret);
                DecodedJWT jwt = JWT.require(algorithm)
                        .withIssuer(ConstantsUtil.AUTH_ISSUE)
                        .build()
                        .verify(token);

                String username = jwt.getSubject();
                List<String> roles = jwt.getClaim("roles").asList(String.class);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }
        filterChain.doFilter(request, response);
    }

}

