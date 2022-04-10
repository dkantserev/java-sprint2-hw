package tracker;

import org.junit.jupiter.api.Test;
import tracker.Tasks.*;
import tracker.exception.OverlappingException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest<C extends TaskManager> {

    protected C taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;
    protected SubTask subTask2;
    protected ZoneId zoneId = ZoneId.systemDefault();

    protected void creatorTestManager() {
        task = new Task(TypeTask.TYPE_TASK, "testTask1", "...", 1, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2028, 1, 1, 21, 1), zoneId)
                , Duration.ofHours(3));
        epic = new Epic(TypeTask.TYPE_EPIC, "epicTest1", "...", 2, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2028, 1, 11, 21, 1), zoneId)
                , Duration.ofHours(3));
        subTask = new SubTask(TypeTask.TYPE_SUBTASK, "subtaskTest1", "...", 3
                , Status.TASK_NEW, epic.getId(), ZonedDateTime.of(LocalDateTime.of(2028, 1, 13
                , 21, 1), zoneId), Duration.ofHours(3));
        subTask2 = new SubTask(TypeTask.TYPE_SUBTASK, "subtaskTest1", "...", 4, Status.TASK_NEW
                , epic.getId(), ZonedDateTime.of(LocalDateTime.of(2028, 1, 14, 21, 1)
                , zoneId), Duration.ofHours(3));
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
        Task taskUpdate = new Task(TypeTask.TYPE_TASK, "testTask1", "...", 1, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2030, 1, 1, 21, 1), zoneId)
                , Duration.ofHours(3));
        taskManager.updateTask(taskUpdate, 1);
        assertNotNull(taskManager.getTask(1), "задача потеряна при обновлении");
        assertEquals(taskUpdate, taskManager.getTask(1), "задача не обновляется");
    }

    @Test
    void updateEpic() {
        Epic epicUpdate = new Epic(TypeTask.TYPE_EPIC, "epicTest1", "...", 2, Status.DONE
                , ZonedDateTime.of(LocalDateTime.of(2028, 1, 11, 21, 1), zoneId)
                , Duration.ofHours(3));
        List<SubTask> subtaskTest = taskManager.getEpic(2).subTasks;
        taskManager.updateEpic(epicUpdate, 2);
        assertNotNull(taskManager.getEpic(2), "эпик потерян при обновлении");
        assertEquals(epicUpdate, taskManager.getEpic(2), "эпик не обновляется");
        assertEquals(taskManager.getEpic(2).subTasks.size(), subtaskTest.size(), "не корректное обновление ");
    }

    @Test
    void updateSubTask() {
        SubTask subTaskUpdate = new SubTask(TypeTask.TYPE_SUBTASK, "subtaskTest1", "update", 3
                , Status.TASK_NEW, epic.getId(), ZonedDateTime.of(LocalDateTime.of(2028, 1, 13
                , 21, 1), zoneId), Duration.ofHours(3));
        taskManager.updateSubTask(subTaskUpdate);
        assertNotNull(subTask, "подзадача потеряна при обновлении");
        assertEquals(subTaskUpdate, taskManager.getSubTask(subTask.getEpic(), subTask.getId())
                , "подзадача не обновлена");
    }

    @Test
    void addTaskToMap() {
        Task taskTest = new Task(TypeTask.TYPE_TASK, "testTask1", "...", 11, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2038, 1, 1, 21, 1), zoneId)
                , Duration.ofHours(3));
        taskManager.addTaskToMap(taskTest.getId(), taskTest);
        assertEquals(taskManager.getTaskMap().size(), 2, "задача не добавлена");
        assertEquals(taskManager.getTask(taskTest.getId()), taskTest, "добавленная задача не совпадает");
    }

    @Test
    void addEpicToMap() {
        Epic epicTest = new Epic(TypeTask.TYPE_EPIC, "epicTest1", "...", 55, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(3028, 11, 11, 21, 1), zoneId)
                , Duration.ofHours(3));
        taskManager.addEpicToMap(epicTest.getId(), epicTest);
        assertEquals(taskManager.getEpicMap().size(), 2, "задача не добавлена");
        assertEquals(taskManager.getEpic(epicTest.getId()), epicTest, "добавленная задача не совпадает");
    }

    @Test
    void addSubTaskMap() {
        SubTask subTaskTest = subTask2 = new SubTask(TypeTask.TYPE_SUBTASK, "subtaskTest1", "..."
                , 88, Status.TASK_NEW, epic.getId(), ZonedDateTime.of(LocalDateTime.of(2228, 1
                , 14, 21, 1), zoneId), Duration.ofHours(3));
        taskManager.addSubTaskMap(subTaskTest, subTaskTest.getEpic());
        assertEquals(taskManager.getEpic(subTaskTest.getEpic()).subTasks.size(), 3
                , "подзадача не добавлена");
        assertEquals(taskManager.getSubTask(subTaskTest.getEpic(), subTaskTest.getId()), subTaskTest
                , "подзадача не совпадает");
    }

    @Test
    void setStatusSubTask() {
        taskManager.setStatusSubTask(subTask, Status.IN_PROGRESS);
        assertEquals(taskManager.getSubTask(subTask.getEpic(), subTask.getId()).getStatus(), Status.IN_PROGRESS
                , "не верный статус");
        assertEquals(taskManager.getEpic(subTask.getEpic()).getStatus(), Status.IN_PROGRESS
                , "не верный расчет статуса эпика");
        taskManager.setStatusSubTask(subTask, Status.DONE);
        assertEquals(taskManager.getSubTask(subTask.getEpic(), subTask.getId()).getStatus(), Status.DONE
                , "не верный статус");
        assertEquals(taskManager.getEpic(subTask.getEpic()).getStatus(), Status.IN_PROGRESS
                , "не верный расчет статуса эпика");
        taskManager.setStatusSubTask(subTask2, Status.DONE);
        assertEquals(taskManager.getEpic(subTask.getEpic()).getStatus(), Status.DONE
                , "не верный расчет статуса эпика");
        taskManager.setStatusSubTask(subTask, Status.TASK_NEW);
        assertEquals(taskManager.getSubTask(subTask.getEpic(), subTask.getId()).getStatus()
                , Status.TASK_NEW, "не верный статус");
        assertEquals(taskManager.getEpic(subTask.getEpic()).getStatus(), Status.IN_PROGRESS
                , "не верный расчет статуса эпика");
        taskManager.setStatusSubTask(subTask2, Status.TASK_NEW);
        assertEquals(taskManager.getEpic(subTask.getEpic()).getStatus(), Status.TASK_NEW
                , "не верный расчет статуса эпика");


    }

    @Test
    void setStatus() {
        taskManager.setStatus(task, Status.IN_PROGRESS);
        assertEquals(taskManager.getTask(task.getId()).getStatus(), Status.IN_PROGRESS, "не верный статус");
        taskManager.setStatus(task, Status.DONE);
        assertEquals(taskManager.getTask(task.getId()).getStatus(), Status.DONE, "не верный статус");
        taskManager.setStatus(task, Status.TASK_NEW);
        assertEquals(taskManager.getTask(task.getId()).getStatus(), Status.TASK_NEW, "не верный статус");

    }

    @Test
    void deleteTaskById() {
        taskManager.deleteTaskById(task.getId());
        assertEquals(taskManager.getTaskMap().size(), 0, "задача не удалена");
    }

    @Test
    void deleteEpicById() {
        taskManager.deleteEpicById(epic.getId());
        assertEquals(taskManager.getEpicMap().size(), 0, "эпик не удален");
    }

    @Test
    void deleteSubTaskById() {
        taskManager.deleteSubTaskById(subTask.getEpic(), subTask.getId());
        assertEquals(taskManager.getEpic(epic.getId()).subTasks.size(), 1, "подзадача не удалена");
    }

    @Test
    void clearTaskMap() {
        taskManager.clearTaskMap();
        assertEquals(taskManager.getTaskMap().size(), 0, "список задач не очищен");
    }

    @Test
    void clearEpicMap() {
        taskManager.clearEpicMap();
        assertEquals(taskManager.getEpicMap().size(), 0, "список эпиков не очищен");
    }

    @Test
    void removeEverythingCompletely() {
        taskManager.getTask(1);
        taskManager.removeEverythingCompletely();
        assertEquals(taskManager.getTaskMap().size(), 0, "список задач не очищен");
        assertEquals(taskManager.getEpicMap().size(), 0, "список эпиков не очищен");
        assertEquals(taskManager.getHistoryManager().getHistory().size(), 0, "история не очищена");
    }

    @Test
    void getEpic() {
        assertNotNull(taskManager.getEpic(2), "пустой эпик");
        assertEquals(taskManager.getEpic(2), epic, "эпики не совпадают");
        assertEquals(taskManager.getEpic(2).subTasks.size(), epic.subTasks.size()
                , "эпики не совпадают по подзадачам");
    }

    @Test
    void getTask() {
        assertNotNull(taskManager.getTask(1), "пустая задача");
        assertEquals(task, taskManager.getTask(1), "задачи не совпадают");
    }

    @Test
    void getSubTask() {
        assertNotNull(taskManager.getSubTask(epic.getId(), subTask.getId()), "пустая подзадача");
        assertEquals(taskManager.getSubTask(epic.getId(), subTask.getId()), subTask, "подзадачи не совпадают");
    }

    @Test
    void overlapping() {
        Task taskOver = new Task(TypeTask.TYPE_TASK, "testTask1", "...", 111, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2028, 1, 1, 21, 1), zoneId)
                , Duration.ofHours(3));
        try {
            taskManager.addTaskToMap(taskOver.getId(), taskOver);
        } catch (OverlappingException e) {
            assertEquals(e.getMessage(), "пересечение времени", "прохождение дат с пересечением");
        }
    }

    @Test
    void getPrioritizedTasks() {
        TreeSet<Task> testTree = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        testTree.add(task);
        testTree.add(subTask);
        testTree.add(subTask2);
        assertArrayEquals(testTree.toArray(), taskManager.getPrioritizedTasks().toArray(new Task[0])
               , "не верный список приорететов");
        assertEquals(testTree.first(), taskManager.getPrioritizedTasks().first()
                , "не верный список приорететов");
        assertEquals(testTree.last(), taskManager.getPrioritizedTasks().last()
                , "не верный список приорететов");
    }

    @Test
    void TaskToString() {
        assertEquals(task.toString(), "TYPE_TASK,1,testTask1,...,TASK_NEW,2028-01-01-09-01,PT3H"
                , "не верный формат toString");
        assertEquals(epic.toString(), "TYPE_EPIC,2,epicTest1,...,TASK_NEW,2028-01-11-09-01,PT3H"
                , "не верный формат toString");
        assertEquals(subTask.toString(), "TYPE_SUBTASK,3,subtaskTest1,...,TASK_NEW,2,2028-01-13-09-01,PT3H"
                , "не верный формат toString");
    }

}