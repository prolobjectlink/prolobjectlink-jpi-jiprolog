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
import org.logicware.jpi.IndicatorError;
import org.logicware.jpi.jiprolog.JiPrologAtom;
import org.logicware.jpi.jiprolog.JiPrologDouble;
import org.logicware.jpi.jiprolog.JiPrologFloat;
import org.logicware.jpi.jiprolog.JiPrologInteger;
import org.logicware.jpi.jiprolog.JiPrologLong;
import org.logicware.jpi.jiprolog.JiPrologProvider;
import org.logicware.jpi.jiprolog.JiPrologVariable;

import com.ugos.jiprolog.engine.JIPNumber;

public class JiPrologFloatTest {

	private JiPrologFloat f;

	@Before
	public void setUp() throws Exception {
		f = new JiPrologFloat(3.14);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFloatAdapter() {
		assertNotNull(f.value);
		assertEquals(PrologTerm.FLOAT_TYPE, f.type);
		assertEquals(JIPNumber.create(3.14F), f.value);
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new JiPrologInteger[0], f.getArguments());
	}

	@Test
	public final void testGetPrologInteger() {
		assertEquals(new JiPrologInteger(3), f.getPrologInteger());
	}

	@Test
	public final void testGetPrologFloat() {
		assertEquals(new JiPrologFloat(3.14), f.getPrologFloat());
	}

	@Test
	public final void testGetPrologLong() {
		assertEquals(new JiPrologLong(3), f.getPrologLong());
	}

	@Test
	public final void testGetPrologDouble() {
		assertEquals(new JiPrologDouble(3.14F), f.getPrologDouble());
	}

	@Test
	public final void testGetLongValue() {
		assertEquals(3, f.getLongValue());
	}

	@Test
	public final void testGetDoubleValue() {
		assertEquals(3.14F, f.getDoubleValue(), 0);
	}

	@Test
	public final void testGetIntValue() {
		assertEquals(3, f.getIntValue());
	}

	@Test
	public final void testGetFloatValue() {
		assertEquals(3.140000104904175, f.getFloatValue(), 0);
	}

	@Test(expected = IndicatorError.class)
	public final void testGetKey() {
		f.getIndicator();
	}

	@Test
	public final void testGetType() {
		assertEquals(PrologTerm.FLOAT_TYPE, f.getType());
	}

	@Test
	public final void testIsAtom() {
		assertFalse(f.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertTrue(f.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertTrue(f.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(f.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(f.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(f.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(f.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(f.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(f.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(f.isEvaluable());
	}

	@Test(expected = ArityError.class)
	public final void testGetArity() {
		f.getArity();
	}

	@Test(expected = FunctorError.class)
	public final void testGetFunctor() {
		f.getFunctor();
	}

	@Test
	public final void testUntify() {

		// with atom
		PrologFloat fValue = new JiPrologFloat(36.47);
		PrologAtom atom = new JiPrologAtom("doe");
		assertFalse(fValue.unify(atom));

		// with integer
		PrologInteger iValue = new JiPrologInteger(28);
		assertFalse(fValue.unify(iValue));

		// with long
		PrologLong lValue = new JiPrologLong(28);
		assertFalse(fValue.unify(lValue));

		// with float
		PrologFloat fValue1 = new JiPrologFloat(100.98);
		// true because are equals
		assertTrue(fValue.unify(fValue));
		// false because are different
		assertFalse(fValue.unify(fValue1));

		// with double
		PrologDouble dValue = new JiPrologDouble(36.47);
		PrologDouble dValue1 = new JiPrologDouble(100.98);
		// true because are equals
		assertFalse(fValue.unify(dValue));
		// false because are different
		assertFalse(fValue.unify(dValue1));

		// with variable
		PrologVariable variable = new JiPrologVariable("X");
		// true. case float and variable
		assertTrue(fValue.unify(variable));

		// with predicate
		PrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertFalse(fValue.unify(structure));

		// with list
		PrologList flattenedList = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertFalse(fValue.unify(flattenedList));

		// with expression
		PrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertFalse(fValue.unify(expression));

	}

	@Test
	public final void testCompareTo() {

		// with atom
		PrologFloat fValue = new JiPrologFloat(36.47);
		PrologAtom atom = new JiPrologAtom("doe");
		assertEquals(fValue.compareTo(atom), -1);

		// with integer
		PrologInteger iValue = new JiPrologInteger(28);
		assertEquals(fValue.compareTo(iValue), 1);

		// with long
		PrologLong lValue = new JiPrologLong(28);
		assertEquals(fValue.compareTo(lValue), 1);

		// with float
		PrologFloat fValue1 = new JiPrologFloat(100.98);
		// true because are equals
		assertEquals(fValue.compareTo(fValue), 0);
		// false because are different
		assertEquals(fValue.compareTo(fValue1), -1);

		// with double
		PrologDouble dValue = new JiPrologDouble(36.47);
		PrologDouble dValue1 = new JiPrologDouble(100.98);
		// true because are equals
		assertEquals(fValue.compareTo(dValue), 0);
		// false because are different
		assertEquals(fValue.compareTo(dValue1), -1);

		// with variable
		PrologVariable variable = new JiPrologVariable("X");
		// true. case float and variable
		assertEquals(fValue.compareTo(variable), 1);

		// with predicate
		PrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(fValue.compareTo(structure), -1);

		// with list
		PrologList flattenedList = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertEquals(fValue.compareTo(flattenedList), -1);

		// with expression
		PrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertEquals(fValue.compareTo(expression), -1);

	}

}
