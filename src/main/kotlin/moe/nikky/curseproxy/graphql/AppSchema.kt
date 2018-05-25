package moe.nikky.curseproxy.graphql

import com.github.pgutkowski.kgraphql.KGraphQL
import moe.nikky.curseproxy.dao.AddonStorage
import java.time.LocalDate

class AppSchema(private val storage: AddonStorage) {

    val schema = KGraphQL.schema {
//
//        configure {
//            useDefaultPrettyPrinter = true
//        }
//
//        stringScalar<LocalDate> {
//            serialize = { date -> date.toString() }
//            deserialize = { dateString -> LocalDate.parse(dateString) }
//        }
//
//        query("sightings") {
//            resolver { size: Int? -> storage.getAll(size?.toLong() ?: 10) }.withArgs {
//                arg<Long> { name = "size"; defaultValue = 10; description = "The number of records to return" }
//            }
//        }
//
//        query("sighting") {
//            resolver { id: Int -> storage.getSighting(id) ?: throw NotFoundException("Sighting with id: $id does not exist") }
//        }
//
//        query("topSightings") {
//            description = "Returns a list of the top 10 state,country based on the number of sightings"
//
//            resolver(storage::getTopSightings)
//        }
//
//        mutation("createUFOSighting") {
//            description = "Adds a new UFO Sighting to the database"
//
//            resolver { input: CreateUFOSightingInput -> storage.createSighting(input.toUFOSighting()) }
//        }
//
//        inputType<CreateUFOSightingInput>()
//
//        type<UFOSighting> {
//            description = "A UFO sighting"
//
//            property(UFOSighting::date) {
//                description = "The date of the sighting"
//            }
//        }
//
//        type<CountrySightings> {
//            description = "A country sighting; contains total number of occurrences"
//        }
    }

}