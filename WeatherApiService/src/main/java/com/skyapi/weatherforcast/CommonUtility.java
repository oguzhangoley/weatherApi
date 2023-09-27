package com.skyapi.weatherforcast;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtility {

    private static Logger logger = LoggerFactory.getLogger(CommonUtility.class);

    public static String getIPAddress(HttpServletRequest request)
    {
        String ip = request.getHeader("X-FORWARED-FOR");

        if(ip==null || ip.isEmpty())
        {
            ip = request.getRemoteAddr();
        }
        logger.info("Clients IP Address: "+ip);
        return ip;
    }
}
