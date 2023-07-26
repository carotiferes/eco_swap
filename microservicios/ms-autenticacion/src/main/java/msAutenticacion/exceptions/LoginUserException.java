package msAutenticacion.exceptions;

public class LoginUserException extends RuntimeException {

    public LoginUserException(String errorMessage) {
        super(errorMessage);
    }
}
