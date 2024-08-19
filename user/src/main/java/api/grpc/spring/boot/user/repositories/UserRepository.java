package api.grpc.spring.boot.user.repositories;

import api.grpc.spring.boot.user.domain.orm.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndEmail(String login, String email);
}
