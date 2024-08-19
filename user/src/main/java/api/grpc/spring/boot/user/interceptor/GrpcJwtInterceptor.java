package api.grpc.spring.boot.user.interceptor;

import api.grpc.spring.boot.user.config.SecurityProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.grpc.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GrpcJwtInterceptor implements ServerInterceptor {

    private final SecurityProperties securityProperties;

    public GrpcJwtInterceptor(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();

        if (GrpcMethodInterceptor.verifyMethodSecurityInterceptorGrpc(methodName)) {
            return next.startCall(call, headers);
        }

        String token = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecret());
                DecodedJWT jwt = JWT.require(algorithm).build().verify(token);

                String username = jwt.getSubject();
                List<String> roles = jwt.getClaim("roles").asList(String.class);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                call.close(Status.UNAUTHENTICATED.withDescription("Invalid token"), new Metadata());
                return new ServerCall.Listener<>() {};
            }
        } else {
            call.close(Status.UNAUTHENTICATED.withDescription("Token not found"), new Metadata());
            return new ServerCall.Listener<>() {};
        }

        return next.startCall(call, headers);
    }

}

