package com.idea.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JsonTable {
    private String tableName;
    private Integer level;
    private List<Column> insert;
    private List<Column> update;
    private List<Column> delete;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Column{
        private String columnName;
    }
}
