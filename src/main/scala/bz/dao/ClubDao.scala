package bz.dao

import bz.model.Club
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.dao.BsonDao
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15/2/3.
 */
object ClubDao extends BsonDao[Club, BSONObjectID](BaseMongoDbDao.db, "clubs")

