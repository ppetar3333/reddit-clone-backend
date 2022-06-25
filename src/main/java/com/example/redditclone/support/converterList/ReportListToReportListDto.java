package com.example.redditclone.support.converterList;

import com.example.redditclone.models.Report;
import com.example.redditclone.support.converterOne.ReportToReportDto;
import com.example.redditclone.web.dto.ReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ReportListToReportListDto implements Converter<List<Report>, List<ReportDto>> {

    @Autowired
    private ReportToReportDto toDto;

    public ReportListToReportListDto() { }

    public List<ReportDto> convert(List<Report> source) {
        List<ReportDto> target = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Report u = (Report)var3.next();
            ReportDto dto = this.toDto.convert(u);
            target.add(dto);
        }

        return target;
    }

}
