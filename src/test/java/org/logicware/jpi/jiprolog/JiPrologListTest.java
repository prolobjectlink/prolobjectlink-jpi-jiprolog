package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

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
import org.logicware.jpi.JiPrologBaseTest;

import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPTerm;

public class JiPrologListTest extends JiPrologBaseTest {

	private JiPrologList list;

	@Before
	public void setUp() throws Exception {
		list = new JiPrologList(new IPrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine });
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testListAdapter() {

		assertNotNull(list.value);
		assertEquals(IPrologTerm.LIST_TYPE, list.type);

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

		JIPList jipList = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			jipList = JIPList.create(arguments[i], jipList);
		}

		assertEquals(jipList, list.value);

	}

	@Test
	public final void testGetArguments() {
		JiPrologTerm[] terms = { zero, one, two, three, four, five, six, seven, eight, nine };
		assertArrayEquals(terms, list.getArguments());
	}

	@Test
	public final void testSize() {
		assertEquals(10, list.size());
	}

	@Test
	public final void testClear() {
		list.clear();
		assertTrue(list.isEmpty());
		assertEquals(JIPList.NIL, list.value);
	}

	@Test
	public final void testIsEmpty() {
		assertFalse(list.isEmpty());
		list.clear();
		System.out.println(list.size());
		System.out.println(((JIPList) list.value).length());
		assertTrue(list.isEmpty());
	}

	@Test
	public final void testIterator() {
		int number = 0;
		for (Iterator<IPrologTerm> iterator = list.iterator(); iterator.hasNext();) {
			IPrologTerm prologTerm = (IPrologTerm) iterator.next();
			assertEquals(new JiPrologInteger(number++), prologTerm);
		}
	}

	@Test
	public final void testGetHead() {
		assertEquals(new JiPrologInteger(0), list.getHead());
	}

	@Test
	public final void testGetTail() {
		assertEquals(new JiPrologList(new IPrologTerm[] { one, two, three, four, five, six, seven, eight, nine }), list.getTail());
	}

	@Test
	public final void testGetType() {
		assertEquals(IPrologTerm.LIST_TYPE, list.getType());
	}

	@Test
	public final void testGetKey() {
		assertEquals("./2", list.getIndicator());
	}

	@Test
	public final void testIsAtom() {
		assertFalse(list.isAtom());
	}

	@Test
	public final void testIsNumber() {
		assertFalse(list.isNumber());
	}

	@Test
	public final void testIsFloat() {
		assertFalse(list.isFloat());
	}

	@Test
	public final void testIsInteger() {
		assertFalse(list.isInteger());
	}

	@Test
	public final void testIsVariable() {
		assertFalse(list.isVariable());
	}

	@Test
	public final void testIsList() {
		assertTrue(list.isList());
	}

	@Test
	public final void testIsStructure() {
		assertFalse(list.isStructure());
	}

	@Test
	public final void testIsNil() {
		assertFalse(list.isNil());
	}

	@Test
	public final void testIsEmptyList() {
		assertFalse(list.isEmptyList());
	}

	@Test
	public final void testIsExpression() {
		assertFalse(list.isExpression());
	}

	@Test
	public final void testGetArity() {
		assertEquals(2, list.getArity());
	}

	@Test
	public final void testGetFunctor() {
		assertEquals(".", list.getFunctor());
	}

	@Test
	public final void testUnify() {

		IPrologTerm empty = new JiPrologProvider().prologEmpty();
		IPrologList flattened = new JiPrologProvider().parsePrologList("[a,b,c]");
		IPrologList headTail = new JiPrologProvider().parsePrologList("[a|[b|[c|[]]]]");

		// with atom
		IPrologAtom atom = new JiPrologAtom("John Doe");
		assertFalse(flattened.unify(atom));
		assertFalse(headTail.unify(atom));
		assertFalse(empty.unify(atom));

		// with integer
		IPrologInteger iValue = new JiPrologInteger(28);
		assertFalse(flattened.unify(iValue));
		assertFalse(headTail.unify(iValue));
		assertFalse(empty.unify(iValue));

		// with long
		IPrologLong lValue = new JiPrologLong(28);
		assertFalse(flattened.unify(lValue));
		assertFalse(headTail.unify(lValue));
		assertFalse(empty.unify(lValue));

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertFalse(flattened.unify(fValue));
		assertFalse(headTail.unify(fValue));
		assertFalse(empty.unify(fValue));

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertFalse(flattened.unify(dValue));
		assertFalse(headTail.unify(dValue));
		assertFalse(empty.unify(dValue));

		// with variable
		IPrologVariable x = new JiPrologVariable("X");
		IPrologVariable y = new JiPrologVariable("Y");
		IPrologVariable z = new JiPrologVariable("Z");
		assertTrue(flattened.unify(x));
		assertTrue(headTail.unify(y));
		assertTrue(empty.unify(z));

		// with predicate
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("somepredicate(a,b,c)");
		assertFalse(flattened.unify(structure));
		assertFalse(headTail.unify(structure));
		assertFalse(empty.unify(structure));

		// with list
		x = new JiPrologVariable("X");

		IPrologList flattenList1 = new JiPrologProvider().parsePrologList("[X,Y,Z]");
		IPrologList headTailList1 = new JiPrologProvider().parsePrologList("[X|[Y|[Z]]]");

		// true because are equals
		assertTrue(flattened.unify(flattened));
		assertTrue(headTail.unify(headTail));
		assertTrue(empty.unify(empty));

		// true because their terms unify
		assertTrue(flattened.unify(flattenList1));
		assertTrue(headTail.unify(headTailList1));

		// testing different list type that unify
		assertTrue(flattened.unify(headTail));
		assertTrue(flattenList1.unify(headTailList1));
		assertTrue(flattened.unify(headTailList1));
		assertTrue(flattenList1.unify(headTail));

	}

	@Test
	public final void testCompareTo() {

		IPrologTerm empty = new JiPrologProvider().prologEmpty();
		IPrologList flattened = new JiPrologProvider().parsePrologList("[a,b,c]");
		IPrologList headTail = new JiPrologProvider().parsePrologList("[a|[b|[c|[]]]]");

		// with atom
		IPrologAtom atom = new JiPrologAtom("John Doe");
		assertEquals(flattened.compareTo(atom), 1);
		assertEquals(headTail.compareTo(atom), 1);
		assertEquals(empty.compareTo(atom), 1);

		// with integer
		IPrologInteger iValue = new JiPrologInteger(28);
		assertEquals(flattened.compareTo(iValue), 1);
		assertEquals(headTail.compareTo(iValue), 1);
		assertEquals(empty.compareTo(iValue), 1);

		// with long
		IPrologLong lValue = new JiPrologLong(28);
		assertEquals(flattened.compareTo(lValue), 1);
		assertEquals(headTail.compareTo(lValue), 1);
		assertEquals(empty.compareTo(lValue), 1);

		// with float
		IPrologFloat fValue = new JiPrologFloat(36.47);
		assertEquals(flattened.compareTo(fValue), 1);
		assertEquals(headTail.compareTo(fValue), 1);
		assertEquals(empty.compareTo(fValue), 1);

		// with double
		IPrologDouble dValue = new JiPrologDouble(36.47);
		assertEquals(flattened.compareTo(dValue), 1);
		assertEquals(headTail.compareTo(dValue), 1);
		assertEquals(empty.compareTo(dValue), 1);

		// with variable
		IPrologVariable x = new JiPrologVariable("X");
		IPrologVariable y = new JiPrologVariable("Y");
		IPrologVariable z = new JiPrologVariable("Z");
		assertEquals(flattened.compareTo(x), 1);
		assertEquals(headTail.compareTo(y), 1);
		assertEquals(empty.compareTo(z), 1);

		// with predicate
		IPrologStructure structure = new JiPrologProvider().parsePrologStructure("somepredicate(a,b,c)");
		assertEquals(flattened.compareTo(structure), -1);
		assertEquals(headTail.compareTo(structure), -1);
		assertEquals(empty.compareTo(structure), -1);

		// with list
		x = new JiPrologVariable("X");

		IPrologList flattenList1 = new JiPrologProvider().parsePrologList("[X,Y,Z]");
		IPrologList headTailList1 = new JiPrologProvider().parsePrologList("[X|[Y|[Z]]]");

		// true because are equals
		assertEquals(flattened.compareTo(flattened), 0);
		assertEquals(headTail.compareTo(headTail), 0);
		assertEquals(empty.compareTo(empty), 0);

		// true because their terms are equals
		assertEquals(flattened.compareTo(flattenList1), 1);
		assertEquals(headTail.compareTo(headTailList1), 1);

		// testing different list type
		assertEquals(flattened.compareTo(headTail), 0);
		assertEquals(flattenList1.compareTo(headTailList1), 1);
		assertEquals(flattened.compareTo(headTailList1), 1);
		assertEquals(flattenList1.compareTo(headTail), -1);

	}

}
