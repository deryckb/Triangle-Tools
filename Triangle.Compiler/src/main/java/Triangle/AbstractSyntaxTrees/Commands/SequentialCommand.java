/*
 * @(#)SequentialCommand.java                        2.1 2003/10/07
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

package Triangle.AbstractSyntaxTrees.Commands;

import Triangle.AbstractSyntaxTrees.Visitors.CommandVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class SequentialCommand extends Command {

	public SequentialCommand(Command c1AST, Command c2AST, SourcePosition position) {
		super(position);
		C1 = c1AST;
		C2 = c2AST;
	}

	public <TArg, TResult> TResult visit(CommandVisitor<TArg, TResult> v, TArg arg) {
		return v.visitSequentialCommand(this, arg);
	}

	public final Command C1, C2;
}
