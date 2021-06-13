package org.upgrad.upstac.testrequests;

public class Source {

    public char getGrades(int marks){
        if (marks>100 || marks<0) {
            return 'X';
        } else if (marks>=80) {
            return 'A';
        } else if (marks>=60) {
            return 'B';
        } else if (marks>=40) {
            return 'C';
        } else {
            return 'F';
        }
    }

}
