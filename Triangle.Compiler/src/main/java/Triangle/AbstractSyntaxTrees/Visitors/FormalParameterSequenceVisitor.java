package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Formals.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Formals.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Formals.SingleFormalParameterSequence;

public interface FormalParameterSequenceVisitor<TArg, TResult> {

	TResult visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, TArg arg);

	TResult visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, TArg arg);

	TResult visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, TArg arg);

}
