package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import org.logicware.jpi.jiprolog.JiPrologAtom;
import org.logicware.jpi.jiprolog.JiPrologDouble;
import org.logicware.jpi.jiprolog.JiPrologFloat;
import org.logicware.jpi.jiprolog.JiPrologInteger;
import org.logicware.jpi.jiprolog.JiPrologLong;
import org.logicware.jpi.jiprolog.JiPrologProvider;
import org.logicware.jpi.jiprolog.JiPrologVariable;

import com.ugos.jiprolog.engine.JIPAtom;

public class JiPrologAtomTest {

	private JiPrologAtom atom;

	@Before
	public void setUp() throws Exception {
		atom = new JiPrologAtom("an_atom");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClone() {
		assertEquals(new JiPrologAtom("an_atom"), atom.clone());
	}

	@Test
	public final void testAtomAdapter() {
		assertNotNull(atom.value);
		assertEquals(PrologTerm.ATOM_TYPE, atom.type);
		assertEquals(JIPAtom.create("an_atom"), atom.value);
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new JiPrologAtom[0], atom.getArguments());
	}

	@Test
	public final void testGetValue() {
		assertEquals("an_atom", atom.getStringValue());
	}

	@Test
	public final void testSetValue() {
		assertEquals("an_atom", atom.getStringValue());
		atom.setStringValue("other_atom_value");
		assertEquals("other_atom_value", atom.getStringValue());
	}

	@Test
	public final void testGetKey() {
		assertEquals("an_atom/0", atom.getIndicator());
	}

	@Test
	public final void testGetType() {
		assertEquals(PrologTerm.ATOM_TYPE, atom.getType());
	}

	@Test
	public final void testIsAtom() {
		assertTrue(atom.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertFalse(atom.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(atom.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(atom.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(atom.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(atom.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(atom.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(atom.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(atom.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(atom.isExpression());
	}

	@Test
	public final void testIsAtomic() {
		assertTrue(atom.isAtomic());
	}

	@Test
	public final void testIsCompound() {
		assertFalse(atom.isCompound());
	}

	@Test
	public final void testGetArity() {
		assertEquals(0, atom.getArity());
	}

	@Test
	public final void testGetFunctor() {
		assertEquals("an_atom", atom.getFunctor());
	}

	@Test
	public final void testUnify() {
		// with atom
		PrologAtom atom = new JiPrologAtom("smith");
		PrologAtom atom1 = new JiPrologAtom("doe");
		// true because the atoms are equals
		assertTrue(atom.unify(atom));
		// false because the atoms are different
		assertFalse(atom.unify(atom1));

		// with integer
		PrologInteger iValue = new JiPrologInteger(28);
		assertFalse(atom.unify(iValue));

		// with long
		PrologLong lValue = new JiPrologLong(28);
		assertFalse(atom.unify(lValue));

		// with float
		PrologFloat fValue = new JiPrologFloat(36.47);
		assertFalse(atom.unify(fValue));

		// with double
		PrologDouble dValue = new JiPrologDouble(36.47);
		assertFalse(atom.unify(dValue));

		// with variable
		PrologVariable variable = new JiPrologVariable("X");
		// true. case atom and variable
		assertTrue(atom.unify(variable));

		// with predicate
		PrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertFalse(atom.unify(structure));

		// with list
		PrologList flattenedList = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertFalse(atom.unify(flattenedList));

		// with expression
		PrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertFalse(atom.unify(expression));
	}

	@Test
	public final void testCompareTo() {

		// with atom
		PrologAtom atom = new JiPrologAtom("smith");
		PrologAtom atom1 = new JiPrologAtom("doe");
		// true because the atoms are equals
		assertEquals(atom.compareTo(atom), 0);
		// false because the atoms are different
		assertEquals(atom.compareTo(atom1), 1);

		// with integer
		PrologInteger iValue = new JiPrologInteger(28);
		assertEquals(atom.compareTo(iValue), 1);

		// with long
		PrologLong lValue = new JiPrologLong(28);
		assertEquals(atom.compareTo(lValue), 1);

		// with float
		PrologFloat fValue = new JiPrologFloat(36.47);
		assertEquals(atom.compareTo(fValue), 1);

		// with double
		PrologDouble dValue = new JiPrologDouble(36.47);
		assertEquals(atom.compareTo(dValue), 1);

		// with variable
		PrologVariable variable = new JiPrologVariable("X");
		// true. case atom and variable
		assertEquals(atom.compareTo(variable), 1);

		// with predicate
		PrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(atom.compareTo(structure), -1);

		// with list
		PrologList flattenedList = new JiPrologProvider().parsePrologList("[a,b,c]");
		assertEquals(atom.compareTo(flattenedList), -1);

		// with expression
		PrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertEquals(atom.compareTo(expression), -1);

	}

}
