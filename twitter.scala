import org.scala_twitter._
import scala.xml._
import java.text.SimpleDateFormat

object Twitter
{
	var twitter:TwitterParser = null
	private var username:String = null
	private var password:String = null
	def main(args:Array[String]) = {
		try{
			var config = XML.loadFile(".scala_twitter.conf")
			username = (config\"username").text
			password = (config\"password").text
			twitter = new TwitterParser(username,password);
			ui()
		}
		catch{
			case e:java.io.FileNotFoundException => {genconf()}
			case e:Exception => { e.printStackTrace }
		}
	}
	//Really simple ui, for the purposes of testing
	def ui():Unit = {
		val help = List("Commands:",":r - Replies",":f - Friends Timeline",":u - Update, rest of line is parsed as a status",":p - Public Timeline",":d - Direct Messages",":t - Follow this Person, rest of line is parsed as username to follow",":q - Quit",":? - This listing")
		help.foreach{h => println(h)};
		var run = true;
		while (run){
			print("Command: ")
			var input = readLine()
			println()
			input = input.trim()
			var control:String = input.slice(0,2)
			clear_screen;
			control match{
				case ":u" => twitter.update(input.substring(2).trim())
				case ":f" => display(twitter.getTimeline("friends"))
				case ":p" => display(twitter.getTimeline("public"))
				case ":d" => display(twitter.getDMs)
				case ":r" => display(twitter.getReplies)
				case ":t" => twitter.follow(input.substring(2).trim())
				case ":q" => Predef.exit //Quick exit!
				case ":?" => help.foreach{ h => println(h) };
				case _ => println("Not a command")
			}
		}
	}
	/* Pad the username with a buffer */
	def pad(x:String):String = x + (" " * (15-x.length))
	/* Format the twitter date to something more sane */
	def formatDate(x :String):String = {
		var incomingFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss +0000 yyyy");
		var outgoingFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
		var newDate = incomingFormat.parse(x);
		var out = outgoingFormat.format(newDate);
		return out;
	}
	def display(tw:Array[Tweet]) = {
		tw.foreach(tweet => {
			//Print @replies in cyan
			if (tweet.in_reply_to_screen_name == username) {
				print(Console.CYAN)
			}
			println("<" + formatDate(tweet.created_at) + ">" + " " + tweet.screen_name + ": " + tweet.text + Console.RESET)
		})
	}
	/* Create an XML config file */
	def genconf():Unit = {
		println("Config file not found");
		print("Username: ")
		var u:String = readLine
		print("Password: ")
		var p:String = readLine
		println("Saving config")
		var a:Elem = <scala_config>\
			<username>{u}</username>\
			<password>{p}</password>\
			</scala_config>
		XML.save(".scala_twitter.conf",a,"UTF-8");
		twitter = new TwitterParser(u,p)
		ui
	}
	/* Clear the screen - only works in sane terminals */
	def clear_screen():Unit = {
		print("\033[2J") //Clear screen of input
		print("\033[1;1H"); //Move cursor to upper left corner
	}
}
