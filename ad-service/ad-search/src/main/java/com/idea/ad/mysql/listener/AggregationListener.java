package com.idea.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.idea.ad.mysql.TemplateHolder;
import com.idea.ad.mysql.dto.BinlogRowData;
import com.idea.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {
    private String dbName;
    private String tableName;
    private Map<String,IListener> listenerMap = new HashMap<>();
    private final TemplateHolder templateHolder;
    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }
    private String genKey(String dbName, String tableName){
        return dbName + ":" + tableName;
    }
    public void register(String dbName,String tableName,IListener listener){
        log.info("register: {}-{}",dbName,tableName);
        this.listenerMap.put(genKey(dbName,tableName),listener);
    }
    @Override
    public void onEvent(Event event) {
        EventType type = event.getHeader().getEventType();
        log.debug("event type: {}",type);
        if (type == EventType.TABLE_MAP){
            TableMapEventData data = event.getData();
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }
        if (type != EventType.EXT_WRITE_ROWS && type != EventType.EXT_UPDATE_ROWS && type != EventType.EXT_DELETE_ROWS){
            return;
        }
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)){
            log.error("no meta data event");
            return;
        }
        // 找出对应表有兴趣的监听器
        String key = genKey(this.dbName, this.tableName);
        IListener listener = listenerMap.get(key);
        if (null == listener){
            log.debug("skip{}",key);
            return;
        }
        log.info("trigger event: {}",type.name());
        try {
            BinlogRowData rowData  = buildRowData(event.getData());
            if (null == rowData){
                return;
            }
            rowData.setEventType(type);
            listener.onEvent(rowData);
        }catch (Exception ex){
            ex.printStackTrace();
            log.error("event error: {}",ex.getMessage());
        }finally {
            this.dbName = "";
            this.tableName = "";
        }
    }
    private List<Serializable[]> getAfterValues(EventData eventData){
        if (eventData instanceof WriteRowsEventData){
            return ((WriteRowsEventData) eventData).getRows();
        }
        if (eventData instanceof UpdateRowsEventData){
            return ((UpdateRowsEventData) eventData).getRows().stream()
                    .map(Map.Entry::getValue).collect(Collectors.toList());
        }
        if (eventData instanceof DeleteRowsEventData){
            return ((DeleteRowsEventData) eventData).getRows();
        }
        return Collections.emptyList();
    }
    private BinlogRowData buildRowData(EventData eventData){
        TableTemplate table = templateHolder.getTable(tableName);
        if (null == table){
            log.warn("table {} not found",tableName);
        }
        List<Map<String,String>> afterMapList = new ArrayList<>();
        for (Serializable[] afterValue : getAfterValues(eventData)) {
            Map<String,String> afterMap = new HashMap<>();
            int collen = afterValue.length;
            for (int ix = 0; ix < collen; ++ix){
                String colName = table.getPosMap().get(ix);
                //如果没有说明本列没兴趣
                if (null == colName){
                    log.debug("ignore field: {}",ix);
                    continue;
                }
                String colValue = afterValue[ix].toString();
                afterMap.put(colName,colValue);
            }
            afterMapList.add(afterMap);
        }
        BinlogRowData rowData = new BinlogRowData();
        rowData.setTable(table);
        rowData.setAfter(afterMapList);
        return null;
    }
}
