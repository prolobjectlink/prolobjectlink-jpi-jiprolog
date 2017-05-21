package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.jpi.IPrologAtom;
import org.logicware.jpi.IPrologDouble;
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

import com.ugos.jiprolog.engine.JIPVariable;

public class JiPrologVariableTest {

	private JiPrologVariable variable;

	@Before
	public void setUp() throws Exception {
		variable = new JiPrologVariable("X");
	}

	@After
	public void tearDown() throws Exception {
	}

	public final void testVariableAdapter() {
		assertNotNull(variable.value);
		assertEquals(IPrologTerm.VARIABLE_TYPE, variable.type);
		assertEquals(JIPVariable.create(), variable.value);
	}

	public final void testVariableAdapter(String name) {
		assertNotNull(variable.value);
		assertEquals(IPrologTerm.VARIABLE_TYPE, variable.type);
		assertEquals(JIPVariable.create("X"), variable.value);
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new JiPrologInteger[0], variable.getArguments());
	}

	@Test
	public final void testIsAnonim() {
		assertFalse(variable.isAnonim());
		assertTrue(new JiPrologVariable().isAnonim());
	}

	@Test
	public final void testGetName() {
		assertEquals("X", variable.getName());
	}

	@Test
	public final void testSetName() {
		assertEquals("X", variable.getName());
		variable.setName("Y");
		assertEquals("Y", variable.getName());
	}

	@Test(expected = IndicatorError.class)
	public final void testGetKey() {
		variable.getIndicator();
	}

	@Test
	public final void testGetType() {
		assertEquals(IPrologTerm.VARIABLE_TYPE, variable.getType());
	}

	@Test
	public final void testIsAtom() {
		assertFalse(variable.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertFalse(variable.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(variable.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(variable.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertTrue(variable.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(variable.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(variable.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(variable.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(variable.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(variable.isExpression());
	}

	public final void testGetArity() {
		variable.getArity();
	}

	public final void testGetFunctor() {
		variable.getFunctor();
	}

	@Test
	public final void testUnify() {

		// with atom
		IPrologVariable variable = new JiPrologVariable("X");
		IPrologAtom atom = new JiPrologAtom("John Smith");
		assertTrue(variable.unify(atom));

		// with integer
		variable = new JiPrologVariable("X");
		IPrologInteger iValue = new JiPrologInteger(28);
		assertTrue(variable.unify(iValue));

		// with long
		variable = new JiPrologVariable("X");
		IPrologLong lValue = new JiPrologLong(28);
		assertTrue(variable.unify(lValue));

		// with float
		variable = new JiPrologVariable("X");
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertTrue(variable.unify(fValue));

		// with double
		variable = new JiPrologVariable("X");
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertTrue(variable.unify(dValue));

		// with variable
		variable = new JiPrologVariable("X");
		IPrologVariable y = new JiPrologVariable("Y");
		assertTrue(variable.unify(variable)); // are
												// equals
		assertTrue(variable.unify(y)); // alphabetic
		// substitution

		// with predicate with occurs check
		variable = new JiPrologVariable("X");
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertTrue(variable.unify(structure));
		structure = new JiPrologProvider().parsePrologStructure("structure([X])");
		assertTrue(variable.unify(structure));

		variable = new JiPrologVariable("X");
		structure = new JiPrologProvider().parsePrologStructure("structure(A,b,C)");
		assertTrue(variable.unify(structure));

		// with list
		variable = new JiPrologVariable("X");
		IPrologVariable z = new JiPrologVariable("Z");
		IPrologList flattenList = new JiPrologProvider().parsePrologList("[X]");
		IPrologList headTailList = new JiPrologProvider().parsePrologList("[Y|[]]");
		IPrologTerm empty = new JiPrologProvider().prologEmpty();
		assertTrue(variable.unify(flattenList));
		assertTrue(y.unify(headTailList));
		assertTrue(z.unify(empty));
	}

	@Test
	public final void testCompareTo() {

		// with atom
		IPrologVariable variable = new JiPrologVariable("X");
		IPrologAtom atom = new JiPrologAtom("John Smith");
		assertEquals(variable.compareTo(atom), -1);

		// with integer
		variable = new JiPrologVariable("X");
		IPrologInteger iValue = new JiPrologInteger(28);
		assertEquals(variable.compareTo(iValue), -1);

		// with long
		variable = new JiPrologVariable("X");
		IPrologLong lValue = new JiPrologLong(28);
		assertEquals(variable.compareTo(lValue), -1);

		// with float
		variable = new JiPrologVariable("X");
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertEquals(variable.compareTo(fValue), -1);

		// with double
		variable = new JiPrologVariable("X");
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertEquals(variable.compareTo(dValue), -1);

		// with variable
		variable = new JiPrologVariable("X");
		IPrologVariable y = new JiPrologVariable("Y");
		assertEquals(variable.compareTo(variable), 0); // are
		// equals
		assertEquals(variable.compareTo(y), 0); // alphabetic
		// substitution

		variable = new JiPrologVariable("X");
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a,b,c)");
		assertEquals(variable.compareTo(structure), -1);
		structure = new JiPrologProvider().parsePrologStructure("structure([X])");
		assertEquals(variable.compareTo(structure), -1);

		variable = new JiPrologVariable("X");
		structure = new JiPrologProvider().parsePrologStructure("structure(A,b,C)");
		assertEquals(variable.compareTo(structure), -1);

		// with list
		variable = new JiPrologVariable("X");
		IPrologVariable z = new JiPrologVariable("Z");
		IPrologList flattenList = new JiPrologProvider().parsePrologList("[X]");
		IPrologList headTailList = new JiPrologProvider().parsePrologList("[Y|[]]");
		IPrologTerm empty = new JiPrologProvider().prologEmpty();
		assertEquals(variable.compareTo(flattenList), -1);
		assertEquals(y.compareTo(headTailList), -1);
		assertEquals(z.compareTo(empty), -1);
	}

}
