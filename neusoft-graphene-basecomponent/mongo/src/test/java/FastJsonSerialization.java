import java.io.IOException;
import java.io.InputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJsonSerialization implements Serialization {

	@Override
	public byte[] serialize(Object obj) throws IOException {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);
        serializer.config(SerializerFeature.WriteClassName, true);
        serializer.write(obj);
        return out.toBytes("UTF-8");
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
		return JSON.parseObject(new String(bytes), clz);
	}

    /* (non-Javadoc)
     * @see com.neusoft.dsf.serialize.Serialization#deserialize(java.io.InputStream, java.lang.Class)
     */
    @Override
    public <T> T deserialize(InputStream in, Class<T> clz) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
}