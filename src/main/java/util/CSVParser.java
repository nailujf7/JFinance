package util;

import au.com.bytecode.opencsv.CSVReader;
import model.Payment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVParser {
    private static Database database = Database.getDatabase();
    private static List<Payment> payments;

    /**
     * Imports CSV file entries to database
     * @param file
     * @throws IOException
     * @throws ParseException
     */
    public static void importCSV(File file) throws IOException, ParseException {
        //Build reader instance
        CSVReader reader = new CSVReader(new FileReader(file), ';', '"', 1);
        //Read all rows at once
        List<String[]> allRows = reader.readAll();
        //Read CSV line by line and use the string array as you want
        payments = new ArrayList<>();
        for (String[] row : allRows) {
            String amount = row[8];
            String name = row[5];
            String information = row[4];
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(Util.parseDateToFormat((row[2])));
            payments.add(new Payment(name, Double.valueOf(amount.replace(",", ".")), new java.sql.Date(date.getTime()), information));
        }
        database.batchPaymentInsert();

    }

    /**
     * Parser for 2003 xls file
     * @return
     */
//    public static void importExcelXlsFile(File file) {
//        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            //Get the workbook instance for XLS file
//            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
//            //Get first sheet from the workbook
//            HSSFSheet sheet = workbook.getSheetAt(0);
//
//            int i = 1;
//            while (i <= sheet.getLastRowNum()) {
//                int rowNumber = i;
//                DataFormatter formatter = new DataFormatter();
//                String amount = formatter.formatCellValue(sheet.getRow(rowNumber).getCell(8));
//                String name = formatter.formatCellValue(sheet.getRow(rowNumber).getCell(5));
//                String information = formatter.formatCellValue(sheet.getRow(rowNumber).getCell(4));
//                Date date = sheet.getRow(rowNumber).getCell(2).getDateCellValue();
//                i++;
//                database.batchPaymentInsert(name, Double.valueOf(amount.replace(",", ".")), new java.sql.Date(date.getTime()), information);
//            }
//            fileInputStream.close();
//            FileOutputStream out = new FileOutputStream(file);
//            workbook.write(out);
//            out.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static List<Payment> getPayments() {
        return payments;
    }

}
