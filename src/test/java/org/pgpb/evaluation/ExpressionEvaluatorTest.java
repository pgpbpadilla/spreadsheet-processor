package org.pgpb.evaluation;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionEvaluatorTest {

    @DataProvider(name = "expressions")
    public Object [][] getExpressions() {
        return new Object[][] {
            {"4", "4"},
            {"=4", "4"},
            {"-4", String.valueOf(EvaluationError.INVALID_FORMAT)},
            {"=-4", String.valueOf(EvaluationError.INVALID_FORMAT)},
            {"A", String.valueOf(EvaluationError.INVALID_FORMAT)},
        };
    }
    @Test(dataProvider = "expressions")
    public void testEvaluate(String expression, String expected) {
        assertThat(ExpressionEvaluator.evaluate(expression)).isEqualTo(expected);
    }
}