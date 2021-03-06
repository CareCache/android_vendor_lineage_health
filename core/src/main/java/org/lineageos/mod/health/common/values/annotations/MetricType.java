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

package org.lineageos.mod.health.common.values.annotations;

import androidx.annotation.IntDef;

import org.lineageos.mod.health.common.Metric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link IntDef} annotation to mark that a value
 * needs to be one of the following:
 *
 * <ul>
 *     <li>{@link Metric#CYCLING}</li>
 *     <li>{@link Metric#RUNNING}</li>
 *     <li>{@link Metric#WALKING}</li>
 *     <li>{@link Metric#WORKOUT}</li>
 *     <li>{@link Metric#ABDOMINAL_CIRCUMFERENCE}</li>
 *     <li>{@link Metric#BODY_MASS_INDEX}</li>
 *     <li>{@link Metric#BODY_TEMPERATURE}</li>
 *     <li>{@link Metric#LEAN_BODY_MASS}</li>
 *     <li>{@link Metric#MENSTRUAL_CYCLE}</li>
 *     <li>{@link Metric#UV_INDEX}</li>
 *     <li>{@link Metric#LEAN_BODY_MASS}</li>
 *     <li>{@link Metric#WATER_INTAKE}</li>
 *     <li>{@link Metric#WEIGHT}</li>
 *     <li>{@link Metric#INHALER_USAGE}</li>
 *     <li>{@link Metric#OXYGEN_SATURATION}</li>
 *     <li>{@link Metric#PEAK_EXPIRATORY_FLOW}</li>
 *     <li>{@link Metric#RESPIRATORY_RATE}</li>
 *     <li>{@link Metric#VITAL_CAPACITY}</li>
 *     <li>{@link Metric#BLOOD_ALCOHOL_CONCENTRATION}</li>
 *     <li>{@link Metric#BLOOD_PRESSURE}</li>
 *     <li>{@link Metric#GLUCOSE}</li>
 *     <li>{@link Metric#HEART_RATE}</li>
 *     <li>{@link Metric#PERFUSION_INDEX}</li>
 *     <li>{@link Metric#MEDITATION}</li>
 *     <li>{@link Metric#MOOD}</li>
 *     <li>{@link Metric#SLEEP}</li>
 * </ul>
 */
@IntDef({
        // Activity
        Metric.CYCLING,
        Metric.RUNNING,
        Metric.WALKING,
        Metric.WORKOUT,
        // Body
        Metric.ABDOMINAL_CIRCUMFERENCE,
        Metric.BODY_MASS_INDEX,
        Metric.BODY_TEMPERATURE,
        Metric.LEAN_BODY_MASS,
        Metric.MENSTRUAL_CYCLE,
        Metric.UV_INDEX,
        Metric.WATER_INTAKE,
        Metric.WEIGHT,
        // Breathing
        Metric.INHALER_USAGE,
        Metric.OXYGEN_SATURATION,
        Metric.PEAK_EXPIRATORY_FLOW,
        Metric.RESPIRATORY_RATE,
        Metric.VITAL_CAPACITY,
        // Heart and blood
        Metric.BLOOD_ALCOHOL_CONCENTRATION,
        Metric.BLOOD_PRESSURE,
        Metric.GLUCOSE,
        Metric.HEART_RATE,
        Metric.PERFUSION_INDEX,
        // Mindfulness
        Metric.MEDITATION,
        Metric.MOOD,
        Metric.SLEEP
})
@Retention(RetentionPolicy.SOURCE)
public @interface MetricType {
}
