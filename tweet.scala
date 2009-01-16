package org.scala_twitter
/**
 * Simple object that holds tweets
 */
class Tweet {
	var user_description:String = null
	var user_url:String = null
	var user_name:String = null
	var profile_image_url:String = null
	var user_protected:Boolean = false
	var screen_name:String = null
	var location:String = null
	var user_id:Double = 0.0
	var followers_count:Double = 0.0
	var in_reply_to_screen_name:String = null
	var in_reply_to_status_id:Double = 0.0
	var in_reply_to_user_id:String = null
	var truncated:Boolean = false
	var favorited:Boolean = false
	var created_at:String = null
	var text:String = null
	var source:String = null
	var msg_id:Double = 0.0
	def parse(json:List[Tuple2[String,Any]]):Unit = {
		json.foreach(tp => {
			tp._1 match {
				case "user" => {parseUser(tp._2.asInstanceOf[List[Tuple2[String,Any]]])}
				case "in_reply_to_screen_name" => { in_reply_to_screen_name = tp._2.asInstanceOf[String] }
				case "in_reply_to_status_id" => { in_reply_to_status_id = tp._2.asInstanceOf[Double] }
				case "truncated" => { truncated = tp._2.asInstanceOf[Boolean] }
				case "favorited" => { favorited = tp._2.asInstanceOf[Boolean] }
				case "create_at" => { created_at = tp._2.asInstanceOf[String] }
				case "text" => { text = tp._2.asInstanceOf[String] }
				case "id" => { msg_id = tp._2.asInstanceOf[Double] }
				case "source" => { source = tp._2.asInstanceOf[String] }
				case _ => {}
			}
		})
	}
	def parseUser(user_list:List[Tuple2[String,Any]]):Unit = {
		user_list.foreach(p => {
			p._1 match {
				case "description" => { user_description = p._2.asInstanceOf[String] }
				case "url" => { user_url = p._2.asInstanceOf[String] }
				case "name" => { user_name = p._2.asInstanceOf[String] }
				case "profile_image_url" => { profile_image_url = p._2.asInstanceOf[String] }
				case "protected" => { user_protected = p._2.asInstanceOf[Boolean] }
				case "screen_name" => { screen_name = p._2.asInstanceOf[String] }
				case "location" => { location = p._2.asInstanceOf[String] }
				case "id" => { user_id = p._2.asInstanceOf[Double] }
				case "followers_count" => { followers_count = p._2.asInstanceOf[Double] }
				case _ => {}
			}
		})
	}
}
