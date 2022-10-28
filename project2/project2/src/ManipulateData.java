public interface ManipulateData extends java.rmi.Remote {
    public String put(String key, String value)
            throws java.rmi.RemoteException, InterruptedException;

    public String get(String key)
            throws java.rmi.RemoteException, InterruptedException;

    public String delete(String key)
            throws java.rmi.RemoteException, InterruptedException;
}
