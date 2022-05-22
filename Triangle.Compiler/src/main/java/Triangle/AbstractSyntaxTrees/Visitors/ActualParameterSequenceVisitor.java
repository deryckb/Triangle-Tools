package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Actuals.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.Actuals.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.Actuals.SingleActualParameterSequence;

public interface ActualParameterSequenceVisitor<TArg, TResult> {

	TResult visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, TArg arg);

	TResult visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, TArg arg);

	TResult visitSingleActualParameterSequence(SingleActualParameterSequence ast, TArg arg);

}
