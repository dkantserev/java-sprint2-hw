package tracker.Tasks;

import java.util.ArrayList;

public class Epic extends Task { // Грандиозная задача с подзадачами.
    public ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String nameTask, String taskBody, int id, Status status) {
        super(nameTask, taskBody, id, status);
    }

    @Override
    public String toString() {
        return "Epic{" +

                ", nameTask='" + nameTask + '\'' +
                ", taskBody='" + taskBody + '\'' +
                ", id=" + id +
                ", status=" + status +
                " subTasks=" + subTasks +
                '}';
    }
}
