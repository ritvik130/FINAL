package com.example.servicenovigrad.data.Class;

public class Customer extends UserModel {
    private Address address = null;
    private String firstName,lastName,dob;

    // constructor
    public Customer(){

    }
    public Customer(String username, String password, String email) {
        super(username, password, email);
    }

    public Customer(String username, String password, String email, Address address) {
        super(username, password, email);
        this.address = address;
    }

    public Customer(Address address) {
        this.address = address;
    }

    // getter and setter
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Customer(Address address, String firstName, String lastName, String dob) {
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
