package org.logicware.jpi.jiprolog;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.logicware.jpi.JiPrologBaseTest;
import org.logicware.jpi.PredicateIndicator;
import org.logicware.jpi.PrologAtom;
import org.logicware.jpi.PrologEngine;
import org.logicware.jpi.PrologQuery;
import org.logicware.jpi.PrologStructure;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.PrologVariable;

import com.ugos.jiprolog.engine.JIPClausesDatabase;
import com.ugos.jiprolog.engine.JIPEngine;

public class JiPrologEngineTest extends JiPrologBaseTest {

    private PrologEngine engine;
    private PrologQuery query;

    @Before
    public void setUp() throws Exception {

	engine = provider.newEngine();

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
	engine.dispose();
    }

    @Test
    public final void testInclude() {

	engine.include("family.pl");
	engine.include("company.pl");
	engine.include("zoo.pl");
	assertFalse(engine.isProgramEmpty());
	assertEquals(50, engine.getProgramSize());

    }

    @Test
    public final void testConsult() {

	engine = provider.newEngine();
	engine.consult("family.pl");
	assertFalse(engine.isProgramEmpty());
	assertEquals(21, engine.getProgramSize());
	engine.dispose();

	engine = provider.newEngine();
	engine.consult("company.pl");
	assertFalse(engine.isProgramEmpty());
	assertEquals(21, engine.getProgramSize());
	engine.dispose();

	engine = provider.newEngine();
	engine.consult("zoo.pl");
	assertFalse(engine.isProgramEmpty());
	assertEquals(8, engine.getProgramSize());
	engine.dispose();

    }

    @Test
    public final void testSave() {
	// Family save test case
	engine = provider.newEngine();

	engine.assertz(provider.newStructure(parent, pam, bob));
	engine.assertz(provider.newStructure(parent, tom, bob));
	engine.assertz(provider.newStructure(parent, tom, liz));
	engine.assertz(provider.newStructure(parent, bob, ann));
	engine.assertz(provider.newStructure(parent, bob, pat));
	engine.assertz(provider.newStructure(parent, pat, jim));

	engine.assertz(provider.newStructure(female, pam));
	engine.assertz(provider.newStructure(male, tom));
	engine.assertz(provider.newStructure(male, bob));
	engine.assertz(provider.newStructure(female, liz));
	engine.assertz(provider.newStructure(female, ann));
	engine.assertz(provider.newStructure(female, pat));
	engine.assertz(provider.newStructure(male, jim));

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");
	engine.assertz(provider.newStructure(offspring, x, y), provider.newStructure(parent, x, y));

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
		provider.newStructure(female, x));

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");
	engine.assertz(provider.newStructure(grandparent, x, z), provider.newStructure(parent, x, y),
		provider.newStructure(parent, y, z));

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");

	engine.assertz(provider.newStructure(sister, x, y), provider.newStructure(parent, z, x),
		provider.newStructure(parent, z, y), provider.newStructure(female, x),
		provider.newStructure(different, x, y));

	x = provider.newVariable("X");
	engine.assertz(provider.newStructure(different, x, x), provider.prologCut(), provider.prologFail());

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	engine.assertz(provider.newStructure(different, x, y));

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");
	engine.assertz(provider.newStructure(predecessor, x, z), provider.newStructure(parent, x, z));

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");
	engine.assertz(provider.newStructure(predecessor, x, z), provider.newStructure(parent, x, y),
		provider.newStructure(predecessor, y, z));

	engine.persist("family.pl");

	// Physic existence test
	File file = new File("family.pl");
	assertTrue(file.exists());
	assertTrue(file.length() > 0);

	// Logical program saved
	engine.consult("family.pl");
	assertFalse(engine.isProgramEmpty());
	assertEquals(21, engine.getProgramSize());
	engine.dispose();

	// Company save test case
	engine = provider.newEngine();

	// employee relationship
	engine.assertz(provider.newStructure(employee, mcardon, one, five));
	engine.assertz(provider.newStructure(employee, treeman, two, three));
	engine.assertz(provider.newStructure(employee, chapman, one, two));
	engine.assertz(provider.newStructure(employee, claessen, four, one));
	engine.assertz(provider.newStructure(employee, petersen, five, eight));
	engine.assertz(provider.newStructure(employee, cohn, one, seven));
	engine.assertz(provider.newStructure(employee, duffy, one, nine));

	// department relationship
	engine.assertz(provider.newStructure(department, one, board));
	engine.assertz(provider.newStructure(department, two, human_resources));
	engine.assertz(provider.newStructure(department, three, production));
	engine.assertz(provider.newStructure(department, four, technical_services));
	engine.assertz(provider.newStructure(department, five, administration));

	// salary relationship
	engine.assertz(provider.newStructure(salary, one, thousand));
	engine.assertz(provider.newStructure(salary, two, thousandFiveHundred));
	engine.assertz(provider.newStructure(salary, three, twoThousand));
	engine.assertz(provider.newStructure(salary, four, twoThousandFiveHundred));
	engine.assertz(provider.newStructure(salary, five, threeThousand));
	engine.assertz(provider.newStructure(salary, six, threeThousandFiveHundred));
	engine.assertz(provider.newStructure(salary, seven, fourThousand));
	engine.assertz(provider.newStructure(salary, eight, fourThousandFiveHundred));
	engine.assertz(provider.newStructure(salary, nine, fiveThousand));

	engine.persist("company.pl");

	// Physic existence test
	file = new File("company.pl");
	assertTrue(file.exists());
	assertTrue(file.length() > 0);

	// Logical program saved
	engine.consult("company.pl");
	assertFalse(engine.isProgramEmpty());
	assertEquals(21, engine.getProgramSize());
	engine.dispose();

	// Zoo save test case
	engine = provider.newEngine();

	engine.assertz(provider.newStructure("big", bear));
	engine.assertz(provider.newStructure("big", elephant));
	engine.assertz(provider.newStructure("small", cat));
	engine.assertz(provider.newStructure("brown", bear));
	engine.assertz(provider.newStructure("black", cat));
	engine.assertz(provider.newStructure("gray", elephant));

	// dark rules
	z = provider.newVariable("Z");
	engine.assertz(provider.newStructure("dark", z), provider.newStructure("black", z));

	z = provider.newVariable("Z");
	engine.assertz(provider.newStructure("dark", z), provider.newStructure("brown", z));

	engine.persist("zoo.pl");

	// Physic existence test
	file = new File("zoo.pl");
	assertTrue(file.exists());
	assertTrue(file.length() > 0);

