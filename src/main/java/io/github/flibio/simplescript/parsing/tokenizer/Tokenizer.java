/*
 * This file is part of SimpleScript, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 - 2016 Flibio
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.flibio.simplescript.parsing.tokenizer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;

public class Tokenizer {

    private String string;
    private List<TokenType> tokenTypes = new ArrayList<>(EnumSet.allOf(TokenTypes.class));

    public Tokenizer(String string) {
        this.string = string;
    }

    public Token nextToken() {
        string = string.trim();

        if (string.isEmpty()) {
            return Token.of("", TokenTypes.EMPTY);
        }

        for (TokenType tokenType : tokenTypes) {
            Matcher matcher = tokenType.getPattern().matcher(string);
            // Check if the string matches the regex pattern
            if (matcher.find()) {
                String tokenValue = matcher.group().trim();
                string = matcher.replaceFirst("");

                // If token is a string remove the quotes before returning
                return tokenType.equals(TokenTypes.STRING) ? Token.of(tokenValue.substring(1, tokenValue.length() - 1), tokenType) : Token.of(
                        tokenValue, tokenType);
            }
        }

        // The string could not be parsed to a token
        throw new InvalidTokenException("Invalid token found in '" + string + "'");
    }
}
