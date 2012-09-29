package com.chenjw.tool.beancopy.test;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import com.chenjw.tool.beancopy.test.testpojo.EnumPojo.TestEnum;
import com.chenjw.tool.beancopy.test.testpojo.SimplePojo;
import com.chenjw.tools.BeanCopyUtils;
import com.chenjw.tools.beancopy.SmartBeanCopier;

public class BeanCopyUtilsPerformanceTest {

	private SmartBeanCopier copier1 = new SmartBeanCopier();

	public SimplePojo initOriginObject() {

		SimplePojo p1 = new SimplePojo();
		p1.setTestDate(new Date(11));
		p1.setTestDoubleArray(new double[] { 1.1D, 1.2D });
		p1.setTestEnum(TestEnum.VALUE2);
		p1.setTestLong(1L);
		p1.setTestString("ssss");
		p1.setTestString1("ssss1");
		p1.setTestString2("ssss2");
		p1.setTestString3("ssss3");
		p1.setTestStringArray(new String[] { "1", "2" });
		return p1;
	}

	private static long time1 = 0;

	private void test1(SimplePojo p1) {
		long start = System.nanoTime();
		SimplePojo p2 = new SimplePojo();
		copier1.copyProperties(p2, p1, null, Object.class);
		// BeanCopyUtils.copyProperties(p2, p1);
		time1 += System.nanoTime() - start;
	}

	public void testPerformance() throws IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {
		Class.forName(BeanCopyUtils.class.getName());

		SimplePojo p1 = initOriginObject();

		int times = 100000;
		test1(p1);

		//
		time1 = 0;

		for (int i = 0; i < times; i++) {
			test1(p1);

		}
		System.out.println("try " + times + " times, BeanCopyUtils used "
				+ time1 / 1000000 + "ms ");
		//

	}

	public static void main(String[] args) throws IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {
		BeanCopyUtilsPerformanceTest test = new BeanCopyUtilsPerformanceTest();
		test.testPerformance();
	}
}
