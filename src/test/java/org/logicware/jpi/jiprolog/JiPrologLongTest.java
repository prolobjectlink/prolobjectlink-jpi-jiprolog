package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.jpi.ArityError;
import org.logicware.jpi.FunctorError;
import org.logicware.jpi.IPrologAtom;
import org.logicware.jpi.IPrologDouble;
import org.logicware.jpi.IPrologExpression;
import org.logicware.jpi.IPrologFloat;
import org.logicware.jpi.IPrologInteger;
import org.logicware.jpi.IPrologList;
import org.logicware.jpi.IPrologLong;
import org.logicware.jpi.IPrologStructure;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.IPrologVariable;
import org.logicware.jpi.IndicatorError;
import org.logicware.jpi.jiprolog.JiPrologAtom;
import org.logicware.jpi.jiprolog.JiPrologDouble;
import org.logicware.jpi.jiprolog.JiPrologFloat;
import org.logicware.jpi.jiprolog.JiPrologInteger;
import org.logicware.jpi.jiprolog.JiPrologLong;
import org.logicware.jpi.jiprolog.JiPrologProvider;
import org.logicware.jpi.jiprolog.JiPrologVariable;

import com.ugos.jiprolog.engine.JIPNumber;

public class JiPrologLongTest {

	private JiPrologLong long1 = new JiPrologLong(100);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testLongAdapter() {
		assertNotNull(long1.value);
		assertEquals(IPrologTerm.LONG_TYPE, long1.type);
		assertEquals(JIPNumber.create(100), long1.value);
	}

	@Test
	public void testGetLongValue() {
		assertEquals(100, long1.getLongValue());
	}

	@Test
	public void testGetDoubleValue() {
		assertEquals(100.0, long1.getDoubleValue(), 0);
	}

	@Test
	public void testGetPrologInteger() {
		assertEquals(new JiPrologInteger(100), long1.getPrologInteger());
	}

	@Test
	public void testGetPrologFloat() {
		assertEquals(new JiPrologFloat(100.0), long1.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(new JiPrologLong(100), long1.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(new JiPrologDouble(100.0), long1.getPrologDouble());
	}

	@Test
	public void testPrologInteger() {
		JIPNumber integer = JIPNumber.create(0);
		assertEquals(0, (int) integer.getDoubleValue());
	}

	@Test
	public void testPrologIntegerLong() {
		assertEquals(100, (long) JIPNumber.create(100).getDoubleValue());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(long1.equals(new JiPrologLong(100)));
	}

	@Test
	public void testToString() {
		assertEquals("100", long1.toString());
	}

	@Test(expected = ArityError.class)
	public final void testGetArity() {
		long1.getArity();
	}

	@Test(expected = FunctorError.class)
	public final void testGetFunctor() {
		long1.getFunctor();
	}

	@Test
	public void testGetArguments() {
		assertArrayEquals(new IPrologTerm[0], long1.getArguments());
	}

	@Test
	public void testGetType() {
		assertEquals(IPrologTerm.LONG_TYPE, long1.getType());
	}

	@Test
	public void testIsAtom() {
		assertFalse(long1.isAtom());
	}

	@Test
	public void testIsNumber() {
		assertTrue(long1.isNumber());
	}

	@Test
	public void testIsFloat() {
		assertFalse(long1.isFloat());
	}

	@Test
	public void testIsInteger() {
		assertFalse(long1.isInteger());
	}

	@Test
	public final void testIsDouble() {
		assertFalse(long1.isDouble());
	}

	@Test
	public final void testIsLong() {
		assertTrue(long1.isLong());
	}

	@Test
	public void testIsVariable() {
		assertFalse(long1.isVariable());
	}

	@Test
	public void testIsList() {
		assertFalse(long1.isList());
	}

	@Test
	public void testIsStructure() {
		assertFalse(long1.isStructure());
	}

	@Test
	public void testIsNil() {
		assertFalse(long1.isNil());
	}

	@Test
	public void testIsEmptyList() {
		assertFalse(long1.isEmptyList());
	}

	@Test
	public void testIsExpression() {
		assertFalse(long1.isExpression());
	}

	@Test(expected = IndicatorError.class)
	public void testGetKey() {
		long1.getIndicator();
	}

	@Test
	public void testUnify() {

		// with atom
		IPrologLong lValue = new JiPrologLong(28);
		IPrologAtom atom = new JiPrologAtom("John Doe");
		assertFalse(lValue.unify(atom));

		// with integer
		IPrologInteger iValue = new JiPrologInteger(36);
		// false because they are different
		assertFalse(lValue.unify(iValue));

		// with long
		IPrologLong lValue1 = new JiPrologLong(36);
		// true because are equals
		assertTrue(lValue.unify(lValue));
		// false because they are different
		assertFalse(lValue.unify(lValue1));

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertFalse(lValue.unify(fValue));

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertFalse(lValue.unify(dValue));

		// with variable
		IPrologVariable variable = new JiPrologVariable("X");
		// true. case atom and variable
		assertTrue(lValue.unify(variable));

		// with predicate
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertFalse(lValue.unify(structure));

		// with list
		IPrologList list = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertFalse(lValue.unify(list));

		// with expression
		IPrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertFalse(lValue.unify(expression));
	}

	@Test
	public void testCompareTo() {

		// with atom
		IPrologLong lValue = new JiPrologLong(28);
		IPrologAtom atom = new JiPrologAtom("John Doe");
		assertEquals(lValue.compareTo(atom), -1);

		// with integer
		IPrologInteger iValue = new JiPrologInteger(36);
		// false because they are different
		assertEquals(lValue.compareTo(iValue), -1);

		// with long
		IPrologLong lValue1 = new JiPrologLong(36);
		// true because are equals
		assertEquals(lValue.compareTo(lValue), 0);
		// false because they are different
		assertEquals(lValue.compareTo(lValue1), -1);

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertEquals(lValue.compareTo(fValue), -1);

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertEquals(lValue.compareTo(dValue), -1);

		// with variable
		IPrologVariable variable = new JiPrologVariable("X");
		// true. case atom and variable
		assertEquals(lValue.compareTo(variable), 1);

		// with predicate
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(lValue.compareTo(structure), -1);

		// with list
		IPrologList list = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertEquals(lValue.compareTo(list), -1);

		// with expression
		IPrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertEquals(lValue.compareTo(expression), -1);
	}

}
