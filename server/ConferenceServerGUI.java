package server;

import shared.ConferenceRegistration;
import shared.Participant;

import java.rmi.RemoteException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ConferenceServerGUI extends UnicastRemoteObject implements ConferenceRegistration {
    private List<Participant> participants;
    private JFrame frame;
    private JTextField hostField;
    private JTextField portField;
    private JTextField participantsField;
    private JTextArea logArea;
    private JButton startButton;
    private JButton stopButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton exitButton;
    private Registry registry;

    protected ConferenceServerGUI() throws RemoteException {
        participants = new ArrayList<>();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Conference Server");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 6));

        hostField = new JTextField("localhost");
        portField = new JTextField("1099");
        participantsField = new JTextField("0");
        participantsField.setEditable(false);

        topPanel.add(new JLabel("                 Host:"));
        topPanel.add(hostField);
        topPanel.add(new JLabel("                 Port:"));
        topPanel.add(portField);
        topPanel.add(new JLabel("Participants"));
        topPanel.add(participantsField);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setMargin(new Insets(10, 10, 10, 10));  
        JScrollPane scrollPane = new JScrollPane(logArea);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 5, 10, 0)); 

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        exitButton = new JButton("Exit");

        bottomPanel.add(startButton);
        bottomPanel.add(stopButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(loadButton);
        bottomPanel.add(exitButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveParticipants();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadParticipants();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void startServer() {
        try {
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText());
            registry = LocateRegistry.createRegistry(port);
            registry.rebind("ConferenceService", this);
            logArea.append("Server started on " + host + ":" + port + "\n");
        } catch (Exception e) {
            logArea.append("Failed to start server: " + e.getMessage() + "\n");
        }
    }

    private void stopServer() {
        try {
            if (registry != null) {
                registry.unbind("ConferenceService");
                UnicastRemoteObject.unexportObject(this, true);
                logArea.append("Server stopped\n");
            }
        } catch (Exception e) {
            logArea.append("Failed to stop server: " + e.getMessage() + "\n");
        }
    }

    private void saveParticipants() {
        String filePath = "participants.xml";
        XMLUtils.exportToXML(participants, filePath);
        logArea.append("Participants saved to " + filePath + "\n");
    }

    private void loadParticipants() {
        String filePath = "participants.xml";
        participants = XMLUtils.importFromXML(filePath);
        logArea.append("Participants loaded from " + filePath + "\n");
        displayParticipantsWithIndex();
    }
    
    private void displayParticipantsWithIndex() {
        logArea.append("Current participants:\n");
        for (int i = 0; i < participants.size(); i++) {
            Participant p = participants.get(i);
            int index = i + 1; 
            logArea.append("Participant #" + index + ": " + p.getName() + " " + p.getFamily() + " (" + p.getOrganization() + ") - " + p.getReport() + " - " + p.getEmail() + "\n");
        }
        updateParticipantsCount();
    }
    
    private void updateParticipantsCount() {
        participantsField.setText(String.valueOf(participants.size()));
    }

    @Override
    public int registerParticipant(Participant participant) throws RemoteException {
        participants.add(participant);
        int index = participants.size(); 
        logArea.append("Participant #" + index + ": Name:" + participant.getName() + " Family:" + participant.getFamily() +
                " Organization:" + participant.getOrganization() + " Report: " + participant.getReport() +
                " Email:" + participant.getEmail() + "\n");
        updateParticipantsCount();
        return index; 
    }

    @Override
    public List<Participant> getParticipants() throws RemoteException {
        return new ArrayList<>(participants);
    }

    @Override
    public String getParticipantsAsXML() throws RemoteException {
        try {
            return XMLUtils.convertToString(participants);
        } catch (Exception e) {
            logArea.append("Failed to convert participants to XML: " + e.getMessage() + "\n");
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            new ConferenceServerGUI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}






