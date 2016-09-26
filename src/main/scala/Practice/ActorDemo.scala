package Practice

import java.io.{File, PrintWriter}

import akka.actor.Actor
import spray.http.Uri.Path


/**
  * Created by tameem on 26/9/16.
  */
class ActorDemo extends Actor{
  val file = new File("/home/tameem/Desktop/ScalaDemo.txt" )
  val pw = new PrintWriter(file)
  override def receive: Receive = {
    case v:String => {
      pw.write(System.currentTimeMillis()+","+v+"\n")
      pw.flush()
    }
    case _ => println("What??????")
  }
  override def postStop(): Unit ={
    pw.close
  }
}

