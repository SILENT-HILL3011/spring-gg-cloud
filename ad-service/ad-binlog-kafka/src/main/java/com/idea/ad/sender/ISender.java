package com.idea.ad.sender;

import com.idea.ad.dto.MysqlRowData;

public interface ISender {
    void sender(MysqlRowData mysqlRowData);
}
