/*
 * @(#)BinaryOperatorDeclaration.java                2.1 2003/10/07
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

import Triangle.AbstractSyntaxTrees.Terminals.Operator;
import Triangle.AbstractSyntaxTrees.Types.TypeDenoter;
import Triangle.AbstractSyntaxTrees.Visitors.DeclarationVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class BinaryOperatorDeclaration extends Declaration {

	public BinaryOperatorDeclaration(Operator oAST, TypeDenoter arg1AST, TypeDenoter arg2AST, TypeDenoter resultAST,
			SourcePosition position) {
		super(position);
		O = oAST;
		ARG1 = arg1AST;
		ARG2 = arg2AST;
		RES = resultAST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitBinaryOperatorDeclaration(this, arg);
	}

	public final Operator O;
	public final TypeDenoter ARG1, ARG2, RES;
}
