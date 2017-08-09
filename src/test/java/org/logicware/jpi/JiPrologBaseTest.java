package org.logicware.jpi;

import java.util.HashMap;
import java.util.Map;

import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.jiprolog.JiPrologAtom;
import org.logicware.jpi.jiprolog.JiPrologInteger;
import org.logicware.jpi.jiprolog.JiPrologTerm;
import org.logicware.jpi.jiprolog.JiPrologVariable;

public abstract class JiPrologBaseTest {

	protected String male = "male";
	protected String parent = "parent";
	protected String female = "female";
	protected String mother = "mother";
	protected String sister = "sister";
	protected String different = "different";
	protected String offspring = "offspring";
	protected String grandparent = "grandparent";
	protected String predecessor = "predecessor";

	protected String salary = "salary";
	protected String employee = "employee";
	protected String department = "department";

	protected JiPrologVariable x = new JiPrologVariable("X");
	protected JiPrologVariable y = new JiPrologVariable("Y");
	protected JiPrologVariable z = new JiPrologVariable("Z");

	protected JiPrologVariable name = new JiPrologVariable("Name");
	protected JiPrologVariable dpto = new JiPrologVariable("Dpto");
	protected JiPrologVariable scale = new JiPrologVariable("Scale");
	protected JiPrologVariable dptoName = new JiPrologVariable("DepartmentName");
	protected JiPrologVariable money = new JiPrologVariable("Money");

	protected JiPrologAtom pam = new JiPrologAtom("pam");
	protected JiPrologAtom bob = new JiPrologAtom("bob");
	protected JiPrologAtom tom = new JiPrologAtom("tom");
	protected JiPrologAtom liz = new JiPrologAtom("liz");
	protected JiPrologAtom ann = new JiPrologAtom("ann");
	protected JiPrologAtom pat = new JiPrologAtom("pat");
	protected JiPrologAtom jim = new JiPrologAtom("jim");

	protected JiPrologInteger zero = new JiPrologInteger();
	protected JiPrologInteger one = new JiPrologInteger(1);
	protected JiPrologInteger two = new JiPrologInteger(2);
	protected JiPrologInteger three = new JiPrologInteger(3);
	protected JiPrologInteger four = new JiPrologInteger(4);
	protected JiPrologInteger five = new JiPrologInteger(5);
	protected JiPrologInteger six = new JiPrologInteger(6);
	protected JiPrologInteger seven = new JiPrologInteger(7);
	protected JiPrologInteger eight = new JiPrologInteger(8);
	protected JiPrologInteger nine = new JiPrologInteger(9);
	protected JiPrologInteger ten = new JiPrologInteger(10);

	protected JiPrologAtom cat = new JiPrologAtom("cat");
	protected JiPrologAtom bear = new JiPrologAtom("bear");
	protected JiPrologAtom elephant = new JiPrologAtom("elephant");

	protected JiPrologAtom mcardon = new JiPrologAtom("mcardon");
	protected JiPrologAtom treeman = new JiPrologAtom("treeman");
	protected JiPrologAtom chapman = new JiPrologAtom("chapman");
	protected JiPrologAtom claessen = new JiPrologAtom("claessen");
	protected JiPrologAtom petersen = new JiPrologAtom("petersen");
	protected JiPrologAtom cohn = new JiPrologAtom("cohn");
	protected JiPrologAtom duffy = new JiPrologAtom("duffy");

	protected JiPrologAtom board = new JiPrologAtom("board");
	protected JiPrologAtom human_resources = new JiPrologAtom("human_resources");
	protected JiPrologAtom production = new JiPrologAtom("production");
	protected JiPrologAtom technical_services = new JiPrologAtom("technical_services");
	protected JiPrologAtom administration = new JiPrologAtom("administration");

	protected JiPrologInteger thousand = new JiPrologInteger(1000);
	protected JiPrologInteger thousandFiveHundred = new JiPrologInteger(1500);
	protected JiPrologInteger twoThousand = new JiPrologInteger(2000);
	protected JiPrologInteger twoThousandFiveHundred = new JiPrologInteger(2500);
	protected JiPrologInteger threeThousand = new JiPrologInteger(3000);
	protected JiPrologInteger threeThousandFiveHundred = new JiPrologInteger(3500);
	protected JiPrologTerm fourThousand = new JiPrologInteger(4000);
	protected JiPrologTerm fourThousandFiveHundred = new JiPrologInteger(4500);
	protected JiPrologTerm fiveThousand = new JiPrologInteger(5000);

	protected JiPrologTerm[] expecteds0 = new JiPrologTerm[] { mcardon, one, five, board, threeThousand };
	protected JiPrologTerm[] expecteds1 = new JiPrologTerm[] { treeman, two, three, human_resources, twoThousand };
	protected JiPrologTerm[] expecteds2 = new JiPrologTerm[] { chapman, one, two, board, thousandFiveHundred };
	protected JiPrologTerm[] expecteds3 = new JiPrologTerm[] { claessen, four, one, technical_services, thousand };
	protected JiPrologTerm[] expecteds4 = new JiPrologTerm[] { petersen, five, eight, administration, fourThousandFiveHundred };
	protected JiPrologTerm[] expecteds5 = new JiPrologTerm[] { cohn, one, seven, board, fourThousand };
	protected JiPrologTerm[] expecteds6 = new JiPrologTerm[] { duffy, one, nine, board, fiveThousand };

	protected JiPrologTerm[][] solution = new JiPrologTerm[7][5];

	protected Map<String, PrologTerm> solutionMap;
	protected Map<String, PrologTerm>[] allSolutionMap;

	protected Map<String, PrologTerm>[] famillyAllSolutionMap;
	protected Map<String, PrologTerm> famillySolutionMap = new HashMap<String, PrologTerm>();

}
