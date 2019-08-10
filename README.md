[![Build Status](https://api.travis-ci.org/eobermuhlner/jshell-scriptengine.svg?branch=master)](https://travis-ci.org/eobermuhlner/spel-scriptengine)
[![Code Coverage](https://badgen.net/codecov/c/github/eobermuhlner/spel-scriptengine)](https://codecov.io/gh/eobermuhlner/spel-scriptengine)
[![Open Issues](https://badgen.net/github/open-issues/eobermuhlner/spel-scriptengine)](https://github.com/eobermuhlner/spel-scriptengine/issues)
[![Commits](https://badgen.net/github/commits/eobermuhlner/spel-scriptengine)](https://github.com/eobermuhlner/spel-scriptengine/graphs/commit-activity)
[![Last Commits](https://badgen.net/github/last-commit/eobermuhlner/spel-scriptengine)](https://github.com/eobermuhlner/spel-scriptengine/graphs/commit-activity)

# Spring Expression Language

The Spring Expression Language (SpEL) is a powerful expression language that supports querying and manipulating an object graph at runtime.

Refer to the
[5.1.x Spring Documentation](https://docs.spring.io/autorepo/docs/spring/5.1.x/spring-framework-reference/core.html#expressions)
for details.

The following example shows how to execute a simple SpEL script with variables:
```java
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
```

The console output shows that the `output` variable was modified by the script:
```console
Result: 5
Output Variable: 5
```

Since SpEL has the concept of a root object that is passed into expression
the `SpringExpressionScriptEngine` provides a special variable `ROOT`.

```java
public class Person {
    public String name;
    public int birthYear;

    @Override
    public String toString() {
        return "Person{name=" + name + ", birthYear=" + birthYear + "}";
    }
}
```

```java
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
```

```console
Result: Eric1967
```




 