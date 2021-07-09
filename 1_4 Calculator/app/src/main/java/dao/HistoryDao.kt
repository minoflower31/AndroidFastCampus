package dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insert(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()
}