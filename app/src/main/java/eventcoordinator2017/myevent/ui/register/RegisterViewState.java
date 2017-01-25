package eventcoordinator2017.myevent.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

import eventcoordinator2017.myevent.app.Constants;


public class RegisterViewState implements RestorableViewState<RegisterView> {
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String birthday;
    private String contact;
    private String address;
    private String city;
    private String zipCode;
    private String country;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putString(Constants.EMAIL, email);
        out.putString(Constants.PASSWORD, password);
        out.putString(Constants.CONFIRM_PASSWORD, confirmPassword);
        out.putString(Constants.FIRST_NAME, firstName);
        out.putString(Constants.LAST_NAME, lastName);
        out.putString(Constants.BIRTHDAY, birthday);
        out.putString(Constants.CONTACT,contact);
        out.putString(Constants.ADDRESS, address);
        out.putString(Constants.CITY, city);
        out.putString(Constants.ZIP_CODE, zipCode);
        out.putString(Constants.COUNTRY, country);
    }

    @Override
    public RestorableViewState<RegisterView> restoreInstanceState(Bundle in) {
        email = in.getString(Constants.EMAIL, "");
        password = in.getString(Constants.PASSWORD, "");
        confirmPassword = in.getString(Constants.CONFIRM_PASSWORD, "");
        firstName = in.getString(Constants.FIRST_NAME, "");
        lastName = in.getString(Constants.LAST_NAME, "");
        birthday = in.getString(Constants.BIRTHDAY, "");
        contact = in.getString(Constants.CONTACT, "");
        address = in.getString(Constants.ADDRESS, "");
        city = in.getString(Constants.CITY, "");
        zipCode = in.getString(Constants.ZIP_CODE, "");
        country = in.getString(Constants.COUNTRY, "");

        return this;
    }

    @Override
    public void apply(RegisterView view, boolean retained) {
        view.setEditTextValue(email, password, confirmPassword,firstName,lastName,birthday,contact,address,city,zipCode,country);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
