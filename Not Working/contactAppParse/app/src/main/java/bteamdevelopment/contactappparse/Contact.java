package bteamdevelopment.contactappparse;

import java.util.ArrayList;
import java.util.Arrays;

/* Created by wkohusjr on 9/23/2015.
 * Contact Class provides all of the getters and setters for the attributes
 */
public class Contact extends ArrayList<Contact> {

    String id;
    String fName = null;
    String lName = null;
    String email = null;
    String mobile = null;
    String home = null;
    String address = null;
    String city = null;
    String state = null;
    String zip = null;
    byte[] _image;

    public Contact() {

    }

    public Contact(String id, String fname, String lname, String mobile, String home, String email, String address, String city, String state, String zip, byte[] _image) {
        this.id = id;
        this.fName = fname;
        this.lName = lname;
        this.mobile = mobile;
        this.home = home;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this._image = _image;
    }

    public String getId(String objectId) {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fname) {
        this.fName = fname;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lname) {
        this.lName = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public byte[] getImage() {
        return this._image;
    }

    public void setImage(byte[] _image) {
        this._image = _image;
    }

    @Override
    public String toString() {
        return fName + " " + lName + " " + mobile + " " + home + " " + email + " " + address + " " + city + " " + state + " " + zip + " " + Arrays.toString(_image);
    }
}


