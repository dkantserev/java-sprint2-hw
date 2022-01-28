public class Task { // Родительский класс для всех типов задач.
     protected String nameTask;
     protected String taskBody;
     protected int id;
     protected boolean taskNew;
     protected boolean inProgress;
     protected boolean done;

    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", taskBody='" + taskBody + '\'' +
                ", id=" + id +
                ", taskNew=" + taskNew +
                ", inProgress=" + inProgress +
                ", done=" + done +
                '}';
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getTaskBody() {
        return taskBody;
    }

    public void setTaskBody(String taskBody) {
        this.taskBody = taskBody;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTaskNew() {
        return taskNew;
    }

    public void setTaskNew(boolean taskNew) {
        this.taskNew = taskNew;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Task(String nameTask, String taskBody, int id, boolean taskNew, boolean inProgress, boolean done) {
        this.nameTask = nameTask;
        this.taskBody = taskBody;
        this.id = id;
        this.taskNew = taskNew;
        this.inProgress = inProgress;
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (taskNew != task.taskNew) return false;
        if (inProgress != task.inProgress) return false;
        if (done != task.done) return false;
        if (nameTask != null ? !nameTask.equals(task.nameTask) : task.nameTask != null) return false;
        return taskBody != null ? taskBody.equals(task.taskBody) : task.taskBody == null;
    }

    @Override
    public int hashCode() {
        int result = nameTask != null ? nameTask.hashCode() : 0;
        result = 31 * result + (taskBody != null ? taskBody.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (taskNew ? 1 : 0);
        result = 31 * result + (inProgress ? 1 : 0);
        result = 31 * result + (done ? 1 : 0);
        return result;
    }
}
