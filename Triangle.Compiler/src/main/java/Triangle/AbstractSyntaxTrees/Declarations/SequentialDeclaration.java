/*
 * @(#)SequentialDeclaration.java                2.1 2003/10/07
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

import Triangle.AbstractSyntaxTrees.Visitors.DeclarationVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class SequentialDeclaration extends Declaration {

	public SequentialDeclaration(Declaration d1AST, Declaration d2AST, SourcePosition position) {
		super(position);
		D1 = d1AST;
		D2 = d2AST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitSequentialDeclaration(this, arg);
	}

	public final Declaration D1, D2;
}
