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

package org.lineageos.mod.health.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log
import org.lineageos.mod.health.UriConst
import org.lineageos.mod.health.common.HealthStoreUri
import org.lineageos.mod.health.db.MedicalProfileDbHelper
import org.lineageos.mod.health.common.db.MedicalProfileColumns
import org.lineageos.mod.health.db.tables.MedicalProfileTable

internal class MedicalProfileProvider : ContentProvider() {

    private lateinit var dbHelper: MedicalProfileDbHelper

    override fun onCreate(): Boolean {
        dbHelper = MedicalProfileDbHelper.getInstance(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        if (uriMatcher.match(uri) != UriConst.MATCH_ALL) return null

        return withMyId {
            val qb = SQLiteQueryBuilder().apply { tables = MedicalProfileTable.NAME }
            val db = dbHelper.readableDatabase

            val cursor = qb.query(
                db, projection, "", emptyArray(),
                null, null, ""
            )
            cursor.setNotificationUri(context!!.contentResolver, uri)
            cursor
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = withMyId {
        val db = dbHelper.writableDatabase

        val existingId = getProfileId(db)
        if (existingId != null) {
            // Clear everything
            db.delete(MedicalProfileTable.NAME, null, null)
        }

        val rowId = db.insert(MedicalProfileTable.NAME, null, values)
        if (rowId <= 0) return@withMyId null

        context!!.contentResolver.notifyChange(HealthStoreUri.MEDICAL_PROFILE, null)
        ContentUris.withAppendedId(HealthStoreUri.MEDICAL_PROFILE, rowId)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException(
            "Update not is supported for Medical Profile, " +
                "use .insert() instead."
        )
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if (uriMatcher.match(uri) != UriConst.MATCH_ALL) {
            throw UnsupportedOperationException("Cannot delete this URI: $uri")
        }

        return withMyId {
            val db = dbHelper.writableDatabase
            val count = db.delete(MedicalProfileTable.NAME, null, emptyArray())
            if (count > 0) {
                context!!.contentResolver.notifyChange(HealthStoreUri.MEDICAL_PROFILE, null)
            }
            count
        }
    }

    override fun getType(uri: Uri) = when (uriMatcher.match(uri)) {
        UriConst.MATCH_ALL -> "vnd.android.cursor.dir"
        else -> null
    }

    private fun getProfileId(db: SQLiteDatabase): String? {
        val cursor = db.query(
            true, MedicalProfileTable.NAME,
            arrayOf(MedicalProfileColumns._ID), null, null,
            null, null, null, "1"
        )

        val id = if (cursor.moveToFirst())
            cursor.getLong(0).toString()
        else
            null
        cursor.close()
        return id
    }

    companion object {
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(HealthStoreUri.AUTHORITY, "profile", UriConst.MATCH_ALL)
        }
    }
}
