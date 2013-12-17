package clienttest

import org.junit.Test
import iterace.IteRaceOption._
import iterace.IteRaceOption
import org.junit.Ignore
import iterace.stage.RaceAbstractTest

class TestAsync extends RaceAbstractTest {
  import IteRaceOption._
  // 0: false positives, race on list
  // 1: false positives
  // 2: throw exception
  // 4: false positives
  // 5: false negative related to database query
  // 6: false positives
  // 7: false positives
  // 8: throw exception
  // 9: false positives
  // 10: false positives
  // TODO: detect race between extracted code and all other public/protected methods
  val entryClasses = Array("Lasyncsubjects/AndroidTest",
    "Lorg/connectbot/PubkeyListActivity",
    "Lorg/connectbot/PubkeyListActivity\"$12$1\"",
    "Lorg/connectbot/service/TerminalKeyListener",
    "Lorg/connectbot/HostListActivity",
    "Lorg/connectbot/SettingsActivity",
    "Lorg/connectbot/HostEditorActivity",
    "Lorg/vudroid/core/views/ZoomRoll",
    "Lorg/vudroid/core/DocumentView\"$3\"",
    "Lorg/geometerplus/android/fbreader/image/ImageViewActivity",
    "Lorg/geometerplus/zlibrary/ui/android/view/ZLAndroidPaintContext")
  val entryMethods = Array("onCreate(Landroid/os/Bundle;)V",
    "onActivityResult(IILandroid/content/Intent;)V",
    "onClick(Landroid/content/DialogInterface;I)V",
    "onKey(Landroid/view/View;ILandroid/view/KeyEvent;)Z",
    "startConsoleActivity()Z",
    "onCreate(Landroid/os/Bundle;)V",
    "onCreate(Landroid/os/Bundle;)V",
    "<init>(Landroid/content/Context;Lorg/vudroid/core/models/ZoomModel;)V",
    "run()V",
    "onCreate(Landroid/os/Bundle;)V",
    "drawImage(IILorg/geometerplus/zlibrary/core/image/ZLImageData;Lorg/geometerplus/zlibrary/core/view/ZLPaintContext\"$Size\";Lorg/geometerplus/zlibrary/core/view/ZLPaintContext\"$ScalingType\";)V")

  val entryClass = entryClasses(1)
  override val options = Set[IteRaceOption](Filtering, TwoThreads, BubbleUp)

  override lazy val entryMethod = entryMethods(1)

  @Test def compute1 = expect(entryClasses(1), entryMethods(1), "\n\n")

  @Test def compute2 = expect(entryClasses(2), entryMethods(2), "\n\n")

  @Test def compute3 = expect(entryClasses(3), entryMethods(3), "\n\n")

  @Test def compute4 = expect(entryClasses(4), entryMethods(4), "\n\n")

  @Test def compute5 = expect(entryClasses(5), entryMethods(5), "\n\n")

  @Test def compute6 = expect(entryClasses(6), entryMethods(6), "\n\n") // TODO: FP because of code before async.execute 

  @Test def compute7 = expect(entryClasses(7), entryMethods(7), "\n\n")

  @Test def compute8 = expect(entryClasses(8), entryMethods(8), "\n\n")

  @Test def compute9 = expect(entryClasses(9), entryMethods(9), "\n\n")

  @Test def compute10 = expect(entryClasses(10), entryMethods(10), "\n\n")

  @Test def compute0 = expect("Lasyncsubjects/AndroidTest", "onCreate(Landroid/os/Bundle;)V", """
Loop: async task: Node: < Application, Lasyncsubjects/AndroidTest$1, doInBackground([Lasyncsubjects/AndroidTest$Particle;)Ljava/lang/Void; > Context: Everywhereasyncsubjects.AndroidTest$Particle: asyncsubjects.AndroidTest.onCreate(AndroidTest.java:31)
 .x
   (a)  asyncsubjects.AndroidTest.onCreate(AndroidTest.java:34)
   (b)  asyncsubjects.AndroidTest$1.doInBackground(AndroidTest.java:23)
asyncsubjects.AndroidTest: com.ibm.wala.FakeRootClass.fakeRootMethod(FakeRootClass.java:)
 .raceOnMe
   (a)  asyncsubjects.AndroidTest.onCreate(AndroidTest.java:33)
   (b)  asyncsubjects.AndroidTest$1.doInBackground(AndroidTest.java:22)
""")
}