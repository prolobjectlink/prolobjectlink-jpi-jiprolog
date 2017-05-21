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
import org.logicware.jpi.IPrologExpression;
import org.logicware.jpi.IPrologFloat;
import org.logicware.jpi.IPrologInteger;
import org.logicware.jpi.IPrologList;
import org.logicware.jpi.IPrologLong;
import org.logicware.jpi.IPrologStructure;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.IPrologVariable;
import org.logicware.jpi.JiPrologBaseTest;
import org.logicware.jpi.jiprolog.JiPrologAtom;
import org.logicware.jpi.jiprolog.JiPrologDouble;
import org.logicware.jpi.jiprolog.JiPrologFloat;
import org.logicware.jpi.jiprolog.JiPrologInteger;
import org.logicware.jpi.jiprolog.JiPrologLong;
import org.logicware.jpi.jiprolog.JiPrologProvider;
import org.logicware.jpi.jiprolog.JiPrologStructure;
import org.logicware.jpi.jiprolog.JiPrologTerm;
import org.logicware.jpi.jiprolog.JiPrologVariable;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPTerm;

public class JiPrologStructureTest extends JiPrologBaseTest {

	private JiPrologStructure structure;

	@Before
	public void setUp() throws Exception {
		structure = new JiPrologStructure("digits", zero, one, two, three, four, five, six, seven, eight, nine);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testStructureAdapter() {
		assertNotNull(structure.value);
		assertEquals(IPrologTerm.STRUCTURE_TYPE, structure.type);

		JIPTerm[] arguments = new JIPTerm[] {

		JIPNumber.create(0),

		JIPNumber.create(1),

		JIPNumber.create(2),

		JIPNumber.create(3),

		JIPNumber.create(4),

		JIPNumber.create(5),

		JIPNumber.create(6),

		JIPNumber.create(7),

		JIPNumber.create(8),

		JIPNumber.create(9) };

		JIPCons jipCons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			jipCons = JIPCons.create(arguments[i], jipCons);
		}

		assertEquals(JIPFunctor.create("digits", jipCons), structure.value);

	}

	@Test
	public final void testGetArguments() {
		JiPrologTerm[] terms = { zero, one, two, three, four, five, six, seven, eight, nine };
		assertArrayEquals(terms, structure.getArguments());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public final void testGetArgument() {
		assertEquals(zero, structure.getArgument(0));
		assertEquals(one, structure.getArgument(1));
		assertEquals(two, structure.getArgument(2));
		assertEquals(three, structure.getArgument(3));
		assertEquals(four, structure.getArgument(4));
		assertEquals(five, structure.getArgument(5));
		assertEquals(six, structure.getArgument(6));
		assertEquals(seven, structure.getArgument(7));
		assertEquals(eight, structure.getArgument(8));
		assertEquals(nine, structure.getArgument(9));

		structure.getArgument(-25);
		structure.getArgument(10);
	}

	@Test
	public final void testIsAtom() {
		assertFalse(structure.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertFalse(structure.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(structure.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(structure.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(structure.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(structure.isList());
	}

	@Test
	public final void testIsStructure() {
		assertTrue(structure.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(structure.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(structure.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(structure.isExpression());
	}

	@Test
	public final void testGetType() {
		assertEquals(IPrologTerm.STRUCTURE_TYPE, structure.getType());
	}

	@Test
	public final void testGetKey() {
		assertEquals("digits/10", structure.getIndicator());
	}

	@Test
	public final void testGetArity() {
		assertEquals(10, structure.getArity());
	}

	@Test
	public final void testGetFunctor() {
		assertEquals("digits", structure.getFunctor());
	}

	@Test
	public final void testUnify() {

		// with atom
		IPrologAtom atom = new JiPrologAtom("John Doe");
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a)");
		assertFalse(structure.unify(atom));

		// with integer
		IPrologInteger iValue = new JiPrologInteger(28);
		assertFalse(structure.unify(iValue));

		// with long
		IPrologLong lValue = new JiPrologLong(28);
		assertFalse(structure.unify(lValue));

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertFalse(structure.unify(fValue));

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertFalse(structure.unify(dValue));

		// with variable
		IPrologVariable variable = new JiPrologVariable("X");
		// true. case predicate and variable
		assertTrue(structure.unify(variable));

		// with predicate
		IPrologStructure structure1 = new JiPrologProvider().parsePrologStructure("some_predicate(X)");
		IPrologStructure structure2 = new JiPrologProvider().parsePrologStructure("some_predicate(28)");
		// true because are equals
		assertTrue(structure.unify(structure));
		// true because match and their arguments unify
		assertTrue(structure.unify(structure1));
		// false because match but their arguments not unify
		assertFalse(structure.unify(structure2));

		// with list
		IPrologList flattenList = new JiPrologProvider().parsePrologList("['Some Literal']");
		IPrologList headTailList = new JiPrologProvider().parsePrologList("['Some Literal'|[]]");
		IPrologTerm empty = new JiPrologProvider().prologEmpty();
		assertFalse(structure.unify(flattenList));
		assertFalse(structure.unify(headTailList));
		assertFalse(structure.unify(empty));

		// with expression
		IPrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertFalse(structure.unify(expression));

	}

	@Test
	public final void testCompareTo() {

		// with atom
		IPrologAtom atom = new JiPrologAtom("John Doe");
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("some_predicate(a)");
		assertEquals(structure.compareTo(atom), 1);

		// with integer
		IPrologInteger iValue = new JiPrologInteger(28);
		assertEquals(structure.compareTo(iValue), 1);

		// with long
		IPrologLong lValue = new JiPrologLong(28);
		assertEquals(structure.compareTo(lValue), 1);

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertEquals(structure.compareTo(fValue), 1);

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertEquals(structure.compareTo(dValue), 1);

		// with variable
		IPrologVariable variable = new JiPrologVariable("X");
		// true. case predicate and variable
		assertEquals(structure.compareTo(variable), 1);

		// with predicate
		IPrologStructure structure1 = new JiPrologProvider().parsePrologStructure("some_predicate(X)");
		IPrologStructure structure2 = new JiPrologProvider().parsePrologStructure("some_predicate(28)");
		// true because are equals
		assertEquals(structure.compareTo(structure), 0);
		// true because match and their arguments compareTo
		assertEquals(structure.compareTo(structure1), 1);
		// false because match but their arguments not compareTo
		assertEquals(structure.compareTo(structure2), 1);

		// with list
		IPrologList flattenList = new JiPrologProvider().parsePrologList("['Some Literal']");
		IPrologList headTailList = new JiPrologProvider().parsePrologList("['Some Literal'|[]]");
		IPrologTerm empty = new JiPrologProvider().prologEmpty();
		assertEquals(structure.compareTo(flattenList), -1);
		assertEquals(structure.compareTo(headTailList), -1);
		assertEquals(structure.compareTo(empty), 1);

		// with expression
		IPrologExpression expression = new JiPrologProvider().parsePrologExpression("58+93*10");
		assertEquals(structure.compareTo(expression), -1);

	}

}
