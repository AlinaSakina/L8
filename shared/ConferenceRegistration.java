package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ConferenceRegistration extends Remote {
    int registerParticipant(Participant participant) throws RemoteException;
    List<Participant> getParticipants() throws RemoteException;
    String getParticipantsAsXML() throws RemoteException;  
}
