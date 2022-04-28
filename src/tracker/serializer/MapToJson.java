package tracker.serializer;
import tracker.tasks.Epic;
import tracker.tasks.SubTask;
import tracker.tasks.Task;
import tracker.history.HistoryManager;
import java.util.HashMap;
import java.util.TreeSet;

public class MapToJson {
    public static String epicMapToJsonList(HashMap<Integer, Epic> epicMap, TaskSerializer taskSerializer) {
        StringBuilder ret = new StringBuilder();
        for (Epic value : epicMap.values()) {
            ret.append(taskSerializer.toObject(value));
            ret.append("%");
        }
        ret.delete(ret.length() - 1, ret.length());
        return ret.toString();
    }

    public static String taskMapToJsonList(HashMap<Integer, Task> taskMap, TaskSerializer taskSerializer) {
        StringBuilder ret = new StringBuilder();
        for (Task o : taskMap.values()) {
            ret.append(taskSerializer.toObject(o));
            ret.append("%");
        }
        ret.delete(ret.length() - 1, ret.length());
        return ret.toString();
    }

    public static String subtaskMapToJsonList(HashMap<Integer, Epic> epicMap, TaskSerializer taskSerializer) {
        StringBuilder ret = new StringBuilder();
        for (Epic value : epicMap.values()) {
            for (SubTask subTask : value.subTasks) {
                ret.append(taskSerializer.toObject(subTask));
                ret.append("%");
            }
        }
        ret.delete(ret.length() - 1, ret.length());
        return ret.toString();
    }

    public static String historyMapToJsonList(HistoryManager historyManager, TaskSerializer taskSerializer) {
        StringBuilder ret = new StringBuilder();
        for (Task value : historyManager.getHistory()) {
            ret.append(taskSerializer.toObject(value));
            ret.append("%");
        }
        ret.delete(ret.length() - 1, ret.length());
        return ret.toString();
    }

    public static String prioritizedMapToJsonList(TreeSet<Task> prioritized, TaskSerializer taskSerializer) {
        StringBuilder ret = new StringBuilder();
        for (Task value : prioritized) {
            ret.append(taskSerializer.toObject(value));
            ret.append("%");
        }
        ret.delete(ret.length() - 1, ret.length());
        return ret.toString();
    }

}
