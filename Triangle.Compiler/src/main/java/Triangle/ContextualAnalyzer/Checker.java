/*
 * @(#)Checker.java                        2.1 2003/10/07
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

package Triangle.ContextualAnalyzer;

import Triangle.ErrorReporter;
import Triangle.StdEnvironment;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.AbstractSyntaxTrees.Actuals.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.Actuals.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.Actuals.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.Actuals.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.Actuals.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.Actuals.SingleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.Actuals.VarActualParameter;
import Triangle.AbstractSyntaxTrees.Aggregates.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.Aggregates.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.Aggregates.SingleArrayAggregate;
import Triangle.AbstractSyntaxTrees.Aggregates.SingleRecordAggregate;
import Triangle.AbstractSyntaxTrees.Commands.AssignCommand;
import Triangle.AbstractSyntaxTrees.Commands.CallCommand;
import Triangle.AbstractSyntaxTrees.Commands.EmptyCommand;
import Triangle.AbstractSyntaxTrees.Commands.IfCommand;
import Triangle.AbstractSyntaxTrees.Commands.LetCommand;
import Triangle.AbstractSyntaxTrees.Commands.SequentialCommand;
import Triangle.AbstractSyntaxTrees.Commands.WhileCommand;
import Triangle.AbstractSyntaxTrees.Declarations.BinaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.Declaration;
import Triangle.AbstractSyntaxTrees.Declarations.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.UnaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.VarDeclaration;
import Triangle.AbstractSyntaxTrees.Expressions.ArrayExpression;
import Triangle.AbstractSyntaxTrees.Expressions.BinaryExpression;
import Triangle.AbstractSyntaxTrees.Expressions.CallExpression;
import Triangle.AbstractSyntaxTrees.Expressions.CharacterExpression;
import Triangle.AbstractSyntaxTrees.Expressions.EmptyExpression;
import Triangle.AbstractSyntaxTrees.Expressions.IfExpression;
import Triangle.AbstractSyntaxTrees.Expressions.IntegerExpression;
import Triangle.AbstractSyntaxTrees.Expressions.LetExpression;
import Triangle.AbstractSyntaxTrees.Expressions.RecordExpression;
import Triangle.AbstractSyntaxTrees.Expressions.UnaryExpression;
import Triangle.AbstractSyntaxTrees.Expressions.VnameExpression;
import Triangle.AbstractSyntaxTrees.Formals.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Formals.FormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Formals.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Formals.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Formals.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Terminals.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Terminals.Identifier;
import Triangle.AbstractSyntaxTrees.Terminals.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.Terminals.Operator;
import Triangle.AbstractSyntaxTrees.Terminals.Terminal;
import Triangle.AbstractSyntaxTrees.Types.AnyTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.BoolTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.CharTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.ErrorTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.FieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.IntTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.Types.TypeDenoter;
import Triangle.AbstractSyntaxTrees.Visitors.ActualParameterSequenceVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.ActualParameterVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.ArrayAggregateVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.CommandVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.DeclarationVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.ExpressionVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.FormalParameterSequenceVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.FormalParameterVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.IdentifierVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.LiteralVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.OperatorVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.ProgramVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.RecordAggregateVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.TypeDenoterVisitor;
import Triangle.AbstractSyntaxTrees.Visitors.VnameVisitor;
import Triangle.AbstractSyntaxTrees.Vnames.DotVname;
import Triangle.AbstractSyntaxTrees.Vnames.SimpleVname;
import Triangle.AbstractSyntaxTrees.Vnames.SubscriptVname;
import Triangle.SyntacticAnalyzer.SourcePosition;

public final class Checker implements ActualParameterVisitor<FormalParameter, Void>,
		ActualParameterSequenceVisitor<FormalParameterSequence, Void>, ArrayAggregateVisitor<Void, TypeDenoter>,
		CommandVisitor<Void, Void>, DeclarationVisitor<Void, Void>, ExpressionVisitor<Void, TypeDenoter>,
		FormalParameterVisitor<Void, Void>, FormalParameterSequenceVisitor<Void, Void>,
		IdentifierVisitor<Void, Declaration>, LiteralVisitor<Void, TypeDenoter>, OperatorVisitor<Void, Declaration>,
		ProgramVisitor<Void, Void>, RecordAggregateVisitor<Void, FieldTypeDenoter>,
		TypeDenoterVisitor<Void, TypeDenoter>, VnameVisitor<Void, TypeDenoter> {

	// Commands

	// Always returns null. Does not use the given object.

	@Override
	public Void visitAssignCommand(AssignCommand ast, Void arg) {
		var vType = ast.V.visit(this);
		var eType = ast.E.visit(this);

		if (!ast.V.variable) {
			reporter.reportError("LHS of assignment is not a variable", "", ast.V.getPosition());
		}

		if (!eType.equals(vType)) {
			reporter.reportError("assignment incompatibilty", "", ast.getPosition());
		}

		return null;
	}

	@Override
	public Void visitCallCommand(CallCommand ast, Void arg) {

		var binding = ast.I.visit(this);
		if (binding == null) {
			reportUndeclared(ast.I);
		} else if (binding instanceof ProcDeclaration) {
			ast.APS.visit(this, ((ProcDeclaration) binding).FPS);
		} else if (binding instanceof ProcFormalParameter) {
			ast.APS.visit(this, ((ProcFormalParameter) binding).FPS);
		} else {
			reporter.reportError("\"%\" is not a procedure identifier", ast.I.spelling, ast.I.getPosition());
		}

		return null;
	}

	@Override
	public Void visitEmptyCommand(EmptyCommand ast, Void arg) {
		return null;
	}

	@Override
	public Void visitIfCommand(IfCommand ast, Void arg) {
		var eType = ast.E.visit(this);
		if (!eType.equals(StdEnvironment.booleanType)) {
			reporter.reportError("Boolean expression expected here", "", ast.E.getPosition());
		}

		ast.C1.visit(this);
		ast.C2.visit(this);

		return null;
	}

	@Override
	public Void visitLetCommand(LetCommand ast, Void arg) {
		idTable.openScope();
		ast.D.visit(this);
		ast.C.visit(this);
		idTable.closeScope();
		return null;
	}

	@Override
	public Void visitSequentialCommand(SequentialCommand ast, Void arg) {
		ast.C1.visit(this);
		ast.C2.visit(this);
		return null;
	}

	@Override
	public Void visitWhileCommand(WhileCommand ast, Void arg) {
		var eType = ast.E.visit(this);
		if (!eType.equals(StdEnvironment.booleanType)) {
			reporter.reportError("Boolean expression expected here", "", ast.E.getPosition());
		}
		ast.C.visit(this);
		return null;
	}

	// Expressions

	// Returns the TypeDenoter denoting the type of the expression. Does
	// not use the given object.

	@Override
	public TypeDenoter visitArrayExpression(ArrayExpression ast, Void arg) {
		var elemType = ast.AA.visit(this);
		var il = new IntegerLiteral(Integer.toString(ast.AA.elemCount), ast.getPosition());
		ast.type = new ArrayTypeDenoter(il, elemType, ast.getPosition());
		return ast.type;
	}

	@Override
	public TypeDenoter visitBinaryExpression(BinaryExpression ast, Void arg) {

		var e1Type = ast.E1.visit(this);
		var e2Type = ast.E2.visit(this);
		var binding = ast.O.visit(this);

		if (binding == null) {
			reportUndeclared(ast.O);
		} else {
			if (!(binding instanceof BinaryOperatorDeclaration))
				reporter.reportError("\"%\" is not a binary operator", ast.O.spelling, ast.O.getPosition());
			var bbinding = (BinaryOperatorDeclaration) binding;
			if (bbinding.ARG1 == StdEnvironment.anyType) {
				// this operator must be "=" or "\="
				if (!e1Type.equals(e2Type)) {
					reporter.reportError("incompatible argument types for \"%\"", ast.O.spelling, ast.getPosition());
				}
			} else if (!e1Type.equals(bbinding.ARG1)) {
				reporter.reportError("wrong argument type for \"%\"", ast.O.spelling, ast.E1.getPosition());
			} else if (!e2Type.equals(bbinding.ARG2)) {
				reporter.reportError("wrong argument type for \"%\"", ast.O.spelling, ast.E2.getPosition());
			}

			ast.type = bbinding.RES;
		}

		return ast.type;
	}

	@Override
	public TypeDenoter visitCallExpression(CallExpression ast, Void arg) {
		var binding = ast.I.visit(this);
		if (binding == null) {
			reportUndeclared(ast.I);
			ast.type = StdEnvironment.errorType;
		} else if (binding instanceof FuncDeclaration) {
			ast.APS.visit(this, ((FuncDeclaration) binding).FPS);
			ast.type = ((FuncDeclaration) binding).T;
		} else if (binding instanceof FuncFormalParameter) {
			ast.APS.visit(this, ((FuncFormalParameter) binding).FPS);
			ast.type = ((FuncFormalParameter) binding).T;
		} else {
			reporter.reportError("\"%\" is not a function identifier", ast.I.spelling, ast.I.getPosition());
		}

		return ast.type;
	}

	@Override
	public TypeDenoter visitCharacterExpression(CharacterExpression ast, Void arg) {
		ast.type = StdEnvironment.charType;
		return ast.type;
	}

	@Override
	public TypeDenoter visitEmptyExpression(EmptyExpression ast, Void arg) {
		ast.type = null;
		return ast.type;
	}

	@Override
	public TypeDenoter visitIfExpression(IfExpression ast, Void arg) {
		var e1Type = ast.E1.visit(this);
		if (!e1Type.equals(StdEnvironment.booleanType)) {
			reporter.reportError("Boolean expression expected here", "", ast.E1.getPosition());
		}
		var e2Type = ast.E2.visit(this);
		var e3Type = ast.E3.visit(this);
		if (!e2Type.equals(e3Type)) {
			reporter.reportError("incompatible limbs in if-expression", "", ast.getPosition());
		}
		ast.type = e2Type;
		return ast.type;
	}

	@Override
	public TypeDenoter visitIntegerExpression(IntegerExpression ast, Void arg) {
		ast.type = StdEnvironment.integerType;
		return ast.type;
	}

	@Override
	public TypeDenoter visitLetExpression(LetExpression ast, Void arg) {
		idTable.openScope();
		ast.D.visit(this);
		ast.type = ast.E.visit(this);
		idTable.closeScope();
		return ast.type;
	}

	@Override
	public TypeDenoter visitRecordExpression(RecordExpression ast, Void arg) {
		var rType = ast.RA.visit(this);
		ast.type = new RecordTypeDenoter(rType, ast.getPosition());
		return ast.type;
	}

	@Override
	public TypeDenoter visitUnaryExpression(UnaryExpression ast, Void arg) {

		var eType = ast.E.visit(this);
		var binding = ast.O.visit(this);
		if (binding == null) {
			reportUndeclared(ast.O);
			ast.type = StdEnvironment.errorType;
		} else if (!(binding instanceof UnaryOperatorDeclaration)) {
			reporter.reportError("\"%\" is not a unary operator", ast.O.spelling, ast.O.getPosition());
		} else {
			var ubinding = (UnaryOperatorDeclaration) binding;
			if (!eType.equals(ubinding.ARG)) {
				reporter.reportError("wrong argument type for \"%\"", ast.O.spelling, ast.O.getPosition());
			}
			ast.type = ubinding.RES;
		}
		return ast.type;
	}

	@Override
	public TypeDenoter visitVnameExpression(VnameExpression ast, Void arg) {
		ast.type = ast.V.visit(this);
		return ast.type;
	}

	// Declarations

	// Always returns null. Does not use the given object.
	@Override
	public Void visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Void arg) {
		return null;
	}

	@Override
	public Void visitConstDeclaration(ConstDeclaration ast, Void arg) {
		ast.E.visit(this);
		idTable.enter(ast.I.spelling, ast);
		if (ast.duplicated) {
			reporter.reportError("identifier \"%\" already declared", ast.I.spelling, ast.getPosition());
		}
		return null;
	}

	@Override
	public Void visitFuncDeclaration(FuncDeclaration ast, Void arg) {
		ast.T = ast.T.visit(this);
		idTable.enter(ast.I.spelling, ast); // permits recursion
		if (ast.duplicated) {
			reporter.reportError("identifier \"%\" already declared", ast.I.spelling, ast.getPosition());
		}
		idTable.openScope();
		ast.FPS.visit(this);
		var eType = ast.E.visit(this);
		idTable.closeScope();
		if (!ast.T.equals(eType)) {
			reporter.reportError("body of function \"%\" has wrong type", ast.I.spelling, ast.E.getPosition());
		}
		return null;
	}

	@Override
	public Void visitProcDeclaration(ProcDeclaration ast, Void arg) {
		idTable.enter(ast.I.spelling, ast); // permits recursion
		if (ast.duplicated) {
			reporter.reportError("identifier \"%\" already declared", ast.I.spelling, ast.getPosition());
		}
		idTable.openScope();
		ast.FPS.visit(this);
		ast.C.visit(this);
		idTable.closeScope();
		return null;
	}

	@Override
	public Void visitSequentialDeclaration(SequentialDeclaration ast, Void arg) {
		ast.D1.visit(this);
		ast.D2.visit(this);
		return null;
	}

	@Override
	public Void visitTypeDeclaration(TypeDeclaration ast, Void arg) {
		ast.T = ast.T.visit(this);
		idTable.enter(ast.I.spelling, ast);
		if (ast.duplicated) {
			reporter.reportError("identifier \"%\" already declared", ast.I.spelling, ast.getPosition());
		}
		return null;
	}

	@Override
	public Void visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Void arg) {
		return null;
	}

	@Override
	public Void visitVarDeclaration(VarDeclaration ast, Void arg) {
		ast.T = ast.T.visit(this);
		idTable.enter(ast.I.spelling, ast);
		if (ast.duplicated) {
			reporter.reportError("identifier \"%\" already declared", ast.I.spelling, ast.getPosition());
		}

		return null;
	}

	// Array Aggregates

	// Returns the TypeDenoter for the Array Aggregate. Does not use the
	// given object.

	@Override
	public TypeDenoter visitMultipleArrayAggregate(MultipleArrayAggregate ast, Void arg) {
		var eType = ast.E.visit(this);
		var elemType = ast.AA.visit(this);
		ast.elemCount = ast.AA.elemCount + 1;
		if (!eType.equals(elemType)) {
			reporter.reportError("incompatible array-aggregate element", "", ast.E.getPosition());
		}
		return elemType;
	}

	@Override
	public TypeDenoter visitSingleArrayAggregate(SingleArrayAggregate ast, Void arg) {
		var elemType = ast.E.visit(this);
		ast.elemCount = 1;
		return elemType;
	}

	// Record Aggregates

	// Returns the TypeDenoter for the Record Aggregate. Does not use the
	// given object.

	@Override
	public FieldTypeDenoter visitMultipleRecordAggregate(MultipleRecordAggregate ast, Void arg) {
		var eType = ast.E.visit(this);
		var rType = ast.RA.visit(this);
		var fType = checkFieldIdentifier(rType, ast.I);
		if (fType != StdEnvironment.errorType) {
			reporter.reportError("duplicate field \"%\" in record", ast.I.spelling, ast.I.getPosition());
		}
		ast.type = new MultipleFieldTypeDenoter(ast.I, eType, rType, ast.getPosition());
		return ast.type;
	}

	@Override
	public FieldTypeDenoter visitSingleRecordAggregate(SingleRecordAggregate ast, Void arg) {
		var eType = ast.E.visit(this);
		ast.type = new SingleFieldTypeDenoter(ast.I, eType, ast.getPosition());
		return ast.type;
	}

	// Formal Parameters

	// Always returns null. Does not use the given object.

	@Override
	public Void visitConstFormalParameter(ConstFormalParameter ast, Void arg) {
		ast.T = ast.T.visit(this);
		idTable.enter(ast.I.spelling, ast);
		if (ast.duplicated) {
			reporter.reportError("duplicated formal parameter \"%\"", ast.I.spelling, ast.getPosition());
		}
		return null;
	}

	@Override
	public Void visitFuncFormalParameter(FuncFormalParameter ast, Void arg) {
		idTable.openScope();
		ast.FPS.visit(this);
		idTable.closeScope();
		ast.T = ast.T.visit(this);
		idTable.enter(ast.I.spelling, ast);
		if (ast.duplicated) {
			reporter.reportError("duplicated formal parameter \"%\"", ast.I.spelling, ast.getPosition());
		}
		return null;
	}

	@Override
	public Void visitProcFormalParameter(ProcFormalParameter ast, Void arg) {
		idTable.openScope();
		ast.FPS.visit(this);
		idTable.closeScope();
		idTable.enter(ast.I.spelling, ast);
		if (ast.duplicated) {
			reporter.reportError("duplicated formal parameter \"%\"", ast.I.spelling, ast.getPosition());
		}
		return null;
	}

	@Override
	public Void visitVarFormalParameter(VarFormalParameter ast, Void arg) {
		ast.T = ast.T.visit(this);
		idTable.enter(ast.I.spelling, ast);
		if (ast.duplicated) {
			reporter.reportError("duplicated formal parameter \"%\"", ast.I.spelling, ast.getPosition());
		}
		return null;
	}

	@Override
	public Void visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Void arg) {
		return null;
	}

	@Override
	public Void visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Void arg) {
		ast.FP.visit(this);
		ast.FPS.visit(this);
		return null;
	}

	@Override
	public Void visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Void arg) {
		ast.FP.visit(this);
		return null;
	}

	// Actual Parameters

	// Always returns null. Uses the given FormalParameter.

	@Override
	public Void visitConstActualParameter(ConstActualParameter ast, FormalParameter arg) {
		var eType = ast.E.visit(this);
		if (!(arg instanceof ConstFormalParameter)) {
			reporter.reportError("const actual parameter not expected here", "", ast.getPosition());
		} else if (!eType.equals(((ConstFormalParameter) arg).T)) {
			reporter.reportError("wrong type for const actual parameter", "", ast.E.getPosition());
		}
		return null;
	}

	@Override
	public Void visitFuncActualParameter(FuncActualParameter ast, FormalParameter arg) {
		var binding = ast.I.visit(this);
		if (binding == null) {
			reportUndeclared(ast.I);
		} else if (!(binding instanceof FuncDeclaration || binding instanceof FuncFormalParameter)) {
			reporter.reportError("\"%\" is not a function identifier", ast.I.spelling, ast.I.getPosition());
		} else if (!(arg instanceof FuncFormalParameter)) {
			reporter.reportError("func actual parameter not expected here", "", ast.getPosition());
		} else {
			FormalParameterSequence FPS = null;
			TypeDenoter T = null;
			if (binding instanceof FuncDeclaration) {
				FPS = ((FuncDeclaration) binding).FPS;
				T = ((FuncDeclaration) binding).T;
			} else {
				FPS = ((FuncFormalParameter) binding).FPS;
				T = ((FuncFormalParameter) binding).T;
			}
			if (!FPS.equals(((FuncFormalParameter) arg).FPS)) {
				reporter.reportError("wrong signature for function \"%\"", ast.I.spelling, ast.I.getPosition());
			} else if (!T.equals(((FuncFormalParameter) arg).T)) {
				reporter.reportError("wrong type for function \"%\"", ast.I.spelling, ast.I.getPosition());
			}
		}
		return null;
	}

	@Override
	public Void visitProcActualParameter(ProcActualParameter ast, FormalParameter arg) {
		var binding = ast.I.visit(this);
		if (binding == null) {
			reportUndeclared(ast.I);
		} else if (!(binding instanceof ProcDeclaration || binding instanceof ProcFormalParameter)) {
			reporter.reportError("\"%\" is not a procedure identifier", ast.I.spelling, ast.I.getPosition());
		} else if (!(arg instanceof ProcFormalParameter)) {
			reporter.reportError("proc actual parameter not expected here", "", ast.getPosition());
		} else {
			FormalParameterSequence FPS = null;
			if (binding instanceof ProcDeclaration) {
				FPS = ((ProcDeclaration) binding).FPS;
			} else {
				FPS = ((ProcFormalParameter) binding).FPS;
			}
			if (!FPS.equals(((ProcFormalParameter) arg).FPS)) {
				reporter.reportError("wrong signature for procedure \"%\"", ast.I.spelling, ast.I.getPosition());
			}
		}
		return null;
	}

	@Override
	public Void visitVarActualParameter(VarActualParameter ast, FormalParameter arg) {
		var vType = ast.V.visit(this);
		if (!ast.V.variable) {
			reporter.reportError("actual parameter is not a variable", "", ast.V.getPosition());
		} else if (!(arg instanceof VarFormalParameter)) {
			reporter.reportError("var actual parameter not expected here", "", ast.V.getPosition());
		} else if (!vType.equals(((VarFormalParameter) arg).T)) {
			reporter.reportError("wrong type for var actual parameter", "", ast.V.getPosition());
		}
		return null;
	}

	@Override
	public Void visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, FormalParameterSequence arg) {
		if (!(arg instanceof EmptyFormalParameterSequence)) {
			reporter.reportError("too few actual parameters", "", ast.getPosition());
		}
		return null;
	}

	@Override
	public Void visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, FormalParameterSequence arg) {
		if (!(arg instanceof MultipleFormalParameterSequence)) {
			reporter.reportError("too many actual parameters", "", ast.getPosition());
		} else {
			ast.AP.visit(this, ((MultipleFormalParameterSequence) arg).FP);
			ast.APS.visit(this, ((MultipleFormalParameterSequence) arg).FPS);
		}
		return null;
	}

	@Override
	public Void visitSingleActualParameterSequence(SingleActualParameterSequence ast, FormalParameterSequence arg) {
		if (!(arg instanceof SingleFormalParameterSequence)) {
			reporter.reportError("incorrect number of actual parameters", "", ast.getPosition());
		} else {
			ast.AP.visit(this, ((SingleFormalParameterSequence) arg).FP);
		}
		return null;
	}

	// Type Denoters

	// Returns the expanded version of the TypeDenoter. Does not
	// use the given object.

	@Override
	public TypeDenoter visitAnyTypeDenoter(AnyTypeDenoter ast, Void arg) {
		return StdEnvironment.anyType;
	}

	@Override
	public TypeDenoter visitArrayTypeDenoter(ArrayTypeDenoter ast, Void arg) {
		ast.T = ast.T.visit(this);
		if ((Integer.valueOf(ast.IL.spelling)) == 0) {
			reporter.reportError("arrays must not be empty", "", ast.IL.getPosition());
		}
		return ast;
	}

	@Override
	public TypeDenoter visitBoolTypeDenoter(BoolTypeDenoter ast, Void arg) {
		return StdEnvironment.booleanType;
	}

	@Override
	public TypeDenoter visitCharTypeDenoter(CharTypeDenoter ast, Void arg) {
		return StdEnvironment.charType;
	}

	@Override
	public TypeDenoter visitErrorTypeDenoter(ErrorTypeDenoter ast, Void arg) {
		return StdEnvironment.errorType;
	}

	@Override
	public TypeDenoter visitSimpleTypeDenoter(SimpleTypeDenoter ast, Void arg) {
		var binding = ast.I.visit(this);
		if (binding == null) {
			reportUndeclared(ast.I);
			return StdEnvironment.errorType;
		} else if (!(binding instanceof TypeDeclaration)) {
			reporter.reportError("\"%\" is not a type identifier", ast.I.spelling, ast.I.getPosition());
			return StdEnvironment.errorType;
		}
		return ((TypeDeclaration) binding).T;
	}

	@Override
	public TypeDenoter visitIntTypeDenoter(IntTypeDenoter ast, Void arg) {
		return StdEnvironment.integerType;
	}

	@Override
	public TypeDenoter visitRecordTypeDenoter(RecordTypeDenoter ast, Void arg) {
		ast.FT = (FieldTypeDenoter) ast.FT.visit(this);
		return ast;
	}

	@Override
	public TypeDenoter visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Void arg) {
		ast.T = ast.T.visit(this);
		ast.FT.visit(this);
		return ast;
	}

	@Override
	public TypeDenoter visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Void arg) {
		ast.T = ast.T.visit(this);
		return ast;
	}

	// Literals, Identifiers and Operators
	@Override
	public TypeDenoter visitCharacterLiteral(CharacterLiteral CL, Void arg) {
		return StdEnvironment.charType;
	}

	@Override
	public Declaration visitIdentifier(Identifier I, Void arg) {
		var binding = idTable.retrieve(I.spelling);
		if (binding != null) {
			I.decl = binding;
		}
		return binding;
	}

	@Override
	public TypeDenoter visitIntegerLiteral(IntegerLiteral IL, Void arg) {
		return StdEnvironment.integerType;
	}

	@Override
	public Declaration visitOperator(Operator O, Void arg) {
		var binding = idTable.retrieve(O.spelling);
		if (binding != null) {
			O.decl = binding;
		}
		return binding;
	}

	// Value-or-variable names

	// Determines the address of a named object (constant or variable).
	// This consists of a base object, to which 0 or more field-selection
	// or array-indexing operations may be applied (if it is a record or
	// array). As much as possible of the address computation is done at
	// compile-time. Code is generated only when necessary to evaluate
	// index expressions at run-time.
	// currentLevel is the routine level where the v-name occurs.
	// frameSize is the anticipated size of the local stack frame when
	// the object is addressed at run-time.
	// It returns the description of the base object.
	// offset is set to the total of any field offsets (plus any offsets
	// due to index expressions that happen to be literals).
	// indexed is set to true iff there are any index expressions (other
	// than literals). In that case code is generated to compute the
	// offset due to these indexing operations at run-time.

	// Returns the TypeDenoter of the Vname. Does not use the
	// given object.

	@Override
	public TypeDenoter visitDotVname(DotVname ast, Void arg) {
		ast.type = null;
		var vType = ast.V.visit(this);
		ast.variable = ast.V.variable;
		if (!(vType instanceof RecordTypeDenoter)) {
			reporter.reportError("record expected here", "", ast.V.getPosition());
		} else {
			ast.type = checkFieldIdentifier(((RecordTypeDenoter) vType).FT, ast.I);
			if (ast.type == StdEnvironment.errorType) {
				reporter.reportError("no field \"%\" in this record type", ast.I.spelling, ast.I.getPosition());
			}
		}
		return ast.type;
	}

	@Override
	public TypeDenoter visitSimpleVname(SimpleVname ast, Void arg) {
		ast.variable = false;
		ast.type = StdEnvironment.errorType;
		var binding = ast.I.visit(this);
		if (binding == null) {
			reportUndeclared(ast.I);
		} else if (binding instanceof ConstDeclaration) {
			ast.type = ((ConstDeclaration) binding).E.type;
			ast.variable = false;
		} else if (binding instanceof VarDeclaration) {
			ast.type = ((VarDeclaration) binding).T;
			ast.variable = true;
		} else if (binding instanceof ConstFormalParameter) {
			ast.type = ((ConstFormalParameter) binding).T;
			ast.variable = false;
		} else if (binding instanceof VarFormalParameter) {
			ast.type = ((VarFormalParameter) binding).T;
			ast.variable = true;
		} else {
			reporter.reportError("\"%\" is not a const or var identifier", ast.I.spelling, ast.I.getPosition());
		}
		return ast.type;
	}

	@Override
	public TypeDenoter visitSubscriptVname(SubscriptVname ast, Void arg) {
		var vType = ast.V.visit(this);
		ast.variable = ast.V.variable;
		var eType = ast.E.visit(this);
		if (vType != StdEnvironment.errorType) {
			if (!(vType instanceof ArrayTypeDenoter)) {
				reporter.reportError("array expected here", "", ast.V.getPosition());
			} else {
				if (!eType.equals(StdEnvironment.integerType)) {
					reporter.reportError("Integer expression expected here", "", ast.E.getPosition());
				}
				ast.type = ((ArrayTypeDenoter) vType).T;
			}
		}
		return ast.type;
	}

	// Programs

	@Override
	public Void visitProgram(Program ast, Void arg) {
		ast.C.visit(this);
		return null;
	}

	// Checks whether the source program, represented by its AST, satisfies the
	// language's scope rules and type rules.
	// Also decorates the AST as follows:
	// (a) Each applied occurrence of an identifier or operator is linked to
	// the corresponding declaration of that identifier or operator.
	// (b) Each expression and value-or-variable-name is decorated by its type.
	// (c) Each type identifier is replaced by the type it denotes.
	// Types are represented by small ASTs.

	public void check(Program ast) {
		ast.visit(this);
	}

	/////////////////////////////////////////////////////////////////////////////

	public Checker(ErrorReporter reporter) {
		this.reporter = reporter;
		this.idTable = new IdentificationTable();
		establishStdEnvironment();
	}

	private IdentificationTable idTable;
	private static SourcePosition dummyPos = new SourcePosition();
	private ErrorReporter reporter;

	// Reports that the identifier or operator used at a leaf of the AST
	// has not been declared.

	private void reportUndeclared(Terminal leaf) {
		reporter.reportError("\"%\" is not declared", leaf.spelling, leaf.getPosition());
	}

	private static TypeDenoter checkFieldIdentifier(FieldTypeDenoter ast, Identifier I) {
		if (ast instanceof MultipleFieldTypeDenoter) {
			var ft = (MultipleFieldTypeDenoter) ast;
			if (ft.I.spelling.compareTo(I.spelling) == 0) {
				I.decl = ast;
				return ft.T;
			} else {
				return checkFieldIdentifier(ft.FT, I);
			}
		} else if (ast instanceof SingleFieldTypeDenoter) {
			var ft = (SingleFieldTypeDenoter) ast;
			if (ft.I.spelling.compareTo(I.spelling) == 0) {
				I.decl = ast;
				return ft.T;
			}
		}
		return StdEnvironment.errorType;
	}

	// Creates a small AST to represent the "declaration" of a standard
	// type, and enters it in the identification table.

	private TypeDeclaration declareStdType(String id, TypeDenoter typedenoter) {

		var binding = new TypeDeclaration(new Identifier(id, dummyPos), typedenoter, dummyPos);
		idTable.enter(id, binding);
		return binding;
	}

	// Creates a small AST to represent the "declaration" of a standard
	// type, and enters it in the identification table.

	private ConstDeclaration declareStdConst(String id, TypeDenoter constType) {

		// constExpr used only as a placeholder for constType
		var constExpr = new IntegerExpression(null, dummyPos);
		constExpr.type = constType;
		var binding = new ConstDeclaration(new Identifier(id, dummyPos), constExpr, dummyPos);
		idTable.enter(id, binding);
		return binding;
	}

	// Creates a small AST to represent the "declaration" of a standard
	// type, and enters it in the identification table.

	private ProcDeclaration declareStdProc(String id, FormalParameterSequence fps) {

		var binding = new ProcDeclaration(new Identifier(id, dummyPos), fps, new EmptyCommand(dummyPos), dummyPos);
		idTable.enter(id, binding);
		return binding;
	}

	// Creates a small AST to represent the "declaration" of a standard
	// type, and enters it in the identification table.

	private FuncDeclaration declareStdFunc(String id, FormalParameterSequence fps, TypeDenoter resultType) {

		var binding = new FuncDeclaration(new Identifier(id, dummyPos), fps, resultType, new EmptyExpression(dummyPos),
				dummyPos);
		idTable.enter(id, binding);
		return binding;
	}

	// Creates a small AST to represent the "declaration" of a
	// unary operator, and enters it in the identification table.
	// This "declaration" summarises the operator's type info.

	private UnaryOperatorDeclaration declareStdUnaryOp(String op, TypeDenoter argType, TypeDenoter resultType) {

		var binding = new UnaryOperatorDeclaration(new Operator(op, dummyPos), argType, resultType, dummyPos);
		idTable.enter(op, binding);
		return binding;
	}

	// Creates a small AST to represent the "declaration" of a
	// binary operator, and enters it in the identification table.
	// This "declaration" summarises the operator's type info.

	private BinaryOperatorDeclaration declareStdBinaryOp(String op, TypeDenoter arg1Type, TypeDenoter arg2type,
			TypeDenoter resultType) {

		var binding = new BinaryOperatorDeclaration(new Operator(op, dummyPos), arg1Type, arg2type, resultType,
				dummyPos);
		idTable.enter(op, binding);
		return binding;
	}

	// Creates small ASTs to represent the standard types.
	// Creates small ASTs to represent "declarations" of standard types,
	// constants, procedures, functions, and operators.
	// Enters these "declarations" in the identification table.

	private final static Identifier dummyI = new Identifier("", dummyPos);

	private void establishStdEnvironment() {

		// idTable.startIdentification();
		StdEnvironment.booleanType = new BoolTypeDenoter(dummyPos);
		StdEnvironment.integerType = new IntTypeDenoter(dummyPos);
		StdEnvironment.charType = new CharTypeDenoter(dummyPos);
		StdEnvironment.anyType = new AnyTypeDenoter(dummyPos);
		StdEnvironment.errorType = new ErrorTypeDenoter(dummyPos);

		StdEnvironment.booleanDecl = declareStdType("Boolean", StdEnvironment.booleanType);
		StdEnvironment.falseDecl = declareStdConst("false", StdEnvironment.booleanType);
		StdEnvironment.trueDecl = declareStdConst("true", StdEnvironment.booleanType);
		StdEnvironment.notDecl = declareStdUnaryOp("\\", StdEnvironment.booleanType, StdEnvironment.booleanType);
		StdEnvironment.andDecl = declareStdBinaryOp("/\\", StdEnvironment.booleanType, StdEnvironment.booleanType,
				StdEnvironment.booleanType);
		StdEnvironment.orDecl = declareStdBinaryOp("\\/", StdEnvironment.booleanType, StdEnvironment.booleanType,
				StdEnvironment.booleanType);

		StdEnvironment.integerDecl = declareStdType("Integer", StdEnvironment.integerType);
		StdEnvironment.maxintDecl = declareStdConst("maxint", StdEnvironment.integerType);
		StdEnvironment.addDecl = declareStdBinaryOp("+", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.integerType);
		StdEnvironment.subtractDecl = declareStdBinaryOp("-", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.integerType);
		StdEnvironment.multiplyDecl = declareStdBinaryOp("*", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.integerType);
		StdEnvironment.divideDecl = declareStdBinaryOp("/", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.integerType);
		StdEnvironment.moduloDecl = declareStdBinaryOp("//", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.integerType);
		StdEnvironment.lessDecl = declareStdBinaryOp("<", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.booleanType);
		StdEnvironment.notgreaterDecl = declareStdBinaryOp("<=", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.booleanType);
		StdEnvironment.greaterDecl = declareStdBinaryOp(">", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.booleanType);
		StdEnvironment.notlessDecl = declareStdBinaryOp(">=", StdEnvironment.integerType, StdEnvironment.integerType,
				StdEnvironment.booleanType);

		StdEnvironment.charDecl = declareStdType("Char", StdEnvironment.charType);
		StdEnvironment.chrDecl = declareStdFunc("chr",
				new SingleFormalParameterSequence(
						new ConstFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos),
				StdEnvironment.charType);
		StdEnvironment.ordDecl = declareStdFunc("ord",
				new SingleFormalParameterSequence(new ConstFormalParameter(dummyI, StdEnvironment.charType, dummyPos),
						dummyPos),
				StdEnvironment.integerType);
		StdEnvironment.eofDecl = declareStdFunc("eof", new EmptyFormalParameterSequence(dummyPos),
				StdEnvironment.booleanType);
		StdEnvironment.eolDecl = declareStdFunc("eol", new EmptyFormalParameterSequence(dummyPos),
				StdEnvironment.booleanType);
		StdEnvironment.getDecl = declareStdProc("get", new SingleFormalParameterSequence(
				new VarFormalParameter(dummyI, StdEnvironment.charType, dummyPos), dummyPos));
		StdEnvironment.putDecl = declareStdProc("put", new SingleFormalParameterSequence(
				new ConstFormalParameter(dummyI, StdEnvironment.charType, dummyPos), dummyPos));
		StdEnvironment.getintDecl = declareStdProc("getint", new SingleFormalParameterSequence(
				new VarFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos));
		StdEnvironment.putintDecl = declareStdProc("putint", new SingleFormalParameterSequence(
				new ConstFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos));
		StdEnvironment.geteolDecl = declareStdProc("geteol", new EmptyFormalParameterSequence(dummyPos));
		StdEnvironment.puteolDecl = declareStdProc("puteol", new EmptyFormalParameterSequence(dummyPos));
		StdEnvironment.equalDecl = declareStdBinaryOp("=", StdEnvironment.anyType, StdEnvironment.anyType,
				StdEnvironment.booleanType);
		StdEnvironment.unequalDecl = declareStdBinaryOp("\\=", StdEnvironment.anyType, StdEnvironment.anyType,
				StdEnvironment.booleanType);

	}
}
