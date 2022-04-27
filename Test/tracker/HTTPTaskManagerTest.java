package tracker;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.HttpTaskServer.HttpTaskServer;
import tracker.KVKlient.KVClient;
import tracker.KVServer.KVServer;
import tracker.Serializer.TaskJsonSerializer;
import tracker.Serializer.TaskSerializer;
import tracker.Tasks.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import static tracker.Serializer.MapToJson.*;
import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {


    KVServer server;
    HttpTaskServer httpTaskServer;
    TaskJsonSerializer taskJsonSerializer = new TaskJsonSerializer();

    @BeforeEach
    public void start() throws IOException {
        server = new KVServer();
        server.start();
        File file = new File("testServer.csv");
        String URL = "http://localhost:8078";
        TaskSerializer taskSerializer = new TaskJsonSerializer();
        KVClient kvClient = new KVClient(URL, taskSerializer);
        kvClient.register();
        taskManager = new HTTPTaskManager(file, kvClient);
        creatorTestManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    public void stop() {
        server.stop();
        httpTaskServer.stop();
    }


    @Test
    void endpointTasks_TaskReturnList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskMapToJsonList(taskManager.getTaskMap(), taskJsonSerializer), response.body());
    }

    @Test
    void endpointTasks_TaskReturnVoidList() throws IOException, InterruptedException {
        taskManager.getTaskMap().clear();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 505);

    }

    @Test
    void endpointTasks_TaskDeleteList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getTaskMap().size(), 0);
    }

    @Test
    void endpointTasks_TaskPostId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = "{\"nameTask\":\"testTask1\",\"taskBody\":\"...\",\"id\":2,\"status\":\"TASK_NEW\",\"typeTask\":" +
                "\"TYPE_TASK\",\"startTime\":{\"dateTime\":{\"date\":{\"year\":2018,\"month\":1,\"day\":1},\"time\":{" +
                "\"hour\":21,\"minute\":1,\"second\":0,\"nano\":0}},\"offset\":{\"totalSeconds\":10800},\"zone\":{\"id" +
                "\":\"Europe/Moscow\"}},\"duration\":{\"seconds\":10800,\"nanos\":0},\"formatter\":{\"printerParser\":{" +
                "\"printerParsers\":[{\"field\":\"YEAR_OF_ERA\",\"minWidth\":4,\"maxWidth\":19,\"signStyle\":\"EXCEEDS_PAD" +
                "\",\"subsequentWidth\":0},{\"literal\":\"-\"},{\"field\":\"MONTH_OF_YEAR\",\"minWidth\":2,\"maxWidth\":2," +
                "\"signStyle\":\"NOT_NEGATIVE\",\"subsequentWidth\":0},{\"literal\":\"-\"},{\"field\":\"DAY_OF_MONTH\"," +
                "\"minWidth\":2,\"maxWidth\":2,\"signStyle\":\"NOT_NEGATIVE\",\"subsequentWidth\":0},{\"literal\":\"-\"},{" +
                "\"field\":\"CLOCK_HOUR_OF_AMPM\",\"minWidth\":2,\"maxWidth\":2,\"signStyle\":\"NOT_NEGATIVE\"," +
                "\"subsequentWidth\":0},{\"literal\":\"-\"},{\"field\":\"MINUTE_OF_HOUR\",\"minWidth\":2,\"maxWidth\":2," +
                "\"signStyle\":\"NOT_NEGATIVE\",\"subsequentWidth\":0}],\"optional\":false},\"locale\":\"ru_RU\"," +
                "\"decimalStyle\":{\"zeroDigit\":\"0\",\"positiveSign\":\"+\",\"negativeSign\":\"-\",\"decimalSeparator\":" +
                "\".\"},\"resolverStyle\":\"SMART\"}}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getTaskMap().size(), 2);
    }

    @Test
    void endpointTasks_TaskGetId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskJsonSerializer.toObject(taskManager.getTask(1)), response.body());
    }

    @Test
    void endpointTasks_TaskDeleteId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getTaskMap().size(), 0);
    }

    @Test
    void endpointTasks_EpicReturnList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(epicMapToJsonList(taskManager.getEpicMap(), taskJsonSerializer), response.body());
    }

    @Test
    void endpointTasks_EpicReturnVoidList() throws IOException, InterruptedException {
        taskManager.getTaskMap().clear();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 505);

    }

    @Test
    void endpointTasks_EpicDeleteList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getEpicMap().size(), 0);
    }

    @Test
    void endpointTasks_EpicPostId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=11");
        Epic epic2 = new Epic(TypeTask.TYPE_EPIC, "epicTest1", "...", 11, Status.TASK_NEW
                , ZonedDateTime.of(LocalDateTime.of(2228, 1, 11, 21, 1), zoneId)
                , Duration.ofHours(3));
        Gson gson = new Gson();
        String json = gson.toJson(epic2);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getEpic(11), epic2);
    }

    @Test
    void endpointTasks_EpicGetId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskJsonSerializer.toObject(taskManager.getEpic(2)), response.body());
    }

    @Test
    void endpointTasks_EpicDeleteId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getEpicMap().size(), 0);
    }

    @Test
    void endpointTasks_SubtaskReturnList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(subtaskMapToJsonList(taskManager.getEpicMap(), taskJsonSerializer), response.body());
    }

    @Test
    void endpointTasks_SubtaskDeleteList() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtasks?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getEpic(2).subTasks.size(), 0);
    }

    @Test
    void endpointTasks_SubtaskDeleteId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtasks?id=2&id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getEpic(2).subTasks.size(), 1);
    }

    @Test
    void endpointTasks_SubtaskPostId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtasks?id=2&id=33");
        SubTask subTask33 = new SubTask(TypeTask.TYPE_SUBTASK, "subtaskTest1", "...", 33
                , Status.TASK_NEW, epic.getId(), ZonedDateTime.of(LocalDateTime.of(1028, 1, 13
                , 21, 1), zoneId), Duration.ofHours(3));
        Gson gson = new Gson();
        String json = gson.toJson(subTask33);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskManager.getEpic(2).subTasks.size(), 3);
    }

    @Test
    void endpointTasks_TaskReturnListPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(prioritizedMapToJsonList(taskManager.getPrioritizedTasks(),taskJsonSerializer), response.body());
    }

    @Test
    void endpointTasks_TaskReturnListHistory() throws IOException, InterruptedException {
        taskManager.getTask(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(historyMapToJsonList(taskManager.getHistoryManager(),taskJsonSerializer), response.body());
    }

    @Test
    void load() throws IOException, InterruptedException {
        taskManager.getTask(1);
        assertEquals(taskManager.getHistoryManager().getHistory().size(),1);
        assertEquals(taskManager.getTaskMap().size(),1);
        assertEquals(taskManager.getTaskMap().size(),1);
        taskManager.removeEverythingCompletely();
        assertEquals(taskManager.getHistoryManager().getHistory().size(),0);
        assertEquals(taskManager.getTaskMap().size(),0);
        assertEquals(taskManager.getTaskMap().size(),0);
        taskManager.load();
        assertEquals(taskManager.getHistoryManager().getHistory().size(),1);
        assertEquals(taskManager.getTaskMap().size(),1);
        assertEquals(taskManager.getTaskMap().size(),1);
    }
}