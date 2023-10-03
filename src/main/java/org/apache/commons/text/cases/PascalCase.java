/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.cases;

import java.util.ArrayList;
import java.util.List;

/**
 * Case implementation which parses and formats strings of the form 'MyPascalString'
 * <p>
 * PascalCase is a case where tokens are delimited by uppercase ASCII characters. Each parsed token
 * <b>must</b> begin with an uppercase character, but the case of the remaining token characters is
 * ignored and returned as-is.
 * </p>
 */
public final class PascalCase implements Case {

    /** constant reusable instance of this case. */
    public static final PascalCase INSTANCE = new PascalCase();

    /**
     * Constructs a new PascalCase instance.
     */
    private PascalCase() {
    }

    /**
     * Parses a PascalCase string into tokens.
     * <p>
     * String characters are iterated over and any time an upper case ASCII character is
     * encountered, that character is considered to be the start of a new token, with the character
     * itself included in the token. This method should never return empty tokens. The first
     * character of the string must be an uppercase ASCII character. No further restrictions are
     * placed on string contents.
     * </p>
     * @param string The Pascal Cased string to parse
     * @return the list of tokens found in the string
     * @throws IllegalArgumentException if the string does not begin with an uppercase ASCII alpha character
     */
    @Override
    public List<String> parse(String string) {
        List<String> tokens = new ArrayList<>();
        if (string.length() == 0) {
            return tokens;
        }
        if (!Character.isUpperCase(string.codePointAt(0))) {
            throw new IllegalArgumentException(createExceptionString(string.codePointAt(0), 0, "must be a Unicode uppercase letter"));
        }
        int strLen = string.length();
        int[] tokenCodePoints = new int[strLen];
        int tokenCodePointsOffset = 0;
        for (int i = 0; i < string.length();) {
            final int codePoint = string.codePointAt(i);
            if (Character.isUpperCase(codePoint)) {
                if (tokenCodePointsOffset > 0) {
                    tokens.add(new String(tokenCodePoints, 0, tokenCodePointsOffset));
                    tokenCodePoints = new int[strLen];
                    tokenCodePointsOffset = 0;
                }
                tokenCodePoints[tokenCodePointsOffset++] = codePoint;
                i += Character.charCount(codePoint);
            } else {
                tokenCodePoints[tokenCodePointsOffset++] = codePoint;
                i += Character.charCount(codePoint);
            }
        }
        tokens.add(new String(tokenCodePoints, 0, tokenCodePointsOffset));
        return tokens;
    }

    /**
     * Formats string tokens into a Pascal Case string.
     * <p>
     * Iterates the tokens and formats each one into a Pascal Case token. The first character of
     * the token must be an ASCII alpha character. This character is forced upper case in the
     * output. The remaining alpha characters of the token are forced lowercase. Any other
     * characters in the token are returned as-is. Empty tokens are not supported.
     * </p>
     * @param tokens The string tokens to be formatted into Pascal Case
     * @return the Pascal Case formatted string
     * @throws IllegalArgumentException if any token is empty String or does not begin with an ASCII alpha character
     */
    @Override
    public String format(Iterable<String> tokens) {
        StringBuilder formattedString = new StringBuilder();
        int tokenIndex = 0;
        for (String token : tokens) {
            if (token.length() == 0) {
                throw new IllegalArgumentException("Unsupported empty token at index " + tokenIndex);
            }
            for (int i = 0; i < token.length();) {
                final int codePoint = token.codePointAt(i);
                int codePointFormatted = codePoint;
                if (i == 0) {
                    //must uppercase
                    if (!Character.isUpperCase(codePoint)) {
                        codePointFormatted = Character.toUpperCase(codePoint);
                        if (codePoint == codePointFormatted || !Character.isUpperCase(codePointFormatted)) {
                            throw new IllegalArgumentException(createExceptionString(codePoint, i, "cannot be mapped to uppercase"));
                        }
                    }
                } else {
                    //only need to force lowercase if the letter is uppercase, otherwise just add it
                    if (Character.isUpperCase(codePoint)) {
                        codePointFormatted = Character.toLowerCase(codePoint);
                        if (codePoint == codePointFormatted || !Character.isLowerCase(codePointFormatted)) {
                            throw new IllegalArgumentException(createExceptionString(codePoint, i, "cannot be mapped to lowercase"));
                        }
                    }
                }
                formattedString.appendCodePoint(codePointFormatted);
                i += Character.charCount(codePoint);
            }
            tokenIndex++;
        }
        return formattedString.toString();
    }

    private static String createExceptionString(int codePoint, int index, String suffix) {
        return "Character '" + new String(new int[] {codePoint}, 0, 1) + "' with code point " + codePoint + " at index " + index + " " + suffix;
    }
}