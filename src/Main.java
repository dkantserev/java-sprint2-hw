import tracker.*;
import tracker.KVKlient.KVClient;
import tracker.KVServer.KVServer;
import tracker.Serializer.TaskJsonSerializer;
import tracker.Serializer.TaskSerializer;
import tracker.Tasks.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;



public class Main { // Класс для тестирование работы классов менеджер и задач.
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Пришло время практики!");
        File file = new File("test.csv");
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        ZoneId zoneId = ZoneId.systemDefault();
        Task task1 = new Task(TypeTask.TYPE_TASK, "Таск1", "задача", InMemoryTaskManager.generaticId(),
                Status.TASK_NEW,ZonedDateTime.of(LocalDateTime.of(2028,1,1,21,1),zoneId),Duration.ofHours(3));
        Task task2 = new Task(TypeTask.TYPE_TASK, "Таск2", "задача", InMemoryTaskManager.generaticId(),
                Status.DONE,ZonedDateTime.of(LocalDateTime.of(2023,1,11,1,1),zoneId),Duration.ofHours(2));
        Epic epic1 = new Epic(TypeTask.TYPE_EPIC, "Эпик1",
                "задача", InMemoryTaskManager.generaticId(), Status.TASK_NEW,ZonedDateTime.of(LocalDateTime.of(2024,1,11,1,1),zoneId),Duration.ofHours(7));
        SubTask subTask1 = new SubTask(TypeTask.TYPE_SUBTASK, "сабтаск1", "ooo",
                InMemoryTaskManager.generaticId(),
                Status.TASK_NEW, epic1.getId(),ZonedDateTime.of(LocalDateTime.of(2025,1,1,1,1),zoneId),Duration.ofHours(5));
        SubTask subTask2 = new SubTask(TypeTask.TYPE_SUBTASK, "сабтаск2", "ooooo",
                InMemoryTaskManager.generaticId(), Status.TASK_NEW, epic1.getId(),ZonedDateTime.of(LocalDateTime.of(2026,11,1,1,1),zoneId),Duration.ofHours(8));
        Epic epic2 = new Epic(TypeTask.TYPE_EPIC, "эпик2", "oooo", InMemoryTaskManager.generaticId(),
                Status.TASK_NEW,ZonedDateTime.of(LocalDateTime.of(2027,1,14,1,1),zoneId),Duration.ofHours(5));

      KVServer server = new KVServer();
       server.start();
        String URL = "http://localhost:8078";
        TaskSerializer taskSerializer = new TaskJsonSerializer();
        KVClient KVClient = new KVClient(URL,taskSerializer);
        KVClient.register();
        KVClient.save(task1);
        System.out.println(KVClient.load(task1.getId()));
        System.out.println(task1);
        /*String test ="";
        test=new TaskJsonSerializer().toObject(task1);
        System.out.println(test);
        Task task66 = new TaskJsonSerializer().fromString(test);
        System.out.println(task66.getTaskBody());*/







        manager.addTaskToMap(task1.getId(), task1);
        manager.addEpicToMap(epic1.getId(), epic1);
        manager.addSubTaskMap(subTask1, epic1.getId());
        manager.addSubTaskMap(subTask2, epic1.getId());
        manager.addEpicToMap(epic2.getId(), epic2);

        manager.addTaskToMap(task2.getId(), task2);
        System.out.println(manager.getEpic(epic1.getId()).getEndTime());
        System.out.println(manager.getSubTask(epic1.getId(),subTask1.getId()).getEndTime());
        System.out.println(manager.getSubTask(epic1.getId(),subTask2.getId()).getEndTime());
        System.out.println(manager.getEpic(epic1.getId()).getStartTime());
        System.out.println(manager.getSubTask(epic1.getId(),subTask1.getId()).getStartTime());
        System.out.println(manager.getSubTask(epic1.getId(),subTask2.getId()).getStartTime());
        System.out.println(manager.getEpic(epic2.getId()).getStartTime());
        System.out.println(manager.getPrioritizedTasks());



        manager.getSubTask(epic1.getId(), subTask1.getId());
        System.out.println(manager.getHistoryManager().getHistory());

        System.out.println(manager.getHistoryManager().getHistory());
        manager.getSubTask(epic1.getId(), subTask1.getId());
        manager.getSubTask(epic1.getId(), subTask2.getId());

        System.out.println(manager.getHistoryManager().getSize());
        System.out.println(manager.getHistoryManager().getHistory());
        manager.getSubTask(epic1.getId(), subTask1.getId());
        manager.getSubTask(epic1.getId(), subTask2.getId());

        System.out.println(manager.getHistoryManager().getHistory());


        System.out.println(manager.getHistoryManager().getHistory());

        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());
        // manager.removeEverythingCompletely();
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());
        // manager.removeEverythingCompletely();
        System.out.println(manager.getTaskMap());
        System.out.println(manager.getEpicMap());
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());


    }
}
