package com.ehealthsystem.pdf;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;


/**
 * Class for file save dialogs.
 * Used to save the PDF document in the directory and under the file name chosen by the user.
 */
public class fileChooserSave {

    /**
     * Method to invoke the file save dialog.
     * @param stage stage where the file save dialog is to be shown for.
     * @return Path where the PDF document shall be saved.
     */
    public static String chooseDest(Stage stage) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("export.pdf");

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) { //dialog wasn't cancelled
            String dest = file.getAbsolutePath();
            //document should always end with ".pdf"
            if (!dest .endsWith(".pdf"))
                dest += ".pdf";
            return dest;
        }
        return "";
    }
}
