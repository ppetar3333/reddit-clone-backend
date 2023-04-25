package com.example.redditclone.web.dto.mapper;

import com.example.redditclone.models.Reader;
import com.example.redditclone.web.dto.ReaderDTO;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;


public class ReaderMapper {
    public static List<ReaderDTO> mapDtos(SearchHits<Reader> searchHits) {
        return searchHits
                .map(reader -> mapDto(reader.getContent()))
                .toList();
    }

    public static ReaderDTO mapDto(Reader reader) {
        return ReaderDTO.builder()
                .name(reader.getName())
                .lastName(reader.getLastName())
                .address(reader.getAddress())
                .build();
    }
}
