package net.aicomp.entity

import org.scalacheck.Properties
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

object PointProp extends Properties("Point") {
  val field = new Field(7)

  property("shortestPathTo") = forAll(pointWithin, pointWithin) { (p1: Point, p2: Point) =>
    p1.shortestPathTo(p2, field).get.size == p1.distance(p2)
  }

  property("shortestPathTo") = forAll(pointWithin | pointWithout, pointWithout) { (p1: Point, p2: Point) =>
    p1.shortestPathTo(p2, field) == None
  }

  property("shortestPathTo") = forAll(pointWithout, pointWithin | pointWithout) { (p1: Point, p2: Point) =>
    p1.shortestPathTo(p2, field) == None
  }

  property("shortestPathTo") = forAll(pointWithout, pointWithout) { (p1: Point, p2: Point) =>
    p1.shortestPathTo(p2, field) == None
  }

  def pointWithin = (for {
    x <- Gen.choose(-field.radius, field.radius)
    y <- Gen.choose(-field.radius, field.radius)
  } yield Point(x, y)).filter(p => p.within(field.radius))

  def pointWithout = (for {
    x <- Gen.choose(-field.radius * 2, field.radius * 2)
    y <- Gen.choose(-field.radius * 2, field.radius * 2)
  } yield Point(x, y)).filter(p => !p.within(field.radius))

  implicit def arbPoint: Arbitrary[Point] =
    Arbitrary {
      for {
        x <- Gen.choose(-field.radius, field.radius)
        y <- Gen.choose(-field.radius, field.radius)
      } yield Point(x, y)
    }
}
