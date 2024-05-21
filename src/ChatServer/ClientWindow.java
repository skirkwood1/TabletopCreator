package ChatServer;

import ChatServer.Messages.GameMessage;
import ChatServer.Messages.QuitMessage;
import Models.Game;
import UI.UIHelpers.FileChooserCreator;
import UI.UIHelpers.FileViewerUI;
import UI.UIHelpers.ScrollBarUICreator;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientWindow implements ActionListener,Runnable {

    private JFrame frame;
    //private CommandLog log;
    private JTextPane log;
    private JScrollPane logPane;
    private JTextField prompt;
    private JSplitPane center;
    private JPanel buttons;
    private JList<String> users;

    String chatText = "";

    private JFileChooser fileUpload;
    private JButton upload;
    private JLabel uploadNote;
    private JButton cancelUpload;

    private GameListener gameListener;
    private ArrayList<Game> pastMessages;

    HashMap<String, String> commandMap;

    private final int port = 8888;
    private String ip;

    private Socket socket;

    //private Scanner in;
    //private volatile PrintWriter out;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Thread thread;

    private GameMessage gameMessage;

    public ClientWindow(String ip) throws Exception {
        this.frame = new JFrame("Chat Client");

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("ChatIcon.png"));
        this.frame.setIconImage(icon.getImage());

        this.fileUpload = new JFileChooser();
        fileUpload.setBackground(Color.WHITE);
        fileUpload.setOpaque(true);
        fileUpload.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        fileUpload.setFileView(new FileViewerUI());


        Component[] comp = fileUpload.getComponents();
        FileChooserCreator.setFileChooserElements(comp);

        this.ip = ip;
        this.pastMessages = new ArrayList<>();

        this.frame.setPreferredSize(new Dimension(400, 400));

        this.log = new JTextPane();
        log.setEditable(false);
        //log.setContentType("text/html");

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        log.setFont(font);
//        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
//                "font-size: " + font.getSize() + "pt; }";
//        String wordWrap = "div {\n" +
//                "    width: 400px;\n" +
//                "    word-wrap: break-word;\n" +
//                "}";
//        ((HTMLDocument)log.getDocument()).getStyleSheet().addRule(bodyRule);
//        ((HTMLDocument)log.getDocument()).getStyleSheet().addRule(wordWrap);

        this.logPane = new JScrollPane(log);
        logPane.getVerticalScrollBar().setUI(ScrollBarUICreator.scrollBarUI());

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

        this.uploadNote = new JLabel("");
        Dimension labelSize = new Dimension(200,50);
        this.uploadNote.setMaximumSize(labelSize);
        this.uploadNote.setSize(labelSize);

        this.cancelUpload = new JButton("X");
        this.cancelUpload.addActionListener(e -> {removeGameUpload();});
        this.cancelUpload.setVisible(false);

        //uploadConfirmBar.add(cancelUpload);
        this.buttons.add(uploadNote);
        this.buttons.add(cancelUpload);
        //uploadConfirmBar.setVisible(false);

        this.center.setLayout(new BorderLayout());
        this.frame.setLayout(new BorderLayout());

        this.center.add(logPane, BorderLayout.CENTER);
        this.center.add(prompt, BorderLayout.SOUTH);
        this.center.add(buttons,BorderLayout.NORTH);

        this.center.setPreferredSize(new Dimension(400,400));

        this.users = new JList<>();
        JScrollPane userPane = new JScrollPane(users);
        userPane.setPreferredSize(new Dimension(200,400));

        this.frame.add(center,BorderLayout.CENTER);
        this.frame.add(userPane,BorderLayout.EAST);

        this.frame.setSize(600, 400);
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        this.prompt.addActionListener(this);

        this.thread = new Thread(this);
        this.socket = new Socket(ip,port);
    }

     public void start(){
         frame.setVisible(true);
         thread.start();
     }

     public void run(){
         try (
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
         ){
             this.out = out;
             this.in = in;

             this.frame.addWindowListener(new WindowAdapter() {
                 public void windowClosing(WindowEvent e) {
                     closeFrame();
                 }
             });


             //this.out = new PrintWriter(output,true);

             while(true){
                 int index = 0;

                 try{
                     GameMessage gm = (GameMessage)in.readObject();
                     String str = gm.getMessage();
                     Game game = gm.getGame();
                     long fileSize = gm.getFileSize();

                     if(gm.getClients().size() != 0){
                         DefaultListModel<String> dlm = new DefaultListModel<>();
                         dlm.addAll(gm.getClients());
                         users.setModel(dlm);
                     }

                     if(game != null){
                         this.pastMessages.add(game);
                         String gameName = game.getName();

                         ChatGameButton gameButton = new ChatGameButton(game.getBoard().getColor());
                         gameButton.setText(gameName);
                         gameButton.setDataSize(fileSize);
                         gameButton.setOpaque(false);
                         gameButton.setPreferredSize(new Dimension(240,60));
                         gameButton.setMinimumSize(new Dimension(240,60));
                         gameButton.setFocusPainted(false);
                         //gameButton.
                         gameButton.addActionListener(new ActionListener() {
                             @Override
                             public void actionPerformed(ActionEvent e) {
                                 gameListener.gameEmitted(game);
                             }
                         });

                         display(str,gameButton);
                         //log.insertComponent(gameButton);

                         index++;
                     }else{
                         display(str,null);
                     }

                     //gameListener.gameEmitted(game);
                 }catch(ClassNotFoundException ce){
                     ce.printStackTrace();
                 }
             }

//             while (in.hasNextLine()) {
//                 display(in.nextLine());
//             }
         } catch (IOException e) {}
     }

     public void actionPerformed(ActionEvent e){
         //String s = prompt.getText();
         if(!prompt.getText().equals("")){
             gameMessage.setMessage(prompt.getText());
             if (out != null) {
                 try{
                     out.writeObject(gameMessage);
                     this.gameMessage = new GameMessage();
                     this.uploadNote.setText("");
                     this.cancelUpload.setVisible(false);
                 }catch(IOException ie){

                 }
                 //out.println(s);
             }

         }
         //display(s);
         prompt.setText("");
     }

    private void display(final String s,ChatGameButton cgb) {
        EventQueue.invokeLater(new Runnable() {
            //@Override
            public void run() {
                StyledDocument doc = log.getStyledDocument();
                SimpleAttributeSet attr = new SimpleAttributeSet();
                try{
                    doc.insertString(doc.getLength(),s + "\n\r" ,attr);
                    if(cgb != null){
                        log.insertComponent(cgb);
                        doc.insertString(doc.getLength(), "\n\r", attr );
                    }
                    log.setCaretPosition(log.getDocument().getLength());
                }catch(Exception e){

                }
            }
        });
    }

    private Game uploadGameFile(){
        int userSelection = fileUpload.showOpenDialog(this.frame);
        if(userSelection == JFileChooser.APPROVE_OPTION){
            File file = fileUpload.getSelectedFile();
            gameMessage.setFileSize(file.length());

            try{
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                Game gameIn = (Game)objectIn.readObject();
                this.uploadNote.setText("Attached: " + file.getName());
                this.cancelUpload.setVisible(true);
                //System.out.println(gameIn.toString());
                return gameIn;
            }catch(Exception ex){
                this.uploadNote.setText("Failed attachment");
                ex.printStackTrace();
            }
        }
        return null;
    }

    public void removeGameUpload(){
        gameMessage.setGame(null);
        this.cancelUpload.setVisible(false);
        this.uploadNote.setText("");
    }

    public void setGameListener(GameListener gameListener){
        this.gameListener = gameListener;
    }

    public void closeFrame(){
        try {
            out.writeObject(new QuitMessage());
            System.out.println("Quit");
            out.close();
            in.close();
            socket.close();
        } catch (IOException ex) {
        }
        frame.dispose();
    }
}