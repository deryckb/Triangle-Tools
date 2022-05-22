/*
 * @(#)TypeDeclaration.java                        2.1 2003/10/07
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

package Triangle.AbstractSyntaxTrees.Types;

import Triangle.AbstractSyntaxTrees.Declarations.Declaration;
import Triangle.AbstractSyntaxTrees.Terminals.Identifier;
import Triangle.AbstractSyntaxTrees.Visitors.DeclarationVisitor;
import Triangle.SyntacticAnalyzer.SourcePosition;

public class TypeDeclaration extends Declaration {

	public TypeDeclaration(Identifier iAST, TypeDenoter tAST, SourcePosition position) {
		super(position);
		I = iAST;
		T = tAST;
	}

	public <TArg, TResult> TResult visit(DeclarationVisitor<TArg, TResult> v, TArg arg) {
		return v.visitTypeDeclaration(this, arg);
	}

	public final Identifier I;
	public TypeDenoter T;
}
