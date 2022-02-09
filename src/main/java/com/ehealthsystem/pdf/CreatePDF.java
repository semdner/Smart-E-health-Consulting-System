package com.ehealthsystem.pdf;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.healthInformation.HealthInformation;
import com.ehealthsystem.tools.Session;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TabAlignment;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for creating a PDF document.
 */
public class CreatePDF {

    /**
     * Method to create a PDF document with the health information and data of the user.
     * @param dest path where the created PDF doc should be saved.
     * @param forAppointment true if only the health information for the appointment in creation shall be included
     * @throws IOException PDF document is not able to be saved.
     * @throws SQLException if a connection issue with the database occurs
     */
    public static void create_Pdf(String dest, boolean forAppointment) throws IOException, SQLException {

        //If the user does not choose a destination for the file, the method(create_Pdf()) will be aborted
        if(dest.isBlank()){
            System.out.println("Saving PDF file was cancelled!"); return;
        }

        //Creating the Document as a pdf and setting the page size.
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDoc);

        //Used fonts in the document.
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

        //getting "PageSize" for Centering Text -> HEADLINE
        Rectangle pageSize = pdfDoc.getDefaultPageSize();
        float width = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();

        //Static text/headlines for the document
        Text title = new Text("Health Information").setFont(bold).setFontSize(20f);
        Text headline1 = new Text("\n" + "Personal Information:").setFont(bold).setFontSize(14f);
        Text headline2 = new Text("\n" + "Medical Conditions:").setFont(bold).setFontSize(14f);
        Text headline3 = new Text("\n" + "Insurance:").setFont(bold).setFontSize(14f);

        //Creating Table where the data will be stored
        float[] col1 = {75f, 200f, 70f, 1f};
        Table table1 = new Table(col1);
        float[] col2 = {75f, 125f, 75f, 70f, 1f};
        Table table2 = new Table(col2);
        float[] columnList = {280f, 280f};
        Table table3 = new Table(columnList);
        float[] col3 = {125f, 150f, 70f, 1f};
        Table table4 = new Table(col3);

        //Adding Data into the Table.
        PersonalData1(table1);
        PersonalData2(table2);
        Disease(table3, forAppointment);
        InsuranceData(table4);

        //Adding the prepared data into the document.
        // Headline
        document.add(CenteredParagraph(title, width));
        document.add(new Paragraph().add(headline1));
        // Patient Information
        document.add(table1);
        document.add(table2);
        // Medical Conditions
        document.add(new Paragraph().add(headline2));
        document.add(table3);
        // Insurance Information
        document.add(new Paragraph().add(headline3));
        document.add(table4);

        //always close the document in the end, so it can be saved.
        document.close();
    }

    /**
     * Method to center the text.
     * For this document it is only used for the headline.
     * @param text The text which will be centered.
     * @param width The width of the Document to calculate the center.
     * @return The Text in the center of the document.
     */
    private static Paragraph CenteredParagraph(Text text, float width) {
        List<TabStop> tabStops = new ArrayList<>();
        tabStops.add(new TabStop(width / 2, TabAlignment.CENTER));

        var output = new Paragraph().addTabStops((tabStops));
        output.add(new Tab())
                .add(text);

        return output;
    }

    /**
     * Method to add first and last name of the user into the table.
     * @param table The provided Table for the data.
     */
    private static void PersonalData1(Table table) {
        table.addCell(new Cell().add("First Name: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getFirstName()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Last Name: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getLastName()).setBorder(Border.NO_BORDER));
    }

    /**
     * Method to add street and birthday of the user into the table.
     * @param table The provided Table for the data.
     */
    private static void PersonalData2(Table table) {
        table.addCell(new Cell().add("Address: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getStreet()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getHouseNumber()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Birthday: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getBirthDate().format(Session.dateFormatter)).setBorder(Border.NO_BORDER));
    }

    /**
     * Method to add the health-problems of the user into the table.
     * @param table The provided table for the data.
     * @param forAppointment true if only the health information for the appointment in creation shall be included
     * @throws SQLException Throws Exception during connection issues with the Database.
     */
    private static void Disease(Table table, boolean forAppointment) throws SQLException {
        ArrayList<HealthInformation> healthInformation = forAppointment ? Session.appointment.getHealthInformation() : Database.getHealthInformation(Session.user.getMail());
        if (healthInformation.size() == 0) {
            table.addCell(new Cell().add("--- None ---").setBorder(Border.NO_BORDER));
        } else {
            for (HealthInformation h : healthInformation) {
                Paragraph x = new Paragraph(h.getICD() + ": " + h.getDisease());
                table.addCell(new Cell().add(x).setBorder(Border.NO_BORDER));
            }
        }
    }

    /**
     * Method to add insurance name and type into the table.
     * @param table The provided table for the data.
     */
    private static void InsuranceData(Table table) {
        String insuranceType = Session.user.isPrivateInsurance() ? "Private" : "Public";

        table.addCell(new Cell().add("Insurance Name:").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getInsuranceName()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Type: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(insuranceType).setBorder(Border.NO_BORDER));
    }
}
