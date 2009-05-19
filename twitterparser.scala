package org.scala_twitter
import java.net._
import java.io._
import scala.xml._
import scala.collection.jcl.ArrayList
/**
 * Connects to twitter xml API
 */
class TwitterParser(username:String,password:String){
	private val http = "http://twitter.com/"
	private val connect = new Auth(username,password);
	private def getConnection(turl:String):URLConnection = {
		try {
			//Fail fast on bad usernames, passwords, or urls
			System.setProperty("http.maxRedirects","2");
			Authenticator.setDefault(connect)
			var url:URL = new URL(turl)
			url.openConnection
		}
		catch {
			case e:ProtocolException => {println("username or password incorrect");null}
			case e:MalformedURLException => {println("url was bad");null}
			case e:Exception => {println(e.getMessage());null}
		}
	}
	/**
	 * Pull line from twitter url xml feed and parse it
	 */
	private def get(turl:String):Array[Tweet] = {
		if (turl == null) return null;
		var connection:URLConnection = getConnection(turl)
		if (connection == null) return null;
		var content:InputStream = connection.getInputStream();
		var reader:BufferedReader = new BufferedReader(new InputStreamReader(content));
		val xml = XML.load(reader)
		var tweets = new ArrayList[Tweet]
		xml.label match {
			case "statuses" => (xml \ "status").foreach(tweet => tweets.add(Tweet(tweet)))
			case "direct-messages" => (xml \ "direct_message").foreach(dm => tweets.add(DM(dm)))
			case _ => println(xml)
		}
		return tweets.toArray[Tweet]
	}
	/**
	 * Push an update to twitter
	 */
	private def push(url:String,update:String):Unit =
	{
		var connection:URLConnection = getConnection(url)
		if (connection == null) return;
		connection.setDoOutput(true)
		var wr = new OutputStreamWriter(connection.getOutputStream())
		wr.write(URLEncoder.encode("status","UTF-8") + "=" + URLEncoder.encode(update,"UTF-8"))
		wr.flush()
		var rd = new BufferedReader(new InputStreamReader(connection.getInputStream()))
		rd.readLine()
		wr.close()
		rd.close()
	}
	//Only necessary for Authenticator
	class Auth(username:String, password:String) extends Authenticator
	{
		override def getPasswordAuthentication():PasswordAuthentication = new PasswordAuthentication(username,password.toCharArray())
	}
	/**
	 * Get a timeline
	 */
	def getTimeline(timeline:String):Array[Tweet] =
	{
		var url:String = http+"statuses/"
		if (timeline == "friends" || timeline == "public" || timeline == "user")
		{
			url+=timeline+"_timeline.xml?count=40"
		}
		else
		{
			url = null
		}
		return get(url)
	}
	/**
	 * Send update to twitter
	 */
	def update(status:String):Unit = push("http://twitter.com/statuses/update.xml",status)
	/**
	 * Delete message by id
	 */
	def delete(id:Int):Unit = push("http://twitter.com/statuses/destroy/"+id+".xml","")
	/**
	 * Get a single message by id
	 */
	def getMsg(id:Int):Array[Tweet] = get("http://twitter.com/statuses/show/"+id+".xml")
	def getReplies():Array[Tweet] = get("http://twitter.com/statuses/replies.xml")
	def getDMs():Array[Tweet] = get("http://twitter.com/direct_messages.xml")
	def getSentDMs():Array[Tweet] = get("http://twitter.com/direct_messages/sent.xml")
	def destroyDM(id:Int):Unit = push("http://twitter.com/direct_messages/destroy/"+id+".xml","")
	def follow(user:String):Unit = push("http://twitter.com/friendships/create/"+user+".xml?follow=true","")
	def leave(user:String):Unit = push("http://twitter.com/friendships/destroy/"+user+".xml","")
	def block(user:String):Unit = push("http://twitter.com/blocks/create/"+user+".xml","")
	def unBlock(user:String):Unit = push("http://twitter.com/blocks/destroy/"+user+".xml","")
	def favorites():Array[Tweet] = get("http://twitter.com/favorites.xml")
	def createFavorite(id:Int):Unit = push("http://twitter.com/favorites/create/"+id+".xml","")
	def destroyFavorite(id:Int):Unit = push("http://twitter.com/favorites/destroy/"+id+".xml","")
	/**
	 * Shorten a url with is.gd
	 */
	def shortenURL(url:String):String =
	{
		var makeShort = new URL("http://is.gd/api.php?longurl="+url)
		var connection = new BufferedReader(new InputStreamReader(makeShort.openConnection().getInputStream()))
		var shortUrl:String = connection.readLine()
		return shortUrl
	}
}
