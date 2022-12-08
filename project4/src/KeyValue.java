

import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class KeyValue {

    private HashMap<String, String> store;

    public KeyValue() {
        this.store = new HashMap<>();
    }
    private Logger logger = Logger.getLogger(KeyValue.class.getName());
    static Semaphore semaphore = new Semaphore(1);

    public String put(String key, String value) throws InterruptedException {
        logger.info("Acquiring lock...");
        semaphore.acquire();
        logger.info("Got permit!");
        String ret = store.put(key, value);

        semaphore.release();
        logger.info("Lock released!");
        return ret;
    }

    public String get(String key) {
        return store.get(key);
    }

    public String delete(String key) throws InterruptedException {
        logger.info("Acquiring lock...");
        semaphore.acquire();
        logger.info("Got permit!");
        String ret = store.remove(key);
        semaphore.release();
        logger.info("Lock released!");
        return ret;
    }
}
