package tracker.Serializer;

import tracker.Tasks.Epic;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;



public interface TaskSerializer {
    String toObject(Task task);

    Task fromString(String string);

    Epic epicFromString(String string);

    SubTask subtaskFromString(String string);

}
