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
    public List<Task> getHistory() { // Возвращает статистику по запросам
        return historyList;
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


