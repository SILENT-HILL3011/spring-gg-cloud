package com.idea.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.idea.ad.mysql.dto.MysqlRowData;
import com.idea.ad.sender.ISender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("kafkaSender")
public class KafkaSender implements ISender {
    @Value("${adconf.kafka.topic}")
    private String topic;
    private final KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sender(MysqlRowData mysqlRowData) {
        kafkaTemplate.send(topic, JSON.toJSONString(mysqlRowData));
    }
    @KafkaListener(topics = {"ad-search-mysql-data"},groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            MysqlRowData rowData = JSON.parseObject(message.toString(),MysqlRowData.class);
            System.out.println("kafka message:"+JSON.toJSONString(rowData));
        }
    }
}
