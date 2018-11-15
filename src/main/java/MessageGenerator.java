public class MessageGenerator implements Runnable{
    private int numProcesses;
    private int ownerPid;
    private Clock ownerClock;

    public MessageGenerator(int numProcesses, int ownerPid, Clock ownerClock) {
        this.numProcesses = numProcesses;
        this.ownerPid = ownerPid;
        this.ownerClock = ownerClock;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("Process %d alive", ownerPid));
        }
    }
}
