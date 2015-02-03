package bz.model

import reactivemongo.bson.{Macros, BSONObjectID}

/**
 * Created by joey on 15/2/3.
 */
case class Club(
                 _id: BSONObjectID = BSONObjectID.generate,
                 name: String,
                 creatorId: Option[BSONObjectID],
                 limit: Int,
                 members: List[String])

object Club{
  implicit val personHandler = Macros.handler[Club]
}