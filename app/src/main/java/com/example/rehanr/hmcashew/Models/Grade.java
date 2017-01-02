package com.example.rehanr.hmcashew.Models;

/**
 * Created by rehan r on 03-01-2017.
 */
public class Grade {

    int grageNumber;
    String gradeName,gradeType,gradeActive;

    public Grade(int grageNumber, String gradeName, String gradeType, String gradeActive) {
        this.grageNumber = grageNumber;
        this.gradeName = gradeName;
        this.gradeType = gradeType;
        this.gradeActive = gradeActive;
    }

    public int getGrageNumber() {
        return grageNumber;
    }

    public void setGrageNumber(int grageNumber) {
        this.grageNumber = grageNumber;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String getGradeActive() {
        return gradeActive;
    }

    public void setGradeActive(String gradeActive) {
        this.gradeActive = gradeActive;
    }
}
