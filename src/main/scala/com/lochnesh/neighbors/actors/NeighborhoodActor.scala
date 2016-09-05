package com.lochnesh.neighbors.actors

import akka.persistence.{SnapshotOffer, PersistentActor}

//commands
case class BuildHouse(address: String)
case class HomeCount()

//events
case class HouseBuilt(address: String)

case class NeighborhoodState(houses: List[String] = Nil) {
  def updated(evt: HouseBuilt): NeighborhoodState = copy(evt.address :: houses)
  def size: Int = houses.length
  override def toString: String = houses.reverse.toString
}

class NeighborhoodActor(id: String) extends PersistentActor {
  override def persistenceId: String = id

  var state = NeighborhoodState()

  def updateState(event: HouseBuilt): Unit = state = state.updated(event)

  def houses: Seq[String] = state.houses

  override def receiveRecover: Receive = {
      case evt: HouseBuilt => updateState(evt)
      case SnapshotOffer(_, snapshot: NeighborhoodState) => state = snapshot
  }

  override def receiveCommand: Receive = {
    case cmd: BuildHouse ⇒
      if (houses.contains(cmd.address)) {
        sender() ! s"there is already a house at ${cmd.address}"
      } else {
        persist(HouseBuilt(cmd.address))(updateState)
        sender() ! s"house built ${cmd.address}"
      }

    case count: HomeCount ⇒ sender() ! houses.size
  }
}
