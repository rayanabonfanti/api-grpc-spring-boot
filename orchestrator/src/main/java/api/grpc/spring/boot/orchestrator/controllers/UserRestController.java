package api.grpc.spring.boot.orchestrator.controllers;

import api.grpc.spring.boot.orchestrator.domain.orm.UploadImageUserResponseDTO;
import api.grpc.spring.boot.orchestrator.domain.orm.User;
import api.grpc.spring.boot.orchestrator.service.UserServiceImpl;
import api.grpc.spring.boot.user.UploadImageUserRequest;
import api.grpc.spring.boot.user.UploadImageUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/rest/v1/orchestrator")
public class UserRestController {

    private final UserServiceImpl userServiceImpl;

    public UserRestController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/get-login")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<User> getLogin(@RequestParam String login) {
        User user = userServiceImpl.getUserByLogin(login);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/upload-image-user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<UploadImageUserResponseDTO> uploadImageUser(@RequestParam String message, @RequestParam MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();

        UploadImageUserRequest request = UploadImageUserRequest.newBuilder()
                .setMessage(message)
                .setFile(com.google.protobuf.ByteString.copyFrom(fileBytes))
                .build();

        UploadImageUserResponse grpcResponse = userServiceImpl.uploadImageUser(request);

        UploadImageUserResponseDTO responseDTO = new UploadImageUserResponseDTO(
                grpcResponse.getStatus(),
                grpcResponse.getDetails(),
                grpcResponse.getFile().toByteArray()
        );

        return ResponseEntity.ok(responseDTO);
    }

}
