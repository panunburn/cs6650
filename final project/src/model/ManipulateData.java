package model;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface ManipulateData extends java.rmi.Remote {
    public String put(String key, String value)
            throws java.rmi.RemoteException, InterruptedException;

    public String get(String key)
            throws java.rmi.RemoteException, InterruptedException;

    public String delete(String key)
            throws java.rmi.RemoteException, InterruptedException;

    public void register(String ip, int port) throws MalformedURLException, NotBoundException, RemoteException, InterruptedException;
    public String commit(Action action, String key, String value) throws InterruptedException, RemoteException, MalformedURLException, NotBoundException;
}
