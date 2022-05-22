package Triangle.CodeGenerator.Entities;

import Triangle.AbstractSyntaxTrees.Vnames.Vname;
import Triangle.CodeGenerator.Emitter;
import Triangle.CodeGenerator.Frame;

public abstract class AddressableEntity extends RuntimeEntity implements FetchableEntity {

	protected final ObjectAddress address;

	protected AddressableEntity(int size, int level, int displacement) {
		super(size);
		address = new ObjectAddress(level, displacement);
	}

	protected AddressableEntity(int size, Frame frame) {
		this(size, frame.getLevel(), frame.getSize());
	}

	public ObjectAddress getAddress() {
		return address;
	}

	public abstract void encodeStore(Emitter emitter, Frame frame, int size, Vname vname);

	public abstract void encodeFetchAddress(Emitter emitter, Frame frame, Vname vname);

	public abstract void encodeFetch(Emitter emitter, Frame frame, int size, Vname vname);
}
