package bz.dao

import reactivemongo.api.{MongoDriver, DB}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by joey on 15/2/3.
 */
object BaseMongoDbDao {
  val driver = new MongoDriver
  val connection = driver.connection(List("localhost"))
  def db: DB = connection("wechat_app")
}
