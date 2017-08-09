package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.jpi.PrologAtom;
import org.logicware.jpi.PrologEngine;
import org.logicware.jpi.PrologExpression;
import org.logicware.jpi.PrologFloat;
import org.logicware.jpi.PrologInteger;
import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologQuery;
import org.logicware.jpi.IPrologStructure;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.IPrologVariable;
import org.logicware.jpi.JiPrologBaseTest;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPVariable;

public class JiPrologProviderTest extends JiPrologBaseTest {

	private JiPrologProvider provider = new JiPrologProvider();

	@Before
	public void setUp() throws Exception {

		solution[0][0] = mcardon;
		solution[0][1] = one;
		solution[0][2] = five;
		solution[0][3] = board;
		solution[0][4] = threeThousand;

		solution[1][0] = treeman;
		solution[1][1] = two;
		solution[1][2] = three;
		solution[1][3] = human_resources;
		solution[1][4] = twoThousand;

		solution[2][0] = chapman;
		solution[2][1] = one;
		solution[2][2] = two;
		solution[2][3] = board;
		solution[2][4] = thousandFiveHundred;

		solution[3][0] = claessen;
		solution[3][1] = four;
		solution[3][2] = one;
		solution[3][3] = technical_services;
		solution[3][4] = thousand;

		solution[4][0] = petersen;
		solution[4][1] = five;
		solution[4][2] = eight;
		solution[4][3] = administration;
		solution[4][4] = fourThousandFiveHundred;

		solution[5][0] = cohn;
		solution[5][1] = one;
		solution[5][2] = seven;
		solution[5][3] = board;
		solution[5][4] = fourThousand;

		solution[6][0] = duffy;
		solution[6][1] = one;
		solution[6][2] = nine;
		solution[6][3] = board;
		solution[6][4] = fiveThousand;

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testNewEngine() {
		assertNotNull(provider.newPrologEngine());
		// assertEquals(new EngineAdapter(), adapter.newEngine());
	}

	@Test
	public final void testNewPrologAtom() {
		PrologAtom atom = provider.newPrologAtom("an_atom");
		assertEquals(PrologTerm.ATOM_TYPE, atom.getType());
		assertEquals(new JiPrologAtom("an_atom"), atom);
		assertEquals("an_atom", atom.getFunctor());
		assertEquals("an_atom/0", atom.getIndicator());
		assertEquals(0, atom.getArity());
	}

	@Test
	public final void testNewPrologFloat() {
		PrologFloat f = provider.newPrologFloat(3.14);
		assertEquals(PrologTerm.FLOAT_TYPE, f.getType());
		assertEquals(new JiPrologFloat(3.14), f);
		assertEquals(3.14F, f.getFloatValue(), 0);
	}

	@Test
	public final void testNewPrologInteger() {
		PrologInteger integer = provider.newPrologInteger(100);
		assertEquals(PrologTerm.INTEGER_TYPE, integer.getType());
		assertEquals(new JiPrologInteger(100), integer);
		assertEquals(100, integer.getIntValue());
	}

	@Test
	public final void testNewPrologVariable() {
		IPrologVariable variable = provider.newPrologVariable("X");
		assertEquals(PrologTerm.VARIABLE_TYPE, variable.getType());
		assertEquals(new JiPrologVariable("X"), variable);
		assertEquals("X", variable.getName());
	}

	@Test
	public final void testNewPrologList() {
		PrologList list = provider.newPrologList(new PrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine });
		assertEquals(new JiPrologList(new PrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine }), list);
		assertEquals(PrologTerm.LIST_TYPE, list.getType());
		assertEquals(".", list.getFunctor());
		assertEquals("./2", list.getIndicator());
		assertEquals(2, list.getArity());
	}

	@Test
	public final void testNewPrologStructure() {
		IPrologStructure structure = provider.newPrologStructure("digits", zero, one, two, three, four, five, six, seven, eight, nine);
		assertEquals(new JiPrologStructure("digits", zero, one, two, three, four, five, six, seven, eight, nine), structure);
		assertEquals(PrologTerm.STRUCTURE_TYPE, structure.getType());
		assertEquals("digits", structure.getFunctor());
		assertEquals("digits/10", structure.getIndicator());
		assertEquals(10, structure.getArity());
	}

	@Test
	public final void testNewPrologExpression() {
		PrologExpression expression = provider.newPrologExpression(x, "+", y);
		assertEquals(new JiPrologExpression(x, "+", y), expression);
		assertEquals(PrologTerm.EXPRESSION_TYPE, expression.getType());
		assertEquals("+", expression.getFunctor());
		assertEquals("+/2", expression.getIndicator());
		assertEquals(2, expression.getArity());
	}

	@Test
	public final void testParseTerm() {

		assertEquals(provider.prologCut(), provider.parsePrologTerm("!"));
		assertEquals(provider.prologNil(), provider.parsePrologTerm("nil"));
		assertEquals(provider.prologTrue(), provider.parsePrologTerm("true"));
		assertEquals(provider.prologFalse(), provider.parsePrologTerm("false"));
		assertEquals(provider.prologFail(), provider.parsePrologTerm("fail"));
		assertEquals(provider.prologEmpty(), provider.parsePrologTerm("[]"));

		PrologAtom atom = (PrologAtom) provider.parsePrologTerm("an_atom");
		assertEquals(new JiPrologAtom("an_atom"), atom);

		// IPrologFloat f = (IPrologFloat) adapter.parseTerm("3.14");
		// assertEquals(new FloatAdapter(3.14), f);

		PrologList list = (PrologList) provider.parsePrologTerm("[0,1,2,3,4,5,6,7,8,9]");
		assertEquals(new JiPrologList(new PrologTerm[] { zero, one, two, three, four, five, six, seven, eight, nine }), list);

		IPrologStructure structure = (IPrologStructure) provider.parsePrologTerm("digits(0,1,2,3,4,5,6,7,8,9)");
		assertEquals(new JiPrologStructure("digits", zero, one, two, three, four, five, six, seven, eight, nine), structure);

		PrologExpression expression = (PrologExpression) provider.parsePrologTerm("X+Y");
		assertEquals(new JiPrologExpression(x, "+", y), expression);

	}

	@Test
	public final void testParseTerms() {

		PrologTerm employeeStructure = provider.newPrologStructure(employee, name, dpto, scale);
		PrologTerm departmentStructure = provider.newPrologStructure(department, dpto, dptoName);
		PrologTerm salaryStructure = provider.newPrologStructure(salary, scale, money);

		PrologTerm expression = provider.newPrologExpression(money, "<", provider.newPrologInteger(2000));

		PrologTerm[] structures = new PrologTerm[] { employeeStructure, departmentStructure, salaryStructure, expression };
		assertArrayEquals(structures, provider.parsePrologTerms("employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money),Money < 2000"));

	}

	@Test
	public final void test() {

		PrologEngine iPrologEngine = provider.newPrologEngine();
		iPrologEngine.consult("company.pl");
		// Map<String, IPrologTerm> map =
		// iPrologEngine.find("employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money),Money < 2000");
		// Map<String, IPrologTerm> map =
		// iPrologEngine.find(adapter.parseTerms("employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money),Money < 2000"));

		PrologExpression expression = provider.newPrologExpression(money, "<", provider.newPrologInteger(2000));
		PrologQuery query = iPrologEngine.createQuery(expression);

		System.out.println(query.hasSolution());

		query = iPrologEngine.createQuery("Money is 1000, Money < 2000");
		System.out.println(query.hasSolution());

		JIPFunctor eValue = JIPFunctor.create("<", JIPCons.create(JIPVariable.create("HJ"), JIPCons.create(JIPNumber.create(200), null)));

		JIPEngine jipEngine = new JIPEngine();
		JIPQuery jipQuery = jipEngine.openSynchronousQuery(eValue);
		System.out.println(jipQuery.hasMoreChoicePoints());

		// System.out.println(map);

	}

}
