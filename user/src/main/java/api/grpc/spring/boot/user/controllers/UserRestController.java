package api.grpc.spring.boot.user.controllers;

import api.grpc.spring.boot.user.domain.dto.RegisterUserDto;
import api.grpc.spring.boot.user.domain.orm.User;
import api.grpc.spring.boot.user.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rest/v1/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserDto registerUserDto) {
        userService.registerUser(registerUserDto);
        return ResponseEntity.ok("User saved successfully!");
    }

    @GetMapping("/get-login")
    public ResponseEntity<User> getLogin(@RequestParam String login) {
        User user = userService.getUserByLogin(login);
        return ResponseEntity.ok(user);
    }

}
