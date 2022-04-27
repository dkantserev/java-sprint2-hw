package tracker;

import tracker.KVKlient.KVClient;
import tracker.Serializer.TaskJsonSerializer;
import tracker.Serializer.TaskSerializer;
import tracker.history.HistoryManager;
import tracker.history.InMemoryHistoryManager;

import java.io.File;

public class Manager { // служебный класс

    public static TaskManager getDefault() {         // возвращает объект класса
        File file = new File("testServer.csv");
        String URL = "http://localhost:8078";
        TaskSerializer taskSerializer = new TaskJsonSerializer();
        KVClient kvClient = new KVClient(URL, taskSerializer);
        kvClient.register();
        return new HTTPTaskManager(file,kvClient);   // управляющего хранением и обработкой задач
    }

    public static HistoryManager getDefaultHistory() { //возвращает объект класса управляющего хранением истории
        return new InMemoryHistoryManager();
    }

}