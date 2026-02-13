package com.idea.ad.consumer;

import com.alibaba.fastjson.JSON;
import com.idea.ad.dto.MysqlRowData;
import com.idea.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class BinlogConsumer {
    private final ISender sender;
    @Autowired
    public BinlogConsumer(ISender sender) {
        this.sender = sender;
    }
    @KafkaListener(topics = {"ad-search-mysql-data"},groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            MysqlRowData data = JSON.parseObject(message.toString(), MysqlRowData.class);
            log.info("MysqlRowData: {}", data);
            sender.sender(data);
        }
    }
}
