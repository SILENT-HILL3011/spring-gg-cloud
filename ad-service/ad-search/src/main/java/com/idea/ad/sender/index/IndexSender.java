package com.idea.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.idea.ad.client.vo.AdPlan;
import com.idea.ad.dump.table.*;
import com.idea.ad.handler.AdLevelDataHandler;
import com.idea.ad.index.DataLevel;
import com.idea.ad.constant.Constant;
import com.idea.ad.dto.MysqlRowData;
import com.idea.ad.sender.ISender;
import com.idea.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IndexSender implements ISender {
    @Override
    public void sender(MysqlRowData mysqlRowData) {
        String level = mysqlRowData.getLevel();
        if (DataLevel.LEVEL2.getLevel().equals(level)){
            level2RowData(mysqlRowData);
        }else if (DataLevel.LEVEL3.getLevel().equals(level)){
            level3RowData(mysqlRowData);
        }else if (DataLevel.LEVEL4.getLevel().equals(level)){
            level4RowData(mysqlRowData);
        }else {
            log.error("MysqlRowData level error: {}", JSON.toJSONString(mysqlRowData));
        }
    }
    private void level2RowData(MysqlRowData mysqlRowData){
        if (mysqlRowData.getTableName().equals(Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)){
            List<AdPlanTable> adPlanTables = new ArrayList<>();
            for (Map<String, String> map : mysqlRowData.getFieldValue()) {
                AdPlanTable planTable = new AdPlanTable();
                map.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            planTable.setId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            planTable.setUserId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            planTable.setPlanStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            planTable.setStartDate(CommonUtils.parseStringDate(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            planTable.setEndDate(CommonUtils.parseStringDate(v));
                            break;
                    }
                });
                adPlanTables.add(planTable);
            }
            adPlanTables.forEach(p-> AdLevelDataHandler.handleLevel2(p,mysqlRowData.getOpType()));

        }else if (mysqlRowData.getTableName().equals(Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)){
            List<AdCreativeTable> adCreativeTables = new ArrayList<>();
            for (Map<String, String> map : mysqlRowData.getFieldValue()) {
                AdCreativeTable creativeTable = new AdCreativeTable();
                map.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            creativeTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            creativeTable.setType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            creativeTable.setMaterialType(Integer.valueOf(v));
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            creativeTable.setHeight(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            creativeTable.setWidth(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            creativeTable.setAuditStatus(Integer.valueOf(v));
                    }
                });
                adCreativeTables.add(creativeTable);
                AdLevelDataHandler.handleLevel2(creativeTable,mysqlRowData.getOpType());
            }
        }
    }
    private void level3RowData(MysqlRowData mysqlRowData){
        if (mysqlRowData.getTableName().equals(Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)){
            List<AdUnitTable> adUnitTables = new ArrayList<>();
            for (Map<String, String> map : mysqlRowData.getFieldValue()) {
                AdUnitTable unitTable = new AdUnitTable();
                map.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(v));
                            break;
                    }
                });
                adUnitTables.add(unitTable);
            }
            adUnitTables.forEach(u->{
                AdLevelDataHandler.handleLevel3(u,mysqlRowData.getOpType());
            });
        } else if (mysqlRowData.getTableName().equals(Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
            for (Map<String, String> map : mysqlRowData.getFieldValue()) {
                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();
                map.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(v));
                            break;
                    }
                });
                creativeUnitTables.add(creativeUnitTable);
            }
            creativeUnitTables.forEach(c->{
                AdLevelDataHandler.handleLevel3(c,mysqlRowData.getOpType());
            });
        }
    }
    private void level4RowData(MysqlRowData mysqlRowData){
        switch (mysqlRowData.getTableName()){
            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME:
                List<AdUnitDistrictTable> districtTables = new ArrayList<>();
                for (Map<String, String> map : mysqlRowData.getFieldValue()) {
                    AdUnitDistrictTable districtTable = new AdUnitDistrictTable();
                    map.forEach((k,v)->{
                        switch (k){
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                                districtTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                                districtTable.setProvince(v);
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                                districtTable.setCity(v);
                                break;
                        }
                    });
                    districtTables.add(districtTable);
                }
                districtTables.forEach(d->{
                    AdLevelDataHandler.handleLevel4(d,mysqlRowData.getOpType());
                });
                break;
                case Constant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME:
                    List<AdUnitItTable> itTables = new ArrayList<>();
                    for (Map<String, String> map : mysqlRowData.getFieldValue()) {
                        AdUnitItTable itTable = new AdUnitItTable();
                        map.forEach((k,v)->{
                            switch (k){
                                case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                                    itTable.setUnitId(Long.valueOf(v));
                                    break;
                                case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                                    itTable.setItTag(v);
                                    break;
                            }
                        });
                        itTables.add(itTable);
                    }
                    itTables.forEach(i->{
                        AdLevelDataHandler.handleLevel4(i,mysqlRowData.getOpType());
                    });
                    break;
                    case Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME:
                        List<AdUnitKeywordTable> keywordTables = new ArrayList<>();
                        for (Map<String, String> map : mysqlRowData.getFieldValue()) {
                            AdUnitKeywordTable keywordTable = new AdUnitKeywordTable();
                            map.forEach((k,v)->{
                                switch (k){
                                    case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                                        keywordTable.setUnitId(Long.valueOf(v));
                                        break;
                                    case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                                        keywordTable.setKeyword(v);
                                        break;
                                }
                            });
                            keywordTables.add(keywordTable);
                        }
                        keywordTables.forEach(k->{
                            AdLevelDataHandler.handleLevel4(k,mysqlRowData.getOpType());
                        });
                        break;
        }
    }
}
