
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class SortListServer {

    public SortListServer() {
        try {
            SortList s = new SortListImpl();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/SortListService", s);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    public static void main(String args[]) {
        new SortListServer();
    }
}


