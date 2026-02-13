package com.idea.ad.index;

import com.alibaba.fastjson.JSON;
import com.idea.ad.dump.DConstant;
import com.idea.ad.dump.table.*;
import com.idea.ad.handler.AdLevelDataHandler;
import com.idea.ad.constant.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@DependsOn("dataTable")
public class IndexFileLoader {
    @PostConstruct
    public void init() {
        List<String> adPlanStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN)
        );
        adPlanStrings.forEach(s -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(s, AdPlanTable.class),
                OpType.ADD
        ));
        List<String> adCreativeStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE)
        );
        adCreativeStrings.forEach(s -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(s, AdCreativeTable.class),
                OpType.ADD
        ));
        List<String> adUnitStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT)
        );
        adUnitStrings.forEach(s -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(s, AdUnitTable.class),
                OpType.ADD
        ));
        List<String> adCreativeUnitStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT)
        );
        adCreativeUnitStrings.forEach(s -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(s, AdCreativeUnitTable.class),
                OpType.ADD
        ));
        List<String> adUnitDistrictStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT)
        );
        adUnitDistrictStrings.forEach(s -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(s, AdUnitDistrictTable.class),
                OpType.ADD
        ));
        List<String> adUnitItStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT)
        );
        adUnitItStrings.forEach(s -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(s, AdUnitItTable.class),
                OpType.ADD
        ));
        List<String> adUnitKeywordStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD)
        );
        adUnitKeywordStrings.forEach(s -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(s, AdUnitKeywordTable.class),
                OpType.ADD
        ));
    }
    private List<String> loadDumpData(String fileName){
        try(BufferedReader bf = Files.newBufferedReader(Paths.get(fileName))){
            return bf.lines().collect(Collectors.toList());
        }catch (IOException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
