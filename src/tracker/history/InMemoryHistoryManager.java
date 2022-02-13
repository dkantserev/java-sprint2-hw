package tracker.history;
import tracker.Tasks.Status;
import tracker.Tasks.Epic;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager { // запись запросов в историю, печать истории
    private List<Task> historyList = new ArrayList<>();

    @Override
    public List<Task> addHistory(Task task) { // Добавляет обьект в history
        if (historyList.size() < 10) {
            historyList.add(task);
        }
        if (historyList.size() >= 10) {
            historyList.remove(1);
            historyList.add(task);
        }
        return historyList;
    }

    @Override
    public void getHistory() { // Возвращает статистику по запросам
        Epic epic = new Epic("", "", 0, Status.TASK_NEW);
        Task task1 = new Task("", "", 0, Status.TASK_NEW);
        SubTask subTask = new SubTask("", "", 0, Status.TASK_NEW, epic);
        byte epicCall = 0;
        byte taskCall = 0;
        byte subTaskCall = 0;
        for (Task task : historyList) {
            if (task.getClass() == epic.getClass()) {
                epicCall++;
            }
            if (task.getClass() == task1.getClass()) {
                taskCall++;
            }
            if (task.getClass() == subTask.getClass()) {
                subTaskCall++;
            }
        }
        System.out.println("Task был запрошен " + taskCall + ";" + "\n"
                + "Epic был запрошен " + epicCall + ";" + "\n"
                + "SubTusk был запрошен " + subTaskCall + ";" + "\n");
        System.out.println("Печать historyList     " + historyList + "\n");
    }

    @Override
    public void checkActualHistoryList(Task task) { // Проверка на наличие в истории удаленных задач
        for (int i = 1; i < historyList.size(); i++) {
            if (historyList.get(i).equals(task)) {
                historyList.remove(i);
            }
        }
    }

    @Override
    public void clearHistory() { // очистка истории
        historyList.clear();
    }
}


