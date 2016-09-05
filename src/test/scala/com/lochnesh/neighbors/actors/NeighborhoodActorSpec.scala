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

      "build a new house" in {
        val neighborhood = system.actorOf(Props(new NeighborhoodActor("one house")))
        val buildHouse = BuildHouse("1234 Main St")
        neighborhood ! buildHouse
        expectMsg("house built 1234 Main St")
      }

      "build many new houses" in {
        val neighborhood = system.actorOf(Props(new NeighborhoodActor("multiple houses")))
        val mainSt = BuildHouse("1234 Main St")
        val centerSt = BuildHouse("1234 Center St")
        neighborhood ! mainSt
        neighborhood ! centerSt
        expectMsg("house built 1234 Main St")
        expectMsg("house built 1234 Center St")
      }

      "not build multiple homes at the same address" in {
        val neighborhood = system.actorOf(Props(new NeighborhoodActor("duplicate houses")))
        val mainSt = BuildHouse("1234 Main St")
        neighborhood ! mainSt
        neighborhood ! mainSt
        expectMsg("house built 1234 Main St")
        expectMsg("there is already a house at 1234 Main St")
      }

    }
  }
