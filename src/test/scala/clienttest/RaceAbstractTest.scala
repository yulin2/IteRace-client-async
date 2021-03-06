package iterace.stage

import org.junit.Test
import iterace.datastructure.ProgramRaceSet
import org.junit.Assert._
import iterace.IteRace
import iterace.IteRaceOption
import sppa.util.debug
import edu.illinois.wala.Facade._
import edu.illinois.wala.S
import iterace.datastructure.LockSets
import sppa.util.JavaTest
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions
import com.typesafe.config.ConfigResolveOptions
import edu.illinois.wala.ipa.callgraph.AnalysisOptions
import com.typesafe.config.Config
import scala.collection.JavaConversions._

abstract class RaceAbstractTest extends JavaTest {
  debug.activate

  def entryClass: String

  def entryMethod = testName.getMethodName() + "()V"

  val localConfig = (entryClass: String, entryMethod: String) => "wala.entry { " +
    "class: " + entryClass + "\n" +
  	"method: " + entryMethod + "\n" +
    "}\n"
  	
  val localConfigWithSig = (entrySig: String) => "wala.entry { " +
    "signature-pattern: \".*" + entrySig + ".*\" \n" +
    "}\n"

  val config = (localConfig: String) => ConfigFactory.parseString(localConfig) withFallback ConfigFactory.load("test")

  val analysis = (config: Config) => IteRace(AnalysisOptions()(config), options)
  def options: Set[IteRaceOption]

  def printRaces(iterace: IteRace): String = "\n" + iterace.races.prettyPrint(
    { s: S[I] =>
      iterace.lockSetMapping.getLoopFor(s.n) match {
        case Some(_) => iterace.lockSetMapping.getLockSet(s).map("         " + _.prettyPrint).reduceOption(_ + "\n" + _).map("\n" + _).getOrElse("")
        case None => ""
      }
    }) + "\n"

  def expect(entryClass: String, entryMethod: String, expectedResult: String) = {
    val analysisE = analysis(config(localConfig(entryClass, entryMethod)))
    println(analysisE.pa.cg)
    println("entrypoint:" + analysisE.pa.cg.getEntrypointNodes())
    val result = printRaces(analysisE)
    println(result)
    assertEquals(expectedResult, result)
  }
  
  def expect(entrySig: String, expectedResult: String) = {
    val analysisE = analysis(config(localConfigWithSig(entrySig)))
    println(analysisE.pa.cg.getEntrypointNodes() mkString "\n")
    import analysisE._
    
//    println(pa.heap.collect({case p: P if p.toString() contains "pubkeydb" => (pa.heap.getSuccNodes(p) flatMap (pa.heap.getPredNodes(_))) mkString "\n"}) mkString "\n-----------\n")
    
//    println(analysisE.pa.cg.find(n => n.toString() contains "readKeyFromFile") map {_.ir} mkString "\n")
//    println(analysisE.pa.cg.getEntrypointNodes().head.getMethod().getSignature())
    assertEquals(expectedResult, printRaces(analysisE))
  }
    
  def expect(expectedResult: String): Unit = expect(entryClass, entryMethod, expectedResult)
  def expectNoRaces: Unit = expectNoRaces(testName.getMethodName() + "()V")
  def expectNoRaces(entry: String) = expect("\n\n")
  def expectSomeRaces = assertNotSame("\n\n", printRaces(analysis(config(localConfig(entryClass, entryMethod)))))
}