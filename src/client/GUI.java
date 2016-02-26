package client;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class GUI {

    private Controller c;

    private JFrame frame;

    private JTextField in_ip;
    private JTextField in_requestssec;
    private JTextField in_clientname;
    private JButton button_start;
    private JButton button_stop;
    private JButton button_close;

    private JTextArea out_requests;
    private JTextArea out_response;

    public GUI(Controller c){
        this.c = c;
        this.initGui();
    }

    public void initGui(){
        /** FRAME INIT */
        this.frame = new JFrame("Client");
        this.frame.setSize(600,600);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);

        /** MAINPANEL INIT */
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));

        mainPanel.add(Box.createRigidArea(new Dimension(30,0)));
        JPanel panelContent = new JPanel();
        panelContent.setLayout(new BoxLayout(panelContent, BoxLayout.PAGE_AXIS));
        mainPanel.add(panelContent);
        mainPanel.add(Box.createRigidArea(new Dimension(30,0)));

        /** PANEL FOR INPUT OF LOADBALANCER IP */
        JPanel panelIp = new JPanel();
        panelIp.setLayout(new BoxLayout(panelIp, BoxLayout.LINE_AXIS));
        panelIp.add(new JLabel("Loadbalancer IP"));
        panelIp.add(Box.createRigidArea(new Dimension(30,0)));
        this.in_ip = new JTextField(15);
        this.in_ip.setMaximumSize(new Dimension(300,30));
        panelIp.add(this.in_ip);

        /** PANEL FOR INPUT OF REQUESTS/SECOND */
        JPanel panelRequestssec = new JPanel();
        panelRequestssec.setLayout(new BoxLayout(panelRequestssec, BoxLayout.LINE_AXIS));
        panelRequestssec.add(new JLabel("Requests/s"));
        panelRequestssec.add(Box.createRigidArea(new Dimension(65,0)));
        this.in_requestssec = new JTextField(15);
        this.in_requestssec.setMaximumSize(new Dimension(300,30));
        panelRequestssec.add(this.in_requestssec);

        /** PANEL FOR INPUT OF CLIENTNAME */
        JPanel panelClientname = new JPanel();
        panelClientname.setLayout(new BoxLayout(panelClientname, BoxLayout.LINE_AXIS));
        panelClientname.add(new JLabel("Client Name"));
        panelClientname.add(Box.createRigidArea(new Dimension(58,0)));
        this.in_clientname = new JTextField(15);
        this.in_clientname.setMaximumSize(new Dimension(300,30));
        panelClientname.add(this.in_clientname);

        /** PANEL FOR CONTROL BUTTONS */
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.LINE_AXIS));
        this.button_start = new JButton("Start");
        this.button_start.setActionCommand("button_start");
        this.button_start.addActionListener(this.c);
        this.button_stop = new JButton("Stop");
        this.button_stop.setActionCommand("button_stop");
        this.button_stop.addActionListener(this.c);
        this.button_close = new JButton("Close");
        this.button_close.setActionCommand("button_close");
        this.button_close.addActionListener(this.c);
        panelButtons.add(this.button_start);
        panelButtons.add(Box.createRigidArea(new Dimension(30,0)));
        panelButtons.add(this.button_stop);
        panelButtons.add(Box.createRigidArea(new Dimension(30,0)));
        panelButtons.add(this.button_close);

        /** PANEL FOR OUTPUT OF REQUESTS AND RESPONSE */
        JPanel panelOutput = new JPanel();
        panelOutput.setLayout(new BoxLayout(panelOutput, BoxLayout.LINE_AXIS));

        // REQUESTS
        JPanel panelRequests = new JPanel();
        panelRequests.setLayout(new BoxLayout(panelRequests, BoxLayout.PAGE_AXIS));
        panelRequests.add(new JLabel("Requests"));
        panelRequests.add(Box.createRigidArea(new Dimension(0,10)));
        this.out_requests = new JTextArea();
        this.out_requests.setRows(10);
        this.out_requests.setEditable(false);
        this.out_requests.setFocusable(false);
        this.out_requests.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panelRequests.add(this.out_requests);

        // RESPONSE
        JPanel panelResponse = new JPanel();
        panelResponse.setLayout(new BoxLayout(panelResponse, BoxLayout.PAGE_AXIS));
        panelResponse.add(new JLabel("Response"));
        panelResponse.add(Box.createRigidArea(new Dimension(0,10)));
        this.out_response = new JTextArea();
        this.out_response.setRows(10);
        this.out_response.setEditable(false);
        this.out_response.setFocusable(false);
        this.out_response.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panelResponse.add(this.out_response);

        // PUT TOGETHER
        panelOutput.add(panelRequests);
        panelOutput.add(Box.createRigidArea(new Dimension(30,0)));
        panelOutput.add(panelResponse);

        /** ADD EVERYTHING TO CONTENT PANEL */
        panelContent.add(Box.createRigidArea(new Dimension(0,30)));
        panelContent.add(panelIp);
        panelContent.add(Box.createRigidArea(new Dimension(0,10)));
        panelContent.add(panelRequestssec);
        panelContent.add(Box.createRigidArea(new Dimension(0,10)));
        panelContent.add(panelClientname);
        panelContent.add(Box.createRigidArea(new Dimension(0,20)));
        panelContent.add(panelButtons);
        panelContent.add(Box.createRigidArea(new Dimension(0,80)));
        panelContent.add(panelOutput);
        panelContent.add(Box.createRigidArea(new Dimension(0,30)));

        /** ADD TO MAINPANEL AND SET VISIBLE */
        this.frame.add(mainPanel);

        this.frame.setVisible(true);
    }

    public String getIp(){
        return this.in_ip.getText();
    }

    public String getRequestssec(){
        return this.in_requestssec.getText();
    }

    public String getClientname(){
        return this.in_clientname.getText();
    }

    public void addOutRequests(String text){
        this.out_requests.append(text);
    }

    public void addOutResponse(String text){
        this.out_response.append(text);
    }

}
