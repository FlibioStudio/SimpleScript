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
import io.github.flibio.simplescript.parsing.variable.VariableFunction;
import io.github.flibio.simplescript.parsing.variable.VariableFunctions;

import java.util.List;

public class InlineVariableFunctionParser {

    public static boolean isValid(String tokenizerData, List<String> endChars) {
        Tokenizer tokenizer = new Tokenizer(tokenizerData);
        String functionString = "";
        // Seperate the function from the rest of the string
        String tokenValue = tokenizer.nextToken().getValue();
        while (!isEnd(endChars, tokenValue)) {
            functionString += " " + tokenValue;
            tokenValue = tokenizer.nextToken().getValue();
        }
        // Parse the function
        VariableFunction func = VariableFunctions.getEnum(functionString.trim().toUpperCase().replaceAll(" ", "_"));
        return func != null;
    }

    public static ParsedFunction parse(Tokenizer tokenizer, List<String> endChars) {
        String functionString = "";
        // Seperate the function from the rest of the string
        String tokenValue = tokenizer.nextToken().getValue();
        while (!isEnd(endChars, tokenValue)) {
            functionString += " " + tokenValue;
            tokenValue = tokenizer.nextToken().getValue();
        }
        // Parse the function
        functionString = functionString.trim().toUpperCase().replaceAll(" ", "_");
        return new ParsedFunction(VariableFunctions.getEnum(functionString), tokenizer);
    }

    private static boolean isEnd(List<String> endChars, String tokenValue) {
        for (String endChar : endChars) {
            if (endChar.equalsIgnoreCase(tokenValue)) {
                return true;
            }
        }
        return false;
    }

    public static class ParsedFunction {

        private VariableFunction result;
        private Tokenizer tokenizer;

        public ParsedFunction(VariableFunction result, Tokenizer tokenizer) {
            this.result = result;
            this.tokenizer = tokenizer;
        }

        public VariableFunction getFunction() {
            return result;
        }

        public Tokenizer getTokenizer() {
            return tokenizer;
        }
    }

}
