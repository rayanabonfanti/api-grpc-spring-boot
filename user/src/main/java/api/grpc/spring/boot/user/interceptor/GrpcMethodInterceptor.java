package api.grpc.spring.boot.user.interceptor;

public class GrpcMethodInterceptor {

    private GrpcMethodInterceptor() {}

    static boolean verifyMethodSecurityInterceptorGrpc(String methodName) {
        return "api.grpc.spring.boot.user.UserService/RegisterUser".equals(methodName);
    }

}
