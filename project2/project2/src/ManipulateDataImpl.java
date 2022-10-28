import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

public class ManipulateDataImpl
        extends java.rmi.server.UnicastRemoteObject
        implements ManipulateData {

    private KeyValue store;
    private static Semaphore semaphore = new Semaphore(1);
    protected ManipulateDataImpl() throws RemoteException {
        super();
        if (store == null) {
            store = new KeyValue();
        }
    }

    @Override
    public String put(String key, String value) throws RemoteException, InterruptedException {
        semaphore.acquire();
        String ret = store.put(key, value);
        semaphore.release();
        return ret;
    }

    @Override
    public String get(String key) throws RemoteException, InterruptedException {
        semaphore.acquire();
        String ret = store.get(key);
        semaphore.release();
        return ret;
    }

    @Override
    public String delete(String key) throws RemoteException, InterruptedException {
        semaphore.acquire();
        String ret = store.delete(key);
        semaphore.release();
        return ret;
    }
}
