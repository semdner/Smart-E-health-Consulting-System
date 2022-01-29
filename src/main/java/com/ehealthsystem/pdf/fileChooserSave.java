package com.ehealthsystem.pdf;

import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.awt.event.*;

public class fileChooserSave {

    private static File fileToSave;

    public static String chooseDest() {
        String dest = "";
        String x = ".pdf";
        JFrame parentFrame = new JFrame();

        //Standard Open Dialog
        //Create a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("*.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("pdf file","pdf"));
        //Response of button click
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if(userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            dest = fileToSave.getAbsolutePath();
            if (!dest .endsWith(".pdf"))
                dest += ".pdf";
        } else if(userSelection == JFileChooser.CANCEL_OPTION) {
            System.out.println("Saving PDFFile was cancelled!");
            return dest = " ";
        }

        return dest;
    }

}
