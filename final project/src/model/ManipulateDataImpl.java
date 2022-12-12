package model;

import controller.ServerAppRMI;
import view.serverView;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ManipulateDataImpl
        extends java.rmi.server.UnicastRemoteObject
        implements ManipulateData {

    public KeyValue store;
    private static Logger logger = Logger.getLogger(ServerAppRMI.class.getName());
    private static Semaphore semaphore = new Semaphore(1);

    private ManipulateData coordinator;
    private String coord_ip;
    private int coord_port;
    private boolean isCoord;
    public HashMap<String, ManipulateData> slaves;

    private serverView view;

    private int myport;

    public ManipulateDataImpl(int port, String coord_ip, int coord_port, serverView view) throws RemoteException, MalformedURLException, NotBoundException {
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

        }
        slaves = new HashMap<String, ManipulateData>();
        this.view = view;
        this.coord_ip = coord_ip;
        this.coord_port = coord_port;
        this.myport = port;
    }

    @Override
    public String put(String key, String value) throws RemoteException, InterruptedException {
        semaphore.acquire();
        String ret = store.put(key, value);
        logger.info("put " + key + " " + value + " performed on local machine");
        semaphore.release();
        if (view != null){
            view.displayServer(this);
        }
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
        if (view != null){
            view.displayServer(this);
        }
        return ret;
    }

    @Override
    public void register(String ip, int port) throws MalformedURLException, NotBoundException, RemoteException, InterruptedException {
        //semaphore.acquire();
        System.out.println("enter Registry!");
        ManipulateData temp = (ManipulateData) Naming.lookup(
                "rmi://" + ip + ":" + port + "/StoreService");
        slaves.put(ip +":" + port, temp);
        logger.info("Replica server: " + ip + ":" + port + " registered in this coordinator server");
        //semaphore.release();
        System.out.println("leave Registry!");
    }

    @Override
    public String commit(Action action, String key, String value) throws InterruptedException, RemoteException, MalformedURLException, NotBoundException {
        semaphore.acquire();
        String reply = "";
        if (isCoord) {
            logger.info("Receive update request from replica server!");
            if (action == Action.DELETE) {
                store.delete(key);
                logger.info("delete " + key + " performed on local machine");
                if (view != null){
                    view.displayServer(this);
                }

                slaves.forEach(
                        (k, v)
                            -> {
                            try {
                                String s = CompletableFuture.supplyAsync(() -> {
                                            try {
                                                String ret = v.delete(key);
                                                logger.info("Delete performed on replica server: " + k);
                                                return ret;
                                            } catch (InterruptedException | RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
                                        })
                                        .get(1, TimeUnit.SECONDS);
                            } catch (TimeoutException e) {
                                System.out.println("Time out has occurred");
                            } catch (InterruptedException | ExecutionException e) {
                                // Handle
                            }
                        });

            } else if (action == Action.PUT) {
                store.put(key, value);
                logger.info("put " + key + " " + value + " performed on local machine");
                if (view != null){
                    view.displayServer(this);
                }
                if (slaves != null) {
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
        }
        else {
            semaphore.release();
            logger.info("Commit to coordinator!");
            try {
                String s = CompletableFuture.supplyAsync(() -> {
                            try {
                                coordinator.commit(action, key, value);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            } catch (NotBoundException e) {
                                throw new RuntimeException(e);
                            }
                            logger.info("Commit success");
                                return "Commit Success";
                        })
                        .get(1, TimeUnit.SECONDS);
            }
             catch (Exception e) {
                // Handle
                System.out.println("Time out has occurred");
                boolean find = false;
                int port = coord_port + 1;
                for (port = coord_port + 1; port < myport; port++) {
                    System.out.println("Try port" + port);
                    try {
                        coordinator = (ManipulateData) Naming.lookup(
                                "rmi://" + coord_ip + ":" + port + "/StoreService");
                        System.out.println("Connect to new coord!");
                        find = true;
                        coord_port = port;
                        break;
                    }
                    catch (Exception thisE) {
                        System.out.println("Keep finding!");
                        continue;
                    }
                }
                if (find) {
                    System.out.println("find!");
                    coordinator = (ManipulateData) Naming.lookup(
                            "rmi://" + coord_ip + ":" + port + "/StoreService");
                    System.out.println("Connected");
                    coordinator.register(coord_ip, myport);
                    System.out.println("Finished register!");
                    reply = "Time out has occurred! Elected new coordinator: " + coordinator + " " + port;
                }
                else {
                    System.out.println("not find!");
                    this.isCoord = true;
                    reply = "Time out has occurred! I am the new coordinator!";
                }
                 System.out.println("here!");
                 this.commit(action, key, value);
            }

            logger.info("Commit finished!");
            semaphore.acquire();
        }
        semaphore.release();
        return reply;
    }
}
