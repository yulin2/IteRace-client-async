package clienttest

import org.junit.Test
import iterace.IteRaceOption._
import iterace.IteRaceOption
import org.junit.Ignore
import iterace.stage.RaceAbstractTest
import iterace.IteRace
import edu.illinois.wala.ipa.callgraph.AnalysisOptions
import edu.illinois.wala.ipa.callgraph.Dependency
import edu.illinois.wala.ipa.callgraph.DependencyNature
import edu.illinois.wala.ipa.callgraph.AnalysisScope

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
    "Lcom/piusvelte/sonet/About",
    "Lcom/owncloud/android/oc_framework_test_project/TestActivity",
    "Lcom/allplayers/android/MessageInbox", //"Lorg/connectbot/service/TerminalKeyListener", // XXX bridge.transport.write
    "Lnet/kw/shrdlu/grtgtfs/TimesActivity",
    "Lorg/connectbot/SettingsActivity",
    "Lorg/connectbot/HostEditorActivity",
    "Lorg/vudroid/core/views/ZoomRoll", // XXX what is the signature for this?
    "Lorg/vudroid/djvudroid/PdfViewerActivity", //"Lorg/vudroid/core/BaseViewerActivity", //"Lorg/vudroid/core/DocumentView\"$3\"", // XXX and this?
    "Lorg/geometerplus/android/fbreader/image/ImageViewActivity", // XXX myBitmap
    "Lorg/geometerplus/zlibrary/ui/android/view/ZLAndroidPaintContext", // XXX sig of drawImage
    "Lcom/artifex/mupdf/MuPDFActivity")
  val entryMethods = Array("onCreate(Landroid/os/Bundle;)V",
    //      "main(\"[\"Ljava/lang/String;)V",
    "<init>()V",
    "<init>()V",
    "<init>()V", //"onKey(Landroid/view/View;ILandroid/view/KeyEvent;)Z",
    "<init>()V", // "startConsoleActivity()Z",
    "<init>()V",
    "<init>()V",
    "<init>(Landroid/content/Context;Lorg/vudroid/core/models/ZoomModel;)V",
    "<init>()V", //"onCreate(Landroid/os/Bundle;)V", //"run()V",
    "<init>()V",
    "blabla()V", //"drawImage(IILorg/geometerplus/zlibrary/core/image/ZLImageData;Lorg/geometerplus/zlibrary/core/view/ZLPaintContext\"$Size\";Lorg/geometerplus/zlibrary/core/view/ZLPaintContext\"$ScalingType\";)V"
    "<init>()V")

  val entryClass = entryClasses(1)
  override val options = Set[IteRaceOption](Filtering, TwoThreads, BubbleUp, Synchronized)

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

  @Test def compute11 = expect(entryClasses(11), entryMethods(11), "\n\n")
  
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

  def getResult(entryClass: String, entryMethod: String, binaryPath: String, jarPath: String, jrePath: String): String = {
    val stringConfig = "wala.jre-lib-path = " + jrePath + "\n" +
      "wala.dependencies.binary = [" + binaryPath + "]\n" +
      "wala.dependencies.jar = [" + jarPath + "]\n" +
      "wala.entry { " +
      "class: " + entryClass + "\n" +
      "method: " + entryMethod + "\n" +
      "}\n"
      println(stringConfig)
    
    val dependencies = jarPath.split(",") map {_.replaceAll("\"", "").trim} map {
      Dependency(_, DependencyNature.Jar, AnalysisScope.Extension)
    }
      
    val analysisE = IteRace(AnalysisOptions(Seq(), dependencies)(config(stringConfig)), options)
    println(analysisE.pa.cg)
    println("entrypoint:" + analysisE.pa.cg.getEntrypointNodes())
    return printRaces(analysisE)
  }
}