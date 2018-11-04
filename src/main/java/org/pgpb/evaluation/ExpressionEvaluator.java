package org.pgpb.evaluation;

public class ExpressionEvaluator {
    public static String evaluate(String content) {
        if (content.charAt(0) != '=') {
            try {
                Integer value = Integer.parseInt(content);
                if (value < 0) {
                    return formatError(String.valueOf(EvaluationError.INVALID_FORMAT));
                }
            } catch (NumberFormatException e) {
                return formatError(String.valueOf(EvaluationError.INVALID_FORMAT));
            }

            return content;
        }

        for (char c : content.toCharArray()) {

        }
        return content.substring(1);
    }

    private static String formatError(String message) {
        return "#" + message;
    }
}
