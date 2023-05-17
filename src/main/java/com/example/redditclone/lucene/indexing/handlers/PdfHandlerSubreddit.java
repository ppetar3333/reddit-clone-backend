package com.example.redditclone.lucene.indexing.handlers;

import com.example.redditclone.models.PostElastic;
import com.example.redditclone.models.SubredditElastic;
import org.apache.lucene.document.DateTools;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PdfHandlerSubreddit extends DocumentHandlerSubreddit {

    @Override
    public SubredditElastic getIndexUnit(File file) {
        SubredditElastic retVal = new SubredditElastic();
        try {
            PDFParser parser = new PDFParser((RandomAccessRead) new RandomAccessFile(file, "r"));
            parser.parse();
            String text = getText(parser);
            retVal.setDescription(text);

            // metadata extraction
            PDDocument pdf = parser.getPDDocument();
            PDDocumentInformation info = pdf.getDocumentInformation();

            String title = ""+info.getTitle();
            retVal.setName(title);

            String keywords = ""+info.getKeywords();
            retVal.setKeywords(keywords);

            retVal.setFilename(file.getCanonicalPath());

            String modificationDate= DateTools.dateToString(new Date(file.lastModified()), DateTools.Resolution.DAY);

            pdf.close();
        } catch (IOException e) {
            System.out.println("Greksa pri konvertovanju dokumenta u pdf");
        }

        return retVal;
    }

    @Override
    public String getText(File file) {
        try {
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
            parser.parse();
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(parser.getPDDocument());
        } catch (IOException e) {
            System.out.println("Greksa pri konvertovanju dokumenta u pdf");
        }
        return null;
    }

    public String getText(PDFParser parser) {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(parser.getPDDocument());
        } catch (IOException e) {
            System.out.println("Greksa pri konvertovanju dokumenta u pdf");
        }
        return null;
    }
}
