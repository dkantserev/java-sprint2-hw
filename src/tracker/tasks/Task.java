package tracker.tasks;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Task { // Родительский класс для всех типов задач.
    protected String nameTask;
    protected String taskBody;
    protected int id;
    protected Status status;
    protected TypeTask typeTask;
    protected ZonedDateTime startTime;
    protected Duration duration;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm");

    public TypeTask getTypeTask() {
        return typeTask;
    }

    public Task(TypeTask typeTask, String nameTask, String taskBody, int id, Status status, ZonedDateTime startTime
            , Duration duration) {
        this.nameTask = nameTask;
        this.taskBody = taskBody;
        this.id = id;
        this.status = status;
        this.typeTask = typeTask;
        this.startTime = startTime;
        this.duration = duration;
    }



    public ZonedDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getTaskBody() {
        return taskBody;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task protoTask = (Task) o;

        if (id != protoTask.id) return false;
        if (nameTask != null ? !nameTask.equals(protoTask.nameTask) : protoTask.nameTask != null) return false;
        if (taskBody != null ? !taskBody.equals(protoTask.taskBody) : protoTask.taskBody != null) return false;
        return status == protoTask.status;
    }

    @Override
    public int hashCode() {
        int result = nameTask != null ? nameTask.hashCode() : 0;
        result = 31 * result + (taskBody != null ? taskBody.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return typeTask + "," +
                id + "," +
                nameTask + "," +
                taskBody + "," +
                status + "," +
                startTime.format(formatter) + "," +
                duration
                ;
    }
}
