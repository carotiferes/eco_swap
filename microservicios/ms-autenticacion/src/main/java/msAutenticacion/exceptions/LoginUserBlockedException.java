package msAutenticacion.exceptions;

public class LoginUserBlockedException extends RuntimeException {
    public LoginUserBlockedException(String errorMessage) {
        super(errorMessage);
    }
}
