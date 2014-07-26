package com.lvl6.mobsters.conditions.dynamo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.lvl6.mobsters.conditions.framework.AbstractTypedConditionFactory;
import com.lvl6.mobsters.conditions.framework.IStringConditionFactory;

class StringConditionFactory 
	extends AbstractTypedConditionFactory<Condition>
	implements IStringConditionFactory<Condition>
{

	@Override
	public void inStringCollection(Collection<String> matchValues) {
		final ArrayList<AttributeValue> attrValList = 
			new ArrayList<>(matchValues.size());
		for( final String nextString : matchValues ) {
			attrValList.add(
				getStringAttrVal(nextString));
		}
		handleSuccess(
			new Condition()
				.withComparisonOperator(ComparisonOperator.IN)
				.withAttributeValueList(attrValList));
	}

	@Override
	public void isNull() {
		handleSuccess(
			new Condition()
				.withComparisonOperator(ComparisonOperator.NULL));
	}

	@Override
	public void isNotNull() {
		handleSuccess(
			new Condition()
				.withComparisonOperator(ComparisonOperator.NOT_NULL));
	}

	@Override
	public void in(String... matchValues) {
		final ArrayList<AttributeValue> attrValList = 
			new ArrayList<>(matchValues.length);
		for( final String nextString : matchValues ) {
			attrValList.add(
				getStringAttrVal(nextString));
		}
		handleSuccess(
			new Condition()
				.withComparisonOperator(ComparisonOperator.IN)
				.withAttributeValueList(attrValList));
	}

	@Override
	public void isString(String value) {
		addUnaryCondition(ComparisonOperator.EQ, value);
	}

	@Override
	public void isNotString(String value) {
		addUnaryCondition(ComparisonOperator.NE, value);
	}

	@Override
	public void after(String value) {
		addUnaryCondition(ComparisonOperator.GT, value);
	}

	@Override
	public void afterOrEquals(String value) {
		addUnaryCondition(ComparisonOperator.GE, value);
	}

	@Override
	public void before(String value) {
		addUnaryCondition(ComparisonOperator.LT, value);
	}

	@Override
	public void beforeOrEquals(String value) {
		addUnaryCondition(ComparisonOperator.LE, value);
	}

	@Override
	public void hasSubstring(String substring) {
		addUnaryCondition(ComparisonOperator.CONTAINS, substring);
	}

	@Override
	public void lacksSubstring(String substring) {
		addUnaryCondition(ComparisonOperator.NOT_CONTAINS, substring);
	}

	@Override
	public void hasPrefix(String prefix) {
		addUnaryCondition(ComparisonOperator.BEGINS_WITH, prefix);
	}

	@Override
	public void betweenStrings(String lower, String upper) {
		final ArrayList<AttributeValue> attrValList = 
			new ArrayList<>(2);
		attrValList.add(
			getStringAttrVal(lower));
		attrValList.add(
			getStringAttrVal(upper));
		handleSuccess(
			new Condition()
				.withComparisonOperator(ComparisonOperator.BETWEEN)
				.withAttributeValueList(attrValList));
	}

	private void addUnaryCondition(final ComparisonOperator operator, final String operand) {
		handleSuccess(
			new Condition()
				.withComparisonOperator(operator)
				.withAttributeValueList(
					Collections.singletonList(
						getStringAttrVal(operand))));
	}

	private AttributeValue getStringAttrVal(final String operand) {
		return new AttributeValue().withS(operand);
	}
}
