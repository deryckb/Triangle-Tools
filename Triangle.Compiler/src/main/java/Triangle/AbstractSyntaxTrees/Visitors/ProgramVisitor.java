package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Program;

public interface ProgramVisitor<TArg, TResult> {

	TResult visitProgram(Program ast, TArg arg);

}
