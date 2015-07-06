package com.stansvec.dropwizard.auth;

/**
 * The class use MVEL expression contained in Auth type to evaluate the authorization condition.
 * The class performs caching of expressions to achieve better performance.
 *
 * @author Stan Svec
 * Date: 4/10/13
 */
public class MvelExpressionEvaluation<R> {

    private static final String ALWAYS_TRUE_EXP = "";

    /*private final MvelVariableProvider<R> variableProvider;

    private final VariableResolverFactory commonVariables;*/

/*    private final ConcurrentMap<String, Serializable> expressionCache;

    public MvelExpressionEvaluation(MvelVariableProvider<R> variableProvider) {
        this.variableProvider = variableProvider;
        this.commonVariables = variableProvider.createCommonVariables();
        this.expressionCache = new ConcurrentHashMap<String, Serializable>();
    }
*/
    public boolean evaluate(R res, Auth auth/*, HttpContext httpContext*/) {
        if (ALWAYS_TRUE_EXP.equals(auth.exp())) {
            return true;
        }
        /*VariableResolverFactory vars = variableProvider.createPerRequestVariables(res, httpContext);
        vars.setNextFactory(commonVariables);
        Serializable exp = expressionCache.get(auth.exp());
        if (exp == null) {
            exp = MVEL.compileExpression(auth.exp());
            expressionCache.put(auth.exp(), exp);
        }
        return (Boolean) MVEL.executeExpression(exp, vars);*/
        return false;
    }
}
