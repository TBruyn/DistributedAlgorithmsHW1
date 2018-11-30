package ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigReader {

    public static String[] getComponentNames() throws IOException {
        Path path = Paths.get(ConfigReader.class.getResource("ex2components.dat").getPath());
        BufferedReader reader = Files.newBufferedReader(path);
        return (String[]) reader.lines().toArray();
    }
}
