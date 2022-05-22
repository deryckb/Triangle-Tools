/*
 * @(#)EqualityRoutine.java                        2.1 2003/10/07
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

package Triangle.CodeGenerator.Entities;

import Triangle.AbstractMachine.OpCode;
import Triangle.AbstractMachine.Primitive;
import Triangle.AbstractMachine.Register;
import Triangle.CodeGenerator.Emitter;
import Triangle.CodeGenerator.Frame;

public class EqualityRoutine extends RuntimeEntity implements RoutineEntity {

	private final Primitive primitive;

	public EqualityRoutine(int size, Primitive primitive) {
		super(size);
		this.primitive = primitive;
	}

	public final Primitive getPrimitive() {
		return primitive;
	}

	public void encodeCall(Emitter emitter, Frame frame) {
		emitter.emit(OpCode.LOADL, frame.getSize() / 2);
		emitter.emit(OpCode.CALL, Register.PB, primitive);
	}

	public void encodeFetch(Emitter emitter, Frame frame) {
		emitter.emit(OpCode.LOADA, 0, Register.SB, 0);
		emitter.emit(OpCode.LOADA, Register.PB, primitive);
	}

}
