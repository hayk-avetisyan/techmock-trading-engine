package am.techmock.trading.engine.core.internal.analysis;

import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public interface NamedIndicator extends Indicator<Num> {

	IndicatorType getType();
}
