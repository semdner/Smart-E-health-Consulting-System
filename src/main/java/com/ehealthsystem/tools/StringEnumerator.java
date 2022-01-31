package com.ehealthsystem.tools;

import java.util.ArrayList;

public class StringEnumerator {
    public static String enumerate(ArrayList<String> list) {
        StringBuilder specializations = new StringBuilder();
        for (String s : list) {
            specializations.append(s).append(", ");
        }
        return specializations.substring(0, specializations.length() - ", ".length());
    }
}
