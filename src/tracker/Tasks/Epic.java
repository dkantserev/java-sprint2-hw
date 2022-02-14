package tracker.Tasks;

import java.util.ArrayList;

public class Epic extends Task { // Грандиозная задача с подзадачами.
    public ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String nameTask, String taskBody, int id, Status status) {
        super(nameTask, taskBody, id, status);
    }

    @Override
    public Status getStatus() { // получает актуальный статус эпика
        int progress = 0;
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getStatus().equals(Status.DONE)) {
                progress++;
            }
            if (progress < subTasks.size() && progress > 0) {
                setStatus(Status.IN_PROGRESS);
            }
            if (progress == subTasks.size()) {
                setStatus(Status.DONE);
            }
        }
        return status;
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
