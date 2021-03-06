package moe.nikky.curseproxy.dao

import moe.nikky.curseproxy.LOG
import moe.nikky.curseproxy.model.Section
import moe.nikky.curseproxy.model.graphql.Addon
import org.jetbrains.squash.connection.DatabaseConnection
import org.jetbrains.squash.connection.transaction
import org.jetbrains.squash.dialects.h2.H2Connection
import org.jetbrains.squash.expressions.eq
import org.jetbrains.squash.expressions.like
import org.jetbrains.squash.expressions.or
import org.jetbrains.squash.query.from
import org.jetbrains.squash.query.select
import org.jetbrains.squash.query.where
import org.jetbrains.squash.results.ResultRow
import org.jetbrains.squash.results.get
import org.jetbrains.squash.schema.create
import org.jetbrains.squash.statements.deleteFrom
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.values

fun ResultRow.toSparseAddon() = Addon(
    id = this[Addons.id],
    gameID = this[Addons.gameId],
    name = this[Addons.name],
    slug = this[Addons.slug],
    primaryAuthorName = this[Addons.primaryAuthorName],
    primaryCategoryName = this[Addons.primaryCategoryName],
    section = Section.fromId(this[Addons.sectionId]),
    dateModified = this[Addons.dateModified],
    dateCreated = this[Addons.dateCreated],
    dateReleased = this[Addons.dateReleased],
    categoryList = this[Addons.categoryList],
    gameVersions = this[Addons.gameVersions].split("|").filter { it.isNotBlank() }.toSet()
)

//TODO: add more database row conversions

class AddonDatabase(val db: DatabaseConnection = H2Connection.createMemoryConnection()) : AddonStorage {
    init {
        db.transaction {
            databaseSchema().create(Addons)
        }
    }

    override fun getAddon(id: Int) = db.transaction {
        val row = from(Addons).where { Addons.id eq id }.execute().singleOrNull()
        row?.toSparseAddon()
    }

    override fun getAll(
        gameId: Int?,
        name: String?,
        slug: String?,
        category: String?,
        section: Section?,
        gameVersions: List<String>?
    ) = db.transaction {
        from(Addons)
            .select()
            .apply {
                gameId?.let {
                    LOG.debug("added gameID filter '$it'")
                    where { Addons.gameId eq it }
                }
                name?.let {
                    LOG.debug("added name filter '$it'")
                    where { Addons.name like it }
                }
                slug?.let {
                    LOG.debug("added slug filter '$it'")
                    where { Addons.slug like it }
                }
                category?.let {
                    LOG.debug("added category filter '$it'")
                    where { (Addons.primaryCategoryName like it) or (Addons.categoryList like "%$category%") }
                }
                section?.let {
                    LOG.debug("added section filter '$it'")
                    where { Addons.sectionId eq it.id }
                }
            }
//                .orderBy(Addons.date, ascending = false)
            .execute()
            .map { it.toSparseAddon() }
            .filter { addon ->
                if(gameVersions == null)
                    return@filter true

                addon.gameVersions.any { version ->
                    gameVersions.contains(version)
                }
            }
            .toList()

    }

    override fun createAddon(addon: Addon) {

        db.transaction {
            deleteFrom(Addons)
                .where(Addons.id eq addon.id)
                .execute()

            insertInto(Addons).values {
                it[id] = addon.id
                it[gameId] = addon.gameID
                it[name] = addon.name
                it[slug] = addon.slug
                it[primaryAuthorName] = addon.primaryAuthorName
                it[primaryCategoryName] = addon.primaryCategoryName
                it[sectionId] = addon.section?.id ?: -1
                it[dateModified] = addon.dateModified
                it[dateCreated] = addon.dateCreated
                it[dateReleased] = addon.dateReleased
                it[categoryList] = addon.categoryList
                it[gameVersions] = addon.gameVersions.joinToString("|")
            }.execute()
        }
    }

    override fun close() {}
}