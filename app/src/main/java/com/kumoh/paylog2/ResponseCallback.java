package com.kumoh.paylog2;

import com.kumoh.paylog2.dto.HistoryVO;

public interface ResponseCallback {
    void showAddSpendingDialog(HistoryVO historyVO);
}
