package com.neatorobotics.sdk.android

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2019 Neato Robotics. All rights reserved.
 *
 * The only purpose of this content provider is to self-initialize the library with a Context.
 * In that way you don't have to ask the developers to init the library into the Application onCreate() method.
 */
class NeatoSDKInitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        NeatoSDK.init(this.context)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}