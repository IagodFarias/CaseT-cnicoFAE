package exceptions;

public class NoCheckedException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 6352022980170118956L;

    private String msg;

    public NoCheckedException() {

    }

    public NoCheckedException(String message) {
        super(message);

        this.msg = message;
    }

    //
    public String getMessage() {
        return this.msg;
    }
}
