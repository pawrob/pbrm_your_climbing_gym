package pl.ftims.ias.your_climbing_gym.utils.loggers;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class TransactionLogger {
    @Getter
    private final Logger logger;
    private String prefix = "";

    private String prepareLog(String log) {
        return "[" + prefix + "] " + log;
    }

    public TransactionLogger() {
        prefix = UUID.randomUUID().toString();
        logger = LogManager.getLogger(this.getClass());
    }

    public void logTransactionBegin() {
        logger.info(prepareLog("transaction begin"));
    }

    /**
     * Metoda dodająca do wpisu informacje o zakonczonej transakcji.
     *
     * @param committed informacja o tym czy transakcja została zatwierdzona
     */
    public void logTransactionEnd(boolean committed) {
        String log = "transaction end with " + (committed ? "commit" : "rollback");
        logger.info(prepareLog(log));
    }

    public void logTransactionError() {
        String log = "Error during transaction, set to rollback";
        logger.info(prepareLog(log));
    }
}
