import java.rmi.RemoteException;

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
        int numberProcesses = Integer.getInteger(args[0]);

        /** create the registry */
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numberProcesses; i++) {
            try {
                launchProcess(i);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("some exception occurred when launching processes");
            }
        }
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
        String codebase = "Djava.rmi.server.codebase=http://mycomputer/~ann/classes/compute.jar";
        ProcessBuilder processBuilder =
                new ProcessBuilder(path, "-cp",
                        classpath,
                        HW1.class.getName());
        Process process = processBuilder.start();
        process.waitFor();
    }
}
