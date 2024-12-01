package me.parade.lib_common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManager private constructor(private val context: Context) {

    // 使用 Context 扩展属性创建 DataStore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

    // 泛型保存方法
    suspend fun <T> save(key: String, value: T) {
        val preferenceKey = when (value) {
            is Int -> intPreferencesKey(key)
            is Long -> longPreferencesKey(key)
            is Double -> doublePreferencesKey(key)
            is Float -> floatPreferencesKey(key)
            is Boolean -> booleanPreferencesKey(key)
            is String -> stringPreferencesKey(key)
            else -> throw IllegalArgumentException("不支持的数据类型")
        }

        context.dataStore.edit { preferences ->
            @Suppress("UNCHECKED_CAST")
            preferences[preferenceKey as Preferences.Key<T>] = value
        }
    }

    // 泛型读取方法
    fun <T> get(key: String, defaultValue: T): Flow<T> {
        return context.dataStore.data.map { preferences ->
            val preferenceKey = when (defaultValue) {
                is Int -> intPreferencesKey(key)
                is Long -> longPreferencesKey(key)
                is Double -> doublePreferencesKey(key)
                is Float -> floatPreferencesKey(key)
                is Boolean -> booleanPreferencesKey(key)
                is String -> stringPreferencesKey(key)
                else -> throw IllegalArgumentException("不支持的数据类型")
            }

            @Suppress("UNCHECKED_CAST")
            (preferences[preferenceKey as Preferences.Key<T>] ?: defaultValue)
        }
    }

    // 移除特定键的数据
    suspend fun remove(key: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }

    // 清除所有存储的数据
    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}