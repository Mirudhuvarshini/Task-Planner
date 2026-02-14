package com.taskplanner.service;

import com.taskplanner.model.Task;
import com.taskplanner.model.User;
import com.taskplanner.repository.TaskRepository;
import com.taskplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);

        // Sort: Priority (HIGH > MEDIUM > LOW) then Deadline (Ascending)
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getPriority)
                        .thenComparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setDeadline(taskDetails.getDeadline());
            task.setPriority(taskDetails.getPriority());
            task.setStatus(taskDetails.getStatus());
            return taskRepository.save(task);
        }
        return null;
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
