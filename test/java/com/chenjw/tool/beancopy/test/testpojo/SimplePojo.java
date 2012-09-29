package com.chenjw.tool.beancopy.test.testpojo;

import java.util.Date;

import com.chenjw.tool.beancopy.test.testpojo.EnumPojo.TestEnum;

public class SimplePojo {

    private String[] testStringArray;
    private Long     testLong;
    private String   testString;
    private String   testString1;
    private String   testString2;
    private String   testString3;
    private double[] testDoubleArray;
    private TestEnum testEnum;
    private Date     testDate;

    public String[] getTestStringArray() {
        return testStringArray;
    }

    public void setTestStringArray(String[] testStringArray) {
        this.testStringArray = testStringArray;
    }

    public Long getTestLong() {
        return testLong;
    }

    public void setTestLong(Long testLong) {
        this.testLong = testLong;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public String getTestString1() {
        return testString1;
    }

    public void setTestString1(String testString1) {
        this.testString1 = testString1;
    }

    public String getTestString2() {
        return testString2;
    }

    public void setTestString2(String testString2) {
        this.testString2 = testString2;
    }

    public String getTestString3() {
        return testString3;
    }

    public void setTestString3(String testString3) {
        this.testString3 = testString3;
    }

    public double[] getTestDoubleArray() {
        return testDoubleArray;
    }

    public void setTestDoubleArray(double[] testDoubleArray) {
        this.testDoubleArray = testDoubleArray;
    }

    public TestEnum getTestEnum() {
        return testEnum;
    }

    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

}
