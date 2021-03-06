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

package org.lineageos.mod.health.sdk.repo;

import android.content.ContentResolver;
import android.database.Cursor;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import org.lineageos.mod.health.common.CareCacheUri;
import org.lineageos.mod.health.common.Metric;
import org.lineageos.mod.health.common.db.RecordColumns;
import org.lineageos.mod.health.sdk.model.records.heartblood.BaseHeartBloodRecord;
import org.lineageos.mod.health.sdk.model.records.heartblood.BloodAlcoholConcentrationRecord;
import org.lineageos.mod.health.sdk.model.records.heartblood.BloodPressureRecord;
import org.lineageos.mod.health.sdk.model.records.heartblood.GlucoseRecord;
import org.lineageos.mod.health.sdk.model.records.heartblood.HeartBloodRecord;
import org.lineageos.mod.health.sdk.model.records.heartblood.HeartRateRecord;
import org.lineageos.mod.health.sdk.model.records.heartblood.PerfusionIndexRecord;
import org.lineageos.mod.health.sdk.model.values.BloodGlucoseValue;
import org.lineageos.mod.health.sdk.model.values.PressureValue;
import org.lineageos.mod.health.sdk.util.CcRuntimePermission;
import org.lineageos.mod.health.sdk.util.RecordTimeComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HEART_BLOOD records repository.
 * <p>
 * This class allows you to retrieve, insert, update and delete
 * hearth and blood records.
 * <p>
 * Supported records types:
 * <ul>
 *     <li>{@link BloodAlcoholConcentrationRecord}</li>
 *     <li>{@link BloodPressureRecord}</li>
 *     <li>{@link GlucoseRecord}</li>
 *     <li>{@link HeartRateRecord}</li>
 *     <li>{@link PerfusionIndexRecord}</li>
 * </ul>
 * <p>
 * Operations performed in this class should be executed outside of the
 * main / UI thread.
 *
 * @see HeartBloodRecord
 * @see BloodAlcoholConcentrationRecord
 * @see BloodPressureRecord
 * @see GlucoseRecord
 * @see HeartRateRecord
 * @see PerfusionIndexRecord
 */
@Keep
public final class HeartBloodRecordsRepo extends RecordsRepo<HeartBloodRecord<?>> {

    @Nullable
    private static volatile HeartBloodRecordsRepo instance;
    @NonNull
    private static final Object instanceLock = new Object();

    private HeartBloodRecordsRepo(@NonNull ContentResolver contentResolver) {
        super(contentResolver, CareCacheUri.HEART_BLOOD);
    }

    @NonNull
    public static HeartBloodRecordsRepo getInstance(
            @NonNull ContentResolver contentResolver) {
        HeartBloodRecordsRepo currentInstance = instance;
        // use double-checked locking
        // (https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java)
        if (currentInstance == null) {
            synchronized (instanceLock) {
                currentInstance = instance;
                if (currentInstance == null) {
                    currentInstance = new HeartBloodRecordsRepo(contentResolver);
                    instance = currentInstance;
                }
            }
        }

        return currentInstance;
    }

    @NonNull
    @Override
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public List<HeartBloodRecord<?>> getAll() {
        final List<HeartBloodRecord<?>> list = new ArrayList<>();
        list.addAll(getAllBloodAlcoholConcentrationRecords());
        list.addAll(getAllBloodPressureRecords());
        list.addAll(getAllGlucoseRecords());
        list.addAll(getAllHeartRateRecords());
        list.addAll(getAllPerfusionIndexRecords());
        list.sort(new RecordTimeComparator());
        return list;
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public List<BloodAlcoholConcentrationRecord> getAllBloodAlcoholConcentrationRecords() {
        return getByMetric(Metric.BLOOD_ALCOHOL_CONCENTRATION).parallelStream()
                .map(BloodAlcoholConcentrationRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public List<BloodPressureRecord> getAllBloodPressureRecords() {
        return getByMetric(Metric.BLOOD_PRESSURE).parallelStream()
                .map(BloodPressureRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public List<GlucoseRecord> getAllGlucoseRecords() {
        return getByMetric(Metric.GLUCOSE).parallelStream()
                .map(GlucoseRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public List<HeartRateRecord> getAllHeartRateRecords() {
        return getByMetric(Metric.HEART_RATE).parallelStream()
                .map(HeartRateRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public List<PerfusionIndexRecord> getAllPerfusionIndexRecords() {
        return getByMetric(Metric.PERFUSION_INDEX).parallelStream()
                .map(PerfusionIndexRecord.class::cast)
                .collect(Collectors.toList());
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public BloodAlcoholConcentrationRecord getBloodAlcoholConcentrationRecord(long id) {
        return (BloodAlcoholConcentrationRecord) getById(Metric.BLOOD_ALCOHOL_CONCENTRATION, id);
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public BloodPressureRecord getBloodPressureRecord(long id) {
        return (BloodPressureRecord) getById(Metric.BLOOD_PRESSURE, id);
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public GlucoseRecord getGlucoseRecord(long id) {
        return (GlucoseRecord) getById(Metric.GLUCOSE, id);
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public HeartRateRecord getHeartRateRecord(long id) {
        return (HeartRateRecord) getById(Metric.HEART_RATE, id);
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public PerfusionIndexRecord getPerfusionIndexRecord(long id) {
        return (PerfusionIndexRecord) getById(Metric.PERFUSION_INDEX, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public OperationResult insert(@NonNull HeartBloodRecord<?> record) {
        return super.insert(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public OperationResult update(@NonNull HeartBloodRecord<?> record) {
        return super.update(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequiresPermission(CcRuntimePermission.HEART_BLOOD)
    public OperationResult delete(@NonNull HeartBloodRecord<?> record) {
        return super.delete(record);
    }

    @NonNull
    @Override
    protected HeartBloodRecord<?> parseRow(@NonNull Cursor cursor) {
        final long id = cursor.getLong(cursor.getColumnIndex(RecordColumns._ID));
        final int metric = cursor.getInt(cursor.getColumnIndex(RecordColumns._METRIC));
        final long time = cursor.getLong(cursor.getColumnIndex(RecordColumns.TIME));
        final int beforeMeal = cursor.getInt(cursor.getColumnIndex(RecordColumns.MEAL_RELATION));
        final PressureValue pressureSystolic = PressureValue.mmHg(cursor.getInt(cursor.getColumnIndex(
                RecordColumns.PRESSURE_SYSTOLIC)));
        final PressureValue pressureDiastolic = PressureValue.mmHg(cursor.getInt(cursor.getColumnIndex(
                RecordColumns.PRESSURE_DIASTOLIC)));
        final double value = cursor.getDouble(cursor.getColumnIndex(RecordColumns.VALUE));

        switch (metric) {
            case Metric.BLOOD_ALCOHOL_CONCENTRATION:
                return new BloodAlcoholConcentrationRecord(id, time, value);
            case Metric.BLOOD_PRESSURE:
                return new BloodPressureRecord(id, time, pressureSystolic, pressureDiastolic);
            case Metric.GLUCOSE:
                return new GlucoseRecord(id, time, beforeMeal, BloodGlucoseValue.mmolL(value));
            case Metric.HEART_RATE:
                return new HeartRateRecord(id, time, value);
            case Metric.PERFUSION_INDEX:
                return new PerfusionIndexRecord(id, time, value);
            default:
                return new BaseHeartBloodRecord(id, metric, time, beforeMeal, pressureSystolic,
                        pressureDiastolic, value);
        }
    }
}
