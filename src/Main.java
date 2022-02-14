import tracker.*;
import tracker.Tasks.Epic;
import tracker.Tasks.Status;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;

public class Main { // Класс для тестирование работы классов менеджер и задач.
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        TaskManager manager = new Manager().getDefault();
        Task task1 = new Task("поесть ", "найти еду", 2, Status.TASK_NEW);
        Epic epic1 = new Epic("сделать проект 2го спринта",
                "написать код отвечающий требованиям ревьюера", 1, Status.TASK_NEW);
        SubTask subTask1 = new SubTask("изучить ТЗ", "ознакомится с требованиями проекта", 1,
                Status.TASK_NEW, epic1);
        SubTask subTask2 = new SubTask("написать код", "создать логику отвечающую требованиям ТЗ",
                2, Status.TASK_NEW, epic1);
        Epic epic2 = new Epic("отдохнуть", "набраться сил перед новой рабочей неделей", 2,
                Status.TASK_NEW);
        SubTask subTask3 = new SubTask("выспаться", "пораньше лечь попозже встать", 1,
                Status.TASK_NEW, epic2);
        Epic test = new Epic("сccccccccccccccделать проект 2го спринта",
                "написать код отвечающий требованиям ревьюера", 1, Status.TASK_NEW);
        SubTask testSub = new SubTask("изучить ТЗ", "ознакомится с требованиями проекта", 1,
                Status.TASK_NEW, epic1);

        manager.addTaskToMap(task1.getId(), task1);
        manager.addEpicToMap(epic1.getId(), epic1);
        manager.addSubTaskMap(subTask1, epic1);
        manager.addSubTaskMap(subTask2, epic1);
        manager.addEpicToMap(epic2.getId(), epic2);
        manager.addSubTaskMap(subTask3, epic2);
        manager.printAllTasks();
        System.out.println(manager.getEpicMap());
        System.out.println(manager.getTaskMap());
        manager.getEpicMap().get(1).getStatus();
        manager.setStatusSubTask(subTask1,Status.DONE);
        manager.setStatus(task1,Status.IN_PROGRESS);
        System.out.println(manager.getEpicMap());
        System.out.println(manager.getTaskMap());
        manager.setStatus(task1,Status.TASK_NEW);
        manager.setStatusSubTask(subTask2,Status.IN_PROGRESS);
        manager.getEpic(1);
        manager.getTask(2);
        manager.getSubTask(1, 1);
        manager.getEpic(1);
        manager.getTask(2);
        manager.getSubTask(1, 1);
        manager.getHistoryManager().getHistory();
        manager.getEpic(1);
        manager.getTask(2);
        manager.getSubTask(1, 1);
        manager.getSubTask(1, 1);
        manager.getSubTask(1, 1);
        manager.getSubTask(1, 1);
        manager.getHistoryManager().getHistory();
        System.out.println(manager.getEpicMap());
        System.out.println(manager.getTaskMap());
        manager.updateEpic(test);
        System.out.println(manager.getEpic(1));
        manager.updateSubTask(testSub);
        System.out.println(manager.getEpic(1));
        manager.deleteTaskById(2);
        manager.deleteEpicById(2);
        manager.deleteSubTaskById(1, 1);
        manager.setStatusSubTask(subTask2,Status.DONE);
        System.out.println(manager.getEpicMap());
        System.out.println(manager.getTaskMap());
        manager.removeEverythingCompletely();
        System.out.println(manager.getEpicMap());
        System.out.println(manager.getTaskMap());
        manager.getHistoryManager().getHistory();

    }
}
