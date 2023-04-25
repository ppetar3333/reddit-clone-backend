package com.example.redditclone.lucene.indexing.handlers;

import com.example.redditclone.models.Post;
import com.example.redditclone.models.PostElastic;

import java.io.File;

public abstract class DocumentHandler {
	/**
	 * Od prosledjene datoteke se konstruise Lucene Document
	 * 
	 * @param file
	 *            datoteka u kojoj se nalaze informacije
	 * @return Lucene Document
	 */
	public abstract PostElastic getIndexUnit(File file);
	public abstract String getText(File file);

}
