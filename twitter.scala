import org.scala_twitter._
import scala.util.matching.Regex
object Twitter
{
	var twitter:TwitterParser = null
	private var username:String = null
	private var password:String = null
	def main(args:Array[String]) =
	{
		try
		{
			username=args(0);
			password=args(1);
			twitter = new TwitterParser(username,password);
			ui()
		}
		catch
		{
			case e:ArrayIndexOutOfBoundsException => { println("Need username and password") }
		}
	}
	def ui():Unit =
	{
		//Really simple ui, for the purposes of testing
		print("Command: ")
		var input = readLine()
		println()
		input = input.trim()
		var control:String = input.slice(0,2)
		control match
		{
			case ":u" => twitter.update(input.substring(2).trim())
			case ":f" => display(twitter.getTimeline("friends"))
			case ":p" => display(twitter.getTimeline("public"))
			case ":d" => display(twitter.getDMs)
			case ":q" => return
			case _ => println("Not a command")
		}
		println()
		ui()
	}
	def pad(x:String):String = x + (" " * (15-x.length))
	def display(tw:Array[Tweet]) =
	{
		tw.foreach(tweet =>
		{
			//Print @replies in red
			if (tweet.in_reply_to_screen_name == username)
			{
				print(scala.Console.RED)
			}
			println(pad(tweet.screen_name) + ": " + tweet.text.replaceAll("<3","\u2665") + scala.Console.RESET)
		})
	}
}
