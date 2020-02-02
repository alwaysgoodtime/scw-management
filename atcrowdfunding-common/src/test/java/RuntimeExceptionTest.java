/**
 * @author goodtime
 * @create 2020-02-02 9:38 下午
 */
public class RuntimeExceptionTest {
//运行时异常，捕获后也能继续执行后面的代码
    public static void main(String[] args) {
        try {
            int i = 1;
            int j = 1/0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(0);
    }
}
