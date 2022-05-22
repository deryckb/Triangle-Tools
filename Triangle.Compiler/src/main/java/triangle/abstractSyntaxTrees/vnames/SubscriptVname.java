/*
 * @(#)SubscriptVname.java                        2.1 2003/10/07
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

package triangle.abstractSyntaxTrees.vnames;

import triangle.abstractSyntaxTrees.expressions.Expression;
import triangle.abstractSyntaxTrees.visitors.VnameVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class SubscriptVname extends Vname {

	public SubscriptVname(Vname vAST, Expression eAST, SourcePosition position) {
		super(position);
		V = vAST;
		E = eAST;
	}

	public <TArg, TResult> TResult visit(VnameVisitor<TArg, TResult> v, TArg arg) {
		return v.visitSubscriptVname(this, arg);
	}

	public final Expression E;
	public final Vname V;
}
