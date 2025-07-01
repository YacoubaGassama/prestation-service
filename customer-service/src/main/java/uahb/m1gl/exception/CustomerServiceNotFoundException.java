package uahb.m1gl.exception;

public class CustomerServiceNotFoundException extends RuntimeException{
    public CustomerServiceNotFoundException(String message) {
        super(message);
    }
}
