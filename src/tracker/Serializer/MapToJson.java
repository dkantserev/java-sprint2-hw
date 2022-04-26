package tracker.Serializer;

import tracker.TaskManager;
import tracker.Tasks.Epic;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;

public  class MapToJson {
    public static String epicMapToJsonList(TaskManager manager,TaskSerializer taskSerializer){
        StringBuilder ret = new StringBuilder();
        for (Epic value : manager.getEpicMap().values()) {
            ret.append(taskSerializer.toObject(value));
            ret.append(",");
        }
        ret.delete(ret.length()-1,ret.length());
        return ret.toString();
    }

    public static String taskMapToJsonList(TaskManager manager,TaskSerializer taskSerializer){
        StringBuilder ret = new StringBuilder();
        for (Task o : manager.getTaskMap().values()) {
            ret.append(taskSerializer.toObject(o));
            ret.append(",");
        }
        ret.delete(ret.length()-1,ret.length());
        return ret.toString();
    }

    public static String subtaskMapToJsonList(TaskManager manager,TaskSerializer taskSerializer){
        StringBuilder ret= new StringBuilder();
        for (Epic value : manager.getEpicMap().values()) {
            for (SubTask subTask : value.subTasks) {
                ret.append(taskSerializer.toObject(subTask));
                ret.append(",");
            }
        }
        ret.delete(ret.length()-1,ret.length());
        return ret.toString();
    }
    public static String historyMapToJsonList(TaskManager manager,TaskSerializer taskSerializer){
        StringBuilder ret= new StringBuilder();
        for (Task value : manager.getHistoryManager().getHistory()) {
                ret.append(taskSerializer.toObject(value));
                ret.append(",");
        }
        ret.delete(ret.length()-1,ret.length());
        return ret.toString();
    }

    public static String prioritizedMapToJsonList(TaskManager manager,TaskSerializer taskSerializer){
        StringBuilder ret = new StringBuilder();
        for (Task value : manager.getPrioritizedTasks()) {
            ret.append(taskSerializer.toObject(value));
            ret.append(",");
        }
        ret.delete(ret.length()-1,ret.length());
        return ret.toString();
    }

}
