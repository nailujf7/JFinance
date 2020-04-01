package util;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Payment;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * PDFCreator class to export ledger and payment data
 */
public class PDFCreator {
    private static List<Payment> payments;
    private static Database database = Database.getDatabase();

    /**
     * Creates PDF file
     */
    public static void createPDF() {
        Document document = new Document();
        try {
            File selectedFile = Util.showFileChooser();
            if (selectedFile != null) {
                //Create PDF file
                File file = new File(selectedFile.getPath());
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                // Fonts for header and table entries
                Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
                Font boldTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
                document.open();

                Paragraph accountName = new Paragraph(database.getAccount().getFirstname() + " " + database.getAccount().getLastname(), boldFont);
                accountName.setAlignment(Element.ALIGN_RIGHT);
                document.add(accountName);

                //Title
                Paragraph paragraph = new Paragraph("J-Finance Account Statements", boldTitle);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                //Image
                Image imagePDF = Image.getInstance(Constants.IMAGE_JFINANCE_PDF);
                //Fixed Positioning
                imagePDF.setAbsolutePosition(35f, 770f);
                //Scale to new height and new width of image
                imagePDF.scaleAbsolute(68, 54);
                document.add(imagePDF);

                //Table
                PdfPTable table = new PdfPTable(5); // 5 columns.
                table.setWidthPercentage(100); //Width 100%
                table.setSpacingBefore(10f); //Space before table
                table.setSpacingAfter(10f); //Space after table

                //Set Column widths
                float[] columnWidths = {0.6f, 1.3f, 0.95f, 2.8f, 1.0f};
                table.setWidths(columnWidths);

                //Columns header
                PdfPCell cell1 = new PdfPCell(new Paragraph("ID", boldFont));
                cell1.setBorderColor(BaseColor.BLACK);
                cell1.setPaddingLeft(10);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell2 = new PdfPCell(new Paragraph("NAME", boldFont));
                cell2.setBorderColor(BaseColor.BLACK);
                cell2.setPaddingLeft(10);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell3 = new PdfPCell(new Paragraph("DATE", boldFont));
                cell3.setBorderColor(BaseColor.BLACK);
                cell3.setPaddingLeft(10);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell4 = new PdfPCell(new Paragraph("INFORMATION", boldFont));
                cell4.setBorderColor(BaseColor.BLACK);
                cell4.setPaddingLeft(10);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell cell5 = new PdfPCell(new Paragraph("AMOUNT", boldFont));
                cell5.setBorderColor(BaseColor.BLACK);
                cell5.setPaddingLeft(10);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);

                // Table payment entries
                if (Util.isAccountPayments()) {
                    payments = database.getAccountPayments();
                } else {
                    payments = database.getPaymentList();
                }
                for (Payment payment : payments) {
                    PdfPCell cellPaymentID = new PdfPCell(new Phrase((String.valueOf(payment.getPayment_id()))));
                    cellPaymentID.setPadding(6);
                    table.addCell(cellPaymentID);

                    PdfPCell cellName = new PdfPCell(new Phrase((payment.getName())));
                    cellName.setPadding(6);
                    table.addCell(cellName);

                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                    PdfPCell celDate = new PdfPCell(new Phrase(format.format(payment.getDate())));
                    celDate.setPadding(6);
                    table.addCell(celDate);

                    PdfPCell cellInformation = new PdfPCell(new Phrase((payment.getInformation())));
                    cellPaymentID.setPadding(6);
                    table.addCell(cellInformation);

                    PdfPCell cellAmount = new PdfPCell(new Phrase(payment.getAmount() + " €"));
                    cellAmount.setPadding(6);
                    cellAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cellAmount);
                }

                // Total balance
                PdfPCell cell6 = new PdfPCell();
                cell6.setPadding(6);
                table.addCell("");

                PdfPCell cell7 = new PdfPCell();
                cell7.setPadding(6);
                table.addCell("");

                PdfPCell cell8 = new PdfPCell();
                cell8.setPadding(6);
                table.addCell("");

                PdfPCell cell9 = new PdfPCell();
                cell9.setPadding(6);
                table.addCell("");

                Phrase phrase = new Phrase("", boldFont);
                if (Util.isAccountPayments()) {
                    phrase.add(database.getSumAmountAll() + " €");
                } else {
                    phrase.add(database.getSumAmount() + " €");

                }
                PdfPCell cellTotalBalance = new PdfPCell(phrase);
                cellTotalBalance.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellTotalBalance.setPadding(6);
                table.addCell(cellTotalBalance);

                document.add(table);
                document.close();
                writer.close();
                Desktop.getDesktop().open(new File(selectedFile.getPath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
