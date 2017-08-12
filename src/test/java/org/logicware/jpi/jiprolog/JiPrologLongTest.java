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

import com.ugos.jiprolog.engine.JIPNumber;

public class JiPrologLongTest extends JiPrologBaseTest {

	private PrologLong long1 = provider.newPrologLong(100);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
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
		assertEquals(provider.newPrologInteger(100), long1.getPrologInteger());
	}

	@Test
	public void testGetPrologFloat() {
		assertEquals(provider.newPrologFloat(100.0), long1.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(provider.newPrologLong(100), long1.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(provider.newPrologDouble(100.0), long1.getPrologDouble());
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
		assertTrue(long1.equals(provider.newPrologLong(100)));
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
		assertArrayEquals(new PrologTerm[0], long1.getArguments());
	}

	@Test
	public void testGetType() {
		assertEquals(PrologTerm.LONG_TYPE, long1.getType());
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
		assertFalse(long1.isEvaluable());
	}

	@Test(expected = IndicatorError.class)
	public void testGetKey() {
		long1.getIndicator();
	}

	@Test
	public void testUnify() {

		// with atom
		PrologLong lValue = provider.newPrologLong(28);
		PrologAtom atom = provider.newPrologAtom("John Doe");
		assertFalse(lValue.unify(atom));

		// with integer
		PrologInteger iValue = provider.newPrologInteger(36);
		// false because they are different
		assertFalse(lValue.unify(iValue));

		// with long
		PrologLong lValue1 = provider.newPrologLong(36);
		// true because are equals
		assertTrue(lValue.unify(lValue));
		// false because they are different
		assertFalse(lValue.unify(lValue1));

		// with float
		PrologFloat fValue = provider.newPrologFloat(36.47);
		assertFalse(lValue.unify(fValue));

		// with double
		PrologDouble dValue = provider.newPrologDouble(36.47);
		assertFalse(lValue.unify(dValue));

		// with variable
		PrologVariable variable = provider.newPrologVariable("X");
		// true. case atom and variable
		assertTrue(lValue.unify(variable));

		// with predicate
		PrologStructure structure = provider.parsePrologStructure("some_predicate(a,b,c)");
		assertFalse(lValue.unify(structure));

		// with list
		PrologList list = provider.parsePrologList("[a,b,c]");
		assertFalse(lValue.unify(list));

		// with expression
		PrologExpression expression = provider.parsePrologExpression("58+93*10");
		assertFalse(lValue.unify(expression));
	}

	@Test
	public void testCompareTo() {

		// with atom
		PrologLong lValue = provider.newPrologLong(28);
		PrologAtom atom = provider.newPrologAtom("John Doe");
		assertEquals(lValue.compareTo(atom), -1);

		// with integer
		PrologInteger iValue = provider.newPrologInteger(36);
		// false because they are different
		assertEquals(lValue.compareTo(iValue), -1);

		// with long
		PrologLong lValue1 = provider.newPrologLong(36);
		// true because are equals
		assertEquals(lValue.compareTo(lValue), 0);
		// false because they are different
		assertEquals(lValue.compareTo(lValue1), -1);

		// with float
		PrologFloat fValue = provider.newPrologFloat(36.47);
		assertEquals(lValue.compareTo(fValue), -1);

		// with double
		PrologDouble dValue = provider.newPrologDouble(36.47);
		assertEquals(lValue.compareTo(dValue), -1);

		// with variable
		PrologVariable variable = provider.newPrologVariable("X");
		// true. case atom and variable
		assertEquals(lValue.compareTo(variable), 1);

		// with predicate
		PrologStructure structure = provider.parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(lValue.compareTo(structure), -1);

		// with list
		PrologList list = provider.parsePrologList("[a,b,c]");
		assertEquals(lValue.compareTo(list), -1);

		// with expression
		PrologExpression expression = provider.parsePrologExpression("58+93*10");
		assertEquals(lValue.compareTo(expression), -1);
	}

}
