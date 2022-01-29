import tracker.Epic;
import tracker.Manager;
import tracker.SubTask;
import tracker.Task;

public class Main { // Класс для тестирование работы классов менеджер и задач.
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        Manager manager = new Manager();
        Task task1 = new Task("поесть ", "найти еду", 2, true, false, false);
        Epic epic1 = new Epic("сделать проект 2го спринта",
                "написать код отвечающий требованиям ревьюера", 1, true, false, false);
        SubTask subTask1 = new SubTask("изучить ТЗ", "ознакомится с требованиями проекта", 1,
                true, false, false, epic1);
        SubTask subTask2 = new SubTask("написать код", "создать логику отвечающую требованиям ТЗ",
                2, true, false, false, epic1);
        Epic epic2 = new Epic("отдохнуть", "набраться сил перед новой рабочей неделей", 2,
                true, false, false);
        SubTask subTask3 = new SubTask("выспаться", "пораньше лечь попозже встать", 1,
                true, false, false, epic2);

        manager.addTaskToMap(task1.getId(), task1);
        manager.addEpicToMap(epic1.getId(), epic1);
        manager.addSubTaskMap(subTask1, epic1);
        manager.addSubTaskMap(subTask2, epic1);
        manager.addEpicToMap(epic2.getId(), epic2);
        manager.addSubTaskMap(subTask3, epic2);
        manager.printAllTasks();
        System.out.println(manager.epicMap);
        System.out.println(manager.taskMap);
        manager.setStatusDoneSubTask(subTask1);
        manager.setStatusProgressSubTask(subTask2);
        manager.setStatusProgress(task1.getId());
        System.out.println(manager.epicMap);
        System.out.println(manager.taskMap);
        manager.setStatusDone(task1.getId());
        manager.setStatusDoneSubTask(subTask2);
        System.out.println(manager.epicMap);
        System.out.println(manager.taskMap);
        manager.deleteTaskById(2);
        manager.deleteEpicById(2);
        manager.deleteSubTaskById(1, 1);
        System.out.println(manager.epicMap);
        System.out.println(manager.taskMap);
        manager.removeEverythingCompletely();
        System.out.println(manager.epicMap);
        System.out.println(manager.taskMap);

    }
}
