package com.aigame.baselib.utils.verify;

import android.text.TextUtils;

import com.aigame.baselib.log.DebugLog;
import com.aigame.baselib.utils.crash.ExceptionUtils;
import com.aigame.baselib.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

/**
 * Created by songguobin on 2016/12/19.
 *
 * CRC校验
 *
 */
public class CRCVerification implements BaseVerification{

    public static final String TAG = "CRCVerification";

    public static final int BUFFER_SIZE_CRC = 1024;

    @Override
    public boolean verify(String filepath, String sign) {


        return checksumByCRC(new File(filepath),sign);

    }

    public boolean verifyCrcFull(String filepath, String sign) {
        if (TextUtils.isEmpty(filepath)) {
            return false;
        }
        File file = new File(filepath);
        byte[] buffer = getBytesBySize(file);
        if (null == buffer) {
            return false;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(buffer);
        String value = String.format("%08X", crc32.getValue());
        DebugLog.log(TAG, "filepath=", filepath, ", crc=", value);

        while (value != null && value.startsWith("0")) {
            value = value.substring(1);
        }

        while (sign != null && sign.startsWith("0")) {
            sign = sign.substring(1);
        }
        DebugLog.log(TAG, "value=", value, ", crc=", sign);
        return value != null && value.equalsIgnoreCase(sign);
    }

    private  boolean checksumByCRC(File f, String crcValue) {
        if (StringUtils.isEmpty(crcValue)) {
            return false;
        }
        byte[] buffer = getBytesBySize(f, BUFFER_SIZE_CRC);
        if (null == buffer) {
            return false;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(buffer);
        String value = Long.toHexString(crc32.getValue()).toUpperCase();
        DebugLog.log(TAG,"input crcValue = " , crcValue);
        DebugLog.log(TAG,"calc crcValue = " , value);
        return value.equals(crcValue);
    }

    private  byte[] getBytesBySize(File f, int size) {
        if (null == f || !f.exists() || !f.canRead() || size > BUFFER_SIZE_CRC) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            byte[] buffer = new byte[size];
            int readCount = fis.read(buffer);
            if (readCount > 0) {
                return buffer;
            }
        } catch (IOException e) {
            ExceptionUtils.printStackTrace(e);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    ExceptionUtils.printStackTrace(e);
                }
            }
        }
        return null;
    }

    private  byte[] getBytesBySize(File f) {
        if (null == f || !f.exists() || !f.canRead()) {
            return null;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(f);
            baos = new ByteArrayOutputStream();
            int len;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            ExceptionUtils.printStackTrace(e);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    ExceptionUtils.printStackTrace(e);
                }
            }
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    ExceptionUtils.printStackTrace(e);
                }
            }
        }
        return null;
    }



}
