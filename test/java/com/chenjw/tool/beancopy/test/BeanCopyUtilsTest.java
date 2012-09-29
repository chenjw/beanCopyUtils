package com.chenjw.tool.beancopy.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

import com.chenjw.tool.beancopy.test.testpojo.ArrayPojo;
import com.chenjw.tool.beancopy.test.testpojo.BasicNumberPojo;
import com.chenjw.tool.beancopy.test.testpojo.DatePojo;
import com.chenjw.tool.beancopy.test.testpojo.EnumPojo;
import com.chenjw.tool.beancopy.test.testpojo.EnumPojo.TestEnum;
import com.chenjw.tool.beancopy.test.testpojo.EnumPojo.TestIntEnum;
import com.chenjw.tool.beancopy.test.testpojo.EnumPojo.TestLongEnum;
import com.chenjw.tool.beancopy.test.testpojo.EnumPojo.TestStringEnum;
import com.chenjw.tool.beancopy.test.testpojo.EnumValuePojo;
import com.chenjw.tool.beancopy.test.testpojo.NumberPojo;
import com.chenjw.tool.beancopy.test.testpojo.SimplePojo;
import com.chenjw.tool.beancopy.test.testpojo.StringNumberPojo;
import com.chenjw.tools.BeanCopyUtils;
import com.chenjw.tools.beancopy.util.DateUtils;

public class BeanCopyUtilsTest extends JTester {

	/**
	 * 基本类型到封装类新的转换
	 */
	@Test
	public void testBasicType() {
		// double->Double
		// float->Float
		// int->Integer
		// long->Long
		// short->Short
		// boolean->Boolean
		// byte->Byte
		// char->Character
		NumberPojo p1 = new NumberPojo();
		p1.setTestDouble(1.1D);
		p1.setTestFloat(1.2F);
		p1.setTestInt(1);
		p1.setTestLong(2L);
		p1.setTestShort((short) 3);
		p1.setTestBoolean(true);
		p1.setTestByte((byte) 5);
		p1.setTestChar((char) 6);
		BasicNumberPojo p2 = new BasicNumberPojo();
		// BeanCopier bc = BeanCopier.create(NumberPojo.class,
		// BasicNumberPojo.class, false);
		// bc.copy(p1, p2, null);
		BeanCopyUtils.copyProperties(p2, p1);
		want.object(p2.getTestDouble()).isEqualTo(1.1D);
		want.object(p2.getTestFloat()).isEqualTo(1.2F);
		want.object(p2.getTestInt()).isEqualTo(1);
		want.object(p2.getTestLong()).isEqualTo(2L);
		want.object(p2.getTestShort()).isEqualTo((short) 3);
		want.object(p2.getTestBoolean()).isEqualTo(true);
		want.object(p2.getTestByte()).isEqualTo((byte) 5);
		want.object(p2.getTestChar()).isEqualTo((char) 6);
	}

	/**
	 * 基本类型到字符窜的转换
	 */
	@Test
	public void testNumber2String() {
		// Double->String
		// Float->String
		// Integer->String
		// Long->String
		// Short->String
		// Boolean->String
		// Byte->String
		// Character->String
		BasicNumberPojo p1 = new BasicNumberPojo();
		p1.setTestDouble(1.1D);
		p1.setTestFloat(1.2F);
		p1.setTestInt(1);
		p1.setTestLong(2L);
		p1.setTestShort((short) 3);
		p1.setTestBoolean(true);
		p1.setTestByte((byte) 5);
		p1.setTestChar('a');
		StringNumberPojo p3 = new StringNumberPojo();
		BeanCopyUtils.copyProperties(p3, p1);
		want.object(p3.getTestDouble()).isEqualTo("1.1");
		want.object(p3.getTestFloat()).isEqualTo("1.2");
		want.object(p3.getTestInt()).isEqualTo("1");
		want.object(p3.getTestLong()).isEqualTo("2");
		want.object(p3.getTestShort()).isEqualTo("3");
		want.object(p3.getTestBoolean()).isEqualTo("true");
		want.object(p3.getTestByte()).isEqualTo("5");
		want.object(p3.getTestChar()).isEqualTo("a");
	}

