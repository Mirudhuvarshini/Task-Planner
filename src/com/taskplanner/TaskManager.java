package com.taskplanner;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;
    private int nextId;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.nextId = 1;
    }

    public void addTask(String title, String description, String deadline, Task.Priority priority, Task.Status status) {
        Task newTask = new Task(nextId++, title, description, deadline, priority, status);
        tasks.add(newTask);
        System.out.println("Task added successfully with ID: " + newTask.getId());
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task getTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public boolean updateTaskWithoutStatus(int id, String title, String description, String deadline, Task.Priority priority) {
         Task task = getTaskById(id);
         if (task != null) {
             if (title != null && !title.isEmpty()) task.setTitle(title);
             if (description != null && !description.isEmpty()) task.setDescription(description);
             if (deadline != null && !deadline.isEmpty()) task.setDeadline(deadline);
             if (priority != null) task.setPriority(priority);
             return true;
         }
         return false;
    }
    
    public boolean updateTaskStatus(int id, Task.Status status) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setStatus(status);
            return true;
        }
        return false;
    }

    public boolean deleteTask(int id) {
        Task task = getTaskById(id);
        if (task != null) {
            tasks.remove(task);
            return true;
        }
        return false;
    }
}
