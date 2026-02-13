package com.idea.ad.dto;

import com.idea.ad.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MysqlRowData {
    private String tableName;
    private String level;
    private OpType opType;
    private List<Map<String,String>> fieldValue = new ArrayList<>();
}
