package io.github.prolobjectlink.prolog.jiprolog;

import io.github.prolobjectlink.prolog.AbstractProgram;
import io.github.prolobjectlink.prolog.PrologClauses;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologProgram;

class JiPrologProgram extends AbstractProgram implements PrologProgram {

	JiPrologProgram(PrologEngine engine) {
		super(engine);
	}

	public PrologClauses newClauses(String functor, int arity) {
		return new JiPrologClauses(functor, arity);
	}

}
