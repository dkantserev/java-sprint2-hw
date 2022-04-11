package tracker.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.Tasks.Status;
import tracker.Tasks.Task;
import tracker.Tasks.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static ZoneId zoneId = ZoneId.systemDefault();
    private Task task;
    private Task task1;
    private Task task3;
    private Task task4;
    private HistoryManager historyManager;

    @BeforeEach
    void startDate() {
        historyManager = new InMemoryHistoryManager();
        task = new Task(TypeTask.TYPE_TASK, "testTask1", "...", 1, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2028, 1, 1, 21, 1), zoneId)
                , Duration.ofHours(3));
        task1 = new Task(TypeTask.TYPE_TASK, "testTask2", "...", 2, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2027, 1, 1, 21, 1), zoneId)
                , Duration.ofHours(3));
        task3 = new Task(TypeTask.TYPE_TASK, "testTask3", "...", 3, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2026, 1, 1, 21, 1), zoneId)
                , Duration.ofHours(3));
        task4 = new Task(TypeTask.TYPE_TASK, "testTask4", "...", 4, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2025, 1, 1, 21, 1), zoneId)
                , Duration.ofHours(3));
    }

    @Test
    void getSize() {
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task4);
        historyManager.add(task4);
        assertEquals(historyManager.getSize(), 3, "дублирование истории");
    }

    @Test
    void add() {
        historyManager.clearHistory();
        historyManager.add(task);
        assertEquals(historyManager.getSize(), 1, "задача не добавляется в историю");
    }

    @Test
    void getHistory() {
        List<Task> testList = new ArrayList<>();
        testList.add(task1);
        testList.add(task3);
        testList.add(task4);
        historyManager.clearHistory();
        historyManager.add(task1);
        historyManager.add(task3);
        historyManager.add(task4);
        assertArrayEquals(testList.toArray(), historyManager.getHistory().toArray(new Task[0])
                , "не верная история");
    }

    @Test
    void remove() {
        historyManager.clearHistory();
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.remove(task.getId());
        assertFalse(historyManager.getHistory().contains(task), "задача не удаляется из начала списка");
        historyManager.remove(task4.getId());
        assertFalse(historyManager.getHistory().contains(task4), "задача не удаляется из конца списка");
        historyManager.clearHistory();
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.remove(task1.getId());
        assertFalse(historyManager.getHistory().contains(task1), "задача не удаляется из середины списка");
    }

    @Test
    void clearHistory() {
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.clearHistory();
        assertEquals(historyManager.getSize(), 0, "история не очищена");
    }
}