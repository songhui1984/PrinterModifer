import org.junit.Test;

import java.io.IOException;

public class TestSeriazalition {



    @Test
    public void testS1(){

        PojoA a = new PojoA();
        a.setAge(18);
        a.setName("songhui");

        Serialization serialization = new FastJsonSerialization();

        try {
            byte[] bytes = serialization.serialize(a);

            PojoA a1 = serialization.deserialize(bytes,PojoA.class);
            System.out.print(a1.getAge());



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
