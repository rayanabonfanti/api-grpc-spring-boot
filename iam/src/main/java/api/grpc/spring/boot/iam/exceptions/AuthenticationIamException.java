package api.grpc.spring.boot.iam.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationIamException extends AuthenticationException {
    public AuthenticationIamException(String msg) {
        super(msg);
    }
}
