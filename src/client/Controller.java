package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class Controller implements ActionListener{

    private GUI gui;

    public Controller(){

    }

    public void setGuiInstance(GUI gui){
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch(cmd){
            case "button_start":
                String ip = this.gui.getIp();
                String requestssec = this.gui.getRequestssec();
                String clientname = this.gui.getClientname();
                break;
            case "button_stop":
                break;
            case "button_close":
                System.exit(0);
        }
    }
}
