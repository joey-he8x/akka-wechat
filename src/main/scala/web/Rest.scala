package web

import api.{WechatApi}
import core.{Core, BootedCore, CoreActors}


object Rest extends App with BootedCore with Core with CoreActors with WechatApi with StaticResources