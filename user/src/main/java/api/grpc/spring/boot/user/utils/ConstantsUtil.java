package api.grpc.spring.boot.user.utils;

public class ConstantsUtil {

    private ConstantsUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String AUTH_ISSUE = "auth-api";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
}
