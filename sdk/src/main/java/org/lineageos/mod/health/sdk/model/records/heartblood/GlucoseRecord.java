/*
 * Copyright (C) 2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.mod.health.sdk.model.records.heartblood;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import org.lineageos.mod.health.common.Metric;
import org.lineageos.mod.health.common.values.MealRelation;
import org.lineageos.mod.health.sdk.model.values.BloodGlucoseValue;
import org.lineageos.mod.health.sdk.model.values.PressureValue;

/**
 * Blood glucose record.
 *
 * <ul>
 *     <li>{@link Long} id: db identifier (default to <code>0L</code>)</li>
 *     <li>{@link Long} time: timestamp ({@link System#currentTimeMillis()})</li>
 *     <li>{@link Double} value: blood glucose level in milligrams per deciliter (mg/dL)</li>
 *     <li>{@link Integer} mealRelation: {@link MealRelation}</li>
 * </ul>
 *
 * <a href="https://en.wikipedia.org/wiki/Blood_sugar_level">More info</a>
 *
 * @see Metric#GLUCOSE
 */
@Keep
public final class GlucoseRecord extends HeartBloodRecord<BloodGlucoseValue> {

    public GlucoseRecord(long id, long time,
                         @MealRelation.Value int mealRelation,
                         @NonNull BloodGlucoseValue value) {
        super(id, Metric.GLUCOSE, time, mealRelation,
                PressureValue.ZERO, PressureValue.ZERO, value);
    }

    @NonNull
    public BloodGlucoseValue getValue() {
        return super.getValue();
    }

    public void setValue(@NonNull BloodGlucoseValue value) {
        super.setValue(value);
    }

    /**
     * @see MealRelation
     */
    @MealRelation.Value
    @Override
    public int getMealRelation() {
        return super.getMealRelation();
    }

    /**
     * @see MealRelation
     */
    @Override
    public void setMealRelation(@MealRelation.Value int mealRelation) {
        super.setMealRelation(mealRelation);
    }

    @Override
    protected double valueAsDouble() {
        return value.mmolL();
    }
}
