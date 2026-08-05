package exceptions;

public class EntityNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 6352022910870118956L;

    private String msg;

    public EntityNotFoundException() {

    }

    public EntityNotFoundException(String message) {
        super(message);

        this.msg = message;
    }

    //
    public String getMessage() {
        return this.msg;
    }
}
