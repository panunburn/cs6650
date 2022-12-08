

import java.io.IOException;
import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

/*
 * A Key value store. It has a hashmap inside and make sure change is atomic.
 */
public class KeyValue {

    private HashMap<String, String> store;

    /**
     * Constructor of kv store.
     */
    public KeyValue() {
        this.store = new HashMap<>();
    }
    private Logger logger = Logger.getLogger(KeyValue.class.getName());
    static Semaphore semaphore = new Semaphore(1);

    /**
     * Put in the store.
     * @param key the key.
     * @param value the value.
     * @throws InterruptedException throws exception when interrupts.
     */
    public String put(String key, String value) throws InterruptedException {
        logger.info("Acquiring lock...");
        semaphore.acquire();
        logger.info("Got permit!");
        String ret = store.put(key, value);

        semaphore.release();
        logger.info("Lock released!");
        return ret;
    }

    /**
     * Get from the store.
     * @param key the key.
     */
    public String get(String key) {
        return store.get(key);
    }

    /**
     * Delete in the store.
     * @param key the key.
     * @throws InterruptedException throws exception when interrupts.
     */
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
