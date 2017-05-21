package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.JiPrologBaseTest;
import org.logicware.jpi.jiprolog.JiPrologEngine;
import org.logicware.jpi.jiprolog.JiPrologQuery;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTermParser;

public class JiPrologQueryTest extends JiPrologBaseTest {

	private JIPEngine engine;
	private JIPTermParser parser;
	private JiPrologQuery query;

	@Before
	public final void setUp() throws Exception {

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

		engine = new JIPEngine();
		parser = engine.getTermParser();

		// employee relationship
		engine.assertz(parser.parseTerm("employee( mcardon, 1, 5 )."));
		engine.assertz(parser.parseTerm("employee( treeman, 2, 3 )."));
		engine.assertz(parser.parseTerm("employee( chapman, 1, 2 )."));
		engine.assertz(parser.parseTerm("employee( claessen, 4, 1 )."));
		engine.assertz(parser.parseTerm("employee( petersen, 5, 8 )."));
		engine.assertz(parser.parseTerm("employee( cohn, 1, 7 )."));
		engine.assertz(parser.parseTerm("employee( duffy, 1, 9 )."));

		// department relationship
		engine.assertz(parser.parseTerm("department( 1, board )."));
		engine.assertz(parser.parseTerm("department( 2, human_resources )."));
		engine.assertz(parser.parseTerm("department( 3, production )."));
		engine.assertz(parser.parseTerm("department( 4, technical_services )."));
		engine.assertz(parser.parseTerm("department( 5, administration )."));

		// salary relationship
		engine.assertz(parser.parseTerm("salary( 1, 1000 )."));
		engine.assertz(parser.parseTerm("salary( 2, 1500 )."));
		engine.assertz(parser.parseTerm("salary( 3, 2000 )."));
		engine.assertz(parser.parseTerm("salary( 4, 2500 )."));
		engine.assertz(parser.parseTerm("salary( 5, 3000 )."));
		engine.assertz(parser.parseTerm("salary( 6, 3500 )."));
		engine.assertz(parser.parseTerm("salary( 7, 4000 )."));
		engine.assertz(parser.parseTerm("salary( 8, 4500 )."));
		engine.assertz(parser.parseTerm("salary( 9, 5000 )."));

		query = new JiPrologQuery(engine, "employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money)");

	}

	@After
	public final void tearDown() throws Exception {
		query.dispose();
	}

	@Test
	public final void testGetEngine() {
		assertEquals(new JiPrologEngine(engine), query.getEngine());
	}

	@Test
	public final void testHasSolution() {
		assertTrue(query.hasSolution());
	}

	@Test
	public final void testHasMoreSolutions() {
		assertTrue(query.hasMoreSolutions());
	}

	@Test
	public final void testOneSolution() {
		assertArrayEquals(expecteds0, query.oneSolution());
	}

	@Test
	public final void testOneVariablesSolution() {
		assertEquals(mcardon, query.oneVariablesSolution().get("Name"));
		assertEquals(one, query.oneVariablesSolution().get("Dpto"));
		assertEquals(five, query.oneVariablesSolution().get("Scale"));
		assertEquals(board, query.oneVariablesSolution().get("DepartmentName"));
		assertEquals(threeThousand, query.oneVariablesSolution().get("Money"));
	}

	@Test
	public final void testNextSolution() {

		assertArrayEquals(expecteds0, query.nextSolution());
		assertArrayEquals(expecteds1, query.nextSolution());
		assertArrayEquals(expecteds2, query.nextSolution());
		assertArrayEquals(expecteds3, query.nextSolution());
		assertArrayEquals(expecteds4, query.nextSolution());
		assertArrayEquals(expecteds5, query.nextSolution());
		assertArrayEquals(expecteds6, query.nextSolution());

	}

