package bz.model

import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONObjectID

/**
 * Created by joey on 15-1-30.
 */
case class User(_id: BSONObjectID = BSONObjectID.generate,
                name:String)

object User {
}

trait UserDao {
}