package tracker.Tasks;

import java.time.Duration;
import java.time.ZonedDateTime;

public class SubTask extends Task { // Подзадача в составе эпика.

    private int epic;


    public SubTask(TypeTask typeTask, String nameTask, String taskBody, int id, Status status, int epic, ZonedDateTime startTime, Duration duration) {
        super(typeTask,nameTask, taskBody, id, status,startTime,duration);
        this.epic = epic;
        this.typeTask=typeTask;
    }

    public int getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return typeTask + "," +
                id + "," +
                nameTask + "," +
                taskBody + "," +
                status +"," +
                epic + "," +
                startTime + "," +
                duration
                ;
    }
}
