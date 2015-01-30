package wechat.model

/**
 * Created by joey on 15-1-30.
 */
class User(val name:String) {

}

object User {
  def find(name:String)={
    new User(name)
  }
}