package com.ehealthsystem.pdf;
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

public class CreatePDF {

    public static void create_Pdf(String dest) throws IOException, SQLException {
        if(dest.isBlank()){
            System.out.println("Saving PDFFile was cancelled!"); return;
        }

        // MySql Connection
        Connection con = null;
        String fileName = "ehealth.sqlite3";

        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            System.out.println("Connection was succesful!");
        } catch (SQLException e) {
            System.out.println("Connection failed! " + e);
        }


        // Creating (PDF-)Document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDoc);

        // PDF Fonts
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

        // Manipulating the Document
        // getting "PageSize" for Centering Text -> HEADLINE
        Rectangle pageSize = pdfDoc.getDefaultPageSize();
        float width = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();

        // PDF static Text
        Text title = new Text("Health Information").setFont(bold).setFontSize(20f);
        Text headline1 = new Text("\n" + "Personal Information:").setFont(bold).setFontSize(14f);
        Text headline2 = new Text("\n" + "Medical Conditions:").setFont(bold).setFontSize(14f);
        Text headline3 = new Text("\n" + "Allergies:").setFont(bold).setFontSize(14f);
        Text headline4 = new Text("\n" + "Insurance:").setFont(bold).setFontSize(14f);

        // Creating Table and editing Size
        float col1[] = {75f, 200f, 70f, 1f};
        Table table1 = new Table(col1);
        float col2[] = {75f, 125f, 75f, 70f, 1f};
        Table table2 = new Table(col2);
        float columnList[] = {280f, 280f};
        Table table3 = new Table(columnList);
        Table table4 = new Table(columnList);
        float col3[] = {125f, 150f, 70f, 1f};
        Table table5 = new Table(col3);

        // Adding Data into Table
        PersonalData1(table1);
        PersonalData2(table2);
        Disease(table3, con);
        //Allergies(table4, con);
        InsuranceData(table5);

        // Headline
        document.add(CenteredParagraph(title, width));
        document.add(new Paragraph().add(headline1));

        // Patient Information
        //document.add(new Paragraph().add("\n"));
        document.add(table1);
        document.add(table2);

        // Medical Conditions
        document.add(new Paragraph().add(headline2));
        document.add(table3);

        /* Allergies
        document.add(new Paragraph().add(headline3));
        document.add(table4); */

        // Insurance Information
        document.add(new Paragraph().add(headline4));
        document.add(table5);

        document.close();
        System.out.println("Pdf created!");
    }

    private static Paragraph CenteredParagraph(Text text, float width) {
        List<TabStop> tabStops = new ArrayList<>();
        tabStops.add(new TabStop(width / 2, TabAlignment.CENTER));

        var output = new Paragraph().addTabStops((tabStops));
        output.add(new Tab())
                .add(text);

        return output;
    }

    private static void PersonalData1(Table table) {
        table.addCell(new Cell().add("First Name: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getFirstName()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Last Name: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getLastName()).setBorder(Border.NO_BORDER));
    }

    private static void PersonalData2(Table table) {
        table.addCell(new Cell().add("Address: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getStreet()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getHouseNumber()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Birthday: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(String.valueOf(Session.user.getBirthDate())).setBorder(Border.NO_BORDER));
    }

    private static void Disease(Table table, Connection con) throws SQLException {
        Paragraph x = null;
        ResultSet rs1;
        PreparedStatement st1 = null;
        String sql1 = "select disease_name from disease";

        st1 = con.prepareStatement(sql1);
        rs1 = st1.executeQuery();
        /*
        int i = 0;
        while(rs1.next()){
            i++;
            x = new Paragraph(rs1.getString("disease_name"));
            table.addCell(new Cell().add(x).setBorder(Border.NO_BORDER));
        }
        if(!rs1.next() && i == 0)
        {
            table.addCell(new Cell().add("--- None ---").setBorder(Border.NO_BORDER));
        }
        */
        table.addCell(new Cell().add("--- None ---").setBorder(Border.NO_BORDER));
    }

    private static void Allergies(Table table, Connection con) throws SQLException {
        /*Paragraph x = null;
        ResultSet rs1;
        String sql1 = "select Allergies from allergies";


        PreparedStatement st1 = con.prepareStatement(sql1);
        rs1 = st1.executeQuery();

        int i = 0;
        while(rs1.next()){
            i++;
            x = new Paragraph(rs1.getString("Allergies"));
            table.addCell(new Cell().add(x).setBorder(Border.NO_BORDER));
        }
        if(!rs1.next() && i == 0)
        {
            table.addCell(new Cell().add("--- None ---").setBorder(Border.NO_BORDER));
        }*/

    }

    private static void InsuranceData(Table table) {
        String insuranceType = Session.user.isPrivateInsurance() ? "Private" : "Public";

        table.addCell(new Cell().add("Insurance Name:").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getInsuranceName()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Type: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(insuranceType).setBorder(Border.NO_BORDER));
    }
}
