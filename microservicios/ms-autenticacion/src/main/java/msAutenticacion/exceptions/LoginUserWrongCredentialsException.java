package msAutenticacion.exceptions;

public class LoginUserWrongCredentialsException extends RuntimeException{
    public LoginUserWrongCredentialsException(String errorMessage) {
        super(errorMessage);
    }
}
