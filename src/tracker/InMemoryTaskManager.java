package tracker;

import tracker.Tasks.*;
import tracker.exception.OverlappingException;
import tracker.history.HistoryManager;

import java.io.IOException;
import java.util.*;

public class InMemoryTaskManager implements TaskManager { // Хранилище всех типов задач, вся логика работы с ними

    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyManager = Manager.getDefaultHistory();
    private final Set<Task> setTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public TreeSet<Task> getPrioritizedTasks() { // список задач по приоритету начала
        setTask.addAll(taskMap.values());
        for (Epic value : epicMap.values()) {
            setTask.addAll(value.subTasks);
        }
        return (TreeSet<Task>) setTask;
    }

    @Override
    public void load() throws IOException, InterruptedException {

    }

    public static Integer generaticId() {
        HashSet<Integer> id = new HashSet<>(1000);
        Random random = new Random();
        int r = 0;
        while (id.add(r)) {
            r = random.nextInt(1000);
        }
        return r;
    }

    @Override
    public Boolean overlappingTask(Task task) {

        if (task.getTypeTask().equals(TypeTask.TYPE_EPIC)) {
            task = (Epic) task;
        }
        for (Task value : taskMap.values()) {
            if (task.getStartTime().isBefore(value.getStartTime()) && task.getEndTime().isAfter(value.getStartTime())) {
                return true;
            }
            if (task.getStartTime().isBefore(value.getEndTime()) && task.getEndTime().isAfter(value.getEndTime())) {
                return true;
            }
            if (task.getStartTime().equals(value.getStartTime()) && task.getEndTime().equals(value.getEndTime())) {
                return true;
            }
        }
        for (Epic value1 : epicMap.values()) {
            if (task.getStartTime().isBefore(value1.getStartTime()) && task.getEndTime().isAfter(value1.getStartTime())
                    && value1.subTasks.contains((SubTask) task)) {
                return true;
            }
            if (task.getStartTime().isBefore(value1.getEndTime()) && task.getEndTime().isAfter(value1.getEndTime())
                    && value1.subTasks.contains((SubTask) task)) {
                return true;
            }
            if (task.getStartTime().equals(value1.getStartTime()) && task.getEndTime().equals(value1.getEndTime())
                    && value1.subTasks.contains((SubTask) task)) {
                return true;
            }
        }
        for (Epic value : epicMap.values()) {
            for (SubTask value2 : value.subTasks) {
                if (task.getStartTime().isBefore(value2.getStartTime()) && task.getEndTime()
                        .isAfter(value2.getStartTime()) && value2.getEpic() != task.getId()) {
                    return true;
                }
                if (task.getStartTime().isBefore(value2.getEndTime()) && task.getEndTime().isAfter(value2.getEndTime())
                        && value2.getEpic() != task.getId()) {
                    return true;
                }
                if (task.getStartTime().equals(value2.getStartTime()) && task.getEndTime().equals(value2.getEndTime())
                        && value2.getEpic() != task.getId()) {
                    return true;
                }
            }
        }

        return false;

    }

    @Override
    public HashMap<Integer, Task> getTaskMap() { // геттер хранилища Task
        return taskMap;
    }

    @Override
    public HashMap<Integer, Epic> getEpicMap() { // геттер хранилища Epic
        return epicMap;
    }

    @Override
    public HistoryManager getHistoryManager() { // геттер
        return historyManager;
    }

    @Override
    public void updateTask(Task task, Integer idOldTask) { // Обновление объекта Task
        taskMap.put(idOldTask, task);
    }

    @Override
    public void updateEpic(Epic epic, Integer idOldTask) { // Обновление объекта Epic
        epic.subTasks = epicMap.get(idOldTask).subTasks;
        epicMap.put(idOldTask, epic);
        epic.getStatus();
    }

    @Override
    public void updateSubTask(SubTask subTask) { // Обновление объекта Subtask
        for (int i = 0; i < epicMap.get(subTask.getEpic()).subTasks.size(); i++) {
            if (epicMap.get(subTask.getEpic()).subTasks.get(i).getId() == subTask.getId()) {
                epicMap.get(subTask.getEpic()).subTasks.remove(i);
            }
        }
        epicMap.get(subTask.getEpic()).subTasks.add(subTask);
    }

    @Override
    public void addTaskToMap(Integer id, Task task) { // Задача помещается в хранилищею
        if (!overlappingTask(task)) {
            taskMap.put(id, task);
        } else {
            throw new OverlappingException("пересечение времени");
        }
    }

    @Override
    public void addEpicToMap(Integer id, Epic epic) { // Эпик помещается в хранилище.
        if (!overlappingTask(epic)) {
            epicMap.put(id, epic);
        } else {
            throw new OverlappingException("пересечение времени");
        }
    }

    @Override
    public void addSubTaskMap(SubTask subTask, int epicId) { // Добавляет подзадачу к эпику.
        if (!overlappingTask(subTask)) {
            getEpicMap().get(epicId).subTasks.add(subTask);
        } else {
            throw new OverlappingException("пересечение времени");
        }
    }

    @Override
    public void setStatus(Task task, Status status) { // Устанавливает значение задачи
        taskMap.get(task.getId()).setStatus(status);
    }

    public void setStatusSubTask(SubTask subTask, Status status) { // Устанавливает статус подзадачи
        for (int i = 0; i < epicMap.get(subTask.getEpic()).subTasks.size(); i++) {
            if (epicMap.get(subTask.getEpic()).subTasks.get(i) == subTask) {
                epicMap.get(subTask.getEpic()).subTasks.get(i).setStatus(status);
                epicMap.get(subTask.getEpic()).getStatus();
            }
        }
    }

    @Override
    public void deleteTaskById(Integer id) { // Удалить задачу по id.
        taskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) { // удалить эпик по id
        if (epicMap.get(id).subTasks.isEmpty()) {
            historyManager.remove(id);
        } else {
            for (SubTask subTask : epicMap.get(id).subTasks) {
                historyManager.remove(subTask.getId());
            }
            historyManager.remove(id);
        }
        epicMap.remove(id);
    }

    @Override
    public void deleteSubTaskById(Integer idEpic, Integer id) { // Удалить подзадачу эпика по id.

        for (int i = 0; i < epicMap.get(idEpic).subTasks.size(); i++) {
            if (epicMap.get(idEpic).subTasks.get(i).getId() == id) {
                epicMap.get(idEpic).subTasks.remove(i);
            }
        }
        getEpic(idEpic).getStatus();
        historyManager.remove(id);
    }

    @Override
    public void clearTaskMap() { // Удалить все задачи.
        for (Integer integer : taskMap.keySet()) {
            historyManager.remove(integer);
        }
        taskMap.clear();
    }

    @Override
    public void clearEpicMap() { // Удалить все эпики.
        for (Integer integer : epicMap.keySet()) {
            deleteEpicById(integer);
        }
        epicMap.clear();
    }


    @Override
    public void removeEverythingCompletely() { // Очищает хранилища.
        taskMap.clear();
        epicMap.clear();
        historyManager.clearHistory();
    }

    @Override
    public Epic getEpic(Integer id) { // геттер
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Task getTask(Integer id) { // геттер
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public SubTask getSubTask(Integer epicId, Integer subTaskId) { // геттер

        SubTask s = null;

        for (SubTask subTask : epicMap.get(epicId).subTasks) {
            if (subTask.getId() == subTaskId) {
                s = subTask;
            }
        }
        historyManager.add(s);
        return s;
    }
}
