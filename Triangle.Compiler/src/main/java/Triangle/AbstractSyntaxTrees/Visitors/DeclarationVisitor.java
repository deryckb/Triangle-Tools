package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Declarations.BinaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.UnaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.Declarations.VarDeclaration;
import Triangle.AbstractSyntaxTrees.Types.TypeDeclaration;

public interface DeclarationVisitor<TArg, TResult> extends FormalParameterVisitor<TArg, TResult> {

	TResult visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, TArg arg);

	TResult visitConstDeclaration(ConstDeclaration ast, TArg arg);

	TResult visitFuncDeclaration(FuncDeclaration ast, TArg arg);

	TResult visitProcDeclaration(ProcDeclaration ast, TArg arg);

	TResult visitSequentialDeclaration(SequentialDeclaration ast, TArg arg);

	TResult visitTypeDeclaration(TypeDeclaration ast, TArg arg);

	TResult visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, TArg arg);

	TResult visitVarDeclaration(VarDeclaration ast, TArg arg);

}
