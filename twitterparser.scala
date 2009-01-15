package org.scala_twitter
import java.net._
import java.io._
import scala.util.parsing.json._
import scala.collection.jcl.ArrayList
/**
 * Connects to twitter JSON API
 */
class TwitterParser(username:String,password:String){
	private val connect = new Auth(username,password);
	private def getConnection(turl:String):URLConnection = {
		try{
			//Fail fast on bad usernames, passwords, or urls
			System.setProperty("http.maxRedirects","2");
			Authenticator.setDefault(connect)
			var url:URL = new URL(turl)
			url.openConnection
		}
		catch{
			case e:ProtocolException => {println("username or password incorrect");null}
			case e:MalformedURLException => {println("url was bad");null}
			case e:Exception => {println(e.getMessage());null}
		}
	}
	/**
	 * Pull line from twitter url JSON feed and parse it
	 */
	def get(turl:String):Array[Tweet] = {
		var connection:URLConnection = getConnection(turl)
		if (connection == null) return null;
		var content:InputStream = connection.getInputStream();
		var reader:BufferedReader = new BufferedReader(new InputStreamReader(content));
		var line:String = reader.readLine();
		var json = JSON.parseFull(line);
		var goodStuff:List[Any] = json match {
			case Some(j) => {j.asInstanceOf[List[Any]]}
			case None => {null}
		}
		var tweets:ArrayList[Tweet] = new ArrayList[Tweet]
		if (goodStuff != null) {
			goodStuff.foreach(tweet => {
				var i = new Tweet
				i.parse(tweet.asInstanceOf[List[Any]])
				tweets.add(i)
			})
		}
		return tweets.toArray[Tweet]
	}
	/**
	 * Push an update to twitter
	 */
	def push(update:String):Unit = {
		var connection:URLConnection = getConnection("http://twitter.com/statuses/update.json")
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
	class Auth(username:String, password:String) extends Authenticator{
		override def getPasswordAuthentication():PasswordAuthentication = {new PasswordAuthentication(username,password.toCharArray())}
	}
}
