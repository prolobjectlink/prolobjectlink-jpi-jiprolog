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

public class JiPrologIntegerTest {

	private JiPrologInteger integer;

	@Before
	public void setUp() throws Exception {
		integer = new JiPrologInteger(100);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testIntegerAdapter() {
		assertNotNull(integer.value);
		assertEquals(IPrologTerm.INTEGER_TYPE, integer.type);
		assertEquals(JIPNumber.create(100), integer.value);
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new JiPrologInteger[0], integer.getArguments());
	}

	@Test
	public final void testGetPrologInteger() {
		assertEquals(new JiPrologInteger(100), integer.getPrologInteger());
	}

	@Test
	public final void testGetPrologFloat() {
		assertEquals(new JiPrologFloat(100.0), integer.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(new JiPrologLong(100), integer.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(new JiPrologDouble(100.0), integer.getPrologDouble());
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
		assertEquals(IPrologTerm.INTEGER_TYPE, integer.getType());
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
		assertFalse(integer.isExpression());
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
		IPrologInteger iValue = new JiPrologInteger(28);
		IPrologAtom atom = new JiPrologAtom("John Doe");
		assertFalse(iValue.unify(atom));

		// with integer
		IPrologInteger iValue1 = new JiPrologInteger(36);
		// true because are equals
		assertTrue(iValue.unify(iValue));
		// false because they are different
		assertFalse(iValue.unify(iValue1));

		// with long
		IPrologLong lValue = new JiPrologLong(28);
		IPrologLong lValue1 = new JiPrologLong(100);
		// true because are equals
		assertTrue(iValue.unify(lValue));
		// false because they are different
		assertFalse(iValue.unify(lValue1));

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertFalse(iValue.unify(fValue));

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertFalse(iValue.unify(dValue));

		// with variable
		IPrologVariable variable = new JiPrologVariable("X");
		// true. case atom and variable
		assertTrue(iValue.unify(variable));

		// with predicate
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertFalse(iValue.unify(structure));

		// with list
		IPrologList list = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertFalse(iValue.unify(list));

		// with expression
		IPrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertFalse(iValue.unify(expression));
	}

	@Test
	public void testCompareTo() {

		// with atom
		IPrologInteger iValue = new JiPrologInteger(28);
		IPrologAtom atom = new JiPrologAtom("John Doe");
		assertEquals(iValue.compareTo(atom), -1);

		// with integer
		IPrologInteger iValue1 = new JiPrologInteger(36);
		// true because are equals
		assertEquals(iValue.compareTo(iValue), 0);
		// false because they are different
		assertEquals(iValue.compareTo(iValue1), -1);

		// with long
		IPrologLong lValue = new JiPrologLong(28);
		IPrologLong lValue1 = new JiPrologLong(100);
		// true because are equals
		assertEquals(iValue.compareTo(lValue), 0);
		// false because they are different
		assertEquals(iValue.compareTo(lValue1), -1);

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertEquals(iValue.compareTo(fValue), -1);

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertEquals(iValue.compareTo(dValue), -1);

		// with variable
		IPrologVariable variable = new JiPrologVariable("X");
		// true. case atom and variable
		assertEquals(iValue.compareTo(variable), 1);

		// with predicate
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(iValue.compareTo(structure), -1);

		// with list
		IPrologList list = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertEquals(iValue.compareTo(list), -1);

		// with expression
		IPrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertEquals(iValue.compareTo(expression), -1);
	}

}
