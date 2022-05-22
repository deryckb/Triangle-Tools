/*
 * @(#)UnknownAddress.java                        2.1 2003/10/07
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

import Triangle.AbstractMachine.Machine;
import Triangle.AbstractMachine.OpCode;
import Triangle.AbstractMachine.Primitive;
import Triangle.AbstractMachine.Register;
import Triangle.AbstractSyntaxTrees.Vnames.Vname;
import Triangle.CodeGenerator.Emitter;
import Triangle.CodeGenerator.Frame;

public class UnknownAddress extends AddressableEntity {

	public UnknownAddress(int size, int level, int displacement) {
		super(size, level, displacement);
	}

	public void encodeStore(Emitter emitter, Frame frame, int size, Vname vname) {

		emitter.emit(OpCode.LOAD, Machine.addressSize, frame.getDisplayRegister(address), address.getDisplacement());
		if (vname.indexed) {
			emitter.emit(OpCode.CALL, Register.PB, Primitive.ADD);
		}

		int offset = vname.offset;
		if (offset != 0) {
			emitter.emit(OpCode.LOADL, 0, offset);
			emitter.emit(OpCode.CALL, Register.PB, Primitive.ADD);
		}
		emitter.emit(OpCode.STOREI, size, 0);
	}

	public void encodeFetch(Emitter emitter, Frame frame, int size, Vname vname) {
		emitter.emit(OpCode.LOAD, Machine.addressSize, frame.getDisplayRegister(address), address.getDisplacement());

		if (vname.indexed) {
			emitter.emit(OpCode.CALL, Register.PB, Primitive.ADD);
		}

		int offset = vname.offset;
		if (offset != 0) {
			emitter.emit(OpCode.LOADL, offset);
			emitter.emit(OpCode.CALL, Register.PB, Primitive.ADD);
		}
		emitter.emit(OpCode.LOADI, size);
	}

	public void encodeFetchAddress(Emitter emitter, Frame frame, Vname vname) {

		emitter.emit(OpCode.LOAD, Machine.addressSize, frame.getDisplayRegister(address), address.getDisplacement());
		if (vname.indexed) {
			emitter.emit(OpCode.CALL, Register.PB, Primitive.ADD);
		}

		int offset = vname.offset;
		if (offset != 0) {
			emitter.emit(OpCode.LOADL, offset);
			emitter.emit(OpCode.CALL, Register.PB, Primitive.ADD);
		}
	}
}