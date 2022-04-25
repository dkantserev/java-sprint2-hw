package tracker.Serializer;

import tracker.Tasks.Task;

public interface TaskSerializer {
    String toObject(Task task);

    Task fromString(String string);
}
