package ChatServer;

import Models.Game;
import UI.TextPanel;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
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
    //private TextPanel log;
    private JEditorPane log;
    private JScrollPane logPane;
    private JTextField prompt;
    private JSplitPane center;
    private JPanel buttons;
    private JList users;

    String chatText = "";

    private JFileChooser fileUpload;
    private JButton upload;

    HashMap<String, String> commandMap;

    private final int port = 8888;
    private String ip;

    private Socket socket;

    //private Scanner in;
    //private volatile PrintWriter out;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Thread thread;

    GameMessage gameMessage;

    public ClientWindow(String ip) throws Exception {
        this.frame = new JFrame("Chat Client");

        this.ip = ip;

        this.frame.setPreferredSize(new Dimension(400, 400));

        this.log = new JEditorPane();
        log.setEditable(false);
        log.setContentType("text/html");

        Font font = new Font("Segoe UI", Font.PLAIN, 13);
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
                "font-size: " + font.getSize() + "pt; }";
        ((HTMLDocument)log.getDocument()).getStyleSheet().addRule(bodyRule);

        this.logPane = new JScrollPane(log);

        this.prompt = new JTextField();
        this.center = new JSplitPane();
        this.commandMap = new HashMap<>();

        this.buttons = new JPanel();
        this.buttons.setLayout(new FlowLayout(FlowLayout.LEFT,2,0));

        this.upload = new JButton("File Upload");

        this.gameMessage = new GameMessage();

        this.upload.addActionListener(e -> {
            Game game = uploadGameFile();
            if(game!=null){
                gameMessage.setGame(game);
            }
        });

        this.buttons.add(upload);

        this.center.setLayout(new BorderLayout());
        this.frame.setLayout(new BorderLayout());

        this.center.add(logPane, BorderLayout.CENTER);
        this.center.add(prompt, BorderLayout.SOUTH);
        this.center.add(buttons,BorderLayout.NORTH);

        this.frame.add(center,BorderLayout.CENTER);

        this.frame.setSize(400, 400);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.prompt.addActionListener(this);

        this.thread = new Thread(this);
        this.socket = new Socket(ip,port);

        this.frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try{
                    socket.close();
                }catch(IOException ex){}
            }
        });
    }

     public void start(){
         frame.setVisible(true);
         thread.start();
     }

     public void run(){
         try {
             InputStream input = socket.getInputStream();
             //this.in = new Scanner(input);
             this.in = new ObjectInputStream(input);

             OutputStream output = socket.getOutputStream();
             this.out = new ObjectOutputStream(output);

             //this.out = new PrintWriter(output,true);

             while(true){
                 try{
                     GameMessage gm = (GameMessage)in.readObject();
                     String str = gm.getMessage();
                     Game game = gm.getGame();

                     System.out.println(str);

                     display(str);
                     if(game != null){
                         display(game.toString());
                     }
                 }catch(ClassNotFoundException ce){
                     ce.printStackTrace();
                 }
             }

//             while (in.hasNextLine()) {
//                 display(in.nextLine());
//             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public void actionPerformed(ActionEvent e){
         //String s = prompt.getText();
         if(prompt.getText() != ""){
             gameMessage.setMessage(prompt.getText());
             if (out != null) {
                 try{
                     out.writeObject(gameMessage);
                 }catch(IOException ie){

                 }
                 //out.println(s);
             }
             this.gameMessage = new GameMessage();
         }
         //display(s);
         prompt.setText("");
     }

    private void display(final String s) {
        EventQueue.invokeLater(new Runnable() {
            //@Override
            public void run() {
                chatText = chatText + s + "\n <br>";
                //System.out.print(chatText);
                log.setText("<html>" +
                        "<head><style></style></head>" +
                        chatText +
                        "</html>");
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