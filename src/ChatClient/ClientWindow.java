package ChatClient;

import UI.TextPanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class ClientWindow extends JFrame {
    TextPanel log;
    JTextField prompt;

    HashMap<String, String> commandMap;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private final int port = 8888;
    private String ip;

    public ClientWindow(String ip) {
        super("Chat Server");

        this.ip = ip;

        setPreferredSize(new Dimension(400, 400));

        this.log = new TextPanel();
        this.prompt = new JTextField();
        this.commandMap = new HashMap<>();

        setLayout(new BorderLayout());

        add(prompt, BorderLayout.SOUTH);
        add(log, BorderLayout.CENTER);

        pack();

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void startConnection() throws Exception{
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        prompt.addActionListener(e -> {
            String promptText = prompt.getText();
            try{
                String resp = sendMessage(promptText);
                log.appendBottomText(resp);
            }catch(IOException ioe){ }
        });
    }

    public void stopConnection() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
    }

}