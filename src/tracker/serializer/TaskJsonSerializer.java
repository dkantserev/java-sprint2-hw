package tracker.serializer;

import com.google.gson.*;
import tracker.tasks.Epic;
import tracker.tasks.SubTask;
import tracker.tasks.Task;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class TaskJsonSerializer implements TaskSerializer {
    @Override
    public String toObject(Task task) {
        GsonBuilder gson = new GsonBuilder();
        return gson.create().toJson(task);
    }

    @Override
    public Task fromString(String string) {
        GsonBuilder gson = new GsonBuilder().registerTypeAdapter(ZoneId.class, (JsonDeserializer<ZoneId>) (jsonElement
                , type, jsonDeserializationContext) -> ZoneId.systemDefault()).registerTypeAdapter(DateTimeFormatter.class
                , (JsonDeserializer<DateTimeFormatter>) (jsonElement, type
                        , jsonDeserializationContext) -> DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
        return gson.create().fromJson(string, Task.class);
    }

    @Override
    public Epic epicFromString(String string) {
        GsonBuilder gson = new GsonBuilder().registerTypeAdapter(ZoneId.class, (JsonDeserializer<ZoneId>) (jsonElement
                , type, jsonDeserializationContext) -> ZoneId.systemDefault()).registerTypeAdapter(DateTimeFormatter.class
                , (JsonDeserializer<DateTimeFormatter>) (jsonElement, type
                        , jsonDeserializationContext) -> DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
        return gson.create().fromJson(string, Epic.class);
    }

    @Override
    public SubTask subtaskFromString(String string) {
        GsonBuilder gson = new GsonBuilder().registerTypeAdapter(ZoneId.class, (JsonDeserializer<ZoneId>) (jsonElement
                , type, jsonDeserializationContext) -> ZoneId.systemDefault()).registerTypeAdapter(DateTimeFormatter.class
                , (JsonDeserializer<DateTimeFormatter>) (jsonElement, type
                        , jsonDeserializationContext) -> DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
        return gson.create().fromJson(string, SubTask.class);
    }


}
