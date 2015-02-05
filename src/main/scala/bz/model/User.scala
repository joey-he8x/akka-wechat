package bz.model

import org.joda.time.DateTime
import reactivemongo.bson.{Macros, BSONObjectID}

/**
 * Created by joey on 15-1-30.
 */
case class User(
                 _id: BSONObjectID = BSONObjectID.generate,
                 openId: String,
                 appId: BSONObjectID,
                 subscribeTime: DateTime,
                 lastUpdateTime: DateTime,
                 nickname: Option[String],
                 sex: Option[Int],
                 city: Option[String],
                 province: Option[String],
                 country: Option[String],
                 language: Option[String],
                 headimgurl: Option[String]
                 )

object User {
  implicit val userHandler = Macros.handler[User]
}