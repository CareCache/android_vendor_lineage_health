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

import org.lineageos.mod.health.common.HealthStoreUri;
import org.lineageos.mod.health.common.Metric;
import org.lineageos.mod.health.common.db.RecordColumns;
import org.lineageos.mod.health.sdk.model.records.mindfulness.MeditationRecord;
import org.lineageos.mod.health.sdk.model.records.mindfulness.MindfulnessRecord;
import org.lineageos.mod.health.sdk.model.records.mindfulness.MoodRecord;
import org.lineageos.mod.health.sdk.model.records.mindfulness.SleepRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mindfulness records repository.
 * <p>
 * This class allows you to retrieve, insert, update and delete
 * mindfulness records.
 * <p>
 * Supported records types:
 * <ul>
 *     <li>{@link MeditationRecord}</li>
 *     <li>{@link MoodRecord}</li>
 *     <li>{@link SleepRecord}</li>
 * </ul>
 * <p>
 * Operations performed in this class should be executed outside of the
 * main / UI thread.
 */
@Keep
@SuppressWarnings("unused")
public final class MindfulnessRecordsRepo extends RecordsRepo<MindfulnessRecord> {

    @Nullable
    private static volatile MindfulnessRecordsRepo instance;

    private MindfulnessRecordsRepo(@NonNull ContentResolver contentResolver) {
        super(contentResolver, HealthStoreUri.MINDFULNESS);
    }

    @NonNull
    public static synchronized MindfulnessRecordsRepo getInstance(
            @NonNull ContentResolver contentResolver) {
        final MindfulnessRecordsRepo currentInstance = instance;
        if (currentInstance != null) {
            return currentInstance;
        }

        final MindfulnessRecordsRepo newInstance = new MindfulnessRecordsRepo(contentResolver);
        instance = newInstance;
        return newInstance;
    }

    @NonNull
    public List<MeditationRecord> getAllMeditationRecords() {
        return getByMetric(Metric.MEDITATION).parallelStream()
                .map(MeditationRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    public List<MoodRecord> getAllMoodRecords() {
        return getByMetric(Metric.MOOD).parallelStream()
                .map(MoodRecord.class::cast)
                .collect(Collectors.toList());
    }

    @NonNull
    public List<SleepRecord> getAllSleepRecords() {
        return getByMetric(Metric.SLEEP).parallelStream()
                .map(SleepRecord.class::cast)
                .collect(Collectors.toList());
    }


    @Nullable
    public MeditationRecord getMeditationRecord(long id) {
        return (MeditationRecord) getById(Metric.MEDITATION, id);
    }

    @Nullable
    public MoodRecord getMoodRecord(long id) {
        return (MoodRecord) getById(Metric.MOOD, id);
    }

    @Nullable
    public SleepRecord getSleepRecord(long id) {
        return (SleepRecord) getById(Metric.SLEEP, id);
    }

    @NonNull
    @Override
    protected MindfulnessRecord parseRow(@NonNull Cursor cursor) {
        return new MindfulnessRecord(
                cursor.getLong(cursor.getColumnIndex(RecordColumns._ID)),
                cursor.getInt(cursor.getColumnIndex(RecordColumns._METRIC)),
                cursor.getLong(cursor.getColumnIndex(RecordColumns.TIME)),
                cursor.getLong(cursor.getColumnIndex(RecordColumns.DURATION)),
                cursor.getInt(cursor.getColumnIndex(RecordColumns.MOOD)),
                cursor.getString(cursor.getColumnIndex(RecordColumns.NOTES))
        );
    }
}
