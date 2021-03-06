package moe.nikky.curseproxy.dao

import org.jetbrains.squash.definition.*

object Addons : TableDefinition() {
    val id = integer("id").primaryKey()
    val gameId = integer("gameID")
    val name = varchar("name", 300)
    val slug = varchar("slug", 300)
    val primaryAuthorName = varchar("primaryAuthorName", 60)
    val primaryCategoryName = varchar("primaryCategoryName", 60)
    val sectionId = integer("sectionId")
    val dateModified = date("dateModified")
    val dateCreated = date("dateCreated")
    val dateReleased = date("dateReleased")
    val categoryList = varchar("categoryList", 200)
    val gameVersions = varchar("gameVersions", 300)
}
