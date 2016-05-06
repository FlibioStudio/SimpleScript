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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineVariableParser {

    public static String getRegex() {
        return "([a-zA-Z ]+)";
    }

    public static String parse(Tokenizer tokenizer, String endChar, VariableFunction... functions) {
        String variableString = "";
        // Seperate the variable from the rest of the string
        String tokenValue = tokenizer.nextToken().getValue();
        while (!tokenValue.equalsIgnoreCase(endChar)) {
            variableString += " " + tokenValue;
            tokenValue = tokenizer.nextToken().getValue();
        }
        // Parse the variable
        variableString = variableString.trim();
        for (VariableType vType : getTypes(functions)) {
            if (vType.isValid(variableString)) {
                return vType.parse(variableString);
            }
        }
        throw new InvalidVariableException("'" + variableString + "' could not be parsed to a valid variable!");
    }

    private static List<VariableType> getTypes(VariableFunction... functions) {
        List<VariableType> types = Arrays.asList(VariableTypes.values());
        List<VariableType> retain = new ArrayList<>();
        for (VariableFunction func : functions) {
            retain.addAll(func.getTypes());
        }
        types.retainAll(retain);
        return types;
    }
}
