package tracker.http_task_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tracker.serializer.TaskJsonSerializer;
import tracker.serializer.TaskSerializer;
import tracker.TaskManager;
import tracker.tasks.Epic;
import tracker.tasks.SubTask;
import tracker.tasks.Task;
import static tracker.serializer.MapToJson.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;


public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
    private final TaskManager manager;
    private final TaskSerializer taskSerializer = new TaskJsonSerializer();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.manager = taskManager;

        server.createContext("/tasks/task", createContextTask());
        server.createContext("/tasks/epic", createContextEpic());
        server.createContext("/tasks/subtasks", createContextSubtask());
        server.createContext("/tasks/history", createContextHistory());
        server.createContext("/tasks", createContextPrioritized());
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("сервер выключен");
    }

    private HttpHandler createContextTask() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange h) throws IOException {
                switch (h.getRequestMethod()) {
                    case "GET":
                        if (manager.getTaskMap().isEmpty()) {
                            h.sendResponseHeaders(505, 0);
                            OutputStream os = h.getResponseBody();
                            os.close();
                        }
                        String body = "";
                        if (h.getRequestURI().getQuery() != null) {
                            String[] q = h.getRequestURI().getQuery().split("=");
                            int id = Integer.parseInt(q[1]);
                            body = taskSerializer.toObject(manager.getTask(id));
                        } else {
                            body = taskMapToJsonList(manager.getTaskMap(), taskSerializer);
                        }
                        h.sendResponseHeaders(200, 0);

                        try (OutputStream os = h.getResponseBody()) {
                            os.write(body.getBytes());
                        }
                        break;
                    case "DELETE":
                        if (h.getRequestURI().getQuery() != null) {
                            String[] q = h.getRequestURI().getQuery().split("=");
                            int id = Integer.parseInt(q[1]);
                            manager.deleteTaskById(id);
                            h.sendResponseHeaders(210, 0);
                            OutputStream os = h.getResponseBody();
                            os.close();
                        } else {
                            manager.getTaskMap().clear();
                            h.sendResponseHeaders(210, 0);
                            OutputStream os = h.getResponseBody();
                            os.close();
                        }
                        break;
                    case "POST":
                        String ss = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Task task = taskSerializer.fromString(ss);
                        manager.addTaskToMap(task.getId(), task);
                        h.sendResponseHeaders(250, 0);
                        OutputStream os = h.getResponseBody();
                        os.close();
                        break;
                }
            }
        };
    }

    private HttpHandler createContextEpic() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange h) throws IOException {
                switch (h.getRequestMethod()) {
                    case "GET":
                        if (manager.getTaskMap().isEmpty()) {
                            h.sendResponseHeaders(505, 0);
                            OutputStream os = h.getResponseBody();
                            os.close();
                        }
                        String body = "";
                        if (h.getRequestURI().getQuery() != null) {
                            String[] q = h.getRequestURI().getQuery().split("=");
                            int id = Integer.parseInt(q[1]);
                            body = taskSerializer.toObject(manager.getEpic(id));
                        } else {
                            body = epicMapToJsonList(manager.getEpicMap(), taskSerializer);
                        }
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(body.getBytes());
                        }
                        break;
                    case "DELETE":
                        if (h.getRequestURI().getQuery() != null) {
                            String[] q = h.getRequestURI().getQuery().split("=");
                            int id = Integer.parseInt(q[1]);
                            manager.deleteEpicById(id);
                            h.sendResponseHeaders(210, 0);
                            OutputStream os = h.getResponseBody();
                            os.close();
                        } else {
                            manager.getEpicMap().clear();
                            h.sendResponseHeaders(210, 0);
                            OutputStream os = h.getResponseBody();
                            os.close();
                        }
                        break;
                    case "POST":
                        String ss = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Epic task = taskSerializer.epicFromString(ss);
                        manager.addEpicToMap(task.getId(), task);
                        h.sendResponseHeaders(250, 0);
                        OutputStream os = h.getResponseBody();
                        os.close();
                        break;
                }
            }
        };
    }

    private HttpHandler createContextSubtask() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange h) throws IOException {
                switch (h.getRequestMethod()) {
                    case "GET":
                        String body = "";
                        if (h.getRequestURI().getQuery() != null) {
                            String[] q = h.getRequestURI().getQuery().split("&");
                            String[] epic = q[0].split("=");
                            String[] subtask = q[1].split("=");
                            int idEpic = Integer.parseInt(epic[1]);
                            int idSubtask = Integer.parseInt(subtask[1]);
                            body = taskSerializer.toObject(manager.getSubTask(idEpic, idSubtask));
                        } else {
                            body = subtaskMapToJsonList(manager.getEpicMap(), taskSerializer);
                        }
                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(body.getBytes());
                        }
                    case "DELETE":
                        if (h.getRequestURI().getQuery() == null) {
                            throw new RuntimeException("для удаления подзадачи необходим id эпика и подзадачи," +
                                    "для удаления всех подзадач необходим id эпика");
                        }
                        if (h.getRequestURI().getQuery().contains("&")) {
                            String[] q = h.getRequestURI().getQuery().split("&");
                            String[] epic = q[0].split("=");
                            String[] subtask = q[1].split("=");
                            int idEpic = Integer.parseInt(epic[1]);
                            int idSubtask = Integer.parseInt(subtask[1]);
                            manager.deleteSubTaskById(idEpic, idSubtask);
                            h.sendResponseHeaders(210, 0);
                            OutputStream os = h.getResponseBody();
                            os.close();
                        } else {
                            String[] q = h.getRequestURI().getQuery().split("=");
                            int idEpic = Integer.parseInt(q[1]);
                            manager.getEpic(idEpic).subTasks.clear();
                            h.sendResponseHeaders(210, 0);
                            OutputStream os = h.getResponseBody();
                            os.close();
                        }
                        break;
                    case "POST":
                        if (h.getRequestURI().getQuery() == null) {
                            throw new RuntimeException("для обновления подзадачи необходим  id эпика и подзадачи," +
                                    "для удаления всех подзадач необходим id эпика");
                        }
                        String[] q = h.getRequestURI().getQuery().split("&");
                        String[] epic = q[0].split("=");
                        String[] subtask = q[1].split("=");
                        int idEpic = Integer.parseInt(epic[1]);
                        int idSubtask = Integer.parseInt(subtask[1]);
                        int subCross = -1;
                        for (int i = 0; i < manager.getEpic(idEpic).subTasks.size(); i++) {
                            if (manager.getEpic(idEpic).subTasks.get(i).getId() == idSubtask) {
                                subCross = i;
                            }
                        }
                        if (subCross >= 0) {
                            manager.getEpic(idEpic).subTasks.remove(subCross);
                        }
                        String ss = new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        SubTask task = taskSerializer.subtaskFromString(ss);
                        manager.getEpic(idEpic).subTasks.add(task);
                        h.sendResponseHeaders(250, 0);
                        OutputStream os = h.getResponseBody();
                        os.close();
                        break;
                }
            }
        };
    }

    private HttpHandler createContextHistory() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange h) throws IOException {
                h.sendResponseHeaders(200, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write(historyMapToJsonList(manager.getHistoryManager(), taskSerializer).getBytes());
                }
            }
        };
    }

    private HttpHandler createContextPrioritized() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange h) throws IOException {
                h.sendResponseHeaders(200, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write(prioritizedMapToJsonList(manager.getPrioritizedTasks(), taskSerializer).getBytes());
                }
            }
        };
    }
}



