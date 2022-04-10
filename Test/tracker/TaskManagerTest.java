package tracker;

import org.junit.jupiter.api.Test;
import tracker.Tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest<C extends TaskManager> {

    protected C taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;
    protected SubTask subTask2;
    protected ZoneId zoneId = ZoneId.systemDefault();

    protected void creatorTestManager() {
        task = new Task(TypeTask.TYPE_TASK, "testTask1", "...", 1, Status.TASK_NEW, ZonedDateTime.of(LocalDateTime.of(2028, 1, 1, 21, 1), zoneId), Duration.ofHours(3));
        epic = new Epic(TypeTask.TYPE_EPIC, "epicTest1", "...", 2, Status.TASK_NEW, ZonedDateTime.of(LocalDateTime.of(2028, 1, 11, 21, 1), zoneId), Duration.ofHours(3));
        subTask = new SubTask(TypeTask.TYPE_SUBTASK, "subtaskTest1", "...", 3, Status.TASK_NEW, epic.getId(), ZonedDateTime.of(LocalDateTime.of(2028, 1, 13, 21, 1), zoneId), Duration.ofHours(3));
        subTask2 = new SubTask(TypeTask.TYPE_SUBTASK, "subtaskTest1", "...", 4, Status.TASK_NEW, epic.getId(), ZonedDateTime.of(LocalDateTime.of(2028, 1, 14, 21, 1), zoneId), Duration.ofHours(3));
        taskManager.addTaskToMap(task.getId(), task);
        taskManager.addEpicToMap(epic.getId(), epic);
        taskManager.addSubTaskMap(subTask, epic.getId());
        taskManager.addSubTaskMap(subTask2, epic.getId());
    }


    @Test
    void getTaskMap() {
        assertNotNull(taskManager.getTaskMap(), "карта задач не инициализирована");
        assertEquals(taskManager.getTaskMap().size(), 1, "не верное количество задач");
        assertEquals(taskManager.getTaskMap().get(1), task, "задачи не совпадают");
    }

    @Test
    void getEpicMap() {
        assertNotNull(taskManager.getTaskMap(), "карта эпиков не инициализирована");
        assertEquals(taskManager.getEpicMap().size(), 1, "не верное количество задач");
        assertEquals(taskManager.getEpicMap().get(2), epic, "задачи не совпадают");
    }

    @Test
    void getHistoryManager() {
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubTask(2, 3);
        assertEquals(taskManager.getHistoryManager().getSize(), 3, "не верное количество просмотров");
    }

    @Test
    void updateTask() {
        Task taskUpdate = new Task(TypeTask.TYPE_TASK, "testTask1", "...", 1, Status.TASK_NEW, ZonedDateTime.of(LocalDateTime.of(2030, 1, 1, 21, 1), zoneId), Duration.ofHours(3));
        taskManager.updateTask(taskUpdate, 1);
        assertNotNull(taskManager.getTask(1),"задача потеряна при обновлении");
        assertEquals(taskUpdate, taskManager.getTask(1), "задача не обновляется");
    }

    @Test
    void updateEpic() {
        Epic epicUpdate = new Epic(TypeTask.TYPE_EPIC, "epicTest1", "...", 2, Status.DONE, ZonedDateTime.of(LocalDateTime.of(2028, 1, 11, 21, 1), zoneId), Duration.ofHours(3));
        List<SubTask> subtaskTest = taskManager.getEpic(2).subTasks;
        taskManager.updateEpic(epicUpdate, 2);
        assertNotNull(taskManager.getEpic(2),"эпик потерян при обновлении");
        assertEquals(epicUpdate, taskManager.getEpic(2), "эпик не обновляется");
        assertEquals(taskManager.getEpic(2).subTasks.size(), subtaskTest.size(), "не корректное обновление ");
    }

    @Test
    void updateSubTask() {
        SubTask subTaskUpdate = new SubTask(TypeTask.TYPE_SUBTASK, "subtaskTest1", "update", 3, Status.TASK_NEW, epic.getId(), ZonedDateTime.of(LocalDateTime.of(2028, 1, 13, 21, 1), zoneId), Duration.ofHours(3));
        taskManager.updateSubTask(subTaskUpdate);
        assertNotNull(subTask,"подзадача потеряна при обновлении");
        assertEquals(subTaskUpdate,taskManager.getSubTask(subTask.getEpic(),subTask.getId()),"подзадача не обновлена");
    }

    @Test
    void addTaskToMap() {
    }

    @Test
    void addEpicToMap() {
    }

    @Test
    void addSubTaskMap() {
    }

    @Test
    void setStatusSubTask() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void deleteTaskById() {
    }

    @Test
    void deleteEpicById() {
    }

    @Test
    void deleteSubTaskById() {
    }

    @Test
    void clearTaskMap() {
    }

    @Test
    void clearEpicMap() {
    }

    @Test
    void removeEverythingCompletely() {
    }

    @Test
    void getEpic() {


    }

    @Test
    void getTask() {
        assertNotNull(taskManager.getTask(1), "пустая задача");
        assertEquals(task, taskManager.getTask(1), "задачи не совпадают");
    }

    @Test
    void getSubTask() {

    }
}