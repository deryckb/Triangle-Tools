package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Types.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.SingleFieldTypeDenoter;

public interface FieldTypeDenoterVisitor<TArg, TResult> {

	TResult visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, TArg arg);

	TResult visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, TArg arg);

}
