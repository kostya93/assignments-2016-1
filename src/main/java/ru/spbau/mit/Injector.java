package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.*;


public final class Injector {
    private static Map<String, Object> instances = new HashMap<>();
    private static HashSet<String> markGlobal = new HashSet<>();

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */

    private Injector() {
    }
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class aClass = classLoader.loadClass(rootClassName);
        Constructor c = aClass.getConstructors()[0];
        Class[] parameterTypes = c.getParameterTypes();

        HashSet<String> markLocal = new HashSet<>();

        markGlobal.add(aClass.getCanonicalName());

        if (instances.containsKey(aClass.getCanonicalName())) {
            return instances.get(aClass.getCanonicalName());
        }


        List<Object> parameters = new ArrayList<>();

        for (Class param : parameterTypes) {
            for (String className : implementationClassNames) {
                Class tmp = Class.forName(className);
                if (param.isAssignableFrom(tmp)) {
                    if (markLocal.contains(className)) {
                        throw new AmbiguousImplementationException();
                    }
                    markLocal.add(className);

                    if (markGlobal.contains(className)) {
                        throw new InjectionCycleException();
                    }
                    if (instances.containsKey(className)) {
                        parameters.add(instances.get(className));
                        continue;
                    }
                    Object instance = Injector.initialize(className, implementationClassNames);
                    instances.put(className, instance);
                    parameters.add(Injector.initialize(className, implementationClassNames));
                }
            }
        }

        if (parameterTypes.length != parameters.size()) {
            throw new ImplementationNotFoundException();
        }

        List<Object> parametersInRightOrder = new ArrayList<>();
        int size = parameters.size();
        for (int i = 0; i < size; i++) { //types
            for (int j = 0; j < size; j++) { //objects
                Class parameterType = parameterTypes[i];
                if (parameterType.isAssignableFrom(parameters.get(j).getClass())) {
                    parametersInRightOrder.add(parameters.get(j));
                    break;
                }
            }
        }

        return c.newInstance(parametersInRightOrder.toArray());
    }
}
