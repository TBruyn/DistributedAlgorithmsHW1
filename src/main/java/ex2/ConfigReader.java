package ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ConfigReader {

    public static String[] getComponentNames() throws IOException {
        Path path = Paths.get("src/main/java/ex2/ex2components.dat");
        BufferedReader reader = Files.newBufferedReader(path);
        return reader.lines().collect(Collectors.toList()).toArray(new String[0]);
    }

    public static String[] appendIdComponents(String[] names) {
        String[] fullnames = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            fullnames[i] = names[i] + String.format("/%d", i);
        }
        return fullnames;
    }
}
