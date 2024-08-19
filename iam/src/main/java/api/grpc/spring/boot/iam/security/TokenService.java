package api.grpc.spring.boot.iam.security;

import api.grpc.spring.boot.iam.domain.orm.UserRole;
import api.grpc.spring.boot.iam.exceptions.AuthenticationIamException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import api.grpc.spring.boot.iam.domain.orm.User;
import api.grpc.spring.boot.iam.utils.ConstantsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ConstantsUtil.AUTH_ISSUE)
                    .withSubject(user.login())
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .withClaim("role", user.role().toString())
                    .withExpiresAt(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new AuthenticationIamException("Error while generating token: " + exception.getMessage());
        }
    }

    public User validateTokenAndRetrieveUser(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            var decodedJWT = JWT.require(algorithm)
                    .withIssuer(ConstantsUtil.AUTH_ISSUE)
                    .acceptExpiresAt(5 * 60)
                    .build()
                    .verify(token);
            String username = decodedJWT.getSubject();
            UserRole role = UserRole.valueOf(decodedJWT.getClaim("role").asString());

            return new User(null, null, null, username, null, role);
        } catch (JWTVerificationException exception) {
            throw new AuthenticationIamException("Error to check token: " + exception.getMessage());
        }
    }
}
