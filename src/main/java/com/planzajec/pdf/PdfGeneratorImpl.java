package com.planzajec.pdf;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.planzajec.entity.CzasTrwaniaLekcji;
import com.planzajec.entity.GodzinaLekcyjna;
import com.planzajec.entity.enums.Dni;
import com.planzajec.services.PlanZajec;

import java.io.IOException;



public class PdfGeneratorImpl implements PdfGenerator{

    private PdfFont normalTextFont;

    {
        try {
            FontProgram fontProgram = FontProgramFactory.createFont();
            normalTextFont = PdfFontFactory.createFont(fontProgram, "Cp1257");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawTable(final String PDF_PATH, PlanZajec planZajec) throws Exception {

        PdfWriter writer = new PdfWriter(PDF_PATH);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        float[] columnWidth = {6, 11, 17, 17, 17, 17, 17};
        Table table = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();

        createHeader(table, planZajec.getNazwa());
        createFirstRow(table);
        createMainBody(table, planZajec);

        table.setBorder(new SolidBorder(1f));
        doc.add(table);
        doc.close();
    }

    private void createHeader(Table table, String nazwa) {
        table.addCell(new Cell(1, 7)
                .add(new Paragraph("Plan zajęć - " + nazwa))
                .setFont(normalTextFont)
                .setFontSize(17)
                .setBold()
                .setHeight(30)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorderTop(new SolidBorder(7f))
        );
    }


    private void createFirstRow(Table table) {

        table.addCell(createFirstRowCell("Lp. "));
        table.addCell(createFirstRowCell("Godzina: "));
        for (Dni dzien : Dni.values()) {
            table.addCell(createFirstRowCell(dzien.name().toUpperCase()));
        }
    }


    private void createMainBody(Table table, PlanZajec planZajec) {
        int i = 0;
        GodzinaLekcyjna godzina;
        String freeSpace;
        for (CzasTrwaniaLekcji czasTrwaniaLekcji : planZajec.getCzasTrwaniaWszystkichLekcji()) {
            table.addCell(createMainBodyCell("",czasTrwaniaLekcji.getNumer() + ""));
            table.addCell(createMainBodyCell("",czasTrwaniaLekcji.getCzasRozpoczecia() + " - " + czasTrwaniaLekcji.getCzasZakonczenia()));

            for (Dni dzien : Dni.values()) {
                godzina = planZajec.getPlan().get(dzien.name()).get(i);
                if ((godzina != null) && (godzina.getCzasTrwaniaLekcji().getNumer().equals(czasTrwaniaLekcji.getNumer()))) {
                    if (godzina.getPrzedmiot().getNazwa().toCharArray().length > 15)
                        freeSpace = "\n";
                    else freeSpace = "\n\n";

                    table.addCell(createMainBodyCell(godzina.getPrzedmiot().getNazwa(),  freeSpace + "Sala: " + godzina.getSala().getNumer() + "\n" +
                            (planZajec.getNazwa().matches("^Klasa: .*") ? (godzina.getNauczyciel().getImie().substring(0, 1) + ". " + godzina.getNauczyciel().getNazwisko()) : godzina.getKlasa().getNazwa())
                    ));
                } else {
                    table.addCell(createMainBodyCell("",""));
                }
            }

            i++;
        }
    }


    private Cell createMainBodyCell(String boldedText, String sentence) {
        Text boldText = new Text(boldedText).setBold();
        return new Cell()
                .add(new Paragraph(boldText).add(sentence))
                .setFont(normalTextFont)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHeight(50);
    }

    private Cell createFirstRowCell( String sentence) {
        return new Cell()
                .add(new Paragraph(sentence))
                .setFont(normalTextFont)
                .setFontSize(9)
                .setBold()
                .setHeight(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

    }

}
