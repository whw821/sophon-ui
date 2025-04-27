/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import app.protobuf.I18N
import com.google.protobuf.InvalidProtocolBufferException
import core.PB_HOME
import java.io.File
import java.io.InputStream
import java.io.OutputStream

val i18nDataStore = DataStoreFactory.create(
    serializer = I18NDataStoreSerializer(),
    produceFile = { File("${PB_HOME}/i18n.pb") }
)

class I18NDataStoreSerializer : Serializer<I18N> {

    override val defaultValue: I18N = I18N.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): I18N {
        try {
            return I18N.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: I18N, output: OutputStream) = t.writeTo(output)
}

