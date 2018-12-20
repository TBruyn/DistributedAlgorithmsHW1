package ex3;

import java.util.List;
import java.util.Map;

public class PrintTests {

    public static void main(String[] args){
        runTest(5,5);
    }

    public static void runTest(int numberOfComponents, int numberOfCandidates) {
        Main.setup(numberOfComponents);
        DelayUtil.initialTimeout();
        for(int i = 0; i < numberOfCandidates; i++) {
            RMIUtil.startCandidate(i);
        }

        // wait for termination of algorithm
        while (!Manager.getInstance().allTerminated()) {
            // nothing
            DelayUtil.initialTimeout();
        }
        Map<Integer, Integer> maxLevels = Manager.getInstance().getMaxLevels();
        List<Map<String, Integer>> messageData = Manager.getInstance().getMessageData();

        printMaxLevels(maxLevels);
        printMessageData(messageData);
    }

    private static void printMessageData(List<Map<String,Integer>> messageData) {
        System.out.println();
        System.out.println("Process\trc\tsc\tsk\trk");
        for(int i = 0; i < messageData.size(); i++) {
            Map<String, Integer> data = messageData.get(i);
            System.out.println(
                    String.format("\t%d:\t%d\t%d\t%d\t%d",
                    i,
                    messageData.get(i).get(Manager.RECEIVE_CAPTURE_REQUEST),
                    messageData.get(i).get(Manager.SEND_CAPTURE_CONFIRM),
                    messageData.get(i).get(Manager.SEND_KILL_REQUEST),
                    messageData.get(i).get(Manager.RECEIVE_KILL_CONFIRM)
                            ));
        }
    }

    private static void printMaxLevels(Map<Integer,Integer> maxLevels) {
        System.out.println();
        System.out.println("Process\tLevel");
        for(int process : maxLevels.keySet()) {
            System.out.println(process + "\t\t" + maxLevels.get(process));
        }
    }
}
