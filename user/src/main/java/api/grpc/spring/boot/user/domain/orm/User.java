package api.grpc.spring.boot.user.domain.orm;

import api.grpc.spring.boot.user.domain.enumaration.UserRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public record User(
        @Id String id,
        String name,
        String email,
        String login,
        String password,
        UserRole role,
        byte[] image
) {}
