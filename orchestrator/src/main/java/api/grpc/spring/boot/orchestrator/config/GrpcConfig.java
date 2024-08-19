package api.grpc.spring.boot.orchestrator.config;

import api.grpc.spring.boot.orchestrator.interceptor.GrpcJwtInterceptor;
import api.grpc.spring.boot.orchestrator.interceptor.GrpcSecurityInterceptor;
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
