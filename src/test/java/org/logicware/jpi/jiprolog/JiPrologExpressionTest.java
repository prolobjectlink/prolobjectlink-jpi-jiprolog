package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

public class JiPrologExpressionTest extends JiPrologBaseTest {

	private PrologExpression expression;

	@Before
	public void setUp() throws Exception {
		expression = provider.newPrologExpression(x, "+", y);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetArguments() {
		assertArrayEquals(new PrologTerm[] { x, y }, expression.getArguments());
	}

	@Test
	public final void testGetOperator() {
		assertEquals("+", expression.getOperator());
	}

	@Test
	public final void testGetLeft() {
		assertEquals(provider.newPrologVariable("X"), expression.getLeft());
	}

	@Test
	public final void testGetRight() {
		assertEquals(provider.newPrologVariable("Y"), expression.getRight());
	}

	@Test
	public final void testGetType() {
		assertEquals(PrologTerm.EXPRESSION_TYPE, expression.getType());
	}

	@Test
	public final void testIsAtom() {
		assertFalse(expression.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertFalse(expression.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(expression.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(expression.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(expression.isVariable());
	}

	@Test
	public final void testIsList() {
		assertFalse(expression.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(expression.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(expression.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(expression.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertTrue(expression.isEvaluable());
	}

	@Test
	public final void testGetKey() {
		assertEquals("+/2", expression.getIndicator());
	}

	@Test
	public final void testGetArity() {
		assertEquals(2, expression.getArity());
	}

	@Test
	public final void testGetFunctor() {
		assertEquals("+", expression.getFunctor());
	}

	@Test
	public final void testUnify() {

		PrologExpression expression = provider.parsePrologExpression("58+93*10");

		// with atom
		PrologAtom atom = provider.newPrologAtom("John Doe");
		assertFalse(expression.unify(atom));

		// with integer
		PrologInteger iValue = provider.newPrologInteger(28);
		assertFalse(expression.unify(iValue));

		// with long
		PrologLong lValue = provider.newPrologLong(28);
		assertFalse(expression.unify(lValue));

		// with float
		PrologFloat fValue = provider.newPrologFloat(36.47);
		assertFalse(expression.unify(fValue));

		// with double
		PrologDouble dValue = provider.newPrologDouble(36.47);
		assertFalse(expression.unify(dValue));

		// with variable
		PrologVariable variable = provider.newPrologVariable("X");
		// true. case expression and variable
		assertTrue(expression.unify(variable));

		// with predicate
		PrologStructure structure = provider.parsePrologStructure("some_predicate(a)");
		assertFalse(expression.unify(structure));

		// with list
		PrologList flattenList = provider.parsePrologList("['Some Literal']");
		PrologList headTailList = provider.parsePrologList("['Some Literal'|[]]");
		PrologTerm empty = provider.prologEmpty();
		assertFalse(expression.unify(flattenList));
		assertFalse(expression.unify(headTailList));
		assertFalse(expression.unify(empty));

		// with expression
		PrologExpression expression1 = provider.parsePrologExpression("X+Y*Z");
		PrologExpression expression2 = provider.parsePrologExpression("3.14+1.58*2.71");

		// true because are equals
		assertTrue(expression.unify(expression));
		// true because match and their arguments unify
		assertTrue(expression.unify(expression1));
		// false because match but their arguments not unify
		assertFalse(expression.unify(expression2));

	}

	@Test
	public final void testCompareTo() {

		PrologExpression expression = provider.parsePrologExpression("58+93*10");

		// with atom
		PrologAtom atom = provider.newPrologAtom("John Doe");
		assertEquals(expression.compareTo(atom), 1);

		// with integer
		PrologInteger iValue = provider.newPrologInteger(28);
		assertEquals(expression.compareTo(iValue), 1);

		// with long
		PrologLong lValue = provider.newPrologLong(28);
		assertEquals(expression.compareTo(lValue), 1);

		// with float
		PrologFloat fValue = provider.newPrologFloat(36.47);
		assertEquals(expression.compareTo(fValue), 1);

		// with double
		PrologDouble dValue = provider.newPrologDouble(36.47);
		assertEquals(expression.compareTo(dValue), 1);

		// with variable
		PrologVariable variable = provider.newPrologVariable("X");
		// true. case expression and variable
		assertEquals(expression.compareTo(variable), 1);

		// with predicate
		PrologStructure structure = provider.parsePrologStructure("some_predicate(a)");
		assertEquals(expression.compareTo(structure), 1);

		// with list
		PrologList flattenList = provider.parsePrologList("['Some Literal']");
		PrologList headTailList = provider.parsePrologList("['Some Literal'|[]]");
		PrologTerm empty = provider.prologEmpty();
		assertEquals(expression.compareTo(flattenList), -1);
		assertEquals(expression.compareTo(headTailList), -1);
		assertEquals(expression.compareTo(empty), 1);

		// with expression
		PrologExpression expression1 = provider.parsePrologExpression("X+Y*Z");
		PrologExpression expression2 = provider.parsePrologExpression("3.14+1.58*2.71");

		// true because are equals
		assertEquals(expression.compareTo(expression), 0);
		// true because match and their arguments are equals
		assertEquals(expression.compareTo(expression1), 1);
		// false because match but their arguments not equals
		assertEquals(expression.compareTo(expression2), 1);

	}

}
