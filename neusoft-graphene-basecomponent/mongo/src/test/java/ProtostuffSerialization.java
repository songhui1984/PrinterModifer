import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Throwables;
//import org.springframework.objenesis.Objenesis;
//import org.springframework.objenesis.ObjenesisStd;
//import com.neusoft.dsf.serialize.Serialization;

public class ProtostuffSerialization implements Serialization {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        Class clz = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = getSchema(clz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(Throwables.getStackTraceAsString(e), e);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        try {
            T message = clz.newInstance();
            Schema<T> schema = getSchema(clz);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(Throwables.getStackTraceAsString(e), e);
        }
    }
    
    public <T> T deserialize(InputStream in,Class<T> clz) throws IOException {
        try {
            T message = clz.newInstance();
            Schema<T> schema = getSchema(clz);
            ProtostuffIOUtil.mergeFrom(in, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(Throwables.getStackTraceAsString(e), e);
        }
    }
    
    
    
}