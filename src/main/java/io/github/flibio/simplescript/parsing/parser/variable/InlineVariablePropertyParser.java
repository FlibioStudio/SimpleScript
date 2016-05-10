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
package io.github.flibio.simplescript.parsing.parser.variable;

import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;
import io.github.flibio.simplescript.parsing.variable.VariableProperties;
import io.github.flibio.simplescript.parsing.variable.VariableProperty;

import java.util.List;

public class InlineVariablePropertyParser {

    public static boolean isValid(String tokenizerData, List<String> endChars) {
        final Tokenizer tokenizer = new Tokenizer(tokenizerData);
        String propertyString = "";
        // Seperate the property from the rest of the string
        String tokenValue = tokenizer.nextToken().getValue();
        while (!isEnd(endChars, tokenValue)) {
            propertyString += " " + tokenValue;
            tokenValue = tokenizer.nextToken().getValue();
        }
        // Parse the property
        VariableProperty<?> prop = VariableProperties.valueOf(propertyString);
        return prop != null;
    }

    public static ParsedProperty parse(Tokenizer tokenizer, List<String> endChars) {
        String propertyString = "";
        // Seperate the property from the rest of the string
        String tokenValue = tokenizer.nextToken().getValue();
        while (!isEnd(endChars, tokenValue)) {
            propertyString += " " + tokenValue;
            tokenValue = tokenizer.nextToken().getValue();
        }
        // Parse the property
        propertyString = propertyString.trim().toUpperCase().replaceAll(" ", "_");
        return new ParsedProperty(VariableProperties.valueOf(propertyString), tokenizer);
    }

    private static boolean isEnd(List<String> endChars, String tokenValue) {
        for (String endChar : endChars) {
            if (endChar.equalsIgnoreCase(tokenValue)) {
                return true;
            }
        }
        return false;
    }

    public static class ParsedProperty {

        private VariableProperty<?> result;
        private Tokenizer tokenizer;

        public ParsedProperty(VariableProperty<?> result, Tokenizer tokenizer) {
            this.result = result;
            this.tokenizer = tokenizer;
        }

        public VariableProperty<?> getProperty() {
            return result;
        }

        public Tokenizer getTokenizer() {
            return tokenizer;
        }
    }
}
