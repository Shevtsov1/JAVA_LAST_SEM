package com.vshevtsov.lab12.models;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Employee {
    @Id
    private String id;
    private String name;
    private String surname;
    private String patronymic;
    private String position;
    private double hourlyRate;

    public Employee() {
        this.id = generateShortId();
    }

    public Employee(String name, String surname, String patronymic, String position, double hourlyRate) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.position = position;
        this.hourlyRate = hourlyRate;
    }

    private String generateShortId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 6);
    }

    // Геттеры и сеттеры для всех полей

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
