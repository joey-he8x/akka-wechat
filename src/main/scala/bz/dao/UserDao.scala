package bz.dao

import bz.model.User
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.dao.BsonDao
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15-2-4.
 */
object UserDao extends BsonDao[User, BSONObjectID](BaseMongoDbDao.db, "users")
