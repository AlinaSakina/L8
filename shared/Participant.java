package shared;

import java.io.Serializable;

public class Participant implements Serializable {
    private String name;
    private String family;
    private String organization;
    private String report;
    private String email;

    public Participant() {
    }

    public Participant(String name, String family, String organization, String report, String email) {
        this.name = name;
        this.family = family;
        this.organization = organization;
        this.report = report;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
