package api.grpc.spring.boot.user.controllers;

import api.grpc.spring.boot.user.*;
import api.grpc.spring.boot.user.domain.dto.RegisterUserDto;
import api.grpc.spring.boot.user.domain.enumaration.UserRole;
import api.grpc.spring.boot.user.exceptions.UserNotFoundException;
import api.grpc.spring.boot.user.services.UserService;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import api.grpc.spring.boot.user.domain.orm.User;
import org.springframework.security.access.prepost.PreAuthorize;

@GrpcService
public class UserGrpcController extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    public UserGrpcController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void uploadFileAndString(UploadImageUserRequest request, StreamObserver<UploadImageUserResponse> responseObserver) {
        String message = request.getMessage();
        byte[] fileBytes = request.getFile().toByteArray();

        userService.uploadImageUser(fileBytes);

        responseObserver.onNext(UploadImageUserResponse.newBuilder().setStatus("1").setDetails(message).setFile(ByteString.copyFrom(fileBytes)).build());
        responseObserver.onCompleted();
    }

    @Override
    public void registerUser(RegisterUserRequest request, StreamObserver<RegisterUserResponse> responseObserver) {
        RegisterUserDto registerUserDto = new RegisterUserDto(
                request.getName(),
                request.getEmail(),
                request.getLogin(),
                request.getPassword(),
                UserRole.valueOf(request.getRole().toUpperCase())
        );

        userService.registerUser(registerUserDto);

        RegisterUserResponse response = RegisterUserResponse.newBuilder()
                .setMessage("User registered successfully!")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void getUserByLogin(GetUserByLoginRequest request, StreamObserver<UserResponse> responseObserver) {
        String login = request.getLogin();
        try {
            User user = userService.getUserByLogin(login);

            if (user == null) {
                throw new UserNotFoundException("User with login " + login + " not found.");
            }

            UserResponse response = UserResponse.newBuilder()
                    .setId(user.id())
                    .setName(user.name())
                    .setEmail(user.email())
                    .setLogin(user.login())
                    .setPassword(user.password())
                    .setRole(user.role().toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            //TODO: propagar exceptions para o outro microsservico e gerar interceptor para exceptions globais
        } catch (UserNotFoundException e) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .augmentDescription("User does not exist in the system")
                    .withCause(e)
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.UNKNOWN
                    .withDescription("An unexpected error occurred")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

}
