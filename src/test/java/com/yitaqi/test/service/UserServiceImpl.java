package com.yitaqi.test.service;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-15 9:52
 */
public class UserServiceImpl {

    public void setUserName(String name) {

        System.out.println(name);
    }

    public void setUserAge(String name, Integer age) {

        System.out.println(name + " is " + age + " years old");
    }

    public String getUserName() {

        System.out.println("god save the king");
        return "i could by your romantic sole";
    }
}
