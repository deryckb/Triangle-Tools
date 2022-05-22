/*
 * @(#)Encoder.java                        2.1 2003/10/07
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

package Triangle.CodeGenerator;

import Triangle.ErrorReporter;
import Triangle.StdEnvironment;
import Triangle.AbstractMachine.Machine;
import Triangle.AbstractMachine.OpCode;
import Triangle.AbstractMachine.Primitive;
import Triangle.AbstractMachine.Register;
import Triangle.AbstractSyntaxTrees.AbstractSyntaxTree;
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
import Triangle.AbstractSyntaxTrees.Formals.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Formals.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Formals.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Terminals.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Terminals.Identifier;
import Triangle.AbstractSyntaxTrees.Terminals.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.Terminals.Operator;
import Triangle.AbstractSyntaxTrees.Types.AnyTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.BoolTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.CharTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.ErrorTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.IntTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.TypeDeclaration;
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
import Triangle.AbstractSyntaxTrees.Vnames.Vname;
import Triangle.CodeGenerator.Entities.AddressableEntity;
import Triangle.CodeGenerator.Entities.EqualityRoutine;
import Triangle.CodeGenerator.Entities.FetchableEntity;
import Triangle.CodeGenerator.Entities.Field;
import Triangle.CodeGenerator.Entities.KnownAddress;
import Triangle.CodeGenerator.Entities.KnownRoutine;
import Triangle.CodeGenerator.Entities.KnownValue;
import Triangle.CodeGenerator.Entities.PrimitiveRoutine;
import Triangle.CodeGenerator.Entities.RuntimeEntity;
import Triangle.CodeGenerator.Entities.TypeRepresentation;
import Triangle.CodeGenerator.Entities.UnknownAddress;
import Triangle.CodeGenerator.Entities.UnknownRoutine;
import Triangle.CodeGenerator.Entities.UnknownValue;

public final class Encoder
		implements ActualParameterVisitor<Frame, Integer>, ActualParameterSequenceVisitor<Frame, Integer>,
		ArrayAggregateVisitor<Frame, Integer>, CommandVisitor<Frame, Void>, DeclarationVisitor<Frame, Integer>,
		ExpressionVisitor<Frame, Integer>, FormalParameterVisitor<Frame, Integer>,
		FormalParameterSequenceVisitor<Frame, Integer>, IdentifierVisitor<Frame, Void>, LiteralVisitor<Void, Void>,
		OperatorVisitor<Frame, Void>, ProgramVisitor<Frame, Void>, RecordAggregateVisitor<Frame, Integer>,
		TypeDenoterVisitor<Frame, Integer>, VnameVisitor<Frame, RuntimeEntity> {

	// Commands
	@Override
	public Void visitAssignCommand(AssignCommand ast, Frame frame) {
		var valSize = ast.E.visit(this, frame);
		encodeStore(ast.V, frame.expand(valSize), valSize);
		return null;
	}

	@Override
	public Void visitCallCommand(CallCommand ast, Frame frame) {
		var argsSize = ast.APS.visit(this, frame);
		ast.I.visit(this, frame.replace(argsSize));
		return null;
	}

	@Override
	public Void visitEmptyCommand(EmptyCommand ast, Frame frame) {
		return null;
	}

	@Override
	public Void visitIfCommand(IfCommand ast, Frame frame) {
		ast.E.visit(this, frame);
		var jumpifAddr = emitter.emit(OpCode.JUMPIF, Machine.falseRep, Register.CB, 0);
		ast.C1.visit(this, frame);
		var jumpAddr = emitter.emit(OpCode.JUMP, 0, Register.CB, 0);
		emitter.patch(jumpifAddr);
		ast.C2.visit(this, frame);
		emitter.patch(jumpAddr);
		return null;
	}

	@Override
	public Void visitLetCommand(LetCommand ast, Frame frame) {
		var extraSize = ast.D.visit(this, frame);
		ast.C.visit(this, frame.expand(extraSize));
		if (extraSize > 0) {
			emitter.emit(OpCode.POP, extraSize);
		}
		return null;
	}

	@Override
	public Void visitSequentialCommand(SequentialCommand ast, Frame frame) {
		ast.C1.visit(this, frame);
		ast.C2.visit(this, frame);
		return null;
	}

	@Override
	public Void visitWhileCommand(WhileCommand ast, Frame frame) {
		var jumpAddr = emitter.emit(OpCode.JUMP, 0, Register.CB, 0);
		var loopAddr = emitter.getNextInstrAddr();
		ast.C.visit(this, frame);
		emitter.patch(jumpAddr);
		ast.E.visit(this, frame);
		emitter.emit(OpCode.JUMPIF, Machine.trueRep, Register.CB, loopAddr);
		return null;
	}

	// Expressions
	@Override
	public Integer visitArrayExpression(ArrayExpression ast, Frame frame) {
		ast.type.visit(this, frame);
		return ast.AA.visit(this, frame);
	}

	@Override
	public Integer visitBinaryExpression(BinaryExpression ast, Frame frame) {
		var valSize = ast.type.visit(this);
		var valSize1 = ast.E1.visit(this, frame);
		var frame1 = frame.expand(valSize1);
		var valSize2 = ast.E2.visit(this, frame1);
		var frame2 = frame.replace(valSize1 + valSize2);
		ast.O.visit(this, frame2);
		return valSize;
	}

	@Override
	public Integer visitCallExpression(CallExpression ast, Frame frame) {
		var valSize = ast.type.visit(this);
		var argsSize = ast.APS.visit(this, frame);
		ast.I.visit(this, frame.replace(argsSize));
		return valSize;
	}

	@Override
	public Integer visitCharacterExpression(CharacterExpression ast, Frame frame) {
		var valSize = ast.type.visit(this);
		emitter.emit(OpCode.LOADL, ast.CL.spelling.charAt(1));
		return valSize;
	}

	@Override
	public Integer visitEmptyExpression(EmptyExpression ast, Frame frame) {
		return 0;
	}

	@Override
	public Integer visitIfExpression(IfExpression ast, Frame frame) {
		ast.type.visit(this);
		ast.E1.visit(this, frame);
		var jumpifAddr = emitter.emit(OpCode.JUMPIF, Machine.falseRep, Register.CB, 0);
		var valSize = ast.E2.visit(this, frame);
		var jumpAddr = emitter.emit(OpCode.JUMP, 0, Register.CB, 0);
		emitter.patch(jumpifAddr);
		valSize = ast.E3.visit(this, frame);
		emitter.patch(jumpAddr);
		return valSize;
	}

	@Override
	public Integer visitIntegerExpression(IntegerExpression ast, Frame frame) {
		var valSize = ast.type.visit(this);
		emitter.emit(OpCode.LOADL, Integer.parseInt(ast.IL.spelling));
		return valSize;
	}

	@Override
	public Integer visitLetExpression(LetExpression ast, Frame frame) {
		ast.type.visit(this);
		var extraSize = ast.D.visit(this, frame);
		var frame1 = frame.expand(extraSize);
		var valSize = ast.E.visit(this, frame1);
		if (extraSize > 0) {
			emitter.emit(OpCode.POP, valSize, extraSize);
		}
		return valSize;
	}

	@Override
	public Integer visitRecordExpression(RecordExpression ast, Frame frame) {
		ast.type.visit(this);
		return ast.RA.visit(this, frame);
	}

	@Override
	public Integer visitUnaryExpression(UnaryExpression ast, Frame frame) {
		var valSize = ast.type.visit(this);
		ast.E.visit(this, frame);
		ast.O.visit(this, frame.replace(valSize));
		return valSize;
	}

	@Override
	public Integer visitVnameExpression(VnameExpression ast, Frame frame) {
		var valSize = ast.type.visit(this);
		encodeFetch(ast.V, frame, valSize);
		return valSize;
	}

	// Declarations
	@Override
	public Integer visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Frame frame) {
		return 0;
	}

	@Override
	public Integer visitConstDeclaration(ConstDeclaration ast, Frame frame) {
		var extraSize = 0;
		if (ast.E instanceof CharacterExpression) {
			var CL = ((CharacterExpression) ast.E).CL;
			ast.entity = new KnownValue(Machine.characterSize, CL.getValue());
		} else if (ast.E instanceof IntegerExpression) {
			var IL = ((IntegerExpression) ast.E).IL;
			ast.entity = new KnownValue(Machine.integerSize, IL.getValue());
		} else {
			var valSize = ast.E.visit(this, frame);
			ast.entity = new UnknownValue(valSize, frame.getLevel(), frame.getSize());
			extraSize = valSize;
		}
		writeTableDetails(ast);
		return extraSize;
	}

	@Override
	public Integer visitFuncDeclaration(FuncDeclaration ast, Frame frame) {
		var argsSize = 0;
		var valSize = 0;

		var jumpAddr = emitter.emit(OpCode.JUMP, 0, Register.CB, 0);
		ast.entity = new KnownRoutine(Machine.closureSize, frame.getLevel(), emitter.getNextInstrAddr());
		writeTableDetails(ast);
		if (frame.getLevel() == Machine.maxRoutineLevel) {
			reporter.reportRestriction("can't nest routines more than 7 deep");
		} else {
			var frame1 = frame.push(0);
			argsSize = ast.FPS.visit(this, frame1);
			var frame2 = frame.push(Machine.linkDataSize);
			valSize = ast.E.visit(this, frame2);
		}
		emitter.emit(OpCode.RETURN, valSize, argsSize);
		emitter.patch(jumpAddr);
		return 0;
	}

	@Override
	public Integer visitProcDeclaration(ProcDeclaration ast, Frame frame) {
		var argsSize = 0;
		var jumpAddr = emitter.emit(OpCode.JUMP, 0, Register.CB, 0);
		ast.entity = new KnownRoutine(Machine.closureSize, frame.getLevel(), emitter.getNextInstrAddr());
		writeTableDetails(ast);
		if (frame.getLevel() == Machine.maxRoutineLevel) {
			reporter.reportRestriction("can't nest routines so deeply");
		} else {
			var frame1 = frame.push(0);
			argsSize = ast.FPS.visit(this, frame1);
			var frame2 = frame.push(Machine.linkDataSize);
			ast.C.visit(this, frame2);
		}
		emitter.emit(OpCode.RETURN, argsSize);
		emitter.patch(jumpAddr);
		return 0;
	}

	@Override
	public Integer visitSequentialDeclaration(SequentialDeclaration ast, Frame frame) {
		var extraSize1 = ast.D1.visit(this, frame);
		var frame1 = frame.expand(extraSize1);
		var extraSize2 = ast.D2.visit(this, frame1);
		return extraSize1 + extraSize2;
	}

	@Override
	public Integer visitTypeDeclaration(TypeDeclaration ast, Frame frame) {
		// just to ensure the type's representation is decided
		ast.T.visit(this);
		return 0;
	}

	@Override
	public Integer visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Frame frame) {
		return 0;
	}

	@Override
	public Integer visitVarDeclaration(VarDeclaration ast, Frame frame) {
		var extraSize = ast.T.visit(this);
		emitter.emit(OpCode.PUSH, extraSize);
		ast.entity = new KnownAddress(Machine.addressSize, frame.getLevel(), frame.getSize());
		writeTableDetails(ast);
		return extraSize;
	}

	// Array Aggregates
	@Override
	public Integer visitMultipleArrayAggregate(MultipleArrayAggregate ast, Frame frame) {
		var elemSize = ast.E.visit(this, frame);
		var frame1 = frame.expand(elemSize);
		var arraySize = ast.AA.visit(this, frame1);
		return elemSize + arraySize;
	}

	@Override
	public Integer visitSingleArrayAggregate(SingleArrayAggregate ast, Frame frame) {
		return ast.E.visit(this, frame);
	}

	// Record Aggregates
	@Override
	public Integer visitMultipleRecordAggregate(MultipleRecordAggregate ast, Frame frame) {
		var fieldSize = ast.E.visit(this, frame);
		var frame1 = frame.expand(fieldSize);
		var recordSize = ast.RA.visit(this, frame1);
		return fieldSize + recordSize;
	}

	@Override
	public Integer visitSingleRecordAggregate(SingleRecordAggregate ast, Frame frame) {
		return ast.E.visit(this, frame);
	}

	// Formal Parameters
	@Override
	public Integer visitConstFormalParameter(ConstFormalParameter ast, Frame frame) {
		var valSize = ast.T.visit(this);
		ast.entity = new UnknownValue(valSize, frame.getLevel(), -frame.getSize() - valSize);
		writeTableDetails(ast);
		return valSize;
	}

	@Override
	public Integer visitFuncFormalParameter(FuncFormalParameter ast, Frame frame) {
		var argsSize = Machine.closureSize;
		ast.entity = new UnknownRoutine(Machine.closureSize, frame.getLevel(), -frame.getSize() - argsSize);
		writeTableDetails(ast);
		return argsSize;
	}

	@Override
	public Integer visitProcFormalParameter(ProcFormalParameter ast, Frame frame) {
		var argsSize = Machine.closureSize;
		ast.entity = new UnknownRoutine(Machine.closureSize, frame.getLevel(), -frame.getSize() - argsSize);
		writeTableDetails(ast);
		return argsSize;
	}

	@Override
	public Integer visitVarFormalParameter(VarFormalParameter ast, Frame frame) {
		ast.T.visit(this);
		ast.entity = new UnknownAddress(Machine.addressSize, frame.getLevel(), -frame.getSize() - Machine.addressSize);
		writeTableDetails(ast);
		return Machine.addressSize;
	}

	@Override
	public Integer visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Frame frame) {
		return 0;
	}

	@Override
	public Integer visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Frame frame) {
		var argsSize1 = ast.FPS.visit(this, frame);
		var frame1 = frame.expand(argsSize1);
		var argsSize2 = ast.FP.visit(this, frame1);
		return argsSize1 + argsSize2;
	}

	@Override
	public Integer visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Frame frame) {
		return ast.FP.visit(this, frame);
	}

	// Actual Parameters
	@Override
	public Integer visitConstActualParameter(ConstActualParameter ast, Frame frame) {
		return ast.E.visit(this, frame);
	}

	@Override
	public Integer visitFuncActualParameter(FuncActualParameter ast, Frame frame) {
		if (ast.I.decl.entity instanceof KnownRoutine) {
			var address = ((KnownRoutine) ast.I.decl.entity).getAddress();
			// static link, code address
			emitter.emit(OpCode.LOADA, 0, frame.getDisplayRegister(address), 0);
			emitter.emit(OpCode.LOADA, 0, Register.CB, address.getDisplacement());
		} else if (ast.I.decl.entity instanceof UnknownRoutine) {
			var address = ((UnknownRoutine) ast.I.decl.entity).getAddress();
			emitter.emit(OpCode.LOAD, Machine.closureSize, frame.getDisplayRegister(address),
					address.getDisplacement());
		} else if (ast.I.decl.entity instanceof PrimitiveRoutine) {
			var primitive = ((PrimitiveRoutine) ast.I.decl.entity).getPrimitive();
			// static link, code address
			emitter.emit(OpCode.LOADA, 0, Register.SB, 0);
			emitter.emit(OpCode.LOADA, Register.PB, primitive);
		}
		return Machine.closureSize;
	}

	@Override
	public Integer visitProcActualParameter(ProcActualParameter ast, Frame frame) {
		if (ast.I.decl.entity instanceof KnownRoutine) {
			var address = ((KnownRoutine) ast.I.decl.entity).getAddress();
			// static link, code address
			emitter.emit(OpCode.LOADA, 0, frame.getDisplayRegister(address), 0);
			emitter.emit(OpCode.LOADA, 0, Register.CB, address.getDisplacement());
		} else if (ast.I.decl.entity instanceof UnknownRoutine) {
			var address = ((UnknownRoutine) ast.I.decl.entity).getAddress();
			emitter.emit(OpCode.LOAD, Machine.closureSize, frame.getDisplayRegister(address),
					address.getDisplacement());
		} else if (ast.I.decl.entity instanceof PrimitiveRoutine) {
			var primitive = ((PrimitiveRoutine) ast.I.decl.entity).getPrimitive();
			// static link, code address
			emitter.emit(OpCode.LOADA, 0, Register.SB, 0);
			emitter.emit(OpCode.LOADA, Register.PB, primitive);
		}
		return Machine.closureSize;
	}

	@Override
	public Integer visitVarActualParameter(VarActualParameter ast, Frame frame) {
		encodeFetchAddress(ast.V, frame);
		return Machine.addressSize;
	}

	@Override
	public Integer visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Frame frame) {
		return 0;
	}

	@Override
	public Integer visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Frame frame) {
		var argsSize1 = ast.AP.visit(this, frame);
		var frame1 = frame.expand(argsSize1);
		var argsSize2 = ast.APS.visit(this, frame1);
		return argsSize1 + argsSize2;
	}

	@Override
	public Integer visitSingleActualParameterSequence(SingleActualParameterSequence ast, Frame frame) {
		return ast.AP.visit(this, frame);
	}

	// Type Denoters
	@Override
	public Integer visitAnyTypeDenoter(AnyTypeDenoter ast, Frame frame) {
		return 0;
	}

	@Override
	public Integer visitArrayTypeDenoter(ArrayTypeDenoter ast, Frame frame) {
		int typeSize;
		if (ast.entity == null) {
			var elemSize = ast.T.visit(this);
			typeSize = Integer.parseInt(ast.IL.spelling) * elemSize;
			ast.entity = new TypeRepresentation(typeSize);
			writeTableDetails(ast);
		} else {
			typeSize = ast.entity.getSize();
		}
		return typeSize;
	}

	@Override
	public Integer visitBoolTypeDenoter(BoolTypeDenoter ast, Frame frame) {
		if (ast.entity == null) {
			ast.entity = new TypeRepresentation(Machine.booleanSize);
			writeTableDetails(ast);
		}
		return Machine.booleanSize;
	}

	@Override
	public Integer visitCharTypeDenoter(CharTypeDenoter ast, Frame frame) {
		if (ast.entity == null) {
			ast.entity = new TypeRepresentation(Machine.characterSize);
			writeTableDetails(ast);
		}
		return Machine.characterSize;
	}

	@Override
	public Integer visitErrorTypeDenoter(ErrorTypeDenoter ast, Frame frame) {
		return 0;
	}

	@Override
	public Integer visitSimpleTypeDenoter(SimpleTypeDenoter ast, Frame frame) {
		return 0;
	}

	@Override
	public Integer visitIntTypeDenoter(IntTypeDenoter ast, Frame frame) {
		if (ast.entity == null) {
			ast.entity = new TypeRepresentation(Machine.integerSize);
			writeTableDetails(ast);
		}
		return Machine.integerSize;
	}

	@Override
	public Integer visitRecordTypeDenoter(RecordTypeDenoter ast, Frame frame) {
		int typeSize;
		if (ast.entity == null) {
			typeSize = ast.FT.visit(this, null);
			ast.entity = new TypeRepresentation(typeSize);
			writeTableDetails(ast);
		} else {
			typeSize = ast.entity.getSize();
		}
		return typeSize;
	}

	@Override
	public Integer visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Frame frame) {
		var offset = frame.getSize();
		int fieldSize;
		if (ast.entity == null) {
			fieldSize = ast.T.visit(this);
			ast.entity = new Field(fieldSize, offset);
			writeTableDetails(ast);
		} else {
			fieldSize = ast.entity.getSize();
		}

		var offset1 = frame.replace(offset + fieldSize);
		var recSize = ast.FT.visit(this, offset1);
		return fieldSize + recSize;
	}

	@Override
	public Integer visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Frame frame) {
		var offset = frame.getSize();
		int fieldSize;
		if (ast.entity == null) {
			fieldSize = ast.T.visit(this);
			ast.entity = new Field(fieldSize, offset);
			writeTableDetails(ast);
		} else {
			fieldSize = ast.entity.getSize();
		}

		return fieldSize;
	}

	// Literals, Identifiers and Operators
	@Override
	public Void visitCharacterLiteral(CharacterLiteral ast, Void arg) {
		return null;
	}

	@Override
	public Void visitIdentifier(Identifier ast, Frame frame) {
		if (ast.decl.entity instanceof KnownRoutine) {
			var address = ((KnownRoutine) ast.decl.entity).getAddress();
			emitter.emit(OpCode.CALL, frame.getDisplayRegister(address), Register.CB, address.getDisplacement());
		} else if (ast.decl.entity instanceof UnknownRoutine) {
			var address = ((UnknownRoutine) ast.decl.entity).getAddress();
			emitter.emit(OpCode.LOAD, Machine.closureSize, frame.getDisplayRegister(address),
					address.getDisplacement());
			emitter.emit(OpCode.CALLI, 0);
		} else if (ast.decl.entity instanceof PrimitiveRoutine) {
			var primitive = ((PrimitiveRoutine) ast.decl.entity).getPrimitive();
			if (primitive != Primitive.ID)
				emitter.emit(OpCode.CALL, Register.PB, primitive);
		} else if (ast.decl.entity instanceof EqualityRoutine) { // "=" or "\="
			var primitive = ((EqualityRoutine) ast.decl.entity).getPrimitive();
			emitter.emit(OpCode.LOADL, 0, frame.getSize() / 2);
			emitter.emit(OpCode.CALL, Register.PB, primitive);
		}
		return null;
	}

	@Override
	public Void visitIntegerLiteral(IntegerLiteral ast, Void arg) {
		return null;
	}

	@Override
	public Void visitOperator(Operator ast, Frame frame) {
		if (ast.decl.entity instanceof KnownRoutine) {
			var address = ((KnownRoutine) ast.decl.entity).getAddress();
			emitter.emit(OpCode.CALL, frame.getDisplayRegister(address), Register.CB, address.getDisplacement());
		} else if (ast.decl.entity instanceof UnknownRoutine) {
			var address = ((UnknownRoutine) ast.decl.entity).getAddress();
			emitter.emit(OpCode.LOAD, Machine.closureSize, frame.getDisplayRegister(address),
					address.getDisplacement());
			emitter.emit(OpCode.CALLI, 0);
		} else if (ast.decl.entity instanceof PrimitiveRoutine) {
			var primitive = ((PrimitiveRoutine) ast.decl.entity).getPrimitive();
			if (primitive != Primitive.ID)
				emitter.emit(OpCode.CALL, Register.PB, primitive);
		} else if (ast.decl.entity instanceof EqualityRoutine) { // "=" or "\="
			var primitive = ((EqualityRoutine) ast.decl.entity).getPrimitive();
			emitter.emit(OpCode.LOADL, 0, frame.getSize() / 2);
			emitter.emit(OpCode.CALL, Register.PB, primitive);
		}
		return null;
	}

	// Value-or-variable names
	@Override
	public RuntimeEntity visitDotVname(DotVname ast, Frame frame) {
		var baseObject = ast.V.visit(this, frame);
		ast.offset = ast.V.offset + ((Field) ast.I.decl.entity).getFieldOffset();
		// I.decl points to the appropriate record field
		ast.indexed = ast.V.indexed;
		return baseObject;
	}

	@Override
	public RuntimeEntity visitSimpleVname(SimpleVname ast, Frame frame) {
		ast.offset = 0;
		ast.indexed = false;
		return ast.I.decl.entity;
	}

	@Override
	public RuntimeEntity visitSubscriptVname(SubscriptVname ast, Frame frame) {
		var baseObject = ast.V.visit(this, frame);
		ast.offset = ast.V.offset;
		ast.indexed = ast.V.indexed;
		var elemSize = ast.type.visit(this);
		if (ast.E instanceof IntegerExpression) {
			var IL = ((IntegerExpression) ast.E).IL;
			ast.offset = ast.offset + Integer.parseInt(IL.spelling) * elemSize;
		} else {
			// v-name is indexed by a proper expression, not a literal
			if (ast.indexed) {
				frame = frame.expand(Machine.integerSize);
			}
			ast.E.visit(this, frame);
			if (elemSize != 1) {
				emitter.emit(OpCode.LOADL, 0, elemSize);
				emitter.emit(OpCode.CALL, Register.PB, Primitive.MULT);
			}
			if (ast.indexed)
				emitter.emit(OpCode.CALL, Register.PB, Primitive.ADD);
			else {
				ast.indexed = true;
			}
		}
		return baseObject;
	}

	// Programs
	@Override
	public Void visitProgram(Program ast, Frame frame) {
		return ast.C.visit(this, frame);
	}

	public Encoder(Emitter emitter, ErrorReporter reporter) {
		this.emitter = emitter;
		this.reporter = reporter;

		elaborateStdEnvironment();
	}

	private Emitter emitter;

	private ErrorReporter reporter;

	// Generates code to run a program.
	// showingTable is true iff entity description details
	// are to be displayed.
	public final void encodeRun(Program program, boolean showingTable) {
		tableDetailsReqd = showingTable;
		// startCodeGeneration();
		program.visit(this, Frame.Initial);
		emitter.emit(OpCode.HALT);
	}

	// Decides run-time representation of a standard constant.
	private final void elaborateStdConst(Declaration constDeclaration, int value) {

		if (constDeclaration instanceof ConstDeclaration) {
			var decl = (ConstDeclaration) constDeclaration;
			var typeSize = decl.E.type.visit(this);
			decl.entity = new KnownValue(typeSize, value);
			writeTableDetails(constDeclaration);
		}
	}

	// Decides run-time representation of a standard routine.
	private final void elaborateStdPrimRoutine(Declaration routineDeclaration, Primitive primitive) {
		routineDeclaration.entity = new PrimitiveRoutine(Machine.closureSize, primitive);
		writeTableDetails(routineDeclaration);
	}

	private final void elaborateStdEqRoutine(Declaration routineDeclaration, Primitive primitive) {
		routineDeclaration.entity = new EqualityRoutine(Machine.closureSize, primitive);
		writeTableDetails(routineDeclaration);
	}

	private final void elaborateStdRoutine(Declaration routineDeclaration, int routineOffset) {
		routineDeclaration.entity = new KnownRoutine(Machine.closureSize, 0, routineOffset);
		writeTableDetails(routineDeclaration);
	}

	private final void elaborateStdEnvironment() {
		tableDetailsReqd = false;
		elaborateStdConst(StdEnvironment.falseDecl, Machine.falseRep);
		elaborateStdConst(StdEnvironment.trueDecl, Machine.trueRep);
		elaborateStdPrimRoutine(StdEnvironment.notDecl, Primitive.NOT);
		elaborateStdPrimRoutine(StdEnvironment.andDecl, Primitive.AND);
		elaborateStdPrimRoutine(StdEnvironment.orDecl, Primitive.OR);
		elaborateStdConst(StdEnvironment.maxintDecl, Machine.maxintRep);
		elaborateStdPrimRoutine(StdEnvironment.addDecl, Primitive.ADD);
		elaborateStdPrimRoutine(StdEnvironment.subtractDecl, Primitive.SUB);
		elaborateStdPrimRoutine(StdEnvironment.multiplyDecl, Primitive.MULT);
		elaborateStdPrimRoutine(StdEnvironment.divideDecl, Primitive.DIV);
		elaborateStdPrimRoutine(StdEnvironment.moduloDecl, Primitive.MOD);
		elaborateStdPrimRoutine(StdEnvironment.lessDecl, Primitive.LT);
		elaborateStdPrimRoutine(StdEnvironment.notgreaterDecl, Primitive.LE);
		elaborateStdPrimRoutine(StdEnvironment.greaterDecl, Primitive.GT);
		elaborateStdPrimRoutine(StdEnvironment.notlessDecl, Primitive.GE);
		elaborateStdPrimRoutine(StdEnvironment.chrDecl, Primitive.ID);
		elaborateStdPrimRoutine(StdEnvironment.ordDecl, Primitive.ID);
		elaborateStdPrimRoutine(StdEnvironment.eolDecl, Primitive.EOL);
		elaborateStdPrimRoutine(StdEnvironment.eofDecl, Primitive.EOF);
		elaborateStdPrimRoutine(StdEnvironment.getDecl, Primitive.GET);
		elaborateStdPrimRoutine(StdEnvironment.putDecl, Primitive.PUT);
		elaborateStdPrimRoutine(StdEnvironment.getintDecl, Primitive.GETINT);
		elaborateStdPrimRoutine(StdEnvironment.putintDecl, Primitive.PUTINT);
		elaborateStdPrimRoutine(StdEnvironment.geteolDecl, Primitive.GETEOL);
		elaborateStdPrimRoutine(StdEnvironment.puteolDecl, Primitive.PUTEOL);
		elaborateStdEqRoutine(StdEnvironment.equalDecl, Primitive.EQ);
		elaborateStdEqRoutine(StdEnvironment.unequalDecl, Primitive.NE);
	}

	boolean tableDetailsReqd;

	public static void writeTableDetails(AbstractSyntaxTree ast) {
	}

	// Generates code to fetch the value of a named constant or variable
	// and push it on to the stack.
	// currentLevel is the routine level where the vname occurs.
	// frameSize is the anticipated size of the local stack frame when
	// the constant or variable is fetched at run-time.
	// valSize is the size of the constant or variable's value.

	private void encodeStore(Vname V, Frame frame, int valSize) {

		var baseObject = (AddressableEntity) V.visit(this, frame);
		// If indexed = true, code will have been generated to load an index value.
		if (valSize > 255) {
			reporter.reportRestriction("can't store values larger than 255 words");
			valSize = 255; // to allow code generation to continue
		}

		baseObject.encodeStore(emitter, frame, valSize, V);
	}

	// Generates code to fetch the value of a named constant or variable
	// and push it on to the stack.
	// currentLevel is the routine level where the vname occurs.
	// frameSize is the anticipated size of the local stack frame when
	// the constant or variable is fetched at run-time.
	// valSize is the size of the constant or variable's value.

	private void encodeFetch(Vname V, Frame frame, int valSize) {

		var baseObject = (FetchableEntity) V.visit(this, frame);
		// If indexed = true, code will have been generated to load an index value.
		if (valSize > 255) {
			reporter.reportRestriction("can't load values larger than 255 words");
			valSize = 255; // to allow code generation to continue
		}

		baseObject.encodeFetch(emitter, frame, valSize, V);
	}

	// Generates code to compute and push the address of a named variable.
	// vname is the program phrase that names this variable.
	// currentLevel is the routine level where the vname occurs.
	// frameSize is the anticipated size of the local stack frame when
	// the variable is addressed at run-time.

	private void encodeFetchAddress(Vname V, Frame frame) {

		var baseObject = (AddressableEntity) V.visit(this, frame);
		baseObject.encodeFetchAddress(emitter, frame, V);
	}
}
