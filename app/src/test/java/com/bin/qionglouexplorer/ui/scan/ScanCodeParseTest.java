package com.bin.qionglouexplorer.ui.scan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** 二维码编码解析：三种合法输入形式 + 非法输入。 */
public class ScanCodeParseTest {

    @Test
    public void fullPrefixIsParsed() {
        assertEquals(1002, ScanActivity.parsePointId("QIONGLOU:POINT:1002"));
    }

    @Test
    public void plainNumberIsParsed() {
        assertEquals(1005, ScanActivity.parsePointId("1005"));
    }

    @Test
    public void urlWrappedCodeIsParsed() {
        assertEquals(1007,
                ScanActivity.parsePointId("https://qionglou.example/p?code=QIONGLOU:POINT:1007"));
    }

    @Test
    public void lowercasePrefixIsParsed() {
        assertEquals(1003, ScanActivity.parsePointId("qionglou:point:1003"));
    }

    @Test
    public void invalidFormatsReturnMinusOne() {
        assertEquals(-1, ScanActivity.parsePointId(null));
        assertEquals(-1, ScanActivity.parsePointId(""));
        assertEquals(-1, ScanActivity.parsePointId("hello world"));
        assertEquals(-1, ScanActivity.parsePointId("QIONGLOU:POINT:abc"));
        assertEquals(-1, ScanActivity.parsePointId("QIONGLOU:"));
        assertEquals(-1, ScanActivity.parsePointId("other:POINT:1001"));
    }

    @Test
    public void whitespaceIsTrimmed() {
        assertEquals(1001, ScanActivity.parsePointId("  QIONGLOU:POINT:1001  "));
    }
}
