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
import org.lineageos.mod.health.sdk.model.records.breathing.BreathingRecord;
import org.lineageos.mod.health.sdk.model.records.breathing.InhalerUsageRecord;
import org.lineageos.mod.health.sdk.model.records.breathing.OxygenSaturationRecord;
import org.lineageos.mod.health.sdk.model.records.breathing.PeakExpiratoryFlowRecord;
import org.lineageos.mod.health.sdk.model.records.breathing.RespiratoryRateRecord;
import org.lineageos.mod.health.sdk.model.records.breathing.VitalCapacityRecord;
import org.lineageos.mod.health.sdk.util.CcRuntimePermission;
import org.lineageos.mod.health.sdk.util.RecordTimeComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Breathing records repository.
 * <p>
 * This class allows you to retrieve, insert, update and delete
 * breathing records.
 * <p>
 * Supported records types:
 * <ul>
 *     <li>{@link InhalerUsageRecord}</li>
 *     <li>{@link OxygenSaturationRecord}</li>
 *     <li>{@link PeakExpiratoryFlowRecord}</li>
 *     <li>{@link RespiratoryRateRecord}</li>
 *     <li>{@link VitalCapacityRecord}</li>
 * </ul>
 * <p>
 * Operations performed in this class should be executed outside of the
 * main / UI thread.
 *
 * @see BreathingRecord
 * @see InhalerUsageRecord
 * @see PeakExpiratoryFlowRecord
 * @see RespiratoryRateRecord
 * @see VitalCapacityRecord
 */
@Keep
public final class BreathingRecordsRepo extends RecordsRepo<BreathingRecord> {

    @Nullable
    private static volatile BreathingRecordsRepo instance;
    @NonNull
    private static final Object instanceLock = new Object();

    private BreathingRecordsRepo(@NonNull ContentResolver contentResolver) {
        super(contentResolver, CareCacheUri.BREATHING);
    }

    @NonNull
    public static BreathingRecordsRepo getInstance(
            @NonNull ContentResolver contentResolver) {
        BreathingRecordsRepo currentInstance = instance;
        // use double-checked locking
        // (https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java)
        if (currentInstance == null) {
            synchronized (instanceLock) {
                currentInstance = instance;
                if (currentInstance == null) {
                    currentInstance = new BreathingRecordsRepo(contentResolver);
                    instance = currentInstance;
                }
            }
        }

        return currentInstance;
    }

    @NonNull
    @Override
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public List<BreathingRecord> getAll() {
        final List<BreathingRecord> list = new ArrayList<>();
        list.addAll(getAllInhalerUsageRecords());
        list.addAll(getAllOxygenSaturationRecords());
        list.addAll(getAllPeakExpiratoryFlowRecords());
        list.addAll(getAllRespiratoryRateRecords());
        list.addAll(getAllVitalCapacityRecords());
        list.sort(new RecordTimeComparator());
        return list;
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public List<InhalerUsageRecord> getAllInhalerUsageRecords() {
        return getByMetric(Metric.INHALER_USAGE).parallelStream()
                .map(InhalerUsageRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public List<OxygenSaturationRecord> getAllOxygenSaturationRecords() {
        return getByMetric(Metric.OXYGEN_SATURATION).parallelStream()
                .map(OxygenSaturationRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public List<PeakExpiratoryFlowRecord> getAllPeakExpiratoryFlowRecords() {
        return getByMetric(Metric.PEAK_EXPIRATORY_FLOW).parallelStream()
                .map(PeakExpiratoryFlowRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public List<RespiratoryRateRecord> getAllRespiratoryRateRecords() {
        return getByMetric(Metric.RESPIRATORY_RATE).parallelStream()
                .map(RespiratoryRateRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public List<VitalCapacityRecord> getAllVitalCapacityRecords() {
        return getByMetric(Metric.VITAL_CAPACITY).parallelStream()
                .map(VitalCapacityRecord.class::cast)
                .collect(Collectors.toList());
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public InhalerUsageRecord getInhalerUsageRecord(long id) {
        return (InhalerUsageRecord) getById(Metric.INHALER_USAGE, id);
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public OxygenSaturationRecord getOxygenSaturationRecord(long id) {
        return (OxygenSaturationRecord) getById(Metric.OXYGEN_SATURATION, id);
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public PeakExpiratoryFlowRecord getPeakExpiratoryFlowRecord(long id) {
        return (PeakExpiratoryFlowRecord) getById(Metric.PEAK_EXPIRATORY_FLOW, id);
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public RespiratoryRateRecord getRespiratoryRateRecord(long id) {
        return (RespiratoryRateRecord) getById(Metric.RESPIRATORY_RATE, id);
    }

    @Nullable
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public VitalCapacityRecord getVitalCapacityRecord(long id) {
        return (VitalCapacityRecord) getById(Metric.VITAL_CAPACITY, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public OperationResult insert(@NonNull BreathingRecord record) {
        return super.insert(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public OperationResult update(@NonNull BreathingRecord record) {
        return super.update(record);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequiresPermission(CcRuntimePermission.BREATHING)
    public OperationResult delete(@NonNull BreathingRecord record) {
        return super.delete(record);
    }

    @NonNull
    @Override
    protected BreathingRecord parseRow(@NonNull Cursor cursor) {
        final long id = cursor.getLong(cursor.getColumnIndex(RecordColumns._ID));
        final int metric = cursor.getInt(cursor.getColumnIndex(RecordColumns._METRIC));
        final long time = cursor.getLong(cursor.getColumnIndex(RecordColumns.TIME));
        final String notes = cursor.getString(cursor.getColumnIndex(RecordColumns.NOTES));
        final double value = cursor.getDouble(cursor.getColumnIndex(RecordColumns.VALUE));

        switch (metric) {
            case Metric.INHALER_USAGE:
                return new InhalerUsageRecord(id, time, notes);
            case Metric.OXYGEN_SATURATION:
                return new OxygenSaturationRecord(id, time, value);
            case Metric.PEAK_EXPIRATORY_FLOW:
                return new PeakExpiratoryFlowRecord(id, time, value);
            case Metric.RESPIRATORY_RATE:
                return new RespiratoryRateRecord(id, time, value);
            case Metric.VITAL_CAPACITY:
                return new VitalCapacityRecord(id, time, value);
            default:
                return new BreathingRecord(id, metric, time, notes, value);
        }
    }
}
