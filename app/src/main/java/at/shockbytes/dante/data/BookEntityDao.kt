package at.shockbytes.dante.data

import at.shockbytes.dante.backup.BackupManager
import at.shockbytes.dante.book.BookEntity
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Martin Macheiner
 * Date: 12-Jun-18.
 */
interface BookEntityDao {

    val bookObservable: Observable<List<BookEntity>>

    val booksCurrentlyReading: List<BookEntity>

    fun get(id: Long): BookEntity?

    fun create(entity: BookEntity)

    fun update(entity: BookEntity)

    fun delete(id: Long)

    fun search(query: String): Observable<List<BookEntity>>

    fun restoreBackup(
        backupBooks: List<BookEntity>,
        strategy: BackupManager.RestoreStrategy
    ): Completable
}