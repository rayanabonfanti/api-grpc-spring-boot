package api.grpc.spring.boot.iam.domain.dto;

public record AuthenticationDto(String login, String password) {
}
