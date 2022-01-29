package tracker;
import java.util.ArrayList;

public class Epic extends Task { // Грандиозная задача с подзадачами.
    ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String nameTask, String taskBody, int id, boolean taskNew, boolean inProgress, boolean done) {
        super(nameTask, taskBody, id, taskNew, inProgress, done);
    }

    @Override
    public String toString() {
        return "tracker.Epic{" +
                "nameTask='" + nameTask + '\'' +
                ", taskBody='" + taskBody + '\'' +
                ", id=" + id +
                ", taskNew=" + taskNew +
                ", inProgress=" + inProgress +
                ", done=" + done +
                ", subTasks " + subTasks + '}';
    }
}
