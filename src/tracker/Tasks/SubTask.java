package tracker.Tasks;

public class SubTask extends Task { // Подзадача в составе эпика.

    private int epic;


    public SubTask(TypeTask typeTask,String nameTask, String taskBody, int id, Status status, int epic) {
        super(typeTask,nameTask, taskBody, id, status);
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
                epic
                ;
    }
}
