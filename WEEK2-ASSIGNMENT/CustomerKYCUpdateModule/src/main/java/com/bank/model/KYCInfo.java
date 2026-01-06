package com.bank.model;

public class KYCInfo {
    private int kycId;
    private int userId;
    private String fullName;
    private String address;
    private String mobileNumber;
    private String email;
    private String dateOfBirth;
    
    // Constructors
    public KYCInfo() {}
    
    public KYCInfo(int userId, String fullName, String address, String mobileNumber, 
                   String email, String dateOfBirth) {
        this.userId = userId;
        this.fullName = fullName;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }
    
    // Getters and Setters
    public int getKycId() { return kycId; }
    public void setKycId(int kycId) { this.kycId = kycId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}