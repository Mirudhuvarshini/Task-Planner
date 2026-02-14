package com.taskplanner;

public class Task {
    private int id;
    private String title;
    private String description;
    private String deadline;
    private Priority priority;
    private Status status;

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    public enum Status {
        PENDING, COMPLETED
    }

    public Task(int id, String title, String description, String deadline, Priority priority, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
            "Task ID: %d\nTitle: %s\nDescription: %s\nDeadline: %s\nPriority: %s\nStatus: %s\n----------------------------",
            id, title, description, deadline, priority, status
        );
    }
}
