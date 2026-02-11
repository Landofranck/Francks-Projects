package project.adapter.out.persistence;

import java.util.InputMismatchException;

public class NotFoundException extends InputMismatchException {
    public NotFoundException(String message) {
        super(message);
    }
}
