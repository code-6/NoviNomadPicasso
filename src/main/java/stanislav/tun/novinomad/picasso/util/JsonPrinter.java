package stanislav.tun.novinomad.picasso.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;


public abstract class JsonPrinter {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        mapper.registerModule(new JodaModule());
        mapper.configure(
                com.fasterxml.jackson.databind.SerializationFeature.
                        WRITE_DATES_AS_TIMESTAMPS , false);
    }

    public static String getString(Object o){
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return  mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            System.err.println("Print failed. "+e.getMessage());
            return "";
        }
    }
}
