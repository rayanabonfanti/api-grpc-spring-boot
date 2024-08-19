package api.grpc.spring.boot.user.interceptor;

import api.grpc.spring.boot.user.utils.ConstantsUtil;
import io.grpc.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public class GrpcSecurityInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String methodName = call.getMethodDescriptor().getFullMethodName();

        if (GrpcMethodInterceptor.verifyMethodSecurityInterceptorGrpc(methodName)) {
            return next.startCall(call, headers);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !hasRequiredRole(authentication)) {
            call.close(Status.PERMISSION_DENIED.withDescription("Permission Denied"), new Metadata());
            return new ServerCall.Listener<>() {};
        }

        return next.startCall(call, headers);
    }

    private boolean hasRequiredRole(Authentication authentication) {
        Collection<?> authorities = authentication.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority(ConstantsUtil.ROLE_ADMIN)) ||
                authorities.contains(new SimpleGrantedAuthority(ConstantsUtil.ROLE_USER));
    }

}
