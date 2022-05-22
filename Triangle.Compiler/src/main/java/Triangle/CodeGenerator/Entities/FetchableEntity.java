package Triangle.CodeGenerator.Entities;

import Triangle.AbstractSyntaxTrees.Vnames.Vname;
import Triangle.CodeGenerator.Emitter;
import Triangle.CodeGenerator.Frame;

public interface FetchableEntity {

    void encodeFetch(Emitter emitter, Frame frame, int size, Vname vname);

}
