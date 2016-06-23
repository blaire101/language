package com.qunar.fresh.librarysystem.model.Handler;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-4-8 Time: 下午4:26 To change this template use File | Settings |
 * File Templates.
 */
public class DateHandler extends JsonSerializer<Date> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        String after = dateFormat.format(date);
        jsonGenerator.writeString(after);
    }
}
