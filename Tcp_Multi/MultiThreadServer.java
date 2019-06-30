package BIT.Tcp_Multi;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Created With IntelliJ IDEA.
 * Descriptions:
 * User:Mr.Du
 * Date:2019-06-30
 * Time:21:04
 */
public class MultiThreadServer {
    //保存所有连接的客户端
    private static Map<String, Socket> clientMap = new ConcurrentHashMap<>();

    private static class ExecuteClientMap implements Runnable{

        private Socket client;

        public ExecuteClientMap(Socket client) {
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
                if(sc.hasNextLine()){
                    String msgFromClient = sc.nextLine();
                    if(msgFromClient.startsWith("R:")){
                        //注册
                        //用户名
                        String username = msgFromClient.split(":")[1];
                        userRegister(username,client);
                    }else if(msgFromClient.startsWith("P:")){
                        //私聊P:用户名-信息
                        String userName = msgFromClient.split(":")[1].split("-")[0];
                        String msg = msgFromClient.split("-")[1];
                        sendPriUser(userName,msg);

                    }else if(msgFromClient.startsWith("G:")){
                        //群聊
                        String groupMsg = msgFromClient.split(":")[1];
                        group(groupMsg);
                    }
                }
            }
        }

        private void sendPriUser(String userName,String msg) {
            Socket sc = clientMap.get(userName);
            PrintStream ps = null;
            try {
                ps = new PrintStream(sc.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ps.println("私聊信息为:"+msg);
            ps.close();
            try {
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void userRegister(String username, Socket client) {
            clientMap.put(username,client);
            String str = username+"上线了";
            group(str);
        }

        private void group(String str) {
            Collection<Socket> values = clientMap.values();
            for(Socket sc : values){
                PrintStream ps = null;
                try {
                    ps = new PrintStream(sc.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ps.println(str);

            }

        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8888);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(int i = 0;i<20;i++){
            System.out.println("等待客户端连接");
            Socket sc = ss.accept();
            System.out.println("连接成功，端口号为："+sc.getPort());
            //新建一个线程处理客户端的连接
            executorService.submit(new ExecuteClientMap(sc));
        }
        ss.close();

    }
}
