package com.yitaqi.collects;

import com.yitaqi.ApmContext;
import javassist.ClassPool;
import javassist.CtClass;

import java.lang.instrument.Instrumentation;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-20 10:15
 */
public class C3p0Collect extends AbstractByteTransformCollect {

    public static C3p0Collect INSTANCE;
    private final ApmContext context;
    private final static String targetClass = "com.mchange.v2.c3p0.ComboPooledDataSource";

    private static final String beginSrc;
    private static final String errorSrc;
    private static final String endSrc;

    public C3p0Collect(Instrumentation instrumentation, ApmContext context) {
        super(instrumentation);
        INSTANCE = this;
        this.context = context;
    }

    static {
        beginSrc = "com.yitaqi.collects.C3p0Collect instance = com.yitaqi.collects.C3p0Collect.INSTANCE;";
        errorSrc = "";
        endSrc = "";
    }

    @Override
    public byte[] transform(ClassLoader loader, String className) throws Exception {

        if (!className.equals(targetClass)) {
            return null;
        }
        CtClass ctClass = toCtClass(loader, className);
        ctClass.getConstructor("()V").insertAfter("System.getProperties().put(\"c3p0Source$agent\", $0);");
        byte[] bytes = ctClass.toBytecode();

        return bytes;
    }


}