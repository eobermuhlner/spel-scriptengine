package ch.obermuhlner.scriptengine.spring.expression;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.script.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Compiled script to be executed in the Spring Expression Language (SpEL).
 */
public class SpringExpressionCompiledScript extends CompiledScript {
    private final SpringExpressionScriptEngine engine;
    private final Expression expression;
    private final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    SpringExpressionCompiledScript(SpringExpressionScriptEngine engine, Expression expression) {
        this.engine = engine;
        this.expression = expression;
    }

    @Override
    public Object eval(ScriptContext context) throws ScriptException {
        try {
            Bindings globalBindings = context.getBindings(ScriptContext.GLOBAL_SCOPE);
            Bindings engineBindings = context.getBindings(ScriptContext.ENGINE_SCOPE);

            Map<String, Object> variables = mergeBindings(globalBindings, engineBindings);
            evaluationContext.setVariables(variables);

            Object rootObject = variables.get(SpringExpressionScriptEngine.ROOT);

            Object result = expression.getValue(evaluationContext, rootObject);

            for (String key : variables.keySet()) {
                Object value = evaluationContext.lookupVariable(key);

                setBindingsValue(globalBindings, engineBindings, key, value);
            }

            return result;
        } catch (Exception ex) {
            throw new ScriptException(ex);
        }
    }

    @Override
    public ScriptEngine getEngine() {
        return engine;
    }

    private Map<String, Object> mergeBindings(Bindings... bindingsToMerge) {
        Map<String, Object> variables = new HashMap<>();

        for (Bindings bindings : bindingsToMerge) {
            if (bindings != null) {
                for (Map.Entry<String, Object> globalEntry : bindings.entrySet()) {
                    variables.put(globalEntry.getKey(), globalEntry.getValue());
                }
            }
        }

        return variables;
    }

    private void setBindingsValue(Bindings globalBindings, Bindings engineBindings, String name, Object value) {
        if (!engineBindings.containsKey(name) && globalBindings.containsKey(name)) {
            globalBindings.put(name, value);
        } else {
            engineBindings.put(name, value);
        }
    }
}
