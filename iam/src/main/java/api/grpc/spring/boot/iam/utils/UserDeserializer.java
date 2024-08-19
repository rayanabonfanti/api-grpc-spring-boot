package api.grpc.spring.boot.iam.utils;

import api.grpc.spring.boot.iam.domain.orm.User;
import api.grpc.spring.boot.iam.domain.orm.UserRole;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {
    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String id = node.has("id") ? node.get("id").asText() : null;
        String name = node.has("name") ? node.get("name").asText() : null;
        String email = node.has("email") ? node.get("email").asText() : null;
        String login = node.has("login") ? node.get("login").asText() : null;
        String password = node.has("password") ? node.get("password").asText() : null;
        UserRole role = node.has("role") ? UserRole.valueOf(node.get("role").asText()) : null;

        return new User(id, name, email, login, password, role);
    }
}
