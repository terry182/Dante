package at.shockbytes.dante.book.realm

import at.shockbytes.dante.util.Gsonify
import com.google.gson.JsonObject
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Author:  Martin Macheiner
 * Date:    27.08.2016
 */
open class RealmBook @JvmOverloads constructor(
    @PrimaryKey var id: Long = -1,
    var title: String = "",
    var subTitle: String = "",
    var author: String = "",
    var pageCount: Int = 0,
    var publishedDate: String = "",
    var position: Int = 0,
    var isbn: String = "",
    var thumbnailAddress: String? = null,
    var googleBooksLink: String? = null, // Version 1
    var startDate: Long = 0,
    var endDate: Long = 0,
    var wishlistDate: Long = 0,
    var language: String = "NA", // Version 2
    var rating: Int = 0, // 1 - 5
    var currentPage: Int = 0, // Version 3
    var notes: String? = null,
    var summary: String? = null, // Version 4
    var labels: RealmList<String> = RealmList()
) : RealmObject(), Gsonify {

    enum class State {
        READ_LATER, READING, READ
    }

    private var ordinalState: Int = 0

    var state: State
        get() = State.values()[ordinalState]
        set(state) {
            this.ordinalState = state.ordinal
        }

    val reading: Boolean
        get() = state == State.READING

    init {
        ordinalState = State.READ_LATER.ordinal
    }

    override fun toJson(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("position", position)
        jsonObject.addProperty("title", title)
        jsonObject.addProperty("subTitle", subTitle)
        jsonObject.addProperty("author", author)
        jsonObject.addProperty("pageCount", pageCount)
        jsonObject.addProperty("publishedDate", publishedDate)
        jsonObject.addProperty("isbn", isbn)
        jsonObject.addProperty("language", language)
        jsonObject.addProperty("currentPage", currentPage)
        jsonObject.addProperty("notes", notes)
        jsonObject.addProperty("thumbnailAddress", thumbnailAddress)
        jsonObject.addProperty("googleBooksLink", googleBooksLink)
        jsonObject.addProperty("ordinalState", state.ordinal)
        jsonObject.addProperty("rating", rating)
        jsonObject.addProperty("startDate", startDate)
        jsonObject.addProperty("endDate", endDate)
        jsonObject.addProperty("wishlistDate", wishlistDate)
        return jsonObject
    }

    override fun toString() = toJson().toString()
}
