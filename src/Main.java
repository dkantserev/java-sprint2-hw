import tracker.*;
import tracker.Tasks.*;

import java.io.File;

public class Main { // Класс для тестирование работы классов менеджер и задач.
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        File file = new File("test.csv");
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        Task task1 = new Task(TypeTask.TYPE_TASK, "Таск1", "задача", InMemoryTaskManager.generaticId(),
                Status.TASK_NEW);
        Task task2 = new Task(TypeTask.TYPE_TASK, "Таск2", "задача", InMemoryTaskManager.generaticId(),
                Status.DONE);
        Epic epic1 = new Epic(TypeTask.TYPE_EPIC, "Эпик1",
                "задача", InMemoryTaskManager.generaticId(), Status.TASK_NEW);
        SubTask subTask1 = new SubTask(TypeTask.TYPE_SUBTASK, "сабтаск1", "ooo",
                InMemoryTaskManager.generaticId(),
                Status.TASK_NEW, epic1.getId());
        SubTask subTask2 = new SubTask(TypeTask.TYPE_SUBTASK, "сабтаск2", "ooooo",
                InMemoryTaskManager.generaticId(), Status.TASK_NEW, epic1.getId());
        Epic epic2 = new Epic(TypeTask.TYPE_EPIC, "эпик2", "oooo", InMemoryTaskManager.generaticId(),
                Status.TASK_NEW);
        SubTask subTask3 = new SubTask(TypeTask.TYPE_SUBTASK, "сабтаск3", "  ",
                InMemoryTaskManager.generaticId(),
                Status.TASK_NEW, epic2.getId());
        Epic epic11 = new Epic(TypeTask.TYPE_EPIC, "эпик11", "  ",
                InMemoryTaskManager.generaticId(), Status.IN_PROGRESS);
        Epic epic311 = new Epic(TypeTask.TYPE_EPIC, "эпик311", "  ",
                InMemoryTaskManager.generaticId(), Status.IN_PROGRESS);
        Epic epic211 = new Epic(TypeTask.TYPE_EPIC, "эпик211", "  ",
                InMemoryTaskManager.generaticId(), Status.IN_PROGRESS);


        manager.addTaskToMap(task1.getId(), task1);
        manager.addEpicToMap(epic1.getId(), epic1);
        manager.addSubTaskMap(subTask1, epic1.getId());
        manager.addSubTaskMap(subTask2, epic1.getId());
        manager.addEpicToMap(epic2.getId(), epic2);
        manager.addEpicToMap(epic11.getId(), epic11);
        manager.addTaskToMap(task2.getId(), task2);
        manager.addEpicToMap(epic311.getId(), epic311);
        manager.addEpicToMap(epic211.getId(), epic211);
        manager.addSubTaskMap(subTask3, epic2.getId());
        manager.getSubTask(epic1.getId(), subTask1.getId());
        System.out.println(manager.getHistoryManager().getHistory());
        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        System.out.println(manager.getHistoryManager().getHistory());
        manager.getSubTask(epic1.getId(), subTask1.getId());
        manager.getSubTask(epic1.getId(), subTask2.getId());
        manager.getSubTask(epic1.getId(), subTask3.getId());
        System.out.println(manager.getHistoryManager().getSize());
        System.out.println(manager.getHistoryManager().getHistory());
        manager.getSubTask(epic1.getId(), subTask1.getId());
        manager.getSubTask(epic1.getId(), subTask2.getId());
        manager.getTask(task1.getId());
        System.out.println(manager.getHistoryManager().getHistory());
        manager.deleteEpicById(epic1.getId());
        System.out.println(manager.getHistoryManager().getHistory());
        manager.getEpic(epic11.getId());
        manager.getEpic(epic311.getId());
        manager.getEpic(epic211.getId());
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());
        // manager.removeEverythingCompletely();
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());
        // manager.removeEverythingCompletely();
        System.out.println(manager.getTaskMap());
        System.out.println(manager.getEpicMap());
        manager.getSubTaskMap();
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());


    }
}
