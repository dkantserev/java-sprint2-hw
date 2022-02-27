package tracker;

import tracker.Tasks.Epic;
import tracker.Tasks.Status;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;
import tracker.history.HistoryManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager { // Хранилище всех типов задач, вся логика работы с ними

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    HistoryManager historyManager = Manager.getDefaultHistory();

    public static Integer generaticId() { // генератор уникальных id
        HashSet<Integer> id = new HashSet<>();
        Random random = new Random();
        int r = 0;
        while (id.add(r)) {
            r = random.nextInt(1000);
        }
        return r;

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
        for (int i = 0; i < epicMap.get(subTask.getEpic().getId()).subTasks.size(); i++) {
            if (epicMap.get(subTask.getEpic().getId()).subTasks.get(i).getId() == subTask.getId()) {
                epicMap.get(subTask.getEpic().getId()).subTasks.remove(i);
            }
        }
        epicMap.get(subTask.getEpic().getId()).subTasks.add(subTask);
    }

    @Override
    public void addTaskToMap(Integer id, Task task) { // Задача помещается в хранилищею
        taskMap.put(id, task);
    }

    @Override
    public void addEpicToMap(Integer id, Epic epic) { // Эпик помещается в хранилище.
        epicMap.put(id, epic);
    }

    @Override
    public void addSubTaskMap(SubTask subTask, Epic epic) { // Добавляет подзадачу к эпику.
        epic.subTasks.add(subTask);
    }

    @Override
    public void setStatus(Task task, Status status) { // Устанавливает значение задачи
        taskMap.get(task.getId()).setStatus(status);
    }

    public void setStatusSubTask(SubTask subTask, Status status) { // Устанавливает статус подзадачи
        for (int i = 0; i < epicMap.get(subTask.getEpic().getId()).subTasks.size(); i++) {
            if (epicMap.get(subTask.getEpic().getId()).subTasks.get(i) == subTask) {
                epicMap.get(subTask.getEpic().getId()).subTasks.get(i).setStatus(status);
                epicMap.get(subTask.getEpic().getId()).getStatus();
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
                historyManager.add(s);
            }
        }
        return s;
    }

}
