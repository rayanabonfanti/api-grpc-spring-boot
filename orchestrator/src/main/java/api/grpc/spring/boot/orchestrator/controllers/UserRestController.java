package api.grpc.spring.boot.orchestrator.controllers;

import api.grpc.spring.boot.orchestrator.domain.orm.User;
import api.grpc.spring.boot.orchestrator.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

}
