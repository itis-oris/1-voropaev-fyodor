package ru.itis.merch.store.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RoleToPathReaderImpl implements RoleToPathReader {

    @Override
    public Map<String, List<String>> readToMap(String filename) {
        Map<String, List<String>> result = new ConcurrentHashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename))))) {
            String role = null;
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.endsWith(":")) {
                    role = line.substring(0, line.length() - 1);
                    result.put(role, new ArrayList<>());
                    continue;
                }
                if (role == null) {
                    throw new IllegalArgumentException("The pattern is specified before the role");
                }
                result.get(role).add(line);
            }
            return result;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Error while parsing paths: " + exception.getMessage(), exception);
        }
    }
}