	// Logical program saved
	engine.consult("zoo.pl");
	assertFalse(engine.isProgramEmpty());
	assertEquals(8, engine.getProgramSize());
	engine.dispose();

    }

    @Test
    public final void testAbolish() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	assertEquals(6, engine.getProgramSize());

	engine.abolish("parent", 2);
	assertTrue(engine.isProgramEmpty());
	assertEquals(0, engine.getProgramSize());

    }

    @Test
    public final void testAssertaString() {
	engine.asserta("parent( pam, bob)");
	assertEquals(1, engine.getProgramSize());

	engine.asserta("parent( pam, bob)");
	// the engine size is one because the added clause is already defined
	assertEquals(1, engine.getProgramSize());

	engine.asserta("parent( tom, bob)");
	assertEquals(2, engine.getProgramSize());

	engine.asserta("mother( X, Y):-parent( X, Y),female( X)");
	assertEquals(3, engine.getProgramSize());

	engine.asserta("mother( X, Y):-parent( X, Y),female( X)");
	// the program size is three because the added clause is already defined
	assertEquals(3, engine.getProgramSize());

    }

    @Test
    public final void testAssertaIPrologTerm() {
	engine.asserta(provider.newStructure(parent, pam, bob));
	assertEquals(1, engine.getProgramSize());

	engine.asserta(provider.newStructure(parent, pam, bob));
	// the engine size is one because the added clause is already defined
	assertEquals(1, engine.getProgramSize());

	engine.asserta(provider.newStructure(parent, tom, bob));
	assertEquals(2, engine.getProgramSize());

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	engine.asserta(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
		provider.newStructure(female, x));
	assertEquals(3, engine.getProgramSize());

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	engine.asserta(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
		provider.newStructure(female, x));
	// the program size is three because the added clause is already defined
	assertEquals(3, engine.getProgramSize());

    }

    @Test
    public final void testAssertaIPrologTermIPrologTermArray() {
	PrologVariable x = provider.newVariable("X");
	PrologVariable y = provider.newVariable("Y");
	engine.asserta(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
		provider.newStructure(female, x));
	assertEquals(1, engine.getProgramSize());
    }

    @Test
    public final void testAssertzString() {
	engine.assertz("parent( pam, bob)");
	assertEquals(1, engine.getProgramSize());

	engine.assertz("parent( pam, bob)");
	// the engine size is one because the added clause is already defined
	assertEquals(1, engine.getProgramSize());

	engine.assertz("parent( tom, bob)");
	assertEquals(2, engine.getProgramSize());

	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	assertEquals(3, engine.getProgramSize());

	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	// the program size is three because the added clause is already defined
	assertEquals(3, engine.getProgramSize());
    }

    @Test
    public final void testAssertzIPrologTerm() {
	engine.assertz(provider.newStructure(parent, pam, bob));
	assertEquals(1, engine.getProgramSize());

	engine.assertz(provider.newStructure(parent, pam, bob));
	// the engine size is one because the added clause is already defined
	assertEquals(1, engine.getProgramSize());

	engine.assertz(provider.newStructure(parent, tom, bob));
	assertEquals(2, engine.getProgramSize());

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
		provider.newStructure(female, x));
	assertEquals(3, engine.getProgramSize());

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
		provider.newStructure(female, x));
	// the program size is three because the added clause is already defined
	assertEquals(3, engine.getProgramSize());

    }

    @Test
    public final void testAssertzIPrologTermIPrologTermArray() {
	PrologVariable x = provider.newVariable("X");
	PrologVariable y = provider.newVariable("Y");
	engine.assertz(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
		provider.newStructure(female, x));
	assertEquals(1, engine.getProgramSize());
    }

    @Test
    public final void testClauseString() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");

	assertTrue(engine.clause("parent( X, Y)"));
	assertTrue(engine.clause("parent( pam, bob)"));
	assertTrue(engine.clause("parent( pat, jim)"));
	assertTrue(engine.clause("predecessor( X, Z):-parent( X, Z)"));
	assertTrue(engine.clause("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)"));

    }

    @Test
    public final void testClauseIPrologTerm() {
	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");

	PrologAtom pam = provider.newAtom("pam");
	PrologAtom bob = provider.newAtom("bob");
	assertTrue(engine.clause(provider.newStructure("parent", pam, bob)));

	PrologAtom pat = provider.newAtom("pat");
	PrologAtom jim = provider.newAtom("jim");
	assertTrue(engine.clause(provider.newStructure("parent", pat, jim)));

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	assertTrue(engine.clause(provider.newStructure("parent", x, y)));
    }

    @Test
    public final void testClauseIPrologTermIPrologTermArray() {
	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");
	assertTrue(engine.clause(provider.newStructure("predecessor", x, z), provider.newStructure("parent", x, z)));

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");
	assertTrue(engine.clause(provider.newStructure("predecessor", x, z), provider.newStructure("parent", x, y),
		provider.newStructure("predecessor", y, z)));

    }

    @Test
    public final void testRetractString() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");

	engine.retract("parent( tom, bob)");
	assertEquals(5, engine.getProgramSize());

	engine.retract("parent( bob, pat)");
	assertEquals(4, engine.getProgramSize());

	engine.retract("parent( pat, jim)");
	assertEquals(3, engine.getProgramSize());

	engine.retract("parent( pam, bob)");
	assertEquals(2, engine.getProgramSize());

	engine.retract("parent( tom, liz)");
	assertEquals(1, engine.getProgramSize());

	engine.retract("parent( bob, ann)");
	assertEquals(0, engine.getProgramSize());

    }

    @Test
    public final void testRetractIPrologTerm() {
	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");

	engine.retract(provider.newStructure(parent, tom, bob));
	assertEquals(5, engine.getProgramSize());

	engine.retract(provider.newStructure(parent, bob, pat));
	assertEquals(4, engine.getProgramSize());

	engine.retract(provider.newStructure(parent, pat, jim));
	assertEquals(3, engine.getProgramSize());

	engine.retract(provider.newStructure(parent, pam, bob));
	assertEquals(2, engine.getProgramSize());

	engine.retract(provider.newStructure(parent, tom, liz));
	assertEquals(1, engine.getProgramSize());

	engine.retract(provider.newStructure(parent, bob, ann));
	assertEquals(0, engine.getProgramSize());
    }

    @Test
    public final void testRetractIPrologTermIPrologTermArray() {
	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	assertEquals(1, engine.getProgramSize());

	PrologVariable x = provider.newVariable("X");
	PrologVariable y = provider.newVariable("Y");
	engine.retract(provider.newStructure(mother, x, y), provider.newStructure(parent, x, y),
		provider.newStructure(female, x));
	assertEquals(0, engine.getProgramSize());
    }

    @Test
    public final void testFindString() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("female( pam)");
	engine.assertz("male( tom)");
	engine.assertz("male( bob)");
	engine.assertz("female( liz)");
	engine.assertz("female( ann)");
	engine.assertz("female( pat)");
	engine.assertz("male( jim)");
	engine.assertz("offspring( Y, X):-parent( X, Y)");
	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
	engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
	engine.assertz("different( X, X) :- !, fail");
	engine.assertz("different( X, Y)");

	famillySolutionMap.put("X", pam);
	famillySolutionMap.put("Y", bob);

	solutionMap = engine.find("mother(X,Y)");
	assertEquals(famillySolutionMap, solutionMap);

    }

    @Test
    public final void testFindIPrologTerm() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("female( pam)");
	engine.assertz("male( tom)");
	engine.assertz("male( bob)");
	engine.assertz("female( liz)");
	engine.assertz("female( ann)");
	engine.assertz("female( pat)");
	engine.assertz("male( jim)");
	engine.assertz("offspring( Y, X):-parent( X, Y)");
	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
	engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
	engine.assertz("different( X, X) :- !, fail");
	engine.assertz("different( X, Y)");

	famillySolutionMap.put("X", pam);
	famillySolutionMap.put("Y", bob);

	PrologVariable x = provider.newVariable("X");
	PrologVariable y = provider.newVariable("Y");
	solutionMap = engine.find(provider.newStructure(mother, x, y));
	assertEquals(famillySolutionMap, solutionMap);
    }

    @Test
    public final void testFindIPrologTermArray() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("female( pam)");
	engine.assertz("male( tom)");
	engine.assertz("male( bob)");
	engine.assertz("female( liz)");
	engine.assertz("female( ann)");
	engine.assertz("female( pat)");
	engine.assertz("male( jim)");
	engine.assertz("offspring( Y, X):-parent( X, Y)");
	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
	engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
	engine.assertz("different( X, X) :- !, fail");
	engine.assertz("different( X, Y)");

	famillySolutionMap.put("X", pam);
	famillySolutionMap.put("Y", bob);
	famillySolutionMap.put("Z", ann);

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");

	solutionMap = engine.find(provider.newStructure(mother, x, y), provider.newStructure(grandparent, x, z));

	assertEquals(famillySolutionMap, solutionMap);
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testFindAllString() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("female( pam)");
	engine.assertz("male( tom)");
	engine.assertz("male( bob)");
	engine.assertz("female( liz)");
	engine.assertz("female( ann)");
	engine.assertz("female( pat)");
	engine.assertz("male( jim)");
	engine.assertz("offspring( Y, X):-parent( X, Y)");
	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
	engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
	engine.assertz("different( X, X) :- !, fail");
	engine.assertz("different( X, Y)");

	famillyAllSolutionMap = new HashMap[6];
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", pam);
	solutionMap.put("Y", bob);
	famillyAllSolutionMap[0] = solutionMap;
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", tom);
	solutionMap.put("Y", bob);
	famillyAllSolutionMap[1] = solutionMap;

	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", tom);
	solutionMap.put("Y", liz);
	famillyAllSolutionMap[2] = solutionMap;
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", bob);
	solutionMap.put("Y", ann);
	famillyAllSolutionMap[3] = solutionMap;

	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", bob);
	solutionMap.put("Y", pat);
	famillyAllSolutionMap[4] = solutionMap;
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", pat);
	solutionMap.put("Y", jim);
	famillyAllSolutionMap[5] = solutionMap;

	Map<String, PrologTerm>[] allSolutionMap = engine.findAll("parent( X, Y)");
	assertArrayEquals(famillyAllSolutionMap, allSolutionMap);

    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testFindAllIPrologTerm() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("female( pam)");
	engine.assertz("male( tom)");
	engine.assertz("male( bob)");
	engine.assertz("female( liz)");
	engine.assertz("female( ann)");
	engine.assertz("female( pat)");
	engine.assertz("male( jim)");
	engine.assertz("offspring( Y, X):-parent( X, Y)");
	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
	engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
	engine.assertz("different( X, X) :- !, fail");
	engine.assertz("different( X, Y)");

	famillyAllSolutionMap = new HashMap[6];
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", pam);
	solutionMap.put("Y", bob);
	famillyAllSolutionMap[0] = solutionMap;
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", tom);
	solutionMap.put("Y", bob);
	famillyAllSolutionMap[1] = solutionMap;

	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", tom);
	solutionMap.put("Y", liz);
	famillyAllSolutionMap[2] = solutionMap;
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", bob);
	solutionMap.put("Y", ann);
	famillyAllSolutionMap[3] = solutionMap;

	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", bob);
	solutionMap.put("Y", pat);
	famillyAllSolutionMap[4] = solutionMap;
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", pat);
	solutionMap.put("Y", jim);
	famillyAllSolutionMap[5] = solutionMap;

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	Map<String, PrologTerm>[] allSolutionMap = engine.findAll(provider.newStructure(parent, x, y));
	assertArrayEquals(famillyAllSolutionMap, allSolutionMap);
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testFindAllIPrologTermArray() {

	engine.assertz("parent( pam, bob)");
	engine.assertz("parent( tom, bob)");
	engine.assertz("parent( tom, liz)");
	engine.assertz("parent( bob, ann)");
	engine.assertz("parent( bob, pat)");
	engine.assertz("parent( pat, jim)");
	engine.assertz("female( pam)");
	engine.assertz("male( tom)");
	engine.assertz("male( bob)");
	engine.assertz("female( liz)");
	engine.assertz("female( ann)");
	engine.assertz("female( pat)");
	engine.assertz("male( jim)");
	engine.assertz("offspring( Y, X):-parent( X, Y)");
	engine.assertz("mother( X, Y):-parent( X, Y),female( X)");
	engine.assertz("grandparent( X, Z):-parent( X, Y),parent( Y, Z)");
	engine.assertz("sister( X, Y):-parent( Z, X),parent( Z, Y),female( X),different( X, Y)");
	engine.assertz("predecessor( X, Z):-parent( X, Z)");
	engine.assertz("predecessor( X, Z):-parent( X, Y),predecessor( Y, Z)");
	engine.assertz("different( X, X) :- !, fail");
	engine.assertz("different( X, Y)");

	famillyAllSolutionMap = new HashMap[2];
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", pam);
	solutionMap.put("Y", bob);
	solutionMap.put("Z", ann);
	famillyAllSolutionMap[0] = solutionMap;
	solutionMap = new HashMap<String, PrologTerm>();
	solutionMap.put("X", pam);
	solutionMap.put("Y", bob);
	solutionMap.put("Z", pat);
	famillyAllSolutionMap[1] = solutionMap;

	x = provider.newVariable("X");
	y = provider.newVariable("Y");
	z = provider.newVariable("Z");
	Map<String, PrologTerm>[] allSolutionMap = engine.findAll(provider.newStructure(mother, x, y),
		provider.newStructure(grandparent, x, z));

	assertArrayEquals(famillyAllSolutionMap, allSolutionMap);
    }

    @Test
    public final void testCreateQueryString() {

	// employee relationship
	engine.assertz("employee( mcardon, 1, 5 )");
	engine.assertz("employee( treeman, 2, 3 )");
	engine.assertz("employee( chapman, 1, 2 )");
	engine.assertz("employee( claessen, 4, 1 )");
	engine.assertz("employee( petersen, 5, 8 )");
	engine.assertz("employee( cohn, 1, 7 )");
	engine.assertz("employee( duffy, 1, 9 )");

	// department relationship
	engine.assertz("department( 1, board )");
	engine.assertz("department( 2, human_resources )");
	engine.assertz("department( 3, production )");
	engine.assertz("department( 4, technical_services )");
	engine.assertz("department( 5, administration )");

	// salary relationship
	engine.assertz("salary( 1, 1000 )");
	engine.assertz("salary( 2, 1500 )");
	engine.assertz("salary( 3, 2000 )");
	engine.assertz("salary( 4, 2500 )");
	engine.assertz("salary( 5, 3000 )");
	engine.assertz("salary( 6, 3500 )");
	engine.assertz("salary( 7, 4000 )");
	engine.assertz("salary( 8, 4500 )");
	engine.assertz("salary( 9, 5000 )");

	query = engine.query("employee(Name,Dpto,Scale),department(Dpto,DepartmentName),salary(Scale,Money)");
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

    @Test
    public final void testCreateQueryIPrologTermArray() {

	// employee relationship
	engine.assertz("employee( mcardon, 1, 5 )");
	engine.assertz("employee( treeman, 2, 3 )");
	engine.assertz("employee( chapman, 1, 2 )");
	engine.assertz("employee( claessen, 4, 1 )");
	engine.assertz("employee( petersen, 5, 8 )");
	engine.assertz("employee( cohn, 1, 7 )");
	engine.assertz("employee( duffy, 1, 9 )");

	// department relationship
	engine.assertz("department( 1, board )");
	engine.assertz("department( 2, human_resources )");
	engine.assertz("department( 3, production )");
	engine.assertz("department( 4, technical_services )");
	engine.assertz("department( 5, administration )");

	// salary relationship
	engine.assertz("salary( 1, 1000 )");
	engine.assertz("salary( 2, 1500 )");
	engine.assertz("salary( 3, 2000 )");
	engine.assertz("salary( 4, 2500 )");
	engine.assertz("salary( 5, 3000 )");
	engine.assertz("salary( 6, 3500 )");
	engine.assertz("salary( 7, 4000 )");
	engine.assertz("salary( 8, 4500 )");
	engine.assertz("salary( 9, 5000 )");

	PrologStructure employee = provider.newStructure("employee", name, dpto, scale);
	PrologStructure department = provider.newStructure("department", dpto, dptoName);
	PrologStructure salary = provider.newStructure("salary", scale, money);

	query = engine.query(employee, department, salary);
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

    @Test
    public final void testOperator() {

	assertFalse(engine.currentOperator(1200, "xfx", "<=="));
	engine.operator(1200, "xfx", "<==");
	assertTrue(engine.currentOperator(1200, "xfx", "<=="));

    }

    @Test
    public final void testCurrentPredicates() {
	Set<PredicateIndicator> builtins = new HashSet<PredicateIndicator>();
	Collection<JIPClausesDatabase> collection = new JIPEngine().getGlobalDataBase();
	for (JIPClausesDatabase jipClausesDatabase : collection) {
	    String functor = jipClausesDatabase.getFunctorName();
	    int arity = jipClausesDatabase.getArity();
	    PredicateIndicator pi = new PredicateIndicator(functor, arity);
	    builtins.add(pi);
	}
	assertEquals(builtins, engine.currentPredicates());
    }

    @Test
    public final void testCurrentOperators() {
	assertEquals(JiPrologConverter.OPERATORS, engine.currentOperators());
    }

    @Test
    public final void testCurrentPredicate() {

	engine.include("family.pl");
	engine.include("company.pl");
	engine.include("zoo.pl");

	// user defined
	assertTrue(engine.currentPredicate("predecessor", 2));
	assertTrue(engine.currentPredicate("grandparent", 2));
	assertTrue(engine.currentPredicate("different", 2));
	assertTrue(engine.currentPredicate("offspring", 2));
	assertTrue(engine.currentPredicate("parent", 2));
	assertTrue(engine.currentPredicate("mother", 2));
	assertTrue(engine.currentPredicate("sister", 2));
	assertTrue(engine.currentPredicate("female", 1));
	assertTrue(engine.currentPredicate("male", 1));

	assertTrue(engine.currentPredicate("big", 1));
	assertTrue(engine.currentPredicate("small", 1));
	assertTrue(engine.currentPredicate("dark", 1));
	assertTrue(engine.currentPredicate("gray", 1));
	assertTrue(engine.currentPredicate("brown", 1));
	assertTrue(engine.currentPredicate("black", 1));

	assertTrue(engine.currentPredicate("salary", 2));
	assertTrue(engine.currentPredicate("employee", 3));
	assertTrue(engine.currentPredicate("department", 2));

	// supported built-ins
	assertTrue(engine.currentPredicate("list_to_set", 2));
	assertTrue(engine.currentPredicate("atom_chars", 2));
	assertTrue(engine.currentPredicate("number", 1));
	assertTrue(engine.currentPredicate("intersection", 3));
	assertTrue(engine.currentPredicate("setof", 3));
	assertTrue(engine.currentPredicate("member", 3));
	assertTrue(engine.currentPredicate("sumlist", 2));
	assertTrue(engine.currentPredicate("difference", 3));
	assertTrue(engine.currentPredicate("peek_chars", 2));
	assertTrue(engine.currentPredicate("between", 3));
	assertTrue(engine.currentPredicate("msort", 2));
	assertTrue(engine.currentPredicate("read_clause", 3));
	assertTrue(engine.currentPredicate("split_command_", 2));
	assertTrue(engine.currentPredicate("xml_element_by_name", 3));
	assertTrue(engine.currentPredicate("^", 2));
	assertTrue(engine.currentPredicate("write_elements", 1));
	assertTrue(engine.currentPredicate("tell", 2));
	assertTrue(engine.currentPredicate("copy_term", 2));
	assertTrue(engine.currentPredicate("meta_predicate", 1));
	assertTrue(engine.currentPredicate("release_object", 1));
	assertTrue(engine.currentPredicate("split", 5));
	assertTrue(engine.currentPredicate("maplist", 3));
	assertTrue(engine.currentPredicate("one", 1));
	assertTrue(engine.currentPredicate("use_module", 1));
	assertTrue(engine.currentPredicate("term_is_free_of", 3));
	assertTrue(engine.currentPredicate("last_", 3));
	assertTrue(engine.currentPredicate("at_end_of_stream", 0));
	assertTrue(engine.currentPredicate("time_file", 2));
	assertTrue(engine.currentPredicate("get_fields", 2));
	assertTrue(engine.currentPredicate("atom_prefix", 2));
	assertTrue(engine.currentPredicate("reverse", 2));
	assertTrue(engine.currentPredicate("peek_chars", 1));
	assertTrue(engine.currentPredicate("member", 2));
	assertTrue(engine.currentPredicate("throw", 1));
	assertTrue(engine.currentPredicate("simple", 1));
	assertTrue(engine.currentPredicate("msort", 3));
	assertTrue(engine.currentPredicate("phrase", 2));
	assertTrue(engine.currentPredicate("C", 3));
	assertTrue(engine.currentPredicate("tell", 1));
	assertTrue(engine.currentPredicate("lists_to_atoms", 2));
	assertTrue(engine.currentPredicate("peek_code", 2));
	assertTrue(engine.currentPredicate("atomic", 1));
	assertTrue(engine.currentPredicate("$char", 2));
	assertTrue(engine.currentPredicate("put_code", 2));
	assertTrue(engine.currentPredicate("xml_document_version", 2));
	assertTrue(engine.currentPredicate("xml_append_attribute", 3));
	assertTrue(engine.currentPredicate("tab", 2));
	assertTrue(engine.currentPredicate("time", 1));
	assertTrue(engine.currentPredicate("findall", 4));
	assertTrue(engine.currentPredicate("dcg_or", 4));
	assertTrue(engine.currentPredicate("at_end_of_stream", 1));
	assertTrue(engine.currentPredicate("bagof", 3));
	assertTrue(engine.currentPredicate("phrase", 3));
	assertTrue(engine.currentPredicate("term_is_free_of", 2));
	assertTrue(engine.currentPredicate("peek_char", 2));
	assertTrue(engine.currentPredicate("xml_element_children", 2));
	assertTrue(engine.currentPredicate("read_clause", 1));
	assertTrue(engine.currentPredicate("number_chars", 2));
	assertTrue(engine.currentPredicate("told", 1));
	assertTrue(engine.currentPredicate("xml_element_child_by_name", 3));
	assertTrue(engine.currentPredicate("downcase", 1));
	assertTrue(engine.currentPredicate("atom_number", 2));
	assertTrue(engine.currentPredicate("listing", 1));
	assertTrue(engine.currentPredicate("memberchk", 2));
	assertTrue(engine.currentPredicate("keysort", 2));
	assertTrue(engine.currentPredicate("peek_code", 1));
	assertTrue(engine.currentPredicate("forall", 2));
	assertTrue(engine.currentPredicate("xml_document_encoding", 2));
	assertTrue(engine.currentPredicate("seeing", 1));
	assertTrue(engine.currentPredicate("wait", 1));
	assertTrue(engine.currentPredicate("$subsumes", 2));
	assertTrue(engine.currentPredicate("peek_char", 1));
	assertTrue(engine.currentPredicate("compound", 1));
	assertTrue(engine.currentPredicate("downcase_chars", 2));
	assertTrue(engine.currentPredicate("read_clause", 2));
	assertTrue(engine.currentPredicate("\\==", 2));
	assertTrue(engine.currentPredicate("*->", 2));
	assertTrue(engine.currentPredicate("current_stream", 3));
	assertTrue(engine.currentPredicate("nil", 1));
	assertTrue(engine.currentPredicate("tag", 4));
	assertTrue(engine.currentPredicate(";", 2));
	assertTrue(engine.currentPredicate("listing", 0));
	assertTrue(engine.currentPredicate("write_dir", 1));
	assertTrue(engine.currentPredicate("numlist", 3));
	assertTrue(engine.currentPredicate("write_elements", 2));
	assertTrue(engine.currentPredicate("=", 2));
	assertTrue(engine.currentPredicate("ignore", 1));
	assertTrue(engine.currentPredicate("xml_pi", 3));
	assertTrue(engine.currentPredicate("seeing", 2));
	assertTrue(engine.currentPredicate("predicate_property", 2));
	assertTrue(engine.currentPredicate("declare_extern", 3));
	assertTrue(engine.currentPredicate("access_file", 2));
	assertTrue(engine.currentPredicate("call_cleanup", 2));
	assertTrue(engine.currentPredicate("xml_doctype_id", 2));
	assertTrue(engine.currentPredicate("write", 2));
	assertTrue(engine.currentPredicate("downcase_char", 2));
	assertTrue(engine.currentPredicate("xml_append_child", 3));
	assertTrue(engine.currentPredicate("display", 1));
	assertTrue(engine.currentPredicate("false", 0));
	assertTrue(engine.currentPredicate("is_absolute_file_name", 1));
	assertTrue(engine.currentPredicate("write_doctype", 1));
	assertTrue(engine.currentPredicate("upcase", 1));
	assertTrue(engine.currentPredicate("merge_set", 3));
	assertTrue(engine.currentPredicate("ver", 2));
	assertTrue(engine.currentPredicate("file_base_name", 2));
	assertTrue(engine.currentPredicate("shell", 1));
	assertTrue(engine.currentPredicate("sort", 2));
	assertTrue(engine.currentPredicate("list_instances", 4));
	assertTrue(engine.currentPredicate("ms", 2));
	assertTrue(engine.currentPredicate("expand_term", 2));
	assertTrue(engine.currentPredicate("name", 2));
	assertTrue(engine.currentPredicate("downcase_char", 1));
	assertTrue(engine.currentPredicate("qsort", 3));
	assertTrue(engine.currentPredicate("shell", 2));
	assertTrue(engine.currentPredicate("xml_child", 1));
	assertTrue(engine.currentPredicate("append", 1));
	assertTrue(engine.currentPredicate("nl", 1));
	assertTrue(engine.currentPredicate("make_directory", 1));
	assertTrue(engine.currentPredicate("string_concat", 3));
	assertTrue(engine.currentPredicate("findall", 3));
	assertTrue(engine.currentPredicate("working_directory", 2));
	assertTrue(engine.currentPredicate("atom_length", 2));
	assertTrue(engine.currentPredicate("get_byte", 2));
	assertTrue(engine.currentPredicate("convert_time", 8));
	assertTrue(engine.currentPredicate("flatten2", 2));
	assertTrue(engine.currentPredicate("assertz", 1));
	assertTrue(engine.currentPredicate("xml_comment", 2));
	assertTrue(engine.currentPredicate("list_instances", 5));
	assertTrue(engine.currentPredicate("msort1", 5));
	assertTrue(engine.currentPredicate("option", 3));
	assertTrue(engine.currentPredicate("permutation", 3));
	assertTrue(engine.currentPredicate("load_files", 1));
	assertTrue(engine.currentPredicate("op", 3));
	assertTrue(engine.currentPredicate("xml_remove_child", 3));
	assertTrue(engine.currentPredicate("nonvar", 1));
	assertTrue(engine.currentPredicate("xml_text", 2));
	assertTrue(engine.currentPredicate("recorda", 1));
	assertTrue(engine.currentPredicate("char_code", 2));
	assertTrue(engine.currentPredicate("string_to_atom", 2));
	assertTrue(engine.currentPredicate("flatten1", 3));
	assertTrue(engine.currentPredicate("get_byte", 1));
	assertTrue(engine.currentPredicate("tab", 1));
	assertTrue(engine.currentPredicate("sort", 4));
	assertTrue(engine.currentPredicate("xml_object_value", 2));
	assertTrue(engine.currentPredicate("file_directory_name", 2));
	assertTrue(engine.currentPredicate("concat_atom", 2));
	assertTrue(engine.currentPredicate("list_is_free_of", 2));
	assertTrue(engine.currentPredicate("list_instances", 2));
	assertTrue(engine.currentPredicate("display", 2));
	assertTrue(engine.currentPredicate("permutation", 2));
	assertTrue(engine.currentPredicate("string_length", 2));
	assertTrue(engine.currentPredicate("sumlist", 3));
	assertTrue(engine.currentPredicate("invoke", 4));
	assertTrue(engine.currentPredicate("searchpath", 1));
	assertTrue(engine.currentPredicate("check_options", 1));
	assertTrue(engine.currentPredicate("halt", 0));
	assertTrue(engine.currentPredicate("sublist", 3));
	assertTrue(engine.currentPredicate("string_to_list", 2));
	assertTrue(engine.currentPredicate("set_stream_property", 2));
	assertTrue(engine.currentPredicate("put", 1));
	assertTrue(engine.currentPredicate("list_instances", 3));
	assertTrue(engine.currentPredicate("concat_atom", 3));
	assertTrue(engine.currentPredicate("term_variables", 2));
	assertTrue(engine.currentPredicate("=\\=", 2));
	assertTrue(engine.currentPredicate("get0", 1));
	assertTrue(engine.currentPredicate("delete_file", 1));
	assertTrue(engine.currentPredicate("symdiff", 3));
	assertTrue(engine.currentPredicate("subsumes_term", 2));
	assertTrue(engine.currentPredicate("time", 8));
	assertTrue(engine.currentPredicate("xml_object", 1));
	assertTrue(engine.currentPredicate("open", 4));
	assertTrue(engine.currentPredicate("recorded", 3));
	assertTrue(engine.currentPredicate("put", 2));
	assertTrue(engine.currentPredicate("subset", 2));
	assertTrue(engine.currentPredicate("read_term", 2));
	assertTrue(engine.currentPredicate("vars", 2));
	assertTrue(engine.currentPredicate("current_prolog_flag", 2));
	assertTrue(engine.currentPredicate("nextto", 3));
	assertTrue(engine.currentPredicate("dcg_rhs", 4));
	assertTrue(engine.currentPredicate("write_term", 2));
	assertTrue(engine.currentPredicate(">=", 2));
	assertTrue(engine.currentPredicate("check_handle", 2));
	assertTrue(engine.currentPredicate("unify_with_occurs_check", 2));
	assertTrue(engine.currentPredicate("set", 3));
	assertTrue(engine.currentPredicate("write_prolog", 2));
	assertTrue(engine.currentPredicate("flush_output", 0));
	assertTrue(engine.currentPredicate("close", 1));
	assertTrue(engine.currentPredicate("file_attributes", 7));
	assertTrue(engine.currentPredicate("unload", 1));
	assertTrue(engine.currentPredicate("replace_key_variables", 3));
	assertTrue(engine.currentPredicate("write_doctype", 2));
	assertTrue(engine.currentPredicate("get_char", 1));
	assertTrue(engine.currentPredicate("not", 1));
	assertTrue(engine.currentPredicate("put_char", 2));
	assertTrue(engine.currentPredicate("dcg_and", 3));
	assertTrue(engine.currentPredicate("xml_element", 4));
	assertTrue(engine.currentPredicate("abolish_files", 1));
	assertTrue(engine.currentPredicate("generate_num", 3));
	assertTrue(engine.currentPredicate("order", 3));
	assertTrue(engine.currentPredicate("=<", 2));
	assertTrue(engine.currentPredicate("write_term", 3));
	assertTrue(engine.currentPredicate("translate", 2));
	assertTrue(engine.currentPredicate("is_set", 1));
	assertTrue(engine.currentPredicate("telling", 2));
	assertTrue(engine.currentPredicate("xml_object_name", 2));
	assertTrue(engine.currentPredicate("jip_init_modules", 0));
	assertTrue(engine.currentPredicate("flush_output", 1));
	assertTrue(engine.currentPredicate("->", 2));
	assertTrue(engine.currentPredicate("merge", 5));
	assertTrue(engine.currentPredicate("rename_file", 2));
	assertTrue(engine.currentPredicate("free_variables", 2));
	assertTrue(engine.currentPredicate("close", 2));
	assertTrue(engine.currentPredicate("get", 1));
	assertTrue(engine.currentPredicate("absolute_file_name", 3));
	assertTrue(engine.currentPredicate("current_input", 1));
	assertTrue(engine.currentPredicate("writeq", 2));
	assertTrue(engine.currentPredicate("get_methods", 2));
	assertTrue(engine.currentPredicate("xml_cdata", 2));
	assertTrue(engine.currentPredicate("exists_file", 1));
	assertTrue(engine.currentPredicate("upcase_char", 2));
	assertTrue(engine.currentPredicate("list", 1));
	assertTrue(engine.currentPredicate("recorded", 1));
	assertTrue(engine.currentPredicate("@<", 2));
	assertTrue(engine.currentPredicate("setup_call_cleanup", 3));
	assertTrue(engine.currentPredicate("@=", 2));
	assertTrue(engine.currentPredicate("skip", 2));
	assertTrue(engine.currentPredicate("catch", 3));
	assertTrue(engine.currentPredicate("@>", 2));
	assertTrue(engine.currentPredicate("seek", 4));
	assertTrue(engine.currentPredicate("numbervars", 3));
	assertTrue(engine.currentPredicate("number_codes", 2));
	assertTrue(engine.currentPredicate("combine", 3));
	assertTrue(engine.currentPredicate("list_or_partial_list", 1));
	assertTrue(engine.currentPredicate("cyclic_term", 1));
	assertTrue(engine.currentPredicate("absolute_file_name", 2));
	assertTrue(engine.currentPredicate("file_name_extension", 2));
	assertTrue(engine.currentPredicate("append", 2));
	assertTrue(engine.currentPredicate("xml_document_root", 2));
	assertTrue(engine.currentPredicate("same_file", 2));
	assertTrue(engine.currentPredicate("get", 2));
	assertTrue(engine.currentPredicate("statistics", 0));
	assertTrue(engine.currentPredicate("var_member_chk", 2));
	assertTrue(engine.currentPredicate("write_canonical", 2));
	assertTrue(engine.currentPredicate("cd", 1));
	assertTrue(engine.currentPredicate("writeq", 1));
	assertTrue(engine.currentPredicate("upcase_atom", 2));
	assertTrue(engine.currentPredicate("make_connects", 4));
	assertTrue(engine.currentPredicate("once", 1));
	assertTrue(engine.currentPredicate("write_prolog", 1));
	assertTrue(engine.currentPredicate("convert_chars", 2));
	assertTrue(engine.currentPredicate("skip", 1));
	assertTrue(engine.currentPredicate("xml_attribute", 3));
	assertTrue(engine.currentPredicate("is_list", 1));
	assertTrue(engine.currentPredicate("append", 3));
	assertTrue(engine.currentPredicate("arg", 3));
	assertTrue(engine.currentPredicate("remove_duplicates", 2));
	assertTrue(engine.currentPredicate("true", 0));
	assertTrue(engine.currentPredicate("get", 3));
	assertTrue(engine.currentPredicate("read", 1));
	assertTrue(engine.currentPredicate("set_stream_properties", 2));
	assertTrue(engine.currentPredicate("upcase_chars", 2));
	assertTrue(engine.currentPredicate("<!", 0));
	assertTrue(engine.currentPredicate("set_stream_position", 2));
	assertTrue(engine.currentPredicate("time", 4));
	assertTrue(engine.currentPredicate("delete_directory", 1));
	assertTrue(engine.currentPredicate("*/", 0));
	assertTrue(engine.currentPredicate("print", 1));
	assertTrue(engine.currentPredicate("select", 3));
	assertTrue(engine.currentPredicate("split_command", 2));
	assertTrue(engine.currentPredicate("xml_element_attribute_by_name", 3));
	assertTrue(engine.currentPredicate("\\=", 2));
	assertTrue(engine.currentPredicate("concordant_subset", 5));
	assertTrue(engine.currentPredicate("create_object", 3));
	assertTrue(engine.currentPredicate("xml_element_attributes", 2));
	assertTrue(engine.currentPredicate("free_variables", 5));
	assertTrue(engine.currentPredicate("current_output", 1));
	assertTrue(engine.currentPredicate("char_atom", 2));
	assertTrue(engine.currentPredicate("read", 2));
	assertTrue(engine.currentPredicate("get_class", 2));
	assertTrue(engine.currentPredicate("xml_write_document", 2));
	assertTrue(engine.currentPredicate("numlist_", 3));
	assertTrue(engine.currentPredicate("halve", 4));
	assertTrue(engine.currentPredicate("@=<", 2));
	assertTrue(engine.currentPredicate("exists_directory", 1));
	assertTrue(engine.currentPredicate("flatten", 2));
	assertTrue(engine.currentPredicate("time", 5));
	assertTrue(engine.currentPredicate("sub_atom", 5));
	assertTrue(engine.currentPredicate("compare", 5));
	assertTrue(engine.currentPredicate("xml_remove_attribute", 3));
	assertTrue(engine.currentPredicate("bsort", 3));
	assertTrue(engine.currentPredicate("options", 3));
	assertTrue(engine.currentPredicate("print", 2));
	assertTrue(engine.currentPredicate("list_to_set_", 2));
	assertTrue(engine.currentPredicate("char", 1));
	assertTrue(engine.currentPredicate("concordant_subset", 4));
	assertTrue(engine.currentPredicate("merge2", 4));
	assertTrue(engine.currentPredicate("set_prolog_flag", 2));
	assertTrue(engine.currentPredicate("see", 1));
	assertTrue(engine.currentPredicate("string", 1));
	assertTrue(engine.currentPredicate("union", 3));
	assertTrue(engine.currentPredicate("current_stream", 1));
	assertTrue(engine.currentPredicate("sleep", 1));
	assertTrue(engine.currentPredicate("xml_write_document", 1));
	assertTrue(engine.currentPredicate("get_time", 1));
	assertTrue(engine.currentPredicate("told", 0));
	assertTrue(engine.currentPredicate("seen", 1));
	assertTrue(engine.currentPredicate("@>=", 2));
	assertTrue(engine.currentPredicate("size_file", 2));
	assertTrue(engine.currentPredicate("get_constructors", 2));
	assertTrue(engine.currentPredicate("callable", 1));
	assertTrue(engine.currentPredicate("compare", 4));
	assertTrue(engine.currentPredicate("stream_property", 2));
	assertTrue(engine.currentPredicate("concordant_subset", 3));
	assertTrue(engine.currentPredicate("dir", 0));
	assertTrue(engine.currentPredicate("\\+", 1));
	assertTrue(engine.currentPredicate("atom_concat", 3));
	assertTrue(engine.currentPredicate("checklist", 2));
	assertTrue(engine.currentPredicate("explicit_binding", 4));
	assertTrue(engine.currentPredicate("xml_read_document", 1));
	assertTrue(engine.currentPredicate("writeln", 2));
	assertTrue(engine.currentPredicate("xml_document", 5));
	assertTrue(engine.currentPredicate("apply", 2));
	assertTrue(engine.currentPredicate("see", 2));
	assertTrue(engine.currentPredicate("put_code", 1));
	assertTrue(engine.currentPredicate("set_input", 1));
	assertTrue(engine.currentPredicate("reverse", 4));
	assertTrue(engine.currentPredicate("write_attributes", 2));
	assertTrue(engine.currentPredicate("last", 2));
	assertTrue(engine.currentPredicate("get_code", 2));
	assertTrue(engine.currentPredicate("atom_codes", 2));
	assertTrue(engine.currentPredicate("put_byte", 2));
	assertTrue(engine.currentPredicate("date", 3));
	assertTrue(engine.currentPredicate("save_instances", 2));
	assertTrue(engine.currentPredicate("xml_doctype", 4));
	assertTrue(engine.currentPredicate("include", 1));
	assertTrue(engine.currentPredicate("write_id", 2));
	assertTrue(engine.currentPredicate("seen", 0));
	assertTrue(engine.currentPredicate("current_predicate", 2));
	assertTrue(engine.currentPredicate("telling", 1));
	assertTrue(engine.currentPredicate("repeat", 1));
	assertTrue(engine.currentPredicate("prolog_flag", 2));
	assertTrue(engine.currentPredicate("peek_byte", 2));
	assertTrue(engine.currentPredicate("downcase_atom", 2));
	assertTrue(engine.currentPredicate("set_stream", 2));
	assertTrue(engine.currentPredicate("dir", 1));
	assertTrue(engine.currentPredicate("xml_read_document", 2));
	assertTrue(engine.currentPredicate("read_term", 3));
	assertTrue(engine.currentPredicate("delete", 3));
	assertTrue(engine.currentPredicate("set_output", 1));
	assertTrue(engine.currentPredicate("open", 3));
	assertTrue(engine.currentPredicate("time", 7));
	assertTrue(engine.currentPredicate("writeln", 1));
	assertTrue(engine.currentPredicate("write_attributes", 1));
	assertTrue(engine.currentPredicate("call", 1));
	assertTrue(engine.currentPredicate("get0", 2));
	assertTrue(engine.currentPredicate("get_code", 1));
	assertTrue(engine.currentPredicate("put_char", 1));
	assertTrue(engine.currentPredicate("xml_object_type", 2));
	assertTrue(engine.currentPredicate("get_char", 2));
	assertTrue(engine.currentPredicate("date", 4));
	assertTrue(engine.currentPredicate("free_variables", 4));
	assertTrue(engine.currentPredicate("write_id", 1));
	assertTrue(engine.currentPredicate("merge", 3));
	assertTrue(engine.currentPredicate("repeat", 0));
	assertTrue(engine.currentPredicate("current_predicate", 1));
	assertTrue(engine.currentPredicate("peek_byte", 1));
	assertTrue(engine.currentPredicate("put_byte", 1));
	assertTrue(engine.currentPredicate("nth0", 3));
	assertTrue(engine.currentPredicate("nth1", 3));

    }

    @Test
    public final void testCurrentOperator() {

	assertTrue(engine.currentOperator(700, "xfx", ">"));
	assertTrue(engine.currentOperator(1000, "xfy", ","));
	assertTrue(engine.currentOperator(700, "xfx", "=.."));
	assertTrue(engine.currentOperator(700, "xfx", "=<"));
	assertTrue(engine.currentOperator(900, "fy", "nospy"));
	assertTrue(engine.currentOperator(500, "yfx", "/\\"));
	assertTrue(engine.currentOperator(700, "xfx", "=="));
	assertTrue(engine.currentOperator(600, "xfy", ":"));
	assertTrue(engine.currentOperator(700, "xfx", ">="));
	assertTrue(engine.currentOperator(1200, "fx", ":-"));
	assertTrue(engine.currentOperator(500, "yfx", "\\/"));
	assertTrue(engine.currentOperator(700, "xfx", "\\="));
	assertTrue(engine.currentOperator(901, "fy", "{"));
	assertTrue(engine.currentOperator(400, "yfx", ">>"));
	assertTrue(engine.currentOperator(1050, "xfy", "->"));
	assertTrue(engine.currentOperator(300, "xfx", "rem"));
	assertTrue(engine.currentOperator(400, "yfx", "/"));
	assertTrue(engine.currentOperator(1200, "xfx", "-->"));
	assertTrue(engine.currentOperator(700, "xfx", "=:="));
	assertTrue(engine.currentOperator(700, "xfx", "\\=="));
	assertTrue(engine.currentOperator(1150, "fx", "meta_predicate"));
	assertTrue(engine.currentOperator(700, "xfx", "=\\="));
	assertTrue(engine.currentOperator(300, "xfx", "mod"));
	assertTrue(engine.currentOperator(500, "yfx", "-"));
	assertTrue(engine.currentOperator(400, "yfx", "*"));
	assertTrue(engine.currentOperator(1100, "xfy", ";"));
	assertTrue(engine.currentOperator(500, "yfx", "+"));
	assertTrue(engine.currentOperator(200, "xfy", "^"));
	assertTrue(engine.currentOperator(700, "xfx", "@>="));
	assertTrue(engine.currentOperator(700, "xfx", "is"));
	assertTrue(engine.currentOperator(700, "xfx", "@=<"));
	assertTrue(engine.currentOperator(400, "fx", "cd"));
	assertTrue(engine.currentOperator(1050, "xfy", "*->"));
	assertTrue(engine.currentOperator(1150, "fx", "module_transparent"));
	assertTrue(engine.currentOperator(1150, "fx", "multifile"));
	assertTrue(engine.currentOperator(400, "yfx", "xor"));
	assertTrue(engine.currentOperator(400, "yfx", "<<"));
	assertTrue(engine.currentOperator(1150, "fx", "dynamic"));
	assertTrue(engine.currentOperator(500, "fx", "\\"));
	assertTrue(engine.currentOperator(200, "xfx", "**"));
	assertTrue(engine.currentOperator(1200, "fx", "?-"));
	assertTrue(engine.currentOperator(900, "fy", "spy"));
	assertTrue(engine.currentOperator(700, "xfx", "="));
	assertTrue(engine.currentOperator(700, "xfx", "@<"));
	assertTrue(engine.currentOperator(700, "xfx", "<"));
	assertTrue(engine.currentOperator(400, "yfx", "//"));
	assertTrue(engine.currentOperator(700, "xfx", "@>"));
	assertTrue(engine.currentOperator(900, "xf", "}"));
	assertTrue(engine.currentOperator(700, "xfx", "@="));
	assertTrue(engine.currentOperator(900, "fy", "not"));
	assertTrue(engine.currentOperator(900, "fy", "\\+"));

    }

    @Test
    public final void testGetVersion() {
	assertEquals(JIPEngine.getVersion(), engine.getVersion());
    }

    @Test
    public final void testDispose() {
	engine.dispose();
	assertTrue(engine.isProgramEmpty());
	assertEquals(0, engine.getProgramSize());
    }

}
