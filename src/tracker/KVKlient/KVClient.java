package tracker.KVKlient;

import tracker.Serializer.TaskSerializer;
import tracker.Tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    private final String url;
    private  String apiKey;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final static String requestTemplate = "%s/%s/%s?API_KEY=%s";
    private final TaskSerializer taskSerializer;

    public KVClient(String url,TaskSerializer taskSerializer) {
        this.url = url;
        this.taskSerializer=taskSerializer;
    }

    private String registerURL() {
        return String.format("%s/register", this.url);
    }

    private String saveURL(int key) {
        if (apiKey == null) {
            throw new IllegalStateException("регистрация не пройдена");
        }
        return String.format(requestTemplate, this.url, "save", Integer.toString(key), apiKey);
    }

    private String loadURL(int key) {
        if (apiKey == null) {
            throw new IllegalStateException("регистрация не пройдена");
        }
        return String.format(requestTemplate, this.url, "load", Integer.toString(key), apiKey);
    }

    public void register() {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(registerURL())).build();
        try {
          String apiKey = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
          if(apiKey==null){
              throw new IllegalStateException("регистрация не пройдена на сервере");
          }
          this.apiKey=apiKey;
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("регистрация не пройдена на сервере");
        }
    }
    public void save(Task task){
        String json = taskSerializer.toObject(task);
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).uri(URI.create(saveURL(task.getId()))).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Task load (int taskId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).GET().uri(URI.create(loadURL(taskId))).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return taskSerializer.fromString(response.body());

    }

}
