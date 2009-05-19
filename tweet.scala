package org.scala_twitter
import scala.xml._
/**
 * Simple object that holds tweets
 */
class Tweet
{
	var user_description:String = null
	var user_url:String = null
	var user_name:String = null
	var profile_image_url:String = null
	var user_protected:Boolean = false
	var screen_name:String = null
	var location:String = null
	var user_id:Int = 0
	var followers_count:Int = 0
	var in_reply_to_screen_name:String = null
	var in_reply_to_status_id:Option[Long] = None
	var in_reply_to_user_id:Option[Long] = None
	var truncated:Boolean = false
	var favorited:Boolean = false
	var created_at:String = null
	var text:String = null
	var source:String = null
	var msg_id:Long = 0
	def parse(xml:Node):Unit = {
		created_at = (xml \ "created_at").text
		msg_id = (xml \ "id").text.toLong
		text = (xml \ "text").text.replaceAll("&lt;","<").replaceAll("&gt;",">")
		source = (xml \ "source").text
		truncated = (xml \ "truncated").text.toBoolean
		in_reply_to_status_id = (xml \ "in_reply_to_status_id").text
		in_reply_to_user_id = (xml \ "in_reply_to_user_id").text
		favorited = (xml \ "favorited").text.toBoolean
		in_reply_to_screen_name = (xml \ "in_reply_to_screen_name").text
		user_id = (xml \ "user" \ "id").text.toInt
		user_name = (xml \ "user" \ "name").text
		screen_name = (xml \ "user"\ "screen_name").text
		location = (xml \ "user" \ "location").text
		user_description = (xml \ "user" \ "description").text
		profile_image_url = (xml \ "user" \ "profile_image_url").text
		user_url = (xml \ "user" \ "url").text
		user_protected = (xml \ "user" \ "protected").text.toBoolean
		followers_count = (xml \ "user" \ "followers_count").text.toInt
	}
	implicit def optioner(x:String):Option[Long] = {
		x match {
			case "" => None
			case _ => Some(x.toLong)
		}
	}
}
/**
 * Companion object that allows for factory creation
 */
object Tweet
{
	def apply(x:Node):Tweet =
	{
		var i = new Tweet
		i.parse(x)
		return i
	}
}
