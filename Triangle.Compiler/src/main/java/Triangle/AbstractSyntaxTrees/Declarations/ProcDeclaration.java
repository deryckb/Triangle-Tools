/*
 * @(#)ProcDeclaration.java                        2.1 2003/10/07
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

package Triangle.AbstractSyntaxTrees.Declarations;

import Triangle.AbstractSyntaxTrees.Commands.Command;
import Triangle.AbstractSyntaxTrees.Formals.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Terminals.Identifier;
import Triangle.AbstractSyntaxTrees.Visitors.DeclarationVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class ProcDeclaration extends Declaration {

	public ProcDeclaration(Identifier iAST, FormalParameterSequence fpsAST, Command cAST, SourcePosition position) {
		super(position);
		I = iAST;
		FPS = fpsAST;
		C = cAST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitProcDeclaration(this, arg);
	}

	public final Identifier I;
	public final FormalParameterSequence FPS;
	public final Command C;
}
