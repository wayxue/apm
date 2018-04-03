package com.yitaqi.test;

import com.yitaqi.test.service.UserServiceImpl;
import javassist.*;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-20 10:46
 */
public class ConstructureTest {

    public void test() throws NotFoundException {
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new ClassClassPath(UserServiceImpl.class));
        CtClass ctClass = pool.get("com.yitaqi.test.service.UserServiceImpl");
        CtConstructor constructor = ctClass.getConstructor("()V");
    }
}
