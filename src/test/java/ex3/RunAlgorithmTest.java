package ex3;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class RunAlgorithmTest {

    @Test
    void fiveComponentOneCandidateTest() {
        final int NUM_COMPONENTS = 5;
        final int CANDIDATE = 2;
        Main.setup(NUM_COMPONENTS);
        DelayUtil.initialTimeout();
        RMIUtil.startCandidate(CANDIDATE);

        // wait for termination of algorithm
        while (!Manager.getInstance().allTerminated()) {
            // nothing
            DelayUtil.initialTimeout();
        }
        int winner = Manager.getInstance().getWinnerId();
        List<Map<String, Integer>> messageData = Manager.getInstance().getMessageData();
        Map<Integer, Integer> maxLevels = Manager.getInstance().getMaxLevels();

        // for every ordinary process we expect 1 capture request/confirm and 0 kill request/confirm

        for (int i = 0; i < NUM_COMPONENTS; i++) {
            assertEquals(new Integer(1), messageData.get(i).get(Manager.RECEIVE_CAPTURE_REQUEST));
            assertEquals(new Integer(1), messageData.get(i).get(Manager.SEND_CAPTURE_CONFIRM));
            assertEquals(new Integer(0), messageData.get(i).get(Manager.SEND_KILL_REQUEST));
            assertEquals(new Integer(0), messageData.get(i).get(Manager.RECEIVE_KILL_CONFIRM));
        }

        assertEquals(CANDIDATE, winner);
    }

    @Test
    void twentyComponentOneCandidateTest() {
        final int NUM_COMPONENTS = 20;
        final int CANDIDATE = 0;
        Main.setup(NUM_COMPONENTS);
        DelayUtil.initialTimeout();
        RMIUtil.startCandidate(0);
        RMIUtil.startCandidate(1);
        RMIUtil.startCandidate(2);
        RMIUtil.startCandidate(3);
        RMIUtil.startCandidate(4);
        RMIUtil.startCandidate(5);
        RMIUtil.startCandidate(6);

        // wait for termination of algorithm
        while (!Manager.getInstance().allTerminated()) {
            // nothing
            DelayUtil.initialTimeout();
        }
        int winner = Manager.getInstance().getWinnerId();
        List<Map<String, Integer>> messageData = Manager.getInstance().getMessageData();

        // for every ordinary process we expect 1 capture request/confirm and 0 kill request/confirm

        for (int i = 0; i < NUM_COMPONENTS; i++) {
            System.out.println(messageData.get(i).get(Manager.RECEIVE_CAPTURE_REQUEST));
            System.out.println(messageData.get(i).get(Manager.SEND_CAPTURE_CONFIRM));
            System.out.println(messageData.get(i).get(Manager.SEND_KILL_REQUEST));
            System.out.println(messageData.get(i).get(Manager.RECEIVE_KILL_CONFIRM));

//            assertEquals(new Integer(1), messageData.get(i).get(Manager.RECEIVE_CAPTURE_REQUEST));
//            assertEquals(new Integer(1), messageData.get(i).get(Manager.SEND_CAPTURE_CONFIRM));
//            assertEquals(new Integer(0), messageData.get(i).get(Manager.SEND_KILL_REQUEST));
//            assertEquals(new Integer(0), messageData.get(i).get(Manager.RECEIVE_KILL_CONFIRM));
        }



//        assertEquals(CANDIDATE, winner);
    }
}
