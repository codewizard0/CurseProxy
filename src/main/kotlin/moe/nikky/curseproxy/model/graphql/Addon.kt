package moe.nikky.curseproxy.model.graphql

import moe.nikky.curseproxy.model.CurseAddon
import moe.nikky.curseproxy.model.Section
import java.time.LocalDate
import java.time.ZoneId

data class Addon(
    val id: Int,
    val gameID: Int,
    val name: String,
    val slug: String,
    val primaryAuthorName: String?,
    val primaryCategoryName: String?,
    val section: Section?,
    val dateModified: LocalDate,
    val dateCreated: LocalDate,
    val dateReleased: LocalDate,
    val categoryList: String,
    val gameVersions: Set<String>
) {
    companion object {
        fun fromCurseAddon(curseAddon: CurseAddon): Addon {
            return Addon(
                id = curseAddon.id,
                gameID = curseAddon.gameId,
                name = curseAddon.name,
                slug = curseAddon.slug,
                primaryAuthorName = curseAddon.primaryAuthorName,
                primaryCategoryName = curseAddon.primaryCategoryName,
//                sectionName = Section.fromId(curseAddon.categorySection.id)?.sectionName ?: "unknown_${curseAddon.sectionName}_${curseAddon.categorySection.id}",
                section = Section.fromId(curseAddon.categorySection.id),
                dateModified = curseAddon.dateModified.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                dateCreated = curseAddon.dateCreated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                dateReleased = curseAddon.dateReleased.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                categoryList = curseAddon.categoryList,
                gameVersions = curseAddon.gameVersionLatestFiles.map { it.gameVersion }.toSet()
            )
        }
    }
}