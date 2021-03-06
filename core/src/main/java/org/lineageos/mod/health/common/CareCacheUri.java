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

package org.lineageos.mod.health.common;

import android.net.Uri;

import androidx.annotation.NonNull;

public final class CareCacheUri {
    private static final String PROTOCOL = "content://";

    private CareCacheUri() {
    }

    /**
     * Base {@link Uri} for the access ContentProvider.
     * <br>
     * This is to be used only by system / oem apps to manage
     * control access.
     */
    @NonNull
    public static final Uri ACCESS = Uri.parse(PROTOCOL + Authority.ACCESS);

    /**
     * Base {@link Uri} for the Activity ContentProvider.
     *
     * <ul>
     *     <li><code>/#</code>: all elements of a metric. Can be used for insert and queries</li>
     *     <li><code>/#/#</code>: a specific element of a metric. Can be used for update and delete</li>
     * </ul>
     */
    @NonNull
    public static final Uri ACTIVITY = Uri.parse(PROTOCOL + Authority.ACTIVITY);

    /**
     * Base {@link Uri} for the Body ContentProvider.
     *
     * <ul>
     *     <li><code>/#</code>: all elements of a metric. Can be used for insert and queries</li>
     *     <li><code>/#/#</code>: a specific element of a metric. Can be used for update and delete</li>
     * </ul>
     */
    @NonNull
    public static final Uri BODY = Uri.parse(PROTOCOL + Authority.BODY);

    /**
     * Base {@link Uri} for the Breathing ContentProvider.
     *
     * <ul>
     *     <li><code>/#</code>: all elements of a metric. Can be used for insert and queries</li>
     *     <li><code>/#/#</code>: a specific element of a metric. Can be used for update and delete</li>
     * </ul>
     */
    @NonNull
    public static final Uri BREATHING = Uri.parse(PROTOCOL + Authority.BREATHING);

    /**
     * Base {@link Uri} for the Heart &amp; Blood ContentProvider.
     *
     * <ul>
     *     <li><code>/#</code>: all elements of a metric. Can be used for insert and queries</li>
     *     <li><code>/#/#</code>: a specific element of a metric. Can be used for update and delete</li>
     * </ul>
     */
    @NonNull
    public static final Uri HEART_BLOOD = Uri.parse(PROTOCOL + Authority.HEART_BLOOD);

    /**
     * Base {@link Uri} for the Mindfulness ContentProvider.
     *
     * <ul>
     *     <li><code>/#</code>: all elements of a metric. Can be used for insert and queries</li>
     *     <li><code>/#/#</code>: a specific element of a metric. Can be used for update and delete</li>
     * </ul>
     */
    @NonNull
    public static final Uri MINDFULNESS = Uri.parse(PROTOCOL + Authority.MINDFULNESS);

    /**
     * Base {@link Uri} for the MedicalProfile ContentProvider.
     */
    @NonNull
    public static final Uri MEDICAL_PROFILE = Uri.parse(PROTOCOL + Authority.MEDICAL_PROFILE);

    /**
     * ContentProviders authorities.
     */
    public static final class Authority {
        private Authority() {
        }

        @NonNull
        private static final String BASE = "org.lineageos.mod.health";

        @NonNull
        public static final String ACCESS = BASE + ".access";
        @NonNull
        public static final String ACTIVITY = BASE + ".activity";
        @NonNull
        public static final String BODY = BASE + ".body";
        @NonNull
        public static final String BREATHING = BASE + ".breathing";
        @NonNull
        public static final String HEART_BLOOD = BASE + ".heart";
        @NonNull
        public static final String MINDFULNESS = BASE + ".mindfulness";
        @NonNull
        public static final String MEDICAL_PROFILE = BASE + ".profile";
    }
}