	/**
	 * 字符串到基本类型的转换
	 */
	@Test
	public void testString2Number() {
		// String->Double
		// String->Float
		// String->Integer
		// String->Long
		// String->Short
		// String->Boolean
		// String->Byte
		// String->Character
		StringNumberPojo p3 = new StringNumberPojo();
		p3.setTestDouble("1.1");
		p3.setTestFloat("1.2");
		p3.setTestInt("1");
		p3.setTestLong("2");
		p3.setTestShort("3");
		p3.setTestBoolean("true");
		p3.setTestByte("5");
		p3.setTestChar("a");
		NumberPojo p2 = new NumberPojo();
		BeanCopyUtils.copyProperties(p2, p3);
		want.object(p2.getTestDouble()).isEqualTo(1.1D);
		want.object(p2.getTestFloat()).isEqualTo(1.2F);
		want.object(p2.getTestInt()).isEqualTo(1);
		want.object(p2.getTestLong()).isEqualTo(2L);
		want.object(p2.getTestShort()).isEqualTo((short) 3);

		want.object(p2.getTestBoolean()).isEqualTo(true);
		want.object(p2.getTestByte()).isEqualTo((byte) 5);
		want.object(p2.getTestChar()).isEqualTo('a');
	}

	/**
	 * 枚举到值的转换
	 */
	@Test
	public void testEnum2Value() {
		// Enum->String name()
		// Enum->int 自定义方法取值
		// Enum->long 自定义方法取值
		// Enum->String 自定义方法取值

		EnumPojo p1 = new EnumPojo();
		p1.setTestEnum(TestEnum.VALUE2);
		p1.setTestIntEnum(TestIntEnum.VALUE2);
		p1.setTestLongEnum(TestLongEnum.VALUE2);
		p1.setTestStringEnum(TestStringEnum.VALUE2);
		EnumValuePojo p2 = new EnumValuePojo();
		BeanCopyUtils.copyProperties(p2, p1);
		want.object(p2.getTestEnum()).isEqualTo("VALUE2");
		want.object(p2.getTestIntEnum()).isEqualTo(2);
		want.object(p2.getTestLongEnum()).isEqualTo(2L);
		want.object(p2.getTestStringEnum()).isEqualTo("2");
	}

	/**
	 * 值到枚举的转换
	 */
	@Test
	public void testValue2Enum() {
		// String->Enum valueOf
		// int->Enum 自定义解析方法
		// long->Enum 自定义解析方法
		// String->Enum 自定义解析方法
		EnumValuePojo p1 = new EnumValuePojo();
		p1.setTestEnum("VALUE2");
		p1.setTestIntEnum(2);
		p1.setTestLongEnum(2L);
		p1.setTestStringEnum("VALUE2");
		EnumPojo p2 = new EnumPojo();
		BeanCopyUtils.copyProperties(p2, p1);
		want.object(p2.getTestEnum()).isEqualTo(TestEnum.VALUE2);
		want.object(p2.getTestIntEnum()).isEqualTo(TestIntEnum.VALUE2);
		want.object(p2.getTestLongEnum()).isEqualTo(TestLongEnum.VALUE2);
		want.object(p2.getTestStringEnum()).isEqualTo(TestStringEnum.VALUE2);
	}

	/**
	 * 数组到数组的转换
	 */
	@Test
	public void testArray2Array() {
		// double[]->String[]
		// String[]->int[]
		// Object[]->double[] 测试失败时的处理方式
		// String[]->Object[]
		ArrayPojo p1 = new ArrayPojo();
		p1.setTestDoubleArray(new double[] { 0.1D, 0.2D });
		p1.setTestIntArray(new int[] { 1, 2 });
		p1.setTestLongArray(new long[] { 2, 3 });
		p1.setTestString2Array(new String[] { "1", "2" });
		p1.setTestObjectArray(new Object[] { ArrayPojo.class });
		p1.setTestStringArray(new String[] { "3", "4" });
		ArrayPojo p2 = new ArrayPojo();
		Map<String, String> fMap = new HashMap<String, String>();
		fMap.put("testDoubleArray", "testStringArray");
		fMap.put("testStringArray", "testIntArray");
		fMap.put("testObjectArray", "testDoubleArray");
		fMap.put("testString2Array", "testObjectArray");
		BeanCopyUtils.copyProperties(p2, p1, fMap);

		want.object(p2.getTestStringArray()[1]).isEqualTo("0.2");
		want.object(p2.getTestIntArray()[1]).isEqualTo(4);
		want.object(p2.getTestDoubleArray()).isNull();
		want.object(p2.getTestObjectArray()[1]).isEqualTo("2");

	}

