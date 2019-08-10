package ch.obermuhlner.scriptengine.example;

import ch.obermuhlner.scriptengine.spring.expression.SpringExpressionScriptEngine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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
}
