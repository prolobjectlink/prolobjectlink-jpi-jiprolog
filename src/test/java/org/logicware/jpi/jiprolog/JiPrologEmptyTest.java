package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
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
import org.logicware.jpi.jiprolog.JiPrologAtom;
import org.logicware.jpi.jiprolog.JiPrologDouble;
import org.logicware.jpi.jiprolog.JiPrologEmpty;
import org.logicware.jpi.jiprolog.JiPrologFloat;
import org.logicware.jpi.jiprolog.JiPrologInteger;
import org.logicware.jpi.jiprolog.JiPrologLong;
import org.logicware.jpi.jiprolog.JiPrologProvider;
import org.logicware.jpi.jiprolog.JiPrologVariable;

public class JiPrologEmptyTest {

	private JiPrologEmpty empty = new JiPrologEmpty();

	@Test
	public final void testGetKey() {
		assertEquals("[]/0", empty.getIndicator());
	}

	@Test
	public final void testGetArity() {
		assertEquals(0, empty.getArity());
	}

	@Test
	public final void testGetFunctor() {
		assertEquals("[]", empty.getFunctor());
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new IPrologTerm[0], empty.getArguments());
	}

	@Test
	public final void testToString() {
		assertEquals("[]", empty.toString());
	}

	@Test
	public final void testHashCode() {
		assertEquals(new JiPrologEmpty().hashCode(), empty.hashCode());
	}

	@Test
	public final void testGetType() {
		assertEquals(IPrologTerm.EMPTY_TYPE, empty.getType());
	}

	@Test
	public final void testIsAtom() {
		assertFalse(empty.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertFalse(empty.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(empty.isFloat());
	}

	@Test
	public final void testIsDouble() {
		assertFalse(empty.isDouble());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(empty.isInteger());
	}

	@Test
	public final void testIsLong() {
		assertFalse(empty.isLong());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(empty.isVariable());
	}

	@Test
	public final void testIsList() {
		assertTrue(empty.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(empty.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(empty.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertTrue(empty.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(empty.isExpression());
	}

	@Test
	public final void testUnify() {

		// with atom
		IPrologTerm empty = new JiPrologEmpty();
		IPrologAtom atom = new JiPrologAtom("John Doe");
		assertFalse(empty.unify(atom));

		// with integer
		IPrologInteger iValue = new JiPrologInteger(36);
		assertFalse(empty.unify(iValue));

		// with long
		IPrologLong lValue = new JiPrologLong(28);
		assertFalse(empty.unify(lValue));

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertFalse(empty.unify(fValue));

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertFalse(empty.unify(dValue));

		// with variable
		IPrologVariable variable = new JiPrologVariable("X");
		// true. case [] and variable
		assertTrue(empty.unify(variable));

		// with predicate
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertFalse(empty.unify(structure));

		// with list
		IPrologList list = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertFalse(empty.unify(list));
		assertTrue(empty.unify(empty));

		// with expression
		IPrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertFalse(empty.unify(expression));

	}

	@Test
	public final void testCompareTo() {

		// with atom
		IPrologTerm empty = new JiPrologEmpty();
		IPrologAtom atom = new JiPrologAtom("John Doe");
		assertEquals(empty.compareTo(atom), 1);

		// with integer
		IPrologInteger iValue = new JiPrologInteger(36);
		assertEquals(empty.compareTo(iValue), 1);

		// with long
		IPrologLong lValue = new JiPrologLong(28);
		assertEquals(empty.compareTo(lValue), 1);

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertEquals(empty.compareTo(fValue), 1);

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertEquals(empty.compareTo(dValue), 1);

		// with variable
		IPrologVariable variable = new JiPrologVariable("X");
		// true. case [] and variable
		assertEquals(empty.compareTo(variable), 1);

		// with predicate
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(empty.compareTo(structure), -1);

		// with list
		IPrologList list = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertEquals(empty.compareTo(list), -1);
		assertEquals(empty.compareTo(empty), 0);

		// with expression
		IPrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertEquals(empty.compareTo(expression), -1);

	}

	@Test
	public final void testEqualsObject() {
		assertEquals(new JiPrologEmpty(), empty);
	}

}
