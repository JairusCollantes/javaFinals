import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminPanel  extends JFrame {
    private JTabbedPane mainTabPanel;
    private JComboBox comboBoxTickets;
    private JTextArea textArea;
    private JButton checkOutButton;
    private JButton logOutButton;
    private JTable tableHistory;
    private JButton logOutButtonHistory;
    private JRadioButton allRadioButton;
    private JRadioButton inLotRadioButton;
    private JRadioButton exitedRadioButton;

    DefaultTableModel tableModelHistory;

    private final ParkingSystem parkingSystem;

    public AdminPanel(ParkingSystem manager) {
        super("SM PARKING Admin-Panel");
        this.parkingSystem = manager;
        
        setContentPane(mainTabPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);

        tableModelHistory = new DefaultTableModel();
        tableModelHistory.addColumn("Ticket");
        tableModelHistory.addColumn("Plate");
        tableModelHistory.addColumn("Name");
        tableModelHistory.addColumn("Vehicle");
        tableModelHistory.addColumn("Service");
        tableModelHistory.addColumn("Price");
        tableModelHistory.addColumn("Status");

        tableHistory.setModel(tableModelHistory);
        allRadioButton.setSelected(true);
        updateUI();

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPanel(parkingSystem).setVisible(true);
            }
        });
        logOutButtonHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPanel(parkingSystem).setVisible(true);
            }
        });
        allRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshHistory();
            }
        });
        inLotRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshHistory();
            }
        });
        exitedRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshHistory();
            }
        });
        comboBoxTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateExitSummary();
            }
        });
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ticket ticket = (Ticket)comboBoxTickets.getSelectedItem();
                if (ticket == null) {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please Select a Ticket", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(ticket.isExited()){
                    JOptionPane.showMessageDialog(AdminPanel.this, "Ticket Already Checked Out", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (JOptionPane.showConfirmDialog(null,
                        "Check out " + ticket.getVehicle().getPlateNumber() +
                                " (owner: " + ticket.getVehicle().getOwnerName() + ")?",
                            "Confirm",JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                    return;
                }
                ticket.markExited();
                updateUI();

                JOptionPane.showMessageDialog(null, "Ticket "+ticket.getTicketId()+" Marked Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void refreshHistory(){
        tableModelHistory.setRowCount(0);
        boolean inLot =inLotRadioButton.isSelected();
        boolean exit = exitedRadioButton.isSelected();

        List<Ticket> tickets = parkingSystem.getTickets();
        for(Ticket t: tickets ){
            if(inLot && t.isExited()){
                continue;
            }
            if(exit && !t.isExited()){
                continue;
            }
            tableModelHistory.addRow(new Object[]{
                    t.getTicketId(),
                    t.getVehicle().getPlateNumber(),
                    t.getVehicle().getOwnerName(),
                    t.getVehicle().getVehicleType(),
                    t.getService().getLabel(),
                    String.format("%.2f", t.getTotalFee()),
                    t.isExited() ?"Exited":"In Lot"
            });
        }
    }
     void updateExitSummary() {
        Ticket t = (Ticket) comboBoxTickets.getSelectedItem();
        if (t == null) {
            textArea.setText("No active tickets.");
            return;
        }
        textArea.setText(
                "Ticket  : " + t.getTicketId() + "\n" +
                        "Plate   : " + t.getVehicle().getPlateNumber() + "\n" +
                        "Owner   : " + t.getVehicle().getOwnerName() + "\n" +
                        "Service : " + t.getService().getLabel() + "\n" +
                        "Paid    : PHP " + String.format("%,.2f", t.getTotalFee()));
    }
    void updateUI(){
        List<Ticket> tickets = parkingSystem.getTickets();
        DefaultComboBoxModel<Ticket> model = new DefaultComboBoxModel<>();
        for(Ticket t: tickets){
            model.addElement(t);
        }
        comboBoxTickets.setModel(model);
        updateExitSummary();
        refreshHistory();
    }
}
