package com.ntnu.gidd.service.activity.expression;

import com.ntnu.gidd.model.GeoLocation;
import com.ntnu.gidd.model.QActivity;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.querydsl.core.types.dsl.MathExpressions.*;
import static com.querydsl.core.types.dsl.MathExpressions.radians;

public class ActivityExpression {

    private static final int EARTH_RADIUS_IN_KM = 6371;
    private Predicate predicate;
    private Map<ActivityExpressionType, Expression<?>> expressions;

    private ActivityExpression(Predicate predicate) {
        this.predicate = predicate;
        this.expressions = new HashMap<>();
    }

    public static ActivityExpression of(Predicate predicate) {
        return new ActivityExpression(predicate);
    }

    public ActivityExpression closestTo(GeoLocation position) {
        QActivity activity = QActivity.activity;

        NumberPath<Double> lat = activity.geoLocation.id.lat;
        NumberPath<Double> lng = activity.geoLocation.id.lng;

        Expression<Double> positionLat = Expressions.constant(position.getLat());
        Expression<Double> positionLng = Expressions.constant(position.getLng());

        NumberExpression<Double> formula = acos(sin(radians(lat)).multiply(sin(radians(positionLat))).add(cos(radians(lat)).multiply(cos(radians(positionLat))).multiply(cos(radians(positionLng).subtract(radians(lng))))))
                .multiply(EARTH_RADIUS_IN_KM);

        expressions.put(ActivityExpressionType.CLOSEST_TO, formula);
        return this;
    }

    public ActivityExpression range(Double range) {
        NumberExpression<Double> formula = (NumberExpression<Double>) expressions.get(ActivityExpressionType.CLOSEST_TO);
        predicate = ExpressionUtils.allOf(predicate, formula.lt(range));
        return this;
    }

    public Predicate toPredicate() {
        return predicate;
    }

    @RequiredArgsConstructor
    private enum ActivityExpressionType {
        CLOSEST_TO("CLOSEST_TO");
        private final String type;
    }
}
