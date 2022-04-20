package tracker.KVKlient;

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

    public KVClient(String url) {
        this.url = url;
    }

    private String registerURL() {
        return String.format("%s/register", this.url);
    }

    private String saveURL(String key) {
        if (apiKey == null) {
            throw new IllegalStateException("регистрация не пройдена");
        }
        return String.format(requestTemplate, this.url, "save", key, apiKey);
    }

    private String loadURL(String key) {
        if (apiKey == null) {
            throw new IllegalStateException("регистрация не пройдена");
        }
        return String.format(requestTemplate, this.url, "load", key, apiKey);
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

}
