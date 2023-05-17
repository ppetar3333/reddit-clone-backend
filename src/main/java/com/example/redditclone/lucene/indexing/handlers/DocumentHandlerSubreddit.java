package com.example.redditclone.lucene.indexing.handlers;

import com.example.redditclone.models.PostElastic;
import com.example.redditclone.models.SubredditElastic;

import java.io.File;

public abstract class DocumentHandlerSubreddit {

    /**
     * Od prosledjene datoteke se konstruise Lucene Document
     *
     * @param file
     *            datoteka u kojoj se nalaze informacije
     * @return Lucene Document
     */
    public abstract SubredditElastic getIndexUnit(File file);
    public abstract String getText(File file);
}
