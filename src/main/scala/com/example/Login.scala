package com.example

import Practice.ActorDemo
import akka.actor.{ActorSystem, Props}
import spray.http.HttpCookie
import spray.routing.Directive1
import spray.routing.Directives._

/**
  * Created by tameem on 24/9/16.
  */
trait Login {

  def checkLogin(username: String, password: String): Boolean = {
    if (username == "demo" && password == "demo") {
      setCookie(HttpCookie("loginCookie", content = "loginCookie"))
      true
    }
    else {
      false
    }
  }

  def checkUser:Directive1[Option[Boolean]]={
    /*optionalCookie("loginCookie") {
      case Some(nameCookie) => complete(s"The logged in user is '${nameCookie.content}'")
      case None => complete("No user logged in")
    }*/
    extract[Option[Boolean]](temp => {
      val cookies = temp.request.cookies.filter(_.name == "loginCookie")
      if (cookies.nonEmpty) {
        Some(true)
      } else {
        None
      }
    }
    )
  }

  val system = ActorSystem()
  val helloActor = system.actorOf(Props(new ActorDemo()))
  def invalidLog(userName: String): Unit ={
    helloActor ! userName+" is invalid user"
  }
}
