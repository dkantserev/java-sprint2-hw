package tracker;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.Tasks.Epic;
import tracker.Tasks.Task;
import tracker.exception.ManagerSaveException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    public void start() {
        file = new File("test1.csv");
        taskManager = new FileBackedTasksManager(file);
        creatorTestManager();
    }

    @Test
    void isBlank() {
        assertEquals(taskManager.getTask(task.getId()), task, "не верная задача");
        assertEquals(taskManager.getEpic(epic.getId()), epic, "не верная задача");
        assertEquals(taskManager.getSubTask(epic.getId(), subTask.getId()), subTask, "не верная задача");
    }


    @Test
    void LoadInMemory() {
        File file = new File("test1.csv");
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file);
        assertArrayEquals(taskManager.getTaskMap().values().toArray(new Task[0]), manager.getTaskMap().values()
                .toArray(), "список задач восстановлен не корректно");
        assertArrayEquals(taskManager.getEpicMap().values().toArray(new Epic[0]), manager.getEpicMap().values()
                .toArray(new Epic[0]), "список эпиков и подзадач восстановлен не корректно");
        assertArrayEquals(taskManager.getHistoryManager().getHistory().toArray(new Task[0]), manager.getHistoryManager()
                .getHistory().toArray(new Task[0]), "история запросов восстановлена не верно");
    }

    @Test
    void ManagerSaveException() {
        try {
            File file = new File("exception.csv");
            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        } catch (ManagerSaveException e) {
            assertEquals(e.getMessage(), "ошибка загрузки из файла", "не известная ошибка");
        }
    }
}