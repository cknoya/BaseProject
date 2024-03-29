package com.aigame.baselib.utils.verify;


import com.aigame.baselib.log.DebugLog;

/**
 * Created by songguobin on 2016/12/19.
 */
public class VerifyFactory {

    private static final String TAG = "VerifyFactory";

    /**
     * RSA校验
     */
    public static final int VERIFY_WAY_RSA = 1;

    /**
     * CRC校验,仅校验1024字节
     */
    public static final int VERIFY_WAY_CRC = 2;

    /**
     * MD5校验
     */
    public static final int VERIFY_WAY_MD5 = 3;

    /**
     * crc全量校验
     */
    public static final int VERIFY_WAY_CRC_FULL = 4;

    public static boolean verify(int verifyWay,String filepath,String sign){

        DebugLog.log(TAG,"verifyWay = " , verifyWay);

          switch (verifyWay){
              case VERIFY_WAY_RSA:
                  return new RSAVerification().verify(filepath,sign);
              case VERIFY_WAY_CRC:
                  return new CRCVerification().verify(filepath,sign);
              case VERIFY_WAY_MD5:
                  return new MDFiveVerification().verify(filepath,sign);
              case VERIFY_WAY_CRC_FULL:
                  return new CRCVerification().verifyCrcFull(filepath,sign);
              default:
                  return false;
          }

    }

}
