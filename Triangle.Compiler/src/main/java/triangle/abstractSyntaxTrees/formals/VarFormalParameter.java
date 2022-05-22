/*
 * @(#)ValFormalParameter.java                        2.1 2003/10/07
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

package triangle.abstractSyntaxTrees.formals;

import triangle.abstractSyntaxTrees.terminals.Identifier;
import triangle.abstractSyntaxTrees.types.TypeDenoter;
import triangle.abstractSyntaxTrees.visitors.DeclarationVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class VarFormalParameter extends FormalParameter {

	public VarFormalParameter(Identifier iAST, TypeDenoter tAST, SourcePosition position) {
		super(position);
		I = iAST;
		T = tAST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitVarFormalParameter(this, arg);
	}

	@Override
	public boolean equals(Object fpAST) {
		if (fpAST instanceof VarFormalParameter) {
			VarFormalParameter vfpAST = (VarFormalParameter) fpAST;
			return T.equals(vfpAST.T);
		} else {
			return false;
		}
	}

	public final Identifier I;
	public TypeDenoter T;
}
