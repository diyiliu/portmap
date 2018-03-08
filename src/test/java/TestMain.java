import org.junit.Test;

/**
 * Description: TestMain
 * Author: DIYILIU
 * Update: 2018-03-08 08:57
 */
public class TestMain {


    @Test
    public void test(){
        String str = "no static";

        System.out.println(str.split(" ").length);

        String content = "XUTZ# sh ru\n";
        String s = "# sh";

        System.out.println(content.contains(s));
    }
}
