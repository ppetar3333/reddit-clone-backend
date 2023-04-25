package com.example.redditclone.lucene.indexing.handlers;

import com.example.redditclone.models.Post;
import com.example.redditclone.models.PostElastic;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class WordHandler extends DocumentHandler {

	public PostElastic getIndexUnit(File file) {
		PostElastic retVal = new PostElastic();
		InputStream is;

		try {
			is = new FileInputStream(file);
			WordExtractor we = new WordExtractor(is);
			String text = we.getText();
			retVal.setText(text);
			
			SummaryInformation si = we.getSummaryInformation();
			String title = si.getTitle();
			retVal.setTitle(title);

			String keywords = si.getKeywords();
			retVal.setKeywords(keywords);
			
			retVal.setFilename(file.getCanonicalPath());
			
			we.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Dokument ne postoji");
		} catch (Exception e) {
			System.out.println("Problem pri parsiranju doc fajla");
		}

		return retVal;
	}

	@Override
	public String getText(File file) {
		String text = null;
		try {
			WordExtractor we = new WordExtractor(new FileInputStream(file));
			text = we.getText();
			we.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Dokument ne postoji");
		} catch (Exception e) {
			System.out.println("Problem pri parsiranju doc fajla");
		}
		return text;
	}

}
