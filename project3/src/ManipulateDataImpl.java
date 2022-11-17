import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class ManipulateDataImpl
        extends java.rmi.server.UnicastRemoteObject
        implements ManipulateData {

    private KeyValue store;
    private static Logger logger = Logger.getLogger(ServerAppRMI.class.getName());
    private static Semaphore semaphore = new Semaphore(1);

    private ManipulateData coordinator;
    private boolean isCoord;
    private HashMap<String, ManipulateData> slaves;

    protected ManipulateDataImpl(int port, String coord_ip, int coord_port) throws RemoteException, MalformedURLException, NotBoundException {
        super();
        if (store == null) {
            store = new KeyValue();
        }
        if (coord_port > 0) {
            isCoord = false;
            coordinator = (ManipulateData) Naming.lookup(
                    "rmi://" + coord_ip + ":" + coord_port + "/StoreService");
        }
        else {
            isCoord = true;
            slaves = new HashMap<String, ManipulateData>();
        }
    }

    @Override
    public String put(String key, String value) throws RemoteException, InterruptedException {
        semaphore.acquire();
        String ret = store.put(key, value);
        logger.info("put " + key + " " + value + " performed on local machine");
        semaphore.release();
        return ret;
    }

    @Override
    public String get(String key) throws RemoteException, InterruptedException {
        semaphore.acquire();
        String ret = store.get(key);
        logger.info("get " + key + " performed");
        semaphore.release();
        return ret;
    }

    @Override
    public String delete(String key) throws RemoteException, InterruptedException {
        semaphore.acquire();
        String ret = store.delete(key);
        logger.info("delete " + key + " performed on local machine");
        semaphore.release();
        return ret;
    }

    @Override
    public void register(String ip, int port) throws MalformedURLException, NotBoundException, RemoteException, InterruptedException {
        semaphore.acquire();
        ManipulateData temp = (ManipulateData) Naming.lookup(
                "rmi://" + ip + ":" + port + "/StoreService");
        slaves.put(ip +":" + port, temp);
        logger.info("Replica server: " + ip + ":" + port + " registered in this coordinator server");
        semaphore.release();
    }

    @Override
    public String commit(Action action, String key, String value) throws InterruptedException, RemoteException, MalformedURLException, NotBoundException {
        semaphore.acquire();
        if (isCoord) {
            logger.info("Receive update request from replica server!");
            if (action == Action.DELETE) {
                store.delete(key);
                logger.info("delete " + key + " performed on local machine");

                slaves.forEach(
                        (k, v)
                            -> {
                            try {
                                v.delete(key);
                                logger.info("Delete performed on replica server: " + k);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });

            } else if (action == Action.PUT) {
                store.put(key, value);
                logger.info("put " + key + " " + value + " performed on local machine");
                slaves.forEach(
                        (k, v)
                                -> {
                            try {
                                v.put(key, value);
                                logger.info("Put performed on replica server: " + k);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }
        else {
            semaphore.release();
            logger.info("Commit to coordinator!");
            String ret = coordinator.commit(action, key, value);
            logger.info("Commit finished!");
            semaphore.acquire();
        }
        semaphore.release();
        return "Commit Success";
    }
}
