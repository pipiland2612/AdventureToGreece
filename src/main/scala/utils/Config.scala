package utils

import game.GamePanel
import java.io.{BufferedReader, BufferedWriter, File, FileWriter, InputStreamReader}

class Config(gp: GamePanel):

  private val configFileName = "config.txt"
  private val configFile = new File(s"${System.getProperty("user.dir")}/src/main/resources/$configFileName")

  def saveConfig(): Unit =
    try
      val bw = new BufferedWriter(new FileWriter(configFile))
      bw.write(String.valueOf(gp.sound.volumeScale))
      bw.newLine()
      bw.write(String.valueOf(gp.se.volumeScale))
      bw.newLine()
      bw.close()
    catch
      case e: Exception => e.printStackTrace()

  def loadConfig(): Unit =
    try
      val br = new BufferedReader(new InputStreamReader(configFile.toURI.toURL.openStream()))
      var s = br.readLine()
      gp.sound.volumeScale = s.toInt
      s = br.readLine()
      gp.se.volumeScale = s.toInt
      br.close()
    catch
      case e: Exception => e.printStackTrace()
