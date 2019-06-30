package BIT.TCP_single;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created With IntelliJ IDEA.
 * Descriptions:
 * User:Mr.Du
 * Date:2019-06-30
 * Time:19:24
 */
/*
* 服务端
*   1.建立服务端Socket
*   ServerSocket ss = new ServerSocket(端口号);
*   2.等待客户端连接,会一直阻塞知道有客户端连接成功，返回客户端连接
*   Socket sc = ss.accept();
*   3.拿到输入输出流
*
*
* */

public class SingleThreadServer {
    public static void main(String[] args) throws IOException {
        //1.建立基站
        ServerSocket ss = new ServerSocket(8888);
        System.out.println("等待连接");
        Socket sc = new Socket();
        try {
            sc = ss.accept();
            System.out.println("连接成功,端口号为:"+sc.getPort());

            //读取客户端发来的信息
            Scanner scanner = new Scanner(sc.getInputStream());
            if(scanner.hasNextLine()){
                System.out.println("客户端发来信息为:"+scanner.nextLine());
            }
            //向客户端发送一条消息
            PrintStream ps = new PrintStream(sc.getOutputStream());
            ps.println("cjf");
            scanner.close();
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            ss.close();
            sc.close();

        }
    }
}
