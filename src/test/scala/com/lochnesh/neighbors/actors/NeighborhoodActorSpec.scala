package com.lochnesh.neighbors.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class NeighborhoodActorSpec extends TestKit(ActorSystem("NeighborhoodActorSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

    private def fixture = {
      new {
        val length = 15
        val neighborhood = system.actorOf(Props(new NeighborhoodActor(scala.util.Random.alphanumeric.take(length).mkString)))
      }
    }

    override def afterAll {
      TestKit.shutdownActorSystem(system)
    }

    "A neighborhood actor" must {

      "initialize with 0 homes" in {
        fixture.neighborhood ! HomeCount()
        expectMsg(0)
      }

      "build a new house" in {
        val neighborhood = fixture.neighborhood
        val buildHouse = BuildHouse("1234 Main St")
        neighborhood ! buildHouse
        expectMsg("house built 1234 Main St")
        neighborhood ! HomeCount()
        expectMsg(1)
      }

      "build many new houses" in {
        val neighborhood = fixture.neighborhood
        val mainSt = BuildHouse("1234 Main St")
        val centerSt = BuildHouse("1234 Center St")
        neighborhood ! mainSt
        neighborhood ! centerSt
        expectMsg("house built 1234 Main St")
        expectMsg("house built 1234 Center St")
        neighborhood ! HomeCount()
        expectMsg(2)
      }

      "not build multiple homes at the same address" in {
        val neighborhood = fixture.neighborhood
        val mainSt = BuildHouse("1234 Main St")
        neighborhood ! mainSt
        expectMsg("house built 1234 Main St")
        neighborhood ! mainSt
        expectMsg("there is already a house at 1234 Main St")
        neighborhood ! HomeCount()
        expectMsg(1)
      }

    }
  }
