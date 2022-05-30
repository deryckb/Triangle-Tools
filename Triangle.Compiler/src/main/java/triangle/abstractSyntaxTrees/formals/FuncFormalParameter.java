/*
 * @(#)FuncFormalParameter.java                2.1 2003/10/07
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

import triangle.abstractSyntaxTrees.declarations.FunctionDeclaration;
import triangle.abstractSyntaxTrees.terminals.Identifier;
import triangle.abstractSyntaxTrees.types.TypeDenoter;
import triangle.abstractSyntaxTrees.visitors.DeclarationVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class FuncFormalParameter extends FormalParameter implements FunctionDeclaration {

	public FuncFormalParameter(Identifier iAST, FormalParameterSequence fpsAST, TypeDenoter tAST,
			SourcePosition position) {
		super(position);
		I = iAST;
		FPS = fpsAST;
		T = tAST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitFuncFormalParameter(this, arg);
	}

	@Override
	public FormalParameterSequence getFormals() {
		return FPS;
	}
	
	@Override
	public TypeDenoter getType() {
		return T;
	}
	
	@Override
	public boolean equals(Object fpAST) {
		if (fpAST instanceof FuncFormalParameter) {
			FuncFormalParameter ffpAST = (FuncFormalParameter) fpAST;
			return FPS.equals(ffpAST.FPS) && T.equals(ffpAST.T);
		} else
			return false;
	}

	public final Identifier I;
	public final FormalParameterSequence FPS;
	public TypeDenoter T;
}
