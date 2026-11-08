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
    private JTable tableReservation;
    private JButton buttonReserveLogOut;
    private JButton cancelReservationButton;
    private JButton checkInButton;

    DefaultTableModel tableModelHistory;
    DefaultTableModel tableModelReserve;

    private final ParkingSystem parkingSystem;

    public AdminPanel(ParkingSystem manager) {
        super("SM PARKING Admin-Panel");
        this.parkingSystem = manager;
        
        setContentPane(mainTabPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(750,500);
        setLocationRelativeTo(null);

        tableModelHistory = new DefaultTableModel();
        tableModelHistory.addColumn("Ticket");
        tableModelHistory.addColumn("Plate");
        tableModelHistory.addColumn("Name");
        tableModelHistory.addColumn("Vehicle");
        tableModelHistory.addColumn("Service");
        tableModelHistory.addColumn("Price");
        tableModelHistory.addColumn("Status");

        tableModelReserve = new DefaultTableModel();
        tableModelReserve.addColumn("ID");
        tableModelReserve.addColumn("Plate");
        tableModelReserve.addColumn("Name");
        tableModelReserve.addColumn("Service");
        tableModelReserve.addColumn("Schedule");
        tableModelReserve.addColumn("Status");

        tableHistory.setModel(tableModelHistory);
        tableReservation.setModel(tableModelReserve);
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
        buttonReserveLogOut.addActionListener(new ActionListener() {
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
        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tableReservation.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please Select a Reservation", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String id = tableModelReserve.getValueAt(row, 0).toString();
                Reservation r = null;
                for (Reservation res : parkingSystem.getReservations()) {
                    if (res.getReservationId().equals(id)) {
                        r = res;
                        break;
                    }
                }
                if (r == null) {
                    return;
                }
                double fee = r.getService().computeTotalFee();
                if (JOptionPane.showConfirmDialog(null,
                        "Check in " + r.getVehicle().getPlateNumber() +
                                " (owner: " + r.getVehicle().getOwnerName() + ")?\n" +
                                "Collect fee: PHP " + String.format("%,.2f", fee),
                        "Confirm Check-In", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                    return;
                }
                Ticket t = new Ticket(parkingSystem.nextTicketId(), r.getVehicle(), r.getService());
                parkingSystem.addTicket(t);
                parkingSystem.removeReservation(r.getReservationId());
                updateUI();

                JOptionPane.showMessageDialog(null, "Ticket " + t.getTicketId() + " Issued — Payment Collected",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        cancelReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tableReservation.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please Select a Reservation", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String id = tableModelReserve.getValueAt(row, 0).toString();
                if (JOptionPane.showConfirmDialog(null,
                        "Cancel reservation " + id + "?",
                        "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                    return;
                }
                parkingSystem.removeReservation(id);
                updateUI();

                JOptionPane.showMessageDialog(null, "Reservation " + id + " Cancelled", "Success", JOptionPane.INFORMATION_MESSAGE);
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
    private void refreshReservations() {
        tableModelReserve.setRowCount(0);
        for (Reservation r : parkingSystem.getReservations()) {
            tableModelReserve.addRow(new Object[]{
                    r.getReservationId(),
                    r.getVehicle().getPlateNumber(),
                    r.getVehicle().getOwnerName(),
                    r.getService().getLabel(),
                    r.getScheduled(),
                    "Reserved"
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
        List<Ticket> tickets = parkingSystem.getActiveTickets();
        DefaultComboBoxModel<Ticket> model = new DefaultComboBoxModel<>();
        for(Ticket t: tickets){
            model.addElement(t);
        }
        comboBoxTickets.setModel(model);
        updateExitSummary();
        refreshHistory();
        refreshReservations();
    }
}
