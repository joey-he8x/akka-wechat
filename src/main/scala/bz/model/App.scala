package bz.model

import reactivemongo.bson.BSONObjectID

/**
 * Created by joey on 15-2-4.
 */
case class App(
                _id: BSONObjectID = BSONObjectID.generate,
                endpoint: Int,
                secret: String
                )
