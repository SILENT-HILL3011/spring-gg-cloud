package com.idea.ad.index.keyword;

import com.idea.ad.index.IndexAware;
import com.idea.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {
    private static Map<String, Set<Long>> keywordUnitMap;
    private static Map<Long, Set<String>> unitKeywordMap;
    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        if (StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }
        Set<Long> unitIds = keywordUnitMap.get(key);
        if (unitIds == null){
            return Collections.emptySet();
        }
        return unitIds;
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("before add: {}",unitKeywordMap);
        Set<Long> unitIdSet = CommonUtils.getOrCreate(
                key,keywordUnitMap, ConcurrentSkipListSet::new
        );
        unitIdSet.addAll(value);
        for (Long unitId : value){
            Set<String> keywordSet = CommonUtils.getOrCreate(
                    unitId,unitKeywordMap, ConcurrentSkipListSet::new
            );
            keywordSet.add(key);
        }
        log.info("after add: {}",unitKeywordMap);

    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("before delete: {}",unitKeywordMap);
        Set<Long> unitIdSet = CommonUtils.getOrCreate(
                key,keywordUnitMap, ConcurrentSkipListSet::new
        );
        unitIdSet.removeAll(value);
        for (Long unitId : value){
            Set<String> keywordSet = CommonUtils.getOrCreate(
                    unitId,unitKeywordMap, ConcurrentSkipListSet::new
            );
            keywordSet.remove(key);
        }
        log.info("after delete: {}",unitKeywordMap);
    }
    public boolean match(Long unitId, List<String>  keywords){
        if (unitKeywordMap.containsKey(unitId)&& CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))){
            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            return CollectionUtils.isSubCollection(keywords,unitKeywords);
        }
        return false;
    }
}
