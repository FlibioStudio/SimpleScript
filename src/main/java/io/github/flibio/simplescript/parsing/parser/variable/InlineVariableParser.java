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

import io.github.flibio.simplescript.parsing.tokenizer.Token;
import io.github.flibio.simplescript.parsing.tokenizer.TokenTypes;
import io.github.flibio.simplescript.parsing.tokenizer.Tokenizer;
import io.github.flibio.simplescript.parsing.variable.Variable;
import io.github.flibio.simplescript.parsing.variable.VariableFunction;
import io.github.flibio.simplescript.parsing.variable.VariableProperties;
import io.github.flibio.simplescript.parsing.variable.VariableProperty;
import io.github.flibio.simplescript.parsing.variable.types.DefinedVariableType;
import io.github.flibio.simplescript.parsing.variable.types.DefinedVariableTypes;
import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableType;
import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableTypes;
import io.github.flibio.simplescript.parsing.variable.types.RuntimeVariableTypes.ParsedVarType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InlineVariableParser {

    public static String getRegex() {
        return "(([a-zA-Z ]+)|(\".*\")|([-]?[0-9]+(.[0-9]+)?))";
    }

    public static ParsedType parse(Tokenizer tokenizer, List<String> endChars, VariableFunction... functions) {
        String variableString = "";
        // Seperate the variable from the rest of the string
        Token token = tokenizer.nextToken();
        while (!isEnd(endChars, token.getValue().trim())) {
            // Add the quotes back to the token if it was a string
            variableString += token.getType().equals(TokenTypes.STRING) ? " \"" + token.getValue() + "\"" : " " + token.getValue();
            token = tokenizer.nextToken();
        }
        // Trim the variable and remove 'the'
        variableString = variableString.trim();
        if (variableString.startsWith("the")) {
            variableString = variableString.replaceFirst("the", "").trim();
        }
        // Check if the variable is a defined type
        for (DefinedVariableType vType : DefinedVariableTypes.values()) {
            if (vType.getFunctions().containsAll(Arrays.asList(functions))) {
                if (vType.getId().equalsIgnoreCase(variableString)) {
                    return new ParsedType(variableString, tokenizer);
                }
            }
        }
        // Check if the variable is a defined type with property
        String[] varArray = variableString.split(" ", 2);
        if (varArray.length == 2) {
            String variable = varArray[0];
            String property = varArray[1];
            for (DefinedVariableType vType : DefinedVariableTypes.values()) {
                if (vType.getFunctions().containsAll(Arrays.asList(functions))) {
                    if (vType.getId().equalsIgnoreCase(variable)) {
                        for (VariableProperty<?> prop : VariableProperties.values()) {
                            if (prop.getId().equalsIgnoreCase(property)) {
                                // The property is valid
                                return new ParsedType(variable + " " + property, tokenizer);
                            }
                        }
                    }
                }
            }
        }
        // Check if the variable is a normal type
        for (RuntimeVariableType<?> type : RuntimeVariableTypes.values()) {
            if (type.isValid(variableString)) {
                Optional<?> rOpt = type.parse(variableString);
                if (rOpt.isPresent()) {
                    Object raw = rOpt.get();
                    if (raw instanceof ParsedVarType<?>) {
                        ParsedVarType<?> pType = (ParsedVarType<?>) raw;
                        return new ParsedType(pType.getKey(), tokenizer, new Variable(pType.getKey(), pType.getResult(), type));
                    }
                }
            }
        }
        // The variable could not be parsed
        throw new VariableException("'" + variableString + "' could not be parsed to a valid variable!");
    }

    private static boolean isEnd(List<String> endChars, String tokenValue) {
        for (String endChar : endChars) {
            if (endChar.trim().equalsIgnoreCase(tokenValue.trim())) {
                return true;
            }
        }
        return false;
    }

    public static class ParsedType {

        private String result;
        private Tokenizer tokenizer;
        private List<Variable> variables = new ArrayList<>();

        public ParsedType(String result, Tokenizer tokenizer, Variable... vars) {
            this.result = result;
            this.tokenizer = tokenizer;
            this.variables = Arrays.asList(vars);
        }

        public String getResult() {
            return result;
        }

        public Tokenizer getTokenizer() {
            return tokenizer;
        }

        public List<Variable> getVariables() {
            return variables;
        }
    }

}
