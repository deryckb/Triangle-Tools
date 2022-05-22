package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Expressions.ArrayExpression;
import Triangle.AbstractSyntaxTrees.Expressions.BinaryExpression;
import Triangle.AbstractSyntaxTrees.Expressions.CallExpression;
import Triangle.AbstractSyntaxTrees.Expressions.CharacterExpression;
import Triangle.AbstractSyntaxTrees.Expressions.EmptyExpression;
import Triangle.AbstractSyntaxTrees.Expressions.IfExpression;
import Triangle.AbstractSyntaxTrees.Expressions.IntegerExpression;
import Triangle.AbstractSyntaxTrees.Expressions.LetExpression;
import Triangle.AbstractSyntaxTrees.Expressions.RecordExpression;
import Triangle.AbstractSyntaxTrees.Expressions.UnaryExpression;
import Triangle.AbstractSyntaxTrees.Expressions.VnameExpression;

public interface ExpressionVisitor<TArg, TResult> {

	TResult visitArrayExpression(ArrayExpression ast, TArg arg);

	TResult visitBinaryExpression(BinaryExpression ast, TArg arg);

	TResult visitCallExpression(CallExpression ast, TArg arg);

	TResult visitCharacterExpression(CharacterExpression ast, TArg arg);

	TResult visitEmptyExpression(EmptyExpression ast, TArg arg);

	TResult visitIfExpression(IfExpression ast, TArg arg);

	TResult visitIntegerExpression(IntegerExpression ast, TArg arg);

	TResult visitLetExpression(LetExpression ast, TArg arg);

	TResult visitRecordExpression(RecordExpression ast, TArg arg);

	TResult visitUnaryExpression(UnaryExpression ast, TArg arg);

	TResult visitVnameExpression(VnameExpression ast, TArg arg);

}
