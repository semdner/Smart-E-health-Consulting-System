package com.ehealthsystem.tools;

import java.util.ArrayList;

public class StringEnumerator {
    public static String enumerate(ArrayList<String> list) {
        StringBuilder specializations = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            specializations.append(list.get(i)).append(", ");
        }
        return specializations.substring(0, specializations.length() - ", ".length());
    }
}
