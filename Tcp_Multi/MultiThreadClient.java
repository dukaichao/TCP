package BIT.Tcp_Multi;

import com.sun.security.ntlm.Client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created With IntelliJ IDEA.
 * Descriptions:
 * User:Mr.Du
 * Date:2019-06-30
 * Time:20:52
 */
public class MultiThreadClient {

    private static class RecMsg implements Runnable{
        private Socket client;

        public RecMsg(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            Scanner sc = null;
            try {
                sc = new Scanner(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true){
                if(client.isClosed()){
                    System.out.println("客户端退出");
                    break;
                }
                if(sc.hasNextLine()) {
                    System.out.println("服务端说:" + sc.nextLine());
                }
            }
            sc.close();
        }
    }


    private static class SendMsg implements  Runnable{
        private Socket client;

        public SendMsg(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
            PrintStream ps = null;
            try {
                ps = new PrintStream(client.getOutputStream(),true,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true){
                System.out.println("请输入内容");
                String str = sc.nextLine();
                ps.println(str);
                if(str.contains("bye")){
                    break;
                }
            }
            ps.close();
            sc.close();

        }
    }

    public static void main(String[] args) throws IOException {
        Socket sc = new Socket("127.0.0.1",8888);
        new Thread(new RecMsg(sc)).start();
        new Thread(new SendMsg(sc)).start();
    }
}
