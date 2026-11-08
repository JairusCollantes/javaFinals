import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserPanel extends JFrame {
    private JComboBox comboBoxVehicle;
    private JComboBox comboBoxParking;
    private JTextField textFieldAvailability;
    private JTextField textFieldTotalFee;
    private JButton payButton;
    private JButton logOutButton;
    private JButton reserveButton;
    private JComboBox comboBoxReserveVehicle;
    private JComboBox comboBoxReserveService;
    private JTextField textFieldDate;
    private JTextField textFieldReserveAvailability;
    private JTextField textFieldReserveFee;
    private JButton logOutButton1;
    private JComboBox comboBoxMyActiveTickets;
    private JTextArea textArea1;
    private JButton logOutButton2;
    private JButton checkOutButton;
    private JTabbedPane mainTabbedPane;
    private JLabel labelName;

    private final ParkingSystem parkingSystem;
    private final String logInNames;
    private final String logInPlate;

    public UserPanel(ParkingSystem parkingSystem, String logInNames, String logInPlate) {
        super("SM PARKING User-Panel");
        this.parkingSystem = parkingSystem;
        this.logInNames = logInNames;
        this.logInPlate = logInPlate;
        
        setContentPane(mainTabbedPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(750,500);
        setLocationRelativeTo(null);

        labelName.setText("User Info: "+ logInNames +" (" + logInPlate + ")");
        updateUI();


        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (comboBoxParking.getSelectedItem() == null) {
                        JOptionPane.showMessageDialog(null,"Please select a parking type.",
                                "Error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String serviceName = comboBoxParking.getSelectedItem().toString();
                    ParkingType parkingType = createService(serviceName);

                    if (parkingSystem.getAvailable(parkingType) <= 0) {
                        JOptionPane.showMessageDialog(null,"No available capacity for "
                                + serviceName + ".",
                                "Error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String vt = comboBoxVehicle.getSelectedItem().toString();
                    Vehicle vehicle = new Vehicle(logInPlate, logInNames, vt);
                    double total = parkingType.computeTotalFee();
                    String breakdown;
                    if (serviceName.equals("Valet Service")) {
                        breakdown = "PHP 125.00 - Valet Service";
                    }else if (serviceName.equals("EV Charging")) {
                        breakdown = "PHP 50.00 — EV Charging";
                    }else {
                    breakdown= "PHP 25.00 - Regular";
                    }
                    if (JOptionPane.showConfirmDialog(null,
                            "Collect PHP " + String.format("%,.2f", total) +
                                    " for " + serviceName + "?\n\n" +
                                    breakdown + "\n\n" +
                                    "Plate: " + logInPlate + "   Owner: " + logInNames,
                            "Confirm Payment", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) return;

                    Ticket t = new Ticket(parkingSystem.nextTicketId(), vehicle, parkingType);
                    parkingSystem.addTicket(t);
                    updateUI();

                    JOptionPane.showMessageDialog(null,
                            "Payment Received — You May Now Enter\n\n" +
                                    "Ticket  : " + t.getTicketId() + "\n" +
                                    "Plate   : " + logInPlate + "\n" +
                                    "Service : " + serviceName + "\n" +
                                    "Paid    : PHP " + String.format("%,.2f", total),
                            "Ticket Issued", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });
        comboBoxParking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEntryAvailability();
                updateEntryFee();
            }
        });

        comboBoxReserveService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateResAvailability();
                updateResFee();
            }
        });

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String schedule = textFieldDate.getText().trim();
                    if (schedule.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"Please enter a schedule/date.",
                                "Error",JOptionPane.ERROR_MESSAGE);
                        return; }

                    if (comboBoxReserveService.getSelectedItem() == null) {
                        JOptionPane.showMessageDialog(null,"Please select a parking type.");
                        return;
                    }
                    String serviceName = comboBoxReserveService.getSelectedItem().toString();
                    ParkingType parkingType = createService(serviceName);

                    if (parkingSystem.getAvailable(parkingType) <= 0) {
                        JOptionPane.showMessageDialog(null,"No available capacity for " + serviceName + ".");
                        return;
                    }

                    for (Reservation r : parkingSystem.getReservations()) {
                        if (r.getVehicle().getOwnerName().equalsIgnoreCase(logInNames)
                                && r.getService().getLabel().equals(serviceName)) {
                            JOptionPane.showMessageDialog(null,"You already have a " + serviceName + " reservation scheduled.");
                            return;
                        }
                    }

                    String vt = comboBoxReserveVehicle.getSelectedItem().toString();
                    Vehicle vehicle = new Vehicle(logInPlate, logInNames, vt);
                    double est = parkingType.computeTotalFee();

                    if (JOptionPane.showConfirmDialog(null,
                            "Reserve " + serviceName + " on " + schedule + "?\n\n" +
                                    "Fee at check-in: PHP " + String.format("%,.2f", est),
                            "Confirm Reservation", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) return;

                    Reservation r = new Reservation(
                            parkingSystem.nextReservationId(), vehicle, parkingType, schedule);
                    parkingSystem.addReservation(r);
                    updateUI();

                    JOptionPane.showMessageDialog(null,
                            "Reservation Confirmed\n\n" +
                                    "ID       : " + r.getReservationId() + "\n" +
                                    "Service  : " + serviceName + "\n" +
                                    "Schedule : " + r.getScheduled(),
                            "Reservation Confirmed", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ticket t = (Ticket) comboBoxMyActiveTickets.getSelectedItem();
                if (t == null) {
                    JOptionPane.showMessageDialog(null,"No active ticket selected.");
                    return;
                }
                if (t.isExited()) {
                    JOptionPane.showMessageDialog(null,"Ticket already used for exit.");
                    return;
                }
                if (!t.getVehicle().getOwnerName().equalsIgnoreCase(logInNames)) {
                    JOptionPane.showMessageDialog(null,"You can only check out your own tickets.");
                    return;
                }

                if (JOptionPane.showConfirmDialog(null,
                        "Confirm exit of " + t.getVehicle().getPlateNumber() + "?",
                        "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) return;

                t.markExited();
                updateUI();

                JOptionPane.showMessageDialog(null,
                        "Exit Recorded. Thank you!",
                        "Goodbye", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        comboBoxMyActiveTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMyTicketInfo();
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPanel(parkingSystem).setVisible(true);
            }
        });
        logOutButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPanel(parkingSystem).setVisible(true);
            }
        });
        logOutButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPanel(parkingSystem).setVisible(true);
            }
        });
    }
    private ParkingType createService(String serviceName) {
        if(serviceName.equals("Valet Service")) {
            return new ValetPark();
        }
        if(serviceName.equals("EV Charging")) {
            return new EVPark();
        }
        return new RegularPark();
    }
    private void updateEntryAvailability() {
        if (comboBoxParking.getSelectedItem() == null) {
            textFieldAvailability.setText("");
            return;
        }
        String serviceName = comboBoxParking.getSelectedItem().toString();
        ParkingType service = createService(serviceName);
        textFieldAvailability.setText(parkingSystem.getAvailable(service) + " / " + service.getCapacity());
    }

    private void updateEntryFee() {
        if (comboBoxParking.getSelectedItem() == null) {
            textFieldTotalFee.setText("PHP 0.00");
            return;
        }
        String serviceName = comboBoxParking.getSelectedItem().toString();
        double total = createService(serviceName).computeTotalFee();
        textFieldTotalFee.setText("PHP " + String.format("%,.2f", total));
    }

    private void updateResAvailability() {
        if (comboBoxReserveService.getSelectedItem() == null) { textFieldReserveAvailability.setText(""); return; }
        String serviceName = comboBoxReserveService.getSelectedItem().toString();
        ParkingType service = createService(serviceName);
        textFieldReserveAvailability.setText(parkingSystem.getAvailable(service) + " / " + service.getCapacity());
    }

    private void updateResFee() {
        if (comboBoxReserveService.getSelectedItem() == null) { textFieldReserveFee.setText("PHP 0.00"); return; }
        String serviceName = comboBoxReserveService.getSelectedItem().toString();
        double total = createService(serviceName).computeTotalFee();
        textFieldReserveFee.setText("PHP " + String.format("%,.2f", total));
    }

    private void updateMyTicketInfo() {
        Ticket t = (Ticket) comboBoxMyActiveTickets.getSelectedItem();
        if (t == null) { textArea1.setText("No active tickets for you."); return; }
        textArea1.setText(
                "Ticket  : " + t.getTicketId() + "\n" +
                        "Plate   : " + t.getVehicle().getPlateNumber() + "\n" +
                        "Service : " + t.getService().getLabel() + "\n" +
                        "Paid    : PHP " + String.format("%,.2f", t.getTotalFee()));
    }

    void updateUI() {
        updateEntryAvailability();
        updateEntryFee();
        updateResAvailability();
        updateResFee();

        List<Ticket> mine = parkingSystem.getTicketsByOwner(logInNames);
        DefaultComboBoxModel<Ticket> model = new DefaultComboBoxModel<>();
        for (Ticket t : mine) {
            if (!t.isExited()) model.addElement(t);
        }
        comboBoxMyActiveTickets.setModel(model);

        updateMyTicketInfo();

    }
}
