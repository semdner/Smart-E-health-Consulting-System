package com.ehealthsystem.pdf;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class fileChooserSave {

    public static String chooseDest(Stage stage) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("export.pdf");

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) { //wasn't cancelled
            String dest = file.getAbsolutePath();
            if (!dest .endsWith(".pdf"))
                dest += ".pdf";
            return dest;
        }
        return "";
    }
}
