package com.usta.models.documentations;

import java.sql.Date;

public class Documentation {
    private int id;
    private String documentType;
    private String documentNumber;
    private Date issueDate;
    private String issuingAuthority;
    private Integer livingAreaId;  // Cambié a Integer para permitir valores nulos

    public Documentation(int id, String documentType, String documentNumber, 
                         Date issueDate, String issuingAuthority, Integer livingAreaId) {
        this.id = id;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.issueDate = issueDate;
        this.issuingAuthority = issuingAuthority;
        this.livingAreaId = livingAreaId;  
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public Integer getLivingAreaId() {
        return livingAreaId;
    }

    public void setLivingAreaId(Integer livingAreaId) {
        this.livingAreaId = livingAreaId;
    }

    @Override
    public String toString() {
        return "Documentation{" +
                "id=" + id +
                ", documentType='" + documentType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", issueDate=" + issueDate +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                ", livingAreaId=" + livingAreaId +
                '}';
    }
}
