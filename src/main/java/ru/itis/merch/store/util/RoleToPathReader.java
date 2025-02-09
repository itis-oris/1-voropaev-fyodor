package ru.itis.merch.store.util;

import java.util.List;
import java.util.Map;

public interface RoleToPathReader {

    Map<String, List<String>> readToMap(String filename);
}
