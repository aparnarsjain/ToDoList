package com.example.aparna.todolist;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by aparna on 1/20/16.
 */
public class Item implements Serializable{
    public static final int HIGH_PRIORITY = 0;
    public static final int MEDIUM_PRIORITY = 1;
    public static final int LOW_PRIORITY = 2;

    public static final int TASK_STATUS_DONE = 0;
    public static final int TASK_STATUS_PENDING = 1;

    private long id;
    private String text;
    private Calendar dueDate;
    private String note;
    private int priorityLevel;
    private int completionStatus;

    public Item() {
        this.id = -1;
        this.text = "";
        this.dueDate = Calendar.getInstance();
        this.note = "";
        this.priorityLevel = HIGH_PRIORITY;
        this.completionStatus = TASK_STATUS_DONE;
    }

    public Item(long id, String title, Calendar dueDate, String note,
                int priorityLevel, int completionStatus) {
        super();
        this.id = id;
        this.text = title;
        this.dueDate = dueDate;
        this.note = note;
        this.priorityLevel = priorityLevel;
        this.completionStatus = completionStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text= text;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public String getNotes() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getStatus() {
        return completionStatus;
    }

    public void setStatus(int completionStatus) {
        this.completionStatus = completionStatus;
    }


}

