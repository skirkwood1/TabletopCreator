package ChatClient;

import UI.TextPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientWindow implements ActionListener,Runnable {

    JFrame frame;
    TextPanel log;
    JTextField prompt;

    HashMap<String, String> commandMap;

    private final int port = 8888;
    private String ip;

    private Scanner in;
    private InputStream input;

    private OutputStream output;
    private volatile PrintWriter out;

    private Thread thread;

    public ClientWindow(String ip) throws Exception {
        this.frame = new JFrame("Chat Client");

        this.ip = ip;

        frame.setPreferredSize(new Dimension(400, 400));

        this.log = new TextPanel();
        this.prompt = new JTextField();
        this.commandMap = new HashMap<>();

        frame.setLayout(new BorderLayout());

        frame.add(prompt, BorderLayout.SOUTH);
        frame.add(log, BorderLayout.CENTER);

        frame.pack();
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        prompt.addActionListener(this);

        this.thread = new Thread(this);
    }

     public void handleInput() throws IOException{
        String inLine;
        while((inLine = in.nextLine()) != null){
             log.appendBottomText(inLine);
         }
     }

     public void start(){
         frame.setVisible(true);
         thread.start();
     }

     public void run(){
         try {
             Socket socket = new Socket(ip,port);
             this.input = socket.getInputStream();
             this.in = new Scanner(input);

             this.output = socket.getOutputStream();
             this.out = new PrintWriter(output,true);
             while (true) {
                 display(in.nextLine());
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public void actionPerformed(ActionEvent e){
         String s = prompt.getText();
         if (out != null) {
             out.println(s);
         }
         //display(s);
         prompt.setText("");
     }

    private void display(final String s) {
        EventQueue.invokeLater(new Runnable() {
            //@Override
            public void run() {
                log.appendText(s + "\n\r");
            }
        });
    }

    public static void main(String[] args){

        EventQueue.invokeLater(new Runnable(){
            public void run(){
                try{
                    new ClientWindow("localhost").start();
                }catch(Exception e){}
            }
        });
    }
}