
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

public class SortListClient {

    public static void main(String[] args) {
        try {
            SortList s = (SortList)
                    Naming.lookup(
                            "rmi://localhost:1099/SortListService");
            List<Integer> list = new ArrayList<>();
            list.add(10);
            list.add(9);
            list.add(8);
            list.add(7);
            list.add(6);
            list.add(5);
            list.add(4);
            list.add(3);
            list.add(2);
            list.add(1);
            System.out.println( s.sort(list) );
        }
        catch (MalformedURLException murle) {
            System.out.println();
            System.out.println(
                    "MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println();
            System.out.println(
                    "RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println();
            System.out.println(
                    "NotBoundException");
            System.out.println(nbe);
        }
        catch (
                java.lang.ArithmeticException
                        ae) {
            System.out.println();
            System.out.println(
                    "java.lang.ArithmeticException");
            System.out.println(ae);
        }
    }
}

