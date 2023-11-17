package com.stit.utils.sql;

public class UrlUtil {

    private UrlUtil() {
    }

    ;

 /**
  * iso-8850-1 to utf-8, for uri/url
  * @param input
  * @return
  * @throws Exception 
  */
 public static String toUTF8(String input) throws Exception {
        return new String(input.getBytes("ISO-8859-1"), "UTF-8");
    }
}
