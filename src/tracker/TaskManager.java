package tracker;

import tracker.Tasks.Epic;
import tracker.Tasks.Status;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;
import tracker.history.HistoryManager;

import java.util.HashMap;
import java.util.TreeSet;

public interface TaskManager { // интерфейс менеджера управляющего хранением и обработкой задач
    HashMap<Integer, Task> getTaskMap();

    HashMap<Integer, Epic> getEpicMap();

    HistoryManager getHistoryManager();

    void updateTask(Task task, Integer idOldTask);

    void updateEpic(Epic epic, Integer idOldTask);

    void updateSubTask(SubTask subTask);

    void addTaskToMap(Integer id, Task task);

    void addEpicToMap(Integer id, Epic epic);

    void addSubTaskMap(SubTask subTask, int epicId);

    void setStatusSubTask(SubTask subTask, Status status);

    void setStatus(Task task, Status status);

    void deleteTaskById(Integer id);

    void deleteEpicById(Integer id);

    void deleteSubTaskById(Integer idEpic, Integer id);

    void clearTaskMap();

    void clearEpicMap();

    void removeEverythingCompletely();

    Epic getEpic(Integer id);

    Task getTask(Integer id);

    SubTask getSubTask(Integer epicId, Integer subTaskId);

    Boolean overlappingTask(Task task);

    TreeSet<Task> getPrioritizedTasks();


}
