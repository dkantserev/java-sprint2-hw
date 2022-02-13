package tracker.history;
import tracker.Tasks.Task;
import java.util.List;

public interface HistoryManager { // интерфейс менеджера по учету запросов задач

    List<Task> addHistory(Task task);

    void getHistory();

    void checkActualHistoryList(Task task);

    void clearHistory();
}
