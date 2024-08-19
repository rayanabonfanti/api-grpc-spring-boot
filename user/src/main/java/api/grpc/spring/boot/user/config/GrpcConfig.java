package api.grpc.spring.boot.user.config;

import api.grpc.spring.boot.user.interceptor.GrpcJwtInterceptor;
import api.grpc.spring.boot.user.interceptor.GrpcSecurityInterceptor;
import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    private final SecurityProperties securityProperties;

    public GrpcConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public GlobalServerInterceptorConfigurer globalInterceptorConfigurerAdapter() {
        return registry -> {
            registry.add(new GrpcJwtInterceptor(securityProperties));
            registry.add(new GrpcSecurityInterceptor());
        };
    }
}
