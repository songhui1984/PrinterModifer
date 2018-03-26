import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class Hessian2Serialization implements Serialization {

    @Override
    public byte[] serialize(Object data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output h2o  = new Hessian2Output(bos);
		h2o.startMessage();
        h2o.writeObject(data);
		h2o.completeMessage();
		h2o.close();
        return bos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        Hessian2Input h2i = new Hessian2Input(new ByteArrayInputStream(data));
		h2i.startMessage();
        T objectT= (T) h2i.readObject(clz);
		h2i.completeMessage();
		h2i.close();
		return objectT;

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