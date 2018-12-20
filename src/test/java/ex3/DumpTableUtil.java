package ex3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class DumpTableUtil {

    public static void dumpData(List<Map<String, Integer>> msgData, Map<Integer, Integer> maxLevels) {
        String table = msgTable(msgData, maxLevels);
        int numberComponents = msgData.size();
        int numberCandidates = maxLevels.keySet().size();

        String currentDir = Paths.get("./ex3tables").toAbsolutePath().normalize().toString();
        String filename = String.format("%d-%d_msg.table", numberCandidates, numberComponents);
        String filepath = currentDir + "/" + filename;

        try {
            Files.write(Paths.get(filepath), table.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String table2 = levelTable(msgData, maxLevels);
        String filename2 = String.format("%d-%d_level.table", numberCandidates, numberComponents);
        String filepath2 = currentDir + "/" + filename2;

        try {
            Files.write(Paths.get(filepath2), table2.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String levelTable(List<Map<String, Integer>> msgData, Map<Integer, Integer> maxLevels) {
        int numberComponents = msgData.size();
        int numberCandidates = maxLevels.keySet().size();
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("\\begin{table}\n");
        sbuilder.append("\\centering\n");
        sbuilder.append("\\begin{tabular}{||c|c||}\n");
        sbuilder.append(" \\hline\n");
        sbuilder.append(" Candidate & level reached\\\\ [0.5ex]\n");
        sbuilder.append(" \\hline\\hline\n");

        for (Integer key: maxLevels.keySet()) {
            int candidateId = key;
            int levelReached = maxLevels.get(key);
            sbuilder.append(String.format(
                    " %d & %d \\\\ \n",
                    candidateId, levelReached));
        }
        sbuilder.append(" \\hline\n");
        sbuilder.append(" \\end{tabular}\n");
        sbuilder.append(String.format(
                " \\caption{%d components, %d candidates: levels reached per candidate}\n",
                numberComponents, numberCandidates));
        sbuilder.append("\\end{table}\n");
        return sbuilder.toString();
    }

    public static String msgTable(List<Map<String, Integer>> msgData, Map<Integer, Integer> maxLevels) {
        int numberComponents = msgData.size();
        int numberCandidates = maxLevels.keySet().size();
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("\\begin{table}\n");
        sbuilder.append("\\centering\n");
        sbuilder.append("\\begin{tabular}{||c|c c c c | c||}\n");
        sbuilder.append(" \\hline\n");
        sbuilder.append(" Component & rec. Capture & sent Capture & sent Kill & rec. Kill & all \\\\ [0.5ex]\n");
        sbuilder.append(" ID & Requests & Confirms & Requests & Confirms & \\\\ [0.5ex]\n");
        sbuilder.append(" \\hline\\hline\n");

        int totalCaptureRequests = 0;
        int totalCaptureConfirms = 0;
        int totalKillRequests = 0;
        int totalKillConfirms = 0;
        int total = 0;

        for (int i = 0; i < msgData.size(); i++) {
            int componentId = i;
            int captureRequests = msgData.get(i).get(Manager.RECEIVE_CAPTURE_REQUEST);
            totalCaptureRequests += captureRequests;
            int captureConfirms = msgData.get(i).get(Manager.SEND_CAPTURE_CONFIRM);
            totalCaptureConfirms += captureConfirms;
            int killRequests = msgData.get(i).get(Manager.SEND_KILL_REQUEST);
            totalKillRequests += killRequests;
            int killConfirms = msgData.get(i).get(Manager.RECEIVE_KILL_CONFIRM);
            totalKillConfirms += killConfirms;
            int all = captureConfirms + captureRequests
                    + killConfirms + killRequests;
            total += all;
            sbuilder.append(String.format(
                    " %d & %d & %d & %d & %d & %d\\\\ \n",
                    componentId, captureRequests, captureConfirms, killRequests, killConfirms, all));
        }

        sbuilder.append(" \\hline\n");
        sbuilder.append(String.format(
                " all & %d & %d & %d & %d & %d\\\\ \n",
                totalCaptureRequests, totalCaptureConfirms, totalKillRequests, totalKillConfirms, total));

        sbuilder.append(" \\hline\n");
        sbuilder.append(" \\end{tabular}\n");
        sbuilder.append(String.format(
                " \\caption{%d components, %d candidates: kill/capture requests and confirms}\n",
                numberComponents, numberCandidates));
        sbuilder.append("\\end{table}\n");
        return sbuilder.toString();
    }

}
