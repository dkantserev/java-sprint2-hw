import tracker.*;
import tracker.Tasks.Epic;
import tracker.Tasks.Status;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;

public class Main { // Класс для тестирование работы классов менеджер и задач.
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        TaskManager manager = new Manager().getDefault();
        Task task1 = new Task("Таск1", "задача", InMemoryTaskManager.generaticId(), Status.TASK_NEW);
        Epic epic1 = new Epic("Эпик1",
                "задача", InMemoryTaskManager.generaticId(), Status.TASK_NEW);
        SubTask subTask1 = new SubTask("сабтаск1", "  ", InMemoryTaskManager.generaticId(),
                Status.TASK_NEW, epic1);
        SubTask subTask2 = new SubTask("сабтаск2", " ",
                InMemoryTaskManager.generaticId(), Status.TASK_NEW, epic1);
        Epic epic2 = new Epic("эпик2", "  ", InMemoryTaskManager.generaticId(),
                Status.TASK_NEW);
        SubTask subTask3 = new SubTask("сабтаск3", "  ", InMemoryTaskManager.generaticId(),
                Status.TASK_NEW, epic1);
        Epic epic11 = new Epic("эпик11", "  ", InMemoryTaskManager.generaticId(), Status.IN_PROGRESS);
        Epic epic311 = new Epic("эпик311", "  ", InMemoryTaskManager.generaticId(), Status.IN_PROGRESS);
        Epic epic211 = new Epic("эпик211", "  ", InMemoryTaskManager.generaticId(), Status.IN_PROGRESS);


        manager.addTaskToMap(task1.getId(), task1);
        manager.addEpicToMap(epic1.getId(), epic1);
        manager.addSubTaskMap(subTask1, epic1);
        manager.addSubTaskMap(subTask2, epic1);
        manager.addEpicToMap(epic2.getId(), epic2);
        manager.addEpicToMap(epic11.getId(), epic11);
        manager.addEpicToMap(epic311.getId(), epic311);
        manager.addEpicToMap(epic211.getId(), epic211);

        manager.addSubTaskMap(subTask3, epic1);
        manager.getSubTask(epic1.getId(), subTask1.getId());
        System.out.println(manager.getHistoryManager().getHistory());
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
        manager.removeEverythingCompletely();
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());


    }
}
