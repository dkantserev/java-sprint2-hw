public class SubTask extends Task{

    public SubTask(String nameTask, String taskBody, int id, boolean taskNew, boolean inProgress, boolean done) {
        super(nameTask, taskBody, id, taskNew, inProgress, done);
    }
    @Override
    public String toString() {
        return "SubTask{" +
                "nameTask='" + nameTask + '\'' +
                ", taskBody='" + taskBody + '\'' +
                ", id=" + id +
                ", taskNew=" + taskNew +
                ", inProgress=" + inProgress +
                ", done=" + done +
                '}';
    }
}
