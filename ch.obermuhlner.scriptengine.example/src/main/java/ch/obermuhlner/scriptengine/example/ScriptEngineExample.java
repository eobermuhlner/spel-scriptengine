package ch.obermuhlner.scriptengine.example;

import ch.obermuhlner.scriptengine.spring.expression.SpringExpressionScriptEngine;

import javax.script.*;

public class ScriptEngineExample {
    public static void main(String[] args) {
        //runNashornExamples();
        runSpringExpressionExamples();
    }

    private static void runNashornExamples() {
        runExample("nashorn", "2+3");
    }

    private static void runSpringExpressionExamples() {
        runExample("spel", "2+3");

        runSpelBindingExample();
        runSpelRootBindingExample();
        runSpelCompileExample();
    }

    private static void runExample(String engineName, String script) {
        try {
            System.out.println("Engine: " + engineName);
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName(engineName);
            Object result = engine.eval(script);
            System.out.println("Result: " + result);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static void runSpelBindingExample() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("spel");

            engine.put("inputA", 2);
            engine.put("inputB", 3);
            engine.put("output", 0);

            String script = "" +
                    "#output = #inputA + #inputB";

            Object result = engine.eval(script);
            System.out.println("Result: " + result);

            Object output = engine.get("output");
            System.out.println("Output Variable: " + output);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static void runSpelRootBindingExample() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("spel");

            Person person = new Person();
            person.name = "Eric";
            person.birthYear = 1967;
            engine.put(SpringExpressionScriptEngine.ROOT, person);

            String script = "" +
                    "name+birthYear";

            Object result = engine.eval(script);
            System.out.println("Result: " + result);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static void runSpelCompileExample() {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("spel");
            Compilable compiler = (Compilable) engine;

            CompiledScript compiledScript = compiler.compile("#alpha + #beta");

            {
                Bindings bindings = engine.createBindings();

                bindings.put("alpha", 2);
                bindings.put("beta", 3);
                Object result = compiledScript.eval(bindings);
                System.out.println("Result (Integer): " + result);
            }

            {
                Bindings bindings = engine.createBindings();

                bindings.put("alpha", "aaa");
                bindings.put("beta", "bbb");
                Object result = compiledScript.eval(bindings);
                System.out.println("Result (String): " + result);
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
