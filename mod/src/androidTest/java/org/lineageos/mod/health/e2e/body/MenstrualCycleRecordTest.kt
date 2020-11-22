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

package org.lineageos.mod.health.e2e.body

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.lineageos.mod.health.common.values.MenstrualCycleOtherSymptoms
import org.lineageos.mod.health.common.values.MenstrualCyclePhysicalSymptoms
import org.lineageos.mod.health.common.values.SexualActivity
import org.lineageos.mod.health.sdk.model.records.body.MenstrualCycleRecord
import org.lineageos.mod.health.sdk.repo.BodyRecordsRepo

@RunWith(AndroidJUnit4::class)
class MenstrualCycleRecordTest {
    private lateinit var repo: BodyRecordsRepo

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repo = BodyRecordsRepo.getInstance(context.contentResolver)

        // Cleanup
        repo.allMenstrualCycleRecords.forEach { repo.delete(it) }
    }

    @After
    fun tearDown() {
        repo.allMenstrualCycleRecords.forEach { repo.delete(it) }
        Assert.assertEquals(0, repo.allMenstrualCycleRecords.size)
    }

    @Test
    fun testInsert() {
        val a = MenstrualCycleRecord(
            0L,
            System.currentTimeMillis(),
            MenstrualCycleOtherSymptoms.MOOD_SWINGS or
                MenstrualCycleOtherSymptoms.POOR_CONCENTRATION,
            MenstrualCyclePhysicalSymptoms.FATIGUE,
            SexualActivity.NONE,
            2.0
        )
        val idA = repo.insert(a)
        Assert.assertNotEquals(-1L, idA)
        Assert.assertEquals(a, repo.getMenstrualCycleRecord(idA))
    }

    @Test
    fun testMultipleInsert() {
        val a = MenstrualCycleRecord(
            0L,
            System.currentTimeMillis(),
            MenstrualCycleOtherSymptoms.MOOD_SWINGS or
                MenstrualCycleOtherSymptoms.POOR_CONCENTRATION,
            MenstrualCyclePhysicalSymptoms.FATIGUE,
            SexualActivity.NONE,
            2.0
        )
        val b = MenstrualCycleRecord(
            0L,
            System.currentTimeMillis() - 1000L,
            MenstrualCycleOtherSymptoms.HIGH_SEX_DRIVE,
            MenstrualCyclePhysicalSymptoms.CRAMPS,
            SexualActivity.SEX,
            3.0
        )
        val idA = repo.insert(a)
        val idB = repo.insert(b)

        Assert.assertNotEquals(-1L, idA)
        Assert.assertNotEquals(-1L, idB)
        Assert.assertNotEquals(idA, idB)

        Assert.assertEquals(a, repo.getMenstrualCycleRecord(idA))
        Assert.assertEquals(b, repo.getMenstrualCycleRecord(idB))
    }

    @Test
    fun testUpdate() {
        val a = MenstrualCycleRecord(
            0L,
            System.currentTimeMillis(),
            MenstrualCycleOtherSymptoms.MOOD_SWINGS or
                MenstrualCycleOtherSymptoms.POOR_CONCENTRATION,
            MenstrualCyclePhysicalSymptoms.FATIGUE,
            SexualActivity.NONE,
            2.0
        )
        val idA = repo.insert(a)
        Assert.assertNotEquals(-1L, idA)
        val fromDb = repo.getMenstrualCycleRecord(idA)
        if (fromDb == null) {
            Assert.fail("fromDb == null")
            return
        }

        Assert.assertEquals(a, fromDb)
        fromDb.apply {
            otherSymptoms = otherSymptoms or MenstrualCycleOtherSymptoms.ANXIETY
            value += 0.03
        }
        Assert.assertNotEquals(a, fromDb)
        Assert.assertTrue(repo.update(fromDb))
        Assert.assertEquals(fromDb, repo.getMenstrualCycleRecord(idA))
    }

    @Test
    fun testDelete() {
        val a = MenstrualCycleRecord(
            0L,
            System.currentTimeMillis(),
            MenstrualCycleOtherSymptoms.MOOD_SWINGS or
                MenstrualCycleOtherSymptoms.POOR_CONCENTRATION,
            MenstrualCyclePhysicalSymptoms.FATIGUE,
            SexualActivity.NONE,
            2.0
        )
        val initialSize = repo.allMenstrualCycleRecords.size
        val idA = repo.insert(a)
        val finalSize = repo.allMenstrualCycleRecords.size

        Assert.assertNotEquals(-1L, idA)
        Assert.assertTrue(finalSize > initialSize)

        val fromDb = repo.getMenstrualCycleRecord(idA)
        if (fromDb == null) {
            Assert.fail("fromDb == null")
        } else {
            repo.delete(fromDb)
        }

        Assert.assertEquals(finalSize - 1, repo.allMenstrualCycleRecords.size)
        Assert.assertNull(repo.getMenstrualCycleRecord(idA))
    }

    @Test
    fun testValidator() {
        val a = MenstrualCycleRecord(
            0L,
            -1L,
            1 shl 11 or 1 shl 3,
            1 shl 10 or 1 shl 5,
            1 shl 6,
            -2.0
        )
        val idA = repo.insert(a)
        Assert.assertNotEquals(-1L, idA)
        val fromDb = repo.getMenstrualCycleRecord(idA)
        if (fromDb == null) {
            Assert.fail("fromDb == null")
            return
        }

        Assert.assertNotEquals(a.time, fromDb.time)
        Assert.assertEquals(MenstrualCycleOtherSymptoms.NONE, fromDb.otherSymptoms)
        Assert.assertEquals(MenstrualCyclePhysicalSymptoms.NONE, fromDb.physicalSymptoms)
        Assert.assertEquals(SexualActivity.NONE, fromDb.sexualActivity)
        Assert.assertEquals(0.0, fromDb.value, 0.0)
    }

}
