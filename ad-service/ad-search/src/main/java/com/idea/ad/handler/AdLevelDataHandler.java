package com.idea.ad.handler;

import com.alibaba.fastjson.JSON;
import com.idea.ad.dump.table.*;
import com.idea.ad.index.DataTable;
import com.idea.ad.index.IndexAware;
import com.idea.ad.index.adplan.AdPlanIndex;
import com.idea.ad.index.adplan.AdPlanObject;
import com.idea.ad.index.adunit.AdUnitIndex;
import com.idea.ad.index.adunit.AdUnitObject;
import com.idea.ad.index.creatice.CreativeIndex;
import com.idea.ad.index.creatice.CreativeObject;
import com.idea.ad.index.creativeunit.CreativeUnitIndex;
import com.idea.ad.index.creativeunit.CreativeUnitObject;
import com.idea.ad.index.district.UnitDistrictIndex;
import com.idea.ad.index.interest.UnitItIndex;
import com.idea.ad.index.keyword.UnitKeywordIndex;
import com.idea.ad.mysql.constant.OpType;
import com.idea.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AdLevelDataHandler {
    public static void handleLevel4(AdUnitDistrictTable adUnitDistrictTable, OpType opType){
        if (opType == OpType.UPDATE){
            log.error("districtindex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(adUnitDistrictTable.getUnitId());
        if (unitObject == null){
            log.error("handleLevel4 found AdUnitObject error {}",adUnitDistrictTable.getUnitId());
            return;
        }
        String key = CommonUtils.stringConcat(adUnitDistrictTable.getProvince(),adUnitDistrictTable.getCity());
        Set<Long> value = new HashSet<>(Collections.singleton(adUnitDistrictTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                key,
                value,
                opType
        );
    }
    public static void handleLevel4(AdUnitItTable adUnitItTable, OpType opType){
        if (opType == OpType.UPDATE){
            log.error("itindex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(adUnitItTable.getUnitId());
        if (unitObject == null){
            log.error("handleLevel4 found AdUnitObject error {}",adUnitItTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(adUnitItTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),
                adUnitItTable.getItTag(),
                value,
                opType
        );
    }
    public static void handleLevel4(AdUnitKeywordTable adUnitKeywordTable, OpType opType){
        if (opType == OpType.UPDATE){
            log.error("itindex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(adUnitKeywordTable.getUnitId());
        if (unitObject == null){
            log.error("handleLevel4 found AdUnitObject error {}",adUnitKeywordTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(adUnitKeywordTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                adUnitKeywordTable.getKeyword(),
                value,
                opType
        );
    }
    public static void handleLevel3(AdUnitTable adUnitTable, OpType opType){
        AdPlanObject adPlanObject = DataTable.of(
                AdPlanIndex.class
        ).get(adUnitTable.getPlanId());
        if (null == adPlanObject){
            log.error("handleLevel3 found AdPlanObject error {}",adUnitTable.getPlanId());
            return;
        }
        AdUnitObject adUnitObject = new AdUnitObject(
                adUnitTable.getUnitId(),
                adUnitTable.getUnitStatus(),
                adUnitTable.getPositionType(),
                adUnitTable.getPlanId(),
                adPlanObject
        );
        handleBinlogEvent(
                DataTable.of(AdUnitIndex.class),
                adUnitObject.getUnitId(),
                adUnitObject,
                opType
        );
    }
    public static void handleLevel3(AdCreativeUnitTable adCreativeTable, OpType opType){
        if (opType == OpType.UPDATE){
            log.error("creativeindex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(adCreativeTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(
                CreativeIndex.class
        ).get(adCreativeTable.getAdId());
        if (null == unitObject || null == creativeObject){
            log.error("handleLevel3 found AdUnitObject error {}", JSON.toJSONString(adCreativeTable));
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                adCreativeTable.getAdId(),
                adCreativeTable.getUnitId()
        );
        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(
                        creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                opType
        );
    }
    public static void handleLevel2(AdPlanTable adPlanTable, OpType opType){
        AdPlanObject adPlanObject = new AdPlanObject(
                adPlanTable.getId(),
                adPlanTable.getUserId(),
                adPlanTable.getPlanStatus(),
                adPlanTable.getStartDate(),
                adPlanTable.getEndDate()
        );
        handleBinlogEvent(
                DataTable.of(AdPlanIndex.class),
                adPlanObject.getPlanId(),
                adPlanObject,
                opType
        );
    }
    public static void handleLevel2(AdCreativeTable creativeTable,
                                    OpType type) {

        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );
        handleBinlogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }

    private static <K,V> void handleBinlogEvent(IndexAware<K,V> index, K key, V value, OpType opType){
        switch (opType){
            case ADD:
                index.add(key,value);
                break;
            case UPDATE:
                index.update(key,value);
                break;
            case DELETE:
                index.delete(key,value);
                break;
            default:
                break;
        }
    }
}
