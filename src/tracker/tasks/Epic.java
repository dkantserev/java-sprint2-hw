package tracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;


public class Epic extends Task { // Грандиозная задача с подзадачами.

    public ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(TypeTask typeTask, String nameTask, String taskBody, int id, Status status
            , ZonedDateTime startTime, Duration duration) {
        super(typeTask, nameTask, taskBody, id, status,startTime,duration);
    }

    @Override
    public ZonedDateTime getEndTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(1,1,1,1,1)
                ,ZoneId.systemDefault());
        if(subTasks.isEmpty()){
            return super.getEndTime();
        }
        for (SubTask subTask : subTasks) {
            if(zonedDateTime.isBefore(subTask.getEndTime())){
                zonedDateTime=subTask.getEndTime();
            }
        }
        return zonedDateTime;
    }

    @Override
    public ZonedDateTime getStartTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.of(1111111,1,1,1,1)
                ,ZoneId.systemDefault());
       if(subTasks.isEmpty()){
           return startTime;
       }
       else{
           for (SubTask subTask : subTasks) {
               if(subTask.getStartTime().isBefore(zonedDateTime)){
                   zonedDateTime=subTask.getStartTime();
               }
           }
       }
       return zonedDateTime;
    }

    @Override
    public Status getStatus() { // получает актуальный статус эпика
        int progress = 0;
        int regress =0;
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
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.TASK_NEW)) {
                regress++;
            }
            if (regress == subTasks.size()) {
                setStatus(Status.TASK_NEW);
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
                status + "," +
                startTime.format(formatter) + "," +
                duration
                ;
    }
}
