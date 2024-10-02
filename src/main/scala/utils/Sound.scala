package utils

import java.io.File
import javax.sound.sampled.{AudioInputStream, AudioSystem, Clip}

class Sound :
  var clip: Clip = _
  var soundUrl: Array[File] = new Array[File](30)
  soundUrl(0) = new File("/Users/batman/Desktop/Adventure to Greece/src/main/resources/sounds/adventure.wav")
  soundUrl(1) = new File("/Users/batman/Desktop/Adventure to Greece/src/main/resources/sounds/boss_fight.wav")

  def setFile (int : Int): Unit =
    try
      val ais: AudioInputStream = AudioSystem.getAudioInputStream(soundUrl(int))
      clip = AudioSystem.getClip()
      clip.open(ais)
    catch
      case e: Exception =>
        println("Error loading audio")

  def play(): Unit =
    if(clip != null) then
      clip.start()

  def stop(): Unit = clip.stop()

  def loop(): Unit =
    if (clip != null) then
      clip.loop(Clip.LOOP_CONTINUOUSLY)
end Sound