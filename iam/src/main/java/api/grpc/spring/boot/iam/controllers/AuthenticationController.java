package api.grpc.spring.boot.iam.controllers;

import api.grpc.spring.boot.iam.domain.dto.AuthenticationDto;
import api.grpc.spring.boot.iam.domain.dto.LoginResponseDto;
import api.grpc.spring.boot.iam.services.implementations.AuthorizationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authorization")
public class AuthenticationController {

    private final AuthorizationServiceImpl authorizationService;

    public AuthenticationController(AuthorizationServiceImpl authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody AuthenticationDto authenticationDto) {
        LoginResponseDto response = authorizationService.authenticateAndGenerateToken(authenticationDto);
        return ResponseEntity.ok(response);
    }

}
