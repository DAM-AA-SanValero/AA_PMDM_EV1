package com.svalero.cybershopapp.domain;

import java.time.LocalDate;

public class Client {

    private String name;
    private String surname;
    private int number;
    private LocalDate register_date;
    private boolean vip;

    public Client() {
    }

    public Client(String name, String surname, int number, LocalDate register_date, boolean vip) {
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.register_date = register_date;
        this.vip = vip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public LocalDate getRegister_date() {
        return register_date;
    }

    public void setRegister_date(LocalDate register_date) {
        this.register_date = register_date;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", register_date=" + register_date +
                ", vip=" + vip +
                '}';
    }
}
