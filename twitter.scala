import org.scala_twitter._
import scala.xml._
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
			ui
		}
		catch{
			case e:java.io.FileNotFoundException => {genconf()}
			case e:Exception => { e.printStackTrace }
		}
	}
	def ui():Unit = {
		//Really simple ui, for the purposes of testing
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
				case ":f " => display(twitter.getTimeline("friends"))
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
	def pad(x:String):String = x + (" " * (15-x.length))
	def display(tw:Array[Tweet]) = {
		tw.foreach(tweet => {
			//Print @replies in red
			if (tweet.in_reply_to_screen_name == username) {
				print(Console.CYAN)
			}
			println(pad(tweet.screen_name) + ": " + tweet.text.replaceAll("<3","\u2665") + Console.RESET)
		})
	}
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
