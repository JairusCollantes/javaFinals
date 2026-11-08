import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
    private JLabel labelUsers;
    private JComboBox comboBoxUsers;

    private final ParkingSystem parkingSystem;

    public LoginPanel(ParkingSystem manager) {
        super("SM Parking Log-in");
        this.parkingSystem = manager;
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);

        userRadioButton.setSelected(true);

        toggleRole();
        refreshUserCombo();

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
                    dispose();
                    AdminPanel adminPanel = new AdminPanel(parkingSystem);
                    adminPanel.setVisible(true);
                } else{
                    String name = textFieldName.getText();
                    String plateNumber = textFieldPlateNumber.getText();
                    if(name.isEmpty() || plateNumber.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Name or Plate Cannot be empty");
                        return;
                    }
                    if(parkingSystem.hasUser(name)){
                        parkingSystem.addUser(new User(name, plateNumber));
                    }
                    dispose();
                    UserPanel userPanel = new UserPanel(parkingSystem,name,plateNumber);
                    userPanel.setVisible(true);
                }
            }
        });
        comboBoxUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object sel = comboBoxUsers.getSelectedItem();
                if (sel == null) {
                    return;
                }
                String selected = sel.toString();
                if ("None".equals(selected)) {
                    textFieldName.setText("");
                    textFieldPlateNumber.setText("");
                } else if (sel instanceof User) {
                    User u = (User) sel;
                    textFieldName.setText(u.getName());
                    textFieldPlateNumber.setText(u.getPlate());
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
        labelUsers.setVisible(!admin);
        comboBoxUsers.setVisible(!admin);
    }

    private void refreshUserCombo() {
        comboBoxUsers.removeAllItems();
        comboBoxUsers.addItem("None");
        List<User> all = parkingSystem.getUsers();
        for (User u : all) {
            comboBoxUsers.addItem(u);
        }
    }

    public static void main(String [] args){
        ParkingSystem parkingSystem = new ParkingSystem();
        LoginPanel login = new LoginPanel(parkingSystem);
        login.setVisible(true);
    }
}
