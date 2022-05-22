/*
 * @(#)CharacterExpression.java                        2.1 2003/10/07
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

import Triangle.AbstractSyntaxTrees.Terminals.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Visitors.ExpressionVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class CharacterExpression extends Expression {

	public CharacterExpression(CharacterLiteral clAST, SourcePosition position) {
		super(position);
		CL = clAST;
	}

	public <TArg, TResult> TResult visit(ExpressionVisitor<TArg, TResult> v, TArg arg) {
		return v.visitCharacterExpression(this, arg);
	}

	public final CharacterLiteral CL;
}
