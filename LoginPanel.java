import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JFrame {

    private static final String ADMIN_PASSWORD = "admin123";

    private JPanel mainPanel;
    private JRadioButton userRadioButton;
    private JRadioButton adminRadioButton;
    private JButton loginButton;
    private JTextField textFieldName;
    private JPasswordField passwordField;
    private JTextField textFieldPlateNumber;
    private JLabel labelName;
    private JLabel labelPlate;
    private JLabel labelPassword;

    public LoginPanel() {
        super("SM Parking Log-in");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);

        userRadioButton.setSelected(true);

        toggleRole();

        userRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleRole();
            }
        });
        adminRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleRole();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(adminRadioButton.isSelected()) {
                    String password = new String(passwordField.getPassword());
                    if(!ADMIN_PASSWORD.equals(password)) {
                        JOptionPane.showMessageDialog(null, "Wrong Password!");
                        return;
                    }
                    //openWindowAdmin
                } else{
                    String name = textFieldName.getText();
                    String plateNumber = textFieldPlateNumber.getText();
                    if(name.isEmpty() || plateNumber.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Name or Plate Cannot be empty");
                    }
                    //openUserWindow
                }
            }
        });
    }

    private void toggleRole(){
        boolean admin = adminRadioButton.isSelected();
        labelName.setVisible(!admin);
        labelPlate.setVisible(!admin);
        textFieldPlateNumber.setVisible(!admin);
        textFieldName.setVisible(!admin);
        labelPassword.setVisible(admin);
        passwordField.setVisible(admin);
    }

    public static void main(String [] args){
        LoginPanel login = new LoginPanel();
        login.setVisible(true);
    }
}
