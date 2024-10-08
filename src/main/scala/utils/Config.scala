package utils

import game.GamePanel

import java.io.{BufferedReader, BufferedWriter, File, FileWriter, InputStreamReader}

class Config (gp : GamePanel) :

  def saveConfig (): Unit =
    try
      val bw : BufferedWriter = new BufferedWriter(new FileWriter("/Users/batman/Desktop/Adventure to Greece/src/main/resources/config.txt"))

      bw.write(String.valueOf(gp.sound.volumeScale))
      bw.newLine()
      bw.write(String.valueOf(gp.se.volumeScale))
      bw.newLine()
      bw.close()
    catch
      case e : Exception => e.printStackTrace()

  def loadConfig(): Unit =
    try
      val loadPath = new File("/Users/batman/Desktop/Adventure to Greece/src/main/resources/config.txt").toURI.toURL.openStream()
      val br = BufferedReader(new InputStreamReader(loadPath))

      var s: String = br.readLine()
      gp.sound.volumeScale = s.toInt

      s = br.readLine()
      gp.se.volumeScale = s.toInt

      br.close()

    catch
      case e : Exception => e.printStackTrace()