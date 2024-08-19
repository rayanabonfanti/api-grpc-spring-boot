package api.grpc.spring.boot.orchestrator.interceptor;

public class GrpcMethodInterceptor {

    private GrpcMethodInterceptor() {}

    static boolean verifyMethodSecurityInterceptorGrpc(String methodName) {
        return "api.grpc.spring.boot.orchestrator.UserService/RegisterUser".equals(methodName);
    }

}
