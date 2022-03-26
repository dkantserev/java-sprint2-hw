package tracker.Tasks;

import java.util.ArrayList;

public class Epic extends Task { // Грандиозная задача с подзадачами.
    public ArrayList<SubTask> subTasks = new ArrayList<>();


    public Epic(TypeTask typeTask, String nameTask, String taskBody, int id, Status status) {
        super(typeTask, nameTask, taskBody, id, status);


    }

    @Override
    public Status getStatus() { // получает актуальный статус эпика
        int progress = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                setStatus(Status.IN_PROGRESS);
            }
        }
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.DONE)) {
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
        return typeTask + "," +
                id + "," +
                nameTask + "," +
                taskBody + "," +
                status
                ;
    }
}
