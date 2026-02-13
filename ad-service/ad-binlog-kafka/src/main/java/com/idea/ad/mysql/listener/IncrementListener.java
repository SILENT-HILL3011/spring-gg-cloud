package com.idea.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.idea.ad.constant.Constant;
import com.idea.ad.constant.OpType;
import com.idea.ad.dto.BinlogRowData;
import com.idea.ad.dto.MysqlRowData;
import com.idea.ad.dto.TableTemplate;
import com.idea.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements IListener{
    @Resource
    private ISender sender;
    private final AggregationListener aggregationListener;
    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    /**
     * 根据binlog事件实现listener
     */
    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener register db and table info");
        Constant.table2Db.forEach((k,v)->{
            aggregationListener.register(v,k,this);
        });
    }

    @Override
    public void onEvent(BinlogRowData eventData) {
        TableTemplate table = eventData.getTable();
        EventType type = eventData.getEventType();
        // 包装成最后投递的数据
        MysqlRowData mysqlRowData = new MysqlRowData();
        mysqlRowData.setTableName(table.getTableName());
        mysqlRowData.setLevel(eventData.getTable().getLevel());
        OpType opType = OpType.to(type);
        mysqlRowData.setOpType(opType);
        // 获取所有字段
        List<String> filedList = table.getOpTypeFieldSetMap().get(opType);
        if (null == filedList){
            log.warn("{} not support for {}",opType,table.getTableName());
            return;
        }
        for (Map<String,String> afterMap : eventData.getAfter()){
            Map<String,String> _afterMap  = new HashMap<>();
            for (Map.Entry<String, String> entry : afterMap.entrySet()) {
                String colName = entry.getKey();
                String colValue = entry.getValue();
                 _afterMap.put(colName,colValue);
            }
            mysqlRowData.getFieldValue().add(_afterMap);
        }
        sender.sender(mysqlRowData);
    }
}
