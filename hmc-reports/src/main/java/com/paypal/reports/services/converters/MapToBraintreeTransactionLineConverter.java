package com.paypal.reports.services.converters;

import com.paypal.infrastructure.support.converter.Converter;
import com.paypal.infrastructure.support.date.DateUtil;
import com.paypal.reports.model.HmcBraintreeTransactionLine;
import com.paypal.reports.model.graphql.braintree.paymentransaction.BraintreeNodeGraphQLModel;
import com.paypal.reports.model.graphql.braintree.paymentransaction.BraintreeTransactionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class MapToBraintreeTransactionLineConverter extends AbstractMapToBraintreeLineConverter
		implements Converter<Map<String, Object>, HmcBraintreeTransactionLine> {

	@Override
	public HmcBraintreeTransactionLine convert(final Map<String, Object> source) {

		final BraintreeNodeGraphQLModel braintreeNodeGraphQLModel = getBraintreeNodeGraphQLModel(source);

		if (Objects.isNull(braintreeNodeGraphQLModel)) {
			return null;
		}
		//@formatter:off
		return HmcBraintreeTransactionLine.builder()
				.paymentTransactionId((Optional.ofNullable((String) source.get("legacyId")).orElse(StringUtils.EMPTY)))
				.orderId(braintreeNodeGraphQLModel.getOrderId())
				.amount(braintreeNodeGraphQLModel.getAmount().getValue())
				.currencyIsoCode(braintreeNodeGraphQLModel.getAmount().getCurrencyCode())
				.paymentTransactionTime(DateUtil.convertToLocalDateTime(braintreeNodeGraphQLModel.getCreatedAt()))
				.transactionType(BraintreeTransactionTypeEnum.OPERATOR_ORDER_AMOUNT.name())
				.build();
		//@formatter:on
	}

}
