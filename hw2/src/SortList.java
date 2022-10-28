import java.util.List;

public interface SortList
        extends java.rmi.Remote {
    public List<Integer> sort(List<Integer> list)
            throws java.rmi.RemoteException;

}

