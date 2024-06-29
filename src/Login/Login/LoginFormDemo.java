package Login.Login;

import javax.swing.*;
import java.awt.*;

import java.lang.Exception;

class LoginFormDemo {
    // main() method start
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);

    }

    public static void main(String arg[]) {
        try {
            // create instance of the CreateLoginForm
            CreateLoginForm form = new CreateLoginForm();
            form.setSize(900, 600); // set size of the frame
            centreWindow(form); // center the frame
            form.setVisible(true); // make form visible to the user
        } catch (Exception e) {
            // handle exception
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
