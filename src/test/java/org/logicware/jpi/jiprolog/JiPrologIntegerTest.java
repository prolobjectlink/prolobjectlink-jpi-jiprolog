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

public class JiPrologIntegerTest extends JiPrologBaseTest {

	private PrologInteger integer;

	@Before
	public void setUp() throws Exception {
		integer = provider.newInteger(100);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new PrologTerm[0], integer.getArguments());
	}

	@Test
	public final void testGetPrologInteger() {
		assertEquals(provider.newInteger(100), integer.getPrologInteger());
	}

	@Test
	public final void testGetPrologFloat() {
		assertEquals(provider.newFloat(100.0), integer.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(provider.newLong(100), integer.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(provider.newDouble(100.0), integer.getPrologDouble());
	}

	@Test
	public final void testGetLongValue() {
		assertEquals(100, integer.getLongValue());
	}

	@Test
	public final void testGetDoubleValue() {
		assertEquals(100.0, integer.getDoubleValue(), 0);
	}

	@Test
	public final void testGetIntValue() {
		assertEquals(100, integer.getIntValue());
	}

	@Test
	public final void testGetFloatValue() {
		assertEquals(100.0, integer.getFloatValue(), 0);
	}

	@Test(expected = IndicatorError.class)
	public final void testGetKey() {
		integer.getIndicator();
	}

	@Test
	public final void testGetType() {
		assertEquals(PrologTerm.INTEGER_TYPE, integer.getType());
	}

	@Test
	public final void testIsAtom() {
		assertFalse(integer.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertTrue(integer.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(integer.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertTrue(integer.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(integer.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(integer.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(integer.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(integer.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(integer.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(integer.isEvaluable());
	}

	@Test(expected = ArityError.class)
	public final void testGetArity() {
		integer.getArity();
	}

	@Test(expected = FunctorError.class)
	public final void testGetFunctor() {
		integer.getFunctor();
	}

	@Test
	public void testUnify() {

		// with atom
		PrologInteger iValue = provider.newInteger(28);
		PrologAtom atom = provider.newAtom("John Doe");
		assertFalse(iValue.unify(atom));

		// with integer
		PrologInteger iValue1 = provider.newInteger(36);
		// true because are equals
		assertTrue(iValue.unify(iValue));
		// false because they are different
		assertFalse(iValue.unify(iValue1));

		// with long
		PrologLong lValue = provider.newLong(28);
		PrologLong lValue1 = provider.newLong(100);
		// true because are equals
		assertTrue(iValue.unify(lValue));
		// false because they are different
		assertFalse(iValue.unify(lValue1));

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertFalse(iValue.unify(fValue));

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertFalse(iValue.unify(dValue));

		// with variable
		PrologVariable variable = provider.newVariable("X");
		// true. case atom and variable
		assertTrue(iValue.unify(variable));

		// with predicate
		PrologStructure structure = provider.parsePrologStructure("some_predicate(a,b,c)");
		assertFalse(iValue.unify(structure));

		// with list
		PrologList list = provider.parsePrologList("[a,b,c]");
		assertFalse(iValue.unify(list));

		// with expression
		PrologExpression expression = provider.parsePrologExpression("58+93*10");
		assertFalse(iValue.unify(expression));
	}

	@Test
	public void testCompareTo() {

		// with atom
		PrologInteger iValue = provider.newInteger(28);
		PrologAtom atom = provider.newAtom("John Doe");
		assertEquals(iValue.compareTo(atom), -1);

		// with integer
		PrologInteger iValue1 = provider.newInteger(36);
		// true because are equals
		assertEquals(iValue.compareTo(iValue), 0);
		// false because they are different
		assertEquals(iValue.compareTo(iValue1), -1);

		// with long
		PrologLong lValue = provider.newLong(28);
		PrologLong lValue1 = provider.newLong(100);
		// true because are equals
		assertEquals(iValue.compareTo(lValue), 0);
		// false because they are different
		assertEquals(iValue.compareTo(lValue1), -1);

		// with float
		PrologFloat fValue = provider.newFloat(36.47);
		assertEquals(iValue.compareTo(fValue), -1);

		// with double
		PrologDouble dValue = provider.newDouble(36.47);
		assertEquals(iValue.compareTo(dValue), -1);

		// with variable
		PrologVariable variable = provider.newVariable("X");
		// true. case atom and variable
		assertEquals(iValue.compareTo(variable), 1);

		// with predicate
		PrologStructure structure = provider.parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(iValue.compareTo(structure), -1);

		// with list
		PrologList list = provider.parsePrologList("[a,b,c]");
		assertEquals(iValue.compareTo(list), -1);

		// with expression
		PrologExpression expression = provider.parsePrologExpression("58+93*10");
		assertEquals(iValue.compareTo(expression), -1);
	}

}
