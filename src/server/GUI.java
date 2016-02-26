package server;

import javax.swing.*;

/**
 *
 */
public class GUI {

    private Controller c;

    private JFrame frame;

    public GUI(Controller c){
        this.c = c;
        this.initGui();
    }

    public void initGui(){
        this.frame = new JFrame("Server");
        this.frame.setSize(700,700);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);

        this.frame.setVisible(true);
    }

}
