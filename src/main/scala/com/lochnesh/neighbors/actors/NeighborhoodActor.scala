package com.lochnesh.neighbors.actors

import akka.persistence.PersistentActor

//commands
case class BuildHouse(address: String)

//events
case class HouseBuilt(address: String)

case class NeighborhoodState(houses: List[String] = Nil) {
  def updated(evt: HouseBuilt): NeighborhoodState = copy(evt.address :: houses)
  def size: Int = houses.length
  override def toString: String = houses.reverse.toString
}

class NeighborhoodActor extends PersistentActor {
  override def persistenceId: String = "neighborhood-actor"

  var state = NeighborhoodState()

  def updateState(event: HouseBuilt): Unit = state = state.updated(event)

  def houses: Seq[String] = state.houses

  override def receiveRecover: Receive = {
    case _ ⇒ println("receiveRecover")
  }

  override def receiveCommand: Receive = {
    case cmd: BuildHouse ⇒ persist(HouseBuilt(cmd.address))(updateState)
  }
}
