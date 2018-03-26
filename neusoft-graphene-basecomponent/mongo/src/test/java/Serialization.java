import java.io.IOException;
import java.io.InputStream;


public interface Serialization {

	byte[] serialize(Object obj) throws IOException;

	<T> T deserialize(byte[] bytes, Class<T> returnClass) throws IOException;
	
    <T> T deserialize(InputStream in, Class<T> clz) throws IOException;
	
//	<T> T deserialize(byte[] bytes, Class<? extends Collection> collectionClass,Class<T> valueClass) throws IOException;
//	
//	<T> T deserialize(byte[] bytes, Class<? extends Map> mapClass,Class<T> keyClass,Class<T> valueClas) throws IOException;
}
