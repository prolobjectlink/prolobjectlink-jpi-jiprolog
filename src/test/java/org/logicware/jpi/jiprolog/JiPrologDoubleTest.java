package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.jpi.ArityError;
import org.logicware.jpi.FunctorError;
import org.logicware.jpi.IndicatorError;
import org.logicware.jpi.JiPrologBaseTest;
import org.logicware.jpi.PrologAtom;
import org.logicware.jpi.PrologDouble;
import org.logicware.jpi.PrologExpression;
import org.logicware.jpi.PrologFloat;
import org.logicware.jpi.PrologInteger;
import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologLong;
import org.logicware.jpi.PrologStructure;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.PrologVariable;

public class JiPrologDoubleTest extends JiPrologBaseTest {

	private PrologDouble double1 = provider.newPrologDouble(1.6180339887);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetPrologInteger() {
		assertEquals(provider.newPrologInteger(1), double1.getPrologInteger());
	}

	@Test
	public final void testGetPrologFloat() {
		assertEquals(provider.newPrologFloat(1.618034), double1.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(provider.newPrologLong(1), double1.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(provider.newPrologDouble(1.6180339887), double1.getPrologDouble());
	}

	@Test
	public final void testGetLongValue() {
		assertEquals(1, double1.getLongValue());
	}

	@Test
	public final void testGetDoubleValue() {
		assertEquals(1.6180339887, double1.getDoubleValue(), 0);
	}

	@Test
	public final void testGetIntValue() {
		assertEquals(1, double1.getIntValue());
	}

	@Test
	public final void testGetFloatValue() {
		assertEquals(1.618034F, double1.getFloatValue(), 0);
	}

	@Test
	public void testEqualsObject() {
		assertTrue(double1.equals(provider.newPrologDouble(1.6180339887)));
	}

	@Test
	public void testToString() {
		assertEquals("1.6180339887", double1.toString());
	}

	@Test(expected = ArityError.class)
	public final void testGetArity() {
		double1.getArity();
	}

	@Test(expected = FunctorError.class)
	public final void testGetFunctor() {
		double1.getFunctor();
	}

	@Test
	public void testGetArguments() {
		assertArrayEquals(new PrologTerm[0], double1.getArguments());
	}

	@Test
	public void testGetType() {
		assertEquals(PrologTerm.DOUBLE_TYPE, double1.getType());
	}

	@Test
	public void testIsAtom() {
		assertFalse(double1.isAtom());
	}

	@Test
	public void testIsNumber() {
		assertTrue(double1.isNumber());
	}

	@Test
	public void testIsFloat() {
		assertFalse(double1.isFloat());
	}

	@Test
	public void testIsInteger() {
		assertFalse(double1.isInteger());
	}

	@Test
	public void testIsDouble() {
		assertTrue(double1.isDouble());
	}

	@Test
	public void testIsLong() {
		assertFalse(double1.isLong());
	}

	@Test
	public void testIsVariable() {
		assertFalse(double1.isVariable());
	}

	@Test
	public void testIsList() {
		assertFalse(double1.isList());
	}

	@Test
	public void testIsStructure() {
		assertFalse(double1.isStructure());
	}

	@Test
	public void testIsNil() {
		assertFalse(double1.isNil());
	}

	@Test
	public void testIsEmptyList() {
		assertFalse(double1.isEmptyList());
	}

	@Test
	public void testIsExpression() {
		assertFalse(double1.isEvaluable());
	}

	@Test(expected = IndicatorError.class)
	public void testGetKey() {
		double1.getIndicator();
	}

	@Test
	public void testUntify() {

		// with atom
		PrologDouble dValue = provider.newPrologDouble(36.47);
		PrologAtom atom = provider.newPrologAtom("doe");
		assertFalse(dValue.unify(atom));

		// with integer
		PrologInteger iValue = provider.newPrologInteger(28);
		assertFalse(dValue.unify(iValue));

		// with long
		PrologLong lValue = provider.newPrologLong(28);
		assertFalse(dValue.unify(lValue));

		// with float
		PrologFloat fValue1 = provider.newPrologFloat(100.98);
		// false because are different
		assertFalse(dValue.unify(fValue1));

		// with float
		PrologDouble dValue1 = provider.newPrologDouble(100.98);
		// true because are equals
		assertTrue(dValue.unify(dValue));
		// false because are different
		assertFalse(dValue.unify(dValue1));

		// with variable
		PrologVariable variable = provider.newPrologVariable("X");
		// true. case float and variable
		assertTrue(dValue.unify(variable));

		// with predicate
		PrologStructure structure = provider.parsePrologStructure("some_predicate(a,b,c)");
		assertFalse(dValue.unify(structure));

		// with list
		PrologList flattenedList = provider.parsePrologList("[a,b,c]");
		assertFalse(dValue.unify(flattenedList));

		// with expression
		PrologExpression expression = provider.parsePrologExpression("58+93*10");
		assertFalse(dValue.unify(expression));

	}

	@Test
	public void testCompareTo() {

		// with atom
		PrologDouble dValue = provider.newPrologDouble(36.47);
		PrologAtom atom = provider.newPrologAtom("doe");
		assertEquals(dValue.compareTo(atom), -1);

		// with integer
		PrologInteger iValue = provider.newPrologInteger(28);
		assertEquals(dValue.compareTo(iValue), 1);

		// with long
		PrologLong lValue = provider.newPrologLong(28);
		assertEquals(dValue.compareTo(lValue), 1);

		// with float
		PrologFloat fValue1 = provider.newPrologFloat(100.98);
		// false because are different
		assertEquals(dValue.compareTo(fValue1), -1);

		// with float
		PrologDouble dValue1 = provider.newPrologDouble(100.98);
		// true because are equals
		assertEquals(dValue.compareTo(dValue), 0);
		// false because are different
		assertEquals(dValue.compareTo(dValue1), -1);

		// with variable
		PrologVariable variable = provider.newPrologVariable("X");
		// true. case float and variable
		assertEquals(dValue.compareTo(variable), 1);

		// with predicate
		PrologStructure structure = provider.parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(dValue.compareTo(structure), -1);

		// with list
		PrologList flattenedList = provider.parsePrologList("[a,b,c]");
		assertEquals(dValue.compareTo(flattenedList), -1);

		// with expression
		PrologExpression expression = provider.parsePrologExpression("58+93*10");
		assertEquals(dValue.compareTo(expression), -1);

	}

}
