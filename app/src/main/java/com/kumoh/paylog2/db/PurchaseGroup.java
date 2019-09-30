package com.kumoh.paylog2.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PurchaseGroup {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String groupName;
    private String contents;

    public PurchaseGroup(String groupName, String contents) {
        this.groupName = groupName;
        this.contents = contents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "PurchaseGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
