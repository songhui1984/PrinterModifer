package hello;

import org.junit.Test;

public class StringFormatTest {


    @Test
    public void testStringFormat(){
        System.out.println("test String format");

        String str = null;
        str = String.format("Hi, %s","宋辉");

        System.out.print(str);

        str = String.format("Hi, %s:%s.%s","宋辉","李群","宋佳忆");

        System.out.print(str);

        System.out.printf("字母a的大写是：%c %n",'A');

        System.out.printf("3>7的结果是：%b %n",3>7);

        System.out.printf("100的一半是：%d %n", 100/2);

        System.out.printf("50元的书打8.5折扣是：%f 元%n", 50*0.85);

        System.out.printf("上面价格的指数表示：%e %n", 50*0.85);

        System.out.printf("%+d",15);

        System.out.printf("%-5d",15);

    }
}
