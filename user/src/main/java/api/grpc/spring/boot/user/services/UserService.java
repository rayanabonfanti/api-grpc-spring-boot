package api.grpc.spring.boot.user.services;

import api.grpc.spring.boot.user.domain.dto.RegisterUserDto;
import api.grpc.spring.boot.user.domain.orm.User;

public interface UserService {
    void registerUser(RegisterUserDto registerUserDto);
    void uploadImageUser(byte[] image);
    User getUserByLogin(String login);
}
