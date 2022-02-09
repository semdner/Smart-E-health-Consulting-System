package com.ehealthsystem.tools;

import java.util.ArrayList;

/**
 * Class for an enumeration of the specialization of the doctor.
 */
public class StringEnumerator {
    /**
     * The method is creating a list of specialization.
     * @param list The arraylist in  which the specialization are stored
     * @return A list of specialization
     */
    public static String enumerate(ArrayList<String> list) {
        StringBuilder specializations = new StringBuilder();
        for (String s : list) {
            specializations.append(s).append(", ");
        }
        return specializations.substring(0, specializations.length() - ", ".length());
    }
}
