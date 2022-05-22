package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Aggregates.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.Aggregates.SingleRecordAggregate;

public interface RecordAggregateVisitor<TArg, TResult> {

	TResult visitMultipleRecordAggregate(MultipleRecordAggregate ast, TArg arg);

	TResult visitSingleRecordAggregate(SingleRecordAggregate ast, TArg arg);

}
