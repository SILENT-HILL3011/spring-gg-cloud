package com.idea.ad.dto;

import com.idea.ad.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Data
public class ParseTemplate {
    private String database;
    private Map<String,TableTemplate> tableTemplateMap = new HashMap<>();
    public static ParseTemplate parse(Template template){
        ParseTemplate parseTemplate = new ParseTemplate();
        parseTemplate.setDatabase(template.getDataBases());
        for (JsonTable table : template.getTableList()) {
            String tableName = table.getTableName();
            Integer level = table.getLevel();
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(tableName);
            tableTemplate.setLevel(level.toString());
            parseTemplate.tableTemplateMap.put(tableName,tableTemplate);
            // 处理各个操作类型对应的字段
            Map<OpType, List<String>> opTypeFieldSetMap = tableTemplate.getOpTypeFieldSetMap();
            for (JsonTable.Column column : table.getInsert()) {
                getAndCreateIfNeed(
                        OpType.ADD,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumnName());
            }
            for (JsonTable.Column column : table.getUpdate()) {
                getAndCreateIfNeed(
                        OpType.UPDATE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumnName());
            }
            for (JsonTable.Column column : table.getDelete()) {
                getAndCreateIfNeed(
                        OpType.DELETE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumnName());
            }
        }
        return parseTemplate;

    }
    private static <T,R> R getAndCreateIfNeed(T key, Map<T,R> map, Supplier<R> factory){
        return map.computeIfAbsent(key, k->factory.get());
    }
}
