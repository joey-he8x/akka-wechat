package bz.helper

import bz.dao.ClubDao
import reactivemongo.extensions.dsl.BsonDsl._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15-2-13.
 */
trait ClubHelper {
  val limit = 5
  def checkUserLimit(openId:String) = {
    ClubDao.count("creatorId" $eq openId).map(_<5)
  }
}
