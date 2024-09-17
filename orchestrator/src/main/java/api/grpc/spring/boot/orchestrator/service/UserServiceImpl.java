package api.grpc.spring.boot.orchestrator.service;

import api.grpc.spring.boot.orchestrator.domain.enumaration.UserRole;
import api.grpc.spring.boot.orchestrator.domain.orm.User;
import api.grpc.spring.boot.orchestrator.exceptions.UserNotFoundException;
import api.grpc.spring.boot.orchestrator.interceptor.AuthorizationInterceptor;
import api.grpc.spring.boot.user.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Service
public class UserServiceImpl {

    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public UserServiceImpl() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        this.blockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public User getUserByLogin(String login) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            AuthorizationInterceptor interceptor = new AuthorizationInterceptor(token);
            blockingStub = blockingStub.withInterceptors(interceptor);
        }

        try {
            GetUserByLoginRequest grpcRequest = GetUserByLoginRequest.newBuilder()
                    .setLogin(login)
                    .build();

            UserResponse userResponse = blockingStub.getUserByLogin(grpcRequest);

            return new User(userResponse.getId(),
                    userResponse.getName(),
                    userResponse.getEmail(),
                    userResponse.getLogin(),
                    userResponse.getPassword(),
                    UserRole.valueOf(userResponse.getRole()));
            //TODO: receber exceptions do outro microsservico grpc
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.NOT_FOUND.getCode()) {
                throw new UserNotFoundException("User with login " + login + " not found.");
            } else {
                throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
            }
        }
    }

    public UploadImageUserResponse uploadImageUser(UploadImageUserRequest uploadImageUserRequest) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            AuthorizationInterceptor interceptor = new AuthorizationInterceptor(token);
            blockingStub = blockingStub.withInterceptors(interceptor);
        }

        try {
            return blockingStub.uploadFileAndString(uploadImageUserRequest);
            //TODO: receber exceptions do outro microsservico grpc
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.NOT_FOUND.getCode()) {
                throw new UserNotFoundException("User with login not found.");
            } else {
                throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
            }
        }
    }

}
