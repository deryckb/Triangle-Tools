package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Types.AnyTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.BoolTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.CharTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.ErrorTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.IntTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.Types.SimpleTypeDenoter;

public interface TypeDenoterVisitor<TArg, TResult> extends FieldTypeDenoterVisitor<TArg, TResult> {

	TResult visitAnyTypeDenoter(AnyTypeDenoter ast, TArg arg);

	TResult visitArrayTypeDenoter(ArrayTypeDenoter ast, TArg arg);

	TResult visitBoolTypeDenoter(BoolTypeDenoter ast, TArg arg);

	TResult visitCharTypeDenoter(CharTypeDenoter ast, TArg arg);

	TResult visitErrorTypeDenoter(ErrorTypeDenoter ast, TArg arg);

	TResult visitSimpleTypeDenoter(SimpleTypeDenoter ast, TArg arg);

	TResult visitIntTypeDenoter(IntTypeDenoter ast, TArg arg);

	TResult visitRecordTypeDenoter(RecordTypeDenoter ast, TArg arg);

}
