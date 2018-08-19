
package ch.bfh.bigdata.semarbeit.twitteralert.messaging;

import com.google.gson.Gson;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.Charset;
import java.util.Map;


public class JsonDeserializer<T> implements Deserializer<T> {

    private Gson gson = new Gson();
    private Class<T> deserializedClass;

    public JsonDeserializer(Class<T> deserializedClass) {
        this.deserializedClass = deserializedClass;
    }

    public JsonDeserializer() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(Map<String, ?> map, boolean b) {
        if (deserializedClass == null) {
            deserializedClass = (Class<T>) map.get("serializedClass");
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        String value = new String(bytes, Charset.forName("UTF-8"));
        String unescapeJson = StringEscapeUtils.unescapeJson(value);
        String replace = unescapeJson.substring(1, unescapeJson.length() - 1);
        return gson.fromJson(replace, deserializedClass);

    }

    @Override
    public void close() {

    }
}
