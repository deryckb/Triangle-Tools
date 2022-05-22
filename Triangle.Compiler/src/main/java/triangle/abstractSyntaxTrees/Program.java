/*
 * @(#)Program.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package triangle.abstractSyntaxTrees;

import triangle.abstractSyntaxTrees.commands.Command;
import triangle.abstractSyntaxTrees.visitors.ProgramVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class Program extends AbstractSyntaxTree {

	public Program(Command cAST, SourcePosition position) {
		super(position);
		C = cAST;
	}

	public Command C;

	public <TArg, TResult> TResult visit(ProgramVisitor<TArg, TResult> visitor, TArg arg) {
		return visitor.visitProgram(this, arg);
	}

	public <TResult> TResult visit(ProgramVisitor<Void, TResult> visitor) {
		return visit(visitor, null);
	}
}