	/**
	 * 日期类型到其他类型的转换
	 */
	@Test
	public void testDate2Other() {
		// date->long
		// timestamp->Long
		// date->String
		// timestamp->String
		// date->timestamp
		// timestamp->date
		DatePojo p1 = new DatePojo();
		p1.setTestDate(new Date(1));
		p1.setTestTimestamp(new Timestamp(1));
		p1.setTestDate2(new Date(2));
		p1.setTestTimestamp2(new Timestamp(3));
		p1.setTestDate3(new Date(4));
		p1.setTestTimestamp3(new Timestamp(5));
		DatePojo p2 = new DatePojo();
		Map<String, String> fMap = new HashMap<String, String>();
		fMap.put("testDate", "testLong");
		fMap.put("testTimestamp", "testLong2");
		fMap.put("testDate2", "testString");
		fMap.put("testTimestamp2", "testString2");
		fMap.put("testDate3", "testTimestamp3");
		fMap.put("testTimestamp3", "testDate3");
		BeanCopyUtils.copyProperties(p2, p1, fMap);
		want.object(p2.getTestLong()).isEqualTo(new Date(1).getTime());
		want.object(p2.getTestLong2()).isEqualTo(new Timestamp(1).getTime());
		want.object(p2.getTestString()).isEqualTo("1970-01-01 08:00:00");

		want.object(p2.getTestString2()).isEqualTo("1970-01-01 08:00:00");
		want.object(p2.getTestTimestamp3()).isEqualTo(new Timestamp(4));
		want.object(p2.getTestDate3()).isEqualTo(new Date(5));
	}

	/**
	 * 其他类型到日期类型的转换
	 */
	@Test
	public void testOther2Date() {
		// long->date
		// Long->timestamp
		// String->date "yyyy-MM-dd HH:mm:ss"
		// String->date "yyyy-MM-dd"
		// String->timestamp "yyyy-MM-dd HH:mm:ss"
		DatePojo p1 = new DatePojo();
		p1.setTestLong(1L);
		p1.setTestLong2(2L);
		p1.setTestString("2001-01-01 08:00:00");
		p1.setTestString2("2001-01-02");
		p1.setTestString3("2001-01-01 08:00:00");
		DatePojo p2 = new DatePojo();
		Map<String, String> fMap = new HashMap<String, String>();
		fMap.put("testLong", "testDate");
		fMap.put("testLong2", "testTimestamp");
		fMap.put("testString", "testDate2");
		fMap.put("testString2", "testDate3");
		fMap.put("testString3", "testTimestamp2");
		BeanCopyUtils.copyProperties(p2, p1, fMap);
		want.object(p2.getTestDate()).isEqualTo(new Date(1));
		want.object(p2.getTestTimestamp()).isEqualTo(new Timestamp(2));
		want.object(p2.getTestDate2()).isEqualTo(
				DateUtils.parseDate("2001-01-01 08:00:00",
						"yyyy-MM-dd HH:mm:ss"));
		want.object(p2.getTestDate3()).isEqualTo(
				DateUtils.parseDate("2001-01-02", "yyyy-MM-dd"));
		want.object(p2.getTestTimestamp2()).isEqualTo(
				new Timestamp(DateUtils.parseDate("2001-01-01 08:00:00",
						"yyyy-MM-dd HH:mm:ss").getTime()));

	}

	@Test
	public void testNumber2Number() {
		// short->int
		// double->long
		// float->double
		// long->float
		// byte->short
		// int->byte
		NumberPojo p1 = new NumberPojo();
		p1.setTestShort((short) 1);
		p1.setTestDouble(2.1D);
		p1.setTestFloat(3.1F);
		p1.setTestLong(4L);
		p1.setTestByte((byte) 5);
		p1.setTestInt(6);
		BasicNumberPojo p2 = new BasicNumberPojo();
		Map<String, String> fMap = new HashMap<String, String>();
		fMap.put("testShort", "testInt");
		fMap.put("testDouble", "testLong");
		fMap.put("testFloat", "testDouble");
		fMap.put("testLong", "testFloat");
		fMap.put("testByte", "testShort");
		fMap.put("testInt", "testByte");
		BeanCopyUtils.copyProperties(p2, p1, fMap);
		want.object(p2.getTestInt()).isEqualTo(1);
		want.object(p2.getTestLong()).isEqualTo(2L);
		want.object(p2.getTestDouble()).isEqualTo(3.1D);
		want.object(p2.getTestFloat()).isEqualTo(4F);
		want.object(p2.getTestShort()).isEqualTo((short) 5);
		want.object(p2.getTestByte()).isEqualTo((byte) 6);
	}

	@Test
	public void testPojo2ObjectMap() {
		SimplePojo p1 = new SimplePojo();
		p1.setTestDate(new Date(11));
		p1.setTestDoubleArray(new double[] { 1.1D, 1.2D });
		p1.setTestEnum(TestEnum.VALUE2);
		p1.setTestLong(1L);
		p1.setTestString("ssss");
		p1.setTestStringArray(new String[] { "1", "2" });
		Map<String, Object> m1 = new HashMap<String, Object>();
		BeanCopyUtils.copyProperties(m1, p1);
		want.object(m1.get("testDate")).isEqualTo(new Date(11));
		want.object(m1.get("testDoubleArray")).isEqualTo(
				new double[] { 1.1D, 1.2D });
		want.object(m1.get("testEnum")).isEqualTo(TestEnum.VALUE2);
		want.object(m1.get("testLong")).isEqualTo(1L);
		want.object(m1.get("testString")).isEqualTo("ssss");
		want.object(m1.get("testStringArray")).isEqualTo(
				new String[] { "1", "2" });
	}

