package clienttest

import org.junit.Test
import iterace.IteRaceOption._
import iterace.IteRaceOption
import org.junit.Ignore
import iterace.stage.RaceAbstractTest

class TestAsyncBySig extends RaceAbstractTest {
  import IteRaceOption._

  val entrySigs = Array(
    "org\\\\.connectbot\\\\..*\\\\.on",
    "org\\\\.vudroid\\\\..*\\\\.on.*",
    "org\\\\.geometerplus\\\\..*\\\\.on")

  override val options = Set[IteRaceOption](Filtering, TwoThreads, BubbleUp)
  val entryClass = "Lasyncsubjects/AndroidTest"
  override lazy val entryMethod = "onCreate(Landroid/os/Bundle;)V"

  @Test def compute1 = expect(entrySigs(0), "\n\n")

  @Test def compute2 = expect(entrySigs(1), "\n\n")

  @Test def compute3 = expect(entrySigs(2), "\n\n")
}