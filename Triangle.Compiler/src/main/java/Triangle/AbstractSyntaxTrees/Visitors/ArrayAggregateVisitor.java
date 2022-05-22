package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Aggregates.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.Aggregates.SingleArrayAggregate;

public interface ArrayAggregateVisitor<TArg, TResult> {

	TResult visitMultipleArrayAggregate(MultipleArrayAggregate ast, TArg arg);

	TResult visitSingleArrayAggregate(SingleArrayAggregate ast, TArg arg);

}
