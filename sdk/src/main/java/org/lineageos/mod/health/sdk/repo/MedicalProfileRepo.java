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
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import org.lineageos.mod.health.common.CareCacheUri;
import org.lineageos.mod.health.common.db.MedicalProfileColumns;
import org.lineageos.mod.health.sdk.model.profile.MedicalProfile;
import org.lineageos.mod.health.sdk.util.CcRuntimePermission;

/**
 * Medical profile repository.
 * <p>
 * This class allows you to retrieve, insert, update and delete
 * medical profile information.
 * <p>
 * Operations performed in this class should be executed outside of the
 * main / UI thread.
 *
 * @see MedicalProfile
 */
@Keep
public final class MedicalProfileRepo {
    private static final String[] DEFAULT_PROJECTION = {
            MedicalProfileColumns.ALLERGIES,
            MedicalProfileColumns.BLOOD_TYPE,
            MedicalProfileColumns.HEIGHT,
            MedicalProfileColumns.MEDICATIONS,
            MedicalProfileColumns.NOTES,
            MedicalProfileColumns.ORGAN_DONOR,
            MedicalProfileColumns.BIOLOGICAL_SEX,
    };

    @Nullable
    private volatile static MedicalProfileRepo instance;
    @NonNull
    private static final Object instanceLock = new Object();

    @NonNull
    private final ContentResolver contentResolver;

    private MedicalProfileRepo(@NonNull ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @NonNull
    public static MedicalProfileRepo getInstance(
            @NonNull ContentResolver contentResolver) {
        MedicalProfileRepo currentInstance = instance;
        // use double-checked locking
        // (https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java)
        if (currentInstance == null) {
            synchronized (instanceLock) {
                currentInstance = instance;
                if (currentInstance == null) {
                    currentInstance = new MedicalProfileRepo(contentResolver);
                    instance = currentInstance;
                }
            }
        }

        return currentInstance;
    }

    @NonNull
    @RequiresPermission(CcRuntimePermission.MEDICAL_PROFILE)
    public MedicalProfile get() {
        final Cursor cursor = contentResolver.query(CareCacheUri.MEDICAL_PROFILE,
                DEFAULT_PROJECTION, null, null, null);
        if (cursor == null) {
            return new MedicalProfile();
        }

        try {
            if (cursor.moveToFirst()) {
                return new MedicalProfile(
                        cursor.getString(cursor.getColumnIndex(MedicalProfileColumns.ALLERGIES)),
                        cursor.getInt(cursor.getColumnIndex(MedicalProfileColumns.BLOOD_TYPE)),
                        cursor.getFloat(cursor.getColumnIndex(MedicalProfileColumns.HEIGHT)),
                        cursor.getString(cursor.getColumnIndex(MedicalProfileColumns.MEDICATIONS)),
                        cursor.getString(cursor.getColumnIndex(MedicalProfileColumns.NOTES)),
                        cursor.getInt(cursor.getColumnIndex(MedicalProfileColumns.ORGAN_DONOR)),
                        cursor.getInt(cursor.getColumnIndex(MedicalProfileColumns.BIOLOGICAL_SEX))
                );
            } else {
                return new MedicalProfile();
            }
        } finally {
            cursor.close();
        }
    }

    @RequiresPermission(CcRuntimePermission.MEDICAL_PROFILE)
    public boolean set(@NonNull MedicalProfile medicalProfile) {
        final ContentValues cv = medicalProfile.toContentValues();
        final Uri uri = contentResolver.insert(CareCacheUri.MEDICAL_PROFILE, cv);
        return uri != null;
    }

    @RequiresPermission(CcRuntimePermission.MEDICAL_PROFILE)
    public boolean reset() {
        return contentResolver.delete(CareCacheUri.MEDICAL_PROFILE, null, null) == 1;
    }
}
