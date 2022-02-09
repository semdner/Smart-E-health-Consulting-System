package com.ehealthsystem.tools;

import java.util.ArrayList;

public class StringEnumerator {
    /**
     * Display any string list (e.g. doctor's specializations) to the user in a friendly way
     * The elements in the list are enumerated, separated by a comma
     * @param list The list that is to be enumerated
     * @return A single string, enumerating all the elements in the provided list
     */
    public static String enumerate(ArrayList<String> list) {
        StringBuilder specializations = new StringBuilder();
        for (String s : list) {
            specializations.append(s).append(", ");
        }
        return specializations.substring(0, specializations.length() - ", ".length());
    }
}
