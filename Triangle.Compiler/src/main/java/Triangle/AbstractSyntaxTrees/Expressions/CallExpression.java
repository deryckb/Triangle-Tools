/*
 * @(#)CallExpression.java                        2.1 2003/10/07
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

package Triangle.AbstractSyntaxTrees.Expressions;

import Triangle.AbstractSyntaxTrees.Actuals.ActualParameterSequence;
import Triangle.AbstractSyntaxTrees.Terminals.Identifier;
import Triangle.AbstractSyntaxTrees.Visitors.ExpressionVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class CallExpression extends Expression {

	public CallExpression(Identifier iAST, ActualParameterSequence apsAST, SourcePosition position) {
		super(position);
		I = iAST;
		APS = apsAST;
	}

	public <TArg, TResult> TResult visit(ExpressionVisitor<TArg, TResult> v, TArg arg) {
		return v.visitCallExpression(this, arg);
	}

	public final Identifier I;
	public final ActualParameterSequence APS;
}
