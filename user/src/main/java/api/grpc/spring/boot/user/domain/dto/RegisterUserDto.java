package api.grpc.spring.boot.user.domain.dto;

import api.grpc.spring.boot.user.domain.enumaration.UserRole;

public record RegisterUserDto(String name,
                              String email,
                              String login,
                              String password,
                              UserRole role) {
}
