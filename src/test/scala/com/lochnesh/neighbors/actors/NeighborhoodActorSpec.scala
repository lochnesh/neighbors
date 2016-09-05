package com.lochnesh.neighbors.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class NeighborhoodActorSpec extends TestKit(ActorSystem("NeighborhoodActorSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

    override def afterAll {
      TestKit.shutdownActorSystem(system)
    }

    "A neighborhood actor" must {

      "build new homes" in {
        val neighborhood = system.actorOf(Props[NeighborhoodActor])
        val buildHouse = BuildHouse("1234 Main St")
        neighborhood ! buildHouse
        expectMsg("house built")
      }

    }
  }
