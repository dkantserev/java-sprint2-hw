package tracker;

import tracker.Tasks.*;
import tracker.exception.ManagerSaveException;
import tracker.history.HistoryManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FileBackedTasksManager extends InMemoryTaskManager { // класс с функцией загрузки из файла

    public static void main(String[] args) { // проверка корректности загрузки из файла
        File file = new File("test.csv");
        FileBackedTasksManager manager = loadFromFile(file);
        System.out.println(manager.getTaskMap());
        System.out.println(manager.getEpicMap());
        System.out.println("\n");
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println(manager.getHistoryManager().getSize());
        System.out.println((manager.getPrioritizedTasks()));
    }

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private static Task fromString(String readLine) { //создает Task из строки
        Task task;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm", Locale.ENGLISH);
        String[] split = readLine.split(",");
        ZonedDateTime zonedDateTime;
        Duration duration;
        Status status = Status.DONE;
        if (split[4].equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        }
        if (split[4].equals("DONE")) {
            status = Status.DONE;
        }
        if (split[4].equals("TASK_NEW")) {
            status = Status.TASK_NEW;
        }
        TypeTask typeTask = TypeTask.TYPE_SUBTASK;
        switch (split[0]) {
            case "TYPE_TASK":
                typeTask = TypeTask.TYPE_TASK;
                break;
            case "TYPE_EPIC":
                typeTask = TypeTask.TYPE_EPIC;
                break;
            case "TYPE_SUBTASK":
                typeTask = TypeTask.TYPE_SUBTASK;
        }

        int x = Integer.parseInt(split[1]);

        if (typeTask.equals(TypeTask.TYPE_SUBTASK)) {
            int s = Integer.parseInt(split[5]);
            zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(split[6], formatter), ZoneId.systemDefault());
            duration = Duration.parse(split[7]);
            return new SubTask(typeTask, split[2], split[3], x, status, s, zonedDateTime, duration);
        }

        if (typeTask.equals(TypeTask.TYPE_EPIC)) {
            zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(split[5], formatter), ZoneId.systemDefault());
            duration = Duration.parse(split[6]);
            return new Epic(typeTask, split[2], split[3], x, status, zonedDateTime, duration);
        } else {
            zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(split[5], formatter), ZoneId.systemDefault());
            duration = Duration.parse(split[6]);
            task = new Task(typeTask, split[2], split[3], x, status, zonedDateTime, duration);
        }
        return task;
    }

    protected static FileBackedTasksManager loadFromFile(File file) { //возвращает загруженный из файла FileBackedTasksManager
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.load(file);
        return fileBackedTasksManager;
    }

    private void load(File file) { // создаёт объекты менеджера из файла

        List<SubTask> listSub = new ArrayList<>();
        String control = "q";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isBlank()) {
                    control = "h";
                }
                switch (control) {
                    case "q": {
                        if (line.contains("TYPE_TASK")) {
                            addTaskToMap(fromString(line).getId(), fromString(line));
                        }
                        if (line.contains("TYPE_EPIC")) {
                            Epic epic;
                            epic = (Epic) fromString(line);
                            addEpicToMap(epic.getId(), epic);
                        }
                        if (line.contains("TYPE_SUBTASK")) {
                            SubTask subTask;
                            subTask = (SubTask) fromString(line);
                            listSub.add(subTask);
                        }
                        break;
                    }
                    case "h": {
                        for (SubTask subTask : listSub) {
                            if (getEpicMap().containsKey(subTask.getEpic())) {
                                addSubTaskMap(subTask, subTask.getEpic());
                            }
                        }
                        if (!fromStringHistory(line).isEmpty()) {
                            for (int o : fromStringHistory(line)) {
                                if (getTaskMap().containsKey(o)) {
                                    getHistoryManagerSave().add(getTask(o));
                                }
                                if (getEpicMap().containsKey(o)) {
                                    getHistoryManagerSave().add(getEpic(o));
                                } else {
                                    for (Epic value : getEpicMap().values()) {
                                        for (SubTask subTask : value.subTasks) {
                                            if (subTask.getId() == o) {
                                                getHistoryManagerSave().add(subTask);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException r) {
            throw new ManagerSaveException("ошибка загрузки из файла");
        }
    }

    private static String toString(HistoryManager manager) { //создает строку с историей запросов
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : manager.getHistory()) {
            stringBuilder.append(task.getId());
            if (count < (manager.getHistory().size()) - 1) {
                stringBuilder.append(",");
            }
            count++;
        }
        return stringBuilder.toString();
    }

    private static List<Integer> fromStringHistory(String value) { // создаёт историю из строки
        List<Integer> array = new ArrayList<>();
        String[] split = value.split(",");
        if (!value.isBlank()) {
            for (String s : split) {
                int t = Integer.parseInt(s);
                array.add(t);
            }
        }
        return array;
    }

    private void save() { // сохраняет изменения в файл
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            for (Integer integer : getTaskMap().keySet()) {
                fileWriter.write(getTaskMap().get(integer).toString() + "\n");
            }
            for (Integer integer : getEpicMap().keySet()) {
                fileWriter.write(getEpicMap().get(integer).toString() + "\n");
            }
            for (Integer integer : getEpicMap().keySet()) {
                for (SubTask subTask : getEpicMap().get(integer).subTasks) {
                    fileWriter.write(subTask.toString() + "\n");
                }
            }
            if (!toString(getHistoryManagerSave()).isBlank()) {
                fileWriter.write("\n");
            }
            fileWriter.write(toString(getHistoryManagerSave()));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, Task> getTaskMap() {
        return super.getTaskMap();
    }

    @Override
    public HashMap<Integer, Epic> getEpicMap() {
        return super.getEpicMap();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();

    }

    private HistoryManager getHistoryManagerSave() {
        return super.getHistoryManager();
    }

    @Override
    public void updateTask(Task task, Integer idOldTask) {
        save();
        super.updateTask(task, idOldTask);
    }

    @Override
    public void updateEpic(Epic epic, Integer idOldTask) {
        save();
        super.updateEpic(epic, idOldTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        save();
        super.updateSubTask(subTask);
    }

    @Override
    public void addTaskToMap(Integer id, Task task) {
        super.addTaskToMap(id, task);
        save();
    }

    @Override
    public void addEpicToMap(Integer id, Epic epic) {
        super.addEpicToMap(id, epic);
        save();
    }

    @Override
    public void addSubTaskMap(SubTask subTask, int epicId) {
        super.addSubTaskMap(subTask, epicId);
        save();
    }

    @Override
    public void setStatus(Task task, Status status) {
        super.setStatus(task, status);
        save();
    }

    @Override
    public void setStatusSubTask(SubTask subTask, Status status) {
        super.setStatusSubTask(subTask, status);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer idEpic, Integer id) {
        super.deleteSubTaskById(idEpic, id);
        save();
    }

    @Override
    public void clearTaskMap() {
        super.clearTaskMap();
        save();
    }

    @Override
    public void clearEpicMap() {
        super.clearEpicMap();
        save();
    }

    @Override
    public void removeEverythingCompletely() {
        super.removeEverythingCompletely();
    }

    @Override
    public Epic getEpic(Integer id) {
        try {
            return super.getEpic(id);
        } finally {
            save();
        }
    }

    @Override
    public Task getTask(Integer id) {
        try {
            return super.getTask(id);
        } finally {
            save();
        }
    }

    @Override
    public SubTask getSubTask(Integer epicId, Integer subTaskId) {
        try {
            return super.getSubTask(epicId, subTaskId);
        } finally {
            save();
        }
    }
}
