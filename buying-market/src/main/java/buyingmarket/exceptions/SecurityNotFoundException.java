package buyingmarket.exceptions;

public class SecurityNotFoundException extends RuntimeException {
    public SecurityNotFoundException(String message) {
        super(message);
    }
}
