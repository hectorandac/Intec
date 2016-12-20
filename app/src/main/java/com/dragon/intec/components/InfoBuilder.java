package com.dragon.intec.components;

/*
 * Created by hecto on 11/19/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.fragments.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoBuilder {
    private Context context;
    private JSONObject obj;
    private LayoutInflater inflater;
    private static int CENTER_GRAVITY = 17;

    public InfoBuilder(Context context, JSONObject obj){
        this.context = context;
        this.obj = obj;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private Title createTitle(int columns, int headIndex){
        Title title = new Title(columns, headIndex);
        return title;
    }

    public View getTitleType_1() throws JSONException {
        Title title = new Title(2, 0);
        JSONObject heading = obj.getJSONObject("heading");
        JSONArray rowsFields = heading.getJSONArray("fields");

        ArrayList<String[]> rows = new ArrayList<>();
        String[] headings_1 = {context.getResources().getString(R.string.id_reports_f), ""};
        String[] headings_2 = {context.getResources().getString(R.string.name_reports_f), ""};
        String[] headings_3 = {context.getResources().getString(R.string.program_reports_f), ""};

        rows.add(headings_1);
        rows.add(headings_2);
        rows.add(headings_3);

        for(int i = 0; i < rows.size(); i++){
            rows.get(i)[1] = rowsFields.getString(i);
        }

        title.addFields(rows);
        return title.getTitle();
    }

    public View createHeading(String text){
        Heading heading = new Heading(text);
        return heading.getHeading();
    }

    public View getTitleType_2() throws JSONException {
        Title title = new Title(2, 0);
        JSONObject heading = obj.getJSONObject("heading");
        JSONArray rowsFields = heading.getJSONArray("fields");

        ArrayList<String[]> rows = new ArrayList<>();
        String[] headings_1 = {context.getResources().getString(R.string.convalidated_reports_f), String.valueOf(HomeFragment.student.getValidatedCredits())};
        String[] headings_2 = {context.getResources().getString(R.string.aproved_reports_f), String.valueOf(HomeFragment.student.getApprovedCredits())};
        String[] headings_3 = {context.getResources().getString(R.string.credits_reports_f), HomeFragment.student.getCredits()};
        String[] headings_4 = {context.getResources().getString(R.string.signatures_reports_f), ""};

        rows.add(headings_1);
        rows.add(headings_2);
        rows.add(headings_3);
        rows.add(headings_4);

        if(rowsFields.getString(3).equals("1")){
            rows.get(3)[1] = "ACTIVO";
        }else{
            rows.get(3)[1] = "DESPLAZADO";
        }

        title.addFields(rows);
        return title.getTitle();
    }

    private int pastCr = 0;
    private double pastPn = 0;

    private int cycle_1 = 0;
    private int cycle_2 = 0;
    private int cycle_3 = 0;

    private double totalIndex = 0;

    private String observation = "";

    public View createTableType_1(String[] heading) throws JSONException {
        ArrayList<String[]> data = new ArrayList<>();
        data.add(heading);

        String[] names = { "id",
                "grade",
                "credits",
                "cicle"};

        JSONArray rows = obj.getJSONArray("pastAnalist");
        ArrayList<String[]> secondaryData = new ArrayList<>();

        for(int i = 0; i < rows.length(); i++){
            String[] info = new String[names.length];
            JSONObject object = rows.getJSONObject(i);

            switch (Integer.parseInt(object.getString("cicle"))){
                case 0:
                    cycle_1 += Integer.parseInt(object.getString("credits"));
                    break;
                case 1:
                    cycle_2 += Integer.parseInt(object.getString("credits"));
                    break;
                case 2:
                    cycle_3 += Integer.parseInt(object.getString("credits"));
                    break;
            }

            for(int j = 0; j < info.length; j++) {
                info[j] = object.optString(names[j]);
            }
            secondaryData.add(info);
        }

        ArrayList<Integer> credits = new ArrayList<>();
        ArrayList<Double> grades = new ArrayList<>();

        for (int i = 0; i < secondaryData.size(); i++){
            try {
                credits.add(Integer.parseInt(secondaryData.get(i)[2]));
            } catch (Exception ex){
                ex.printStackTrace();
                credits.add(0);
            }
            try {
                grades.add(Double.parseDouble(secondaryData.get(i)[1]));
            } catch (Exception ex){
                ex.printStackTrace();
                grades.add(0.0);
            }
        }

        convertGradesDown(grades);
        int creditsTotal = pastCr = getTotalCredits(credits);
        double pointsTotal = pastPn = getTotalPoints(credits, grades);
        double index = pointsTotal/(creditsTotal*1.0);
        String condition = "NORMAL";

        if (index <= 1){
            condition = "PRUEBA ACADEMICA";
        }

        String[] finalData = new String[names.length];
        finalData[0] = String.valueOf(creditsTotal);
        finalData[1] = String.format("%.2f", pointsTotal);
        finalData[2] = String.format("%.2f", index);
        finalData[3] = condition;
        data.add(finalData);

        Table table = new Table(4);
        table.setValues(data);
        return table.getTable();
    }

    public View createTableType_2 (String[] heading) throws JSONException {
        String[] names = {"name",
                "code",
                "section",
                "observation",
                "id",
                "grade",
                "credits",
                "cicle"};

        ArrayList<String[]> data = new ArrayList<>();
        ArrayList<String[]> secondaryData = new ArrayList<>();
        data.add(heading);

        JSONArray info = obj.getJSONArray("actuals");
        for(int i = 0; i < info.length(); i++){
            String[] values = new String[names.length];
            JSONObject obj = info.getJSONObject(i);

            switch (Integer.parseInt(obj.getString("cicle"))){
                case 0:
                    cycle_1 += Integer.parseInt(obj.getString("credits"));
                    break;
                case 1:
                    cycle_2 += Integer.parseInt(obj.getString("credits"));
                    break;
                case 2:
                    cycle_3 += Integer.parseInt(obj.getString("credits"));
                    break;
            }

            observation = obj.optString("observation");

            for(int j = 0; j < names.length; j++){
                values[j] = obj.getString(names[j]);
            }
            secondaryData.add(values);
        }

        for(int i = 0; i < secondaryData.size(); i++){
            String[] finalData = new String[heading.length];
            finalData[0] = secondaryData.get(i)[1];
            finalData[1] = secondaryData.get(i)[2];
            finalData[2] = secondaryData.get(i)[0];

            if(Double.parseDouble(secondaryData.get(i)[5]) < 0){
                finalData[3] = 0 + "";
            }else{
                finalData[3] = secondaryData.get(i)[5];
            }

            try {
                finalData[4] = institutionalAlpha(equivalentInstitutional(Double.parseDouble(secondaryData.get(i)[5])));
            }catch (Exception ex){
                finalData[4] = "";
            }
            finalData[5] = secondaryData.get(i)[6];
            try {
                finalData[6] = String.valueOf(equivalentInstitutional(Double.parseDouble(finalData[3])) * Integer.parseInt(finalData[5]));
            }catch (Exception ex){
                finalData[6] = "";
            }

            data.add(finalData);

        }

        Table table = new Table(7);
        table.setValues(data);
        return table.getTable();
    }

    public View createTableType_3 (String[] heading) throws JSONException{
        ArrayList<String[]> data = new ArrayList<>();
        data.add(heading);

        String[] names = { "id",
                "grade",
                "credits",
                "cicle"};

        JSONArray rows = obj.getJSONArray("actuals");
        ArrayList<String[]> secondaryData = new ArrayList<>();

        for(int i = 0; i < rows.length(); i++){
            String[] info = new String[names.length];
            JSONObject object = rows.getJSONObject(i);
            for(int j = 0; j < info.length; j++) {
                info[j] = object.optString(names[j]);
            }
            secondaryData.add(info);
        }

        ArrayList<Integer> credits = new ArrayList<>();
        ArrayList<Double> grades = new ArrayList<>();

        for (int i = 0; i < secondaryData.size(); i++){
            try {
                credits.add(Integer.parseInt(secondaryData.get(i)[2]));
            } catch (Exception ex){
                ex.printStackTrace();
                credits.add(0);
            }
            try {
                grades.add(Double.parseDouble(secondaryData.get(i)[1]));
            } catch (Exception ex){
                ex.printStackTrace();
                grades.add(0.0);
            }
        }

        convertGradesDown(grades);
        int creditsTotal = getTotalCredits(credits);
        double pointsTotal = getTotalPoints(credits, grades);
        double index = pointsTotal/(creditsTotal*1.0);

        String[] finalData_1 = new String[names.length];
        finalData_1[0] = "Totales del Trimestre";
        finalData_1[1] = String.valueOf(creditsTotal);
        finalData_1[2] = String.format("%.2f", pointsTotal);
        finalData_1[3] = String.format("%.2f", index);
        data.add(finalData_1);

        String[] finalData_2 = new String[names.length];
        finalData_2[0] = "Totales Acumulados";
        finalData_2[1] = String.valueOf(creditsTotal + pastCr);
        finalData_2[2] = String.format("%.2f", pointsTotal + pastPn);
        finalData_2[3] = String.format("%.2f", (pointsTotal + pastPn)/(creditsTotal + pastCr));

        totalIndex = (pointsTotal + pastPn)/(creditsTotal + pastCr);

        data.add(finalData_2);

        Table table = new Table(4, 0);
        table.setValues(data);
        return table.getTable();
    }

    public View createTableType_4 (String[] heading) throws JSONException{
        ArrayList<String[]> data = new ArrayList<>();
        data.add(heading);

        String[] finalData_1 = new String[heading.length];
        finalData_1[0] = "Ciclo Propédeutico";
        finalData_1[1] = String.valueOf(cycle_1);

        String[] finalData_2 = new String[heading.length];
        finalData_2[0] = "Ciclo Formativo";
        finalData_2[1] = String.valueOf(cycle_2);

        String[] finalData_3 = new String[heading.length];
        finalData_3[0] = "Ciclo Profesional";
        finalData_3[1] = String.valueOf(cycle_3);

        String[] total = new String[heading.length];
        total[0] = "Total";
        total[1] = String.valueOf(cycle_1 + cycle_2 + cycle_3);

        data.add(finalData_1);
        data.add(finalData_2);
        data.add(finalData_3);
        data.add(total);

        Table table = new Table(2, 0);
        table.setValues(data);
        return table.getTable();
    }

    public View createTableType_5 (String[] heading) throws JSONException{
        ArrayList<String[]> data = new ArrayList<>();
        data.add(heading);

        String condition = "NORMAL";

        if (totalIndex <= 1){
            condition = "PRUEBA ACADEMICA";
        }

        String[] finalData_1 = new String[heading.length];
        finalData_1[0] = condition;

        data.add(finalData_1);

        Table table = new Table(1);
        table.setValues(data);
        return table.getTable();
    }

    public View createTableType_6 (String[] heading) throws JSONException{
        ArrayList<String[]> data = new ArrayList<>();
        data.add(heading);

        String[] finalData_1 = new String[heading.length];
        finalData_1[0] = observation;

        data.add(finalData_1);

        Table table = new Table(1);
        table.setValues(data);
        return table.getTable();
    }

    private int lab = 0;
    private int teo = 0;
    private int cre = 0;

    public View createTableType_7 (String[] heading) throws JSONException {
        String[] names = {"name",
                "code",
                "section",
                "midG",
                "baseG",
                "credits",
                "teacher"
        };

        ArrayList<String[]> data = new ArrayList<>();
        ArrayList<String[]> secondaryData = new ArrayList<>();
        data.add(heading);

        JSONArray info = obj.getJSONArray("actuals");
        for(int i = 0; i < info.length(); i++){
            String[] values = new String[names.length];
            JSONObject obj = info.getJSONObject(i);

            if(obj.getString("type").equals("T")){
                teo++;
            }else{
                lab++;
            }

            for(int j = 0; j < names.length; j++){
                values[j] = obj.getString(names[j]);
            }
            secondaryData.add(values);
        }

        for(int i = 0; i < secondaryData.size(); i++){
            String[] finalData = new String[heading.length];

            finalData[0] = secondaryData.get(i)[1];
            finalData[1] = secondaryData.get(i)[2];
            finalData[2] = secondaryData.get(i)[0];
            finalData[3] = secondaryData.get(i)[6];
            finalData[4] = secondaryData.get(i)[5];
            finalData[5] = secondaryData.get(i)[4];
            finalData[6] = secondaryData.get(i)[3];

            data.add(finalData);

            try{
                cre += Integer.parseInt(finalData[4]);
            }catch (Exception ex){
                cre += 0;
            }

        }

        Table table = new Table(7);
        table.setValues(data);
        return table.getTable();
    }

    public View createTableType_8 (String[] heading) throws JSONException {
        ArrayList<String[]> data = new ArrayList<>();
        data.add(heading);

        data.add(new String[]{"Asignaturas de Teoría", String.valueOf(teo)});
        data.add(new String[]{"Laboratorios", String.valueOf(lab)});
        data.add(new String[]{"Total Asignaturas", String.valueOf(teo + lab)});
        data.add(new String[]{"Total Créditos", String.valueOf(cre)});

        Table table = new Table(2, 0);
        table.setValues(data);
        return table.getTable();
    }

    private class Table{
        private int columns;
        private int heading_2_index = -1;
        ArrayList<String[]> table = new ArrayList<>();

        Table(int columns, int heading_2_index){
            this.columns = columns;
            this.heading_2_index = heading_2_index;
        }

        Table(int columns){
            this.columns = columns;
        }

        void setValues(ArrayList<String[]> table){
            this.table = table;
        }

        View getTable(){

            View layout = inflater.inflate(R.layout.info_builder_table, null);
            LinearLayout table = (LinearLayout)layout.findViewById(R.id.table);

            for(int i = 0; i < this.table.size(); i++){
                TableRow row = new TableRow(context);
                row.setDividerDrawable(context.getResources().getDrawable(R.drawable.divider_2));
                row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE|LinearLayout.SHOW_DIVIDER_END|LinearLayout.SHOW_DIVIDER_BEGINNING);
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView;
                if(i == 0){
                    for(int j = 0; j < this.table.get(i).length; j++) {
                        textView = createHeading(this.table.get(i)[j]);
                        row.addView(textView);
                    }
                }
                else{
                    for(int j = 0; j < this.table.get(i).length; j++) {
                        if(j == heading_2_index){
                            textView = createHeading(this.table.get(i)[j]);
                            row.addView(textView);
                        }else{
                            textView = createValue(this.table.get(i)[j]);
                            row.addView(textView);
                        }
                    }
                }
                table.addView(row);
            }

            return layout;
        }

        private TextView createHeading(String text){
            TextView heading = (TextView) inflater.inflate(R.layout.info_builder_title_field_heading, null);
            heading.setText(text);
            return heading;
        }

        private TextView createValue(String text){
            TextView value = (TextView) inflater.inflate(R.layout.info_builder_title_field_value, null);
            value.setText(text);
            value.setGravity(CENTER_GRAVITY);
            return value;
        }
    }

    private class Heading{
        private String text;

        Heading(String text){
            this.text = text;
        }

        View getHeading(){
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.info_builder_heading, null);
            ((TextView) layout.findViewById(R.id.heading)).setText(text);
            return layout;
        }

    }

    private class Title {
        private int columnsCount;
        private int rowsCount;
        private int headIndex;
        private ArrayList<String[]> info = new ArrayList<>();

        public Title(int columns, int headIndex) {
            this.columnsCount = columns;
            this.headIndex = headIndex;
        }

        public int getColumns() {
            return columnsCount;
        }

        public void setColumns(int columns) {
            this.columnsCount = columns;
        }

        public int getHeadIndex() {
            return headIndex;
        }

        public void setHeadIndex(int headIndex) {
            this.headIndex = headIndex;
        }

        public int getRows() {
            return rowsCount;
        }

        public void setRows(int rows) {
            this.rowsCount = rows;
        }

        public void addField(String[] title_value) throws Exception {
            if(title_value.length != columnsCount){
                throw new Exception("The input data most have the same amount of columns elected");
            }
            info.add(title_value);
            rowsCount++;
        }

        void addFields(ArrayList<String[]> fields){
            info = fields;
        }

        public View getTitle(){
            HorizontalScrollView main = (HorizontalScrollView) inflater.inflate(R.layout.info_builder_title, null);
            TableLayout layout = (TableLayout) main.findViewById(R.id.main_view);

            for(String[] values : info){
                TableRow field = new TableRow(context);
                field.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                field.setDividerDrawable(context.getResources().getDrawable(R.drawable.divider_2));

                for (int i = 0; i < columnsCount; i++){
                    if(i == headIndex){
                        field.addView(createHeading(values[i]));
                    }
                    else {
                        field.addView(createValue(values[i]));
                    }
                }
                layout.addView(field, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            }

            return main;
        }

        private TextView createHeading(String text){
            TextView heading = (TextView) inflater.inflate(R.layout.info_builder_title_field_heading, null);
            heading.setText(text);
            heading.setPadding(0, 0, 10, 0);
            return heading;
        }

        private TextView createValue(String text){
            TextView value = (TextView) inflater.inflate(R.layout.info_builder_title_field_value, null);
            value.setText(text);
            value.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return value;
        }
    }

    private double equivalentInstitutional(Double realVal){

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
        else if (realVal >= 0)
            realVal = 0.0;
        else if (realVal < 0)
            realVal = -1.0;

        return realVal;
    }

    private void convertGradesDown(ArrayList<Double> main){
        for(int i = 0; i < main.size(); i++){
            main.set(i, equivalentInstitutional(main.get(i)));
        }
    }

    private int getTotalCredits(ArrayList<Integer> creditsList) {

        int total = 0;

        for(Integer credit : creditsList){
            total = total + credit;
        }
        return total;
    }

    private double getTotalPoints(ArrayList<Integer> creditsList, ArrayList<Double> gradesList){
        double total = 0;

        for (int i = 0; i < creditsList.size(); i++){
            total = total + (creditsList.get(i) * gradesList.get(i));
        }

        return total;
    }

    private String institutionalAlpha(Double institutionalIndex){

        String returner = "F";

        if (institutionalIndex >= 4.0){
            returner = "A";
        }else if(institutionalIndex >= 3.5){
            returner = "B+";
        }else if(institutionalIndex >= 3.0){
            returner = "B";
        }else if(institutionalIndex >= 2.5){
            returner = "C+";
        }else if(institutionalIndex >= 2.0){
            returner = "C";
        }else if(institutionalIndex >= 1){
            returner = "D";
        }else if(institutionalIndex >= 0){
            returner = "F";
        }else if(institutionalIndex < 0){
            returner = "R";
        }

        return returner;
    }
}
