package tracker;

import tracker.Tasks.Epic;
import tracker.Tasks.Status;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;
import tracker.history.HistoryManager;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager { // Хранилище всех типов задач, вся логика работы с ними

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    HistoryManager historyManager = Manager.getDefaultHistory();

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
    public void updateTask(Task task) { // Обновление объекта Task
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) { // Обновление объекта Epic
        epic.subTasks = epicMap.get(epic.getId()).subTasks;
        epicMap.put(epic.getId(), epic);
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
        historyManager.checkActualHistoryList(taskMap.get(id));
        taskMap.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) { // Удалить эпик по id.
        historyManager.checkActualHistoryList(epicMap.get(id));
        epicMap.remove(id);
    }

    @Override
    public void deleteSubTaskById(Integer idEpic, Integer id) { // Удалить подзадачу эпика по id.
        historyManager.checkActualHistoryList(epicMap.get(idEpic).subTasks.get(id));
        for (int i = 1; i <= epicMap.get(idEpic).subTasks.size(); i++) {
            if (epicMap.get(idEpic).subTasks.get(i).getId() == id) {
                epicMap.get(idEpic).subTasks.remove(i);
            } else
                break;
        }
        getEpic(idEpic).getStatus();
    }

    @Override
    public void clearTaskMap() { // Удалить все задачи.
        taskMap.clear();
    }

    @Override
    public void clearEpicMap() { // Удалить все эпики.
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
        historyManager.addHistory(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Task getTask(Integer id) { // геттер
        historyManager.addHistory(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public SubTask getSubTask(Integer idEpick, Integer idSubTask) { // геттер
        historyManager.addHistory(epicMap.get(idEpick).subTasks.get(idSubTask));
        return epicMap.get(idEpick).subTasks.get(idSubTask);
    }

    @Override
    public void printAllTasks() { // Печать всех задач.
        if (!taskMap.isEmpty()) {
            for (Integer integer : taskMap.keySet()) {
                System.out.println("Задача " + taskMap.get(integer).getNameTask() + ". Суть "
                        + taskMap.get(integer).getTaskBody());
                if (taskMap.get(integer).getStatus().equals(Status.IN_PROGRESS))
                    System.out.println("Задача в процессе исполнения.");
                if (taskMap.get(integer).getStatus().equals(Status.TASK_NEW))
                    System.out.println("К задаче еще не приступили.");
                if (taskMap.get(integer).getStatus().equals(Status.DONE))
                    System.out.println("Задача выполнена.");
            }
        }
        if (!epicMap.isEmpty()) {
            for (Integer integer : epicMap.keySet()) {
                System.out.println("Задача " + epicMap.get(integer).getNameTask() + ". Суть "
                        + epicMap.get(integer).getTaskBody());
                if (epicMap.get(integer).getStatus().equals(Status.IN_PROGRESS))
                    System.out.println("Задача в процессе исполнения.");
                if (epicMap.get(integer).getStatus().equals(Status.TASK_NEW))
                    System.out.println("К задаче еще не приступили.");
                if (epicMap.get(integer).getStatus().equals(Status.DONE))
                    System.out.println("Задача выполнена.");
                if (epicMap.get(integer).subTasks.size() > 0) {
                    for (int i = 0; i < epicMap.get(integer).subTasks.size(); i++) {
                        System.out.println("Подзадача " + (i + 1) + " " + epicMap.get(integer).subTasks.get(i).getNameTask()
                                + ". Суть " + epicMap.get(integer).subTasks.get(i).getTaskBody());
                        if (epicMap.get(integer).subTasks.get(i).getStatus().equals(Status.IN_PROGRESS))
                            System.out.println("Задача в процессе исполнения.");
                        if (epicMap.get(integer).subTasks.get(i).getStatus().equals(Status.TASK_NEW))
                            System.out.println("К задаче еще не приступили.");
                        if (epicMap.get(integer).subTasks.get(i).getStatus().equals(Status.DONE))
                            System.out.println("Задача выполнена.");
                    }
                }

            }
        }
    }

}
