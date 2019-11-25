package com.kumoh.paylog2.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Image {
    @ForeignKey(entity = Account.class, parentColumns = "accountId", childColumns = "accountId")
    private int accountId;
    @ForeignKey(entity = Account.class, parentColumns = "historyId", childColumns = "historyId")
    private int historyId;
    @PrimaryKey(autoGenerate = true)
    private int imageId;
    private String filePath;

    public Image(int accountId, int historyId, String filePath) {
        this.accountId = accountId;
        this.historyId = historyId;
        this.filePath = filePath;
    }

    public int getAccountId() { return accountId;}

    public void setAccountId(int accountId) { this.accountId = accountId;}

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getImageId() { return imageId;}

    public void setImageId(int imageId) { this.imageId = imageId;}

    public String getFilePath() { return filePath;}

    public void setFilePath(String filePath) { this.filePath = filePath;}
}
