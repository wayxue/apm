package com.yitaqi.collects;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-14 14:51
 */
public abstract class AbstractByteTransformCollect {

    private static Map<ClassLoader, ClassPool> classPoolMap = new ConcurrentHashMap<ClassLoader, ClassPool>();
    private static Logger logger = Logger.getLogger(AbstractByteTransformCollect.class.getName());


    public AbstractByteTransformCollect(Instrumentation instrumentation) {

        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

                if (loader == null) {
                    return null;
                }
                if (className == null || "".equals(className.trim())) {
                    return null;
                }
                className = className.replaceAll("/", ".");
                byte[] bytes;
                try {
                    bytes = AbstractByteTransformCollect.this.transform(loader, className);
                    return bytes;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
                return null;
            }
        });
    }

    /**
     * 插桩
     * @param loader
     * @param className 报名+类名
     * @return byte[] 插桩后的字节码
     */
    public abstract byte[] transform(ClassLoader loader, String className) throws Exception;

    protected static CtClass toCtClass(ClassLoader loader, String className) throws NotFoundException {

        if (!classPoolMap.containsKey(loader)) {
            ClassPool classPool = new ClassPool();
            classPool.insertClassPath(new LoaderClassPath(loader));
            classPoolMap.put(loader, classPool);
        }
        ClassPool classPool = classPoolMap.get(loader);
        className = className.replaceAll("/", ".");
        CtClass ctClass = classPool.get(className);
        return ctClass;
    }
}
