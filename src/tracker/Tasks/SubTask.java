package tracker.Tasks;

public class SubTask extends Task { // Подзадача в составе эпика.

    private Epic epic;

    public SubTask(String nameTask, String taskBody, int id, Status status, Epic epic) {
        super(nameTask, taskBody, id, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                ", nameTask='" + nameTask + "!!!!!!!!!!!";
    }
}
