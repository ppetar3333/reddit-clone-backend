package com.example.redditclone.service;

import com.example.redditclone.lucene.search.QueryBuilderCustom;
import com.example.redditclone.util.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import com.example.redditclone.web.dto.SimpleQueryEs;

public class SearchQueryGenerator {

    public static QueryBuilder createMatchQueryBuilder(SimpleQueryEs simpleQueryEs) {
        if(simpleQueryEs.getValue().startsWith("\"") && simpleQueryEs.getValue().endsWith("\"")) {
            return QueryBuilderCustom.buildQuery(SearchType.PHRASE, simpleQueryEs.getField(), simpleQueryEs.getValue());
        } else {
            return QueryBuilderCustom.buildQuery(SearchType.MATCH, simpleQueryEs.getField(), simpleQueryEs.getValue());
        }
    }

    public static QueryBuilder createRangeQueryBuilder(SimpleQueryEs simpleQueryEs) {
        return QueryBuilderCustom.buildQuery(SearchType.RANGE, simpleQueryEs.getField(), simpleQueryEs.getValue());
    }

}
