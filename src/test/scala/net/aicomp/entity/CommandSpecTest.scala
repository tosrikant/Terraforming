package net.aicomp.entity

import org.specs2.mutable._

class CommandSpecTest extends SpecificationWithJUnit {
  "Command" should {
    "return MoveCommand" in {
      Command.moveCommand(List("2", "4", "ur")) must_==
        MoveCommand(Point(2, 4), Direction.ur)
      Command.moveCommand(List("0x2", "4", "ur")) must throwA[CommandException]
    }
    "return BuildCommand" in {
      Command.buildCommand(List("2", "3", "br")) must_==
        BuildCommand(Point(2, 3), "br")
      Command.buildCommand(List("2", "3", "ur")) must throwA[CommandException]
    }
  }
}
