package com.paellasoft.CRUD.mail;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.paellasoft.CRUD.repository.custom.ByteArrayInputSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class EmailSender {
    private static final int LINES_PER_PAGE = 70;
    @Autowired
    private final JavaMailSender javaMailSender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendEmailWithPdfAttachment(String to, String subject, String text,String signatureImagePath) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            String title = "Resumen Boletín Oficial del Estado (" + LocalDate.now() + ")";
            byte[] pdfBytes = createPdfFromText(text, title,signatureImagePath);

            InputStreamSource pdfResource = new ByteArrayResource(pdfBytes);

            helper.setText("Estimado usuario,\n\nSe adjunta el/los resumen/es del BOE en formato PDF.\n\nAtentamente,\nEquipo de BoeApiSummary");

            helper.addAttachment("resumen_boe_"+LocalDate.now()+".pdf", pdfResource, "application/pdf");

            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            // Manejo de errores al enviar el correo electrónico
        }
    }

    public byte[] createPdfFromText(String text, String title,String signatureImagePath) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Añadir título
            document.add(new Paragraph(title).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(18));

            SolidLine line = new SolidLine(1f);
            LineSeparator ls = new LineSeparator(line);
            document.add(ls);
            // Añadir contenido
            document.add(new Paragraph(text).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)).setFontSize(12));
            document.add(ls);

            document.add(new Paragraph("\n")); // Párrafo vacío

            if (signatureImagePath != null && !signatureImagePath.isEmpty()) {
                Image signatureImage = new Image(ImageDataFactory.create(signatureImagePath))
                        .setWidth(200)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(signatureImage);
            }

            document.showTextAligned(Integer.toString(pdf.getNumberOfPages()), pdf.getDefaultPageSize().getWidth() / 2,
                    20, TextAlignment.LEFT);

        }

        return outputStream.toByteArray();
    }




    private List<String> splitLine(String line, float maxWidth) throws IOException {
        List<String> subLines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        // Dividir la línea en palabras y ajustarlas para que se ajusten dentro del ancho máximo
        String[] words = line.split("\\s+");
        for (String word : words) {
            float currentWidth = PDType1Font.HELVETICA.getStringWidth(currentLine.toString() + word) / 1000 * 12;
            if (currentWidth <= maxWidth) {
                // Si la palabra cabe dentro de la línea actual, agregarla
                currentLine.append(word).append(" ");
            } else {
                // Si la palabra excede el ancho máximo, agregar la línea actual a la lista y comenzar una nueva línea
                subLines.add(currentLine.toString());
                currentLine = new StringBuilder(word + " ");
            }
        }

        // Agregar la última línea al resultado
        subLines.add(currentLine.toString());

        return subLines;
    }


}
