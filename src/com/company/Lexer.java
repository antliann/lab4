package com.company;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private List<Token> lexems = new ArrayList<Token>();

    private boolean inComment = false;
    private boolean inQuotes = false;

    private String reserved = "if|else|switch|case|default|break|int|float|char|double|" +
            "long|for|while|do|void|goto|auto|signed|const|extern|register|" +
            "unsigned|return|continue|enum|sizeof|struct|typedef";
    private String directive = "#include|#define|#undef|#ifdef|#ifndef|#if|#else|#elif|#endif|#error|#pragma";
    private String whitespace = "\\s";
    private String header = "<[A-Za-z]+>";
    private String number = "\\b\\d+\n|\\b\\d+.\\d+|\\b\\d+e\\d+|\\b[\\dA-Fa-f]+";
    private String operator = ">=|!=|\\+\\+|--|==|\\+=|-=|\\*=|/=|<=|\\+|-|=|\\*|%|/|>|<|!|\\^|&|\\|?";
    private String punctuation = "\\(|\\)|\\[|\\]|\\{|}|,|;|:";
    private String quote = "\"";
    private String lineComment = "\\\\";
    private String startComment = "/\\*";
    private String endComment = "\\*/";
    private String identifier = "^[a-zA-Z_][a-zA-Z0-9_]*$";

    public void outResults()
    {
        for (Token t : lexems)
        {
            System.out.println(t.toString());
        }
    }

    private void addWord(StringBuilder w)
    {
        String word = w.toString();
        Token token;
        if (word.matches(reserved))
        {
            token = new Token(Lexem.RESERVED, word);
        }
        else if(word.matches(directive))
        {
            token = new Token(Lexem.DIRECTIVE, word);
        }
        else if(word.matches(header))
        {
            token = new Token(Lexem.HEADER, word);
        }
        else if(word.matches(number))
        {
            token = new Token(Lexem.NUMBER, word);
        }
        else if(word.matches(identifier))
        {
            token = new Token(Lexem.IDENTIFIER, word);
        }
        else
        {
            token = new Token(Lexem.ERROR, word);
        }
        lexems.add(token);
    }

    public void process(String l)
    {
        String line = l.trim();
        inQuotes = false;
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < line.length(); i++)
        {
            if (inComment)
            {
                if (i != (line.length() - 1))
                {
                    String doubleOp = Character.toString(line.charAt(i)) + Character.toString(line.charAt(i + 1));
                    if (doubleOp.matches(endComment))
                    {
                        inComment = false;
                    }
                }
            }
            else if (inQuotes)
            {
                if (Character.toString(line.charAt(i)).matches(quote))
                {
                    inQuotes = false;
                    word.append(line.charAt(i));

                    Token token = new Token(Lexem.SYMBOL, word.toString());
                    lexems.add(token);

                    word = new StringBuilder();
                }
                else
                {
                    word.append(line.charAt(i));
                }
            }
            else if (Character.toString(line.charAt(i)).matches(quote))
            {
                inQuotes = true;
                word.append(line.charAt(i));
            }
            else if (Character.toString(line.charAt(i)).matches(punctuation))
            {
                if (word.length() != 0)
                {
                    addWord(word);
                    word = new StringBuilder();
                }
                Token token = new Token(Lexem.PUNCTUATION, Character.toString(line.charAt(i)));
                lexems.add(token);
            }
            else if (Character.toString(line.charAt(i)).matches(operator))
            {
                if (word.length() != 0)
                {
                    addWord(word);
                    word = new StringBuilder();
                }
                if (i != (line.length() - 1))
                {
                    String doubleOp = Character.toString(line.charAt(i)) + Character.toString(line.charAt(i + 1));
                    if (doubleOp.matches(startComment))
                    {
                        inComment = true;
                    }
                    else if (doubleOp.matches(operator))
                    {
                        Token token = new Token(Lexem.OPERATOR, doubleOp);
                        lexems.add(token);
                        i++;
                    }
                    else
                    {
                        Token token = new Token(Lexem.OPERATOR, Character.toString(line.charAt(i)));
                        lexems.add(token);
                    }
                }
                else {
                    Token token = new Token(Lexem.OPERATOR, Character.toString(line.charAt(i)));
                    lexems.add(token);
                }
            }
            else if ((Character.toString(line.charAt(i)).matches(whitespace)))
            {
                if (word.length() != 0)
                {
                    addWord(word);
                    word = new StringBuilder();
                }
            }
            else {
                word.append(line.charAt(i));
            }
        }

    }

}