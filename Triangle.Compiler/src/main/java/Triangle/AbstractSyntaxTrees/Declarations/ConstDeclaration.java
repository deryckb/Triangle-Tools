/*
 * @(#)ConstDeclaration.java                        2.1 2003/10/07
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
import Triangle.AbstractSyntaxTrees.Terminals.Identifier;
import Triangle.AbstractSyntaxTrees.Visitors.DeclarationVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class ConstDeclaration extends Declaration {

	public ConstDeclaration(Identifier iAST, Expression eAST, SourcePosition position) {
		super(position);
		I = iAST;
		E = eAST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitConstDeclaration(this, arg);
	}

	public final Identifier I;
	public final Expression E;
}
