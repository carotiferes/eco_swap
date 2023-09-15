package msAutenticacion.exceptions;

public class UserDuplicatedException extends RuntimeException {
    public UserDuplicatedException(String errorMessage) {super(errorMessage);}
}
