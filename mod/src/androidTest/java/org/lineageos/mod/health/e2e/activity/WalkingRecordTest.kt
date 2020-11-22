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
import org.lineageos.mod.health.sdk.model.records.activity.WalkingRecord
import org.lineageos.mod.health.sdk.repo.ActivityRecordsRepo

@RunWith(AndroidJUnit4::class)
class WalkingRecordTest {
    private lateinit var repo: ActivityRecordsRepo

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repo = ActivityRecordsRepo.getInstance(context.contentResolver)

        // Cleanup
        repo.allWalkingRecords.forEach { repo.delete(it) }
    }

    @After
    fun tearDown() {
        repo.allWalkingRecords.forEach { repo.delete(it) }
        Assert.assertEquals(0, repo.allWalkingRecords.size)
    }

    @Test
    fun testInsert() {
        val a = WalkingRecord(
            0L,
            System.currentTimeMillis(),
            1000L,
            6.5,
            500,
        )
        val idA = repo.insert(a)
        Assert.assertNotEquals(-1L, idA)
        Assert.assertEquals(a, repo.getWalkingRecord(idA))
    }

    @Test
    fun testMultipleInsert() {
        val a = WalkingRecord(
            0L,
            System.currentTimeMillis(),
            1000L,
            6.5,
            500,
        )
        val b = WalkingRecord(
            0L,
            System.currentTimeMillis() - 1000L,
            45L,
            1.47,
            891,
        )
        val idA = repo.insert(a)
        val idB = repo.insert(b)

        Assert.assertNotEquals(-1L, idA)
        Assert.assertNotEquals(-1L, idB)
        Assert.assertNotEquals(idA, idB)

        Assert.assertEquals(a, repo.getWalkingRecord(idA))
        Assert.assertEquals(b, repo.getWalkingRecord(idB))
    }

    @Test
    fun testUpdate() {
        val a = WalkingRecord(
            0L,
            System.currentTimeMillis(),
            1000L,
            6.5,
            500,
        )
        val idA = repo.insert(a)
        Assert.assertNotEquals(-1L, idA)
        val fromDb = repo.getWalkingRecord(idA)
        if (fromDb == null) {
            Assert.fail("fromDb == null")
            return
        }

        Assert.assertEquals(a, fromDb)
        fromDb.apply {
            time = System.currentTimeMillis() - 90L
            steps -= 89
        }
        Assert.assertNotEquals(a, fromDb)
        Assert.assertTrue(repo.update(fromDb))
        Assert.assertEquals(fromDb, repo.getWalkingRecord(idA))
    }

    @Test
    fun testDelete() {
        val a = WalkingRecord(
            0L,
            System.currentTimeMillis(),
            1000L,
            6.5,
            500,
        )
        val initialSize = repo.allWalkingRecords.size
        val idA = repo.insert(a)
        val finalSize = repo.allWalkingRecords.size

        Assert.assertNotEquals(-1L, idA)
        Assert.assertTrue(finalSize > initialSize)

        val fromDb = repo.getWalkingRecord(idA)
        if (fromDb == null) {
            Assert.fail("fromDb == null")
        } else {
            repo.delete(fromDb)
        }

        Assert.assertEquals(finalSize - 1, repo.allWalkingRecords.size)
        Assert.assertNull(repo.getWalkingRecord(idA))
    }

    @Test
    fun testValidator() {
        val a = WalkingRecord(
            0L,
            -1L,
            -4L,
            -88.2,
            -44,
        )
        val idA = repo.insert(a)
        Assert.assertNotEquals(-1L, idA)
        val fromDb = repo.getWalkingRecord(idA)
        if (fromDb == null) {
            Assert.fail("fromDb == null")
            return
        }

        Assert.assertNotEquals(a.time, fromDb.time)
        Assert.assertEquals(0L, fromDb.duration)
        Assert.assertEquals(0.0, fromDb.distance, 0.0)
        Assert.assertEquals(0, fromDb.steps)
    }
}
