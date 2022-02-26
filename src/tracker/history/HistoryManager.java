package tracker.history;

import tracker.Tasks.Task;

import java.util.List;


public interface HistoryManager { // интерфейс менеджера по учету запросов задач

    void addHistory(Task task);

    List<Task> getHistory();

    void removeId(Integer id);

    int getSize();

    void clearHistory();


}
