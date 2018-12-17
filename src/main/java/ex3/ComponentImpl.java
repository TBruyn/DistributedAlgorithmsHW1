package ex3;

import java.rmi.RemoteException;

public class ComponentImpl implements ComponentInterface {

    private int id;
    private int numberComponents;
    private Ordinary ordinary;
    private Candidate candidate;

    public ComponentImpl(int id, int numberComponents) {
        this.id = id;
        this.numberComponents = numberComponents;
        ordinary = new Ordinary();
        candidate = null;
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
        candidate = new Candidate(numberComponents, id);
    }

    public void start() {

        while (true) {
            if (candidate != null) {
                candidate.start();
            }
        }
    }


}
