package pl.ftims.ias.your_climbing_gym.exceptions;


public class DatabaseErrorAppException extends AbstractAppException {

    static final String DB_CONNECTION_ERROR = "Unable to connect with Database";

    private DatabaseErrorAppException(String message, Throwable cause) {
        super(message, cause);
    }

    private DatabaseErrorAppException(String message) {
        super(message);
    }


    public static DatabaseErrorAppException createDatabaseAppException(Throwable ex) {
        return new DatabaseErrorAppException(DB_CONNECTION_ERROR, ex);
    }

    public static DatabaseErrorAppException createDatabaseAppException() {
        return new DatabaseErrorAppException(DB_CONNECTION_ERROR);
    }
}
