package ChatServer;

import Models.Game;
import UI.TextPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientWindow implements ActionListener,Runnable {

    private JFrame frame;
    private TextPanel log;
    private JTextField prompt;
    private JSplitPane center;
    private JPanel buttons;
    private JList users;

    private JFileChooser fileUpload;
    private JButton upload;

    HashMap<String, String> commandMap;

    private final int port = 8888;
    private String ip;

    private Socket socket;

    private Scanner in;
    private volatile PrintWriter out;

    private Thread thread;

    public ClientWindow(String ip) throws Exception {
        this.frame = new JFrame("Chat Client");

        this.ip = ip;

        this.frame.setPreferredSize(new Dimension(400, 400));

        this.log = new TextPanel();
        this.prompt = new JTextField();
        this.center = new JSplitPane();
        this.commandMap = new HashMap<>();

        this.buttons = new JPanel();
        this.buttons.setLayout(new FlowLayout(FlowLayout.LEFT,2,0));

        this.upload = new JButton("File Upload");

        this.upload.addActionListener(e -> uploadGameFile());

        this.buttons.add(upload);

        this.center.setLayout(new BorderLayout());
        this.frame.setLayout(new BorderLayout());

        this.center.add(log, BorderLayout.CENTER);
        this.center.add(prompt, BorderLayout.SOUTH);
        this.center.add(buttons,BorderLayout.NORTH);

        this.frame.add(center,BorderLayout.CENTER);

        this.frame.setSize(400, 400);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.prompt.addActionListener(this);

        this.thread = new Thread(this);
    }

     public void start(){
         frame.setVisible(true);
         thread.start();
     }

     public void run(){
         try {
             this.socket = new Socket(ip,port);
             InputStream input = socket.getInputStream();
             this.in = new Scanner(input);

             OutputStream output = socket.getOutputStream();
             this.out = new PrintWriter(output,true);

             this.frame.addWindowListener(new WindowAdapter() {
                 public void windowClosing(WindowEvent e) {
                     try{
                         socket.close();
                     }catch(IOException ex){}
                 }
             });

             while (in.hasNextLine()) {
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

    private Game uploadGameFile(){
        this.fileUpload = new JFileChooser();

        int userSelection = fileUpload.showOpenDialog(this.frame);
        if(userSelection == JFileChooser.APPROVE_OPTION){
            File file = fileUpload.getSelectedFile();
            try{
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                Game gameIn = (Game)objectIn.readObject();
                System.out.println(gameIn.toString());
                return gameIn;
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

//    public static void main(String[] args){
//
//        EventQueue.invokeLater(new Runnable(){
//            public void run(){
//                try{
//                    new ClientWindow("localhost").start();
//                }catch(Exception e){}
//            }
//        });
//    }
}