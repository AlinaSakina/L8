package client;

import shared.ConferenceRegistration;
import shared.Participant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class ConferenceClientGUI {
    private JFrame frame;
    private JTextField hostField;
    private JTextField portField;
    private JTextField participantsField;
    private JTextField nameField;
    private JTextField familyField;
    private JTextField organizationField;
    private JTextField reportField;
    private JTextField emailField;
    private ConferenceRegistration conferenceService;

    public ConferenceClientGUI() {
        initializeGUI();
        connectToServer();
    }

    private void initializeGUI() {
        frame = new JFrame("Conference Registration Client");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 6));

        hostField = new JTextField("localhost", 10);
        portField = new JTextField("1099", 5);
        participantsField = new JTextField("0", 5);
        participantsField.setEditable(false);

        topPanel.add(new JLabel("Host"));
        topPanel.add(hostField);
        topPanel.add(new JLabel("Port"));
        topPanel.add(portField);
        topPanel.add(new JLabel("Participants"));
        topPanel.add(participantsField);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(5, 3, 5, 5));

        nameField = new JTextField(10);
        familyField = new JTextField(10);
        organizationField = new JTextField(10);
        reportField = new JTextField(10);
        emailField = new JTextField(10);

        centerPanel.add(new JLabel("    Name:"));
        centerPanel.add(nameField);
        centerPanel.add(new JLabel());
        
        centerPanel.add(new JLabel("    Family:"));
        centerPanel.add(familyField);
        centerPanel.add(new JLabel());
        
        centerPanel.add(new JLabel("    Organization:"));
        centerPanel.add(organizationField);
        centerPanel.add(new JLabel());
        
        centerPanel.add(new JLabel("    Report:"));
        centerPanel.add(reportField);
        centerPanel.add(new JLabel());
        
        centerPanel.add(new JLabel("    Email:"));
        centerPanel.add(emailField);
        centerPanel.add(new JLabel());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear");
        JButton getInfoButton = new JButton("Get Info");
        JButton finishButton = new JButton("Finish");

        bottomPanel.add(registerButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(getInfoButton);
        bottomPanel.add(finishButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerParticipant();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        getInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getParticipantsInfo();
            }
        });

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void connectToServer() {
        try {
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText());
            conferenceService = (ConferenceRegistration) Naming.lookup("//" + host + ":" + port + "/ConferenceService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to connect to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void registerParticipant() {
        try {
            String name = nameField.getText();
            String family = familyField.getText();
            String organization = organizationField.getText();
            String report = reportField.getText();
            String email = emailField.getText();

            Participant participant = new Participant(name, family, organization, report, email);
            int count = conferenceService.registerParticipant(participant);
            participantsField.setText(String.valueOf(count));
            JOptionPane.showMessageDialog(frame, "Registration successful! Total participants: " + count);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(frame, "Failed to register participant: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        familyField.setText("");
        organizationField.setText("");
        reportField.setText("");
        emailField.setText("");
    }

    private void getParticipantsInfo() {
        try {
            List<Participant> participants = conferenceService.getParticipants();
            StringBuilder participantsList = new StringBuilder();
            for (Participant p : participants) {
                participantsList.append(p.getName()).append(" ").append(p.getFamily())
                        .append(" (").append(p.getOrganization()).append(") - ")
                        .append(p.getReport()).append(" - ").append(p.getEmail()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, participantsList.toString(), "Participants Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(frame, "Failed to get participants info: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ConferenceClientGUI();
    }
}













