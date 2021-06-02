import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame{

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //declare components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.BOLD, 20); 


    public Client(){


        try{
            System.out.println("Sending request to Server....");
             socket = new Socket("127.0.0.1", 7777);
             System.out.println("Connection done");

             br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();


             startReading();
            // startWriting();

        }catch(Exception e){
            e.printStackTrace();

        }
    }
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
               
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
               
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    //10 means u have pressed enter button
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + " " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
                
            }
            
        });
    }

    private void createGUI(){
        //GUI code..

        this.setTitle("Client Messager");
        this.setSize(550, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        //heading design
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //layout for frame
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);



        this.setVisible(true);

    }

    //start reading
    public void startReading(){
        //thread - will read
        Runnable r1 =()->{

            System.out.println("Reader Started....");

            while(true){
                try{
                    String msg = br.readLine();

                    if(msg.equals("exit")){

                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        break;
                    }
                    //System.out.println("Server :" + " " + msg);
                    messageArea.append("Server :" + " " + msg + "\n");

                }catch(Exception e){
                    e.printStackTrace();
                }
            }


        };
        new Thread(r1).start();

    }

    //start writing
    public void startWriting(){
        //thread - will write
        Runnable r2 =()->{
            System.out.println("Writer started....");

            while(true){

                try{

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                }catch(Exception e){
                    e.printStackTrace();
                }


            }

        };
        new Thread(r2).start();

    }
    public static void main(String[] args){
        System.out.println("This is Client.....");
        new Client();
    }
}