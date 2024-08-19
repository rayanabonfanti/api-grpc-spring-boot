package api.grpc.spring.boot.iam.services.implementations;

import api.grpc.spring.boot.iam.domain.orm.User;
import api.grpc.spring.boot.iam.utils.ConstantsUtil;
import api.grpc.spring.boot.iam.utils.RedisCache;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthUserCustomerServiceImpl implements UserDetailsService {

    private final RestTemplate restTemplate;
    private final RedisCache redisCache;

    public AuthUserCustomerServiceImpl(RestTemplate restTemplate, RedisCache redisCache) {
        this.restTemplate = restTemplate;
        this.redisCache = redisCache;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//        User cachedUser = redisCache.getCachedUser(login);
//        if (cachedUser != null) {
//            return cachedUser;
//        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(ConstantsUtil.URL_USER_CUSTOMER +
                        ConstantsUtil.PATH_USER_CUSTOMER)
                .queryParam("login", login);

        ResponseEntity<User> response = restTemplate.getForEntity(uriBuilder.toUriString(), User.class);
        User user = response.getBody();

        if (user == null) {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }

        //redisCache.cacheUser(login, user);
        return user;
    }

}