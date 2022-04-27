package tracker;


import tracker.KVKlient.KVClient;
import tracker.Serializer.TaskJsonSerializer;
import tracker.Tasks.Epic;
import tracker.Tasks.SubTask;
import tracker.Tasks.Task;

import static tracker.Serializer.MapToJson.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager { //менеджер с возможностью хранения данных на сервере
    private final KVClient kvClient;
    private final TaskJsonSerializer taskJsonSerializer = new TaskJsonSerializer();

    public HTTPTaskManager(File file, KVClient kvClient) {
        super(file);
        this.kvClient = kvClient;
    }

    private void save() { // сохранения данных менеджера на сервере
        if (!getTaskMap().isEmpty()) {
            kvClient.save(taskMapToJsonList(getTaskMap(), taskJsonSerializer), "TASK");
        }
        if (!getEpicMap().isEmpty()) {
            kvClient.save(epicMapToJsonList(getEpicMap(), taskJsonSerializer), "EPIC");
        }
        if (!getHistoryManager().getHistory().isEmpty()) {
            kvClient.save(historyMapToJsonList(getHistoryManager(), taskJsonSerializer), "HISTORY");
        }
    }

    @Override
    public void load() throws IOException, InterruptedException { // восстановление менеджера из данных сервера

        List<Task> gg = new ArrayList<>();
        List<Epic> hh = new ArrayList<>();
        List<Task> kk = new ArrayList<>();
        String[] json = kvClient.load("TASK").split("%");
        String[] jsonEpic = kvClient.load("EPIC").split("%");
        String[] jsonHistory = kvClient.load("HISTORY").split("%");
        for (String s : json) {
            gg.add(taskJsonSerializer.fromString(s));
        }
        for (String s : jsonEpic) {
            hh.add(taskJsonSerializer.epicFromString(s));
        }
        for (String s : jsonHistory) {
            kk.add(taskJsonSerializer.fromString(s));
        }
        for (Task task : gg) {
            addTaskToMap(task.getId(), task);
        }
        for (Epic epic : hh) {
            addEpicToMap(epic.getId(), epic);
        }
        for (Task task : kk) {
            for (Task task1 : gg) {
                if (task.getId() == task1.getId()) {
                    getHistoryManager().add(task1);
                }
            }
            for (Epic epic : hh) {
                if (task.getId() == epic.getId()) {
                    getHistoryManager().add(epic);
                }
                for (SubTask epic1 : epic.subTasks) {
                    if (task.getId() == epic1.getId()) {
                        getHistoryManager().add(epic1);
                    }
                }
            }
        }
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
    public Epic getEpic(Integer id) {
        try {
            save();
            return super.getEpic(id);
        } finally {
            save();
        }
    }

    @Override
    public Task getTask(Integer id) {
        try {
            save();
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