	@Test
	public void testPojo2StringMap() {
		SimplePojo p1 = new SimplePojo();
		p1.setTestDate(new Date(11));
		p1.setTestDoubleArray(new double[] { 1.1D, 1.2D });
		p1.setTestEnum(TestEnum.VALUE2);
		p1.setTestLong(1L);
		p1.setTestString("ssss");
		p1.setTestStringArray(new String[] { "1", "2" });
		Map<String, String> nameMap = new HashMap<String, String>();
		nameMap.put("testString", "testLong");
		nameMap.put("testLong", "testString");
		Map<String, Object> m1 = new HashMap<String, Object>();
		BeanCopyUtils.copyProperties(m1, p1, nameMap, String.class);
		want.object(m1.get("testDate")).isEqualTo("1970-01-01 08:00:00");
		// want.object(m1.get("testDoubleArray")).isEqualTo(new double[] { 1.1D,
		// 1.2D });
		want.object(m1.get("testEnum")).isEqualTo("VALUE2");
		want.object(m1.get("testLong")).isEqualTo("ssss");
		want.object(m1.get("testString")).isEqualTo("1");
		// want.object(m1.get("testStringArray")).isEqualTo(new String[] { "1",
		// "2" });

	}

	@Test
	public void testObjectMap2Pojo() {
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("testLong", new Date(11));
		m1.put("testString", new double[] { 1.1D, 1.2D });
		m1.put("testStringArray", TestEnum.VALUE2);
		m1.put("testDate", 1L);
		m1.put("testDoubleArray", "ssss");
		m1.put("testEnum", new String[] { "1", "2" });

		SimplePojo p1 = new SimplePojo();
		Map<String, String> nameMap = new HashMap<String, String>();
		nameMap.put("testLong", "testDate");
		nameMap.put("testString", "testDoubleArray");
		nameMap.put("testStringArray", "testEnum");
		nameMap.put("testDate", "testLong");
		nameMap.put("testDoubleArray", "testString");
		nameMap.put("testEnum", "testStringArray");
		BeanCopyUtils.copyProperties(p1, m1, nameMap);
		want.object(p1.getTestDate()).isEqualTo(new Date(11));
		want.object(p1.getTestDoubleArray()).isEqualTo(
				new double[] { 1.1D, 1.2D });
		want.object(p1.getTestEnum()).isEqualTo(TestEnum.VALUE2);
		want.object(p1.getTestLong()).isEqualTo(1L);
		want.object(p1.getTestString()).isEqualTo("ssss");
		want.object(p1.getTestStringArray()).isEqualTo(
				new String[] { "1", "2" });

	}

	@Test
	public void testObjectMap2BasicPojo() {
		Map<String, Object> m1 = new HashMap<String, Object>();
		m1.put("testInt", 1L);
		m1.put("testLong", 2F);
		m1.put("testFloat", 3D);
		m1.put("testDouble", (short) 4);
		m1.put("testShort", true);
		m1.put("testBoolean", 6);
		m1.put("testByte", '7');
		m1.put("testChar", 8);

		BasicNumberPojo p1 = new BasicNumberPojo();
		Map<String, String> nameMap = new HashMap<String, String>();
		nameMap.put("testInt", "testLong");
		nameMap.put("testLong", "testFloat");
		nameMap.put("testFloat", "testDouble");
		nameMap.put("testDouble", "testShort");
		nameMap.put("testShort", "testBoolean");
		nameMap.put("testBoolean", "testByte");
		nameMap.put("testByte", "testChar");
		nameMap.put("testChar", "testInt");
		BeanCopyUtils.copyProperties(p1, m1, nameMap);
		want.object(p1.getTestInt()).isEqualTo(8);
		want.object(p1.getTestLong()).isEqualTo(1L);
		want.object(p1.getTestFloat()).isEqualTo(2F);
		want.object(p1.getTestDouble()).isEqualTo(3D);
		want.object(p1.getTestShort()).isEqualTo((short) 4);
		want.object(p1.getTestBoolean()).isEqualTo(true);
		want.object(p1.getTestByte()).isEqualTo((byte) 6);
		want.object(p1.getTestChar()).isEqualTo('7');

	}
}
