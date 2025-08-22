package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "setting")
public class RestaurantSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "logo_url")
    private String logoUrl;

    @Column(nullable = false, name = "address")
    private String address;

    @Column(nullable = false, name = "working_hours")
    private String workingHours;

    @Column(nullable = false, name = "about_image_url")
    private String aboutImageUrl;

    @Column(nullable = false, name = "about_description")
    private String aboutDescription;

    @Column(nullable = false, name = "terms_and_conditions")
    private String termsAndConditions;

    @Column(nullable = false, name = "facebook_url")
    private String facebookUrl;

    @Column(nullable = false, name = "whatsapp_number")
    private String whatsappNumber;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, name = "second_phone_number")
    private String secondPhoneNumber;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public String getAboutImageUrl() { return aboutImageUrl; }
    public void setAboutImageUrl(String aboutImageUrl) { this.aboutImageUrl = aboutImageUrl; }

    public String getAboutDescription() { return aboutDescription; }
    public void setAboutDescription(String aboutDescription) { this.aboutDescription = aboutDescription; }

    public String getTermsAndConditions() { return termsAndConditions; }
    public void setTermsAndConditions(String termsAndConditions) { this.termsAndConditions = termsAndConditions; }

    public String getFacebookUrl() { return facebookUrl; }
    public void setFacebookUrl(String facebookUrl) { this.facebookUrl = facebookUrl; }

    public String getWhatsappNumber() { return whatsappNumber; }
    public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSecondPhoneNumber() { return secondPhoneNumber; }
    public void setSecondPhoneNumber(String secondPhoneNumber) { this.secondPhoneNumber = secondPhoneNumber; }
}