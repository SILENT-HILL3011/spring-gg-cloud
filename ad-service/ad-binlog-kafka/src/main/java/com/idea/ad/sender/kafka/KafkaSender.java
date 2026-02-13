package com.idea.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.idea.ad.dto.MysqlRowData;
import com.idea.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
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
        log.info("binlog kafka service send MysqlRowData");
        kafkaTemplate.send(topic, JSON.toJSONString(mysqlRowData));
    }

}
