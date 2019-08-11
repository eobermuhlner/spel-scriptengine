[![Build Status](https://api.travis-ci.org/eobermuhlner/jshell-scriptengine.svg?branch=master)](https://travis-ci.org/eobermuhlner/spel-scriptengine)
[![Code Coverage](https://badgen.net/codecov/c/github/eobermuhlner/spel-scriptengine)](https://codecov.io/gh/eobermuhlner/spel-scriptengine)
[![Open Issues](https://badgen.net/github/open-issues/eobermuhlner/spel-scriptengine)](https://github.com/eobermuhlner/spel-scriptengine/issues)
[![Commits](https://badgen.net/github/commits/eobermuhlner/spel-scriptengine)](https://github.com/eobermuhlner/spel-scriptengine/graphs/commit-activity)
[![Last Commits](https://badgen.net/github/last-commit/eobermuhlner/spel-scriptengine)](https://github.com/eobermuhlner/spel-scriptengine/graphs/commit-activity)
[![Maven Central - spel-scriptengine](https://img.shields.io/maven-central/v/ch.obermuhlner/spel-scriptengine.svg)](https://search.maven.org/artifact/ch.obermuhlner/spel-scriptengine)

# Spring Expression Language (SpEL)

The Spring Expression Language (SpEL) is a powerful expression language that supports querying and manipulating an object graph at runtime.

Refer to the
[5.1.x Spring Documentation](https://docs.spring.io/autorepo/docs/spring/5.1.x/spring-framework-reference/core.html#expressions)
for details.

## Using Spring Expression Language scripting engine in your projects 

To use the Spring Expression Language scripting you can either download
the newest version of the .jar file from the
[published releases on Github](https://github.com/eobermuhlner/spel-scriptengine/releases/)
or use the following dependency to
[Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cspel-scriptengine)
in your build script (please verify the version number to be the newest
release):

### Use Spring Expression Language scripting engine in Maven build

```xml
<dependency>
  <groupId>ch.obermuhlner</groupId>
  <artifactId>spel-scriptengine</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Use Spring Expression Language scripting engine in Gradle build

```gradle
repositories {
  mavenCentral()
}

dependencies {
  compile 'ch.obermuhlner:spel-scriptengine:1.0.0'
}
```

## Simple usage

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

## Compiling 

The `SpringExpressionScriptEngine` implements the `Compilable`
interface.

You can compile a script into a `CompiledScript` and execute it multiple
times with different bindings.

```java
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
``` 

The console output shows that the same compiled script was able to run
with different bindings, which where even of different runtime types.

```console
Result (Integer): 5
Result (String): aaabbb
``` 

