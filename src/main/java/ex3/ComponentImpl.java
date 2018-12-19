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
        synchronized (this) {
            ordinary.receive(msg);
        }
    }

    @Override
    public void callCandidate(Message msg) throws RemoteException {
        synchronized (this) {
            candidate.receive(msg);
        }
    }

    @Override
    public void startCandidate() throws RemoteException {
        candidate = new Candidate(numberComponents, id);
    }

    @Override
    public void terminate() throws RemoteException {
        // 1. terminate ordinary (stop logging)
        // 2. terminate candidate (stop and report level)
        if (candidate != null)
            candidate.terminate();
        running = false;
        Manager.getInstance().announceTermination(id);
    }

    public void start() {

        while (running) {
            if (candidate != null) {
                candidate.start();
            }
        }
    }


}
