/*
 * @(#)SingleActualParameterSequence.java                2.1 2003/10/07
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

package triangle.abstractSyntaxTrees.actuals;

import triangle.abstractSyntaxTrees.visitors.ActualParameterSequenceVisitor;
import triangle.syntacticAnalyzer.SourcePosition;

public class SingleActualParameterSequence extends ActualParameterSequence {

	public SingleActualParameterSequence(ActualParameter apAST, SourcePosition position) {
		super(position);
		AP = apAST;
	}

	public <TArg, TResult> TResult visit(ActualParameterSequenceVisitor<TArg, TResult> v, TArg arg) {
		return v.visitSingleActualParameterSequence(this, arg);
	}

	public final ActualParameter AP;
}
