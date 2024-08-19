package api.grpc.spring.boot.user.services.implementations;

import api.grpc.spring.boot.user.exceptions.UserAlreadyExistsException;
import api.grpc.spring.boot.user.repositories.UserRepository;
import api.grpc.spring.boot.user.services.UserService;
import api.grpc.spring.boot.user.domain.dto.RegisterUserDto;
import api.grpc.spring.boot.user.domain.orm.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.findByLoginAndEmail(registerUserDto.login(), registerUserDto.email()).isPresent()) {
            throw new UserAlreadyExistsException("User customer already exists");
        }

        User newUser = new User(
                UUID.randomUUID().toString(),
                registerUserDto.name(),
                registerUserDto.email(),
                registerUserDto.login(),
                bCryptPasswordEncoder.encode(registerUserDto.password()),
                registerUserDto.role()
        );

        userRepository.save(newUser);
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

}
