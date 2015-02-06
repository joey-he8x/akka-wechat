package bz.model

import reactivemongo.bson.Macros

/**
 * Created by joey on 15-2-4.
 */
case class WechatApp(
                _id: Int,
                secret: String
                )
object WechatApp{
  implicit val appHandler = Macros.handler[WechatApp]
}