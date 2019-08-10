package ch.obermuhlner.scriptengine.spring.expression;

import org.junit.Test;

import javax.script.ScriptEngine;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpringExpressionScriptEngineFactoryTest {
    @Test
    public void testGetEngineName() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getEngineName()).isEqualTo("Spring Expression Language");
    }

    @Test
    public void testGetEngineVersion() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getEngineVersion()).isEqualTo("0.1.0");
    }

    @Test
    public void testGetLanguageName() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getLanguageName()).isEqualTo("Spring Expression Language");
    }

    @Test
    public void testGetLanguageVersion() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getLanguageVersion()).isEqualTo("5.1.9");
    }

    @Test
    public void testGetExtensions() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getExtensions()).isEqualTo(Arrays.asList("spel"));
    }

    @Test
    public void testGetMimeTypes() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getMimeTypes()).isEqualTo(Arrays.asList("text/x-spel-source"));
    }

    @Test
    public void testGetNames() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getNames()).isEqualTo(Arrays.asList("spel", "SpEL", "SpringExpression", "ch.obermuhlner:spel-scriptengine", "obermuhlner-spel"));
    }

    @Test
    public void testGetParameters() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getParameter(ScriptEngine.ENGINE)).isEqualTo(factory.getEngineName());
        assertThat(factory.getParameter(ScriptEngine.ENGINE_VERSION)).isEqualTo(factory.getEngineVersion());
        assertThat(factory.getParameter(ScriptEngine.LANGUAGE)).isEqualTo(factory.getLanguageName());
        assertThat(factory.getParameter(ScriptEngine.LANGUAGE_VERSION)).isEqualTo(factory.getLanguageVersion());
        assertThat(factory.getParameter(ScriptEngine.NAME)).isEqualTo("spel");
        assertThat(factory.getParameter("unknown")).isEqualTo(null);
    }

    @Test
    public void testGetMethodCallSyntax() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getMethodCallSyntax("obj", "method")).isEqualTo("obj.method()");
        assertThat(factory.getMethodCallSyntax("obj", "method", "alpha")).isEqualTo("obj.method(alpha)");
        assertThat(factory.getMethodCallSyntax("obj", "method", "alpha", "beta")).isEqualTo("obj.method(alpha,beta)");
    }

    @Test
    public void testGetOutputStatement() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();

        assertThatThrownBy(() -> {
            factory.getOutputStatement("alpha");
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testGetProgram() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getProgram()).isEqualTo("");
        assertThat(factory.getProgram("alpha")).isEqualTo("alpha;\n");
        assertThat(factory.getProgram("alpha", "beta")).isEqualTo("alpha;\nbeta;\n");
    }

    @Test
    public void testGetScriptEngine() {
        SpringExpressionScriptEngineFactory factory = new SpringExpressionScriptEngineFactory();
        assertThat(factory.getScriptEngine() instanceof SpringExpressionScriptEngine).isTrue();
    }

}
