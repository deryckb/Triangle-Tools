package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Formals.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.Formals.VarFormalParameter;

public interface FormalParameterVisitor<TArg, TResult> {

	TResult visitConstFormalParameter(ConstFormalParameter ast, TArg arg);

	TResult visitFuncFormalParameter(FuncFormalParameter ast, TArg arg);

	TResult visitProcFormalParameter(ProcFormalParameter ast, TArg arg);

	TResult visitVarFormalParameter(VarFormalParameter ast, TArg arg);

}
