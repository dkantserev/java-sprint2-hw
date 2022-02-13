package tracker;

import tracker.history.HistoryManager;
import tracker.history.InMemoryHistoryManager;

public  class Manager { // служебный класс
    public static TaskManager  getDefault() { // возвращает объект класса
        return new InMemoryTaskManager();            // управляющего хранением и обработкой задач
    }

    public static HistoryManager getDefaultHistory(){ //возвращает объект класса управляющего хранением истории
        return new InMemoryHistoryManager();
    }

}