package com.chenjw.tool.beancopy.test.testpojo;

import com.chenjw.tool.beancopy.annotation.EnumParseMethod;
import com.chenjw.tool.beancopy.annotation.EnumValueMethod;

public class EnumPojo {

	private TestEnum testEnum;
	private TestIntEnum testIntEnum;
	private TestLongEnum testLongEnum;
	private TestStringEnum testStringEnum;

	public TestEnum getTestEnum() {
		return testEnum;
	}

	public void setTestEnum(TestEnum testEnum) {
		this.testEnum = testEnum;
	}

	public TestIntEnum getTestIntEnum() {
		return testIntEnum;
	}

	public void setTestIntEnum(TestIntEnum testIntEnum) {
		this.testIntEnum = testIntEnum;
	}

	public TestLongEnum getTestLongEnum() {
		return testLongEnum;
	}

	public void setTestLongEnum(TestLongEnum testLongEnum) {
		this.testLongEnum = testLongEnum;
	}

	public TestStringEnum getTestStringEnum() {
		return testStringEnum;
	}

	public void setTestStringEnum(TestStringEnum testStringEnum) {
		this.testStringEnum = testStringEnum;
	}

	public static enum TestEnum {
		VALUE1, VALUE2;
	}

	public static enum TestIntEnum {
		VALUE1(1), VALUE2(2);

		private int value;

		TestIntEnum(int value) {
			this.value = value;
		}

		@EnumValueMethod
		public int getValue() {
			return this.value;
		}

		@EnumParseMethod
		public static TestIntEnum parse(int n) {
			for (TestIntEnum testEnum2 : TestIntEnum.values()) {
				if (testEnum2.value == n) {
					return testEnum2;
				}
			}
			return null;
		}
	}

	public static enum TestLongEnum {
		VALUE1(1), VALUE2(2);

		private long value;

		TestLongEnum(long value) {
			this.value = value;
		}

		@EnumValueMethod
		public long getValue() {
			return this.value;
		}

		@EnumParseMethod
		public static TestLongEnum parse(long n) {
			for (TestLongEnum testEnum2 : TestLongEnum.values()) {
				if (testEnum2.value == n) {
					return testEnum2;
				}
			}
			return null;
		}
	}

	public static enum TestStringEnum {
		VALUE1("1"), VALUE2("2");

		private String value;

		TestStringEnum(String value) {
			this.value = value;
		}

		@EnumValueMethod
		public String getValue() {
			return this.value;
		}

		@EnumParseMethod
		public static TestStringEnum parse(String n) {
			for (TestStringEnum testEnum2 : TestStringEnum.values()) {
				if (testEnum2.value.equals(n)) {
					return testEnum2;
				}
			}
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(TestStringEnum.class.getSuperclass());
	}
}
