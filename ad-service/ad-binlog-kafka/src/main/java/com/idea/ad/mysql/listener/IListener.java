package com.idea.ad.mysql.listener;

import com.idea.ad.dto.BinlogRowData;

public interface IListener {
    void register();
    void onEvent(BinlogRowData eventData);
}
