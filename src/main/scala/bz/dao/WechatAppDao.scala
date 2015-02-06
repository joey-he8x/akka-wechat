package bz.dao

import bz.model.WechatApp
import reactivemongo.bson.BSONObjectID
import reactivemongo.extensions.dao.BsonDao

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15-2-4.
 */
object WechatAppDao extends BsonDao[WechatApp, BSONObjectID](BaseMongoDbDao.db, "wechat_apps")
