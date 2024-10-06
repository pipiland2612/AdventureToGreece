package utils

import java.io.File
import javax.sound.sampled.{AudioInputStream, AudioSystem, Clip, FloatControl}

class Sound :
  var clip: Clip = _
  var fc: FloatControl = _
  var volumeScale: Int = 3
  var volume: Float = _
  var soundUrl: Array[File] = new Array[File](30)
  soundUrl(0) = new File("/Users/batman/Desktop/Adventure to Greece/src/main/resources/sounds/adventure.wav")
  soundUrl(1) = new File("/Users/batman/Desktop/Adventure to Greece/src/main/resources/sounds/boss_fight.wav")

  def setFile (int : Int): Unit =
    try
      val ais: AudioInputStream = AudioSystem.getAudioInputStream(soundUrl(int))
      clip = AudioSystem.getClip()
      clip.open(ais)
      fc = clip.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl]
      checkVolume()
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

  def checkVolume (): Unit =
    volume = volumeScale match
      case 0 => -80f
      case 1 => -20f
      case 2 => -12f
      case 3 => -5f
      case 4 => 1f
      case 5 => 6f
    fc.setValue(volume)
end Sound