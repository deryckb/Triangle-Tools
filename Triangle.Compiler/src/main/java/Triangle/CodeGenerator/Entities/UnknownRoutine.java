/*
 * @(#)UnknownRoutine.java                        2.1 2003/10/07
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
import Triangle.CodeGenerator.Emitter;
import Triangle.CodeGenerator.Frame;

public class UnknownRoutine extends RuntimeEntity implements RoutineEntity {

	private final ObjectAddress address;

	public UnknownRoutine(int size, int level, int displacement) {
		super(size);
		address = new ObjectAddress(level, displacement);
	}

	public final ObjectAddress getAddress() {
		return address;
	}

	public void encodeCall(Emitter emitter, Frame frame) {
		emitter.emit(OpCode.LOAD, Machine.closureSize, frame.getDisplayRegister(address), address.getDisplacement());
		emitter.emit(OpCode.CALLI, 0);
	}

	public void encodeFetch(Emitter emitter, Frame frame) {
		emitter.emit(OpCode.LOAD, Machine.closureSize, frame.getDisplayRegister(address), address.getDisplacement());
	}

}