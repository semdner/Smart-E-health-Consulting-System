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
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreatePDF {

    public static void create_Pdf(String dest) throws IOException, SQLException {
        if(dest == " "){
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
        PersonalData1(table1, con);
        PersonalData2(table2, con);
        Disease(table3, con);
        //Allergies(table4, con);
        InsuranceData(table5, con);

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

    private static void PersonalData1(Table table, Connection con) throws SQLException {
        Paragraph fn1 = null;
        Paragraph fn2 = null;
        ResultSet rs1;
        ResultSet rs2;
        PreparedStatement st1 = null;
        PreparedStatement st2 = null;
        String sql1 = "select first_name from user where user_id = ?";
        String sql2 = "select last_name from user where user_id = ?";

        st1 = con.prepareStatement(sql1);
        st1.setString(1, String.valueOf(3));
        rs1 = st1.executeQuery();

        while(rs1.next()){
            fn1 = new Paragraph(rs1.getString("first_name"));
        }

        st2 = con.prepareStatement(sql2);
        st2.setString(1, "3" );
        rs2 = st2.executeQuery();

        while(rs2.next()){
            fn2 = new Paragraph(rs2.getString("last_name"));
        }

        table.addCell(new Cell().add("First Name: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getFirstName()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Last Name: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getLastName()).setBorder(Border.NO_BORDER));
    }

    private static void PersonalData2(Table table, Connection con) throws SQLException {
        Paragraph fn1 = null;
        Paragraph fn2 = null;
        Paragraph fn3 = null;
        ResultSet rs1;
        ResultSet rs2;
        ResultSet rs3;
        PreparedStatement st1 = null;
        PreparedStatement st2 = null;
        PreparedStatement st3 = null;
        String sql1 = "select street from user where user_id = ?";
        String sql2 = "select number from user where user_id = ?";
        String sql3 = "select birthday from user where user_id = ?";

        st1 = con.prepareStatement(sql1);
        st1.setString(1, "3" );
        rs1 = st1.executeQuery();

        while(rs1.next()){
            fn1 = new Paragraph(rs1.getString("street"));
        }

        st2 = con.prepareStatement(sql2);
        st2.setString(1, "3" );
        rs2 = st2.executeQuery();

        while(rs2.next()){
            fn2 = new Paragraph(rs2.getString("number"));
        }

        st3 = con.prepareStatement(sql3);
        st3.setString(1, "3" );
        rs3 = st3.executeQuery();

        while(rs3.next()){
            fn3 = new Paragraph(rs3.getString("birthday"));
        }


        table.addCell(new Cell().add("Adress: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getStreet()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getHouseNo()).setBorder(Border.NO_BORDER));
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

    private static void InsuranceData(Table table, Connection con) throws SQLException {
        /*Paragraph fn1 = null;
        Paragraph fn2 = null;
        ResultSet rs1;
        ResultSet rs2;
        PreparedStatement st1 = null;
        PreparedStatement st2 = null;
        String sql1 = "select insurance_name from user where user_id = ?";
        String sql2 = "select private_insurance from user where user_id = ?";
        boolean z = true;

        st1 = con.prepareStatement(sql1);
        st1.setString(1, "3" );
        rs1 = st1.executeQuery();

        while(rs1.next()){
            fn1 = new Paragraph(rs1.getString("insurance_name"));
        }

        st2 = con.prepareStatement(sql2);
        st2.setString(1, "3" );
        rs2 = st2.executeQuery();

        while(rs2.next()){
            z = rs2.getBoolean("private_insurance");
        } */
        String x = null;
        if(!Session.user.isPrivateInsurance()) x = "No";
        else x = "Yes";

        table.addCell(new Cell().add("Insurance Name:").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(Session.user.getInsuranceName()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add("Type: ").setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(x).setBorder(Border.NO_BORDER));
    }
}
