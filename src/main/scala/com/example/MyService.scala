package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with Login{

  val myRoute =
    path("login") {
      println(" Log in status - " + checkUser)
      checkUser {
        case Some(true) => {
          complete {"Already Logged In"}}
        case None => get {
            complete {
              "Invaliq Request"
            }
          } ~ post {
            entity(as[FormData]) { formData => {
              val fieldsMap = formData.fields.toMap
              fieldsMap.get("username") match {
                case Some(userName) => fieldsMap.get("password") match {
                  case Some(password) => if(checkLogin(userName,password)){
                    complete("The user is logged in")
                  }else{
                    invalidLog(fieldsMap.get("username").head)
                    complete("Invalid Credentials")
                  }
                  case None =>complete("Invalid Parameters")
                }
                case None => complete("Invalid Parameters")
              }

            }
            }
          }
        }
      }
}