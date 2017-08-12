package org.logicware.jpi;

import java.util.HashMap;
import java.util.Map;

import org.logicware.jpi.jiprolog.JiPrologConverter;

import com.ugos.jiprolog.engine.JIPTerm;

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

	protected static final PrologConverter<JIPTerm> converter = PrologConverterFactory.createPrologAdapter(JiPrologConverter.class);
	protected static final PrologProvider<JIPTerm> provider = converter.createProvider();

	protected PrologVariable x = provider.newPrologVariable("X");
	protected PrologVariable y = provider.newPrologVariable("Y");
	protected PrologVariable z = provider.newPrologVariable("Z");

	protected PrologVariable name = provider.newPrologVariable("Name");
	protected PrologVariable dpto = provider.newPrologVariable("Dpto");
	protected PrologVariable scale = provider.newPrologVariable("Scale");
	protected PrologVariable dptoName = provider.newPrologVariable("DepartmentName");
	protected PrologVariable money = provider.newPrologVariable("Money");

	protected PrologAtom pam = provider.newPrologAtom("pam");
	protected PrologAtom bob = provider.newPrologAtom("bob");
	protected PrologAtom tom = provider.newPrologAtom("tom");
	protected PrologAtom liz = provider.newPrologAtom("liz");
	protected PrologAtom ann = provider.newPrologAtom("ann");
	protected PrologAtom pat = provider.newPrologAtom("pat");
	protected PrologAtom jim = provider.newPrologAtom("m");

	protected PrologInteger zero = provider.newPrologInteger();
	protected PrologInteger one = provider.newPrologInteger(1);
	protected PrologInteger two = provider.newPrologInteger(2);
	protected PrologInteger three = provider.newPrologInteger(3);
	protected PrologInteger four = provider.newPrologInteger(4);
	protected PrologInteger five = provider.newPrologInteger(5);
	protected PrologInteger six = provider.newPrologInteger(6);
	protected PrologInteger seven = provider.newPrologInteger(7);
	protected PrologInteger eight = provider.newPrologInteger(8);
	protected PrologInteger nine = provider.newPrologInteger(9);
	protected PrologInteger ten = provider.newPrologInteger(10);

	protected PrologAtom cat = provider.newPrologAtom("cat");
	protected PrologAtom bear = provider.newPrologAtom("bear");
	protected PrologAtom elephant = provider.newPrologAtom("elephant");

	protected PrologAtom mcardon = provider.newPrologAtom("mcardon");
	protected PrologAtom treeman = provider.newPrologAtom("treeman");
	protected PrologAtom chapman = provider.newPrologAtom("chapman");
	protected PrologAtom claessen = provider.newPrologAtom("claessen");
	protected PrologAtom petersen = provider.newPrologAtom("petersen");
	protected PrologAtom cohn = provider.newPrologAtom("cohn");
	protected PrologAtom duffy = provider.newPrologAtom("duffy");

	protected PrologAtom board = provider.newPrologAtom("board");
	protected PrologAtom human_resources = provider.newPrologAtom("human_resources");
	protected PrologAtom production = provider.newPrologAtom("production");
	protected PrologAtom technical_services = provider.newPrologAtom("technical_services");
	protected PrologAtom administration = provider.newPrologAtom("administration");

	protected PrologInteger thousand = provider.newPrologInteger(1000);
	protected PrologInteger thousandFiveHundred = provider.newPrologInteger(1500);
	protected PrologInteger twoThousand = provider.newPrologInteger(2000);
	protected PrologInteger twoThousandFiveHundred = provider.newPrologInteger(2500);
	protected PrologInteger threeThousand = provider.newPrologInteger(3000);
	protected PrologInteger threeThousandFiveHundred = provider.newPrologInteger(3500);
	protected PrologTerm fourThousand = provider.newPrologInteger(4000);
	protected PrologTerm fourThousandFiveHundred = provider.newPrologInteger(4500);
	protected PrologTerm fiveThousand = provider.newPrologInteger(5000);

	protected PrologTerm[] expecteds0 = new PrologTerm[] { mcardon, one, five, board, threeThousand };
	protected PrologTerm[] expecteds1 = new PrologTerm[] { treeman, two, three, human_resources, twoThousand };
	protected PrologTerm[] expecteds2 = new PrologTerm[] { chapman, one, two, board, thousandFiveHundred };
	protected PrologTerm[] expecteds3 = new PrologTerm[] { claessen, four, one, technical_services, thousand };
	protected PrologTerm[] expecteds4 = new PrologTerm[] { petersen, five, eight, administration, fourThousandFiveHundred };
	protected PrologTerm[] expecteds5 = new PrologTerm[] { cohn, one, seven, board, fourThousand };
	protected PrologTerm[] expecteds6 = new PrologTerm[] { duffy, one, nine, board, fiveThousand };

	protected PrologTerm[][] solution = new PrologTerm[7][5];

	protected Map<String, PrologTerm> solutionMap;
	protected Map<String, PrologTerm>[] allSolutionMap;

	protected Map<String, PrologTerm>[] famillyAllSolutionMap;
	protected Map<String, PrologTerm> famillySolutionMap = new HashMap<String, PrologTerm>();

}
