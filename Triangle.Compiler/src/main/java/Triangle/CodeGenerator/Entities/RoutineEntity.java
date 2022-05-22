package Triangle.CodeGenerator.Entities;

import Triangle.CodeGenerator.Emitter;
import Triangle.CodeGenerator.Frame;

public interface RoutineEntity {

	void encodeCall(Emitter emitter, Frame frame);

	void encodeFetch(Emitter emitter, Frame frame);
}
