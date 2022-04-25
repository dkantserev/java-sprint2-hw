package tracker.Serializer;

import com.google.gson.*;
import tracker.Tasks.Task;

import java.lang.reflect.Type;
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
        GsonBuilder gson = new GsonBuilder().registerTypeAdapter(ZoneId.class, new JsonDeserializer<ZoneId>() {
            @Override
            public ZoneId deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

                return ZoneId.systemDefault();
            }
        }).registerTypeAdapter(DateTimeFormatter.class, new JsonDeserializer<DateTimeFormatter>() {
            @Override
            public DateTimeFormatter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
            }
        });

        return gson.create().fromJson(string,Task.class);
    }
}
