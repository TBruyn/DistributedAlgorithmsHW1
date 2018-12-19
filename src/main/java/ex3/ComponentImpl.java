package ex3;

import java.rmi.RemoteException;

public class ComponentImpl implements ComponentInterface {

    private int id;
    private int numberComponents;
    private Ordinary ordinary;
    private Candidate candidate;
    private boolean running;

    public ComponentImpl(int id, int numberComponents) {
        this.id = id;
        this.numberComponents = numberComponents;
        ordinary = new Ordinary(id);
        candidate = null;
        running = true;
    }

    @Override
    public void callOrdinary(Message msg) throws RemoteException {
        ordinary.receive(msg);
    }

    @Override
    public void callCandidate(Message msg) throws RemoteException {
        candidate.receive(msg);
    }

    @Override
    public void startCandidate() throws RemoteException {
        System.out.println(String.format("Component %d received call to start candidate", id));
        candidate = new Candidate(numberComponents, id);
        System.out.println(String.format("candidate now: %s", candidate.toString()));
    }

    @Override
    public void terminate() throws RemoteException {
        // 1. terminate ordinary (stop logging)
        running = false;
        // 2. terminate candidate (stop and report level)
        if (candidate != null)
            candidate.terminate();
        // 3. report termination
        Manager.getInstance().announceTermination(id);
    }

    public void start() {
        System.out.println(String.format("Component %d started", id));
        while (running) {
            DelayUtil.initialTimeout();
            if (candidate != null) {
                System.out.println(String.format("Component %d starts candidate", id));
                candidate.start();
                return;
            }
        }
    }


}
