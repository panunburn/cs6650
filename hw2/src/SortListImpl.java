import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortListImpl
        extends
        java.rmi.server.UnicastRemoteObject
        implements SortList {

    // Implementations must have an
    //explicit constructor
    // in order to declare the
    //RemoteException exception
    public SortListImpl()
            throws java.rmi.RemoteException {
        super();
    }


    @Override
    public List<Integer> sort(List<Integer> list) throws RemoteException {
        List<Integer> newList = new ArrayList<>(list);
        Collections.sort(newList);
        return newList;
    }
}

