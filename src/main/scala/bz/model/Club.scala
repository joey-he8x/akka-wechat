package bz.model

import reactivemongo.bson.{Macros, BSONObjectID}

/**
 * Created by joey on 15/2/3.
 */
case class Club(
                 _id: BSONObjectID = BSONObjectID.generate,
                 name: String,
                 creatorId: String,
                 limit: Double,
                 members: List[String] = List())

object Club{
  implicit val clubHandler = Macros.handler[Club]
}