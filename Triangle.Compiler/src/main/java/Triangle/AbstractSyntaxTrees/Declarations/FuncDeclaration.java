/*
 * @(#)FuncDeclaration.java                        2.1 2003/10/07
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

import Triangle.AbstractSyntaxTrees.Expressions.Expression;
import Triangle.AbstractSyntaxTrees.Formals.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Terminals.Identifier;
import Triangle.AbstractSyntaxTrees.Types.TypeDenoter;
import Triangle.AbstractSyntaxTrees.Visitors.DeclarationVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class FuncDeclaration extends Declaration {

	public FuncDeclaration(Identifier iAST, FormalParameterSequence fpsAST, TypeDenoter tAST, Expression eAST,
			SourcePosition position) {
		super(position);
		I = iAST;
		FPS = fpsAST;
		T = tAST;
		E = eAST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitFuncDeclaration(this, arg);
	}

	public final Identifier I;
	public final FormalParameterSequence FPS;
	public TypeDenoter T;
	public final Expression E;
}
