package com.dragon.intec.components;

/*
 * Created by hecto on 11/7/2016.
 */


import android.app.Activity;

import com.dragon.intec.MainActivity;
import com.dragon.intec.R;
import com.dragon.intec.objects.ActualsReport;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class PDFBuilder {


    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18,
            Font.BOLD);
    private static Font normalFont = new Font(Font.FontFamily.HELVETICA, 7);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 10,
            Font.BOLD);

    public PDFBuilder(){

    }

    private PdfPCell makeCell(String val, Boolean title, Boolean middle){

        Phrase phrase = new Phrase(val);

        if(title){
            phrase.setFont(smallBold);
        }

        PdfPCell c1 = new PdfPCell(phrase);
        c1.setBackgroundColor(new BaseColor(245, 245, 245));
        c1.setBorderColor(new BaseColor(245, 245, 245));
        c1.setNoWrap(false);

        if(middle){
            Float fontSize = phrase.getFont().getSize();
            Float capHeight = phrase.getFont().getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);
            Float padding = 5f;

            c1.setPadding(padding);
            c1.setPaddingTop(capHeight - fontSize + padding);
        }

        return c1;
    }

    private PdfPCell makeCell(String val, Boolean title, Boolean middle, BaseColor baseColor){

        Phrase phrase = new Phrase(val);
        phrase.setFont(normalFont);

        if(title){
            phrase.setFont(smallBold);
        }
        PdfPCell c1 = new PdfPCell(phrase);
        if(baseColor != null){
            c1.setBackgroundColor(baseColor);
        }

        if(middle){
            Float padding = 5f;
            c1.setPadding(padding);
            c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        }

        return c1;
    }

    public String buildFinalGrades(int quarter, JSONObject object, Activity activity) throws IOException, DocumentException, JSONException {

        //Loads the required font (Calibri.ttf).
        Integer[] raw = {R.raw.calibri};
        File file = new File(MainActivity.myDir.getPath(), "calibri.ttf");
        Utils.createFile(file.getPath() , activity, raw);
        BaseFont baseFont = BaseFont.createFont(file.getPath(), BaseFont.WINANSI, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 14, Font.BOLD);

        String FILE = MainActivity.myDir.getPath() + "/FinalGrade"+ quarter +".pdf";
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();

        Paragraph catPart = new Paragraph();

        //Initial table/////////////////////////////////////////////////////////////////////////////

        PdfPTable infoTable = new PdfPTable(2);

        JSONArray jsonArray = object.getJSONObject("heading").getJSONArray("fields");
        String[] array = new String[3];
        for (int i = 0; i < jsonArray.length(); i++){
            array[i] = jsonArray.getString(i);
        }

        infoTable.addCell(makeCell("ID:", true, false));
        infoTable.addCell(makeCell(array[0], false, false));
        infoTable.addCell(makeCell("Nombre:", true, false));
        infoTable.addCell(makeCell(array[1], false, false));
        infoTable.addCell(makeCell("Programa:", true, false));
        infoTable.addCell(makeCell(array[2], false, false));

        catPart.add(infoTable);
        addEmptyLine(catPart, 1);

        //Second table//////////////////////////////////////////////////////////////////////////////

        catPart.add(new Paragraph("Acumulados del trimestre anterior", font));
        addEmptyLine(catPart, 1);
        jsonArray = object.getJSONArray("pastAnalist");

        ArrayList<Integer> credits = new ArrayList<>();
        ArrayList<Double> grades = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            try {
                credits.add(jsonObject.optInt("credits"));
            } catch (Exception ex){
                ex.printStackTrace();
                credits.add(0);
            }
            try {
                grades.add(Double.parseDouble(jsonObject.optString("grade")));
            } catch (Exception ex){
                ex.printStackTrace();
                grades.add(0.0);
            }
        }

        convertGradesDown(grades);
        int creditsTotal = getTotalCredits(credits);
        double pointsTotal = getTotalPoints(credits, grades);
        double index = pointsTotal/(creditsTotal*1.0);
        String condition = "NORMAL";

        if (index <= 1){
            condition = "PRUEBA ACADEMICA";
        }

        PdfPTable pastTable = new PdfPTable(4);
        pastTable.addCell(makeCell("Creditos Acumulados", true, true, BaseColor.LIGHT_GRAY));
        pastTable.addCell(makeCell("Puntos Acumulados", true, true, BaseColor.LIGHT_GRAY));
        pastTable.addCell(makeCell("Indice Acumulado", true, true, BaseColor.LIGHT_GRAY));
        pastTable.addCell(makeCell("Condicion Academica", true, true, BaseColor.LIGHT_GRAY));
        pastTable.addCell(makeCell(String.valueOf(creditsTotal), false, true, null));
        pastTable.addCell(makeCell(String.format("%.2f", pointsTotal), false, true, null));
        pastTable.addCell(makeCell(String.format("%.2f", index), false, true, null));
        pastTable.addCell(makeCell(condition, false, true, null));

        catPart.add(pastTable);
        addEmptyLine(catPart,1);

        //Third table//////////////////////////////////////////////////////////////////////////////////////////

        catPart.add(new Paragraph("Calificaciones del trimestre", font));
        addEmptyLine(catPart, 1);

        jsonArray = object.getJSONArray("actuals");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject objectA = jsonArray.getJSONObject(i);
            ActualsReport actualsReport = new ActualsReport();
            actualsReport.setSignatureName(objectA.optString("name"));
            actualsReport.setSignatureCode(objectA.optString("code"));
            actualsReport.setSection(objectA.optString("section"));
            actualsReport.setObservation(objectA.optString("observation"));
            actualsReport.setId(objectA.optInt("id"));
            actualsReport.setGrade(objectA.optString("grade"));
            actualsReport.setCredits(objectA.optInt("credits"));
            actualsReport.setCicle(objectA.optInt("cicle"));

        }

        PdfPTable currentTable = new PdfPTable(7);
        currentTable.addCell(makeCell("Clave", true, true, BaseColor.LIGHT_GRAY));
        currentTable.addCell(makeCell("Puntos Acumulados", true, true, BaseColor.LIGHT_GRAY));
        currentTable.addCell(makeCell("Indice Acumulado", true, true, BaseColor.LIGHT_GRAY));
        currentTable.addCell(makeCell("Condicion Academica", true, true, BaseColor.LIGHT_GRAY));
        currentTable.addCell(makeCell("Creditos Acumulados", true, true, BaseColor.LIGHT_GRAY));
        currentTable.addCell(makeCell("Puntos Acumulados", true, true, BaseColor.LIGHT_GRAY));
        currentTable.addCell(makeCell("Indice Acumulado", true, true, BaseColor.LIGHT_GRAY));

        catPart.add(currentTable);

        document.add(catPart);
        document.close();

        return FILE;
    }

    private double getTotalPoints(ArrayList<Integer> creditsList, ArrayList<Double> gradesList){
        double total = 0;

        for (int i = 0; i < creditsList.size(); i++){
            total = total + (creditsList.get(i) * gradesList.get(i));
        }

        return total;
    }

    private int getTotalCredits(ArrayList<Integer> creditsList) {

        int total = 0;

        for(Integer credit : creditsList){
            total = total + credit;
        }
        return total;
    }

    private void convertGradesDown(ArrayList<Double> main){

        for(int i = 0; i < main.size(); i++){
            main.set(i, equivalent(main.get(i)));
        }

    }

    private double equivalent(Double realVal){

        if (realVal >= 90)
            realVal = 4.0;
        else if (realVal >= 85)
            realVal = 3.5;
        else if (realVal >= 80)
            realVal = 3.0;
        else if (realVal >= 75)
            realVal = 2.5;
        else if (realVal >= 70)
            realVal = 2.0;
        else if (realVal >= 60)
            realVal = 1.0;
        else
            realVal = 0.0;

        return realVal;
    }

    public void newDocument(){
        try {
            Document document = new Document();


            //addMetaData(document);
            addTitlePage(document);
            //addContent(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document, String title, String subject, String keywords, String author, String creator) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addKeywords(keywords);
        document.addAuthor(author);
        document.addCreator(creator);
    }

    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Title of the document", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document describes something which is very important ",
                smallBold));

        addEmptyLine(preface, 8);

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    /*private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Section subCatPart = catPart.addSection(new Paragraph());
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }*/

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(2);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
