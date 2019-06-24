package com.luo.poi.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.UUID;

/**
 * @author luosen
 * @version 0.0.1
 * @date 2018/12/12
 * @time 13:34
 * @function 功能:
 * @describe 版本描述:
 * @modifyLog 修改日志:
 */
public class SerialNumberUtil {

    /**
     * 产生唯一 的序列号。
     *
     *
     * @return String
     */
    public static String getSerialNumber()
    {
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0)
        {
            hashCode = -hashCode;
        }
        return DateFormatUtils.format(new Date(), "yyyyMMdd").substring(0, 8) + String.format("%010d", hashCode);
    }
}
