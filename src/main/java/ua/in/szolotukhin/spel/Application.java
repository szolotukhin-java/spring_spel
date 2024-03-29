package ua.in.szolotukhin.spel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import ua.in.szolotukhin.spel.accessor.RowAccessor;
import ua.in.szolotukhin.spel.model.Parameters;
import ua.in.szolotukhin.spel.model.Row;
import ua.in.szolotukhin.spel.model.Table;
import ua.in.szolotukhin.spel.accessor.ValueAccessor;

import java.util.List;

@Slf4j
@SuppressWarnings({"ConstantConditions", "unchecked"})
public class Application {
	public static void main(String[] args) {
		Parameters parameters = Parameters.of(
				Table.of("T1", "T 1 description",
						Row.of("C1", "Value 1",
								"C2", "Value 2",
								"C3", "Value 3"),
						Row.of("C1", "Value 4")),
				Table.of("T2", "T 2 description"),
				Table.of("T3", "T 3 description"));

		StandardEvaluationContext context = new StandardEvaluationContext();
		context.addPropertyAccessor(new RowAccessor());
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("T1");
		context.setRootObject(parameters);
        List<Row> rows = (List<Row>) exp.getValue(context);

		StandardEvaluationContext ctx = new StandardEvaluationContext();
		ctx.addPropertyAccessor(new ValueAccessor());

		for (Row row : rows) {
			ctx.setRootObject(row);
			ExpressionParser prs = new SpelExpressionParser();
			Expression exp1 = prs.parseExpression("C1");
			Object value = exp1.getValue(ctx);

			log.info("Column value: {}", value);
		}
	}
}
