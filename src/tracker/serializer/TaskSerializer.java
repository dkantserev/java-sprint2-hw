package tracker.serializer;

import tracker.tasks.Epic;
import tracker.tasks.SubTask;
import tracker.tasks.Task;



public interface TaskSerializer {
    String toObject(Task task);

    Task fromString(String string);

    Epic epicFromString(String string);

    SubTask subtaskFromString(String string);

}
