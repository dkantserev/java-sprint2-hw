import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");
        Manager manager = new Manager();
        Task task1 = new Task("найти решение","как то разобраться",2, true,false,false);
        Epic epic1= new Epic("купить машину","подобрать машину по параметрам",1,true,false,false);
        SubTask subTask1 = new SubTask("выбрать марку","посмотреть предложение на рынке",1, true,false,false);
        SubTask subTask2 = new SubTask("подходящая цена","а хватит ли денег?",1,true,false,false);
        Epic epic2 = new Epic("купить питомца","подобрать подходящего петомца",2,true,false,false);
        SubTask subTask3 = new SubTask("проверить подходит ли", "нет ли алергии, подходят ли условия",2,true,false,false);
        SubTask subTask4 = new SubTask("царпается", "проверить сертификаты на прививки",3,true,false,false);

        manager.addSubTaskMap(subTask1,epic1);
        manager.addSubTaskMap(subTask2,epic1);
        manager.addTaskToMap(task1.getId(),task1);
        manager.addEpicToMap(epic1.getId(),epic1);

        manager.addEpicToMap(2,epic2);
        manager.addSubTaskMap(subTask3,epic2);
        manager.addSubTaskMap(subTask4,epic2);

        manager.setStatusDoneSubTask(1,subTask1);
        manager.setStatusProgressSubTask(1,subTask2);
        //System.out.println(manager.epicMap.get(1).subTasks);
        //System.out.println(manager.epicMap.get(1));
       // System.out.println(manager.epicMap.get(2).subTasks);
        manager.setStatusProgress(task1.getId());
       // System.out.println(manager.taskMap.get(2));
        manager.setStatusDone(task1.getId());
        manager.setStatusDoneSubTask(1,subTask2);
       // System.out.println(manager.epicMap.get(1));
        //System.out.println(manager.taskMap.get(2));
        //System.out.println(manager.epicMap.get(1));
        //manager.removeEverythingCompletely();
       // System.out.println(manager.epicMap.get(1));
        manager.printAllTasks();














    }
}
