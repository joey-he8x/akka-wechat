package api

import scala.xml.NodeSeq

import spray.routing.directives.MarshallingDirectives._

/**
 * Created by joey on 14-9-5.
 */
trait WechatXmlContent {

  entity(as[NodeSeq]).map[NodeSeq] { node =>

    node
  }

}
