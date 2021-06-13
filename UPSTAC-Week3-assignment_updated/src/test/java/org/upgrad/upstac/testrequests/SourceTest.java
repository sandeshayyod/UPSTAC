package org.upgrad.upstac.testrequests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SourceTest {

    @Test
    public void getGrades(){
        Source gradingScheme = new Source();
        assertEquals('X',gradingScheme.getGrades(101));
        assertEquals('A',gradingScheme.getGrades(100));
        assertEquals('A',gradingScheme.getGrades(80));
        assertEquals('B',gradingScheme.getGrades(79));
        assertEquals('B',gradingScheme.getGrades(60));
        assertEquals('C',gradingScheme.getGrades(59));
        assertEquals('C',gradingScheme.getGrades(40));
        assertEquals('F',gradingScheme.getGrades(39));
        assertEquals('X',gradingScheme.getGrades(-1));
    }

}
