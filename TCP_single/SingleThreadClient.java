package BIT.TCP_single;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created With IntelliJ IDEA.
 * Descriptions:
 * User:Mr.Du
 * Date:2019-06-30
 * Time:19:32
 */
public class SingleThreadClient {
    public static void main(String[] args) throws IOException {
        //1.先与服务器建立连接
        Socket sc = new Socket("127.0.0.1",8888);
        //2.拿到输出流流向服务器发送一条信息
        PrintStream ps = new PrintStream(sc.getOutputStream());
        ps.println("zzc");
        //3.拿到输入流读取服务器发来的信息
        Scanner s = new Scanner(sc.getInputStream());
        if(s.hasNextLine()){
            String str = s.nextLine();
            System.out.println("服务端发来信息为:"+str);
        }
        sc.close();
        ps.close();
        s.close();
    }
}
