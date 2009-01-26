package org.scala_twitter
import scala.xml._
/**
 * Direct Message holder
 */
class DM extends Tweet
{
	override def parse(xml:Node):Unit =
	{
		created_at = (xml \ "created_at").text
		msg_id = (xml \ "id").text.toInt
		text = (xml \ "text").text
		user_id = (xml \ "sender" \ "id").text.toInt
		user_name = (xml \ "sender" \ "name").text
		screen_name = (xml \ "sender" \ "screen_name").text
		location = (xml \ "sender" \ "location").text
		user_description = (xml \ "sender" \ "user_description").text
		profile_image_url = (xml \ "sender" \ "profile_image_url").text
		user_url = (xml \ "sender" \ "url").text
		user_protected= (xml \ "sender" \ "protected").text.toBoolean
		followers_count = (xml \ "sender" \ "followers_count").text.toInt
	}
}
/**
 * Companion factory object
 */
object DM
{
	def apply(x:Node):DM =
	{
		var i = new DM
		i.parse(x)
		return i
	}
}