	@Test
	public final void testNextVariablesSolution() {

		Map<String, IPrologTerm> solutionMap = query.nextVariablesSolution();
		assertEquals(mcardon, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(five, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(threeThousand, solutionMap.get("Money"));

		solutionMap = query.nextVariablesSolution();
		assertEquals(treeman, solutionMap.get("Name"));
		assertEquals(two, solutionMap.get("Dpto"));
		assertEquals(three, solutionMap.get("Scale"));
		assertEquals(human_resources, solutionMap.get("DepartmentName"));
		assertEquals(twoThousand, solutionMap.get("Money"));

		solutionMap = query.nextVariablesSolution();
		assertEquals(chapman, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(two, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(thousandFiveHundred, solutionMap.get("Money"));

		solutionMap = query.nextVariablesSolution();
		assertEquals(claessen, solutionMap.get("Name"));
		assertEquals(four, solutionMap.get("Dpto"));
		assertEquals(one, solutionMap.get("Scale"));
		assertEquals(technical_services, solutionMap.get("DepartmentName"));
		assertEquals(thousand, solutionMap.get("Money"));

		solutionMap = query.nextVariablesSolution();
		assertEquals(petersen, solutionMap.get("Name"));
		assertEquals(five, solutionMap.get("Dpto"));
		assertEquals(eight, solutionMap.get("Scale"));
		assertEquals(administration, solutionMap.get("DepartmentName"));
		assertEquals(fourThousandFiveHundred, solutionMap.get("Money"));

		solutionMap = query.nextVariablesSolution();
		assertEquals(cohn, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(seven, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fourThousand, solutionMap.get("Money"));

		solutionMap = query.nextVariablesSolution();
		assertEquals(duffy, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(nine, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fiveThousand, solutionMap.get("Money"));

	}

	@Test
	public final void testNSolutions() {
		assertArrayEquals(solution, query.nSolutions(7));
	}

	@Test
	public final void testNVariablesSolutions() {

		Map<String, IPrologTerm>[] allSolutionMap = query.nVariablesSolutions(7);

		Map<String, IPrologTerm> solutionMap = allSolutionMap[0];
		assertEquals(mcardon, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(five, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(threeThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[1];
		assertEquals(treeman, solutionMap.get("Name"));
		assertEquals(two, solutionMap.get("Dpto"));
		assertEquals(three, solutionMap.get("Scale"));
		assertEquals(human_resources, solutionMap.get("DepartmentName"));
		assertEquals(twoThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[2];
		assertEquals(chapman, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(two, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(thousandFiveHundred, solutionMap.get("Money"));

		solutionMap = allSolutionMap[3];
		assertEquals(claessen, solutionMap.get("Name"));
		assertEquals(four, solutionMap.get("Dpto"));
		assertEquals(one, solutionMap.get("Scale"));
		assertEquals(technical_services, solutionMap.get("DepartmentName"));
		assertEquals(thousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[4];
		assertEquals(petersen, solutionMap.get("Name"));
		assertEquals(five, solutionMap.get("Dpto"));
		assertEquals(eight, solutionMap.get("Scale"));
		assertEquals(administration, solutionMap.get("DepartmentName"));
		assertEquals(fourThousandFiveHundred, solutionMap.get("Money"));

		solutionMap = allSolutionMap[5];
		assertEquals(cohn, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(seven, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fourThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[6];
		assertEquals(duffy, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(nine, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fiveThousand, solutionMap.get("Money"));

	}

	@Test
	public final void testAllSolutions() {
		assertArrayEquals(solution, query.allSolutions());
	}

	@Test
	public final void testAllVariablesSolutions() {

		Map<String, IPrologTerm>[] allSolutionMap = query.allVariablesSolutions();

		Map<String, IPrologTerm> solutionMap = allSolutionMap[0];
		assertEquals(mcardon, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(five, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(threeThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[1];
		assertEquals(treeman, solutionMap.get("Name"));
		assertEquals(two, solutionMap.get("Dpto"));
		assertEquals(three, solutionMap.get("Scale"));
		assertEquals(human_resources, solutionMap.get("DepartmentName"));
		assertEquals(twoThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[2];
		assertEquals(chapman, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(two, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(thousandFiveHundred, solutionMap.get("Money"));

		solutionMap = allSolutionMap[3];
		assertEquals(claessen, solutionMap.get("Name"));
		assertEquals(four, solutionMap.get("Dpto"));
		assertEquals(one, solutionMap.get("Scale"));
		assertEquals(technical_services, solutionMap.get("DepartmentName"));
		assertEquals(thousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[4];
		assertEquals(petersen, solutionMap.get("Name"));
		assertEquals(five, solutionMap.get("Dpto"));
		assertEquals(eight, solutionMap.get("Scale"));
		assertEquals(administration, solutionMap.get("DepartmentName"));
		assertEquals(fourThousandFiveHundred, solutionMap.get("Money"));

		solutionMap = allSolutionMap[5];
		assertEquals(cohn, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(seven, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fourThousand, solutionMap.get("Money"));

		solutionMap = allSolutionMap[6];
		assertEquals(duffy, solutionMap.get("Name"));
		assertEquals(one, solutionMap.get("Dpto"));
		assertEquals(nine, solutionMap.get("Scale"));
		assertEquals(board, solutionMap.get("DepartmentName"));
		assertEquals(fiveThousand, solutionMap.get("Money"));

	}

}
