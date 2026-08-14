package com.hrm.hrmauto.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    public byte[] generateQRCode(String employeeCode)
            throws Exception {

        // Data stored inside QR
        String qrData = employeeCode;

        Map<EncodeHintType, Object> hints =
                new HashMap<>();

        hints.put(
                EncodeHintType.MARGIN,
                1
        );

        BitMatrix matrix =
                new MultiFormatWriter().encode(
                        qrData,
                        BarcodeFormat.QR_CODE,
                        100,
                        100,
                        hints
                );

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(
                matrix,
                "PNG",
                output
        );

        return output.toByteArray();
    }
}