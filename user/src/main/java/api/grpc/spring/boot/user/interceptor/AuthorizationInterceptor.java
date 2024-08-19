package api.grpc.spring.boot.user.interceptor;

import io.grpc.*;

public class AuthorizationInterceptor implements ClientInterceptor {

    private final String token;

    public AuthorizationInterceptor(String token) {
        this.token = token;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + token);
                super.start(responseListener, headers);
            }
        };
    }
}

