#!/usr/bin/bash
java -classpath "$(dirname "$(pwd)")/lib/*" org.prolobjectlink.prolog.jiprolog.JiPrologConsole ${1+"$@"}