package com.yitaqi.output;

import com.yitaqi.common.Logs;
import com.yitaqi.interfaces.IOutput;

import java.util.logging.Logger;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-14 16:02
 */
public class JulOutput implements IOutput {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void output2Log(Object value){

        logger.info(value.toString());
        Logs.info(value.toString());
    }
}
