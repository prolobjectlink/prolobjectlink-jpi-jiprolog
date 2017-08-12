package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.jpi.JiPrologBaseTest;
import org.logicware.jpi.PrologEngine;
import org.logicware.jpi.PrologQuery;
import org.logicware.jpi.PrologTerm;

public class JiPrologQueryTest extends JiPrologBaseTest {

	private PrologEngine engine;
	private PrologQuery query;

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

		engine = provider.newPrologEngine();

		// employee relationship
		engine.assertz(provider.parsePrologTerm("employee( mcardon, 1, 5 )."));
		engine.assertz(provider.parsePrologTerm("employee( treeman, 2, 3 )."));
		engine.assertz(provider.parsePrologTerm("employee( chapman, 1, 2 )."));
		engine.assertz(provider.parsePrologTerm("employee( claessen, 4, 1 )."));
		engine.assertz(provider.parsePrologTerm("employee( petersen, 5, 8 )."));
		engine.assertz(provider.parsePrologTerm("employee( cohn, 1, 7 )."));
		engine.assertz(provider.parsePrologTerm("employee( duffy, 1, 9 )."));

		// department relationship
		engine.assertz(provider.parsePrologTerm("department( 1, board )."));
		engine.assertz(provider.parsePrologTerm("department( 2, human_resources )."));
		engine.assertz(provider.parsePrologTerm("department( 3, production )."));
		engine.assertz(provider.parsePrologTerm("department( 4, technical_services )."));
		engine.assertz(provider.parsePrologTerm("department( 5, administration )."));

		// salary relationship
		engine.assertz(provider.parsePrologTerm("salary( 1, 1000 )."));
		engine.assertz(provider.parsePrologTerm("salary( 2, 1500 )."));
		engine.assertz(provider.parsePrologTerm("salary( 3, 2000 )."));
		engine.assertz(provider.parsePrologTerm("salary( 4, 2500 )."));
		engine.assertz(provider.parsePrologTerm("salary( 5, 3000 )."));
		engine.assertz(provider.parsePrologTerm("salary( 6, 3500 )."));
		engine.assertz(provider.parsePrologTerm("salary( 7, 4000 )."));
		engine.assertz(provider.parsePrologTerm("salary( 8, 4500 )."));
		engine.assertz(provider.parsePrologTerm("salary( 9, 5000 )."));

		query = engine.query("employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money)");

	}

	@After
	public final void tearDown() throws Exception {
		query.dispose();
	}

	@Test
	public final void testGetEngine() {
		assertEquals(provider.newPrologEngine(), query.getEngine());
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

		Map<String, PrologTerm> solutionMap = query.nextVariablesSolution();
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

		Map<String, PrologTerm>[] allSolutionMap = query.nVariablesSolutions(7);

		Map<String, PrologTerm> solutionMap = allSolutionMap[0];
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

		Map<String, PrologTerm>[] allSolutionMap = query.allVariablesSolutions();

		Map<String, PrologTerm> solutionMap = allSolutionMap[0];
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
