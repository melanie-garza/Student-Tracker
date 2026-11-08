package com.escuela.service;

import com.escuela.model.Alumno;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    class BorderEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            cb.setColorStroke(new Color(85, 40, 115));
            cb.setLineWidth(2f);
            cb.roundRectangle(20, 20, document.getPageSize().getWidth() - 40, document.getPageSize().getHeight() - 40, 15f);
            cb.stroke();
        }
    }

    class RoundedCellEvent implements PdfPCellEvent {
        private Color bgColor;
        private Color borderColor;
        
        public RoundedCellEvent(Color bgColor, Color borderColor) {
            this.bgColor = bgColor;
            this.borderColor = borderColor;
        }

        @Override
        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            PdfContentByte cb = canvases[PdfPTable.BACKGROUNDCANVAS];
            cb.saveState();
            if (bgColor != null) {
                cb.setColorFill(bgColor);
                cb.roundRectangle(position.getLeft(), position.getBottom(), position.getWidth(), position.getHeight(), 10f);
                cb.fill();
            }
            if (borderColor != null) {
                cb.setColorStroke(borderColor);
                cb.setLineWidth(1f);
                cb.roundRectangle(position.getLeft(), position.getBottom(), position.getWidth(), position.getHeight(), 10f);
                cb.stroke();
            }
            cb.restoreState();
        }
    }

    public void generarBoleta(HttpServletResponse response, Alumno alumno) throws IOException {
        Document document = new Document(PageSize.A4, 40, 40, 35, 40);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        
        writer.setPageEvent(new BorderEvent()); 
        document.open();

        Color moradoOscuro = new Color(85, 40, 115);
        Color lilaClaro = new Color(238, 226, 245); 
        Color grisFondoComentario = new Color(245, 245, 245);
        Color grisTexto = new Color(70, 70, 70);
        Color grisFecha = new Color(110, 110, 110); 

        Font fontTitulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 28, moradoOscuro);
        Font fontInfoLabel = FontFactory.getFont(FontFactory.TIMES_BOLD, 13, grisTexto);
        Font fontInfoValue = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, grisTexto);
        Font fontDateLabel = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 13, grisFecha);
        Font fontDateValue = FontFactory.getFont(FontFactory.TIMES_ITALIC, 13, grisFecha);

        Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Color.WHITE);
        Font fontTexto = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Color.BLACK);
        Font fontSubtitulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 15, Color.BLACK);
        
        Font fontFinalGradeText = FontFactory.getFont(FontFactory.TIMES_BOLD, 24, Color.WHITE);
        Font fontBigLetter = FontFactory.getFont(FontFactory.TIMES_BOLD, 55, moradoOscuro);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1.5f, 3.5f}); 
        headerTable.setSpacingAfter(30f); 
        
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        try {
            Image logo = Image.getInstance("src/main/resources/static/logo.png");
            logo.scaleToFit(140, 140);
            logoCell.addElement(logo);
        } catch (Exception e) {
            logoCell.addElement(new Paragraph("ALPHA", fontTitulo)); 
        }
        headerTable.addCell(logoCell);

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        infoCell.setPaddingLeft(15f);

        Paragraph titulo = new Paragraph("PROGRESS REPORT", fontTitulo);
        titulo.setSpacingBefore(0f); 
        titulo.setSpacingAfter(15f);
        infoCell.addElement(titulo);

        Paragraph pName = new Paragraph();
        pName.add(new Chunk("Student: ", fontInfoLabel));
        pName.add(new Chunk(alumno.getNombre(), fontInfoValue));
        pName.setSpacingAfter(5f);

        Paragraph pLevel = new Paragraph();
        pLevel.add(new Chunk("Start up level: ", fontInfoLabel));
        pLevel.add(new Chunk(alumno.getGrupo(), fontInfoValue));
        pLevel.setSpacingAfter(5f);

        Paragraph pTerm = new Paragraph();
        pTerm.add(new Chunk("Term: ", fontInfoLabel));
        pTerm.add(new Chunk(alumno.getTerm() != null ? alumno.getTerm() : "N/A", fontInfoValue));
        pTerm.setSpacingAfter(5f);

        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        Paragraph pDate = new Paragraph();
        pDate.add(new Chunk("Date of issue: ", fontDateLabel));
        pDate.add(new Chunk(fechaActual, fontDateValue));
        pDate.setSpacingAfter(5f);
        
        infoCell.addElement(pName);
        infoCell.addElement(pLevel);
        infoCell.addElement(pTerm);
        infoCell.addElement(pDate);

        headerTable.addCell(infoCell);
        document.add(headerTable);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4f, 2f, 2f}); 
        
        PdfPCell c1 = new PdfPCell(new Phrase("Criteria", fontHeader)); c1.setBackgroundColor(moradoOscuro); c1.setPadding(12f); c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell c2 = new PdfPCell(new Phrase("Weight", fontHeader)); c2.setBackgroundColor(moradoOscuro); c2.setPadding(12f); c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell c3 = new PdfPCell(new Phrase("Score", fontHeader)); c3.setBackgroundColor(moradoOscuro); c3.setPadding(12f); c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1); table.addCell(c2); table.addCell(c3);

        addTableRow(table, "Attendance", "20%", String.valueOf(alumno.getAsistencia()), lilaClaro, fontTexto);
        addTableRow(table, "Mid-term Project", "15%", String.valueOf(alumno.getMidTermProject()), Color.WHITE, fontTexto);
        addTableRow(table, "Mid-term Exam", "15%", String.valueOf(alumno.getMidTermExam()), lilaClaro, fontTexto);
        addTableRow(table, "Final Project", "15%", String.valueOf(alumno.getFinalProject()), Color.WHITE, fontTexto);
        addTableRow(table, "Final Exam", "15%", String.valueOf(alumno.getFinalExam()), lilaClaro, fontTexto);
        addTableRow(table, "Oral Exam", "20%", String.valueOf(alumno.getOralExam()), Color.WHITE, fontTexto);
        document.add(table);
        
        document.add(Chunk.NEWLINE);

        Paragraph commentTitle = new Paragraph("Teacher's comments:", fontSubtitulo);
        commentTitle.setSpacingBefore(15f);
        document.add(commentTitle);
        
        PdfPCell commentCell = new PdfPCell(new Phrase(alumno.getComentarios() != null && !alumno.getComentarios().isEmpty() ? alumno.getComentarios() : "No additional comments.", fontTexto));
        commentCell.setPadding(25f);
        commentCell.setBorder(Rectangle.NO_BORDER); 
        commentCell.setCellEvent(new RoundedCellEvent(grisFondoComentario, Color.LIGHT_GRAY)); 
        
        PdfPTable commentTable = new PdfPTable(1);
        commentTable.setWidthPercentage(100);
        commentTable.setSpacingBefore(15f);
        commentTable.setSpacingAfter(45f); 
        commentTable.addCell(commentCell);
        document.add(commentTable);

        PdfPTable gradeTable = new PdfPTable(2);
        gradeTable.setWidthPercentage(75); 
        gradeTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        gradeTable.setWidths(new float[]{3.5f, 1f});

        PdfPCell numCell = new PdfPCell(new Phrase("FINAL GRADE: " + Math.round(alumno.getCalificacionFinal()), fontFinalGradeText));
        numCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        numCell.setPadding(20f);
        numCell.setBorder(Rectangle.NO_BORDER);
        numCell.setCellEvent(new RoundedCellEvent(moradoOscuro, moradoOscuro)); 

        PdfPCell letterCell = new PdfPCell(new Phrase(alumno.getLetraAmericana(), fontBigLetter));
        letterCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        letterCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        letterCell.setBorder(Rectangle.NO_BORDER);

        gradeTable.addCell(numCell);
        gradeTable.addCell(letterCell);
        document.add(gradeTable);

        document.close();
    }

    private void addTableRow(PdfPTable table, String col1, String col2, String col3, Color bgColor, Font font) {
        PdfPCell cell1 = new PdfPCell(new Phrase(col1, font)); cell1.setBackgroundColor(bgColor); cell1.setPadding(10f); cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell2 = new PdfPCell(new Phrase(col2, font)); cell2.setBackgroundColor(bgColor); cell2.setPadding(10f); cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell3 = new PdfPCell(new Phrase(col3 != null && !col3.equals("null") ? col3 : "0", font)); cell3.setBackgroundColor(bgColor); cell3.setPadding(10f); cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell1); table.addCell(cell2); table.addCell(cell3);
    }
}