package cz.fi.muni.pa165.currency;

import java.math.BigDecimal;
import java.util.Currency;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        logger.trace("Convert() method:\n");
        if (sourceCurrency == null) {
            logger.warn("sourceCurrency is null\n");
            throw new IllegalArgumentException("sourceCurrency is null");
        }
        if (targetCurrency == null) {
            logger.warn("targetCurrency is null\n");
            throw new IllegalArgumentException("targetCurrency is null");
        }
        if (sourceAmount == null) {
            logger.warn("sourceAmount is null\n");
            throw new IllegalArgumentException("sourceAmount is null");
        }
        try {
            BigDecimal exchangeRate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
            if (exchangeRate == null) {
                logger.warn("ExchangeRate is missing\n");
                throw new UnknownExchangeRateException("ExchangeRate is unknown");
            }
            return exchangeRate.multiply(sourceAmount).setScale(2, RoundingMode.HALF_EVEN);
        } catch (ExternalServiceFailureException ex) {
            logger.error("Error when fetching exchange rate\n");
            throw new UnknownExchangeRateException("Error when fetching exchange rate", ex);
        }
    }

}
