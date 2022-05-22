package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Terminals.Operator;

public interface OperatorVisitor<TArg, TResult> {

	TResult visitOperator(Operator ast, TArg arg);

}
