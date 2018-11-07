package org.pgpb.evaluation;

import com.google.common.collect.ImmutableList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

public class ExpressionTokenizerTest {

    @DataProvider(name = "expressionData")
    public Object [][] expressionData(){
        return new Object[][] {
            {"2+4", ImmutableList.of("2", "+", "4")},
            {"4*5", ImmutableList.of("4", "*", "5")},
            {"4/5", ImmutableList.of("4", "/", "5")},
            {"4-5", ImmutableList.of("4", "-", "5")},
        };
    }

    @Test(dataProvider = "expressionData")
    public void testTokenize(String expression, List expected) {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer();
        assertThat(tokenizer.tokenize(expression))
            .isEqualTo(expected);
    }
}