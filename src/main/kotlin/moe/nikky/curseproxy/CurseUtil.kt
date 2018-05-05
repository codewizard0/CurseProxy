package moe.nikky.curseproxy

import aballano.kotlinmemoization.memoize
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import moe.nikky.curseproxy.CurseUtil.getAllFilesForAddOn
import voodoo.curse.AddOn
import voodoo.curse.AddOnFile

/**
 * Created by nikky on 01/05/18.
 * @author Nikky
 * @version 1.0
 */
object CurseUtil {
    val META_URL = "https://cursemeta.dries007.net"
    val useragent = "curseProxy (https://github.com/nikky/CurseProxy)"

    val mapper = jacksonObjectMapper() // Enable Json parsing
            .registerModule(KotlinModule()) // Enable Kotlin support
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)

    fun getAddonCall(addonId: Int): AddOn? {
        val url = "$META_URL/api/v2/direct/GetAddOn/$addonId"

        LOG.debug("get $url")
        val (_, _, result) = url.httpGet()
                .header("User-Agent" to useragent)
                .responseString()
        return when (result) {
            is Result.Success -> {
                mapper.readValue(result.value)
            }
            is Result.Failure -> {
                LOG.error(result.error.toString())
                null
            }
        }
    }
    val getAddon = ::getAddonCall.memoize()

    private fun getAllFilesForAddOnCall(addonId: Int): List<AddOnFile> {
        val url = "$META_URL/api/v2/direct/GetAllFilesForAddOn/$addonId"

        LOG.debug("get $url")
        val (_, _, result) = url.httpGet()
                .header("User-Agent" to useragent)
                .responseString()
        return when (result) {
            is Result.Success -> {
                mapper.readValue(result.value)
            }
            else -> throw Exception("failed getting cursemeta data")
        }
    }

    val getAllFilesForAddOn = ::getAllFilesForAddOnCall.memoize()

}

fun AddOn.latestFile(versions: List<String>): AddOnFile {
    val files = getAllFilesForAddOn(id)
    return if(versions.isEmpty()) {
        val version = files.map { it.gameVersion.sortedWith(VersionComparator.reversed()).first() }.sortedWith(VersionComparator.reversed()).first()
        files.filter { it.gameVersion.contains(version) }.sortedByDescending { it.fileDate }.first()
    } else {
        files.filter { it.gameVersion.intersect(versions).isNotEmpty() }.sortedByDescending { it.fileDate }.first()
    }
}