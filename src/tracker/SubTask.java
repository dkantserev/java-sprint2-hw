package tracker;
public class SubTask extends Task { // Подзадача в составе эпика.

    private Epic epic;

    public SubTask(String nameTask, String taskBody, int id, boolean taskNew, boolean inProgress, boolean done, Epic epic) {
        super(nameTask, taskBody, id, taskNew, inProgress, done);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "tracker.SubTask{" +
                "nameTask='" + nameTask + '\'' +
                ", taskBody='" + taskBody + '\'' +
                ", id=" + id +
                ", taskNew=" + taskNew +
                ", inProgress=" + inProgress +
                ", done=" + done +
                '}';
    }
}
