package com.idea.ad.sender;

import com.idea.ad.mysql.dto.MysqlRowData;

public interface ISender {
    void sender(MysqlRowData mysqlRowData);
}
