package com.example.finalexam;

import java.util.Date;
import java.util.GregorianCalendar;

public class Word {
    int ItemID;
    String value;

    Date startDate;

    boolean isDone;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    Date finishDate;

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        String[] tempStartDate = startDate.split("-");

        this.startDate = new GregorianCalendar(Integer.valueOf(tempStartDate[0]),
                Integer.valueOf(tempStartDate[1]) - 1,
                Integer.valueOf(tempStartDate[2])).getTime();
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        String[] tempFinishDate = finishDate.split("-");
        this.finishDate = new GregorianCalendar(Integer.valueOf(tempFinishDate[0]),
                Integer.valueOf(tempFinishDate[1]) - 1,
                Integer.valueOf(tempFinishDate[2])).getTime();
    }


}
