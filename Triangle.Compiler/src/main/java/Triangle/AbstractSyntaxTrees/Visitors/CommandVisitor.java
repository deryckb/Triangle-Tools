package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Commands.AssignCommand;
import Triangle.AbstractSyntaxTrees.Commands.CallCommand;
import Triangle.AbstractSyntaxTrees.Commands.EmptyCommand;
import Triangle.AbstractSyntaxTrees.Commands.IfCommand;
import Triangle.AbstractSyntaxTrees.Commands.LetCommand;
import Triangle.AbstractSyntaxTrees.Commands.SequentialCommand;
import Triangle.AbstractSyntaxTrees.Commands.WhileCommand;

public interface CommandVisitor<TArg, TResult> {

	TResult visitAssignCommand(AssignCommand ast, TArg arg);

	TResult visitCallCommand(CallCommand ast, TArg arg);

	TResult visitEmptyCommand(EmptyCommand ast, TArg arg);

	TResult visitIfCommand(IfCommand ast, TArg arg);

	TResult visitLetCommand(LetCommand ast, TArg arg);

	TResult visitSequentialCommand(SequentialCommand ast, TArg arg);

	TResult visitWhileCommand(WhileCommand ast, TArg arg);

}
