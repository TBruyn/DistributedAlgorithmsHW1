import java.net.URLClassLoader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    /**
     * this should do the following:
     * 1. setup the rmi registry
     * 2. run all processes on different JVMs
     *
     * @param args number of processes
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("wrong number args");
        }
        int numberProcesses = Integer.parseInt(args[0]);

        /** create the registry */
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numberProcesses; i++) {
            try {
                launchInThread(i, numberProcesses);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("some exception occurred when launching processes");
            }
        }

        System.out.println("launched all processes");
        while (true) {}
    }

    /**
     * launch the process with id i
     * invoke java command to create new jvm that runs the process main method
     * @param i
     */
    private static void launchProcess(int i) throws Exception {
        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String path = System.getProperty("java.home")
                + separator + "bin" + separator + "java";
        String codebase = "-Djava.rmi.server.codebase=file:///Users/jannes/bla.jar";
        String hostname = "-Djava.rmi.server.hostname=rmi://localhost";
        String security = "-Djava.security.policy=server.policy";
        ProcessBuilder processBuilder =
                new ProcessBuilder(path, "-cp",
                        classpath,
                        HW1.class.getName());
        Process process = processBuilder.start();
        process.waitFor();
    }

    private static void launch(int pid, int n) throws Exception {
        System.out.println(String.format("launch %d", pid));
//        String classpath = "/Users/jannes/University2/DistributedAlgorithms/da-homework/src/main/java/dir/";
        String classpath = System.getProperty("java.class.path");
        String command = "java -cp " + classpath;
//        ProcessBuilder pb = new ProcessBuilder(command, HW1.class.getName(), String.valueOf(pid), String.valueOf(n));
        ProcessBuilder pb = new ProcessBuilder("java", HW1.class.getName(), String.valueOf(pid), String.valueOf(n));
        Process process = pb.inheritIO().start();
        process.waitFor();
        System.out.println(String.format("launched %d", pid));
    }

    /**
     * launch process in the same jvm in another thread
     * @param pid
     * @param n
     */
    private static void launchInThread(int pid, int n) {
        new Thread(() -> {
            try {
                HW1 process = new HW1(pid, n);
                HW1Interface stub = (HW1Interface) UnicastRemoteObject.exportObject(process, 0);
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(String.valueOf(pid), stub);
                System.out.println(String.format("Process %s bound", String.valueOf(pid)));
                process.startProcess();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
