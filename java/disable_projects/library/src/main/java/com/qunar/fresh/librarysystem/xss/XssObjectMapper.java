package com.qunar.fresh.librarysystem.xss;

import java.io.IOException;

import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.ser.std.SerializerBase;

/**
 * 在输出时对文本进行过虑，防止XSS攻击
 * 
 * @author hang.gao
 */
public class XssObjectMapper extends ObjectMapper {

    public XssObjectMapper() {
        registerXssJsonSerializer();
    }

    public XssObjectMapper(JsonFactory jf) {
        super(jf);
        registerXssJsonSerializer();
    }

    public XssObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp) {
        super(jf, sp, dp);
        registerXssJsonSerializer();
    }

    public XssObjectMapper(JsonFactory jf, SerializerProvider sp, DeserializerProvider dp, SerializationConfig sconfig,
            DeserializationConfig dconfig) {
        super(jf, sp, dp, sconfig, dconfig);
        registerXssJsonSerializer();
    }

    /**
     * 注册XssJsonSerializer，将XssJsonSerializer注册到ObjectMapper，用于处理字符串的输出到JSON
     */
    private void registerXssJsonSerializer() {
        SimpleModule module = new SimpleModule("xssJson", Version.unknownVersion());
        module.addSerializer(new XssJsonSerializer(CharSequence.class));
        super.registerModule(module);
    }

    /**
     * 字符串JSON序列化，在将字符串写入json对象时，会调用此JsonSerializer，当序列化集合时， 也会通过SerializerProvider来获取序列化字符串的JsonSerializer。
     * 
     * @author hang.gao
     */
    private static final class XssJsonSerializer extends SerializerBase<CharSequence> {

        /**
         * 处理文本，替换文本中的html标签，防止XSS
         */
        private Escaper escaper = HtmlEscapers.htmlEscaper();

        protected XssJsonSerializer(Class<CharSequence> t) {
            super(t);
        }

        @Override
        public void serialize(CharSequence value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(escaper.escape(value.toString()));
        }

    }
}
