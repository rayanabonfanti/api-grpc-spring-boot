package api.grpc.spring.boot.user.exceptions;

import org.apache.coyote.BadRequestException;

public class UserCustomerNotFoundException extends BadRequestException {
    public UserCustomerNotFoundException(String message){
        super(message);
    }
}
