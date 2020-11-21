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

package org.lineageos.mod.health.e2e.activity

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.lineageos.mod.health.sdk.model.records.activity.CyclingRecord
import org.lineageos.mod.health.sdk.repo.ActivityRecordsRepo

@RunWith(AndroidJUnit4::class)
class CyclingRecordTest {
    private lateinit var repo: ActivityRecordsRepo

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repo = ActivityRecordsRepo.getInstance(context.contentResolver)

        // Cleanup
        repo.allCyclingRecords.forEach { repo.delete(it) }
    }

    @After
    fun tearDown() {
        repo.allCyclingRecords.forEach { repo.delete(it) }
        Assert.assertEquals(0, repo.allCyclingRecords.size)
    }

    @Test
    fun testInsert() {
        val a = CyclingRecord(
            0L,
            System.currentTimeMillis(),
            1000L,
            12.0,
            50.0,
            5.0
        )
        val idA = repo.insert(a)
        Assert.assertNotEquals(-1L, idA)
        Assert.assertEquals(a, repo.getCyclingRecord(idA))
    }

    @Test
    fun testMultipleInsert() {
        val a = CyclingRecord(
            0L,
            System.currentTimeMillis(),
            1000L,
            12.0,
            50.0,
            5.0
        )
        val b = CyclingRecord(
            0L,
            System.currentTimeMillis() - 1000L,
            60L,
            1.0,
            9.1,
            88.4
        )
        val idA = repo.insert(a)
        val idB = repo.insert(b)

        Assert.assertNotEquals(-1L, idA)
        Assert.assertNotEquals(-1L, idB)
        Assert.assertNotEquals(idA, idB)

        Assert.assertEquals(a, repo.getCyclingRecord(idA))
        Assert.assertEquals(b, repo.getCyclingRecord(idB))
    }

    @Test
    fun testUpdate() {
        val a = CyclingRecord(
            0L,
            System.currentTimeMillis(),
            1000L,
            12.0,
            50.0,
            5.0
        )
        val idA = repo.insert(a)
        Assert.assertNotEquals(-1L, idA)
        val fromDb = repo.getCyclingRecord(idA)
        if (fromDb == null) {
            Assert.fail("fromDb == null")
            return
        }

        Assert.assertEquals(a, fromDb)
        fromDb.apply {
            duration += 5
            avgSpeed *= 0.4
            elevationGain = 2.0
        }
        Assert.assertNotEquals(a, fromDb)
        Assert.assertTrue(repo.update(fromDb))
        Assert.assertEquals(fromDb, repo.getCyclingRecord(idA))
    }

    @Test
    fun testDelete() {
        val a = CyclingRecord(
            0L,
            System.currentTimeMillis(),
            1000L,
            12.0,
            50.0,
            5.0
        )
        val initialSize = repo.allCyclingRecords.size
        val idA = repo.insert(a)
        val finalSize = repo.allCyclingRecords.size

        Assert.assertNotEquals(-1L, idA)
        Assert.assertTrue(finalSize > initialSize)

        val fromDb = repo.getCyclingRecord(idA)
        if (fromDb == null) {
            Assert.fail("fromDb == null")
        } else {
            repo.delete(fromDb)
        }

        Assert.assertEquals(finalSize - 1, repo.allCyclingRecords.size)
        Assert.assertNull(repo.getCyclingRecord(idA))
    }
}