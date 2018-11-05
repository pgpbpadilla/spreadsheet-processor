package org.pgpb.evaluation;

public class ExpressionEvaluator {
    public static String parseExpression(String expression) {
        String e = expression.substring(1);
        return parseTerm(e);
    }

    public static String parseTerm(String term) {
        try {
            Integer value = Integer.parseInt(term);
            if (value < 0) {
                return formatError(String.valueOf(EvaluationError.NEGATIVE_NUMBER));
            }
            return term;
        } catch (NumberFormatException e) {
            return formatError(String.valueOf(EvaluationError.INVALID_FORMAT));
        }
    }

    public static String evaluate(String content) {
        if ("".equals(content)) {
            return content;
        }

        if (content.charAt(0) == '=') {
            return parseExpression(content);
        }

        if (content.charAt(0) == '\'') {
            return content.substring(1);
        }

        return parseTerm(content);
    }

    private static String formatError(String message) {
        return "#" + message;
    }
}
