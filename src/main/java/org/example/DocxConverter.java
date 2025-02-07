package org.example;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class DocxConverter {

    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) {
        String[] filePaths = {
                "Project Manager-85825Y1023.docx",
                "Application Developer.docx",
        };

        // Ensure output directory exists
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        for (String filePath : filePaths) {
            System.out.println("Processing file: " + filePath);
            try (InputStream stream = new FileInputStream(new File(filePath))) {
                String baseName = getBaseName(filePath);

                printMetadata(stream);

                convertToText(filePath, Paths.get(OUTPUT_DIR, baseName + ".txt").toString());

                convertToHTML(filePath, Paths.get(OUTPUT_DIR, baseName + ".html").toString());

                convertToPDF(filePath, Paths.get(OUTPUT_DIR, baseName + ".pdf").toString());
                Tika tika = new Tika();
                String content = tika.parseToString(new File(filePath)); // Get content of the file
//                Metadata metadata = new Metadata();
//                String payload = createPayload(metadata, content);
//                sendPostRequest(API_URL, payload);
            } catch (IOException | TikaException | SAXException e) {
                System.err.println("Error processing file " + filePath + ": " + e.getMessage());
            }
        }
    }

    private static void printMetadata(InputStream stream) throws IOException, TikaException, SAXException {
        Metadata metadata = new Metadata();
        ContentHandler handler = new BodyContentHandler();
        ParseContext context = new ParseContext();
        Parser parser = new AutoDetectParser();

        parser.parse(stream, handler, metadata, context);

        System.out.println("Metadata Names:");
        for (String name : metadata.names()) {
            System.out.println(name + ": " + metadata.get(name));
        }
        System.out.println();
    }

    private static void convertToText(String filePath, String outputPath) throws IOException, TikaException {
        Tika tika = new Tika();
        String content = tika.parseToString(new File(filePath));
        Files.write(Paths.get(outputPath), content.getBytes());
        System.out.println("Text file created: " + outputPath);
    }

    private static void convertToHTML(String filePath, String outputPath) throws IOException, TikaException, SAXException {
        ContentHandler handler = new ToHTMLContentHandler();
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
        ParseContext context = new ParseContext();

        try (InputStream stream = new FileInputStream(new File(filePath))) {
            parser.parse(stream, handler, metadata, context);
        }

        Files.write(Paths.get(outputPath), handler.toString().getBytes());
        System.out.println("HTML file created: " + outputPath);
    }
    private static void convertToPDF(String filePath, String outputPath) throws IOException, TikaException {
        // Extract content using Tika
        Tika tika = new Tika();
        String content = tika.parseToString(new File(filePath));

        // Use PDFBox to create a PDF
        try (PDDocument pdfDocument = new PDDocument()) {
            PDPage page = new PDPage();
            pdfDocument.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(50, 750);

                String[] lines = content.split("\n");
                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
                contentStream.endText();
            }

            pdfDocument.save(outputPath);
            System.out.println("PDF file created: " + outputPath);
        }
    }

    private static String createPayload(Metadata metadata, String content) {
        StringBuilder payload = new StringBuilder();
        payload.append("{");
        payload.append("\"metadata\":{");
        for (String name : metadata.names()) {
            payload.append("\"").append(name).append("\":\"").append(metadata.get(name)).append("\",");
        }
        payload.deleteCharAt(payload.length() - 1);
        payload.append("},");
        payload.append("\"content\":\"").append(content.replace("\n", "\\n").replace("\"", "\\\"")).append("\"");
        payload.append("}");
        return payload.toString();
    }


    private static String getBaseName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.lastIndexOf("."));
    }
}
