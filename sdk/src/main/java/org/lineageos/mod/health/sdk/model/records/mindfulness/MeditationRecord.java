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

package org.lineageos.mod.health.sdk.model.records.mindfulness;

import androidx.annotation.Keep;

import org.lineageos.mod.health.common.Metric;
import org.lineageos.mod.health.common.values.MoodLevel;

/**
 * Meditation record.
 *
 * <ul>
 *     <li>{@link Long} id: db identifier (default to <code>0L</code>)</li>
 *     <li>{@link Long} time: timestamp ({@link System#currentTimeMillis()})</li>
 *     <li>{@link Long} duration: duration in milliseconds (ms)</li>
 * </ul>
 *
 * <a href="https://en.wikipedia.org/wiki/Meditation">More info</a>
 *
 * @see Metric#MEDITATION
 */
@Keep
public final class MeditationRecord extends MindfulnessRecord {

    public MeditationRecord(long id, long time, long duration) {
        super(id, Metric.MEDITATION, time, duration, MoodLevel.UNKNOWN, "");
    }

    @Override
    public long getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(long duration) {
        super.setDuration(duration);
    }
}
