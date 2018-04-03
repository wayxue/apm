package com.yitaqi.test;

import com.yitaqi.collects.JdbcCollect;
import com.yitaqi.test.service.UserServiceImpl;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import oracle.jdbc.OracleDriver;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-15 9:50
 */
public class ApmAgentTest {

    @Test
    public void agentTest() {

        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserName("tiantian");
        userService.setUserAge("ningci", 17);
        userService.getUserName();
    }

    @Test
    public void jdbcTest() {
        new OracleDriver();
    }

}
